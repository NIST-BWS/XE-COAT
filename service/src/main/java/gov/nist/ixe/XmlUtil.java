package gov.nist.ixe;

import static gov.nist.ixe.Logging.trace;



import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XmlUtil {
	
	public static String EscapeForXml(String value) {
		return value
				.replace("&",  "&amp;") // make sure ampersands are replaced first
				.replace("\"", "&quot;")
				.replace("'",  "&apos;")
				.replace("<",  "&lt;")
				.replace(">",  "&gt;");
	}

	public static void ValidateXml(InputSource schema, InputSource instance) throws ParserConfigurationException, SAXException, IOException {
		ValidateXml(new InputSource[]{schema}, instance);
	}

	
	public static void ValidateXml(InputSource[] schemas, InputSource instance) throws ParserConfigurationException, SAXException, IOException {
		trace();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setValidating(true);
		factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
		factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaSource", schemas);
		
		DocumentBuilder builder = factory.newDocumentBuilder();
		builder.setErrorHandler(new ErrorHandler() {
			public void warning(SAXParseException exception) throws SAXException {
				throw exception;
			}

			public void error(SAXParseException exception) throws SAXException {
				throw exception;
			}

			public void fatalError(SAXParseException exception) throws SAXException {
				throw exception;
			}
		});
		
		
		builder.parse(instance);
		
	}


}
