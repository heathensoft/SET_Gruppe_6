package no.hiof.set.g6.net.core;


import java.util.LinkedList;
import java.util.List;

/**
 * @author Frederik Dahl
 * 13/10/2024
 */


public class EventLog {
    
    public static final int MAX_STORED = 256;
    public static final int DEFAULT_CAP = 64;
    
    private final LinkedList<LogEntry> entries = new LinkedList<>();
    private boolean printDiscarded;
    private int capacity = DEFAULT_CAP;
    private int filter;
    
    
    public void write(LogEntry entry) {
        if (capacity <= 0) {
            if (printDiscarded && filter <= entry.severity()) entry.SysOut();
        } else synchronized (this) {
            while (entries.size() > capacity) {
                LogEntry removed = entries.removeLast();
                if (printDiscarded && filter <= removed.severity()) entry.SysOut();
            } entries.addFirst(entry);
        }
    }
    
    public void flushToConsole() {
        flushToConsole(filter);
    }
    
    public void flushToConsole(LogEntry.Type filter) {
        if (filter == null) flushToConsole();
        else flushToConsole(filter.ordinal());
    }
    
    private synchronized void flushToConsole(int filter) {
        while (!entries.isEmpty()) {
            LogEntry entry = entries.removeLast();
            if (filter <= entry.severity()) {
                entry.SysOut();
            }
        }
    }
    
    public void setFilter(LogEntry.Type type) {
        if (type != null) filter = type.ordinal();
    }
    
    public void setCapacity(int cap) {
        cap = Math.max(Math.min(MAX_STORED,cap),0);
        if (cap != capacity) {
            capacity = cap;
            synchronized (this) {
                while (entries.size() > capacity) {
                    LogEntry removed = entries.removeLast();
                    if (filter <= removed.severity()) {
                        if (printDiscarded) removed.SysOut();
                    }
                }
            }
        }
    }
    
    public synchronized void read(List<LogEntry> dst) {
        while (!entries.isEmpty()) {
            LogEntry entry = entries.removeLast();
            if (filter <= entry.severity()) {
                dst.add(entry);
            } else if (printDiscarded) entry.SysOut();
        }
    }
    
    public synchronized void readAll(List<LogEntry> dst) {
        while (!entries.isEmpty()) dst.add(entries.removeLast());
    }
    
    public void printDiscardedEntries(boolean on) {
        printDiscarded = on;
    }
    
    public synchronized int size() {
        return entries.size();
    }
    
}
