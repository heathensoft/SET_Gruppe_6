package no.hiof.set.g6.app.deprecated;


import io.github.heathensoft.jlib.common.Disposable;
import io.github.heathensoft.jlib.common.utils.U;
import io.github.heathensoft.jlib.lwjgl.gfx.Framebuffer;
import io.github.heathensoft.jlib.lwjgl.gfx.ShaderProgram;
import io.github.heathensoft.jlib.lwjgl.gfx.Texture;
import io.github.heathensoft.jlib.lwjgl.gfx.TextureFormat;
import io.github.heathensoft.jlib.lwjgl.window.Application;
import io.github.heathensoft.jlib.lwjgl.window.BootConfiguration;
import io.github.heathensoft.jlib.lwjgl.window.Engine;
import io.github.heathensoft.jlib.lwjgl.window.Resolution;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_LINEAR;

/**
 * @author Frederik Dahl
 * 20/09/2024
 */


public class DemoApp extends Application {
    
    public static void main(String[] args) {
        Engine.get().run(new DemoApp(),args);
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

    private static final class BubbleDemo implements Disposable {

        private final Framebuffer framebuffer;

        public BubbleDemo(int width, int height) throws Exception {
            this.framebuffer = init_framebuffer(width, height);
        }

        public void renderDemo() {
            float runtime = (float) Engine.get().time().runTimeSeconds();
            Framebuffer.bindDraw(framebuffer);
            Framebuffer.viewport();
            Framebuffer.clear();
            glDisable(GL_BLEND);
            glDisable(GL_DEPTH_TEST);
            float width = framebuffer.width();
            float height = framebuffer.height();
            int program = ShaderProgram.commonPrograms().bubble_demo_program;
            ShaderProgram.bindProgram(program);
            ShaderProgram.setUniform(ShaderProgram.UNIFORM_TIME,runtime);
            ShaderProgram.setUniform(ShaderProgram.UNIFORM_RESOLUTION,width,height);
            ShaderProgram.ShaderPass pass = ShaderProgram.shaderPass();
            pass.draw(0,0,width,height,0,0,1,1);
        }

        private Framebuffer init_framebuffer(int width, int height) throws Exception {
            Framebuffer framebuffer = new Framebuffer(width, height);
            Framebuffer.bind(framebuffer);
            Framebuffer.setClearColor(0,0,0,0);
            Framebuffer.setClearMask(GL_COLOR_BUFFER_BIT);
            Texture texture = Texture.generate2D(width, height);
            texture.bindToActiveSlot();
            texture.allocate(TextureFormat.RGB8_UNSIGNED_NORMALIZED);
            texture.textureFilter(GL_LINEAR,GL_LINEAR);
            texture.textureRepeat();
            Framebuffer.attachColor(texture,0,true);
            Framebuffer.drawBuffer(0);
            Framebuffer.checkStatus();
            return framebuffer;
        }

        public Texture texture() { return framebuffer.texture(0); }

        public void dispose() { Disposable.dispose(framebuffer); }
    }
}
