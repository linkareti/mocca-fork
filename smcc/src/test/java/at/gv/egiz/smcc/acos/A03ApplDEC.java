/*
* Copyright 2008 Federal Chancellery Austria and
* Graz University of Technology
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package at.gv.egiz.smcc.acos;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Random;

import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

import at.gv.egiz.smcc.CardChannelEmul;
import at.gv.egiz.smcc.File;
import at.gv.egiz.smcc.PIN;


@SuppressWarnings("restriction")
public class A03ApplDEC extends ACOSApplDEC {
  
  public static final int KID_PIN_INF = 0x83;

  public A03ApplDEC() {
    super();

    System.arraycopy(IDLINK, 0, EF_INFOBOX, 0, IDLINK.length);
    putFile(new File(FID_EF_INFOBOX, EF_INFOBOX, FCI_EF_INFOBOX, KID_PIN_INF));

    try {
      pins.put(KID_PIN_INF, new PIN("0000\0\0\0\0".getBytes("ASCII"), KID_PIN_INF, 10));
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public ResponseAPDU cmdMANAGE_SECURITY_ENVIRONMENT(CommandAPDU command, CardChannelEmul channel) {

    checkINS(command, 0x22);

    switch (command.getP2()) {
    case 0xA4:
      switch (command.getP1()) {
      case 0x41: {
        // INTERNAL AUTHENTICATE
        byte[] dst = new byte[] { (byte) 0x84, (byte) 0x01, (byte) 0x88, (byte) 0x80, (byte) 0x01, (byte) 0x01 };
        if (Arrays.equals(dst, command.getData())) {
          securityEnv = command.getData();
          return new ResponseAPDU(new byte[] {(byte) 0x90, (byte) 0x00});
        } else {
          return new ResponseAPDU(new byte[] {(byte) 0x6F, (byte) 0x05});
        }
      }
      case 0x81:
        // EXTERNAL AUTHENTICATE
      }
    case 0xB6:
      switch (command.getP1()) {
      case 0x41:
        // PSO - COMPUTE DIGITAL SIGNATURE
      case 0x81:
        // PSO - VERIFY DGITAL SIGNATURE
      }
    case 0xB8:
      switch (command.getP1()) {
      case 0x41:
        // PSO � DECIPHER
      case 0x81:
        // PSO � ENCIPHER
      }
    default:
      return new ResponseAPDU(new byte[] {(byte) 0x6A, (byte) 0x81});
    }

  }

  @Override
  public ResponseAPDU cmdPERFORM_SECURITY_OPERATION(CommandAPDU command, CardChannelEmul channel) {
    
    checkINS(command, 0x2A);

    return new ResponseAPDU(new byte[] {(byte) 0x6A, (byte) 0x00});
    
  }

  @Override
  public ResponseAPDU cmdINTERNAL_AUTHENTICATE(CommandAPDU command, CardChannelEmul channel) {
    
    checkINS(command, 0x88);
    
    if (command.getP1() == 0x10 && command.getP2() == 0x00) {
      
      byte[] data = command.getData();
      
      if (securityEnv == null) {
        // Security Environment not set or wrong
        return new ResponseAPDU(new byte[] {(byte) 0x6F, (byte) 0x05});
      }

      byte[] digestInfo = new byte[] {
          (byte) 0x30, (byte) 0x21, (byte) 0x30, (byte) 0x09, (byte) 0x06, (byte) 0x05, (byte) 0x2B, (byte) 0x0E, 
          (byte) 0x03, (byte) 0x02, (byte) 0x1A, (byte) 0x05, (byte) 0x00, (byte) 0x04, (byte) 0x14
      };
      
      if (data.length != 35 || !Arrays.equals(digestInfo, Arrays.copyOf(data, 15))) {
        return new ResponseAPDU(new byte[] {(byte) 0x67, (byte) 0x00});
      }
        

      if (pins.get(KID_PIN_DEC).state != PIN.STATE_PIN_VERIFIED) {
        // Security Status not satisfied
        return new ResponseAPDU(new byte[] {(byte) 0x69, (byte) 0x82});
      }
      
      byte[] signature = new byte[48]; 
      
      // TODO replace by signature creation
      Random random = new Random();
      random.nextBytes(signature);
      
      byte[] response = new byte[signature.length + 2];
      System.arraycopy(signature, 0, response, 0, signature.length);
      response[signature.length] = (byte) 0x90;
      response[signature.length + 1] = (byte) 0x00;
      
      hash = null;
      pins.get(KID_PIN_DEC).state = PIN.STATE_RESET;

      return new ResponseAPDU(response);
        
      
    } else {
      return new ResponseAPDU(new byte[] {(byte) 0x6A, (byte) 0x81});
    }

  }

}
