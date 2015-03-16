package gov.nist.ixe.templates.templateservices.tests.services;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Response;

import gov.nist.ixe.templates.Constants;
import gov.nist.ixe.templates.TemplateServicesClient;
import gov.nist.ixe.templates.templateservices.tests.HostedTemplateServicesTestFixture;
import gov.nist.ixe.templates.templateservices.tests.ITemplateServicesTestFixture;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

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
	
	@Test public void getVersionReturnsExpectedHeaders() {
		TemplateServicesClient client = fixture.getTemplateServicesClient();
		Response response = client.getVersionAsResponse();
		assertEquals(Constants.Rel.VERSION, response.getHeaderString(Constants.HttpHeader.REL));
	}
	
	
}
