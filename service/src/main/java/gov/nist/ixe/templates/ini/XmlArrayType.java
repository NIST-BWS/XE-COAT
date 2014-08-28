package gov.nist.ixe.templates.ini;

public class XmlArrayType 
	extends XmlFragment implements Comparable<XmlArrayType> {
	
	public XmlArrayType(String elementName, String typeName, String elementTypeName) {
		super.elementName = elementName;
		super.typeNameForDefinition = typeName;
		super.typeNameForReference = XmlModel.namespacePrefix + ":" + typeName;
		this.elementTypeName = elementTypeName;
	}
	
	private String elementTypeName;
	
	public String getElementTypeName() {
		return elementTypeName;
	}
	public void setElementTypeName(String elementTypeName) {
		this.elementTypeName = elementTypeName;
	}
	
	@Override
	public int compareTo(XmlArrayType o) {
		return typeNameForDefinition.compareTo(o.typeNameForDefinition);
	}
	
	@Override
	public boolean equals(Object o) {
		boolean result;
		if (o instanceof XmlArrayType) {
			result = this.compareTo((XmlArrayType) o) == 0;
		} else {
			result = false;
		}
		return result;
	}
	
	@Override
	public int hashCode() {
		return super.hashCode() + elementTypeName.hashCode();
	}


}
