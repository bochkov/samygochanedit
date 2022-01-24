package samygo.model;

public abstract class AirCableChannel extends Channel {

    public static final byte QAM64 = 0x1; // RB changed from 0x0 to 0x1
    public static final byte QAM256 = 0x2; // RB changed from 0x1 to 0x2
    public static final byte QAM_AUTO = 0x8; // DVB-T

    private static final int I_CHAN_NO = 0;         // displayed Channel Number
    private static final int I_CHAN_VPID = 2;       // video Stream PID (or -1)
    private static final int I_CHAN_MPID = 4;       // Program Clock Recovery PID
    private static final int I_CHAN_SID = 6;        // SVB Service Identifier
    private static final int I_CHAN_STATUS = 8;
    private static final int I_CHAN_QAM = 12;       // modulation type (QAM64 | QAM256 | QAM_Auto)
    private static final int I_CHAN_S_TYPE = 15;    // Service Type (0x01 = TV; 0x02 = Radio; 0x0c = Data; 0x19 = HD)
    private static final int I_CHAN_FAV = 16;       // video Codec: 0=MPEG2, 1=MPEG4
    private static final int I_CHAN_ENC = 24;       // Scrambled service: 0=FTA, 1=CSA
    // private int iChan??? =  25;                  // Frame Rate
    private static final int I_CHAN_SYMB_R = 28;    // Symbol Rate
    private static final int I_CHAN_LOCK = 31;      // locked 0=open, 1=locked
    private static final int I_CHAN_O_NID = 32;     // original DVB Network ID
    private static final int I_CHAN_NID = 34;       // DVB Network ID displayed  ???
    private static final int I_CHAN_BOUQET = 36;    // ??? always 0
    private static final int I_CHAN_PROV_ID = 38;   // Service Provider ID (or -1)
    private static final int I_CHAN_FREQ = 42;      // cable channel
    private static final int I_CHAN_LCN = 44;       // Logical Channel Number or -1 ???
    private static final int I_CHAN_TSID = 48;      // Transport Stream Identifier
    private static final int I_CHAN_NAME = 64;      // big-endian Unicode characters
    private static final int I_CHAN_S_NAME = 164;   // big-endian Unicode characters
    private static final int I_CHAN_V_FMT = 182;    // video format: 5=1080i25, 7=720p50, 12=576i25, 13=576i25w, 20=custom
    private static final int I_CHAN_FAV_79 = 290;   // bit-field: 0x1=Fav1, 0x2=Fav2, 0x4=Fav3, 0x8=Fav4

    private static final int L_CHAN_S_NAME = 9;     // name length
    private static final int L_CHAN_NAME = 100;     // name length

    public byte qam = QAM64;
    public int nid = -1;
    public int freq = -1;
    public int symbr = -1;
    public int lcn = 0;

    private final int recordLen;
    private final byte[] rawData;

    protected AirCableChannel(int recordLen) {
        this.recordLen = recordLen;
        this.rawData = new byte[recordLen];
    }

    /**
     * reads the record number "row" out of "inData"
     * @param row    - number of the channel record
     * @param inData - raw / binary date to parse
     */
    public int parse(int row, byte[] inData) {
        /* read inData into the chan.rawData
         * attention, byte data type is not unsigned, conversion must
         * be applied to negative values */

        int size = inData.length / recordLen;
        if (row > size)
            return 0;
        for (int i = row; i < size; i++) /* Search next valid line and return the values */ {
            /* empty line or inactive channel, skip to next */
            int offset = i * recordLen;

            if (inData[offset] == (byte) 0)
                continue; // RB || (inData[offset+8]&Channel.FLAG_ACTIVE)==0) continue
            // RB looks like iChanStatus must be checked on both bytes!
            // if((inData[offset] == (byte)00 && inData[offset+1] == (byte)00) || (inData[offset+iChanStatus] & Channel.FLAG_ACTIVE)==0) continue

            byte chsum = 0;
            for (int j = 0; j < recordLen; j++) {
                rawData[j] = inData[offset + j];
                chsum += inData[offset + j];
            }
            num = convertEndianess(inData[offset + I_CHAN_NO], inData[offset + I_CHAN_NO + 1]);
            vpid = convertEndianess(inData[offset + I_CHAN_VPID], inData[offset + I_CHAN_VPID + 1]);
            mpid = convertEndianess(inData[offset + I_CHAN_MPID], inData[offset + I_CHAN_MPID + 1]);
            sid = convertEndianess(inData[offset + I_CHAN_SID], inData[offset + I_CHAN_SID + 1]);
            fav = inData[offset + I_CHAN_FAV];
            status = inData[offset + I_CHAN_STATUS];
            stype = inData[offset + I_CHAN_S_TYPE];
            qam = inData[offset + I_CHAN_QAM];
            enc = inData[offset + I_CHAN_ENC];
            freq = convertEndianess(inData[offset + I_CHAN_FREQ], inData[offset + I_CHAN_FREQ + 1]);
            symbr = convertEndianess(inData[offset + I_CHAN_SYMB_R], inData[offset + I_CHAN_SYMB_R + 1]);
            lock = inData[offset + I_CHAN_LOCK];
            onid = convertEndianess(inData[offset + I_CHAN_O_NID], inData[offset + I_CHAN_O_NID + 1]);
            bouqet = convertEndianess(inData[offset + I_CHAN_BOUQET], inData[offset + I_CHAN_BOUQET + 1]);
            nid = convertEndianess(inData[offset + I_CHAN_NID], inData[offset + I_CHAN_NID + 1]);
            lcn = convertEndianess(inData[offset + I_CHAN_LCN], inData[offset + I_CHAN_LCN + 1]);
            tsid = convertEndianess(inData[offset + I_CHAN_TSID], inData[offset + I_CHAN_TSID + 1]);
            fav79 = inData[offset + I_CHAN_FAV_79];

            /* TODO
            if (i == 0) {
                // first line, try to detect channel type
                if (symbr != 0)
                    Main.mapType = TYPE_CABLE;
                else
                    Main.mapType = TYPE_AIR;
            }
            */

            /*
             * read channel name (max. 100 chars)
             * only reads a byte, has to be rewritten if
             * the channel name is actually unicode utf8
             */
            StringBuilder nameBuilder = new StringBuilder();
            for (int j = 0; j < L_CHAN_NAME; j++) {
                int c = inData[offset + I_CHAN_NAME + 1 + j * 2];
                if (c == 0x00)
                    break;
                if (c < 0)
                    c += 256;
                nameBuilder.append((char) c);
            }
            name = nameBuilder.toString();
            return num;
        }
        return 0;
    }

