package gov.nist.ixe.templates.ini;

import gov.nist.ixe.XmlUtil;

import java.util.ArrayList;
import java.util.List;

public class XmlSectionElement extends XmlFragment {
	private List<XmlValue> values = new ArrayList<XmlValue>();
	private boolean isMultivalued;
	private boolean isOfMixedTypes;
	
	public boolean getIsMultivalued() { 
		return isMultivalued; 
	}
	protected void setIsMultivalued(boolean value) {
		isMultivalued = value;
	}
	
	public boolean getIsOfMixedTypes() { 
		return isOfMixedTypes; 
	}
	protected void setIsOfMixedTypes(boolean value) { 
		isOfMixedTypes = value; 
	}
	
	
	public String getSimpleValue() { 
		String result = "";
		if (values.get(0) != null) result = values.get(0).getValue();
		return result; 
	}
	
	public String getEscapedSimpleValue() { 		
		return XmlUtil.escapeForXml(getSimpleValue()); 
	}
	
	public List<XmlValue> getValues() { 
		return values; 
	}
}