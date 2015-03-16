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
	
	@Test public void testConnectionViaGetReturnsExpectedHeaders() {
		TemplateServicesClient client = fixture.getTemplateServicesClient();
		Response response = client.testConnectionViaGetAsResponse();
		assertEquals(Constants.Rel.TEST_CONNECTION, response.getHeaderString(Constants.HttpHeader.REL));				
	}
	
	@Test public void testConnectionViaPostReturnsExpectedHeaders() {
		TemplateServicesClient client = fixture.getTemplateServicesClient();
		Response response = client.testConnectionViaPostAsResponse();
		assertEquals(Constants.Rel.TEST_CONNECTION, response.getHeaderString(Constants.HttpHeader.REL));				
	}
	
	@Test public void testConnectionViaDeleteReturnsExpectedHeaders() {
		TemplateServicesClient client = fixture.getTemplateServicesClient();
		Response response = client.testConnectionViaPostAsResponse();
		assertEquals(Constants.Rel.TEST_CONNECTION, response.getHeaderString(Constants.HttpHeader.REL));				
	}
	
	@Test public void getServiceListReturnsExpectedHeaders() {
		TemplateServicesClient client = fixture.getTemplateServicesClient();
		Response response = client.getServiceListAsResponse();
		assertEquals(Constants.Rel.SERVICE_LIST, response.getHeaderString(Constants.HttpHeader.REL));
	}
}
