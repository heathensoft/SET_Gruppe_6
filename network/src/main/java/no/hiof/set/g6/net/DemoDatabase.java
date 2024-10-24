package no.hiof.set.g6.net;

import no.hiof.set.g6.ny.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
        HUBDatabase.super.load();
    }

    @Override
    public void save() throws Exception {
        HUBDatabase.super.save();
    }

    private void accounts_from_json(JSONObject object) throws Exception {

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

}
