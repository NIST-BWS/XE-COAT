package gov.nist.ixe.templates.templateservices.tests.rename;

import static gov.nist.ixe.templates.TemplateServicesUtil.setConfig;
import static gov.nist.ixe.templates.TemplateServicesUtil.setSchema;
import static gov.nist.ixe.templates.TemplateServicesUtil.setTemplate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Date;

import gov.nist.ixe.stringsource.StringSource;
import gov.nist.ixe.stringsource.StringSourceConverters;
import gov.nist.ixe.templates.BuildUri;
import gov.nist.ixe.templates.Constants;
import gov.nist.ixe.templates.exception.IllegalResourceNameException;
import gov.nist.ixe.templates.exception.ResourceNotFoundException;
import gov.nist.ixe.templates.jaxb.RenameResult;
import gov.nist.ixe.templates.jaxb.ResourceHistory;
import gov.nist.ixe.templates.jaxb.ServiceList;
import gov.nist.ixe.templates.templateservices.tests.TemplateServicesTests;
import gov.nist.ixe.templates.tests.Examples;

import org.junit.Test;

public abstract class Tests extends TemplateServicesTests {
	
	 String serviceName = "service0";
	
	@Test (expected=IllegalResourceNameException.class) 
	public void servicesCannotBeRenamedToAnExistingServiceName() {	
		ts.createService("service0");
		ts.createService("service1");
		ts.renameService("service0", "service1");		
	}
	
	@Test (expected=IllegalResourceNameException.class) 
	public void configsCannotBeRenamedToAnExistingName() {
		String s = "service0";
		
		ts.createService(s);
		setConfig(ts, s, "c1", Examples.CONFIG);
		setConfig(ts, s, "c2", Examples.CONFIG);
		ts.renameConfig(s, "c1", "c2");		
	}
	
	@Test (expected=IllegalResourceNameException.class) 
	public void schemasCannotBeRenamedToAnExistingName() {
		
		ts.createService("service0");
		setSchema(ts, "service0", "s1", Examples.SCHEMA);
		setSchema(ts, "service0", "s2", Examples.SCHEMA);
		ts.renameSchema("service0", "s1", "s2");		
	}
	
	
	@Test (expected=IllegalResourceNameException.class) 
	public void servicesCannotBeRenamedToABadServiceName() {	
		ts.createService("service0");
		ts.renameService("service0", "service;;;");		
	}
	
	@Test (expected=IllegalResourceNameException.class) 
	public void configsCannotBeRenamedToABadName() {
		String s = "service0";		
		ts.createService(s);
		setConfig(ts, s, "c1", Examples.CONFIG);
		ts.renameConfig(s, "c1", "*!*!");		
	}
	
	@Test (expected=IllegalResourceNameException.class) 
	public void schemasCannotBeRenamedToABadName() {		
		ts.createService("service0");
		setSchema(ts, "service0", "s1", Examples.SCHEMA);
		ts.renameSchema("service0", "s1", "~^~^~");		
	}
	
	
	
	@Test public void servicesCanBeRenamedCorrectly() {
		
		ts.createService("service0");
		ts.createService("service2");
		ts.renameService("service2", "service1");
		ServiceList services = ts.getServiceList();
		services.sort();
		assertEquals(2, services.size());
		assertEquals("service", services.get(0).getRel());
		assertEquals("service", services.get(1).getRel());
		assertEquals(serverRootUri + "service0", services.get(0).getUri());
		assertEquals(serverRootUri + "service1", services.get(1).getUri());
	}
	
	
	@Test public void servicesHaveTheirChildrenRenamedCorrectly() {
		
		ts.createService("service0");
		
		setConfig(ts, "service0", "c1", Examples.CONFIG);
		setConfig(ts, "service0", "c2", Examples.CONFIG);
		setConfig(ts, "service0", "c3", Examples.CONFIG);
		setSchema(ts, "service0", "s1", Examples.SCHEMA);
		setTemplate(ts, "service0", Examples.TEMPLATE);
		RenameResult rr = ts.renameService("service0", "service1");
		assertEquals(rr.getRenamedResources().size(), 5);
		
		String rootUri = serverRootUri;
		assertEquals(rr.getRenamedResources().get(0).getOldLink().getUri(), 
				BuildUri.getConfigUri(rootUri, "service0", "c1"));
		assertEquals(rr.getRenamedResources().get(0).getNewLink().getUri(), 
				BuildUri.getConfigUri(rootUri, "service1", "c1"));
		
		assertEquals(rr.getRenamedResources().get(1).getOldLink().getUri(), 
				BuildUri.getConfigUri(rootUri, "service0", "c2"));
		assertEquals(rr.getRenamedResources().get(1).getNewLink().getUri(), 
				BuildUri.getConfigUri(rootUri, "service1", "c2"));
		
		assertEquals(rr.getRenamedResources().get(2).getOldLink().getUri(), 
				BuildUri.getConfigUri(rootUri, "service0", "c3"));
		assertEquals(rr.getRenamedResources().get(2).getNewLink().getUri(), 
				BuildUri.getConfigUri(rootUri, "service1", "c3"));
		
		assertEquals(rr.getRenamedResources().get(3).getOldLink().getUri(), 
				BuildUri.getSchemaUri(rootUri, "service0", "s1"));
		assertEquals(rr.getRenamedResources().get(3).getNewLink().getUri(), 
				BuildUri.getSchemaUri(rootUri, "service1", "s1"));
		
		
		assertEquals(rr.getRenamedResources().get(4).getOldLink().getUri(), 
				BuildUri.getTemplateUri(rootUri, "service0"));
		assertEquals(rr.getRenamedResources().get(4).getNewLink().getUri(), 
				BuildUri.getTemplateUri(rootUri, "service1"));
		
	}
	
	
	@Test public void serviceRenameReturnsNewLocationAsLinkXml() {
		
		String serviceName = "service0", oldName = serviceName, newName = "newName";
		
		ts.createService(serviceName);
		RenameResult rr = ts.renameService(oldName, newName);
		
		assertLinkEquals(oldName, Constants.Rel.SERVICE,  BuildUri.getServiceUri(serverRootUri, oldName), 
				rr.getOldLink());
		assertLinkEquals(newName, Constants.Rel.SERVICE,  BuildUri.getServiceUri(serverRootUri, newName), 
				rr.getNewLink());				
	}
	
