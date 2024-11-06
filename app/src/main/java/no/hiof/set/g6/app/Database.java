package no.hiof.set.g6.app;

import no.hiof.set.g6.dt.*;
import no.hiof.set.g6.net.LocalSystemDB;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * File based Database implementation
 */
public class Database implements LocalSystemDB {

    private static final String FILE_NAME = "hub_db.json";
    private static final String JSON_KEY_LOCKS = "Locks";
    private static final String JSON_KEY_USERS = "Users";
    private static final String JSON_KEY_LOCKS_AUTO_INCREMENT = "Locks Auto-inc";

    private final Path path;
    protected final Map<Integer,Lock> locks_table;
    protected final Map<Integer,LocalUser> users_table;

    /** simulating auto inc */
    protected int lock_auto_increment;

    public Database(String directory) {
        this.path = Path.of(directory).resolve(FILE_NAME);
        users_table = new HashMap<>();
        locks_table = new HashMap<>();
    }

    @Override
    public boolean addUser(LocalUser user) {
        if (user == null) throw new IllegalStateException("null arg. user");
        user.ensureFieldsNotNull();
        LocalUser existing = users_table.putIfAbsent(user.accountID,user);
        return existing == null;
    }

    @Override
    public boolean deleteUser(int account_id) {
        return users_table.remove(account_id) != null;
    }

    @Override
    public LocalUser getUserByAccountID(int account_id) {
        return users_table.get(account_id);
    }

    @Override
    public DatatypeArray<LocalUser> getUsers() {
        DatatypeArray<LocalUser> result = new DatatypeArray<>(LocalUser.class,users_table.size());
        for (var entry : users_table.entrySet()) {
            result.add(entry.getValue());
        } return result;
    }

    @Override
    public boolean addLock(int serial_number, String name) {
        name = name == null ? G6Datatype.NULL_STRING : name;
        boolean serial_number_exist = false;
        for (var entry : locks_table.entrySet()) {
            Lock lock = entry.getValue();
            if (lock.serialNumber == serial_number) {
                serial_number_exist = true;
                break;
            }
        }
        if (!serial_number_exist) {
            Lock lock = new Lock();
            lock.serialNumber = serial_number;
            lock.doorName = name;
            lock.id = lock_auto_increment++;
            locks_table.put(lock.id,lock);
            return true;
        } return false;
    }


    @Override
    public boolean deleteLock(int lock_id) {
        return locks_table.remove(lock_id) != null;
    }

    @Override
    public boolean openLock(int lock_id) {
        Lock lock = locks_table.get(lock_id);
        if (lock != null) {
            lock.lockStatus = Lock.LockStatus.UNLOCKED;
            return true;
        } return false;
    }

    @Override
    public boolean closeLock(int lock_id) {
        Lock lock = locks_table.get(lock_id);
        if (lock != null) {
            lock.lockStatus = Lock.LockStatus.LOCKED;
            return true;
        } return false;
    }

    @Override
    public DatatypeArray<Lock> getLocks() {
        DatatypeArray<Lock> result = new DatatypeArray<>(Lock.class,locks_table.size());
        for (var entry : locks_table.entrySet()) {
            result.add(entry.getValue());
        } return result;
    }

    @Override
    public void load() throws Exception {
        JSONObject db_json = JsonUtils.loadFromFile(path);
        Object users_obj = db_json.get(JSON_KEY_USERS);
        Object locks_obj = db_json.get(JSON_KEY_LOCKS);
        Object a_inc_obj = db_json.get(JSON_KEY_LOCKS_AUTO_INCREMENT);
        if (JsonUtils.anyObjectIsNull(users_obj, locks_obj, a_inc_obj
        )) throw new Exception("Incomplete / corrupted DB");
        try { users_from_json((JSONArray) users_obj);
            locks_from_json((JSONArray) locks_obj);
            lock_auto_increment = ((Long) a_inc_obj).intValue();
        } catch (ClassCastException e) {
            throw new Exception("Invalid format of one or more fields", e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void save() throws Exception {
        JSONObject object = new JSONObject();
        object.put(JSON_KEY_USERS,users_to_json());
        object.put(JSON_KEY_LOCKS,locks_to_json());
        object.put(JSON_KEY_LOCKS_AUTO_INCREMENT, lock_auto_increment);
        JsonUtils.saveToFile(object,path);
    }


    @SuppressWarnings("unchecked")
    private JSONArray users_to_json() {
        JSONArray array = new JSONArray();
        for (var entry : users_table.entrySet()) {
            array.add(entry.getValue().toJson());
        } return array;
    }

    private void users_from_json(JSONArray array) throws Exception {
        if (!array.isEmpty()) {
            users_table.clear();
            for (Object object : array) {
                LocalUser user = new LocalUser();
                user.fromJson((JSONObject) object);
                users_table.put(user.accountID,user);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private JSONArray locks_to_json() {
        JSONArray array = new JSONArray();
        for (var entry : locks_table.entrySet()) {
            array.add(entry.getValue().toJson());
        } return array;
    }

    private void locks_from_json(JSONArray array) throws Exception {
        if (!array.isEmpty()) {
            locks_table.clear();
            for (Object object : array) {
                Lock lock = new Lock();
                lock.fromJson((JSONObject) object);
                locks_table.put(lock.id,lock);
            }
        }
    }

}
