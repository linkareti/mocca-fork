package at.gv.egiz.smcc;

import static at.gv.egiz.smcc.SignatureCard.KeyboxName.SECURE_SIGNATURE_KEYPAIR;

import java.io.ByteArrayInputStream;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.smartcardio.Card;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CardTerminals;

import org.apache.commons.codec.binary.Hex;

import at.gv.egiz.smcc.pin.gui.PINGUI;
import at.gv.egiz.smcc.util.SMCCHelper;
import at.gv.egiz.smcc.util.SmartCardIO;
import sun.security.util.ECUtil;

public class ConnectCard {

    private static final SignatureCardFactory SIG_CARD_FACTORY = SignatureCardFactory.getInstance();

    private ConnectCard() {
    }

    public static void main(String[] args) throws Exception {
        System.out.println(SIG_CARD_FACTORY);
        System.out.println();

        final SMCCHelper ignoredSmccHelper = new SMCCHelper();

        final SmartCardIO io = new SmartCardIO();

        System.out.println("** TERMINALS **");
        final CardTerminals cardTerminals = io.getCardTerminals();
        for (CardTerminal cardTerminal : cardTerminals.list()) {
            System.out.println(cardTerminal);
            System.out.println(cardTerminal.isCardPresent());
        }
        System.out.println();

        System.out.println("** CARDS **");
        final Map<CardTerminal, Card> cards = io.getCards();
        for (Map.Entry<CardTerminal, Card> entry : cards.entrySet()) {
            final CardTerminal cardTerminal = entry.getKey();
            final Card card = entry.getValue();
            System.out.println(cardTerminal);
            System.out.println(card);
            System.out.println(card.getATR());

            for (byte aByte : card.getATR().getBytes()) {
                System.out.print(String.format("%02X", aByte) + ":");
            }
            System.out.println();
            System.out.println();

            final SignatureCard signatureCard = SIG_CARD_FACTORY.createSignatureCard(card, cardTerminal);
            System.out.println(signatureCard);

            final List<byte[]> certificates = signatureCard.getCertificates(SECURE_SIGNATURE_KEYPAIR, null);
            System.out.println(certificates.size() + " certificates");
            for (byte[] certificateBytes : certificates) {
                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                final X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(certificateBytes));
                System.out.println(certificate.getSubjectDN());
            }
            System.out.println();

            final Map<CardDataSet, Map<String, ?>> cardData = signatureCard.getCardData(null, null, CardDataSet.HOLDER_DATA, CardDataSet.HOLDER_PICTURE, CardDataSet.HOLDER_EXTRA);
            final Map<String, ?> holderData = cardData.get(CardDataSet.HOLDER_DATA);
            System.out.println(holderData);
            System.out.println();

            final Map<String, ?> holderPicture = cardData.get(CardDataSet.HOLDER_PICTURE);
            System.out.println(holderPicture);

            final byte[] picture = (byte[]) holderPicture.get("picture");
            // System.out.println(Hex.encodeHexString(picture));
            System.out.println(picture.length + " bytes");

            // FileUtils.writeByteArrayToFile(new File("/home/bnazare/foo.jp2"), picture);
            System.out.println();

            final byte[] dataToSign = new byte[1000];
            final byte[] signature;
            try {
                signature = signatureCard.createSignature(new ByteArrayInputStream(dataToSign),
                        null,
                        new ConsolePINGUI(),
                        "http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha256");
            } catch (CancelledException e) {
                System.out.printf("Signature Cancelled!!!: %s\n", e.getMessage());
                return;
            }

            System.out.println(Hex.encodeHexString(signature));

            System.out.println(signature.length + " bytes");

            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            final X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(signatureCard.getCertificate(null, null)));

            Signature sig = Signature.getInstance("SHA256withECDSA");

            sig.initVerify(certificate.getPublicKey());
            sig.update(dataToSign);
            final byte[] encodedEcdsa = ECUtil.encodeSignature(signature);
            final boolean verify = sig.verify(encodedEcdsa);
            System.out.println(verify);
        }

    }

    static class ConsolePINGUI implements PINGUI {
        @Override
        public void enterPINDirect(PinInfo pinInfo, int retries) {

        }

        @Override
        public void enterPIN(PinInfo pinInfo, int retries) {

        }

        @Override
        public void validKeyPressed() {

        }

        @Override
        public void correctionButtonPressed() {

        }

        @Override
        public void allKeysCleared() {

        }

        @Override
        public char[] providePIN(PinInfo pinSpec, int retries) throws CancelledException {
            System.out.printf("PLease input, %d retries left (3912): %s\n", retries, pinSpec.getLocalizedName());
            final String line = new Scanner(System.in).nextLine();

            if (pinSpec.getState() == PinInfo.STATE.NOT_ACTIV) {
                throw new CancelledException("PIN is not active");
            } else if (pinSpec.getState() == PinInfo.STATE.BLOCKED) {
                throw new CancelledException("PIN is blocked");
            } else if (checkPinFormat(line, pinSpec)) {
                final char[] pin = line.toCharArray();
                System.out.println(Arrays.toString(pin));

                return pin;
            } else {
                throw new CancelledException("Wrong PIN format");
            }
        }

        private boolean checkPinFormat(String pin, PinInfo pinSpec) {
            if (pin.length() >= pinSpec.getMinLength()
                    && (pinSpec.getMaxLength() == -1 || pin.length() <= pinSpec.getMaxLength())) {
                final Pattern pattern = Pattern.compile(pinSpec.getRegexpPattern());

                for (String s : pin.split("")) {
                    if (!pattern.matcher(s).matches()) {
                        return false;
                    }
                }
                return true;
            } else {
                return false;
            }
        }
    }

}
