package no.hiof.set.g6.app;

import io.github.heathensoft.jlib.lwjgl.window.Engine;
import io.github.heathensoft.jlib.lwjgl.window.Keyboard;
import io.github.heathensoft.jlib.lwjgl.window.Resolution;
import io.netty.channel.ChannelFuture;
import no.hiof.set.g6.net.core.AppInterface;
import no.hiof.set.g6.net.core.ClientInstance;
import no.hiof.set.g6.net.core.JsonPacket;
import org.lwjgl.glfw.GLFW;
import org.tinylog.Logger;

import java.util.LinkedList;

/**
 * PROOF OF CONCEPT
 * A client program
 * The program is multithreaded in that the main thread handles input,
 * and another group of threads handles incoming / outgoing packages.
 * We won't attempt to reconnect with server for this example.
 * If the client disconnects from the server for any reason -> the program shuts down.
 */
public class Client extends G6App {

    private ClientInstance client_instance;
    private LinkedList<JsonPacket> incoming_packets;

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
    }

    @Override
    protected void on_update(float delta) {
        flushInternalLogsToLogger();
        if (client_instance.isConnected()) {
            processIncomingPackets();
        } else {
            Logger.warn("disconnected from server. program signaled to exit");
            Engine.get().exit();
            return;
        } // HANDLE INPUT EVENTS
        Keyboard keys = Engine.get().input().keys();
        if (keys.just_pressed(GLFW.GLFW_KEY_ESCAPE)) {
            Engine.get().exit();
        } else if (keys.just_pressed(GLFW.GLFW_KEY_1)) {

        } else if (keys.just_pressed(GLFW.GLFW_KEY_2)) {

        } else if (keys.just_pressed(GLFW.GLFW_KEY_3)) {

        } else if (keys.just_pressed(GLFW.GLFW_KEY_4)) {

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

    }

    private void requestAddUser() {

    }
}
