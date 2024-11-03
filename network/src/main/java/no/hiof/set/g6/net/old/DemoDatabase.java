package no.hiof.set.g6.net.old;

import no.hiof.set.g6.dt.old.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * File based, in-memory Database
 *
 * Every Object passed in as arguments to these methods are "validated" beforehand.
 * Meaning. The arguments are never null and have all the necessary fields.
 * Validation happens in the ServerRequestHandler (Caller class)
 *
 */

public class DemoDatabase implements HUBDatabase {

    private final Path directory;
    private final List<UserAccount> accounts_table;
    private final Map<String,LocalUser> users_table;
    private final Map<Integer,Locks> locks_table;

    public static final String FILE_USERS = "Users.json";
    public static final String FILE_ACCOUNTS = "Accounts.json";
    public static final String FILE_LOCKS = "Locks.json";


    public DemoDatabase(String directory) {
        this.directory = Path.of(directory);
        accounts_table = new ArrayList<>();
        users_table = new HashMap<>();
        locks_table = new HashMap<>();
    }

    @Override
    public LocalUser.Role getUserRole(LocalUser user) {
        LocalUser user_in_table = findLocalUser(user);
        return user_in_table == null ? LocalUser.Role.NONE : user_in_table.getRole();
    }

    @Override
    public DatatypeArray<UserAccount> searchForAccount(UserAccount account) throws Exception {
        List<UserAccount> search = findMatchingAccounts(accounts_table,account);
        DatatypeArray<UserAccount> result = new DatatypeArray<>(UserAccount.class,search.size());
        for (UserAccount matching : search) result.add(matching);
        return result;
    }

    @Override
    public DatatypeArray<LocalUser> allStoredLocalUsers() throws Exception {
        DatatypeArray<LocalUser> result = new DatatypeArray<>(LocalUser.class,users_table.size());
        for (var entry : users_table.entrySet()) {
            result.add(entry.getValue());
        } return null;
    }

    @Override
    public boolean addLocalUser(LocalUser user) throws Exception {
        UserAccount account = user.getUserAccount();
        LocalUser existing = users_table.putIfAbsent(account.email,user);
        return existing == null;
    }

    @Override
    public boolean removeLocalUser(LocalUser user) throws Exception {
        UserAccount account = user.getUserAccount();
        return users_table.remove(account.email) != null;
    }

    @Override
    public boolean editLocalUser(LocalUser user) throws Exception {
        LocalUser existing = findLocalUser(user);
        if (existing != null) {
          existing.set(user);
          return true;
        } return false;
    }

    @Override
    public DatatypeArray<Locks> allStoredLocks() throws Exception {
        DatatypeArray<Locks> result = new DatatypeArray<>(Locks.class,locks_table.size());
        for (var entry : locks_table.entrySet()) {
            result.add(entry.getValue());
        } return result;
    }

    @Override
    public boolean editLock(Locks lock) throws Exception {
        Locks existing = findLock(lock);
        if (existing != null) {
            existing.set(lock);
            return true;
        } return false;
    }

    @Override
    public void load() throws Exception {
        Path path; JSONObject jsonObject;

        path = directory.resolve(FILE_ACCOUNTS);
        if (Files.exists(path)) {
            jsonObject = JsonUtils.loadFromFile(path);
            List<UserAccount> account_list = accounts_from_json(jsonObject);
            accounts_table.clear();
            accounts_table.addAll(account_list);
        }

        path = directory.resolve(FILE_USERS);
        if (Files.exists(path)) {
            jsonObject = JsonUtils.loadFromFile(path);
            List<LocalUser> user_list = users_from_json(jsonObject);
            users_table.clear();
            for (LocalUser user : user_list) {
                String email = user.getUserAccount().email;
                users_table.put(email,user);
            }
        }

        path = directory.resolve(FILE_LOCKS);
        if (Files.exists(path)) {
            jsonObject = JsonUtils.loadFromFile(path);
            List<Locks> lock_list = locks_from_json(jsonObject);
            locks_table.clear();
            for (Locks lock : lock_list) {
                locks_table.put(lock.lockId,lock);
            }
        }
    }

    @Override
    public void save() throws Exception {
        Path path; JSONObject jsonObject;

        path = directory.resolve(FILE_ACCOUNTS);
        jsonObject = accounts_to_json();
        JsonUtils.saveToFile(jsonObject,path);

        path = directory.resolve(FILE_USERS);
        jsonObject = users_to_json();
        JsonUtils.saveToFile(jsonObject,path);

        path = directory.resolve(FILE_LOCKS);
        jsonObject = locks_to_json();
        JsonUtils.saveToFile(jsonObject,path);
    }

    private List<UserAccount> accounts_from_json(JSONObject jsonObject) throws Exception {
        if (jsonObject == null) throw new Exception("JSONObject is null");
        Object arrayObject = jsonObject.get(FILE_ACCOUNTS);
        if (arrayObject == null) throw new Exception("Json to array: missing");
        try { List<UserAccount> result = new ArrayList<>();
            JSONArray array = (JSONArray) arrayObject;
            for (Object o : array) {
                JSONObject jo = (JSONObject) o;
                UserAccount account = new UserAccount();
                account.fromJson(jo);
                result.add(account);
            } return result;
        } catch (ClassCastException e) {
            throw new Exception("JSON to array: Invalid format", e);
        }
    }

