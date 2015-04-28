package gov.nist.ixe.templates.templateservices.tests.rename;

import static org.junit.Assert.assertEquals;
import gov.nist.ixe.templates.Constants;
import gov.nist.ixe.templates.TemplateServicesClient;
import gov.nist.ixe.templates.TemplateServicesUtil;
import gov.nist.ixe.templates.templateservices.tests.HostedTemplateServicesTestFixture;
import gov.nist.ixe.templates.templateservices.tests.ITemplateServicesTestFixture;
import gov.nist.ixe.templates.tests.Examples;

import javax.ws.rs.core.Response;

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
	
	@Test
	public void renameServiceShouldReturnProperHeaders() {
		String serviceName = "service0";
		String newName = "service1";
		
		TemplateServicesClient ts = fixture.getTemplateServicesClient();
		ts.createService(serviceName);
		Response r = ts.renameServiceAsResponse(serviceName, newName);
		
		assertEquals(Constants.Rel.SERVICE_RENAMER, r.getHeaderString(Constants.HttpHeader.REL));
		assertEquals(serviceName, r.getHeaderString(Constants.HttpHeader.OLD_NAME));
		assertEquals(newName, r.getHeaderString(Constants.HttpHeader.NEW_NAME));
	}
	
	@Test
	public void renameConfigShouldReturnProperHeaders() {
		String serviceName = "service0";
		String resourceName = "config0";
		String newName = "config1";
		
		TemplateServicesClient ts = fixture.getTemplateServicesClient();
		ts.createService(serviceName);
		TemplateServicesUtil.setConfig(ts, serviceName, resourceName, Examples.CONFIG);
		Response r = ts.renameConfigAsResponse(serviceName, resourceName, newName);
		
		assertEquals(Constants.Rel.CONFIG_RENAMER, r.getHeaderString(Constants.HttpHeader.REL));
		assertEquals(serviceName, r.getHeaderString(Constants.HttpHeader.SERVICE_NAME));
		assertEquals(resourceName, r.getHeaderString(Constants.HttpHeader.OLD_NAME));
		assertEquals(newName, r.getHeaderString(Constants.HttpHeader.NEW_NAME));
	}
	
	@Test
	public void renameSchemaShouldReturnProperHeaders() {
		String serviceName = "service0";
		String resourceName = "schema0";
		String newName = "schema1";
		
		TemplateServicesClient ts = fixture.getTemplateServicesClient();
		ts.createService(serviceName);
		TemplateServicesUtil.setSchema(ts, serviceName, resourceName, Examples.CONFIG);
		Response r = ts.renameSchemaAsResponse(serviceName, resourceName, newName);
		
		assertEquals(Constants.Rel.SCHEMA_RENAMER, r.getHeaderString(Constants.HttpHeader.REL));
		assertEquals(serviceName, r.getHeaderString(Constants.HttpHeader.SERVICE_NAME));
		assertEquals(resourceName, r.getHeaderString(Constants.HttpHeader.OLD_NAME));
		assertEquals(newName, r.getHeaderString(Constants.HttpHeader.NEW_NAME));
	}
	
	
	
}
