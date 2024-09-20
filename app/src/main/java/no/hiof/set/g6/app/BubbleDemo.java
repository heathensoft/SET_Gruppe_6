package no.hiof.set.g6.app;

import io.github.heathensoft.jlib.common.Disposable;
import io.github.heathensoft.jlib.lwjgl.gfx.Framebuffer;
import io.github.heathensoft.jlib.lwjgl.gfx.ShaderProgram;
import io.github.heathensoft.jlib.lwjgl.gfx.Texture;
import io.github.heathensoft.jlib.lwjgl.gfx.TextureFormat;
import io.github.heathensoft.jlib.lwjgl.window.Engine;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author Frederik Dahl
 * 28/03/2024
 */


public class BubbleDemo implements Disposable {

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
