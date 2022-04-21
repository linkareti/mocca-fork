package at.gv.egiz.smcc;

import java.util.LinkedHashMap;
import java.util.Map;

import at.gv.egiz.smcc.util.TLV;
import at.gv.egiz.smcc.util.TLVSequence;

public class BELPICAddress {

    // TLV tags for each field
    private static final short STREET_AND_NUMBER_TAG = 1;
    private static final short ZIP_TAG = 2;
    private static final short MUNICIPALITY_TAG = 3;
   
    public String streetAndNumber;
    public String zip;
    public String municipality;

    public static BELPICAddress parseAddr(byte[] buf) {

        BELPICAddress addr = new BELPICAddress();
        
        for (TLV tlv : new TLVSequence(buf)) {
            switch (tlv.getTag()) {
                case STREET_AND_NUMBER_TAG:
                    addr.streetAndNumber = new String(tlv.getValue());
                    break;
                case ZIP_TAG:
                    addr.zip = new String(tlv.getValue());
                    break;
                case MUNICIPALITY_TAG:
                    addr.municipality = new String(tlv.getValue());
                    break;
            }
        }

        return addr;
    }

    @Override
    public String toString() {
        return "BELPICAddress{" + "streetAndNumber=" + streetAndNumber + ", zip=" + zip + ", municipality=" + municipality + '}';
    }
    
    public Map<String, String> getAllInfo() {
        Map<String, String> info = new LinkedHashMap<String, String>();
        
        putIfNotNull(info, "streetAndNumber", streetAndNumber);
        putIfNotNull(info, "zip", zip);
        putIfNotNull(info, "municipality", municipality);
        
        return info;
    }
    
    private void putIfNotNull(Map<String, String> map, String key, String data) {
        if (data != null) {
            map.put(key, data);
        }
    }
    
}