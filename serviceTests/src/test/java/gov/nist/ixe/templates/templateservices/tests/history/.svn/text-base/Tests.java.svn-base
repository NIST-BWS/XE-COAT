package gov.nist.ixe.templates.templateservices.tests.history;

import static gov.nist.ixe.templates.TemplateServicesUtil.setConfig;
import static gov.nist.ixe.templates.TemplateServicesUtil.setSchema;
import static gov.nist.ixe.templates.TemplateServicesUtil.setTemplate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Date;

import javax.ws.rs.core.Response;

import gov.nist.ixe.stringsource.StringSource;
import gov.nist.ixe.stringsource.StringSourceConverters;
import gov.nist.ixe.templates.BuildUri;
import gov.nist.ixe.templates.Constants;
import gov.nist.ixe.templates.exception.ResourceNotFoundException;
import gov.nist.ixe.templates.jaxb.HistoricLink;
import gov.nist.ixe.templates.jaxb.ResourceHistory;
import gov.nist.ixe.templates.templateservices.tests.TemplateServicesTests;
import gov.nist.ixe.templates.tests.Examples;

import org.junit.Test;

public abstract class Tests extends TemplateServicesTests {

	String serviceName = "service0";

	@Test
	public void schemaHistoryCanBeRetrievedCorrectly() 
			throws ResourceNotFoundException, ParseException, UnsupportedEncodingException {

		String resourceName = "schema0";
		
		ts.createService(serviceName);

		setSchema(ts, serviceName, resourceName, Examples.DUMMY0);	
		long t0 = new Date().getTime();
		setSchema(ts, serviceName, resourceName, Examples.DUMMY1);
		long t1 = new Date().getTime();
		setSchema(ts, serviceName, resourceName, Examples.DUMMY2);
		long t2 = new Date().getTime();
		
		String originalUri = BuildUri.getSchemaUri(serverRootUri, serviceName, resourceName);
		ResourceHistory rh = ts.getSchemaHistory(serviceName, resourceName);

		assertEquals(2, rh.getHistoricLinks().size());
		assertEquals(originalUri, rh.getOriginalUri());
		assertEquals(Constants.Rel.SCHEMA, rh.getOriginalRel());

		// Notice the REVERSE order since history is returned NEWEST to OLDEST
		HistoricLink hl0 = rh.getHistoricLinks().get(1);
		HistoricLink hl1 = rh.getHistoricLinks().get(0);
		
		long T0 = hl0.getTimestamp();
		long T1 = hl1.getTimestamp();

		assertTrue(T0 < T1);
		assertWithin(T0, t0, t1);
		assertWithin(T1, t1, t2);
		
		assertEquals(Examples.DUMMY0.getData().length, (int) hl0.getSizeInBytes());
		assertEquals(Examples.DUMMY1.getData().length, (int) hl1.getSizeInBytes());

		Response r0 = ts.getHistoricSchema(serviceName, resourceName, Long.toString(T0));
		Response r1 = ts.getHistoricSchema(serviceName, resourceName, Long.toString(T1));
		Response r = ts.getSchema(serviceName, resourceName);
		
		// Check the output headers
		
		assertEquals(Constants.Rel.HISTORIC_SCHEMA, r0.getHeaderString(Constants.HttpHeader.REL));
		assertEquals(Constants.Rel.HISTORIC_SCHEMA, r1.getHeaderString(Constants.HttpHeader.REL));
		
		assertEquals(serviceName, r0.getHeaderString(Constants.HttpHeader.SERVICE_NAME));
		assertEquals(serviceName, r1.getHeaderString(Constants.HttpHeader.SERVICE_NAME));
		
		assertEquals(resourceName, r0.getHeaderString(Constants.HttpHeader.RESOURCE_NAME));
		assertEquals(resourceName, r1.getHeaderString(Constants.HttpHeader.RESOURCE_NAME));
			
		assertEquals(originalUri, r0.getHeaderString(Constants.HttpHeader.HISTORIC_VERSION_OF));
		assertEquals(originalUri, r1.getHeaderString(Constants.HttpHeader.HISTORIC_VERSION_OF));	
		
		assertEquals(Constants.Rel.SCHEMA, r0.getHeaderString(Constants.HttpHeader.HISTORIC_REL_OF));
		assertEquals(Constants.Rel.SCHEMA, r1.getHeaderString(Constants.HttpHeader.HISTORIC_REL_OF));
		
		StringSource s0 = StringSourceConverters.fromResponse(r0);
		StringSource s1 = StringSourceConverters.fromResponse(r1);				
		StringSource s = StringSourceConverters.fromResponse(r);

		assertEquals(Examples.DUMMY0.getString(), s0.getString());
		assertEquals(Examples.DUMMY1.getString(), s1.getString());
		assertEquals(Examples.DUMMY2.getString(), s.getString());

	}


