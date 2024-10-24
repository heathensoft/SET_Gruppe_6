package no.hiof.set.g6.net;

import no.hiof.set.g6.ny.DatatypeArray;
import no.hiof.set.g6.ny.LocalUser;
import no.hiof.set.g6.ny.Locks;
import no.hiof.set.g6.ny.UserAccount;

import java.nio.file.Path;
import java.util.Map;

/**
 *
 * Every Object passed in as arguments to these methods are "validated" beforehand.
 * Meaning. The arguments are never null and have all the necessary fields.
 * Validation happens in the ServerRequestHandler (Caller class)
 *
 */

public class DemoDatabase implements HUBDatabase {

    private Path path;
    private Map<String,LocalUser> users_table;
    private Map<String,UserAccount> accounts_table;
    private Map<Integer,Locks> locks_table;


    @Override
    public LocalUser.Role getUserRole(LocalUser user) {
        return null;
    }

    @Override
    public DatatypeArray<UserAccount> searchForAccount(UserAccount account) throws Exception {
        return null;
    }

    @Override
    public DatatypeArray<LocalUser> allStoredLocalUsers() throws Exception {
        return null;
    }

    @Override
    public boolean addLocalUser(LocalUser user) throws Exception {
        return false;
    }

    @Override
    public boolean removeLocalUser(LocalUser user) throws Exception {
        return false;
    }

    @Override
    public boolean editLocalUser(LocalUser user) throws Exception {
        return false;
    }

    @Override
    public DatatypeArray<Locks> allStoredLocks() throws Exception {
        return null;
    }

    @Override
    public boolean editLock(Locks lock) throws Exception {
        return false;
    }

    @Override
    public void load() throws Exception {
        HUBDatabase.super.load();
    }

    @Override
    public void save() throws Exception {
        HUBDatabase.super.save();
    }


    private LocalUser findLocalUser(LocalUser user) {
        UserAccount account = user.getUserAccount();
        return users_table.get(account.email);
    }
}
