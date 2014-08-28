package gov.nist.ixe.tests;

import gov.nist.ixe.XmlUtil;
import gov.nist.ixe.stringsource.StringSource;
import gov.nist.ixe.stringsource.StringSourcePersistence;
import gov.nist.ixe.templates.StringSourceUriPair;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlUtilTests {
	
	public String dummySchema = "txt/dummy.xsd";
	public String dummyConfig = "txt/dummy.xml";
	
	public String dummyMissingBracket = "txt/parseErrors/dummy-missing-bracket.xml";
	
	public String sqlSchema = "txt/sql.xsd";
	public String sqlConfig = "txt/sql.xml";
	
	
	private InputSource load(String fileName) throws IOException, URISyntaxException {
		StringSource ss = StringSourcePersistence.inferFromSystemResource(fileName);
		return StringSourceUriPair.InputSource(ss, fileName);
	}
	
	@Test
	public void ValidationAgainstASingleSchemaWorks() throws IOException, ParserConfigurationException, SAXException, URISyntaxException {
		XmlUtil.validateXml(load(dummySchema), load(dummyConfig));
	}
	
	@Test
	public void ValidationAgainstMultipleSchemasWorks() throws IOException, ParserConfigurationException, SAXException, URISyntaxException {
		XmlUtil.validateXml(new InputSource[] { load(dummySchema), load(sqlSchema) }, load(dummyConfig));
		XmlUtil.validateXml(new InputSource[] { load(dummySchema), load(sqlSchema) }, load(sqlConfig));
	}
	


}