	@Test
	public void configHistoryCanBeRetrievedCorrectly() 
			throws ResourceNotFoundException, ParseException, UnsupportedEncodingException {

		ts.createService(serviceName);
		String resourceName = "config0";

		setConfig(ts, serviceName, resourceName, Examples.DUMMY0);	
		long t0 = new Date().getTime();
		setConfig(ts, serviceName, resourceName, Examples.DUMMY1);
		long t1 = new Date().getTime();
		setConfig(ts, serviceName, resourceName, Examples.DUMMY2);
		long t2 = new Date().getTime();

		ResourceHistory rh = ts.getConfigHistory(serviceName, resourceName);
		String originalUri = BuildUri.getConfigUri(serverRootUri, serviceName, resourceName);
		
		assertEquals(2, rh.getHistoricLinks().size());
		assertEquals(originalUri, rh.getOriginalUri());
		assertEquals(Constants.Rel.CONFIG, rh.getOriginalRel());
		

		// Notice the REVERSE order since history is returned NEWEST to OLDEST
		long T0 = rh.getHistoricLinks().get(1).getTimestamp();
		long T1 = rh.getHistoricLinks().get(0).getTimestamp();

		assertTrue(T0 < T1);
		assertWithin(T0, t0, t1);
		assertWithin(T1, t1, t2);
		
		Response r0 = ts.getHistoricConfig(serviceName, resourceName, Long.toString(T0));
		Response r1 = ts.getHistoricConfig(serviceName, resourceName, Long.toString(T1));
		Response r = ts.getConfig(serviceName, resourceName);
		
		assertEquals(Constants.Rel.HISTORIC_CONFIG, r0.getHeaderString(Constants.HttpHeader.REL));
		assertEquals(Constants.Rel.HISTORIC_CONFIG, r1.getHeaderString(Constants.HttpHeader.REL));
		
		assertEquals(serviceName, r0.getHeaderString(Constants.HttpHeader.SERVICE_NAME));
		assertEquals(serviceName, r1.getHeaderString(Constants.HttpHeader.SERVICE_NAME));
		
		assertEquals(resourceName, r0.getHeaderString(Constants.HttpHeader.RESOURCE_NAME));
		assertEquals(resourceName, r1.getHeaderString(Constants.HttpHeader.RESOURCE_NAME));
			
		assertEquals(originalUri, r0.getHeaderString(Constants.HttpHeader.HISTORIC_VERSION_OF));
		assertEquals(originalUri, r1.getHeaderString(Constants.HttpHeader.HISTORIC_VERSION_OF));	
		
		assertEquals(Constants.Rel.CONFIG, r0.getHeaderString(Constants.HttpHeader.HISTORIC_REL_OF));
		assertEquals(Constants.Rel.CONFIG, r1.getHeaderString(Constants.HttpHeader.HISTORIC_REL_OF));
		
		StringSource c1 = StringSourceConverters.fromResponse(r0);
		StringSource c2 = StringSourceConverters.fromResponse(r1);
		StringSource c = StringSourceConverters.fromResponse(r);

		assertEquals(Examples.DUMMY0.getString(), c1.getString());
		assertEquals(Examples.DUMMY1.getString(), c2.getString());
		assertEquals(Examples.DUMMY2.getString(), c.getString());

	}

