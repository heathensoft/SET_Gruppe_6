package no.hiof.set.g6.net;

import no.hiof.set.g6.dt.DatatypeArray;
import no.hiof.set.g6.dt.LocalUser;
import no.hiof.set.g6.dt.Lock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * We are testing that the request type are
 * the same before and after json conversion. We are not checking the response content for each.
 */
class DBResponseTest {

    DBResponse response_from;

    @BeforeEach
    void setUp() {
        response_from = new DBResponse();
    }

    @Test
    void testResponseUserAddConversion() throws Exception {
        DBResponse response_to = DBResponse.build_response_user_add(true);
        response_from.fromJson(response_to.toJson());
        assertEquals(response_to.request_type,response_from.request_type);
        assertEquals(RequestType.USER_ADD,response_from.request_type);
    }

    @Test
    void testResponseUserDeleteConversion() throws Exception {
        DBResponse response_to = DBResponse.build_response_user_delete(true);
        response_from.fromJson(response_to.toJson());
        assertEquals(response_to.request_type,response_from.request_type);
        assertEquals(RequestType.USER_DELETE,response_from.request_type);
    }

    @Test
    void testResponseUserListConversion() throws Exception {
        DatatypeArray<LocalUser> list = new DatatypeArray<>(LocalUser.class);
        DBResponse response_to = DBResponse.build_response_user_list(list);
        response_from.fromJson(response_to.toJson());
        assertEquals(response_to.request_type,response_from.request_type);
        assertEquals(RequestType.USER_LIST,response_from.request_type);
    }

    @Test
    void testResponseLockToggleConversion() throws Exception {
        DBResponse response_to = DBResponse.build_response_lock_toggle(true);
        response_from.fromJson(response_to.toJson());
        assertEquals(response_to.request_type,response_from.request_type);
        assertEquals(RequestType.LOCK_TOGGLE,response_from.request_type);
    }

    @Test
    void testResponseLockListConversion() throws Exception {
        DatatypeArray<Lock> list = new DatatypeArray<>(Lock.class);
        DBResponse response_to = DBResponse.build_response_lock_list(list);
        response_from.fromJson(response_to.toJson());
        assertEquals(response_to.request_type,response_from.request_type);
        assertEquals(RequestType.LOCK_LIST,response_from.request_type);
    }

    @Test
    void testResponseAccessDeniedConversion() throws Exception {
        DBResponse response_to = DBResponse.build_response_access_denied("");
        response_from.fromJson(response_to.toJson());
        assertEquals(response_to.request_type,response_from.request_type);
        assertEquals(RequestType.ACCESS_DENIED,response_from.request_type);
    }

}
