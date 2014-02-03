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

}
