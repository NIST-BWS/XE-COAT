package gov.nist.ixe.templates.tests;

import gov.nist.ixe.stringsource.StringSource;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class Examples {

	private static final String TEMPLATE_STRING = 
			"<test>\\r\\n" +
			"#foreach ($machine in $m)\\r\\n" +
			"Hostname: $machine.getHostname()\\r\\n" +
			"[DHCP: $machine.isDhcp()]\\r\\n" +
			"#end\\r\\n</test>\\r\\n";
	

	public static final StringSource TEMPLATE = ConvertString(TEMPLATE_STRING, "UTF-16");

	
	private static StringSource ConvertString(String str, String encoding) {
		byte[] data = null;
		try {
			data = str.getBytes(encoding);		
		} catch (UnsupportedEncodingException uee) {
			
		}
		return new StringSource(data, encoding);
		
	}
//	public static final StringSource TEMPLATE = new StringSource(
//			TEMPLATE_STRING.getBytes("UTF-16"), 
//			Charset.defaultCharset().name());

	public static final StringSource SCHEMA = new StringSource(
			("<?xml version=\"1.0\"?>\\r\\n" +
			"<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\\r\\n" + 
			"  <xs:element name=\"Machine\">\\r\\n" +
			"    <xs:complexType>\\r\\n" +
	        "      <xs:sequence>\\r\\n" +
	        "        <xs:element name=\"Hostname\" type=\"xs:string\" />\\r\\n" +
	        "        <xs:element name=\"DHCP\" type=\"xs:boolean\" />\\r\\n" +
	        "        <xs:element name=\"IPAddress\" type=\"xs:string\" />\\r\\n" +
	        "      </xs:sequence>\\r\\n" +
	        "    </xs:complexType>\\r\\n" +
	        "  </xs:element>\\r\\n" +
	        "</xs:schema>\\r\\n").getBytes(),
	        "UTF-16");
	        //Charset.defaultCharset().name());
	
	
	public static final StringSource DUMMY0 = new StringSource("dummy0".getBytes(), Charset.defaultCharset().name());
	public static final StringSource DUMMY1 = new StringSource("dummy11".getBytes(), Charset.defaultCharset().name());
	public static final StringSource DUMMY2 = new StringSource("dummy222".getBytes(), Charset.defaultCharset().name());
	
	public static final String CONFIG_STRING = "<machine><blah></blah></machine>";
	public static final StringSource CONFIG = ConvertString(CONFIG_STRING, "UTF-16");
	
	public static final StringSource EMPTY = new StringSource(
			new byte[]{}, 
			Charset.defaultCharset().name());
	
		
	public static final StringSource WHITESPACE = new StringSource(
			" \t\r\n".getBytes(), 
			Charset.defaultCharset().name());
	
}
