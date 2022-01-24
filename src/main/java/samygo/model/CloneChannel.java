package samygo.model;

public class CloneChannel extends Channel {

    public byte[] rawData = new byte[81];

    public static final byte FLAG_INACTIVE = (byte) 0x20;
    public static final byte FLAG_SCRAMBLED = (byte) 0x04;

    public int nid = -1;
    public int freq = -1;
    // public byte flags	= 0x00;

}
