package at.gv.egiz.smcc;


import java.util.LinkedHashMap;
import java.util.Map;

public class PtEidCitizenData {

    // EF02 IDENTIFICATION
    // identification attributes offsets
    private static final short ISSUING_ENTITY_OFFSET = 0x0;
    private static final short COUNTRY_OFFSET = 0x28;
    private static final short DOCUMENT_TYPE_OFFSET = 0x78;
    private static final short CARD_NUMBER_OFFSET = 0x9A;
    private static final short CARD_NUMBER_PAN_OFFSET = 0xB6;
    private static final short CARD_VERSION_OFFSET = 0xD6;
    private static final short ISSUING_DATE_OFFSET = 0xE6;
    private static final short LOCAL_OF_REQUEST_OFFSET = 0xFA;
    private static final short EXPIRY_DATE_OFFSET = 0x136;
    private static final short SURNAME_OFFSET = 0x14A;
    private static final short GIVEN_NAME_OFFSET = 0x1C2;
    private static final short SEX_OFFSET = 0x23A;
    private static final short NATIONALITY_OFFSET = 0x23C;
    private static final short DATE_OF_BIRTH_OFFSET = 0x242;
    private static final short HEIGHT_OFFSET = 0x256;
    private static final short DOCUMENT_NUMBER_OFFSET = 0x25E;
    private static final short SURNAME_MOTHER_OFFSET = 0x270;
    private static final short GIVEN_NAME_MOTHER_OFFSET = 0x2E8;
    private static final short SURNAME_FATHER_OFFSET = 0x360;
    private static final short GIVEN_NAME_FATHER_OFFSET = 0x3D8;
    private static final short TAX_NUMBER_OFFSET = 0x450;
    private static final short SOCIAL_SECURITY_NUMBER_OFFSET = 0x462;
    private static final short HEALTH_NUMBER_OFFSET = 0x478;
    private static final short ACCIDENTAL_INDICATIONS_OFFSET = 0x48A;
    private static final short MRZ1_OFFSET = 0x502;
    private static final short MRZ2_OFFSET = 0x520;
    private static final short MRZ3_OFFSET = 0x53E;
    /* Fields length */
    private static final short PTEID_DELIVERY_ENTITY_LEN = 40;
    private static final short PTEID_COUNTRY_LEN = 80;
    private static final short PTEID_DOCUMENT_TYPE_LEN = 34;
    private static final short PTEID_CARDNUMBER_LEN = 28;
    private static final short PTEID_CARDNUMBER_PAN_LEN = 32;
    private static final short PTEID_CARDVERSION_LEN = 16;
    private static final short PTEID_DATE_LEN = 20;
    private static final short PTEID_LOCALE_LEN = 60;
    private static final short PTEID_NAME_LEN = 120;
    private static final short PTEID_SEX_LEN = 2;
    private static final short PTEID_NATIONALITY_LEN = 6;
    private static final short PTEID_HEIGHT_LEN = 8;
    private static final short PTEID_NUMBI_LEN = 16;//intial value 16
    private static final short PTEID_NUMNIF_LEN = 18;
    private static final short PTEID_NUMSS_LEN = 22;
    private static final short PTEID_NUMSNS_LEN = 18;
    private static final short PTEID_INDICATIONEV_LEN = 120;
    private static final short PTEID_MRZ_LEN = 30;
    
    public short version;
    public String deliveryEntity;
    public String country;
    public String documentType;
    public String cardNumber;
    public String cardNumberPAN;
    public String cardVersion;
    public String deliveryDate;
    public String locale;
    public String validityDate;
    public String name;
    public String firstname;
    public String sex;
    public String nationality;
    public String birthDate;
    public String height;
    public String numBI;
    public String nameFather;
    public String firstnameFather;
    public String nameMother;
    public String firstnameMother;
    public String numNIF;
    public String numSS;
    public String numSNS;
    public String notes;
    public String mrz1;
    public String mrz2;
    public String mrz3;

