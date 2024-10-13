package no.hiof.set.g6.db.net.ny;


import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.json.simple.JSONObject;

/**
 * @author Frederik Dahl
 * 13/10/2024
 */


public class G6Handler extends SimpleChannelInboundHandler<JSONObject> {

    private final AppInterface interface_;
    
    G6Handler(AppInterface interface_) { this.interface_ = interface_; }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JSONObject msg) throws Exception {
        final Channel cha = ctx.channel();
        interface_.eventLog().write(LogEntry.debug("Packet received from: " + cha));
        interface_.onPacketReceived(new G6Packet(msg,cha));
    }
    
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        final Channel cha = ctx.channel();
        interface_.eventLog().write(LogEntry.debug("Network Channel registered: " + cha));
        interface_.onChannelRegistered(cha);
        super.channelRegistered(ctx);
    }
    
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        final Channel cha = ctx.channel();
        interface_.eventLog().write(LogEntry.debug("Network Channel unregistered: " + cha));
        interface_.onChannelUnregistered(cha);
        super.channelUnregistered(ctx);
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        final Channel cha = ctx.channel();
        interface_.eventLog().write(LogEntry.debug("Network Channel active: " + cha));
        interface_.onChannelActive(cha);
        super.channelActive(ctx);
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        final Channel cha = ctx.channel();
        interface_.eventLog().write(LogEntry.debug("Network Channel inactive: " + cha));
        interface_.onChannelInactive(cha);
        super.channelInactive(ctx);
    }
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        final Channel cha = ctx.channel();
        interface_.eventLog().write(LogEntry.debug("Network Channel read complete: " + cha));
        interface_.onChannelReadComplete(cha);
        super.channelReadComplete(ctx);
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        final Channel cha = ctx.channel();
        final EventLog log = interface_.eventLog();
        final String c_str = cha.toString();
        log.write(LogEntry.error("Exception caught in network channel pipeline: " + c_str));
        log.write(LogEntry.error(cause.getMessage()));
        interface_.onPipelineException(cha);
        ctx.close().addListener(future -> {
            if (future.isSuccess()) {
                log.write(LogEntry.info("Network channel closed: " + c_str));
            } else log.write(LogEntry.error("Network channel failed to closed: " + c_str));
        }); super.exceptionCaught(ctx, cause);
    }
}
