package gov.nist.ixe.templates.ini;

import static gov.nist.ixe.Logging.trace;
import gov.nist.ixe.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class XmlModel {
	public static final String namespacePrefix = "coat";
	
	public static final Class<?> ASSUMED_CLASS_FOR_NULL_VALUES = Object.class;

	private static final String XS_STRING = "xs:string";
	private static final String XS_INT = "xs:int";
	private static final String XS_DECIMAL = "xs:decimal";
	private static final String XS_DOUBLE = "xs:double";
	private static final String XS_ANY = "xs:anyType";

	private Set<XmlArrayType> arrayTypes;
	private List<XmlSection> sections;
	private String rootElementName;
	private String rootElementTypeNameForDefinition;
	private String rootElementTypeNameForReference;
	private String targetNamespace;

	private static Map<Class<?>, XmlArrayType> xmlArrayTypeMap = getArrayTypeMap();
	private static Map<Class<?>, String> xmlTypeMap = getTypeMap();

	public XmlModel(List<IniSection> iniSections, String rootElementName,
			String namespace) {
		trace();

		arrayTypes = getXmlArrayTypes(iniSections);
		sections = getXmlSections(iniSections);
		targetNamespace = namespace;

		if (isMultipleSections()) {
			this.rootElementName = rootElementName;
			this.rootElementTypeNameForDefinition = TypeNameForDefinition
					.fromRootName(rootElementName);
			this.rootElementTypeNameForReference = TypeNameForReference
					.fromRootName(rootElementName);
		} else {
			String sectionName = iniSections.get(0).getName();
			this.rootElementName = sectionName;
			this.rootElementTypeNameForDefinition = TypeNameForDefinition
					.fromSectionName(sectionName);
			this.rootElementTypeNameForReference = TypeNameForReference
					.fromRootName(sectionName);
		}
	}


	private static Map<Class<?>, XmlArrayType> getArrayTypeMap() {
		xmlArrayTypeMap = new HashMap<Class<?>, XmlArrayType>();
		xmlArrayTypeMap.put(String.class,  new XmlArrayType("element", "StringArray",  XS_STRING));
		xmlArrayTypeMap.put(Integer.class, new XmlArrayType("element", "IntegerArray", XS_INT));
		xmlArrayTypeMap.put(Long.class,    new XmlArrayType("element", "DecimalArray", XS_DECIMAL));
		xmlArrayTypeMap.put(Double.class,  new XmlArrayType("element", "DoubleArray",  XS_DOUBLE));
		xmlArrayTypeMap.put(Object.class,  new XmlArrayType("element", "Array",        XS_ANY));
		return xmlArrayTypeMap;
	}

	private static Map<Class<?>, String> getTypeMap() {
		xmlTypeMap = new HashMap<Class<?>, String>();
		xmlTypeMap.put(String.class,  XS_STRING);
		xmlTypeMap.put(Integer.class, XS_INT);
		xmlTypeMap.put(Long.class,    XS_DECIMAL);
		xmlTypeMap.put(Double.class,  XS_DOUBLE);
		xmlTypeMap.put(Object.class,  XS_ANY);
		return xmlTypeMap;
	}

	public boolean isMultipleSections() {
		trace();

		return sections.size() > 1;
	}

	private static Set<XmlArrayType> getXmlArrayTypes(
			List<IniSection> iniSections) {
		trace();

		// For keys with multiple values, collect all of the unique
		// types that additional support types will be needed for.
		//
		Set<XmlArrayType> result = new TreeSet<XmlArrayType>();

		for (IniSection section : iniSections) {
			for (Values values : section.getMultiValueKeys().values()) {
				Class<?> type = values.getMostDetailedCommonType();
				result.add(xmlArrayTypeMap.get(type));
			}
		}

		return result;
	}

	private static List<XmlSection> getXmlSections(List<IniSection> iniSections) {
		trace();

		List<XmlSection> result = new ArrayList<XmlSection>();

		for (IniSection section : iniSections) {

			XmlSection sectionType = new XmlSection();

			sectionType.typeNameForReference = TypeNameForReference
					.fromSectionName(section.getName());
			sectionType.typeNameForDefinition = TypeNameForDefinition
					.fromSectionName(section.getName());
			sectionType.elementName = ElementName.fromSectionName(section
					.getName());

			for (Entry<String, Values> entry : section.getKeyValuesMap()
					.entrySet()) {
				XmlSectionElement element = new XmlSectionElement();

				// Getting the element name is easy
				element.elementName = ElementName.fromKeyName(entry.getKey());

				Values values = entry.getValue();
				element.typeNameForReference = getSectionElementTypeName(values);

				injectValues(element, values);

				sectionType.getElements().add(element);
			}

			result.add(sectionType);
		}

		return result;
	}

	private static String getSectionElementTypeName(Values values) {
		trace();

		// The type name is more complicated. If the key can have multiple
		// values, then we need to get a custom array type.
		//
		Class<?> type = values.getMostDetailedCommonType();
		String typeName;

		if (values.isMultiple()) {
			XmlArrayType arrayType = xmlArrayTypeMap.get(type);
			typeName = arrayType.getTypeNameForReference();
		} else {
			typeName = xmlTypeMap.get(type);
		}

		return typeName;
	}

	private static void injectValues(XmlSectionElement target, Values values) {

		if (values.size() == 1) {
			//target.isMultivalued = false;
			target.setIsMultivalued(false);
			XmlValue value = new XmlValue();

			value.value = values.getUnquotedString(0);
			value.type = xmlTypeMap.get(values.get(0).getClass());
			target.getValues().add(value);
		} else {
			target.setIsMultivalued(true);
			target.setIsOfMixedTypes(values.getMostDetailedCommonType().equals(Object.class));
			//target.isMultivalued = true;
			//target.isOfMixedTypes = values.getMostDetailedCommonType().equals(Object.class);
					

			for (int i = 0; i < values.size(); i++) {
				XmlValue v = new XmlValue();
				v.value = values.getUnquotedString(i);
				Class<?> vType = ASSUMED_CLASS_FOR_NULL_VALUES;
				if (values.get(i) != null) {
					vType = values.get(i).getClass();
				}
				v.type = xmlTypeMap.get(vType);
				target.getValues().add(v);
			}

		}
	}

	protected static class TypeNameForDefinition {
		protected static String fromSectionName(String sectionName) {
			trace();
			return cleanupVariableName(sectionName) + "Type";
		}

		protected static String fromRootName(String rootName) {
			trace();
			return cleanupVariableName(rootName) + "Type";
		}
	}

	protected static class TypeNameForReference {
		protected static String fromSectionName(String sectionName) {
			trace();

			return namespacePrefix + ":"
					+ TypeNameForDefinition.fromSectionName(sectionName);
		}

		protected static String fromRootName(String sectionName) {
			trace();

			return namespacePrefix + ":"
					+ TypeNameForDefinition.fromRootName(sectionName);
		}
	}

	protected static class ElementName {
		protected static String fromSectionName(String sectionName) {
			trace();

			return cleanupVariableName(sectionName);
		}

		protected static String fromKeyName(String keyName) {
			trace();
			return cleanupVariableName(keyName);
		}
	}

	public static String cleanupVariableName(String name) {

		String result = name.trim();
		List<Integer> indexes = new ArrayList<Integer>();
		String regex = "^[^a-zA-Z]+|[^0-9a-zA-Z]+";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(name);
		while (matcher.find()) {
			indexes.add(matcher.end());
		}
		for (int i : indexes) {
			result = StringUtil.capitalize(result, i);
		}
		result = result.replaceAll(regex, "");

		// JAXB camel cases after digits, so this needs to be fixed. For
		// example, a INI key called Foo3d becomes getFoo3D in the JAXB 
		// generated code. This regex finds all of the letters following 
		// a digit so they can be capitalized.
		//
		indexes.clear();
		String jaxbFixRegex = "(?:[0-9])([a-zA-Z])";
		pattern = Pattern.compile(jaxbFixRegex);
		matcher = pattern.matcher(result);
		while (matcher.find()) {
			indexes.add(matcher.start(1));
		}
		for (int i : indexes) {
			result = StringUtil.capitalize(result, i);
		}

		return result;
	}

	public String getRootElementName() {
		return rootElementName;
	}

	public String getRootElementTypeNameForDefinition() {
		return rootElementTypeNameForDefinition;
	}

	public String getRootElementTypeNameForReference() {
		return rootElementTypeNameForReference;
	}

	public String getNamespacePrefix() {
		return namespacePrefix;
	}

	public String getTargetNamespace() {
		return targetNamespace;
	}

	public Set<XmlArrayType> getArrayTypes() {
		return arrayTypes;
	}

	public List<XmlSection> getSections() {
		return sections;
	}
}