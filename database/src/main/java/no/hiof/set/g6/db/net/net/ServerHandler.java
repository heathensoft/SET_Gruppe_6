package no.hiof.set.g6.db.net.net;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.json.simple.JSONObject;

/**
 * @author Frederik Dahl
 * 12/10/2024
 */


public final class ServerHandler extends SimpleChannelInboundHandler<JSONObject> {
    
    private final ServerInstance server;
    
    ServerHandler(ServerInstance server) { this.server = server; }
    
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        server.log(LogEntry.debug("ServerHandler channelRegistered"));
        super.channelRegistered(ctx);
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        server.log(LogEntry.debug("ServerHandler channelActive"));
        super.channelActive(ctx);
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        server.log(LogEntry.debug("ServerHandler channelInactive"));
        server.removeChannel(ctx.channel());
        super.channelInactive(ctx);
    }
    
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        server.log(LogEntry.debug("ServerHandler channelUnregistered"));
        super.channelUnregistered(ctx);
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JSONObject request) throws Exception {
        server.log(LogEntry.debug("ServerHandler channelRead"));
        server.onMessageReceived(new ServerPacket(ctx.channel(),request));
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        server.log(LogEntry.debug("ServerHandler exceptionCaught"));
        server.log(LogEntry.error(cause.getMessage()));
        server.log(LogEntry.info("Closing ChannelHandlerContext"));
        ctx.close().addListener(future -> {
            if (future.isSuccess()) {
                // Test this. (Will closing ctx trigger channelInactive?)
                // Possible requestToRemove is called twice
                server.removeChannel(ctx.channel());
                server.log(LogEntry.info("ChannelHandlerContext Closed"));
            }
        });
    }
}
