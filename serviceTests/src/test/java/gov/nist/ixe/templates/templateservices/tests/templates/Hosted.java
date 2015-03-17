package gov.nist.ixe.templates.templateservices.tests.templates;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Response;

import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;

import gov.nist.ixe.stringsource.StringSource;
import gov.nist.ixe.templates.Constants;
import gov.nist.ixe.templates.TemplateServicesClient;
import gov.nist.ixe.templates.Constants.HttpHeader;
import gov.nist.ixe.templates.templateservices.tests.HostedTemplateServicesTestFixture;
import gov.nist.ixe.templates.templateservices.tests.ITemplateServicesTestFixture;
import gov.nist.ixe.templates.tests.Examples;

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
	public void setTemplateShouldReturnCorrectHeaders() {
		String serviceName = "service0";
		
		TemplateServicesClient client = fixture.getTemplateServicesClient();
		
		client.createService(serviceName);		
		StringSource resource = Examples.TEMPLATE;
		
		Response r = client.setTemplateAsResponse(serviceName, 
				resource.getData(), "text/plain");
		assertEquals(serviceName, r.getHeaderString(HttpHeader.SERVICE_NAME));
		assertEquals(Constants.TEMPLATE_RESOURCE_NAME, r.getHeaderString(HttpHeader.RESOURCE_NAME));
		assertEquals(Constants.Rel.TEMPLATE, r.getHeaderString(HttpHeader.REL));
		
	}
	
	
}
