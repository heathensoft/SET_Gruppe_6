package no.hiof.set.g6.app.client;


import io.github.heathensoft.jlib.lwjgl.window.*;
import no.hiof.set.g6.db.net.ClientInstance;
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


public class ClientTest extends Application {
    
    public static void main(String[] args) {
        Engine.get().run(new ClientTest(),args);
    }
    
    private static final int SCREEN_WIDTH_PIXELS = 400;
    private static final int SCREEN_HEIGHT_PIXELS = 400;
    private static final int PORT = 8080;
    
    private ClientInstance network;
    private JSONObject request;
    private List<LogEntry> log;
    private List<JSONObject> response_list;
    
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
    
    @SuppressWarnings("unchecked")
    protected void on_start(Resolution resolution) throws Exception {
        response_list = new ArrayList<>(32);
        log = new ArrayList<>(32);
        request = new JSONObject();
        request.put("request","This is a request from a Client Application");
        network = new ClientInstance("localhost",PORT);
        if (!network.createdSuccessfully()) {
            logNetworkConnection();
            Engine.get().exit();
        }
    }
    
    protected void on_update(float delta) {
        
        Keyboard keys = Engine.get().input().keys();
        if(keys.just_pressed(GLFW.GLFW_KEY_S)) {
            try { network.sendMessage(request);
            } catch (Exception e) { Logger.warn(e);}
        } else if (keys.just_pressed(GLFW.GLFW_KEY_ESCAPE)) {
            Engine.get().exit();
            return;
        }
        
        collectIncoming();
        logNetworkConnection();
        
    }
    
    protected void on_render(float frame_time, float alpha) {
    
    }
    
    protected void on_exit() {
        try { network.shutDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected void resolution_request(Resolution resolution) throws Exception {
    
    }
    
    private void collectIncoming() {
        response_list.clear();
        network.collectIncoming(response_list);
        for (JSONObject message : response_list) {
            Logger.info(message);
        }
    }
    
    private void logNetworkConnection() {
        log.clear();
        network.collectLogs(log);
        for (LogEntry entry : log) {
            switch (entry.type) {
                case DEBUG -> Logger.debug(entry.message);
                case INFO -> Logger.info(entry.message);
                case WARN -> Logger.warn(entry.message);
                case ERROR -> Logger.error(entry.message);
            }
        }
    }
}