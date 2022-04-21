package at.gv.egiz.smcc;

import java.util.Collections;
import java.util.Map;

public class PtEidPic {

    // picture attributes offsets
    private static final short CBEFF_OFFSET = 0x5DF;
    private static final short FACIALRECHDR_OFFSET = 0x601;
    private static final short FACIALINFO_OFFSET = 0x60F;
    private static final short IMAGEINFO_OFFSET = 0x623;
    private static final short PICTURE_OFFSET = 0x62F;
    // fotografia termina com FF D9
    private static final byte[] jp2_eoc = new byte[]{(byte) 0xFF, (byte) 0xD9};
    //private static final short PTEID_MAX_PICTURE_LEN = 14128;
    //private static final short PTEID_MAX_PICTURE_LEN_HEADER = 111;
    //private static final short PTEID_MAX_PICTUREH_LEN = (PTEID_MAX_PICTURE_LEN + PTEID_MAX_PICTURE_LEN_HEADER);
    private static final short PTEID_MAX_CBEFF_LEN = 34;
    private static final short PTEID_MAX_FACRECH_LEN = 14;
    private static final short PTEID_MAX_FACINFO_LEN = 20;
    private static final short PTEID_MAX_IMAGEINFO_LEN = 12;
    //private static final short PTEID_MAX_IMAGEHEADER_LEN = (PTEID_MAX_CBEFF_LEN + PTEID_MAX_FACRECH_LEN + PTEID_MAX_FACINFO_LEN + PTEID_MAX_IMAGEINFO_LEN);
    
    public short version;
    public byte[] cbeff;
    public byte[] facialrechdr;
    public byte[] facialinfo;
    public byte[] imageinfo;
    public byte[] picture;

    public static PtEidPic parsePic(byte[] buf) {

        PtEidPic pic = new PtEidPic();
        
        pic.cbeff = PtEidUtils.extractRange(buf, CBEFF_OFFSET, PTEID_MAX_CBEFF_LEN);
        pic.facialrechdr = PtEidUtils.extractRange(buf, FACIALRECHDR_OFFSET, PTEID_MAX_FACRECH_LEN);
        pic.facialinfo = PtEidUtils.extractRange(buf, FACIALINFO_OFFSET, PTEID_MAX_FACINFO_LEN);
        pic.imageinfo = PtEidUtils.extractRange(buf, IMAGEINFO_OFFSET, PTEID_MAX_IMAGEINFO_LEN);

        int k;
        int piclength = 0;
        for (k = buf.length - 1; k > PICTURE_OFFSET; k--) {
            if (buf[k] == jp2_eoc[1] && buf[k - 1] == jp2_eoc[0]) {
                piclength = k - PICTURE_OFFSET + 1;
                break;
            }
        }
        
        if (piclength <= 0) {
            throw new RuntimeException("Terminador da imagem não encontrado");
        }

        pic.picture = PtEidUtils.extractRange(buf, PICTURE_OFFSET, piclength);

        return pic;
    }
    
    public Map<String, byte[]> getPicture() {
        return Collections.singletonMap("picture", picture);
    }
}
