package no.hiof.set.g6.db.net;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.json.simple.JSONObject;

/**
 * @author Frederik Dahl
 * 12/10/2024
 */


public final class ClientHandler extends SimpleChannelInboundHandler<JSONObject> {
    
    
    private final ClientInstance client;
    
    ClientHandler(ClientInstance client) {
        this.client = client;
    }
    
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        client.log(LogEntry.debug("ClientHandler channelRegistered"));
        super.channelRegistered(ctx);
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        client.log(LogEntry.debug("ClientHandler channelActive"));
        super.channelActive(ctx);
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        client.log(LogEntry.debug("ClientHandler channelInactive"));
        super.channelInactive(ctx);
    }
    
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        client.log(LogEntry.debug("ClientHandler channelUnregistered"));
        super.channelUnregistered(ctx);
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JSONObject response) throws Exception {
        client.log(LogEntry.debug("ClientHandler channelRead"));
        client.onMessageReceived(response);
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        client.log(LogEntry.debug("ClientHandler exceptionCaught"));
        client.log(LogEntry.error(cause.getMessage()));
        client.log(LogEntry.info("Closing ChannelHandlerContext"));
        ctx.close().addListener(future -> {
           if (future.isSuccess()) {
               client.log(LogEntry.info("ChannelHandlerContext Closed"));
           }
        });
    }
}
