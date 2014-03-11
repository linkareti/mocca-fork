/*
 * Copyright 2011 by Graz University of Technology, Austria
 * MOCCA has been developed by the E-Government Innovation Center EGIZ, a joint
 * initiative of the Federal Chancellery Austria and Graz University of Technology.
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
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



package moaspss.generated;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.3-b02-
 * Generated source version: 2.1
 * 
 */
@WebService(name = "SignatureVerificationPortType", targetNamespace = "http://reference.e-government.gv.at/namespace/moa/wsdl/20020822#")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@XmlSeeAlso({
    ObjectFactory.class
})
public interface SignatureVerificationPortType {


    /**
     * 
     * @param body
     * @return
     *     returns moaspss.generated.VerifyXMLSignatureResponseType
     * @throws MOAFault
     */
    @WebMethod(action = "urn:VerifyXMLSignatureAction")
    @WebResult(name = "VerifyXMLSignatureResponse", targetNamespace = "http://reference.e-government.gv.at/namespace/moa/20020822#", partName = "body")
    public VerifyXMLSignatureResponseType verifyXMLSignature(
        @WebParam(name = "VerifyXMLSignatureRequest", targetNamespace = "http://reference.e-government.gv.at/namespace/moa/20020822#", partName = "body")
        VerifyXMLSignatureRequestType body)
        throws MOAFault
    ;

}