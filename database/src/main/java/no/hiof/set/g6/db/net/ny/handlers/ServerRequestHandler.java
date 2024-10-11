package no.hiof.set.g6.db.net.ny.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.json.simple.JSONObject;

/**
 * Denne klassen tar imot requests og sender tilbake en respons (På server siden)
 * All kode som kommuniserer med Database må skje innenfor den abstrakte metoden "handleRequests"
 * For enkelthetens skyld lar vi Database kommunikasjon skje i samme "Thread" som nettverkskoden.
 * Dette hadde ikke vært gunstig i en reell applikasjon, da database connections "blocker"
 * nettverkskodens "flow". Men det er greit for prosjektet vårt.
 * Så det som skjer her, er at Nettverk socketen venter på at serveren håndterer requests,
 * og fortsetter etter at serveren har håndtert requesten og sender en respons tilbake
 *
 */


public abstract class ServerRequestHandler extends ChannelInboundHandlerAdapter {
    
    
    
    /**
     * Called when the server receives a request from a client.
     * In our case the Json Data contains instructions for a Database operation.
     * To which the servers respond with appropriate data fetched from the Database.
     * Operations like insert, select etc.
     * @param request the client request
     * @return the server response
     * @throws Exception If an Exception is thrown, the channel will close.
     */
    protected abstract JSONObject handleRequest(JSONObject request) throws Exception;
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        JSONObject request = (JSONObject) msg;
        JSONObject response = handleRequest(request);
        ctx.writeAndFlush(response);
    }
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
    
}
