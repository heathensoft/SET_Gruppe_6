package no.hiof.set.g6.db.net.naive;


import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.json.simple.JSONObject;

/**
 * @author Frederik Dahl
 * 04/10/2024
 */


public class JsonObjectHandler extends ChannelInboundHandlerAdapter {
 
    private final RequestHandler requestHandler;
    
    public JsonObjectHandler(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        JSONObject request = (JSONObject) msg;
        System.out.println("Server: Received Request From Client");
        JSONObject response = requestHandler.apply(request);
        if (response == null) {
            System.out.println("Server: No Response Needed");
            ctx.close();
        } else {
            ctx.writeAndFlush(response).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) System.out.println("Server: Response Sent To Client");
                else System.out.println("Server: Failed To Respond To Client");
            });
        }
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
