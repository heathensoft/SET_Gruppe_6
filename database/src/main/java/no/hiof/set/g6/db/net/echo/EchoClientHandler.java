package no.hiof.set.g6.db.net.echo;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @author Frederik Dahl
 * 01/10/2024
 */

@ChannelHandler.Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client connected to Server");
        ctx.writeAndFlush(Unpooled.copiedBuffer("Message", CharsetUtil.UTF_8));
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        System.out.println("Client received: " + in.toString(CharsetUtil.UTF_8));
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
    
    
}
