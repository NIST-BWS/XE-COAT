package gov.nist.ixe.templates.ini;

import gov.nist.ixe.XmlUtil;

import java.util.ArrayList;
import java.util.List;

public class XmlSectionElement extends XmlFragment {
	private List<XmlValue> values = new ArrayList<XmlValue>();
	protected boolean isMultivalued;
	protected boolean isOfMixedTypes;
	
	public boolean isMultivalued() { 
		return isMultivalued; 
	}
	
	public boolean isOfMixedTypes() { 
		return isOfMixedTypes; 
	}
	
	public String getSimpleValue() { 
		String result = "";
		if (values.get(0) != null) result = values.get(0).getValue();
		return result; 
	}
	
	public String getEscapedSimpleValue() { 		
		return XmlUtil.EscapeForXml(getSimpleValue()); 
	}
	
	public List<XmlValue> getValues() { 
		return values; 
	}
}