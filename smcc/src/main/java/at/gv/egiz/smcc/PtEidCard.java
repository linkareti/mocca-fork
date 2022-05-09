/*
 * Copyright 2011 by Graz University of Technology, Austria
 * MOCCA has been developed by the E-Government Innovation Center EGIZ, a joint
 * initiative of the Federal Chancellery Austria and Graz University of Technology.
 *
 * based on an implementation
 *
 * Copyright 2009 Manuel Preliteiro, MULTICERT S.A.
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they will be approved by
 * the European Commission – subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * http://www.osor.eu/eupl/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 *
 * This product combines work with different licenses. See the "NOTICE" text
 * file for details on the various modules and licenses.
 * The "NOTICE" text file is part of the distribution. Any derivative works
 * that you distribute must include a readable copy of the "NOTICE" text file.
 */

package at.gv.egiz.smcc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.gv.egiz.smcc.pin.gui.PINGUI;
import at.gv.egiz.smcc.util.ISO7816Utils;
import at.gv.egiz.smcc.util.SMCCHelper;

public class PtEidCard extends AbstractSignatureCard {

  private final Logger log = LoggerFactory.getLogger(PtEidCard.class);
  
  private static final byte[] AID_APPLET = { (byte) 0x60, (byte) 0x46, (byte) 0x32,
    (byte) 0xff, (byte) 0x00, (byte) 0x00, (byte) 0x02 };

  private static final byte[] DF_ISSUES = {(byte) 0x5F, (byte) 0x00};
  
  private static final byte[] EF_SIGN_CERT = { (byte) 0xEF, (byte) 0x08 };
  
  private static final byte[] EF_SIGN_CA_CERT = { (byte) 0xEF, (byte) 0x0F };
  
  private static final PinInfo QS_PIN_SPEC = 
    new PinInfo(4, 4, "[0-9]",
      "at/gv/egiz/smcc/PtEidCard", "sig.pin", (byte) 0x82, DF_ISSUES, PinInfo.UNKNOWN_RETRIES);
  
  private static final PinInfo ADDR_PIN_SPEC =
          new PinInfo(4, 4, "[0-9]",
                  "at/gv/egiz/smcc/PtEidCard", "addr.pin", (byte) 0x83, DF_ISSUES, PinInfo.UNKNOWN_RETRIES);

  private static final int ID_DATA_LENGHT = 1372;//lenght of all data for id purposes

  @Override
  public byte[] getCertificate(KeyboxName keyboxName, PINGUI provider)
      throws SignatureCardException, InterruptedException {

    try {
      CardChannel channel = getCardChannel();
      // SELECT applet
      execSELECT_AID(channel, AID_APPLET);
      // SELECT DF_ISSUES
      execSELECT_FID(channel, DF_ISSUES);
      // SELECT EF_SIGN_CERT
      byte[] fcx = execSELECT_FID(channel, EF_SIGN_CERT);
      int maxsize = ISO7816Utils.getLengthFromFCx(fcx);
      // READ BINARY
      byte[] certificate = ISO7816Utils.readTransparentFileTLV(channel, maxsize, (byte) 0x30);
      if (certificate == null) {
        throw new NotActivatedException();
      }
      return certificate;
    } catch (FileNotFoundException e) {
      throw new NotActivatedException();
    } catch (CardException e) {
      log.info("Failed to get certificate.", e);
      throw new SignatureCardException(e);
    }

  }
  
  private byte[] getCACertificate(KeyboxName keyboxName, PINGUI provider)
      throws SignatureCardException, InterruptedException {

    try {
      CardChannel channel = getCardChannel();
      // SELECT applet
      execSELECT_AID(channel, AID_APPLET);
      // SELECT DF_ISSUES
      execSELECT_FID(channel, DF_ISSUES);
      // SELECT EF_SIGN_CERT
      byte[] fcx = execSELECT_FID(channel, EF_SIGN_CA_CERT);
      int maxsize = ISO7816Utils.getLengthFromFCx(fcx);
      // READ BINARY
      byte[] certificate = ISO7816Utils.readTransparentFileTLV(channel, maxsize, (byte) 0x30);
      if (certificate == null) {
        throw new NotActivatedException();
      }
      return certificate;
    } catch (FileNotFoundException e) {
      throw new NotActivatedException();
    } catch (CardException e) {
      log.info("Failed to get certificate.", e);
      throw new SignatureCardException(e);
    }

  }

