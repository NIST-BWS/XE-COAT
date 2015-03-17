package gov.nist.ixe.templates.templateservices.tests.upload;

import static org.junit.Assert.assertEquals;
import gov.nist.ixe.templates.Constants;
import gov.nist.ixe.templates.TemplateServicesClient;
import gov.nist.ixe.templates.templateservices.tests.HostedTemplateServicesTestFixture;
import gov.nist.ixe.templates.templateservices.tests.ITemplateServicesTestFixture;
import gov.nist.ixe.templates.tests.Examples;

import java.io.UnsupportedEncodingException;

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
	public void uploadTemplateShouldReturnCorrectHeaders() throws UnsupportedEncodingException { 
		
		TemplateServicesClient ts = fixture.getTemplateServicesClient();
		
		ts.createService(serviceName);
		
		String contentSubType = Constants.ContentType.SubType.PLAIN;
		Response r = ts.uploadAsResponse(serviceName, Constants.Rel.TEMPLATE, Constants.ResourceName.TEMPLATE,
				Examples.DUMMY1.getData(), Examples.DUMMY1.getContentType(contentSubType));
		
		assertEquals(Constants.Rel.UPLOAD, r.getHeaderString(Constants.HttpHeader.REL));
		assertEquals(Constants.ResourceName.TEMPLATE, r.getHeaderString(Constants.HttpHeader.RESOURCE_NAME));
		assertEquals(serviceName, r.getHeaderString(Constants.HttpHeader.SERVICE_NAME));
		assertEquals(Constants.Rel.TEMPLATE, r.getHeaderString(Constants.HttpHeader.UPLOADED_REL));
		
	}
	
	@Test
	public void uploadSchemaShouldReturnCorrectHeaders() throws UnsupportedEncodingException { 
		
		TemplateServicesClient ts = fixture.getTemplateServicesClient();
		
		ts.createService(serviceName);
		
		String resourceName = "schema0";
		
		String contentSubType = Constants.ContentType.SubType.XML;
		Response r = ts.uploadAsResponse(serviceName, Constants.Rel.SCHEMA, resourceName,
				Examples.DUMMY1.getData(), Examples.DUMMY1.getContentType(contentSubType));
		
		assertEquals(Constants.Rel.UPLOAD, r.getHeaderString(Constants.HttpHeader.REL));
		assertEquals(resourceName, r.getHeaderString(Constants.HttpHeader.RESOURCE_NAME));
		assertEquals(serviceName, r.getHeaderString(Constants.HttpHeader.SERVICE_NAME));
		assertEquals(Constants.Rel.SCHEMA, r.getHeaderString(Constants.HttpHeader.UPLOADED_REL));
		
	}
	
	@Test
	public void uploadConfigShouldReturnCorrectHeaders() throws UnsupportedEncodingException { 
		
		TemplateServicesClient ts = fixture.getTemplateServicesClient();
		
		ts.createService(serviceName);
		
		String resourceName = "config0";
		
		String contentSubType = Constants.ContentType.SubType.XML;
		Response r = ts.uploadAsResponse(serviceName, Constants.Rel.CONFIG, resourceName,
				Examples.DUMMY1.getData(), Examples.DUMMY1.getContentType(contentSubType));
		
		assertEquals(Constants.Rel.UPLOAD, r.getHeaderString(Constants.HttpHeader.REL));
		assertEquals(resourceName, r.getHeaderString(Constants.HttpHeader.RESOURCE_NAME));
		assertEquals(serviceName, r.getHeaderString(Constants.HttpHeader.SERVICE_NAME));
		assertEquals(Constants.Rel.CONFIG, r.getHeaderString(Constants.HttpHeader.UPLOADED_REL));
		
	}
	
}
