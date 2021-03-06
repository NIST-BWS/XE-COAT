package gov.nist.ixe.templates.templateservices.tests.process;

import static gov.nist.ixe.templates.TemplateServicesUtil.processTemplate;
import static gov.nist.ixe.templates.TemplateServicesUtil.setConfig;
import static gov.nist.ixe.templates.TemplateServicesUtil.setSchema;
import static gov.nist.ixe.templates.TemplateServicesUtil.setTemplate;
import static org.junit.Assert.assertEquals;
import gov.nist.ixe.stringsource.StringSource;
import gov.nist.ixe.stringsource.StringSourcePersistence;
import gov.nist.ixe.templates.BuildUri;
import gov.nist.ixe.templates.Constants;
import gov.nist.ixe.templates.Constants.HttpHeader;
import gov.nist.ixe.templates.exception.ResourceNotFoundException;
import gov.nist.ixe.templates.exception.TemplateGenerationException;
import gov.nist.ixe.templates.jaxb.Link;
import gov.nist.ixe.templates.templateservices.tests.TemplateServicesTests;
import gov.nist.ixe.templates.tests.Examples;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.Test;

public abstract class Tests extends TemplateServicesTests {

	String serviceName = "service0";
	

	@Test (expected=ResourceNotFoundException.class)
	public void processingOnAnEmptyServiceShouldThrowResourceNotFound() {

		ts.createService(serviceName);
		processTemplate(ts, serviceName, Examples.CONFIG);
	}

	@Test (expected=ResourceNotFoundException.class)
	public void processingOnAServiceThatDoesNotExistShouldThrowResourceNotFound() {
		processTemplate(ts, serviceName, Examples.CONFIG);	
	}


	@Test
	public void HavingAProcessLinkRequiresAPrimarySchemaAndDefaultConfig() {

		ts.createService(serviceName);
		ts.setTemplate(serviceName, Examples.TEMPLATE.getData(), Examples.TEMPLATE.getContentType("plain"));
		setSchema(ts, serviceName, "schema0", Examples.SCHEMA);

		List<Link> processors = ts.getServiceResources(serviceName).getProcessLinks();
		int processor_count_with_non_default_schema = processors.size();

		setSchema(ts, serviceName, Constants.PRIMARY_SCHEMA_NAME, Examples.SCHEMA);
		setConfig(ts, serviceName, Constants.DEFAULT_CONFIGURATION_NAME, Examples.CONFIG);
		processors = ts.getServiceResources(serviceName).getProcessLinks();

		int processor_count_once_default_schema_and_config_are_set = processors.size();

		assertEquals(0, processor_count_with_non_default_schema);
		assertEquals(2, processor_count_once_default_schema_and_config_are_set);

		assertLinkEquals(
				Constants.ResourceName.PROCESS, 
				Constants.Rel.PROCESS, 
				BuildUri.getProcessUri(serverRootUri, serviceName),
				processors.get(0));
		
		assertLinkEquals(
				Constants.NAMED_PROCESS_NAME_PREFIX + Constants.DEFAULT_CONFIGURATION_NAME,
				Constants.Rel.PROCESS, 
				BuildUri.getNamedProcessUri(serverRootUri, serviceName, Constants.DEFAULT_CONFIGURATION_NAME),
				processors.get(1));

		assertEquals(Constants.ResourceName.PROCESS, processors.get(0).getName());			
		assertEquals(Constants.Rel.PROCESS, processors.get(0).getRel());
		assertEquals(BuildUri.getProcessUri(serverRootUri, serviceName), processors.get(0).getUri());			

	}



	@Test
	public void HavingConfigurationsYieldsNamedProcessLinks() {

		// TO DO: This is an extremely fragile test
		String config0Name = "config0";
		String config1Name = "default.xml";
		String serviceName = "service0";
		String schemaName =  "schema0";

		ts.createService(serviceName);
		ts.setTemplate(serviceName, Examples.TEMPLATE.getData(), Examples.TEMPLATE.getContentType("plain"));
		setSchema(ts, serviceName, Constants.PRIMARY_SCHEMA_NAME, Examples.SCHEMA);
		setSchema(ts, serviceName, schemaName, Examples.SCHEMA);
		setConfig(ts, serviceName, config0Name, Examples.CONFIG);
		setConfig(ts, serviceName, config1Name, Examples.CONFIG);

		List<Link> resources = ts.getServiceResources(serviceName).getResources();
		assertEquals(8, resources.size());

		String rootUri = serverRootUri;
		assertLinkEquals(config0Name, 
				Constants.Rel.CONFIG, 
				BuildUri.getConfigUri(rootUri, serviceName, config0Name),
				resources.get(0));

		assertLinkEquals(config1Name, 
				Constants.Rel.CONFIG, 
				BuildUri.getConfigUri(rootUri, serviceName, config1Name),
				resources.get(1));
		
		assertLinkEquals(Constants.ResourceName.PROCESS, 
				Constants.Rel.PROCESS, 
				BuildUri.getProcessUri(rootUri, serviceName),
				resources.get(2));
		assertLinkEquals(Constants.NAMED_PROCESS_NAME_PREFIX + config0Name, 
				Constants.Rel.PROCESS, 
				BuildUri.getNamedProcessUri(rootUri, serviceName, config0Name),
				resources.get(3));
		assertLinkEquals(Constants.NAMED_PROCESS_NAME_PREFIX + config1Name,  
				Constants.Rel.PROCESS, 
				BuildUri.getNamedProcessUri(rootUri, serviceName, config1Name),
				resources.get(4));


		assertLinkEquals(Constants.PRIMARY_SCHEMA_NAME, 
				Constants.Rel.SCHEMA, 
				BuildUri.getSchemaUri(rootUri, serviceName, Constants.PRIMARY_SCHEMA_NAME),
				resources.get(5));


		assertLinkEquals(schemaName,
				Constants.Rel.SCHEMA, 
				BuildUri.getSchemaUri(rootUri, serviceName, schemaName),
				resources.get(6));

		assertLinkEquals(Constants.ResourceName.TEMPLATE,
				Constants.Rel.TEMPLATE, 
				BuildUri.getTemplateUri(rootUri, serviceName),
				resources.get(7));
	}
	
