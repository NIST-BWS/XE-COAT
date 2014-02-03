package gov.nist.ixe.templates.ini;

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
		return values.get(0).getValue(); 
	}
	
	public List<XmlValue> getValues() { 
		return values; 
	}
}