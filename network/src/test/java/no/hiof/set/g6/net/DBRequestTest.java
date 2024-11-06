package no.hiof.set.g6.net;

import no.hiof.set.g6.dt.LocalUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * We are testing that the client account id and the request type are
 * the same before and after json conversion. We are not checking the request content for each.
 */
public class DBRequestTest {

    DBRequest request_from;
    final int client_account_id = 12345;

    @BeforeEach
    void setUp() {
        request_from = new DBRequest();
    }

    @Test
    void testRequestUserAddJsonConversion() throws Exception {
        DBRequest request_to = DBRequest.build_request_user_add(client_account_id,new LocalUser());
        request_from.fromJson(request_to.toJson());
        assertEquals(request_to.request_type,request_from.request_type);
        assertEquals(RequestType.USER_ADD,request_from.request_type);
        assertEquals(request_to.client_account, request_from.client_account);
    }

    @Test
    void testRequestUserDeleteJsonConversion() throws Exception {
        DBRequest request_to = DBRequest.build_request_user_delete(client_account_id,0);
        request_from.fromJson(request_to.toJson());
        assertEquals(request_to.request_type,request_from.request_type);
        assertEquals(RequestType.USER_DELETE,request_from.request_type);
        assertEquals(request_to.client_account, request_from.client_account);
    }

    @Test
    void testRequestUserListJsonConversion() throws Exception {
        DBRequest request_to = DBRequest.build_request_user_list(client_account_id);
        request_from.fromJson(request_to.toJson());
        assertEquals(request_to.request_type,request_from.request_type);
        assertEquals(RequestType.USER_LIST,request_from.request_type);
        assertEquals(request_to.client_account, request_from.client_account);
    }

    @Test
    void testRequestLockToggleJsonConversion() throws Exception {
        DBRequest request_to = DBRequest.build_request_lock_toggle(client_account_id,1,true);
        request_from.fromJson(request_to.toJson());
        assertEquals(request_to.request_type,request_from.request_type);
        assertEquals(RequestType.LOCK_TOGGLE,request_from.request_type);
        assertEquals(request_to.client_account, request_from.client_account);
    }

    @Test
    void testRequestLockListJsonConversion() throws Exception {
        DBRequest request_to = DBRequest.build_request_lock_list(client_account_id);
        request_from.fromJson(request_to.toJson());
        assertEquals(request_to.request_type,request_from.request_type);
        assertEquals(RequestType.LOCK_LIST,request_from.request_type);
        assertEquals(request_to.client_account, request_from.client_account);
    }


}