	@Test public void schemaRenameReturnsNewLocationAsLinkXml() {
		
		
		ts.createService(serviceName);
		setSchema(ts, serviceName, "schema0", Examples.DUMMY0);
		RenameResult rr = ts.renameSchema(serviceName, "schema0", "schema1");
		
		assertLinkEquals("schema1", Constants.Rel.SCHEMA,  
				BuildUri.getSchemaUri(serverRootUri, serviceName, "schema1"), rr.getNewLink());				
	}
	
	@Test public void configRenameReturnsNewLocationAsLinkXml() {
				
		ts.createService(serviceName);
		setConfig(ts, serviceName, "config0", Examples.DUMMY0);
		RenameResult rr = ts.renameConfig(serviceName, "config0", "config1");
		
		assertLinkEquals("config1", Constants.Rel.CONFIG,  
				BuildUri.getConfigUri(serverRootUri, serviceName, "config1"), rr.getNewLink());				
	}
	
	@Test
	public void schemaHistorySurviesARename() 
			throws ResourceNotFoundException, ParseException, UnsupportedEncodingException {
				
		String serviceName = "service0";
		ts.createService(serviceName);
		
		
		setSchema(ts, serviceName, "schema1", Examples.DUMMY0);	
		long t0 = new Date().getTime();
		setSchema(ts, serviceName, "schema1", Examples.DUMMY1);
		long t1 = new Date().getTime();
		setSchema(ts, serviceName, "schema1", Examples.DUMMY2);
		long t2 = new Date().getTime();
		
		ts.renameSchema(serviceName, "schema1", "schema0");
		
		ResourceHistory rh = ts.getSchemaHistory(serviceName, "schema0");
		
		assertEquals(2, rh.getHistoricLinks().size());
		
		// Notice the REVERSE order since history is returned NEWEST to OLDEST
		long T0 = rh.getHistoricLinks().get(1).getTimestamp();
		long T1 = rh.getHistoricLinks().get(0).getTimestamp();
		
		assertTrue(T0 < T1);
		assertWithin(T0, t0, t1);
		assertWithin(T1, t1, t2);
		
		StringSource s0 = StringSourceConverters.fromResponse(ts.getHistoricSchema(serviceName, "schema0", Long.toString(T0)));
		StringSource s1 = StringSourceConverters.fromResponse(ts.getHistoricSchema(serviceName, "schema0", Long.toString(T1)));
		StringSource s = StringSourceConverters.fromResponse(ts.getSchema(serviceName, "schema0"));
		
		assertEquals(Examples.DUMMY0.getString(), s0.getString());
		assertEquals(Examples.DUMMY1.getString(), s1.getString());
		assertEquals(Examples.DUMMY2.getString(), s.getString());
		
	}

	@Test
	public void configHistorySurviesARename() 
			throws ResourceNotFoundException, ParseException, UnsupportedEncodingException {
		
		String serviceName = "service0";
		ts.createService(serviceName);
		
		
		setConfig(ts, serviceName, "d", Examples.DUMMY0);	
		long t0 = new Date().getTime();
		setConfig(ts, serviceName, "d", Examples.DUMMY1);
		long t1 = new Date().getTime();
		setConfig(ts, serviceName, "d", Examples.DUMMY2);
		long t2 = new Date().getTime();
		
		ts.renameConfig(serviceName, "d", "c");
		
		ResourceHistory rh = ts.getConfigHistory(serviceName, "c");
		
		assertEquals(2, rh.getHistoricLinks().size());
		
		// Notice the REVERSE order since history is returned NEWEST to OLDEST
		long T0 = rh.getHistoricLinks().get(1).getTimestamp();
		long T1 = rh.getHistoricLinks().get(0).getTimestamp();
		
		assertTrue(T0 < T1);
		assertWithin(T0, t0, t1);
		assertWithin(T1, t1, t2);
		
		StringSource c0 = StringSourceConverters.fromResponse(ts.getHistoricConfig(serviceName, "c", Long.toString(T0)));
		StringSource c1 = StringSourceConverters.fromResponse(ts.getHistoricConfig(serviceName, "c", Long.toString(T1)));
		StringSource c = StringSourceConverters.fromResponse(ts.getConfig(serviceName, "c"));
		
		assertEquals(Examples.DUMMY0.getString(), c0.getString());
		assertEquals(Examples.DUMMY1.getString(), c1.getString());
		assertEquals(Examples.DUMMY2.getString(), c.getString());
		
	}
}