	@Test
	public void NamedProcessShouldReturnCorrectHeaders() throws IOException, URISyntaxException {

		StringSource template = StringSourcePersistence.inferFromSystemResource("txt/dummy.vm");
		StringSource schema = StringSourcePersistence.inferFromSystemResource("txt/dummy.xsd");
		StringSource config = StringSourcePersistence.inferFromSystemResource("txt/dummy.xml");
		
		ts.createService(serviceName);
		setTemplate(ts, serviceName, template);
		setConfig(ts, serviceName, "named.xml", config);
		setSchema(ts, serviceName, "main.xsd", schema);
		
		javax.ws.rs.core.Response r = ts.processTemplateByName(serviceName, "named.xml");
		assertEquals(serviceName, r.getHeaderString(HttpHeader.SERVICE_NAME));
		assertEquals("process/named.xml", r.getHeaderString(HttpHeader.RESOURCE_NAME));
		assertEquals("process", r.getHeaderString(HttpHeader.REL));
	}
	
	@Test
	public void DefaultProcessShouldReturnCorrectHeaders() throws IOException, URISyntaxException {

		StringSource template = StringSourcePersistence.inferFromSystemResource("txt/dummy.vm");
		StringSource schema = StringSourcePersistence.inferFromSystemResource("txt/dummy.xsd");
		StringSource config = StringSourcePersistence.inferFromSystemResource("txt/dummy.xml");
		
		ts.createService(serviceName);
		setTemplate(ts, serviceName, template);
		setConfig(ts, serviceName, "default.xml", config);
		setSchema(ts, serviceName, "main.xsd", schema);
		
		javax.ws.rs.core.Response r = ts.processDefaultTemplate(serviceName);
		assertEquals(serviceName, r.getHeaderString(HttpHeader.SERVICE_NAME));
		assertEquals("process", r.getHeaderString(HttpHeader.RESOURCE_NAME));
		assertEquals("process", r.getHeaderString(HttpHeader.REL));
	}
	
	@Test
	public void ProcessViaPayloadShouldReturnCorrectHeaders() throws IOException, URISyntaxException {

		StringSource template = StringSourcePersistence.inferFromSystemResource("txt/dummy.vm");
		StringSource schema = StringSourcePersistence.inferFromSystemResource("txt/dummy.xsd");
		StringSource config = StringSourcePersistence.inferFromSystemResource("txt/dummy.xml");
		
		ts.createService(serviceName);
		setTemplate(ts, serviceName, template);
		setSchema(ts, serviceName, "main.xsd", schema);
		
		javax.ws.rs.core.Response r = ts.processTemplate(serviceName, config.getData(), config.getContentType("xml"));
		assertEquals(serviceName, r.getHeaderString(HttpHeader.SERVICE_NAME));
		assertEquals("process/process", r.getHeaderString(HttpHeader.RESOURCE_NAME));
		assertEquals("process", r.getHeaderString(HttpHeader.REL));
	}
	

	@Test (expected=TemplateGenerationException.class)
	public void MultipleRootElementsInMainSchemaAreForbidden() throws IOException, URISyntaxException  {
		
			String configName = "default.xml";
			String schemaName = "main.xsd";
			StringSource template = StringSourcePersistence.inferFromSystemResource("txt/dummy.vm");
			StringSource schema = StringSourcePersistence.inferFromSystemResource("txt/multi-element.xsd");
			StringSource config = StringSourcePersistence.inferFromSystemResource("txt/dummy.xml");
			
			ts.createService(serviceName);
			setTemplate(ts, serviceName, template);
			setConfig(ts, serviceName, configName, config);
			setSchema(ts, serviceName, schemaName, schema);
			
			@SuppressWarnings("unused")
			StringSource result = processTemplate(ts, serviceName);
	}
	
	@Test (expected=TemplateGenerationException.class)
	public void XsdFilesMustBeSchemas() throws IOException, URISyntaxException  {
		
			String configName = "default.xml";
			String schemaName = "main.xsd";
			StringSource template = StringSourcePersistence.inferFromSystemResource("txt/dummy.vm");
			StringSource schema = StringSourcePersistence.inferFromSystemResource("txt/dummy.xml");
			StringSource config = StringSourcePersistence.inferFromSystemResource("txt/dummy.xml");
			
			ts.createService(serviceName);
			setTemplate(ts, serviceName, template);
			setConfig(ts, serviceName, configName, config);
			setSchema(ts, serviceName, schemaName, schema);
			
			@SuppressWarnings("unused")
			StringSource result = processTemplate(ts, serviceName);
	}


}
