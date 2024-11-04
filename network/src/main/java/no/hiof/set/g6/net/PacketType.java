package no.hiof.set.g6.net;


public enum PacketType {
    /** Response from Peer if the Content of a Packet is corrupted / incomplete*/
    INVALID_PACKET("Invalid Packet"),
    /** User making a Request for a Server Database Operation*/
    DATABASE_REQUEST("Database Request"),
    /** Server Response to a Database Request*/
    DATABASE_RESPONSE("Database Response"),
    /** Not Currently Supported */
    MESSAGE("Message");
    PacketType(String descriptor) {
        this.descriptor = descriptor;
    }
    public String toString() { return descriptor; }
    public final String descriptor;
    private static final PacketType[] all;
    static { all = values(); }
    public static PacketType getByOrdinal(int ordinal) {
        if (ordinal < all.length && ordinal > 0) {
            return all[ordinal];
        } return null;
    }
}
