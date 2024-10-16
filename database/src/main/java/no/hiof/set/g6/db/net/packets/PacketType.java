package no.hiof.set.g6.db.net.packets;


/**
 * @author Frederik Dahl
 * 16/10/2024
 */


public enum PacketType {
    CONNECTION_REQUEST(),
    DATABASE_REQUEST(),
    MESSAGE();
    private static final PacketType[] all;
    static { all = values(); }
    public static PacketType getByOrdinal(int ordinal) {
        if (ordinal < all.length && ordinal > 0) {
            return all[ordinal];
        } return null;
    }
}
