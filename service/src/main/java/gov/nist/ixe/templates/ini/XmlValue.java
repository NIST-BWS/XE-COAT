package gov.nist.ixe.templates.ini;

import gov.nist.ixe.XmlUtil;

public class XmlValue {
	protected String value="";
	protected String type="";
	
	public String getValue() {
		return value;
	}		
	public String getEscapedValue() {	
		return XmlUtil.escapeForXml(value);
	}
	public String getType() { return type; }
}
