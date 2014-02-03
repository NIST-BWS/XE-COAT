package gov.nist.ixe.templates.ini;

public abstract class XmlFragment {

	protected String elementName;
	protected String typeNameForDefinition;
	protected String typeNameForReference;
	
	public String getElementName() {
		return elementName;
	}
	public void setElementName(String elementName) {
		this.elementName = elementName;
	}
	public String getTypeNameForDefinition() {
		return typeNameForDefinition;
	}
	public String getTypeNameForReference() {
		return typeNameForReference;
	}

		

}
