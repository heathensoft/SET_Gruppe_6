package no.hiof.set.g6.app;

import io.github.heathensoft.jlib.common.utils.Rand;
import io.github.heathensoft.jlib.lwjgl.window.Engine;
import io.github.heathensoft.jlib.lwjgl.window.Keyboard;
import io.github.heathensoft.jlib.lwjgl.window.Resolution;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import no.hiof.set.g6.dt.DatatypeArray;
import no.hiof.set.g6.dt.LocalUser;
import no.hiof.set.g6.dt.Lock;
import no.hiof.set.g6.net.*;
import no.hiof.set.g6.net.core.AppInterface;
import no.hiof.set.g6.net.core.ClientInstance;
import no.hiof.set.g6.net.core.JsonPacket;
import org.json.simple.JSONObject;
import org.lwjgl.glfw.GLFW;
import org.tinylog.Logger;

import java.util.LinkedList;

/**
 * PROOF OF CONCEPT: A client app program using our network api to communicate with a server
 *
 * Since we don't have a GUI it's difficult to show of all the functionality,
 * this example only provides a few.
 *
 * The program is multithreaded in that the main thread handles input,
 * and another group of threads handles incoming / outgoing packages.
 * We won't attempt to reconnect with server for this example.
 * If the client disconnects from the server for any reason -> the program shuts down.
 */
public class Client extends G6App {

    private ClientInstance client_instance;
    private LinkedList<JsonPacket> incoming_packets;
    private DatatypeArray<Lock> locks;
    private DatatypeArray<LocalUser> users;
    private float refresh_time_accumulator;
    private boolean pause_refresh;

    public static void main(String[] args) { Engine.get().run(new Client(),args); }

    @Override
    protected void on_start(Resolution resolution) throws Exception {
        client_instance = new ClientInstance();
        incoming_packets = new LinkedList<>();
        ChannelFuture connect = client_instance.connectToHost(HOST_ADDRESS,PORT);
        if (!connect.isSuccess()) {
            flushInternalLogsToLogger();
            client_instance.shutDownAndWait();
            throw new Exception("unable to connect with server");
        }
        // Send request to server on start
        refreshLocks();
        refreshUsers();
    }

    @Override
    protected void on_update(float delta) {

        // HANDLE INCOMING PACKETS
        flushInternalLogsToLogger();
        if (client_instance.isConnected()) {
            processIncomingPackets();
        } else {
            Logger.warn("disconnected from server. program signaled to exit");
            Engine.get().exit();
            return;
        }

        // HANDLE INPUT EVENTS
        Keyboard keys = Engine.get().input().keys();
        if (keys.just_pressed(GLFW.GLFW_KEY_ESCAPE)) {
            Engine.get().exit();
        } else if (keys.just_pressed(GLFW.GLFW_KEY_1)) {
            // PRINT OUT USERS
            if (users != null) {
                for (LocalUser user : users) {
                    System.out.println(user);
                }
            }
        } else if (keys.just_pressed(GLFW.GLFW_KEY_2)) {
            // PRINT OUT LOCKS
            if (locks != null) {
                for (Lock lock : locks) {
                    System.out.println(lock);
                }
            }
        } else if (keys.just_pressed(GLFW.GLFW_KEY_3)) {
            // TOGGLE A RANDOM LOCK
            if (locks != null && !locks.isEmpty()) {
                int index = Rand.nextInt(locks.size() - 1);
                toggleLock(locks.get(index));
            }
        } else if (keys.just_pressed(GLFW.GLFW_KEY_P)) {
            // PAUSE REFRESH
            pause_refresh =! pause_refresh;
            refresh_time_accumulator = 0.0f;
        }

        // REFRESH LISTS EVERY N SECONDS

        // Since we are printing out the logs, we keep this high
        // to not clutter the output
        if (!pause_refresh) {
            float refresh_interval_seconds = 10;
            refresh_time_accumulator += delta;
            if (refresh_time_accumulator >= refresh_interval_seconds) {
                refresh_time_accumulator -= refresh_interval_seconds;
                refreshLocks();
                refreshUsers();
            }
        }

    }

    @Override
    protected void on_exit() {
        client_instance.shutDown();
        flushInternalLogsToLogger();
    }

    @Override
    protected AppInterface appInterface() {
        return client_instance;
    }

    private void processIncomingPackets() {
        client_instance.collectIncomingPackets(incoming_packets);
        while (!incoming_packets.isEmpty()) handleIncomingPacket(incoming_packets.removeFirst());
    }

    /** Identify the contents of an incoming packet and handle it accordingly */
    private void handleIncomingPacket(JsonPacket incoming_packet) {
        Channel channel = incoming_packet.channel();
        PacketWrapper wrapper;
        try { wrapper = PacketWrapper.unwrap(incoming_packet.get());
        } catch (Exception e) {
            Logger.warn(e,"corrupted packet");
            return;
        } int packet_id = wrapper.id();
        PacketType packet_type = wrapper.type();
        JSONObject packet_content = wrapper.content();
        Logger.debug("handling packet: " + packet_type + ", ID: " + packet_id);
        switch (packet_type) {
            case DATABASE_RESPONSE -> {
                DBResponse response;
                try {
                    response = new DBResponse();
                    response.fromJson(packet_content);
                } catch (Exception e) {
                    Logger.warn(e,"corrupted packet: " + packet_id);
                    return;
                }
                try {
                    handleServerResponse(response);
                } catch (Exception e) {
                    Logger.warn(e);
                }
            } case INVALID_PACKET -> {
                String key = PacketWrapper.JSON_KEY_MESSAGE;
                Object message_obj = packet_content.get(key);
                if (message_obj == null) {
                    Logger.warn("corrupted packet: " + packet_id);
                } else {
                    try {
                        String error_message_from_server = (String) message_obj;
                        Logger.info("server: " + error_message_from_server);
                    } catch (ClassCastException e) {
                        Logger.warn(e,"corrupted packet: " + packet_id);
                    }
                }
            } case DATABASE_REQUEST -> Logger.warn("invalid packet type: " + packet_type);
            case MESSAGE -> Logger.warn("unsupported packet type: " + packet_type);
            default -> throw new IllegalStateException("unexpected value: " + packet_type);
        }
    }

