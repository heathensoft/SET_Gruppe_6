package no.hiof.set.g6.db.net.test;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Handles a server-side channel.
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter { // (1)
    
    
    
    
    /*
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
        // Discard the received data silently.
        System.out.println("Discarded");
        ((ByteBuf) msg).release(); // (3)
    }
     */
    /*
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = (ByteBuf) msg;
        try {
            while (in.isReadable()) { // (1)
                System.out.print((char) in.readByte());
                System.out.flush();
            }
        } finally {
            ReferenceCountUtil.release(msg); // (2)
        }
    }
    
     */
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ctx.write(msg); // (1)
        ctx.flush(); // (2)
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