	@Test
	public void templateHistoryCanBeRetrievedCorrectly() 
			throws ResourceNotFoundException, ParseException, UnsupportedEncodingException {

		ts.createService(serviceName);

		setTemplate(ts, serviceName, Examples.DUMMY0);	
		long t0 = new Date().getTime();
		setTemplate(ts, serviceName, Examples.DUMMY1);
		long t1 = new Date().getTime();
		setTemplate(ts, serviceName, Examples.DUMMY2);
		long t2 = new Date().getTime();

		ResourceHistory rh = ts.getTemplateHistory(serviceName);
		String originalUri = BuildUri.getTemplateUri(serverRootUri, serviceName);
		
		assertEquals(2, rh.getHistoricLinks().size());
		assertEquals(originalUri, rh.getOriginalUri());
		assertEquals(Constants.Rel.TEMPLATE, rh.getOriginalRel());
	
		// Notice the REVERSE order since history is returned NEWEST to OLDEST
		HistoricLink hl0 = rh.getHistoricLinks().get(1);
		HistoricLink hl1 = rh.getHistoricLinks().get(0);
	
		long T0 = hl0.getTimestamp();
		long T1 = hl1.getTimestamp();

		assertTrue(T0 < T1);
		assertWithin(T0, t0, t1);
		assertWithin(T1, t1, t2);
		
		Response r0 = ts.getHistoricTemplate(serviceName, Long.toString(T0));
		Response r1 = ts.getHistoricTemplate(serviceName, Long.toString(T1));
		Response r = ts.getTemplate(serviceName);		
		
		assertEquals(Constants.Rel.HISTORIC_TEMPLATE, r0.getHeaderString(Constants.HttpHeader.REL));
		assertEquals(Constants.Rel.HISTORIC_TEMPLATE, r1.getHeaderString(Constants.HttpHeader.REL));
		
		assertEquals(serviceName, r0.getHeaderString(Constants.HttpHeader.SERVICE_NAME));
		assertEquals(serviceName, r1.getHeaderString(Constants.HttpHeader.SERVICE_NAME));
		
		assertEquals(Constants.ResourceName.TEMPLATE, r0.getHeaderString(Constants.HttpHeader.RESOURCE_NAME));
		assertEquals(Constants.ResourceName.TEMPLATE, r1.getHeaderString(Constants.HttpHeader.RESOURCE_NAME));
			
		assertEquals(originalUri, r0.getHeaderString(Constants.HttpHeader.HISTORIC_VERSION_OF));
		assertEquals(originalUri, r1.getHeaderString(Constants.HttpHeader.HISTORIC_VERSION_OF));	
		
		assertEquals(Constants.Rel.TEMPLATE, r0.getHeaderString(Constants.HttpHeader.HISTORIC_REL_OF));
		assertEquals(Constants.Rel.TEMPLATE, r1.getHeaderString(Constants.HttpHeader.HISTORIC_REL_OF));
		
		StringSource tmpl0 = StringSourceConverters.fromResponse(r0);
		StringSource tmpl1 = StringSourceConverters.fromResponse(r1);
		StringSource tmpl = StringSourceConverters.fromResponse(r);

		assertEquals(Examples.DUMMY0.getString(), tmpl0.getString());
		assertEquals(Examples.DUMMY1.getString(), tmpl1.getString());
		assertEquals(Examples.DUMMY2.getString(), tmpl.getString());

	}


