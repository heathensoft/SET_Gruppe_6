package no.hiof.set.g6.db.net;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;



@Deprecated
public class ClientHandlerOld extends SimpleChannelInboundHandler<JSONObject> {
    
    private ChannelHandlerContext ctx;
    private BlockingQueue<Promise<JSONObject>> queue = new ArrayBlockingQueue<>(16);
    
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.ctx = ctx;
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        synchronized (this) {
            Promise<JSONObject> promise;
            while ((promise = queue.poll()) != null) {
                promise.setFailure(new IOException("Connection Lost"));
            } queue = null;
            
        }
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JSONObject msg) {
        synchronized (this) {
            if (queue != null) {
                Promise<JSONObject> promise;
                promise = queue.poll();
                if (promise != null) {
                    promise.setSuccess(msg);
                }
            }
        }
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
    
    /** Are we still connected to the Server*/
    public synchronized boolean connectionOpen() {
        return queue != null;
    }
    
    /**
     * @param request The request to send the server
     * @return The server will respond sometime in the "future".
     * When it does, the Future will contain the response.
     */
    public Future<JSONObject> sendRequest(JSONObject request) {
        if(ctx == null) throw new IllegalStateException("ClientHandler Uninitialized");
        return sendRequest(request, ctx.executor().newPromise());
    }
    
    private Future<JSONObject> sendRequest(JSONObject request, Promise<JSONObject> promise) {
        synchronized(this){
            if(!connectionOpen()) {// Connection closed
                promise.setFailure(new IllegalStateException("Connection Closed"));
            } else if(queue.offer(promise)) {
                // Connection open and request accepted
                ctx.writeAndFlush(request);
            } else { // Connection open and request rejected
                promise.setFailure(new BufferOverflowException());
            } return promise;
        }
    }
    
    
    
   
    
    
}
