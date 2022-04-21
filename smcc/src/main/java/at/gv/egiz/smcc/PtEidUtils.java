/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.gv.egiz.smcc;

import java.nio.charset.Charset;
import java.util.Arrays;

/**
 *
 * @author bnazare
 */
class PtEidUtils {

    private static final Charset CARD_CHARSET = Charset.forName("UTF-8");
    private static final char CARD_PADDING = '\0';

    static String extractString(byte[] data, int offset, int length) {
        byte[] targetData = Arrays.copyOfRange(data, offset, offset + length);
        String rawString = new String(targetData, CARD_CHARSET);
        
//        System.out.println("coiso:" + rawString);
        
        int paddingStart = rawString.indexOf(CARD_PADDING);
        if (paddingStart >= 0) {
            return rawString.substring(0, paddingStart);
//            return rawString.trim().substring(0, paddingStart);
        } else {
            return rawString;
        }
    }
    
    static byte[] extractRange(byte[] data, int offset, int length) {
        return Arrays.copyOfRange(data, offset, offset + length);
    }
}
