package no.hiof.set.g6.net;

import io.netty.channel.Channel;
import no.hiof.set.g6.net.core.JsonPacket;
import no.hiof.set.g6.net.core.LogEntry;
import no.hiof.set.g6.net.core.ServerInstance;
import no.hiof.set.g6.net.packet.*;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class G6Server {

    private final int port;
    private float disconnect_timer;
    private ServerInstance server_instance;
    private final ServerRequestHandler request_handler;
    private final List<JsonPacket> incoming_packets;
    private final List<JsonPacket> outgoing_packets;
    private final LinkedList<LogEntry> logs;



    public G6Server(int port, PrototypeDB database) throws Exception {
        this.request_handler = new ServerRequestHandler(database);
        this.incoming_packets = new ArrayList<>(64);
        this.outgoing_packets = new ArrayList<>(64);
        this.server_instance = new ServerInstance(port);
        this.logs = new LinkedList<>();
        this.port = port;
        database.load();
    }

    public void attemptRestartConnection() throws Exception {
        try {
            incoming_packets.clear();
            outgoing_packets.clear();
            try {
                server_instance.shutDownAndWait();
            } finally {
                server_instance.eventLog().readAll(logs);
                request_handler.onServerShutDown();
            }
            Thread.sleep(100);
            server_instance = new ServerInstance(port);
            if (server_instance.isConnected()) {
                disconnect_timer = 0.0f;
                request_handler.onServerStart();
            }
        } finally {
            server_instance.eventLog().readAll(logs);
        }

    }

    public void shutDown(boolean wait) throws Exception {
        try {
            incoming_packets.clear();
            outgoing_packets.clear();
            if (wait) server_instance.shutDownAndWait();
            else server_instance.shutDown();
        } finally {
            server_instance.eventLog().readAll(logs);
            request_handler.onServerShutDown();
        }
    }

    public void update(float dt) {
        while (logs.size() > 64) logs.removeFirst();
        if (server_instance.isConnected()) {
            server_instance.collectIncomingPackets(incoming_packets);
            for (JsonPacket packet : incoming_packets) {
                serve(packet);
            } incoming_packets.clear();
            for (JsonPacket packet : outgoing_packets) {
                server_instance.sendPacket(packet);
            } outgoing_packets.clear();
            disconnect_timer = 0.0f;
        } else {
            disconnect_timer += dt;
        }
    }

    /**
     * @return port of server connection
     */
    public int port() { return port; }

    /**
     * @return the number of connected clients atm
     */
    public int numRegisteredChannels() {
        return server_instance.numRegisteredChannels();
    }

    /**
     * @return whether server is connected to port
     */
    public boolean isConnected() {
        return server_instance.isConnected();
    }

    /**
     * @return duration of disconnect
     */
    public float disconnectedDuration() {
        return disconnect_timer;
    }

    /**
     * If not collected, the logs will be discarded.
     * @return server event logs
     */
    public List<LogEntry> getLogs() {
        return logs;
    }

    private void serve(JsonPacket incoming_packet) {
        if (incoming_packet == null || !incoming_packet.assignedToChannel()) {
            // Will not happen. All Server side packets will be assigned
            logs.add(LogEntry.error("Server: incoming packet null OR not assigned to channel"));
            return;
        }

        Channel client_channel = incoming_packet.channel();
        G6Packet.Type packet_type;
        JSONObject packet_content;
        int packet_id;

        {
            G6Packet.Wrapper wrapper;

            try {
                // Try to "unwrap" the packet to get to it's contents
                JSONObject incoming_payload = incoming_packet.get();
                wrapper = G6Packet.unwrap(incoming_payload);
            } catch (Exception e) {
                // Send Invalid Packet Response and return
                returnInvalidPacket(e.getMessage(), client_channel,G6Packet.ID_UNKNOWN);
                return;
            }

            // ATP. All the following is not null
            packet_id = wrapper.packetID();
            packet_type = wrapper.packetType();
            packet_content = wrapper.packetContent();

        }

        switch (packet_type) {

            case DATABASE_REQUEST -> {
                try {
                    RequestPacket request = new RequestPacket();
                    request.fromJson(packet_content);
                    // All database requests should have content, but might not have.
                    if (request.content == null) {
                        RequestPacketADT.Type request_type = request.request_type;
                        // Send Invalid Packet Response and return
                        String message = "DB Request: " + request_type + ", missing content";
                        returnInvalidPacket(message,client_channel,packet_id);
                        return;
                    }
                    // ATP. we have identified a request with a type and user who requested the server response.
                    ResponsePacket response_packet = request_handler.handleRequest(request);
                    // ATP. The response is valid / complete. If it weren't an exception would be thrown
                    // The request handler ensures this is the case
                    queueOutgoingResponse(response_packet,client_channel,packet_id);

                } catch (Exception e) {
                    returnInvalidPacket(e.getMessage(),client_channel,packet_id);
                } return;
            }

            case MESSAGE -> {
                logs.add(LogEntry.warn("Messages not supported for Prototype"));
                return;
            }

            case DATABASE_RESPONSE, INVALID_PACKET -> {
                // Send Invalid Packet Response and return
                String message = "Invalid Packet Type: " + packet_type;
                returnInvalidPacket(message,client_channel,packet_id);
                return;
            } default -> throw new IllegalStateException("Unexpected value: " + packet_type);
        }


    }

    private void queueOutgoingResponse(ResponsePacket response_packet, Channel channel, int packet_id) {
        G6Packet.Type response_type = G6Packet.Type.DATABASE_RESPONSE;
        JSONObject response_content = response_packet.toJson();
        JSONObject payload = G6Packet.wrap(response_content,response_type,packet_id);
        JsonPacket packet = new JsonPacket(payload,channel);
        outgoing_packets.add(packet);
    }

    private void returnInvalidPacket(String message, Channel channel, int packet_id) {
        logs.add(LogEntry.warn(message));
        JSONObject payload = G6Packet.invalidPacket(packet_id,message);
        JsonPacket invalid_packet = new JsonPacket(payload,channel);
        outgoing_packets.add(invalid_packet);
    }


}
