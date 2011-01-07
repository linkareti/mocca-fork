package at.gv.egiz.smcc.activation;

import iaik.utils.CryptoUtils;

import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class RetailCBCMac {
  
  /**
   * Calculates a Retail CBC Mac from the given message.
   * <p>
   * The retail CBC Mac is calculated according to the following 
   * algorithm:
   * <ul>
   *    <li>
   *       Pad the message to a multiple n of the CBC cipher block size with
   *       a leading one bit followed by as many zero bits as necessary.
   *    </li>
   *    <li>
   *       Create a CBC cipher key from the first <code>cbcCipherKeyLen</code>
   *       bytes of the <code>csk</code> key and use it to calculate a 
   *       CBC Mac value from the first n-1 blocks of the padded message.
   *       For CBC Mac calculation initialize the CBC Cipher with an
   *       IV of all zero bytes.
   *    </li>
   *    <li>
   *       XOR the last block of the padded message with the CBC mac value
   *       and calculate the final retail MAC by encrypting the XOR result
   *       with the given Cipher algorithm in ECB mode using no padding.
   *    </li>
   * </ul>
   * 
   * @param msg the message
   * @param cbcCipherAlg the name of the CBC Cipher algorithm to be used
   * @param cipherAlg the name of the final Cipher algorithm to be used
   * @param csk the secret key to be used
   * @param cbcCipherKeyLen the length of the CBC cipher key to be used
   * @param blockSize the block size of the CBC Cipher
   * 
   * @return the retail CBC Mac value
   * 
   * @throws NoSuchAlgorithmException if any of the requested Cipher algorithms
   *                                  is not available 
   * @throws NoSuchProviderException if the IAIK provider is not installed
   * @throws InvalidKeyException if the key cannot be used with the Ciphers 
   * @throws GeneralSecurityException if the Cipher operation(s) fails
   */
  static byte[] retailMac(byte[] msg, 
                          String cbcCipherAlg,
                          String cipherAlg,
                          SecretKey csk,
                          int cbcCipherKeyLen,
                          int blockSize) 
    throws NoSuchAlgorithmException, 
         NoSuchProviderException, 
         InvalidKeyException, 
         GeneralSecurityException {
    
    if (msg == null) {
      throw new NullPointerException("Message m must not be null!");
    }
    if (csk == null) {
      throw new NullPointerException("Key csk must not be null!");
    }
    
    // calculate key for CBC cipher
    byte[] rawCsk = csk.getEncoded();
    int cskLen = rawCsk.length;
    SecretKey cbcCipherKey;
    if (cskLen == cbcCipherKeyLen) {
      cbcCipherKey = csk;
    } else if (cskLen < cbcCipherKeyLen) {
      throw new InvalidKeyException("Key too short!");
    } else {
      byte[] rawCbcCipherKey = new byte[blockSize];
      System.arraycopy(rawCsk, 0, rawCbcCipherKey, 0, blockSize);
      cbcCipherKey = new SecretKeySpec(rawCbcCipherKey, cbcCipherAlg);
    }
    // if necessary pad message with zeros
    byte[] paddedMsg = pad(msg, blockSize);
    
    // calculate CBC Mac for the first n-1 blocks
    int n = paddedMsg.length;
    int n_1 = n - blockSize;
    byte[] cbcMac = cbcMac(paddedMsg, 0, n_1, cbcCipherKey, cbcCipherAlg, blockSize);

    // calculate retail mac
    byte[] xor = new byte[blockSize];
    CryptoUtils.xorBlock(paddedMsg, n_1, cbcMac, 0, xor, 0, blockSize);
    Cipher cipher = Cipher.getInstance(cipherAlg+"/ECB/NoPadding", "IAIK");
    cipher.init(Cipher.ENCRYPT_MODE, csk);
    byte[] retailMac = cipher.doFinal(xor);
    return retailMac;
  }
  
  /**
   * Calculates a simple CBC Mac from the given (already) padded message.
   * 
   * @param paddedMsg the (zero) padded message
   * @param off the start offset in the paddedMsg array
   * @param len the number of bytes to be processed, starting at <code>off</code>
   * @param key the Cipher key
   * @param cipherAlg the name of the CBC Cipher algorithm to be used
   * @param blockSize the block size of the CBC Cipher
   * 
   * @return the CBC Mac value
   * 
   * @throws NoSuchAlgorithmException if the requested Cipher algorithm
   *                                  is not available 
   * @throws NoSuchProviderException if the IAIK provider is not installed
   * @throws InvalidKeyException if the key cannot be used with the Ciphers 
   * @throws GeneralSecurityException if the Cipher operation fails
   */
  static byte[] cbcMac(byte[] paddedMsg,
                       int off,
                       int len,
                       SecretKey key, 
                       String cipherAlg, 
                       int blockSize) 
    throws NoSuchAlgorithmException, 
           NoSuchProviderException, 
           InvalidKeyException, 
           GeneralSecurityException {  
           
    if (paddedMsg == null) {
      throw new NullPointerException("Message must not be null!");
    }
    if (key == null) {
      throw new NullPointerException("Key csk must not be null!");
    }
    
   
    Cipher cbcCipher = Cipher.getInstance(cipherAlg+"/CBC/NoPadding", "IAIK");
    byte[] iv = new byte[blockSize];
    cbcCipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
    int finOff = off;
    if (len > blockSize) {
      finOff = len - blockSize;
      cbcCipher.update(paddedMsg, 0, finOff);
    }
    byte[] mac = cbcCipher.doFinal(paddedMsg, finOff, blockSize);
    return mac;
  }
  
  /**
   * Pads the given message to a multiple of the given blocksize with
   * a leading one bit followed by as many zero bits as necessary
   * 
   * @param msg the message to be padded
   * @param blockSize the block size
   * 
   * @return the padded message
   */
  static byte[] pad(byte[] msg, int blockSize) {
    int paddingLen;
    byte[] paddedMsg;

    int msgLen = msg.length;
    if (msgLen == 0) {
      paddingLen = blockSize;
    } else {
      paddingLen = blockSize - msgLen % blockSize;
    }  
    if (paddingLen > 0) {
      paddedMsg = new byte[msgLen + paddingLen];
      System.arraycopy(msg, 0, paddedMsg, 0, msgLen);
      paddedMsg[msgLen] = (byte)0x80;
    } else {
      paddedMsg = msg;
    }
    return paddedMsg;
  }
  

  
  

}