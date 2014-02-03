package gov.nist.ixe.templates.tests;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;

import gov.nist.ixe.stringsource.StringSource;
import gov.nist.ixe.templates.IStorageProvider;
import gov.nist.ixe.templates.exception.ResourceNotEmptyException;
import gov.nist.ixe.templates.exception.ResourceNotFoundException;

import org.junit.Test;

public abstract class IStorageProviderTests {

	public abstract IStorageProvider getStorageProvider(); 
	
	@Test public void storageProviderIsNotNull() { 
		assertNotNull(getStorageProvider());
	};
	
	
	// --------
	// SERVICES
	// --------	
	
	@Test
	public void getServiceNamesWorksCorrectly() {
			IStorageProvider storage = getStorageProvider(); 
			storage.addService("service0");
			storage.addService("service1");
			String[] services = storage.getServiceNames();
			assertArrayEquals(new String[] { "service0", "service1" }, services);
	}
	
	@Test
	public void deletingAServiceWorksCorrectly() {
			IStorageProvider storage = getStorageProvider(); 
			storage.addService("service0");
			storage.addService("service1");
			storage.addService("service2");
			storage.deleteService("service1");
			String[] services = storage.getServiceNames();
						
			assertArrayEquals(new String[] { "service0", "service2" }, services);
	}
	
	@Test (expected=ResourceNotEmptyException.class)
	public void cannotDeleteAServiceWithResources() {
		IStorageProvider storage = getStorageProvider(); 
		storage.addService("service0");
		storage.setTemplate("service0", Examples.TEMPLATE);
		storage.deleteService("service0");
	}
	
	@Test 
	public void canDeleteAServiceWithoutResources() {
		IStorageProvider storage = getStorageProvider(); 
		storage.addService("service0");
		storage.setTemplate("service0", Examples.TEMPLATE);
		storage.deleteTemplate("service0");
		storage.deleteService("service0");
		assertEquals(0, storage.getServiceNames().length);
	}
	
	@Test
	public void cannotAddServiceWithEmptyName() {
		getStorageProvider().addService(""); 			
	}
	
	
	// ---------
	// TEMPLATES
	// ---------
	
	@Test (expected=ResourceNotFoundException.class)
	public void gettingATemplateOnAnEmptyServiceShouldThrowResourceNotFound() {
		IStorageProvider storage = getStorageProvider();
		storage.addService("service0");
		storage.getTemplate("service0");
	}
	
	@Test (expected=ResourceNotFoundException.class)
	public void gettingATemplateOnAServiceThatDoesNotExistShouldThrowResourceNotFound() {
		IStorageProvider storage = getStorageProvider();
		storage.getTemplate("service0");
	}
	
	public void settingAndGettingATemplateRoundtripsProperly() {
		IStorageProvider storage = getStorageProvider();
		storage.addService("service0");
		storage.setTemplate("service0", Examples.TEMPLATE);
		StringSource roundTrip = storage.getTemplate("service0");
		assertEquals(Examples.TEMPLATE.toString(), roundTrip.toString());
	}
	
	
	
	

	// ---------
	// SCHEMAS
	// ---------
	
	@Test (expected=ResourceNotFoundException.class)
	public void gettingASchemaOnAServiceThatDoesNotExistShouldThrowResourceNotFound() {
		IStorageProvider storage = getStorageProvider();
		storage.getSchema("service0", "schema.xsd");
	}
	
	@Test (expected=ResourceNotFoundException.class)
	public void addingASchemaOnAnServiceThatDoesNotExistShouldThrowResourceNotFound() {
		IStorageProvider storage = getStorageProvider();
		storage.addSchema("service0", "schema.xsd", Examples.SCHEMA);
	}
	
	@Test (expected=ResourceNotFoundException.class)
	public void deletingASchemaOnAnServiceThatDoesNotExistShouldThrowResourceNotFound() {
		IStorageProvider storage = getStorageProvider();
		storage.deleteSchema("service0", "schema.xsd");
	}
	
	@Test (expected=ResourceNotFoundException.class)
	public void gettingSchemaNamesOnAServiceThatDoesNotExistShouldThrowResourceNotFound() {
		IStorageProvider storage = getStorageProvider();
		storage.getSchemaNames("service0");
	}
	
	@Test (expected=ResourceNotFoundException.class)
	public void gettingASchemaOnAnEmptyServiceShouldThrowResourceNotFound() {
		IStorageProvider storage = getStorageProvider();
		storage.addService("service0");
		storage.getSchema("service0", "schema.xsd");
	}
	
	@Test 
	public void emptyServicesHaveNoSchemas() {
		IStorageProvider storage = getStorageProvider();
		storage.addService("service0");
		String[] schemaNames = storage.getSchemaNames("service0");
		assertTrue(schemaNames.length == 0);
		
	}
	
	@Test
	public void settingAndGettingASchemaRoundtripsProperly() throws UnsupportedEncodingException {
		IStorageProvider storage = getStorageProvider();
		storage.addService("service0");
		storage.addSchema("service0", "schema0", Examples.SCHEMA);
		String roundTrip = storage.getSchema("service0", "schema0").getString();
		assertEquals(Examples.SCHEMA.getString(), roundTrip);
	}
	
