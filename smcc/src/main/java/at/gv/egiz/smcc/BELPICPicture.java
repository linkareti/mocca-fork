package at.gv.egiz.smcc;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

public class BELPICPicture {

    public byte[] picture;

    public static BELPICPicture parsePic(byte[] buf) {

        BELPICPicture pic = new BELPICPicture();
        
        pic.picture = Arrays.copyOf(buf, buf.length);

        return pic;
    }
    
    public Map<String, byte[]> getPicture() {
        return Collections.singletonMap("picture", picture);
    }
    
}