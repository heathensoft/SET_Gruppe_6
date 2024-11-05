package no.hiof.set.g6.app;

import io.github.heathensoft.jlib.lwjgl.window.Engine;
import io.github.heathensoft.jlib.lwjgl.window.Keyboard;
import io.github.heathensoft.jlib.lwjgl.window.Resolution;
import io.netty.channel.Channel;
import no.hiof.set.g6.net.*;
import no.hiof.set.g6.net.core.AppInterface;
import no.hiof.set.g6.net.core.JsonPacket;
import no.hiof.set.g6.net.core.ServerInstance;
import org.json.simple.JSONObject;
import org.lwjgl.glfw.GLFW;
import org.tinylog.Logger;

import java.util.LinkedList;

/**
 * PROOF OF CONCEPT
 * A server program with a database.
 * The program is multithreaded in that the main thread handles the DB,
 * and another group of threads handles incoming / outgoing packages.
 * We won't attempt to reconnect server for this example.
 * If the server disconnects for any reason -> the program shuts down.
 */
public class Server extends G6App {

    private LocalSystemDB database;
    private ServerInstance server_instance;
    private LinkedList<JsonPacket> incoming_packets;
    private LinkedList<JsonPacket> outgoing_packets;

    public static void main(String[] args) { Engine.get().run(new Server(),args); }

    @Override
    protected void on_start(Resolution resolution) throws Exception {
        // INITIALIZE SERVER THREADS, CONNECT TO PORT & LOAD DB
        database = new Database(DB_DIRECTORY);
        server_instance = new ServerInstance(PORT);
        incoming_packets = new LinkedList<>();
        outgoing_packets = new LinkedList<>();
        flushInternalLogsToLogger();
        try { database.load();
        } catch (Exception e) {
            Logger.warn(e,"unable to load DB");
            if (database instanceof Database db) {
                Logger.info("loading default DB...");
                loadDefaultDB(db);
            } else Engine.get().exit();
        }
    }

    @Override
    protected void on_update(float delta) {
        // HANDLE INCOMING & OUTGOING PACKETS
        flushInternalLogsToLogger();
        if (server_instance.isConnected()) {
            processIncomingPackets();
            processOutgoingPackets();
        } else { Logger.warn("server disconnected. program signaled to exit");
            Engine.get().exit();
            return;
        } // HANDLE INPUT EVENTS
        Keyboard keys = Engine.get().input().keys();
        if (keys.just_pressed(GLFW.GLFW_KEY_ESCAPE)) {
            Engine.get().exit();
        }
    }

    @Override
    protected void on_exit() {
        try { // SHUT DOWN NETWORK THREAD GROUPS AND SAVE DB
            server_instance.shutDownAndWait(); // Blocking (seconds)
            flushInternalLogsToLogger();
        } catch (InterruptedException e) {
            Logger.warn(e);
        } try { database.save();
        } catch (Exception e) {
            Logger.warn(e,"unable to save DB");
        }
    }

    @Override
    protected AppInterface appInterface() { return server_instance; }

    private void handleIncomingPacket(JsonPacket incoming_packet) {
        Channel client_channel = incoming_packet.channel();
        PacketWrapper wrapper;
        try { wrapper = PacketWrapper.unwrap(incoming_packet.get());
        } catch (Exception e) {
            queueInvalidPacketResponse(e.getMessage(),client_channel,-1);
            return;
        } int packet_id = wrapper.id();
        PacketType packet_type = wrapper.type();
        JSONObject packet_content = wrapper.content();
        switch (packet_type) {
            case DATABASE_REQUEST -> {
                try { DBRequest request = new DBRequest();
                    request.fromJson(packet_content);
                    Logger.info("handling request: " + request.request_type + ", client: " + request.client_account);
                    DBResponse response = RequestHandler.handleLocalDBRequest(database, request);
                    queueOutgoingResponsePacket(response,client_channel,packet_id);
                } catch (Exception e) { Logger.warn(e,"corrupted packet from " + client_channel);
                    queueInvalidPacketResponse(e.getMessage(),client_channel,packet_id);
                }
            } case DATABASE_RESPONSE, INVALID_PACKET -> {
                String message = "invalid packet Type: " + packet_type;
                queueInvalidPacketResponse(message,client_channel,packet_id);
            } case MESSAGE -> Logger.warn("unsupported packet type: " + packet_type);
            default -> throw new IllegalStateException("unexpected value: " + packet_type);
        }
    }

    private void queueOutgoingResponsePacket(DBResponse response, Channel channel, int packet_id) {
        PacketType response_type = PacketType.DATABASE_RESPONSE;
        JSONObject response_content = response.toJson();
        JSONObject payload = PacketWrapper.wrap(response_content,response_type,packet_id);
        outgoing_packets.add(new JsonPacket(payload,channel));
    }

    private void queueInvalidPacketResponse(String message, Channel channel, int packet_id) {
        JSONObject payload = PacketWrapper.invalidResponse(packet_id,message);
        outgoing_packets.add(new JsonPacket(payload,channel));
    }

    private void processIncomingPackets() {
        server_instance.collectIncomingPackets(incoming_packets);
        while (!incoming_packets.isEmpty()) handleIncomingPacket(incoming_packets.removeFirst());
    }

    private void processOutgoingPackets() {
        while (!outgoing_packets.isEmpty()) server_instance.sendPacket(outgoing_packets.removeFirst());
    }
}
