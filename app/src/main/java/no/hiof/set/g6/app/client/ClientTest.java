package no.hiof.set.g6.app.client;


import io.github.heathensoft.jlib.common.text.Ascii;
import io.github.heathensoft.jlib.lwjgl.window.*;
import io.netty.channel.ChannelFuture;
import no.hiof.set.g6.db.net.ClientInstance;
import no.hiof.set.g6.db.net.G6Packet;
import no.hiof.set.g6.db.net.LogEntry;
import org.json.simple.JSONObject;
import org.lwjgl.glfw.GLFW;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Frederik Dahl
 * 12/10/2024
 */


public class ClientTest extends Application implements TextProcessor {
    
    public static void main(String[] args) {
        Engine.get().run(new ClientTest(),args);
    }
    
    private static final String MESSAGE_KEY = "MESSAGE";
    private static final int SCREEN_WIDTH_PIXELS = 400;
    private static final int SCREEN_HEIGHT_PIXELS = 400;
    private static final int PORT = 8080;
    
    private ClientInstance client;
    private List<LogEntry> logs;
    private List<G6Packet> incoming;
    private Ascii.Buffer text;
    
    protected void engine_init(List<Resolution> supported, BootConfiguration config, String[] args) {
        supported.add(new Resolution(SCREEN_WIDTH_PIXELS,SCREEN_HEIGHT_PIXELS));
        config.settings_width = SCREEN_WIDTH_PIXELS;
        config.settings_height = SCREEN_HEIGHT_PIXELS;
        config.auto_resolution = false;
        config.windowed_mode = true;
        config.resizable_window = false;
        config.vsync_enabled = true;
        config.antialiasing = false;
        config.limit_fps = false;
        config.target_ups = 60;
        config.window_title = "Client Application";
    }
    
   
    protected void on_start(Resolution resolution) throws Exception {
        Engine.get().input().keys().setTextProcessor(this);
        
        incoming = new ArrayList<>(32);
        logs = new ArrayList<>(32);
        text = new Ascii.Buffer(256);
        client = new ClientInstance();
        
        ChannelFuture connect = client.connectToHost("localhost",PORT);
        logNetworkConnection(LogEntry.Type.INFO);
        if (!connect.isSuccess()) {
            client.shutDownAndWait();
            logNetworkConnection(LogEntry.Type.DEBUG);
            Engine.get().exit();
        }
    }
    
    protected void on_update(float delta) {
        Keyboard keys = Engine.get().input().keys();
        if (keys.just_pressed(GLFW.GLFW_KEY_ESCAPE)) {
            Engine.get().exit();
        } collectIncoming();
        logNetworkConnection(LogEntry.Type.INFO);
    }
    
    protected void on_render(float frame_time, float alpha) { }
    protected void on_exit() { client.shutDown(); }
    protected void resolution_request(Resolution resolution) throws Exception { }
    
    private void collectIncoming() {
        client.collectIncomingPackets(incoming);
        for (G6Packet packet : incoming) {
            JSONObject payload = packet.get();
            if (payload != null) {
                Object str = payload.get(MESSAGE_KEY);
                if (str instanceof String) {
                    Logger.info(str);
                }
            }
        } incoming.clear();
    }
    
    private void logNetworkConnection(LogEntry.Type filter) {
        client.eventLog().read(logs);
        for (LogEntry entry : logs) {
            if (entry.severity() >= filter.ordinal())
                switch (entry.type) {
                case DEBUG -> Logger.debug(entry.message);
                case INFO -> Logger.info(entry.message);
                case WARN -> Logger.warn(entry.message);
                case ERROR -> Logger.error(entry.message);
            }
        } logs.clear();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void keyPress(int key, int mods, boolean repeat) {
        if (key == GLFW.GLFW_KEY_ENTER &! repeat) {
            if (!text.isEmpty() && client.isConnected()) {
                String string = text.toString();
                JSONObject request = new JSONObject();
                request.put(MESSAGE_KEY,string);
                client.sendPacket(new G6Packet(request));
                text.clear();
            }
        }
    }
    
    @Override
    public void charPress(byte character) {
        text.append(character);
    }
}