    private List<LocalUser> users_from_json(JSONObject jsonObject) throws Exception {
        if (jsonObject == null) throw new Exception("JSONObject is null");
        Object arrayObject = jsonObject.get(FILE_USERS);
        if (arrayObject == null) throw new Exception("Json to array: missing");
        try { List<LocalUser> result = new ArrayList<>();
            JSONArray array = (JSONArray) arrayObject;
            for (Object o : array) {
                JSONObject jo = (JSONObject) o;
                LocalUser user = new LocalUser();
                user.fromJson(jo);
                result.add(user);
            } return result;
        } catch (ClassCastException e) {
            throw new Exception("JSON to array: Invalid format", e);
        }
    }

    private List<Locks> locks_from_json(JSONObject jsonObject) throws Exception {
        if (jsonObject == null) throw new Exception("JSONObject is null");
        Object arrayObject = jsonObject.get(FILE_LOCKS);
        if (arrayObject == null) throw new Exception("Json to array: missing");
        try { List<Locks> result = new ArrayList<>();
            JSONArray array = (JSONArray) arrayObject;
            for (Object o : array) {
                JSONObject jo = (JSONObject) o;
                Locks lock = new Locks();
                lock.fromJson(jo);
                result.add(lock);
            } return result;
        } catch (ClassCastException e) {
            throw new Exception("JSON to array: Invalid format", e);
        }
    }

    @SuppressWarnings("unchecked")
    private JSONObject accounts_to_json() {
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        for (UserAccount account : accounts_table) {
            array.add(account.toJson());
        } object.put(FILE_ACCOUNTS,array);
        return object;
    }

    @SuppressWarnings("unchecked")
    private JSONObject users_to_json() {
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        for (var entry : users_table.entrySet()) {
            array.add(entry.getValue().toJson());
        } object.put(FILE_USERS,array);
        return object;
    }

    @SuppressWarnings("unchecked")
    private JSONObject locks_to_json() {
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        for (var entry : locks_table.entrySet()) {
            array.add(entry.getValue().toJson());
        } object.put(FILE_LOCKS,array);
        return object;
    }



    private LocalUser findLocalUser(LocalUser user) {
        UserAccount account = user.getUserAccount();
        return users_table.get(account.email);
    }

    private Locks findLock(Locks lock) {
        return locks_table.get(lock.lockId);
    }


    /**
     * add all accounts from source to result where:
     * account.email.equals(email) ||
     * account.phone.equals(phone) ||
     * firstname.startswith(account.firstname) && lastname.startswith(account.lastname)
     * where account is the account argument
     * @param source the list to search
     * @param account the account to search for
     * @return the resulting list of matching accounts
     */
    public static List<UserAccount> findMatchingAccounts(List<UserAccount> source, UserAccount account) {
        if (JsonUtils.anyObjectIsNull(source,account)) throw new IllegalStateException("missing args");
        List<UserAccount> result = new ArrayList<>();
        if (!account.missingFields()) {
            String phoneNumber = account.phoneNumber.toUpperCase();
            String firstName = account.firstName.toUpperCase();
            String lastName = account.lastName.toUpperCase();
            String email = account.email.toUpperCase();
            for (UserAccount account_in_list : source) {
                if (!account_in_list.missingFields()) {
                    if (account_in_list.phoneNumber.toUpperCase().equals(phoneNumber)) {
                        result.add(account_in_list);
                    } else if (account_in_list.email.toUpperCase().equals(email)) {
                        result.add(account_in_list);
                    } else if (account_in_list.firstName.toUpperCase().startsWith(firstName)) {
                        if (account_in_list.lastName.toUpperCase().startsWith(lastName)) {
                            result.add(account_in_list);
                        }
                    }
                }
            }
        } return result;
    }

    public static void main(String[] args) throws Exception {

        DemoDatabase database = new DemoDatabase("");

        Locks lock1 = new Locks();
        lock1.doorName = "Ytterdør Kjeller";
        lock1.batteryStatus = 90;
        lock1.lockStatus = Locks.LockStatus.UNLOCKED;
        lock1.lockId = 1;

        Locks lock2 = new Locks();
        lock2.doorName = "Ytterdør Oppe";
        lock2.batteryStatus = 85;
        lock2.lockId = 2;

        Locks lock3 = new Locks();
        lock3.doorName = "Garasje Inngang";
        lock3.batteryStatus = 90;
        lock3.mechanicalStatus = Locks.MechanicalStatus.FAULT;
        lock3.lockId = 3;

        database.locks_table.put(lock1.lockId,lock1);
        database.locks_table.put(lock2.lockId,lock2);
        database.locks_table.put(lock3.lockId,lock3);


        database.save();


    }
}