    /** Handle incoming server response */
    private void handleServerResponse(DBResponse response) throws Exception {
        RequestType request_type = response.request_type;
        Logger.debug("handling server response: " + request_type);
        switch (request_type) {
            case USER_ADD -> {
                String key = DBResponse.JSON_KEY_SUCCESS;
                Object success_obj = response.getOrNull(key);
                if (success_obj == null) {
                    throw new Exception("server response missing contents");
                } try { boolean success = (Boolean) success_obj;
                    Logger.info("user added: " + success);
                } catch (ClassCastException e) {
                    throw new Exception("corrupted server response",e);
                }
            }
            case USER_DELETE -> {
                String key = DBResponse.JSON_KEY_SUCCESS;
                Object success_obj = response.getOrNull(key);
                if (success_obj == null) {
                    throw new Exception("server response missing contents");
                } try { boolean success = (Boolean) success_obj;
                    Logger.info("user deleted: " + success);
                } catch (ClassCastException e) {
                    throw new Exception("corrupted server response",e);
                }
            }
            case USER_LIST -> {
                String key = DBResponse.JSON_KEY_USER_LIST;
                JSONObject jsonObject = response.getJsonObjectOrNull(key);
                if (jsonObject == null) {
                    throw new Exception("server response missing contents");
                } DatatypeArray<LocalUser> user_list = new DatatypeArray<>(LocalUser.class);
                user_list.fromJson(jsonObject);
                Logger.debug("list of users was refreshed");
                users = user_list;
                users.sort();
            }
            case LOCK_TOGGLE -> {
                String key = DBResponse.JSON_KEY_SUCCESS;
                Object success_obj = response.getOrNull(key);
                if (success_obj == null) {
                    throw new Exception("server response missing contents");
                } try { boolean success = (Boolean) success_obj;
                    Logger.info("lock toggled: " + success);
                } catch (ClassCastException e) {
                    throw new Exception("corrupted server response",e);
                }
            }
            case LOCK_LIST -> {
                String key = DBResponse.JSON_KEY_LOCK_LIST;
                JSONObject jsonObject = response.getJsonObjectOrNull(key);
                if (jsonObject == null) {
                    throw new Exception("server response missing contents");
                } DatatypeArray<Lock> lock_list = new DatatypeArray<>(Lock.class);
                lock_list.fromJson(jsonObject);
                Logger.debug("list of locks was refreshed");
                locks = lock_list;
                locks.sort();
            }
            case ACCESS_DENIED -> {
                String key = DBResponse.JSON_KEY_ACCESS_DENIED_MESSAGE;
                Object message_obj = response.getOrNull(key);
                if (message_obj == null) {
                    throw new Exception("server response missing contents");
                } try { String message = (String) message_obj;
                    Logger.info("access denied message from server: " + message);
                } catch (ClassCastException e) {
                    throw new Exception("corrupted server response",e);
                }
            } default -> throw new IllegalStateException("Unexpected value: " + request_type);
        }
    }

    /** Send request to server for an updated list of locks */
    private void refreshLocks() {
        DBRequest request = DBRequest.build_request_lock_list(CLIENT_USER.accountID);
        int packet_id = PacketWrapper.obtainID();
        JSONObject payload = PacketWrapper.wrap(
                request.toJson(),
                PacketType.DATABASE_REQUEST,
                packet_id);
        Logger.debug("sending request to server for list of all locks. packet id:  " + packet_id);
        client_instance.sendPacket(new JsonPacket(payload));
    }

    /** Send request to server for an updated list of users */
    private void refreshUsers() {
        DBRequest request = DBRequest.build_request_user_list(CLIENT_USER.accountID);
        int packet_id = PacketWrapper.obtainID();
        JSONObject payload = PacketWrapper.wrap(
                request.toJson(),
                PacketType.DATABASE_REQUEST,
                packet_id);
        Logger.debug("sending request to server for list of all users. packet id: " + packet_id);
        client_instance.sendPacket(new JsonPacket(payload));
    }

    /** Send request to server to toggle the lock */
    private void toggleLock(Lock lock) {
        boolean close = lock.lockStatus != Lock.LockStatus.LOCKED;
        DBRequest request = DBRequest.build_request_lock_toggle(
                CLIENT_USER.accountID, lock.id, close
        ); int packet_id = PacketWrapper.obtainID();
        JSONObject payload = PacketWrapper.wrap(
                request.toJson(),
                PacketType.DATABASE_REQUEST,
                packet_id);
        Logger.debug("sending request to server to toggle lock. packet id: " + packet_id);
        client_instance.sendPacket(new JsonPacket(payload));
    }
}
