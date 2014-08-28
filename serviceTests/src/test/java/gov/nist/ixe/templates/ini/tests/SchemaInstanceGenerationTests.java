package gov.nist.ixe.templates.ini.tests;

import gov.nist.ixe.EncodingUtil;
import gov.nist.ixe.XmlUtil;
import gov.nist.ixe.stringsource.StringSource;
import gov.nist.ixe.stringsource.StringSourcePersistence;
import gov.nist.ixe.templates.StringSourceUriPair;
import gov.nist.ixe.templates.ini.Generator;
import gov.nist.ixe.templates.ini.IniSection;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.xml.sax.SAXException;

@RunWith(value = Parameterized.class)
public class SchemaInstanceGenerationTests {

	@Parameters
	public static Collection<Object[]> TestData() {
		Object[][] data = new Object[][] {
				{ "txt/SQLConfigForSccm.ini", "txt/sql", "sql" },			
				{ "txt/dummy.ini", "txt/dummy", "dummy" }			
		};
		return Arrays.asList(data);
	}
	
	private String inputFilename, outputFileBasename, rootElement;
	
	public SchemaInstanceGenerationTests(String inputFilename, String outputFileBasename, String rootElementName) {
		this.inputFilename = inputFilename;
		this.outputFileBasename = outputFileBasename;
		this.rootElement = rootElementName;				
	}

	@Test
	public void ValidInstancesCanBeGeneratedFromIniFiles() 
			throws IOException, ParserConfigurationException, SAXException, URISyntaxException {

		ProcessTemplate(inputFilename, outputFileBasename, rootElement);
	

	}


	private void ProcessTemplate(String inputFilename, String outputFileBasename, String rootElementName) 
			throws IOException, ParserConfigurationException, SAXException, URISyntaxException {
	

		StringSource ini = StringSourcePersistence.inferFromSystemResource(inputFilename);
		String iniNewline = EncodingUtil.detectNewline(ini.getString());
		List<IniSection> iniSections = IniSection.parse(ini.getLines(iniNewline), false);

		Generator g = new Generator(rootElementName);
		StringSource instance = g.genXmlInstance(iniSections);
		StringSource schema = g.genXsdSchema(iniSections);		
		//StringSource template = g.genVelocityTemplate(ini, iniSections, iniNewline);

		//File temp = FileUtil.getRandomTempDirectory("ProcessTemplate");
		//StringSourcePersistence.save(instance, new File(temp, rootElementName + ".xml"));
		//StringSourcePersistence.save(schema, new File(temp, rootElementName + ".xsd"));
		//StringSourcePersistence.save(template, new File(temp, rootElementName + ".vm"));

		instance = instance.getCopyWithoutBOM();
		schema = schema.getCopyWithoutBOM();


		XmlUtil.validateXml(
				StringSourceUriPair.InputSource(schema, "schema"),
				StringSourceUriPair.InputSource(instance, "instance")
				);

	}
}