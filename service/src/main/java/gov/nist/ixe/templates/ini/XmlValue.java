package gov.nist.ixe.templates.ini;

import gov.nist.ixe.XmlUtil;

public class XmlValue {
	protected String Value="";
	protected String Type="";
	public String getValue() {
		return Value;
	}		
	public String getEscapedValue() {	
		return XmlUtil.EscapeForXml(Value);
	}
	public String getType() { return Type; }
}
