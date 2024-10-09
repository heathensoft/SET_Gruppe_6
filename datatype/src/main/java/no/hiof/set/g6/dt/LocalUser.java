package no.hiof.set.g6.dt;


/**
 * @author Frederik Dahl
 * 06/10/2024
 */


public class LocalUser implements G6DataType {
    
    public String userName;
    public LocalPermission localPermission;
    private final UserAccount userAccount;
    
    
    public LocalUser() {
        userName = "";
        localPermission = LocalPermission.NONE;
        userAccount = new UserAccount();
    }
    
    public UserAccount getAccount() {
        return userAccount;
    }
}
