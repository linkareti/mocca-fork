package at.gv.egiz.smcc;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import at.gv.egiz.smcc.pteidv2.PtEidV2CitizenData;
import at.gv.egiz.smcc.pteidv2.PtEidV2Pic;

public class PtEidV2Utils {

    private PtEidV2Utils() {
    }

    static LinkedHashMap<String, String> parseCitizenData(byte[] citizenData) throws IOException {
        final PtEidV2CitizenData pteid_id = new PtEidV2CitizenData();
        pteid_id.decode(new ByteArrayInputStream(citizenData));

        final LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        // map.put("version", String.valueOf(version));
        map.put("deliveryEntity", pteid_id.getDeliveryEntity().toString());
        map.put("country", pteid_id.getCountry().toString());
        map.put("documentType", pteid_id.getDocumentType().toString());
        map.put("cardNumber", pteid_id.getCardNumber().toString());
        map.put("cardVersion", pteid_id.getCardVersion().toString());
        map.put("deliveryDate", pteid_id.getDeliveryDate().toString());
        map.put("locale", pteid_id.getLocale().toString());
        map.put("validityDate", pteid_id.getValidityDate().toString());
        map.put("name", pteid_id.getName().toString());
        map.put("firstname", pteid_id.getFirstname().toString());
        map.put("sex", pteid_id.getSex().toString());
        map.put("nationality", pteid_id.getNationality().toString());
        map.put("birthDate", pteid_id.getBirthDate().toString());
        map.put("height", pteid_id.getHeight().toString());
        map.put("numBI", pteid_id.getNumBI().toString());
        map.put("nameFather", pteid_id.getNameFather().toString());
        map.put("firstnameFather", pteid_id.getFirstnameFather().toString());
        map.put("nameMother", pteid_id.getNameMother().toString());
        map.put("firstnameMother", pteid_id.getFirstnameMother().toString());
        map.put("numNIF", pteid_id.getNumNIF().toString());
        map.put("numSS", pteid_id.getNumSS().toString());
        map.put("numSNS", pteid_id.getNumSNS().toString());
        map.put("notes", pteid_id.getNotes().toString());
        // map.put("mrz1", mrz1);
        // map.put("mrz2", mrz2);
        // map.put("mrz3", mrz3);

        return map;
    }

    static Map<String, byte[]> parseCitizenPicture(byte[] citizenPicture) throws IOException {
        final PtEidV2Pic pteid_pic = new PtEidV2Pic();
        pteid_pic.decode(new ByteArrayInputStream(citizenPicture));

        final byte[] biometricData = pteid_pic.getBiometricInformationTemplateGroupTemplate()
                .getFirstBiometricInformationTemplate()
                .getBiometricData()
                .value;

        // 46 = PTEIDNG_FIELD_ID_LEN_FACIALRECHDR + PTEIDNG_FIELD_ID_LEN_FACIALINFO + PTEIDNG_FIELD_ID_LEN_IMAGEINFO
        final byte[] picture = Arrays.copyOfRange(biometricData, 46, biometricData.length);

        final LinkedHashMap<String, byte[]> map = new LinkedHashMap<String, byte[]>();
        map.put("picture", picture);

        return map;
    }

}
