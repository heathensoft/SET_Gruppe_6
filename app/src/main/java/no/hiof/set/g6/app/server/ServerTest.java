package no.hiof.set.g6.app.server;


import io.github.heathensoft.jlib.lwjgl.window.*;
import no.hiof.set.g6.net.core.JsonPacket;
import no.hiof.set.g6.net.core.LogEntry;
import no.hiof.set.g6.net.core.ServerInstance;
import org.json.simple.JSONObject;
import org.lwjgl.glfw.GLFW;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Frederik Dahl
 * 12/10/2024
 */


public class ServerTest extends Application {
    
    public static void main(String[] args) { Engine.get().run(new ServerTest(),args); }
    
    private static final String MESSAGE_KEY = "MESSAGE";
    private static final int SCREEN_WIDTH_PIXELS = 400;
    private static final int SCREEN_HEIGHT_PIXELS = 400;
    private static final int PORT = 8080;
    
    private ServerInstance server;
    private List<LogEntry> logs;
    private List<JsonPacket> incoming;
    private Map<String,String> database;
    
    protected void engine_init(List<Resolution> supported, BootConfiguration config, String[] args) {
        supported.add(new Resolution(SCREEN_WIDTH_PIXELS,SCREEN_HEIGHT_PIXELS));
        config.settings_width = SCREEN_WIDTH_PIXELS;
        config.settings_height = SCREEN_HEIGHT_PIXELS;
        config.auto_resolution = false;
        config.windowed_mode = true;
        config.resizable_window = false;
        config.vsync_enabled = true;
        config.limit_fps = false;
        config.target_ups = 60;
        config.window_title = "Server Application";
    }
    
    protected void on_start(Resolution resolution) throws Exception {
        incoming = new ArrayList<>(32);
        logs = new ArrayList<>(32);
        server = new ServerInstance(PORT);
        database = new HashMap<>(128);
        loadDatabase();
    }
    
    protected void on_update(float delta) {
        Keyboard keys = Engine.get().input().keys();
        if (keys.just_pressed(GLFW.GLFW_KEY_ESCAPE)) {
            Engine.get().exit();
        } handleClientRequests();
        logNetworkConnection(LogEntry.Type.INFO);
    }
    
    protected void on_render(float frame_time, float alpha) { }
    protected void on_exit() { server.shutDown(); }
    protected void resolution_request(Resolution resolution) throws Exception {}
    
    @SuppressWarnings("unchecked")
    private void handleClientRequests() {
        server.collectIncomingPackets(incoming);
        for (JsonPacket packet : incoming) {
            JSONObject payload = packet.get();
            if (payload != null) {
                Object str = payload.get(MESSAGE_KEY);
                if (str instanceof String) {
                    Logger.info(str);
                    str = database.get(str);
                    if (str != null) {
                        payload = new JSONObject();
                        payload.put(MESSAGE_KEY, str);
                        server.sendPacket(packet.response(payload));
                    }
                }
            }
        } incoming.clear();
    }
    
    private void logNetworkConnection(LogEntry.Type filter) {
        server.eventLog().read(logs);
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
    
    private void loadDatabase() {
        database.put("Mamma","Mia");
        database.put("Hello","World!");
    }
}
