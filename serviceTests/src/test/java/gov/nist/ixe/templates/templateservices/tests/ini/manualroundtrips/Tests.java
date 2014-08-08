package gov.nist.ixe.templates.templateservices.tests.ini.manualroundtrips;

import static org.junit.Assert.assertEquals;
import gov.nist.ixe.EncodingUtil;
import gov.nist.ixe.XmlUtil;
import gov.nist.ixe.stringsource.StringSource;
import gov.nist.ixe.stringsource.StringSourceConverters;
import gov.nist.ixe.stringsource.StringSourcePersistence;
import gov.nist.ixe.templates.StringSourceUriPair;
import gov.nist.ixe.templates.ini.Generator;
import gov.nist.ixe.templates.ini.IniSection;
import gov.nist.ixe.templates.templateservices.tests.TemplateServicesTests;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.xml.sax.SAXException;

@RunWith(value = Parameterized.class)
public abstract class Tests extends TemplateServicesTests {
	
	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = getRoundTripFileServiceNamePairs();
		return Arrays.asList(data);
	}
	
	private String iniFilename;
	private String serviceName;
	public Tests(String iniFilename, String serviceName) {
		this.iniFilename = iniFilename;
		this.serviceName = serviceName;
	}	

	
	@Test 
	public void RoundTrip() throws IOException, ParserConfigurationException, SAXException, URISyntaxException {
		RoundTrip(iniFilename, serviceName);	
	}
	
	//private String dir = FileUtil.getRandomTempDirectoryName("IniRoundtripTests");
	private String dir = "C:\\tmp\\out";

	@SuppressWarnings("unused")
	private void save(StringSource ss, String serviceName, String suffix) throws IOException {
		(new File(dir)).mkdirs();
		File f = new File(dir, serviceName + "." + suffix);
		f.delete(); f.delete();
		StringSourcePersistence.save(ss, f);
	}
	
	private void RoundTrip(String iniFilename, String serviceName) throws IOException, ParserConfigurationException, SAXException, URISyntaxException {
		
		StringSource ini = StringSourcePersistence.inferFromSystemResource(iniFilename);
		String newline = EncodingUtil.detectNewline(ini.getString());
		List<String> iniText = ini.getLines(newline);
					
		List<IniSection> iniSections = IniSection.parse(iniText, false);
		
		Generator g = new Generator(serviceName);
		StringSource config = g.genXmlInstance(iniSections);
		StringSource schema = g.genXsdSchema(iniSections);		
		StringSource template = g.genVelocityTemplate(ini, iniSections, newline);
		
		save(template, serviceName, "vm");
		save(config, serviceName, "xml");
		save(schema, serviceName, "xsd");
		save(ini, serviceName, "ini");
		
		XmlUtil.ValidateXml(
				StringSourceUriPair.InputSource(schema, "schema"), 
				StringSourceUriPair.InputSource(config, "config")
				);
			
		
		ts.createService(serviceName);
		
		ts.setSchema(serviceName, "main.xsd", schema.getData(), schema.getContentType("xml"));
		ts.setTemplate(serviceName, template.getData(), template.getContentType("plain"));
		ts.setConfig(serviceName, "default.xml", config.getData(), config.getContentType("xml"));
		
			
		Response processResponse = ts.processDefaultTemplate(serviceName);
		StringSource roundTrip = StringSourceConverters.fromResponse(processResponse);
		//save(roundTrip, serviceName, "rt.ini");
		assertEquals(ini.getString(), roundTrip.getString());
	
		
	}

}
