package gov.nist.ixe.templates.templateservices.tests.parsing;

import static gov.nist.ixe.templates.TemplateServicesUtil.processTemplate;
import static gov.nist.ixe.templates.TemplateServicesUtil.setConfig;
import static gov.nist.ixe.templates.TemplateServicesUtil.setSchema;
import static gov.nist.ixe.templates.TemplateServicesUtil.setTemplate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nist.ixe.stringsource.StringSource;
import gov.nist.ixe.stringsource.StringSourcePersistence;
import gov.nist.ixe.templates.BuildUri;
import gov.nist.ixe.templates.Constants;
import gov.nist.ixe.templates.exception.TemplateGenerationException;
import gov.nist.ixe.templates.jaxb.ParseError;
import gov.nist.ixe.templates.templateservices.tests.TemplateServicesTests;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public abstract class Tests extends TemplateServicesTests {

	public String empty = "txt/parseErrors/empty.txt";
	public String okTemplate = "txt/parseErrors/dummy.vm";
	public String okConfig = "txt/parseErrors/dummy.xml";
	public String okSchema = "txt/parseErrors/dummy.xsd";
	public String okOutput = "txt/parseErrors/dummy.ini";
	
	public String schemaMissingBracket = "txt/parseErrors/dummy-missing-bracket.xsd";
	public String schemaMissingTypeDef = "txt/parseErrors/dummy-missing-type.xsd";
	public String schemaMultiRoot = "txt/parseErrors/dummy-multi-root.xsd";
	public String schemaNoRoot = "txt/parseErrors/dummy-no-root.xsd";
	
	public String configMissingBracket = "txt/parseErrors/dummy-missing-bracket.xml";
	public String configMissingType = "txt/parseErrors/dummy-missing-type.xml";
	
	public String templateMissingBracket = "txt/parseErrors/dummy-missing-bracket.vm";
			
	public String serviceName = "parseTests";
	
	enum ProcessStyle {
		Default, Named, Posted
	}
	
	String configName = "default.xml";
	String schemaName = "main.xsd";
	
	@Parameters
	public static Collection<Object[]> testParameters() {
		Object[][] data = new Object[][] {
				{ProcessStyle.Default, "process/default.xml"},
				{ProcessStyle.Named, "process/default.xml"},
				{ProcessStyle.Posted, "process/process"}
		};
		return Arrays.asList(data);
	}
	
	public ProcessStyle style;
	public String resourceName;
	public Tests(ProcessStyle style, String resourceName) {
		this.style = style;
		this.resourceName = resourceName;
	}
	
	@Test
	public void GetTemplateCanWorkCorrectly() throws IOException, URISyntaxException {
		StringSource result = getTemplate(okTemplate, okSchema, okConfig, style);
		StringSource expected = StringSourcePersistence.inferFromSystemResource(okOutput);
		assertEquals(expected.getString().trim(), result.getString().trim());
	}
	
	
	private TemplateGenerationException getTemplateGenerationException(String templateFilename, 
			String schemaFilename, 
			String configFilename,
			ProcessStyle style) throws IOException, URISyntaxException {
		TemplateGenerationException result = null;
		try {
			getTemplate(templateFilename, schemaFilename, configFilename, style);			
		} catch (TemplateGenerationException tge) {
			result = tge;			
		} 
		return result;
	}
	
	private StringSource getTemplate(
			String templateFilename, 
			String schemaFilename, 
			String configFilename,
			ProcessStyle style) throws IOException, URISyntaxException {
		
		StringSource template = StringSourcePersistence.inferFromSystemResource(templateFilename);
		StringSource schema = StringSourcePersistence.inferFromSystemResource(schemaFilename);
		StringSource config = StringSourcePersistence.inferFromSystemResource(configFilename);
		
		ts.createService(serviceName);
		setTemplate(ts, serviceName, template);
		setConfig(ts, serviceName, configName, config);
		setSchema(ts, serviceName, schemaName, schema);
		
		StringSource result = null;
		if (style == ProcessStyle.Default) {
			result = processTemplate(ts, serviceName);
		}
		
		if (style == ProcessStyle.Named) {
			result = processTemplate(ts, serviceName, configName);
		}
		
		if (style == ProcessStyle.Posted) {
			result = processTemplate(ts, serviceName, config);
		}
		
		return result;
	}
			
	
	@Test
	public void AEmptySchemaFileGivesTheExpectedParseError() throws IOException, URISyntaxException {
		TemplateGenerationException tge = getTemplateGenerationException(okTemplate, empty, okConfig, style);
		List<ParseError> errors = tge.getParseErrors();
		
		assertEquals(serviceName, tge.getServiceName());
		assertEquals(resourceName, tge.getResourceName());
		
		assertEquals(1, errors.size());
		assertEquals("Premature end of file.", errors.get(0).getMessage());
		assertEquals(1, errors.get(0).getLineNumber());
		assertEquals(1, errors.get(0).getColumnNumber());
				
		assertLinkEquals(schemaName, 
				Constants.Rel.SCHEMA, 
				BuildUri.getSchemaUri(serverRootUri, serviceName, schemaName),
				errors.get(0).getLink());
		
	}
	
	@Test
	public void ASchemaWithAMissingAngleBracketGivesTheExpectedParseError() throws IOException, URISyntaxException {
		
		TemplateGenerationException tge = getTemplateGenerationException(okTemplate, schemaMissingBracket, okConfig, style);
		List<ParseError> errors = tge.getParseErrors();
		
		assertEquals(serviceName, tge.getServiceName());
		assertEquals(resourceName, tge.getResourceName());
		
		
		assertEquals(1, errors.size());
		assertEquals("Element type \"xs:element\" must be followed by either attribute specifications, \">\" or \"/>\".",
				errors.get(0).getMessage());
		assertEquals(48, errors.get(0).getLineNumber());
		assertEquals(57, errors.get(0).getColumnNumber());
		
		assertLinkEquals(schemaName, 
				Constants.Rel.SCHEMA, 
				BuildUri.getSchemaUri(serverRootUri, serviceName, schemaName),
				errors.get(0).getLink());
		
	}
	
	@Test
	public void ASchemaWithAMissingTypeDefinitionGivesTheExpectedParseError() throws IOException, URISyntaxException {
		TemplateGenerationException tge = getTemplateGenerationException(okTemplate, schemaMissingTypeDef, okConfig, style);
		List<ParseError> errors = tge.getParseErrors();
		
		assertEquals(serviceName, tge.getServiceName());
		assertEquals(resourceName, tge.getResourceName());
		
		//List<ParseError> errors = getErrors(okTemplate, schemaMissingTypeDef, okConfig, style);
		
		assertEquals(1, errors.size());
		assertEquals("undefined simple or complex type 'coat:automobilesType'",
				errors.get(0).getMessage());
		assertEquals(15, errors.get(0).getLineNumber());
		assertEquals(67, errors.get(0).getColumnNumber());
		
		assertLinkEquals(schemaName, 
				Constants.Rel.SCHEMA, 
				BuildUri.getSchemaUri(serverRootUri, serviceName, schemaName),
				errors.get(0).getLink());
		
	}

	@Test
	public void AnEmptyConfigFileGivesTheExpectedParseError() throws IOException, URISyntaxException {
		TemplateGenerationException tge = getTemplateGenerationException(okTemplate, okSchema, empty, style);
		List<ParseError> errors = tge.getParseErrors();
		
		assertEquals(serviceName, tge.getServiceName());
		assertEquals(resourceName, tge.getResourceName());
		//List<ParseError> errors = getErrors(okTemplate, okSchema, empty, style);
		
		assertEquals(1, errors.size());
		assertEquals(1, errors.get(0).getLineNumber());
		assertEquals(1, errors.get(0).getColumnNumber());
		assertEquals("Premature end of file.", errors.get(0).getMessage());
		
		String expectedUri = BuildUri.getConfigUri(serverRootUri, serviceName, configName);
		String expectedRel = Constants.Rel.CONFIG;
		String expectedName = configName;
		if (style == ProcessStyle.Posted) {
			expectedUri = BuildUri.getProcessPayloadUri(serverRootUri, serviceName);
			expectedRel = Constants.Rel.PROCESS;
			expectedName = Constants.PROCESS_RESOURCE_NAME;
		}
		assertLinkEquals(expectedName, expectedRel, expectedUri, errors.get(0).getLink());
	}
	
	@Test
	public void AConfigFileWithAMissingBracketGivesTheExpectedParseError() throws IOException, URISyntaxException {
		
		TemplateGenerationException tge = getTemplateGenerationException(okTemplate, okSchema, configMissingBracket, style);
		List<ParseError> errors = tge.getParseErrors();
		
		assertEquals(serviceName, tge.getServiceName());
		assertEquals(resourceName, tge.getResourceName());
		//List<ParseError> errors = getErrors(okTemplate, okSchema, configMissingBracket, style);
		
		assertEquals(1, errors.size());
		
		assertEquals("The end-tag for element type \"element\" must end with a '>' delimiter.", errors.get(0).getMessage());
		assertEquals(19, errors.get(0).getLineNumber());
		assertEquals(3, errors.get(0).getColumnNumber());
		
		
		String expectedUri = BuildUri.getConfigUri(serverRootUri, serviceName, configName);
		String expectedRel = Constants.Rel.CONFIG;
		String expectedName = configName;
		if (style == ProcessStyle.Posted) {
			expectedUri = BuildUri.getProcessPayloadUri(serverRootUri, serviceName);
			expectedRel = Constants.Rel.PROCESS;
			expectedName = Constants.PROCESS_RESOURCE_NAME;
		}
		assertLinkEquals(expectedName, expectedRel, expectedUri, errors.get(0).getLink());
	}
	
	@Test
	public void AConfigFileWithAMissingTypeGivesTheExpectedParseError() throws IOException, URISyntaxException {
		
		TemplateGenerationException tge = getTemplateGenerationException(okTemplate, okSchema, configMissingType, style);
		List<ParseError> errors = tge.getParseErrors();
		
		assertEquals(serviceName, tge.getServiceName());
		assertEquals(resourceName, tge.getResourceName());
		//List<ParseError> errors = getErrors(okTemplate, okSchema, configMissingType, style);
		
		String expectedMessage = "cvc-complex-type.2.4.a: Invalid content was found starting with element 'automobiles'. One of '{\"http://xe.nist.gov/coat\":trains}' is expected.";
		
		// Number of errors
		assertEquals(1, errors.size());
		
		// Location of error
		assertEquals(expectedMessage, errors.get(0).getMessage());
		assertEquals(12, errors.get(0).getLineNumber());
		assertEquals(15, errors.get(0).getColumnNumber());
		
		// Link to error
		String expectedUri = BuildUri.getConfigUri(serverRootUri, serviceName, configName);
		String expectedRel = Constants.Rel.CONFIG;
		String expectedName = configName;
		if (style == ProcessStyle.Posted) {
			expectedUri = BuildUri.getProcessPayloadUri(serverRootUri, serviceName);
			expectedRel = Constants.Rel.PROCESS;
			expectedName = Constants.PROCESS_RESOURCE_NAME;
		}
		assertLinkEquals(expectedName, expectedRel, expectedUri, errors.get(0).getLink());
	}	
	
	@Test
	public void ATemplateWithAMissingBracketGivesTheExpectedParseError() throws IOException, URISyntaxException {
		TemplateGenerationException tge = getTemplateGenerationException(templateMissingBracket, okSchema, okConfig, style);
		List<ParseError> errors = tge.getParseErrors();
		
		assertEquals(serviceName, tge.getServiceName());
		assertEquals(resourceName, tge.getResourceName());
	//	List<ParseError> errors = getErrors(templateMissingBracket, okSchema, okConfig, style);
		
		assertEquals(1, errors.size());
		assertEquals(7, errors.get(0).getLineNumber());
	}	
	
	
	@Test
	public void MultipleRootElementsInTheMainSchemaGivesTheExpectedParseError()  throws IOException, URISyntaxException {
		TemplateGenerationException tge = getTemplateGenerationException(okTemplate, schemaMultiRoot, okConfig, style);
		List<ParseError> errors = tge.getParseErrors();
		
		assertEquals(serviceName, tge.getServiceName());
		assertEquals(resourceName, tge.getResourceName());
		//List<ParseError> errors = getErrors(okTemplate, schemaMultiRoot, okConfig, style);
		assertEquals(1, errors.size());
		assertEquals(56, errors.get(0).getLineNumber());
	}
	
	@Test
	public void NoRootElementInTheMainSchemaGivesExpectedParseError() throws IOException, URISyntaxException {
		TemplateGenerationException tge = getTemplateGenerationException(okTemplate, schemaNoRoot, okConfig, style);
		List<ParseError> errors = tge.getParseErrors();
		
		assertEquals(serviceName, tge.getServiceName());
		assertEquals(resourceName, tge.getResourceName());
		
		//List<ParseError> errors = getErrors(okTemplate, schemaNoRoot, okConfig, style);
		assertEquals(1, errors.size());
		assertEquals(6, errors.get(0).getLineNumber());
		assertTrue(errors.get(0).getMessage().contains("Cannot find the declaration of element 'dummy'"));
	}
	
	private void createService(
			String serviceName,
			String templateFilename, 
			String schemaFilename, 
			String configFilename) throws IOException, URISyntaxException {
		
		
		StringSource template = StringSourcePersistence.inferFromSystemResource(templateFilename);
		StringSource schema = StringSourcePersistence.inferFromSystemResource(schemaFilename);
		StringSource config = StringSourcePersistence.inferFromSystemResource(configFilename);
		
		ts.createService(serviceName);
		setTemplate(ts, serviceName, template);
		setConfig(ts, serviceName, configName, config);
		setSchema(ts, serviceName, schemaName, schema);
	}
	
	
	public void PrepExampleService() throws IOException, URISyntaxException {
		createService("dummy", okTemplate, okSchema, okConfig);
		
		createService("dummy-empty-template", empty, okSchema, okConfig);
		createService("dummy-empty-schema", okTemplate, empty, okConfig);
		createService("dummy-empty-config", okTemplate, okSchema, empty);
		
		createService("dummy-schema-syntax", okTemplate, schemaMissingBracket, okConfig);
		createService("dummy-schema-semantic", okTemplate, schemaMissingTypeDef, okConfig);
		
		createService("dummy-config-syntax", okTemplate, okSchema, configMissingBracket);
		createService("dummy-config-semantic", okTemplate, okSchema, configMissingType);
		
		
		createService("dummy2", okTemplate, okSchema, okConfig);
		
	}

}