	@Test
	public void getSchemaNamesWorksProperly() {
		IStorageProvider storage = getStorageProvider();
		storage.addService("service0");
		storage.addSchema("service0", "s0", Examples.SCHEMA);
		storage.addSchema("service0", "s1", Examples.SCHEMA);
		storage.addSchema("service0", "s2", Examples.SCHEMA);
		String[] names = storage.getSchemaNames("service0");
		assertArrayEquals(new String[] { "s0", "s1", "s2" }, names);
	}
	
	@Test
	public void deleteSchemaWorksProperly() {
		IStorageProvider storage = getStorageProvider();
		String serviceName = "service0";
		storage.addService(serviceName);
		storage.addSchema(serviceName, "s0", Examples.SCHEMA);
		storage.addSchema(serviceName, "s1", Examples.SCHEMA);
		storage.addSchema(serviceName, "s2", Examples.SCHEMA);
		storage.deleteSchema(serviceName, "s1");
		String[] names = storage.getSchemaNames("service0");
		assertArrayEquals(new String[] { "s0", "s2" }, names);
	}
	
	@Test
	public void deletingASchemaThatDoesNotExistIsAllowed() {
		IStorageProvider storage = getStorageProvider();
		String serviceName = "service0";
		storage.addService(serviceName);
		storage.addSchema(serviceName, "s1", Examples.SCHEMA);
		storage.deleteSchema(serviceName, "s1");
		storage.deleteSchema(serviceName, "s1");
		String[] names = storage.getSchemaNames("service0");
		assertEquals(0, names.length);
	}
	
	
	// ---------
	// CONFIG
	// ---------
	
	@Test (expected=ResourceNotFoundException.class)
	public void gettingAConfigOnAServiceThatDoesNotExistShouldThrowResourceNotFound() {
		IStorageProvider storage = getStorageProvider();
		storage.getConfig("service0", "config.xml");
	}
	
	@Test (expected=ResourceNotFoundException.class)
	public void gettingAConfignamesOnAServiceThatDoesNotExistShouldThrowResourceNotFound() {
		IStorageProvider storage = getStorageProvider();
		storage.getConfigNames("service0");
	}
	
	@Test (expected=ResourceNotFoundException.class)
	public void deletingAConfigOnAnServiceThatDoesNotExistShouldThrowResourceNotFound() {
		IStorageProvider storage = getStorageProvider();
		storage.deleteConfig("service0", "config.xml");
	}
	
	@Test (expected=ResourceNotFoundException.class)
	public void gettingConfigNamesOnAServiceThatDoesNotExistShouldThrowResourceNotFound() {
		IStorageProvider storage = getStorageProvider();
		storage.getConfigNames("service0");
	}
	
	
	@Test (expected=ResourceNotFoundException.class)
	public void gettingAConfigOnAnEmptyServiceShouldThrowResourceNotFound() {
		IStorageProvider storage = getStorageProvider();
		storage.addService("service0");
		storage.getConfig("service0", "config.xml");
	}
		
	@Test 
	public void emptyServicesHaveNoConfigurations() {
		IStorageProvider storage = getStorageProvider();
		storage.addService("service0");
		String[] configurationNames = storage.getConfigNames("service0");
		assertTrue(configurationNames.length == 0);
		
	}
	
	@Test
	public void settingAndGettingAConfigRoundtripsProperly() throws UnsupportedEncodingException {
		IStorageProvider storage = getStorageProvider();
		storage.addService("service0");
		storage.addConfig("service0", "c0", Examples.CONFIG);
		String roundTrip = storage.getConfig("service0", "c0").getString();
		assertEquals(Examples.CONFIG.getString(), roundTrip);
	}
	
	@Test
	public void getConfigNamesWorksProperly() {
		IStorageProvider storage = getStorageProvider();
		storage.addService("service0");
		storage.addConfig("service0", "c0", Examples.CONFIG);
		storage.addConfig("service0", "c1", Examples.CONFIG);
		storage.addConfig("service0", "c2", Examples.CONFIG);
		String[] names = storage.getConfigNames("service0");
		assertArrayEquals(new String[] { "c0", "c1", "c2" }, names);
	}
		
	
	@Test
	public void deleteConfigurationWorksProperly() {
		IStorageProvider storage = getStorageProvider();
		String serviceName = "service0";
		storage.addService(serviceName);
		storage.addConfig(serviceName, "s0", Examples.CONFIG);
		storage.addConfig(serviceName, "s1", Examples.CONFIG);
		storage.addConfig(serviceName, "s2", Examples.CONFIG);
		storage.deleteConfig(serviceName, "s1");
		String[] names = storage.getConfigNames("service0");
		assertArrayEquals(new String[] { "s0", "s2" }, names);
	}
	
	@Test
	public void deletingAConfigurationThatDoesNotExistIsAllowed() {
		IStorageProvider storage = getStorageProvider();
		String serviceName = "service0";
		storage.addService(serviceName);
		storage.addConfig(serviceName, "s1", Examples.CONFIG);
		storage.deleteConfig(serviceName, "s1");
		storage.deleteConfig(serviceName, "s1");
		String[] names = storage.getConfigNames("service0");
		assertEquals(0, names.length);
	}
	

	
}
