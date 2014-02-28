package gov.nist.ixe.templates.templateservices.tests.upload;

import static gov.nist.ixe.templates.TemplateServicesUtil.upload;
import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;

import gov.nist.ixe.stringsource.StringSource;
import gov.nist.ixe.stringsource.StringSourceConverters;
import gov.nist.ixe.templates.BuildUri;
import gov.nist.ixe.templates.Constants;
import gov.nist.ixe.templates.jaxb.ResourceList;
import gov.nist.ixe.templates.templateservices.tests.TemplateServicesTests;
import gov.nist.ixe.templates.tests.Examples;

import org.junit.Test;

public abstract class Tests extends TemplateServicesTests {
	
	String serviceName = "s0";
	@Test
	public void templatesCanBeSetViaUpload() throws UnsupportedEncodingException {
		
		ts.createService(serviceName);
		upload(ts, serviceName, Constants.Rel.TEMPLATE, "ingored", Examples.DUMMY1);
		StringSource ss = StringSourceConverters.fromResponse(ts.getTemplate(serviceName));
		assertEquals(Examples.DUMMY1.getString(), ss.getString());
	}
	
	@Test
	public void schemasCanBeSetViaUpload() throws UnsupportedEncodingException {
		
		ts.createService(serviceName);
		upload(ts, serviceName, Constants.Rel.SCHEMA, "s0", Examples.DUMMY1);
		StringSource ss = StringSourceConverters.fromResponse(ts.getSchema(serviceName, "s0"));
		assertEquals(Examples.DUMMY1.getString(), ss.getString());
	}
	
	@Test
	public void configsCanBeSetViaUpload() throws UnsupportedEncodingException {
		
		ts.createService(serviceName);
		upload(ts, serviceName, Constants.Rel.CONFIG, "c0", Examples.DUMMY1);
		StringSource ss = StringSourceConverters.fromResponse(ts.getConfig(serviceName, "c0"));
		assertEquals(Examples.DUMMY1.getString(), ss.getString());
	}
	
	@Test
	public void uploadGivesTheCorrectLinkToANewTemplate() throws UnsupportedEncodingException { 
		
		ts.createService(serviceName);
		ResourceList rl = upload(ts, serviceName, Constants.Rel.TEMPLATE, "ignored", Examples.DUMMY1);
		assertEquals(1, rl.getResources().size());
		assertLinkEquals(
				Constants.TEMPLATE_RESOURCE_NAME, 
				Constants.Rel.TEMPLATE, 
				BuildUri.getTemplateUri(serverRootUri, serviceName),
				rl.getResources().get(0));
	}
	
	@Test
	public void uploadGivesTheCorrectLinkToANewConfig() throws UnsupportedEncodingException { 
		
		String resourceName = "r0";
		ts.createService(serviceName);
		ResourceList rl = upload(ts, serviceName, Constants.Rel.CONFIG, resourceName, Examples.DUMMY1);
		assertEquals(1, rl.getResources().size());
		assertLinkEquals(
				resourceName, 
				Constants.Rel.CONFIG, 
				BuildUri.getConfigUri(serverRootUri, serviceName, resourceName),
				rl.getResources().get(0));
	}
	
	@Test
	public void uploadGivesTheCorrectLinkToANewSchema() throws UnsupportedEncodingException { 
		
		String serviceName = "s0"; String resourceName = "r0";
		ts.createService(serviceName);
		ResourceList rl = upload(ts, serviceName, Constants.Rel.SCHEMA, resourceName, Examples.DUMMY1);
		assertEquals(1, rl.getResources().size());
		assertLinkEquals(
				resourceName, 
				Constants.Rel.SCHEMA, 
				BuildUri.getSchemaUri(serverRootUri, serviceName, resourceName),
				rl.getResources().get(0));
	}

}
