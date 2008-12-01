
package at.buergerkarte.namespaces.cardchannel;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the at.buergerkarte.namespaces.cardchannel package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Grunddaten_QNAME = new QName("", "Grunddaten");
    private final static QName _Script_QNAME = new QName("", "Script");
    private final static QName _Response_QNAME = new QName("", "Response");
    private final static QName _SVPersonenbindung_QNAME = new QName("", "SV-Personenbindung");
    private final static QName _Status_QNAME = new QName("", "Status");
    private final static QName _EHIC_QNAME = new QName("", "EHIC");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: at.buergerkarte.namespaces.cardchannel
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AttributeList }
     * 
     */
    public AttributeList createAttributeList() {
        return new AttributeList();
    }

    /**
     * Create an instance of {@link AttributeType }
     * 
     */
    public AttributeType createAttributeType() {
        return new AttributeType();
    }

    /**
     * Create an instance of {@link ScriptType }
     * 
     */
    public ScriptType createScriptType() {
        return new ScriptType();
    }

    /**
     * Create an instance of {@link ATRType }
     * 
     */
    public ATRType createATRType() {
        return new ATRType();
    }

    /**
     * Create an instance of {@link CommandAPDUType }
     * 
     */
    public CommandAPDUType createCommandAPDUType() {
        return new CommandAPDUType();
    }

    /**
     * Create an instance of {@link ResetType }
     * 
     */
    public ResetType createResetType() {
        return new ResetType();
    }

    /**
     * Create an instance of {@link VerifyAPDUType }
     * 
     */
    public VerifyAPDUType createVerifyAPDUType() {
        return new VerifyAPDUType();
    }

    /**
     * Create an instance of {@link ResponseAPDUType }
     * 
     */
    public ResponseAPDUType createResponseAPDUType() {
        return new ResponseAPDUType();
    }

    /**
     * Create an instance of {@link ResponseType }
     * 
     */
    public ResponseType createResponseType() {
        return new ResponseType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AttributeList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Grunddaten")
    public JAXBElement<AttributeList> createGrunddaten(AttributeList value) {
        return new JAXBElement<AttributeList>(_Grunddaten_QNAME, AttributeList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ScriptType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Script")
    public JAXBElement<ScriptType> createScript(ScriptType value) {
        return new JAXBElement<ScriptType>(_Script_QNAME, ScriptType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Response")
    public JAXBElement<ResponseType> createResponse(ResponseType value) {
        return new JAXBElement<ResponseType>(_Response_QNAME, ResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "SV-Personenbindung")
    @XmlJavaTypeAdapter(HexBinaryAdapter.class)
    public JAXBElement<byte[]> createSVPersonenbindung(byte[] value) {
        return new JAXBElement<byte[]>(_SVPersonenbindung_QNAME, byte[].class, null, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AttributeList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Status")
    public JAXBElement<AttributeList> createStatus(AttributeList value) {
        return new JAXBElement<AttributeList>(_Status_QNAME, AttributeList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AttributeList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "EHIC")
    public JAXBElement<AttributeList> createEHIC(AttributeList value) {
        return new JAXBElement<AttributeList>(_EHIC_QNAME, AttributeList.class, null, value);
    }

}