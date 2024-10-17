package no.hiof.set.g6.db.net;


/**
 * @author Frederik Dahl
 * 12/10/2024
 */


public class LogEntry {
    
    public enum Type {
        DEBUG("[DEBUG] "),
        INFO( "[INFO ] "),
        WARN( "[WARN ] "),
        ERROR("[ERROR] ");
        final String prefix;
        Type(String prefix) {
            this.prefix = prefix;
        }
        public String toString() {
            return prefix;
        }
    }
 
    public final Type type;
    public final String message;
    
    public void SysOut() {
        System.out.println(type + message);
    }
    
    public int severity() {
        return type.ordinal();
    }
    
    private LogEntry(Type type, String message) {
        this.message = message == null ? "" : message;
        this.type = type;
    }
    
    public static LogEntry debug(String message) {
        return new LogEntry(Type.DEBUG,message);
    }
    public static LogEntry info(String message) {
        return new LogEntry(Type.INFO,message);
    }
    public static LogEntry warn(String message) {
        return new LogEntry(Type.WARN,message);
    }
    public static LogEntry error(String message) {
        return new LogEntry(Type.ERROR,message);
    }
    
    
}
