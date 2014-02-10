package gov.nist.ixe.templates.templateservices.tests.schema;

import static gov.nist.ixe.templates.TemplateServicesUtil.setConfig;
import static gov.nist.ixe.templates.TemplateServicesUtil.setSchema;
import static org.junit.Assert.assertEquals;
import gov.nist.ixe.stringsource.StringSource;
import gov.nist.ixe.stringsource.StringSourceConverters;
import gov.nist.ixe.templates.BuildUri;
import gov.nist.ixe.templates.Constants;
import gov.nist.ixe.templates.exception.IllegalContentTypeException;
import gov.nist.ixe.templates.exception.IllegalResourceNameException;
import gov.nist.ixe.templates.exception.ResourceNotFoundException;
import gov.nist.ixe.templates.jaxb.Link;
import gov.nist.ixe.templates.templateservices.tests.TemplateServicesTests;
import gov.nist.ixe.templates.tests.Examples;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.junit.Test;

public abstract class Tests extends TemplateServicesTests {

	String serviceName = "service0";
	String schemaName = "schema0";
	String schema0Name = "schema0";
	String schema1Name = "schema1";

	@Test (expected=ResourceNotFoundException.class)
	public void gettingASchemaOnAnEmptyServiceShouldThrowResourceNotFound() {

		ts.createService(serviceName);
		ts.getSchema(serviceName, schemaName);
	}

	@Test (expected=ResourceNotFoundException.class)
	public void gettingASchemaOnAServiceThatDoesNotExistShouldThrowResourceNotFound() {
		ts.getSchema(serviceName, schemaName);	
	}

	@Test (expected=IllegalResourceNameException.class)
	public void cannotGetSchemaWithABlankServiceName() {
		ts.getSchema("\t \r", schemaName);	
	}

	@Test
	public void settingAndGettingASchemaRoundtripsProperly() throws UnsupportedEncodingException {


		ts.createService(serviceName);
		setSchema(ts, serviceName, schemaName, Examples.SCHEMA);
		String roundTrip = StringSourceConverters.fromResponse(ts.getSchema(serviceName, schemaName)).getString();
		assertEquals(Examples.SCHEMA.getString(), roundTrip);
	}
	
	@Test
	public void settingAndGettingASchemaSetsRelHeaderProperly() throws UnsupportedEncodingException {		
		ts.createService(serviceName);
		setSchema(ts, serviceName, schemaName, Examples.CONFIG);
		assertEquals(ts.getSchema(serviceName, schemaName).getHeaderString(Constants.HttpHeader.REL), Constants.Rel.SCHEMA);
	}

	@Test
	public void settingSchemasProvideResourceLinks() {

		ts.createService(serviceName);
		setSchema(ts, serviceName, schema0Name, Examples.SCHEMA);
		setSchema(ts, serviceName, schema1Name, Examples.SCHEMA);
		List<Link> schemas = ts.getServiceResources(serviceName).getSchemaLinks();

		assertEquals(2, schemas.size());

		assertEquals(schema0Name, schemas.get(0).getName());
		assertEquals(schema1Name, schemas.get(1).getName());

		assertEquals(Constants.Rel.SCHEMA, schemas.get(0).getRel());
		assertEquals(Constants.Rel.SCHEMA, schemas.get(1).getRel());

		assertEquals(BuildUri.getSchemaUri(rootUri, serviceName, schema0Name), schemas.get(0).getUri());
		assertEquals(BuildUri.getSchemaUri(rootUri, serviceName, schema1Name), schemas.get(1).getUri());

	}

	@Test
	public void deletingASchemaThatDoesNotExistIsAllowed() {

		ts.createService(serviceName);
		ts.deleteSchema(serviceName, schemaName);
	}

	@Test
	public void deletingASchemaWorks() {



		ts.createService(serviceName);
		setSchema(ts, serviceName, schema0Name, Examples.SCHEMA);
		setSchema(ts, serviceName, schema1Name, Examples.SCHEMA);

		int schema_count_before_delete = ts.getServiceResources(serviceName).getSchemaLinks().size();
		ts.deleteSchema(serviceName, schema0Name);		
		int schema_count_after_delete = ts.getServiceResources(serviceName).getSchemaLinks().size();

		assertEquals(2, schema_count_before_delete);
		assertEquals(1, schema_count_after_delete);

	}

	public void canSetASchemaWithAnEmptyPayload() {
		
		ts.createService(serviceName);
		setSchema(ts, serviceName, schemaName, Examples.EMPTY);	
	} 

	public void canSetASchemaWithAnWhitespacePayload() {
		
		
		ts.createService(serviceName);
		setSchema(ts, serviceName, schemaName, Examples.WHITESPACE);		
	}
	
	
	@Test (expected=IllegalContentTypeException.class)
	public void settingASchemaWithAnInvalidContentTypeIsForbidden() {
		ts.setSchema(serviceName, "schema0", Examples.SCHEMA.getData(), "notavalidcontenttype");
	}

	@Test (expected=IllegalContentTypeException.class)
	public void settingASchemaWithAnInvalidCharsetIsForbidden() {			
		ts.createService(serviceName);
		ts.setConfig(serviceName, "schema0", Examples.CONFIG.getData(), "text/xml; charset=badcharsetname");
	}
	
	@Test
	public void settingASchemaWithAnEmptyCharsetTriggersCharsetDetection() {
		ts.createService(serviceName);
		ts.setSchema(serviceName, "schema0", Examples.SCHEMA.getData(), "text/xml");
		StringSource rt = StringSourceConverters.fromResponse(ts.getSchema(serviceName, "schema0"));		
		assertEquals(Examples.SCHEMA.getCharset(), rt.getCharset());		
	}
	
	@Test (expected=IllegalResourceNameException.class)
	public void settingASchemaWithABadNameIsForbidden() {
		ts.createService(serviceName);
		setSchema(ts, serviceName, "***", Examples.SCHEMA);	
	}
	
	
}
