//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-661 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.03.05 at 03:21:43 PM EST 
//


package com.sun.grizzly.http.webxml.schema.version_2_5;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 	
 * 
 * 	  The ejb-ref-name element contains the name of an EJB
 * 	  reference. The EJB reference is an entry in the
 * 	  Deployment Component's environment and is relative to the
 * 	  java:comp/env context.  The name must be unique within the
 * 	  Deployment Component.
 * 
 * 	  It is recommended that name is prefixed with "ejb/".
 * 
 * 	  Example:
 * 
 * 	  <ejb-ref-name>ejb/Payroll</ejb-ref-name>
 * 
 * 	  
 *       
 * 
 * <p>Java class for ejb-ref-nameType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ejb-ref-nameType">
 *   &lt;simpleContent>
 *     &lt;restriction base="&lt;http://java.sun.com/xml/ns/javaee>jndi-nameType">
 *     &lt;/restriction>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ejb-ref-nameType")
public class EjbRefNameType
    extends JndiNameType
{


}