  @Override
  public List<byte[]> getCertificates(KeyboxName keyboxName, PINGUI pinGUI) throws SignatureCardException, InterruptedException {
    byte[][] certs = new byte[][] { getCertificate(keyboxName, pinGUI), getCACertificate(keyboxName, pinGUI) };

    return Collections.unmodifiableList(Arrays.asList(certs));
  }
  
  @Override
  public Map<CardDataSet, Map<String, ?>> getCardData(
          KeyboxName keyboxName, PINGUI pinGUI, CardDataSet... datasets)
          throws SignatureCardException, InterruptedException {

    Map<CardDataSet, Map<String, ?>> result = new EnumMap<CardDataSet, Map<String, ?>>(CardDataSet.class);

    for (CardDataSet dataset : datasets) {
      switch(dataset) {
        case HOLDER_DATA:
          byte[] citizenData;
          citizenData = getCitizenData(keyboxName, pinGUI, ID_DATA_LENGHT);
          PtEidCitizenData pteid_id = PtEidCitizenData.parseId(citizenData);
          result.put(CardDataSet.HOLDER_DATA, pteid_id.getAllInfo());

          break;
        case HOLDER_ADDRESS:
          byte[] citizenAddr;
          citizenAddr = getCitizenAddr(keyboxName, pinGUI);
          PtEidAddr pteid_addr = PtEidAddr.parseAddr(citizenAddr);
          result.put(CardDataSet.HOLDER_ADDRESS, pteid_addr.getAllInfo());

          break;
        case HOLDER_PICTURE:
          citizenData = getCitizenData(keyboxName, pinGUI, -1);
          PtEidPic pteid_pic = PtEidPic.parsePic(citizenData);
          result.put(CardDataSet.HOLDER_PICTURE, pteid_pic.getPicture());

          break;
      }
    }

    return result;
  }

  private byte[] getCitizenData(KeyboxName keyboxName, PINGUI provider, int hintMaxSize) throws SignatureCardException, InterruptedException {
    try {
      CardChannel channel = getCardChannel();
      // SELECT applet
      execSELECT_AID(channel, AID_APPLET);
      // SELECT DF_ISSUES
      execSELECT_FID(channel, DF_ISSUES);
      // SELECT EF_SIGN_CERT
      byte[] fcx = execSELECT_FID(channel, new byte[]{(byte) 239, (byte) 2});
      int maxsize = hintMaxSize > 0 ? hintMaxSize : ISO7816Utils.getLengthFromFCx(fcx);
      // READ BINARY
      byte[] citizenData = ISO7816Utils.readTransparentFile(channel, maxsize);
      if (citizenData == null) {
        throw new NotActivatedException();
      }
      return citizenData;
    } catch (FileNotFoundException e) {
      throw new NotActivatedException();
    } catch (CardException e) {
      log.info("Failed to get data.", e);
      throw new SignatureCardException(e);
    }
  }

  public byte[] getCitizenAddr(KeyboxName keyboxName, PINGUI provider) throws SignatureCardException, InterruptedException {
    try {
      CardChannel channel = getCardChannel();
      // SELECT applet
      execSELECT_AID(channel, AID_APPLET);
      // SELECT DF_ISSUES
      execSELECT_FID(channel, DF_ISSUES);

      verifyPINLoop(channel, ADDR_PIN_SPEC, provider);

      // SELECT EF_SIGN_CERT
      byte[] fcx = execSELECT_FID(channel, new byte[]{(byte) 239, (byte) 5});
      int maxsize = ISO7816Utils.getLengthFromFCx(fcx);
      // READ BINARY
      byte[] citizendAddr = ISO7816Utils.readTransparentFile(channel, maxsize);
      if (citizendAddr == null) {
        throw new NotActivatedException();
      }
      return citizendAddr;
    } catch (FileNotFoundException e) {
      throw new NotActivatedException();
    } catch (CardException e) {
      log.info("Failed to get citizend address data", e);
      throw new SignatureCardException(e);
    }
  }

