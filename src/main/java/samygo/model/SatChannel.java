package samygo.model;

public class SatChannel extends Channel {

    private static final int I_CHAN_NO = 0;        // displayed Channel Number
    private static final int I_CHAN_VPID = 2;      // video Stream PID (or -1)
    private static final int I_CHAN_MPID = 4;      // Program Clock Recovery PID
    private static final int I_CHAN_VTYPE = 6;     // Virtual Service Type
    private static final int I_CHAN_STYPE = 14;    // Service Type (0x01 = TV; 0x02 = Radio; 0x0c = Data; 0x19 = HD)
    private static final int I_CHAN_SID = 16;      // SVB Service Identifier
    private static final int I_CHAN_TPID = 18;
    private static final int I_CHAN_SAT = 20;
    private static final int I_CHAN_TSID = 24;     // Transport Stream Identifier
    private static final int I_CHAN_ONID = 28;     // original DVB Network ID
    private static final int I_CHAN_NAME = 36;     // big-endian unicode characters
    private static final int I_CHAN_BOUQET = 138;  // ??? always 0
    private static final int I_CHAN_LOCK = 141;    // locked 0=open, 1=locked
    private static final int I_CHAN_FAV79 = 142;   // bit-field: 0x1=Fav1, 0x2=Fav2, 0x4=Fav3, 0x8=Fav4

    private static final int L_CHAN_NAME = 50;     // max length of the name string in unicode (2 byte per character)

    private final byte[] rawData;
    private final int recordLen;

    public int tpid = -1;
    public int sat = -1;

    protected SatChannel(int recordLen) {
        this.recordLen = recordLen;
        this.rawData = new byte[recordLen];
    }

    public int parse(int row, byte[] inData) {
        /* read rawData
         * attention, byte data type is not unsigned, conversion must
         * be applied to negative values */

        int size = inData.length / recordLen;
        if (row > size) return 0;
        for (int i = row; i < size; i++) /* Search next valid line and return the values */ {
            /* empty line, skip to next // RB: skip only if both bytes of chan.num == 0 */
            int offset = i * recordLen;
            if ((inData[offset] | inData[offset + 1]) == (byte) 0)
                continue;

            byte chsum = 0;
            for (int j = 0; j < recordLen; j++) {
                rawData[j] = inData[offset + j];
                chsum += inData[offset + j];
            }
            num = convertEndianess(rawData[I_CHAN_NO], rawData[I_CHAN_NO + 1]);
            vpid = convertEndianess(rawData[I_CHAN_VPID], rawData[I_CHAN_VPID + 1]);
            mpid = convertEndianess(rawData[I_CHAN_MPID], rawData[I_CHAN_MPID + 1]);
            vtype = rawData[I_CHAN_VTYPE];
            stype = rawData[I_CHAN_STYPE];
            sid = convertEndianess(rawData[I_CHAN_SID], rawData[I_CHAN_SID + 1]);
            tpid = convertEndianess(rawData[I_CHAN_TPID], rawData[I_CHAN_TPID + 1]);
            sat = convertEndianess(rawData[I_CHAN_SAT], rawData[I_CHAN_SAT + 1]);
            tsid = convertEndianess(rawData[I_CHAN_TSID], rawData[I_CHAN_TSID + 1]);
            onid = convertEndianess(rawData[I_CHAN_ONID], rawData[I_CHAN_ONID + 1]);
            bouqet = convertEndianess(rawData[I_CHAN_BOUQET], rawData[I_CHAN_BOUQET + 1]);
            lock = rawData[I_CHAN_LOCK];
            fav79 = rawData[I_CHAN_FAV79];

            /*
             * read channel name
             * only reads a byte, has to be rewritten if
             * the channel name is actually unicode utf8
             */
            StringBuilder nameBuilder = new StringBuilder();
            for (int j = 0; j < L_CHAN_NAME; j++) {
                int c = rawData[I_CHAN_NAME + 1 + j * 2];
                if (c == 0x00)
                    break; // 0x00 is the end delimiter
                if (c < 0)
                    c += 256;
                nameBuilder.append((char) c);
            }
            name = nameBuilder.toString();
            return num;
        }
        return 0;
    }

    public byte[] writeData() {
        revertEndianess(rawData, I_CHAN_NO, num);
        revertEndianess(rawData, I_CHAN_VPID, vpid);
        revertEndianess(rawData, I_CHAN_MPID, mpid);
        rawData[I_CHAN_VTYPE] = vtype;
        rawData[I_CHAN_STYPE] = stype;
        revertEndianess(rawData, I_CHAN_SID, sid);
        revertEndianess(rawData, I_CHAN_TPID, tpid);
        revertEndianess(rawData, I_CHAN_SAT, sat);
        revertEndianess(rawData, I_CHAN_TSID, tsid);
        revertEndianess(rawData, I_CHAN_ONID, onid);

        char[] name = this.name.toCharArray();
        int n = 0;
        for (; n < name.length; n++) {
            rawData[I_CHAN_NAME + 1 + 2 * n] = (byte) name[n];
        }
        for (; n < L_CHAN_NAME; n++) {
            rawData[I_CHAN_NAME + 1 + 2 * n] = 0x00;
        }

        revertEndianess(rawData, I_CHAN_BOUQET, bouqet);
        rawData[I_CHAN_LOCK] = lock;
        rawData[I_CHAN_FAV79] = fav79;

        int crc = recordLen - 1;
        rawData[crc] = 0;
        /* calculate checksum */
        for (int i = 0; i < crc; i++) {
            rawData[crc] += rawData[i];
        }
        return rawData;
    }
}
