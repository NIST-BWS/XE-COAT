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
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

public class GenerationTests {

	@Test
	public void ValidInstancesCanBeGeneratedFromIniFiles() 
			throws IOException, ParserConfigurationException, SAXException, URISyntaxException {

		ProcessTemplate("txt/SQLConfigForSccm.ini", "txt/sql", "sql");
		ProcessTemplate("txt/dummy.ini", "txt/dummy", "dummy");

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
		//StringSourceFilePersistence.save(instance, new File(temp, rootElementName + ".xml"));
		//StringSourceFilePersistence.save(schema, new File(temp, rootElementName + ".xsd"));
		//StringSourceFilePersistence.save(template, new File(temp, rootElementName + ".vm"));

		instance = instance.getCopyWithoutBOM();
		schema = schema.getCopyWithoutBOM();


		XmlUtil.ValidateXml(
				StringSourceUriPair.InputSource(schema, "schema"),
				StringSourceUriPair.InputSource(instance, "instance")
				);

	}
}