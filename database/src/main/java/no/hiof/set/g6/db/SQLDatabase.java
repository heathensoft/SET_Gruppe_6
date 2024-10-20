package no.hiof.set.g6.db;

import no.hiof.set.g6.ny.DatatypeArray;
import no.hiof.set.g6.ny.LocalUser;
import no.hiof.set.g6.ny.UserAccount;

public class SQLDatabase implements PrototypeDB {



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
}
