@XmlSchema(namespace=Constants.NAMESPACE,
xmlns = {
		@XmlNs(prefix="", namespaceURI=Constants.NAMESPACE),
		@XmlNs(prefix="xs", namespaceURI="http://www.w3.org/2001/XMLSchema"),
		@XmlNs(prefix="xsi", namespaceURI="http://www.w3.org/2001/XMLSchema-instance")
	},
	elementFormDefault=XmlNsForm.QUALIFIED,
	attributeFormDefault=XmlNsForm.UNQUALIFIED)

package gov.nist.ixe.templates.jaxb;

import gov.nist.ixe.templates.Constants;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;