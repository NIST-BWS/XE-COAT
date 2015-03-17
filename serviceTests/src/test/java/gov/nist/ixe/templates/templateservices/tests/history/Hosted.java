package gov.nist.ixe.templates.templateservices.tests.history;

import static gov.nist.ixe.templates.TemplateServicesUtil.setTemplate;
import static org.junit.Assert.assertEquals;

import java.text.ParseException;

import javax.ws.rs.core.Response;

import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;

import gov.nist.ixe.templates.Constants;
import gov.nist.ixe.templates.TemplateServicesClient;
import gov.nist.ixe.templates.TemplateServicesUtil;
import gov.nist.ixe.templates.exception.ResourceNotFoundException;
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
	public void getTemplateHistoryShouldReturnProperHeaders() {
		String serviceName = "service0";
		
		TemplateServicesClient ts = fixture.getTemplateServicesClient();
		ts.createService(serviceName);
		ts.setTemplate(serviceName, Examples.TEMPLATE.getData(),
				Examples.TEMPLATE.getContentType("plain"));
		
		Response r = ts.getTemplateHistoryAsResponse(serviceName);
		assertEquals(Constants.Rel.TEMPLATE_HISTORY, r.getHeaderString(Constants.HttpHeader.REL));
		assertEquals(serviceName, r.getHeaderString(Constants.HttpHeader.SERVICE_NAME));
		assertEquals(Constants.TEMPLATE_RESOURCE_NAME, r.getHeaderString(Constants.HttpHeader.RESOURCE_NAME));
	}
	
	
	@Test
	public void getConfigHistoryShouldReturnProperHeaders() {
		String serviceName = "service0";
		String resourceName = "config0";
		
		TemplateServicesClient ts = fixture.getTemplateServicesClient();
		ts.createService(serviceName);
		TemplateServicesUtil.setConfig(ts, serviceName, resourceName, Examples.CONFIG);
		
		Response r = ts.getConfigHistoryAsResponse(serviceName, resourceName);
		assertEquals(Constants.Rel.CONFIG_HISTORY, r.getHeaderString(Constants.HttpHeader.REL));
		assertEquals(serviceName, r.getHeaderString(Constants.HttpHeader.SERVICE_NAME));
		assertEquals(resourceName, r.getHeaderString(Constants.HttpHeader.RESOURCE_NAME));
	}
	
	@Test
	public void getSchemaHistoryShouldReturnProperHeaders() {
		String serviceName = "service0";
		String resourceName = "schema0";
		
		TemplateServicesClient ts = fixture.getTemplateServicesClient();
		ts.createService(serviceName);
		TemplateServicesUtil.setSchema(ts, serviceName, resourceName, Examples.CONFIG);
		
		Response r = ts.getSchemaHistoryAsResponse(serviceName, resourceName);
		assertEquals(Constants.Rel.SCHEMA_HISTORY, r.getHeaderString(Constants.HttpHeader.REL));
		assertEquals(serviceName, r.getHeaderString(Constants.HttpHeader.SERVICE_NAME));
		assertEquals(resourceName, r.getHeaderString(Constants.HttpHeader.RESOURCE_NAME));
	}
	
	@Test
	public void templateHistoryShouldReturnCorrectHeaders() 
			throws ResourceNotFoundException, ParseException {
		
		TemplateServicesClient ts = fixture.getTemplateServicesClient();
		ts.createService(serviceName);
		setTemplate(ts, serviceName, Examples.DUMMY0);
		setTemplate(ts, serviceName, Examples.DUMMY1);			
		Response r = ts.getTemplateHistoryAsResponse(serviceName);
		
		assertEquals(serviceName, r.getHeaderString(Constants.HttpHeader.SERVICE_NAME));
		assertEquals(Constants.Rel.TEMPLATE_HISTORY, r.getHeaderString(Constants.HttpHeader.REL));
		assertEquals(Constants.TEMPLATE_RESOURCE_NAME, r.getHeaderString(Constants.HttpHeader.RESOURCE_NAME));
	}
	
	@Test
	public void schemaHistoryShouldReturnCorrectHeaders() 
			throws ResourceNotFoundException, ParseException {
		
		String serviceName = "service0";
		String resourceName = "schema0";
		
		TemplateServicesClient ts = fixture.getTemplateServicesClient();
		ts.createService(serviceName);
		
		
		TemplateServicesUtil.setSchema(ts, serviceName, resourceName, Examples.DUMMY0);
		TemplateServicesUtil.setSchema(ts, serviceName, resourceName, Examples.DUMMY1);			
		Response r = ts.getSchemaHistoryAsResponse(serviceName, resourceName);
		
		assertEquals(serviceName, r.getHeaderString(Constants.HttpHeader.SERVICE_NAME));
		assertEquals(Constants.Rel.SCHEMA_HISTORY, r.getHeaderString(Constants.HttpHeader.REL));
		assertEquals(resourceName, r.getHeaderString(Constants.HttpHeader.RESOURCE_NAME));
	}
	
	@Test
	public void configHistoryShouldReturnCorrectHeaders() 
			throws ResourceNotFoundException, ParseException {
		
		String serviceName = "service0";
		String resourceName = "config0";
		
		TemplateServicesClient ts = fixture.getTemplateServicesClient();
		ts.createService(serviceName);
		
		
		TemplateServicesUtil.setConfig(ts, serviceName, resourceName, Examples.DUMMY0);
		TemplateServicesUtil.setConfig(ts, serviceName, resourceName, Examples.DUMMY1);			
		Response r = ts.getConfigHistoryAsResponse(serviceName, resourceName);
		
		assertEquals(serviceName, r.getHeaderString(Constants.HttpHeader.SERVICE_NAME));
		assertEquals(Constants.Rel.CONFIG_HISTORY, r.getHeaderString(Constants.HttpHeader.REL));
		assertEquals(resourceName, r.getHeaderString(Constants.HttpHeader.RESOURCE_NAME));
	}
	
}
