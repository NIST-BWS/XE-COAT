package gov.nist.ixe.templates.templateservices.tests.config;

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
	public void setConfigShouldReturnCorrectHeaders() {
		String serviceName = "service0";
		String resourceName = "config0";
		
		TemplateServicesClient client = fixture.getTemplateServicesClient();
		
		client.createService(serviceName);		
		StringSource resource = Examples.CONFIG;
		
		Response r = client.setConfigAsResponse(serviceName, resourceName,
				resource.getData(), resource.getContentType("xml"));
		assertEquals(serviceName, r.getHeaderString(HttpHeader.SERVICE_NAME));
		assertEquals(resourceName, r.getHeaderString(HttpHeader.RESOURCE_NAME));
		assertEquals(Constants.Rel.CONFIG, r.getHeaderString(HttpHeader.REL));
		
	}
	
	@Test
	public void deleteConfigShouldReturnCorrectHeaders() {
		String serviceName = "service0";
		String resourceName = "config0";
		
		TemplateServicesClient client = fixture.getTemplateServicesClient();
		
		client.createService(serviceName);		
		StringSource resource = Examples.CONFIG;
		
		client.setConfigAsResponse(serviceName, resourceName,
				resource.getData(), resource.getContentType("xml"));
		Response r = client.deleteConfigAsResponse(serviceName, resourceName);
		
		
		assertEquals(serviceName, r.getHeaderString(HttpHeader.SERVICE_NAME));
		assertEquals(resourceName, r.getHeaderString(HttpHeader.RESOURCE_NAME));
		assertEquals(Constants.Rel.CONFIG, r.getHeaderString(HttpHeader.REL));
		
	}
	
}