	@Test
	public void schemaHistoryCanBeRetrievedCorrectlyAfterRename() 
			throws ResourceNotFoundException, ParseException, UnsupportedEncodingException {

		ts.createService(serviceName);

		setSchema(ts, serviceName, "schema0", Examples.DUMMY0);	
		long t0 = new Date().getTime();
		setSchema(ts, serviceName, "schema0", Examples.DUMMY1);
		long t1 = new Date().getTime();
		setSchema(ts, serviceName, "schema0", Examples.DUMMY2);
		long t2 = new Date().getTime();

		ts.renameSchema(serviceName, "schema0", "schema1");
		ResourceHistory rh = ts.getSchemaHistory(serviceName, "schema1");
		assertEquals(2, rh.getHistoricLinks().size());

		// Notice the REVERSE order since history is returned NEWEST to OLDEST
		long T0 = rh.getHistoricLinks().get(1).getTimestamp();
		long T1 = rh.getHistoricLinks().get(0).getTimestamp();

		assertTrue(T0 < T1);
		assertWithin(T0, t0, t1);
		assertWithin(T1, t1, t2);

		Response r1 = ts.getHistoricSchema(serviceName, "schema1", Long.toString(T0));
		Response r2 = ts.getHistoricSchema(serviceName, "schema1", Long.toString(T1));
		Response r = ts.getSchema(serviceName, "schema1");
		
		String uri = BuildUri.getSchemaUri(serverRootUri, serviceName, "schema1");
		assertEquals(uri, r1.getHeaderString(Constants.HttpHeader.HISTORIC_VERSION_OF));
		assertEquals(uri, r2.getHeaderString(Constants.HttpHeader.HISTORIC_VERSION_OF));
		
		StringSource s0 = StringSourceConverters.fromResponse(r1);
		StringSource s1 = StringSourceConverters.fromResponse(r2);				
		StringSource s = StringSourceConverters.fromResponse(r);

		assertEquals(Examples.DUMMY0.getString(), s0.getString());
		assertEquals(Examples.DUMMY1.getString(), s1.getString());
		assertEquals(Examples.DUMMY2.getString(), s.getString());


	}
	
	@Test
	public void configHistoryCanBeRetrievedCorrectlyAfterRename() 
			throws ResourceNotFoundException, ParseException, UnsupportedEncodingException {

		ts.createService(serviceName);

		setConfig(ts, serviceName, "cc", Examples.DUMMY0);	
		long t0 = new Date().getTime();
		setConfig(ts, serviceName, "cc", Examples.DUMMY1);
		long t1 = new Date().getTime();
		setConfig(ts, serviceName, "cc", Examples.DUMMY2);
		long t2 = new Date().getTime();

		ts.renameConfig(serviceName, "cc", "c");
		ResourceHistory rh = ts.getConfigHistory(serviceName, "c");
		assertEquals(2, rh.getHistoricLinks().size());

		// Notice the REVERSE order since history is returned NEWEST to OLDEST
		long T0 = rh.getHistoricLinks().get(1).getTimestamp();
		long T1 = rh.getHistoricLinks().get(0).getTimestamp();

		assertTrue(T0 < T1);
		assertWithin(T0, t0, t1);
		assertWithin(T1, t1, t2);
		
		Response r0 = ts.getHistoricConfig(serviceName, "c", Long.toString(T0));
		Response r1 = ts.getHistoricConfig(serviceName, "c", Long.toString(T1));
		Response r = ts.getConfig(serviceName, "c");
		
		String uri = BuildUri.getConfigUri(serverRootUri, serviceName, "c");
		assertEquals(uri, r0.getHeaderString(Constants.HttpHeader.HISTORIC_VERSION_OF));
		assertEquals(uri, r1.getHeaderString(Constants.HttpHeader.HISTORIC_VERSION_OF));
		
		StringSource c0 = StringSourceConverters.fromResponse(r0);
		StringSource c1 = StringSourceConverters.fromResponse(r1);
		StringSource c = StringSourceConverters.fromResponse(r);

		assertEquals(Examples.DUMMY0.getString(), c0.getString());
		assertEquals(Examples.DUMMY1.getString(), c1.getString());
		assertEquals(Examples.DUMMY2.getString(), c.getString());

	}
	
		
	
	
}