    /**
     * provides the Channel data a binary data for saving into a MapChan file
     */
    public byte[] writeData() {
        revertEndianess(rawData, I_CHAN_NO, num);
        revertEndianess(rawData, I_CHAN_VPID, vpid);
        revertEndianess(rawData, I_CHAN_MPID, mpid);
        revertEndianess(rawData, I_CHAN_SID, sid);
        rawData[I_CHAN_FAV] = fav;
        rawData[I_CHAN_QAM] = qam;
        rawData[I_CHAN_STATUS] = status;
        rawData[I_CHAN_S_TYPE] = stype;
        revertEndianess(rawData, I_CHAN_O_NID, onid);
        revertEndianess(rawData, I_CHAN_NID, nid);
        rawData[I_CHAN_ENC] = enc;
        revertEndianess(rawData, I_CHAN_FREQ, freq);
        revertEndianess(rawData, I_CHAN_LCN, lcn);
        revertEndianess(rawData, I_CHAN_SYMB_R, symbr);
        revertEndianess(rawData, I_CHAN_BOUQET, bouqet);
        revertEndianess(rawData, I_CHAN_TSID, tsid);

        char[] name = this.name.toCharArray();
        int n = 0;
        for (; n < name.length; n++) {
            rawData[I_CHAN_NAME + 1 + 2 * n] = (byte) name[n];
        }
        for (; n < L_CHAN_NAME; n++) {
            rawData[I_CHAN_NAME + 1 + 2 * n] = (byte) 0x0;
        }

        rawData[I_CHAN_LOCK] = lock;
        rawData[I_CHAN_FAV_79] = fav79;

        int crc = recordLen - 1;
        rawData[crc] = 0;
        for (int i = 0; i < crc; i++) {
            rawData[crc] += rawData[i];
        }
        return rawData;
    }

    /**
     * converts the Channel/Frequency ID into a text String
     * @return String filled of Channel/Frequency ID in human-readable form of {K|S|!}{number}
     */
    public String getFreq() {
        if (this.freq < 7)
            return "S" + (this.freq + 4);
        if (this.freq < 15)
            return "K" + (this.freq - 2);
        if (this.freq < 46)
            return "S" + (this.freq - 4);
        if (this.freq < 95)
            return "K" + (this.freq - 25);
        if (this.freq < 194)
            return "!" + this.freq;
        if (this.freq < 196)
            return "S" + (this.freq - 192);
        return "?" + this.freq;
    }

    /**
     * converts a String back to the channel/Frequency ID
     * @param s - ASCII Text containing the channel/Frequency ID
     */
    public void setFreq(String s) {
        String fStr = s.substring(1);
        int f = Integer.parseInt(fStr);
        if (s.startsWith("S") && f < 4)
            this.freq = f + 192;
        else if (s.startsWith("S") && f < 11)
            this.freq = f - 4;
        else if (s.startsWith("S") && f < 42)
            this.freq = f + 4;
        else if (s.startsWith("K") && f < 13)
            this.freq = f + 2;
        else if (s.startsWith("K") && f < 70)
            this.freq = f + 25;
        else if (s.startsWith("!") && f < 194)
            this.freq = f;
        else
            this.freq = Integer.parseInt(s);
    }
}
