package gov.nist.ixe.templates.templateservices.tests.inisplitter.roundtrips;

import static org.junit.Assert.assertEquals;
import gov.nist.ixe.stringsource.StringSource;
import gov.nist.ixe.stringsource.StringSourceConverters;
import gov.nist.ixe.stringsource.StringSourcePersistence;
import gov.nist.ixe.templates.jaxb.ServiceList;
import gov.nist.ixe.templates.templateservices.tests.TemplateServicesTests;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

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
	public void RoundTrip() throws IOException, URISyntaxException  {
		RoundTrip(iniFilename, serviceName);	
	}
	
	private void RoundTrip(String iniFileName, String serviceName) throws IOException, URISyntaxException {
		StringSource ini = StringSourcePersistence.inferFromSystemResource(iniFilename);
		ServiceList sl = ts.splitInf(ini.getData(), ini.getContentType("plain"), serviceName);
		StringSource rt = StringSourceConverters.fromResponse(ts.processDefaultTemplate(serviceName));
		assertEquals(ini.getString(), rt.getString());
		assertEquals(1, sl.servicesSize());
		assertEquals(serviceName, sl.getService(0).getName());
	}

}
