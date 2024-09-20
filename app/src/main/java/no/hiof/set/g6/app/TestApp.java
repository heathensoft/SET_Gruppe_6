package no.hiof.set.g6.app;


import io.github.heathensoft.jlib.common.Disposable;
import io.github.heathensoft.jlib.common.utils.U;
import io.github.heathensoft.jlib.lwjgl.gfx.Framebuffer;
import io.github.heathensoft.jlib.lwjgl.gfx.ShaderProgram;
import io.github.heathensoft.jlib.lwjgl.window.Application;
import io.github.heathensoft.jlib.lwjgl.window.BootConfiguration;
import io.github.heathensoft.jlib.lwjgl.window.Engine;
import io.github.heathensoft.jlib.lwjgl.window.Resolution;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.List;

/**
 * @author Frederik Dahl
 * 20/09/2024
 */


public class TestApp extends Application {
    
    public static void main(String[] args) {
        Engine.get().run(new TestApp(),args);
    }
    
    private Vector4f pos;
    private Vector2f res;
    private BubbleDemo demo;
    private static final int SCREEN_WIDTH_PIXELS = 600;
    private static final int SCREEN_HEIGHT_PIXELS = 800;
    
    
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
        config.window_title = "Application";
    }
    
    protected void on_start(Resolution resolution) throws Exception {
        
        int demo_texture_size = Math.min(resolution.width(), resolution.height());
        demo = new BubbleDemo(demo_texture_size,demo_texture_size);
        
        final float w = resolution.width();
        final float h = resolution.height();
        res = new Vector2f(w,h);
        pos = new Vector4f();
        
        if (h > w) {
            pos.x = 0;
            pos.y = (h / 2f) - (w / 2f);
            pos.z = w;
            pos.w = pos.y + w;
        } else if (w > h) {
            pos.x = (w / 2f) - (h / 2f);
            pos.y = 0;
            pos.z = pos.x + h;
            pos.w = h;
        } else {
            pos.x = 0;
            pos.y = 0;
            pos.z = w;
            pos.w = h;
        }
        
        
    }
    
    protected void on_update(float delta) {
    
    }
    
    protected void on_render(float frame_time, float alpha) {
        demo.renderDemo();
        Framebuffer.bindDefault();
        Framebuffer.viewport();
        Framebuffer.clear();
        ShaderProgram.texturePass(
                demo.texture(),
                res, pos,
                U.popSetVec4(0,0,1,1)
        ); U.pushVec4();
        
    }
    
    protected void on_exit() { Disposable.dispose(demo);}
    
    protected void resolution_request(Resolution resolution) {}
}
