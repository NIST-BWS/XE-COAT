package gov.nist.ixe.templates.templateservices.tests.services;

import static org.junit.Assert.assertEquals;
import gov.nist.ixe.templates.Version;
import gov.nist.ixe.templates.exception.IllegalResourceNameException;
import gov.nist.ixe.templates.jaxb.ServiceList;
import gov.nist.ixe.templates.templateservices.tests.TemplateServicesTests;

import org.junit.Test;

public abstract class Tests extends TemplateServicesTests {
	
	@Test public void getVersionWorksCorrectly() {
		assertEquals(Version.getVersion(), ts.getVersion());
	}
	
	@Test public void testConnectionViaGetWorksCorrectly() {
		assertEquals(Version.getVersion(), ts.testConnectionViaGet());
	}
	
	@Test public void testConnectionViaPostWorksCorrectly() {
		assertEquals(Version.getVersion(), ts.testConnectionViaPost());
	}
	
	@Test public void testConnectionViaDeleteWorksCorrectly() {
		assertEquals(Version.getVersion(), ts.testConnectionViaDelete());
	}


	@Test public void getServicesWorksCorrectly() {
		
		ts.createService("service0");
		ts.createService("service1");
		ServiceList services = ts.getServiceList();
		services.sort();
		assertEquals(2, services.size());
		assertEquals("service", services.get(0).getRel());
		assertEquals("service", services.get(1).getRel());
		assertEquals(serverRootUri + "service0", services.get(0).getUri());
		assertEquals(serverRootUri + "service1", services.get(1).getUri());
	}
	
	@Test public void emptyServicesCanBeDeleted() {
		
		ts.createService("service0");
		ts.createService("service1");
		ts.createService("service2");
		
		ts.deleteService("service1");
		
		ServiceList services = ts.getServiceList();
		services.sort();
		assertEquals(2, services.size());
		assertEquals("service", services.get(0).getRel());
		assertEquals("service", services.get(1).getRel());
		assertEquals(serverRootUri + "service0", services.get(0).getUri());
		assertEquals(serverRootUri + "service2", services.get(1).getUri());
	}
	
	
	@Test (expected=IllegalResourceNameException.class)
	public void cannotCreateServiceWithWhitespaceName() {
		ts.createService("\r \t");	
	}
	
	@Test (expected=IllegalResourceNameException.class)
	public void cannotCreateServiceWithAReservedName() {
		ts.createService("service");
	}


}
