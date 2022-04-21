package at.gv.egiz.smcc;

import java.util.LinkedHashMap;
import java.util.Map;

import at.gv.egiz.smcc.util.SMCCHelper;
import at.gv.egiz.smcc.util.TLV;
import at.gv.egiz.smcc.util.TLVSequence;

public class BELPICIdData {

    // TLV tags for each field
    private static final short CARD_NUMBER_TAG = 1;
    private static final short CHIP_NUMBER_TAG = 2;
    private static final short CARD_VALIDITY_DATE_BEGIN_TAG = 3;
    private static final short CARD_VALIDITY_DATE_END_TAG = 4;
    private static final short CARD_DELIVERY_MUNICIPALITY_TAG = 5;
    private static final short NATIONAL_NUMBER_TAG = 6;
    private static final short NAME_TAG = 7;
    private static final short FIRST_NAME_TAG = 8;
    private static final short MIDDLE_NAME_TAG = 9;
    private static final short NATIONALITY_TAG = 10;
    private static final short PLACE_OF_BIRTH_TAG = 11;
    private static final short DATE_OF_BIRTH_TAG = 12;
    private static final short GENDER_TAG = 13;
    private static final short NOBLE_CONDITION_TAG = 14;
    private static final short DOCUMENT_TYPE_TAG = 15;
    private static final short SPECIAL_STATUS_TAG = 16;
    private static final short PHOTO_DIGEST_TAG = 17;
    private static final short DUPLICATE_TAG = 18;
    private static final short SPECIAL_ORGANISATION_TAG = 19;
    private static final short MEMBER_OF_FAMILY_TAG = 20;
    private static final short DATE_AND_COUNTRY_OF_PROTECTION_TAG = 21;
   
    public String cardNumber;
    public byte[] chipNumber;
    public String cardValidityDateBegin;
    public String cardValidityDateEnd;
    public String cardDeliveryMunicipality;
    public String nationalNumber;
    public String name;
    public String firstName;
    public String middleName;
    public String nationality;
    public String placeOfBirth;
    public String dateOfBirth;
    public String gender;
    public String nobleCondition;
    public String documentType;
    public String specialStatus;
    public byte[] photoDigest;
    public String duplicate;
    public String specialOrganisation;
    public boolean memberOfFamily;
    public String dateAndCountryOfProtection;

    public static BELPICIdData parseId(byte[] buf) {

        BELPICIdData id = new BELPICIdData();
        
        for (TLV tlv : new TLVSequence(buf)) {
            switch (tlv.getTag()) {
                case CARD_NUMBER_TAG:
                    id.cardNumber = new String(tlv.getValue());
                    break;
                case CHIP_NUMBER_TAG:
                    id.chipNumber = tlv.getValue();
                    break;
                case CARD_VALIDITY_DATE_BEGIN_TAG:
                    id.cardValidityDateBegin = new String(tlv.getValue());
                    break;
                case CARD_VALIDITY_DATE_END_TAG:
                    id.cardValidityDateEnd = new String(tlv.getValue());
                    break;
                case CARD_DELIVERY_MUNICIPALITY_TAG:
                    id.cardDeliveryMunicipality = new String(tlv.getValue());
                    break;
            
                case NATIONAL_NUMBER_TAG:
                    id.nationalNumber = new String(tlv.getValue());
                    break;
                case NAME_TAG:
                    id.name = new String(tlv.getValue());
                    break;
                case FIRST_NAME_TAG:
                    id.firstName = new String(tlv.getValue());
                    break;
                case MIDDLE_NAME_TAG:
                    id.middleName = new String(tlv.getValue());
                    break;
                case NATIONALITY_TAG:
                    id.nationality = new String(tlv.getValue());
                    break;
            
                case PLACE_OF_BIRTH_TAG:
                    id.placeOfBirth = new String(tlv.getValue());
                    break;
                case DATE_OF_BIRTH_TAG:
                    id.dateOfBirth = new String(tlv.getValue());
                    break;
                case GENDER_TAG:
                    id.gender = new String(tlv.getValue());
                    break;
                case NOBLE_CONDITION_TAG:
                    id.nobleCondition = new String(tlv.getValue());
                    break;
                case DOCUMENT_TYPE_TAG:
                    id.documentType = new String(tlv.getValue());
                    break;
            
                case SPECIAL_STATUS_TAG:
                    id.specialStatus = new String(tlv.getValue());
                    break;
                case PHOTO_DIGEST_TAG:
                    
                    id.photoDigest = tlv.getValue();
                    break;
                case DUPLICATE_TAG:
                    id.duplicate = new String(tlv.getValue());
                    break;
                case SPECIAL_ORGANISATION_TAG:
                    id.specialOrganisation = new String(tlv.getValue());
                    break;
                case MEMBER_OF_FAMILY_TAG:
                    id.memberOfFamily = true;
                    break;
                
                case DATE_AND_COUNTRY_OF_PROTECTION_TAG:
                    id.dateAndCountryOfProtection = new String(tlv.getValue());
                    break;
            }
        }

        return id;
    }