  @Override
  public Set<CardDataSet> getSupportedCardDataSets() {
    return EnumSet.of(CardDataSet.HOLDER_DATA, CardDataSet.HOLDER_PICTURE, CardDataSet.HOLDER_ADDRESS);
  }

  @Override
  public byte[] getInfobox(String infobox, PINGUI pinGUI, String domainId)
      throws SignatureCardException, InterruptedException {
  
    throw new IllegalArgumentException("Infobox '" + infobox
        + "' not supported.");
  }

  @Override
  public byte[] createSignature(InputStream input, KeyboxName keyboxName,
      PINGUI pinGUI, String alg) throws SignatureCardException,
      InterruptedException, IOException {
    
    if (!"http://www.w3.org/2000/09/xmldsig#rsa-sha1".equals(alg)) {
      throw new SignatureCardException("Card does not support algorithm " + alg + ".");
    }

    final byte[] dst = { 
        (byte) 0x80, // algorithm reference
          (byte) 0x01, (byte) 0x12, // RSASSA-PKCS1-v1.5 using SHA1
        (byte) 0x84, // private key reference
          (byte) 0x01, (byte) 0x01};

    MessageDigest md;
    try {
      md = MessageDigest.getInstance("SHA-1");
    } catch (NoSuchAlgorithmException e) {
      log.error("Failed to get MessageDigest.", e);
      throw new SignatureCardException(e);
    }
    // calculate message digest
    byte[] digest = new byte[md.getDigestLength()];
    for (int l; (l = input.read(digest)) != -1;) {
      md.update(digest, 0, l);
    }
    digest = md.digest();


    try {
      
      CardChannel channel = getCardChannel();

      // SELECT applet
      execSELECT_AID(channel, AID_APPLET);
      // SELECT DF_ISSUES
      execSELECT_FID(channel, DF_ISSUES);
      // VERIFY
      verifyPINLoop(channel, QS_PIN_SPEC, pinGUI);
      // MANAGE SECURITY ENVIRONMENT : RESTORE SE
      execMSE(channel, 0x73, 0x03, null);
      // MANAGE SECURITY ENVIRONMENT : SET DST
      execMSE(channel, 0x41, 0xB6, dst);
      // PERFORM SECURITY OPERATION : HASH
      execPSO_HASH(channel, digest);
      // PERFORM SECURITY OPERATION : COMPUTE DIGITAL SIGNATURE
      return execPSO_COMPUTE_DIGITAL_SIGNATURE(channel);

    } catch (CardException e) {
      log.warn("Failed to execute command.", e);
      throw new SignatureCardException("Failed to access card.", e);
    }

  }

  protected void verifyPINLoop(CardChannel channel, PinInfo spec,
      PINGUI provider) throws LockedException, NotActivatedException,
      SignatureCardException, InterruptedException, CardException {
    
    int retries = -1;
    do {
      retries = verifyPIN(channel, spec, provider, retries);
    } while (retries >= -1);
  }
  
  protected int verifyPIN(CardChannel channel, PinInfo pinSpec,
      PINGUI provider, int retries) throws SignatureCardException,
      LockedException, NotActivatedException, InterruptedException,
      CardException {
    
    VerifyAPDUSpec apduSpec = new VerifyAPDUSpec(
        new byte[] { (byte) 0x00, (byte) 0x20, (byte) 0x00, pinSpec.getKID(),
            (byte) 0x08, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF }, 
        0, VerifyAPDUSpec.PIN_FORMAT_ASCII, 8);
    
    ResponseAPDU resp = reader.verify(channel, apduSpec, provider, pinSpec, retries);
    
    if (resp.getSW() == 0x9000) {
      return -2;
    }
    if (resp.getSW() >> 4 == 0x63c) {
      return 0x0f & resp.getSW();
    }
    
    switch (resp.getSW()) {
    case 0x6300:
      // incorrect PIN, number of retries not provided
      return -1;
    case 0x6983:
      // authentication method blocked
      throw new LockedException();
  
    default:
      String msg = "VERIFY failed. SW=" + Integer.toHexString(resp.getSW()); 
      log.info(msg);
      throw new SignatureCardException(msg);
    }
    
  }



