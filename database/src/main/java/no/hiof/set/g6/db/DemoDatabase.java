package no.hiof.set.g6.db;

import no.hiof.set.g6.ny.DatatypeArray;
import no.hiof.set.g6.ny.LocalUser;
import no.hiof.set.g6.ny.Locks;
import no.hiof.set.g6.ny.UserAccount;

import java.nio.file.Path;

public class DemoDatabase implements PrototypeDB {

    private Path path;

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
        PrototypeDB.super.load();
    }

    @Override
    public void save() throws Exception {
        PrototypeDB.super.save();
    }
}
