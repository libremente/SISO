//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.10.24 at 04:01:25 PM CEST 
//


package org.hl7.v3.request;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for COCT_MT510000UV06.ResponsibleParty complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="COCT_MT510000UV06.ResponsibleParty">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{urn:hl7-org:v3}InfrastructureRootElements"/>
 *         &lt;element name="id" type="{urn:hl7-org:v3}II" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="code" type="{urn:hl7-org:v3}CE" minOccurs="0"/>
 *         &lt;element name="addr" type="{urn:hl7-org:v3}AD" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="telecom" type="{urn:hl7-org:v3}TEL" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="agentPerson" type="{urn:hl7-org:v3}COCT_MT510000UV06.Person" minOccurs="0"/>
 *         &lt;choice>
 *           &lt;element name="representedPerson" type="{urn:hl7-org:v3}COCT_MT510000UV06.Person" minOccurs="0"/>
 *           &lt;element name="representedOrganization" type="{urn:hl7-org:v3}COCT_MT510000UV06.Organization" minOccurs="0"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{urn:hl7-org:v3}InfrastructureRootAttributes"/>
 *       &lt;attribute name="nullFlavor" type="{urn:hl7-org:v3}NullFlavor" />
 *       &lt;attribute name="classCode" use="required" type="{urn:hl7-org:v3}RoleClassAgent" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "COCT_MT510000UV06.ResponsibleParty", propOrder = {
    "realmCode",
    "typeId",
    "templateId",
    "id",
    "code",
    "addr",
    "telecom",
    "agentPerson",
    "representedPerson",
    "representedOrganization"
})
public class COCTMT510000UV06ResponsibleParty {

    protected List<CS> realmCode;
    protected II typeId;
    protected List<II> templateId;
    protected List<II> id;
    protected CE code;
    protected List<AD> addr;
    protected List<TEL> telecom;
    @XmlElementRef(name = "agentPerson", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<COCTMT510000UV06Person> agentPerson;
    @XmlElementRef(name = "representedPerson", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<COCTMT510000UV06Person> representedPerson;
    @XmlElementRef(name = "representedOrganization", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<COCTMT510000UV06Organization> representedOrganization;
    @XmlAttribute(name = "nullFlavor")
    protected NullFlavor nullFlavor;
    @XmlAttribute(name = "classCode", required = true)
    protected RoleClassAgent classCode;

    /**
     * Gets the value of the realmCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the realmCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRealmCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CS }
     * 
     * 
     */
    public List<CS> getRealmCode() {
        if (realmCode == null) {
            realmCode = new ArrayList<CS>();
        }
        return this.realmCode;
    }

    /**
     * Gets the value of the typeId property.
     * 
     * @return
     *     possible object is
     *     {@link II }
     *     
     */
    public II getTypeId() {
        return typeId;
    }

    /**
     * Sets the value of the typeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link II }
     *     
     */
    public void setTypeId(II value) {
        this.typeId = value;
    }

    /**
     * Gets the value of the templateId property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the templateId property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTemplateId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link II }
     * 
     * 
     */
    public List<II> getTemplateId() {
        if (templateId == null) {
            templateId = new ArrayList<II>();
        }
        return this.templateId;
    }

    /**
     * Gets the value of the id property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the id property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link II }
     * 
     * 
     */
    public List<II> getId() {
        if (id == null) {
            id = new ArrayList<II>();
        }
        return this.id;
    }

    /**
     * Gets the value of the code property.
     * 
     * @return
     *     possible object is
     *     {@link CE }
     *     
     */
    public CE getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     * 
     * @param value
     *     allowed object is
     *     {@link CE }
     *     
     */
    public void setCode(CE value) {
        this.code = value;
    }

    /**
     * Gets the value of the addr property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the addr property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAddr().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AD }
     * 
     * 
     */
    public List<AD> getAddr() {
        if (addr == null) {
            addr = new ArrayList<AD>();
        }
        return this.addr;
    }

    /**
     * Gets the value of the telecom property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the telecom property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTelecom().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TEL }
     * 
     * 
     */
    public List<TEL> getTelecom() {
        if (telecom == null) {
            telecom = new ArrayList<TEL>();
        }
        return this.telecom;
    }

    /**
     * Gets the value of the agentPerson property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link COCTMT510000UV06Person }{@code >}
     *     
     */
    public JAXBElement<COCTMT510000UV06Person> getAgentPerson() {
        return agentPerson;
    }

    /**
     * Sets the value of the agentPerson property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link COCTMT510000UV06Person }{@code >}
     *     
     */
    public void setAgentPerson(JAXBElement<COCTMT510000UV06Person> value) {
        this.agentPerson = value;
    }

    /**
     * Gets the value of the representedPerson property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link COCTMT510000UV06Person }{@code >}
     *     
     */
    public JAXBElement<COCTMT510000UV06Person> getRepresentedPerson() {
        return representedPerson;
    }

    /**
     * Sets the value of the representedPerson property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link COCTMT510000UV06Person }{@code >}
     *     
     */
    public void setRepresentedPerson(JAXBElement<COCTMT510000UV06Person> value) {
        this.representedPerson = value;
    }

    /**
     * Gets the value of the representedOrganization property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link COCTMT510000UV06Organization }{@code >}
     *     
     */
    public JAXBElement<COCTMT510000UV06Organization> getRepresentedOrganization() {
        return representedOrganization;
    }

    /**
     * Sets the value of the representedOrganization property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link COCTMT510000UV06Organization }{@code >}
     *     
     */
    public void setRepresentedOrganization(JAXBElement<COCTMT510000UV06Organization> value) {
        this.representedOrganization = value;
    }

    /**
     * Gets the value of the nullFlavor property.
     * 
     * @return
     *     possible object is
     *     {@link NullFlavor }
     *     
     */
    public NullFlavor getNullFlavor() {
        return nullFlavor;
    }

    /**
     * Sets the value of the nullFlavor property.
     * 
     * @param value
     *     allowed object is
     *     {@link NullFlavor }
     *     
     */
    public void setNullFlavor(NullFlavor value) {
        this.nullFlavor = value;
    }

    /**
     * Gets the value of the classCode property.
     * 
     * @return
     *     possible object is
     *     {@link RoleClassAgent }
     *     
     */
    public RoleClassAgent getClassCode() {
        return classCode;
    }

    /**
     * Sets the value of the classCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link RoleClassAgent }
     *     
     */
    public void setClassCode(RoleClassAgent value) {
        this.classCode = value;
    }

}