  protected void execSELECT_AID(CardChannel channel, byte[] aid)
      throws SignatureCardException, CardException {

    ResponseAPDU resp = channel.transmit(new CommandAPDU(0x00, 0xA4, 0x04,
        0x0C, aid, 256));

    if (resp.getSW() == 0x6A82) {
      String msg = "File or application not found FID="
          + SMCCHelper.toString(aid) + " SW="
          + Integer.toHexString(resp.getSW()) + ".";
      log.info(msg);
      throw new FileNotFoundException(msg);
    } else if (resp.getSW() != 0x9000) {
      String msg = "Failed to select application FID="
          + SMCCHelper.toString(aid) + " SW="
          + Integer.toHexString(resp.getSW()) + ".";
      log.error(msg);
      throw new SignatureCardException(msg);
    } 

  }
  
  protected byte[] execSELECT_FID(CardChannel channel, byte[] fid)
      throws SignatureCardException, CardException {

    ResponseAPDU resp = channel.transmit(new CommandAPDU(0x00, 0xA4, 0x00,
        0x00, fid, 256));

    if (resp.getSW() == 0x6A82) {
      String msg = "File or application not found FID="
          + SMCCHelper.toString(fid) + " SW="
          + Integer.toHexString(resp.getSW()) + ".";
      log.info(msg);
      throw new FileNotFoundException(msg);
    } else if (resp.getSW() != 0x9000) {
      String msg = "Failed to select application FID="
          + SMCCHelper.toString(fid) + " SW="
          + Integer.toHexString(resp.getSW()) + ".";
      log.error(msg);
      throw new SignatureCardException(msg);
    } else {
      return resp.getBytes();
    }

  }

  protected void execMSE(CardChannel channel, int p1, int p2, byte[] data)
      throws CardException, SignatureCardException {

    ResponseAPDU resp;
    if (data == null) {
      resp = channel.transmit(new CommandAPDU(0x00, 0x22, p1, p2, 256));
    } else {
      resp = channel.transmit(new CommandAPDU(0x00, 0x22, p1, p2, data, 256));
    }

    if (resp.getSW() != 0x9000) {
      throw new SignatureCardException("MSE:SET failed: SW="
          + Integer.toHexString(resp.getSW()));
    }
  }

  protected void execPSO_HASH(CardChannel channel, byte[] hash) throws CardException, SignatureCardException {
    
    ByteArrayOutputStream data = new ByteArrayOutputStream(hash.length + 2);
    try {
      data.write(0x90);
      data.write(hash.length);
      data.write(hash);
    } catch (IOException e) {
      throw new SignatureCardException(e);
    }
    
    ResponseAPDU resp = channel.transmit(
        new CommandAPDU(0x00, 0x2A, 0x90, 0xA0, data.toByteArray()));
    if (resp.getSW() != 0x9000) {
      throw new SignatureCardException("PSO - HASH failed: SW="
          + Integer.toHexString(resp.getSW()));
    }
    
  }
  
  protected byte[] execPSO_COMPUTE_DIGITAL_SIGNATURE(CardChannel channel)
      throws CardException, SignatureCardException {
    
    ResponseAPDU resp = channel
          .transmit(new CommandAPDU(0x00, 0x2A, 0x9E, 0x9A, 0x14));
    
    if (resp.getSW() == 0x6982) {
      throw new SecurityStatusNotSatisfiedException();
    } else if (resp.getSW() == 0x6983) {
      throw new LockedException();
    } else if (resp.getSW() != 0x9000) {
      throw new SignatureCardException(
          "PSO: COMPUTE DIGITAL SIGNATURE failed: SW="
              + Integer.toHexString(resp.getSW()));
    } else {
      return resp.getData();
    }
  }

}
