package gov.nist.ixe.templates.templateservices.tests.inisplitter;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.ws.rs.core.Response;

import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;

import gov.nist.ixe.stringsource.StringSource;
import gov.nist.ixe.stringsource.StringSourcePersistence;
import gov.nist.ixe.templates.Constants;
import gov.nist.ixe.templates.TemplateServicesClient;
import gov.nist.ixe.templates.jaxb.ServiceList;
import gov.nist.ixe.templates.templateservices.tests.HostedTemplateServicesTestFixture;
import gov.nist.ixe.templates.templateservices.tests.ITemplateServicesTestFixture;

public class Hosted extends Tests {

	private HostedTemplateServicesTestFixture fixture;
		
	public ITemplateServicesTestFixture getTestFixture() {
		if (fixture == null) {
			fixture = new HostedTemplateServicesTestFixture();
		}
		return fixture;
	}
	
	@BeforeClass public static void beforeClass() {
		HostedTemplateServicesTestFixture.beforeClass();
	}
	
	
	@AfterClass public static void afterClass() {
		HostedTemplateServicesTestFixture.afterClass();
	}
	
	@Test
	public void iniSplitterShouldReturnCorrectHeaders() throws IOException, URISyntaxException {
		TemplateServicesClient ts = fixture.getTemplateServicesClient();
		StringSource ini = StringSourcePersistence.inferFromSystemResource("txt/roundtrip/dummy.ini");
		Response r = ts.splitInfAsResponse(ini.getData(), ini.getContentType("plain"), "dummy");
		
		assertEquals(Constants.Rel.INI_SPLITTER, r.getHeaderString(Constants.HttpHeader.REL));
		assertEquals(Constants.ResourceName.INI_SPLITTER, r.getHeaderString(Constants.HttpHeader.RESOURCE_NAME));
		assertEquals("dummy", r.getHeaderString(Constants.HttpHeader.SERVICE_NAME));		
		
		
	}
}