    @Override
    public String toString() {
        return "BELPICIdData{" + "cardNumber=" + cardNumber + ", chipNumber=" + chipNumber + ", cardValidityDateBegin=" + cardValidityDateBegin + ", cardValidityDateEnd=" + cardValidityDateEnd + ", cardDeliveryMunicipality=" + cardDeliveryMunicipality + ", nationalNumber=" + nationalNumber + ", name=" + name + ", firstName=" + firstName + ", middleName=" + middleName + ", nationality=" + nationality + ", placeOfBirth=" + placeOfBirth + ", dateOfBirth=" + dateOfBirth + ", gender=" + gender + ", nobleCondition=" + nobleCondition + ", documentType=" + documentType + ", specialStatus=" + specialStatus + ", photoDigest=" + photoDigest + ", duplicate=" + duplicate + ", specialOrganisation=" + specialOrganisation + ", memberOfFamily=" + memberOfFamily + ", dateAndCountryOfProtection=" + dateAndCountryOfProtection + '}';
    }
    
    public Map<String, String> getAllInfo() {
        Map<String, String> info = new LinkedHashMap<String, String>();
        
        putIfNotNull(info, "cardNumber", cardNumber);
        putIfNotNull(info, "chipNumber", SMCCHelper.toString(chipNumber));
        putIfNotNull(info, "cardValidityDateBegin", cardValidityDateBegin);
        putIfNotNull(info, "cardValidityDateEnd", cardValidityDateEnd);
        putIfNotNull(info, "cardDeliveryMunicipality", cardDeliveryMunicipality);
        putIfNotNull(info, "nationalNumber", nationalNumber);
        putIfNotNull(info, "name", name);
        putIfNotNull(info, "firstName", firstName);
        putIfNotNull(info, "middleName", middleName);
        putIfNotNull(info, "nationality", nationality);
        putIfNotNull(info, "placeOfBirth", placeOfBirth);
        putIfNotNull(info, "dateOfBirth", dateOfBirth);
        putIfNotNull(info, "gender", gender);
        putIfNotNull(info, "nobleCondition", nobleCondition);
        putIfNotNull(info, "documentType", documentType);
        putIfNotNull(info, "specialStatus", specialStatus);
        putIfNotNull(info, "photoDigest", SMCCHelper.toString(photoDigest));
        putIfNotNull(info, "duplicate", duplicate);
        putIfNotNull(info, "specialOrganisation", specialOrganisation);
        putIfNotNull(info, "memberOfFamily", Boolean.toString(memberOfFamily));
        putIfNotNull(info, "dateAndCountryOfProtection", dateAndCountryOfProtection);
        
        return info;
    }
    
    private void putIfNotNull(Map<String, String> map, String key, String data) {
        if (data != null) {
            map.put(key, data);
        }
    }
    
}