    public static PtEidCitizenData parseId(byte[] buf) {

        PtEidCitizenData id = new PtEidCitizenData();

        id.deliveryEntity = PtEidUtils.extractString(buf, ISSUING_ENTITY_OFFSET, PTEID_DELIVERY_ENTITY_LEN);
        id.country = PtEidUtils.extractString(buf, COUNTRY_OFFSET, PTEID_COUNTRY_LEN);
        id.documentType = PtEidUtils.extractString(buf, DOCUMENT_TYPE_OFFSET, PTEID_DOCUMENT_TYPE_LEN);
        id.cardNumber = PtEidUtils.extractString(buf, CARD_NUMBER_OFFSET, PTEID_CARDNUMBER_LEN);
        id.cardNumberPAN = PtEidUtils.extractString(buf, CARD_NUMBER_PAN_OFFSET, PTEID_CARDNUMBER_PAN_LEN);
        id.cardVersion = PtEidUtils.extractString(buf, CARD_VERSION_OFFSET, PTEID_CARDVERSION_LEN);
        id.deliveryDate = PtEidUtils.extractString(buf, ISSUING_DATE_OFFSET, PTEID_DATE_LEN);
        id.locale = PtEidUtils.extractString(buf, LOCAL_OF_REQUEST_OFFSET, PTEID_LOCALE_LEN);
        id.validityDate = PtEidUtils.extractString(buf, EXPIRY_DATE_OFFSET, PTEID_DATE_LEN);
        id.name = PtEidUtils.extractString(buf, SURNAME_OFFSET, PTEID_NAME_LEN);
        id.firstname = PtEidUtils.extractString(buf, GIVEN_NAME_OFFSET, PTEID_NAME_LEN);
        id.sex = PtEidUtils.extractString(buf, SEX_OFFSET, PTEID_SEX_LEN);
        id.nationality = PtEidUtils.extractString(buf, NATIONALITY_OFFSET, PTEID_NATIONALITY_LEN);
        id.birthDate = PtEidUtils.extractString(buf, DATE_OF_BIRTH_OFFSET, PTEID_DATE_LEN);
        id.height = PtEidUtils.extractString(buf, HEIGHT_OFFSET, PTEID_HEIGHT_LEN);
        id.numBI = PtEidUtils.extractString(buf, DOCUMENT_NUMBER_OFFSET, PTEID_NUMBI_LEN);
        id.nameFather = PtEidUtils.extractString(buf, SURNAME_FATHER_OFFSET, PTEID_NAME_LEN);
        id.firstnameFather = PtEidUtils.extractString(buf, GIVEN_NAME_FATHER_OFFSET, PTEID_NAME_LEN);
        id.nameMother = PtEidUtils.extractString(buf, SURNAME_MOTHER_OFFSET, PTEID_NAME_LEN);
        id.firstnameMother = PtEidUtils.extractString(buf, GIVEN_NAME_MOTHER_OFFSET, PTEID_NAME_LEN);
        id.numNIF = PtEidUtils.extractString(buf, TAX_NUMBER_OFFSET, PTEID_NUMNIF_LEN);
        id.numSS = PtEidUtils.extractString(buf, SOCIAL_SECURITY_NUMBER_OFFSET, PTEID_NUMSS_LEN);
        id.numSNS = PtEidUtils.extractString(buf, HEALTH_NUMBER_OFFSET, PTEID_NUMSNS_LEN);
        id.notes = PtEidUtils.extractString(buf, ACCIDENTAL_INDICATIONS_OFFSET, PTEID_INDICATIONEV_LEN);
        id.mrz1 = PtEidUtils.extractString(buf, MRZ1_OFFSET, PTEID_MRZ_LEN);
        id.mrz2 = PtEidUtils.extractString(buf, MRZ2_OFFSET, PTEID_MRZ_LEN);
        id.mrz3 = PtEidUtils.extractString(buf, MRZ3_OFFSET, PTEID_MRZ_LEN);

        return id;
    }

    @Override
    public String toString() {
        return "PTEID_ID{" + "version=" + version + ", deliveryEntity=" + deliveryEntity + ", country=" + country + ", documentType=" + documentType + ", cardNumber=" + cardNumber + ", cardNumberPAN=" + cardNumberPAN + ", cardVersion=" + cardVersion + ", deliveryDate=" + deliveryDate + ", locale=" + locale + ", validityDate=" + validityDate + ", name=" + name + ", firstname=" + firstname + ", sex=" + sex + ", nationality=" + nationality + ", birthDate=" + birthDate + ", height=" + height + ", numBI=" + numBI + ", nameFather=" + nameFather + ", firstnameFather=" + firstnameFather + ", nameMother=" + nameMother + ", firstnameMother=" + firstnameMother + ", numNIF=" + numNIF + ", numSS=" + numSS + ", numSNS=" + numSNS + ", notes=" + notes + ", mrz1=" + mrz1 + ", mrz2=" + mrz2 + ", mrz3=" + mrz3 + '}';
    }
    
    public Map<String, String> getAllInfo() {
        return new LinkedHashMap<String, String>() {
            {
                put("version", String.valueOf(version));
                put("deliveryEntity", deliveryEntity);
                put("country", country);
                put("documentType", documentType);
                put("cardNumber", cardNumber);
                put("cardNumberPAN", cardNumberPAN);
                put("cardVersion", cardVersion);
                put("deliveryDate", deliveryDate);
                put("locale", locale);
                put("validityDate", validityDate);
                put("name", name);
                put("firstname", firstname);
                put("sex", sex);
                put("nationality", nationality);
                put("birthDate", birthDate);
                put("height", height);
                put("numBI", numBI);
                put("nameFather", nameFather);
                put("firstnameFather", firstnameFather);
                put("nameMother", nameMother);
                put("firstnameMother", firstnameMother);
                put("numNIF", numNIF);
                put("numSS", numSS);
                put("numSNS", numSNS);
                put("notes", notes);
                put("mrz1", mrz1);
                put("mrz2", mrz2);
                put("mrz3", mrz3);
            }
        };
    }
    
    public Map<String, String> getCMPombalInfo() {
        return new LinkedHashMap<String, String>() {
            {
                put("name", name);
                put("firstname", firstname);
                //Filiação
                put("nameFather", nameFather);
                put("firstnameFather", firstnameFather);
                put("nameMother", nameMother);
                put("firstnameMother", firstnameMother);
                
                put("nationality", nationality);
                put("birthDate", birthDate);
                put("sex", sex);
                put("height", height);
                //numeros de identificação
                put("numBI", numBI);//Nota este numero está a incluir o primeiro algarismo após o nºid civil 
                put("numNIF", numNIF);
                put("numSS", numSS);
                put("numSNS", numSNS);
                //não incluído no CE mas penso que 
                //é importante ter a data de validade do cartão
                put("validityDate", validityDate);
            }
        };
    }
    
}