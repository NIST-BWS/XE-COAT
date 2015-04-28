package gov.nist.ixe.templates.templateservices.tests.config;

import static gov.nist.ixe.templates.TemplateServicesUtil.getConfig;
import static gov.nist.ixe.templates.TemplateServicesUtil.setConfig;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import gov.nist.ixe.stringsource.StringSource;
import gov.nist.ixe.stringsource.StringSourceConverters;
import gov.nist.ixe.templates.BuildUri;
import gov.nist.ixe.templates.Constants;
import gov.nist.ixe.templates.Constants.HttpHeader;
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
	String configName = "config0";
	
	@Test (expected=ResourceNotFoundException.class)
	public void gettingAconfigOnAnEmptyServiceShouldThrowResourceNotFound() {
		ts.createService(serviceName);
		ts.getConfig(serviceName, configName);
	}
	
	@Test (expected=ResourceNotFoundException.class)
	public void gettingAConfigOnAServiceThatDoesNotExistShouldThrowResourceNotFound() {
		ts.getConfig("service0", "Config0");	
	}
	
	@Test
	public void settingAndGettingAConfigRoundtripsProperly() throws UnsupportedEncodingException {		
		ts.createService(serviceName);
		setConfig(ts, serviceName, configName, Examples.CONFIG);
		String roundTrip = getConfig(ts, serviceName, configName).getString();	
	}
	
	@Test
	public void settingAndGettingAConfigSetsRelHeaderProperly() throws UnsupportedEncodingException {		
		ts.createService(serviceName);
		setConfig(ts, serviceName, configName, Examples.CONFIG);
		assertEquals(ts.getConfig(serviceName, configName).getHeaderString(Constants.HttpHeader.REL), Constants.Rel.CONFIG);
	}
	
	@Test
	public void settingConfigsProvideResourceLinks() {
		
		String serviceName = "service0";
		
		String config0Name = "config0";
		String config1Name = "config1";
		
		ts.createService(serviceName);
		setConfig(ts, serviceName, config0Name, Examples.CONFIG);
		setConfig(ts, serviceName, config1Name, Examples.CONFIG);
		List<Link> configs = ts.getServiceResources(serviceName).getConfigLinks();
		
		assertEquals(2, configs.size());
		
		assertEquals(config0Name, configs.get(0).getName());
		assertEquals(config1Name, configs.get(1).getName());
		
		assertEquals(Constants.Rel.CONFIG, configs.get(0).getRel());
		assertEquals(Constants.Rel.CONFIG, configs.get(1).getRel());
		
		assertEquals(BuildUri.getConfigUri(serverRootUri, serviceName, config0Name), configs.get(0).getUri());
		assertEquals(BuildUri.getConfigUri(serverRootUri, serviceName, config1Name), configs.get(1).getUri());
			
	}
	
	@Test
	public void deletingAconfigThatDoesNotExistIsAllowed() {
		
		String serviceName = "service0"; String configName = "config0";
		ts.createService(serviceName);
		ts.deleteConfig(serviceName, configName);
	}
	
	@Test
	public void deletingAConfigWorks() {
		
				
		String config0Name = "config0";
		String config1Name = "config1";
		
		ts.createService(serviceName);
		setConfig(ts, serviceName, config0Name, Examples.CONFIG);
		setConfig(ts, serviceName, config1Name, Examples.CONFIG);
				
		int config_count_before_delete = ts.getServiceResources(serviceName).getConfigLinks().size();
		ts.deleteConfig(serviceName, config0Name);		
		int config_count_after_delete = ts.getServiceResources(serviceName).getConfigLinks().size();
		
		assertEquals(2, config_count_before_delete);
		assertEquals(1, config_count_after_delete);
	
	}
	
	@Test
	public void deletingAConfigWithAnIllegalNameIsAllowed() {
		String serviceName = "service0"; String configName = "config";
		ts.createService(serviceName);
		ts.deleteConfig(serviceName, configName);
	}
	
	

	@Test
	public void settingAConfigWithAEmptyStringPayloadIsAllowed(){	
		ts.createService(serviceName);
		setConfig(ts, serviceName, configName, Examples.EMPTY);
		StringSource result = StringSourceConverters.fromResponse(ts.getConfig(serviceName, configName));
		assertArrayEquals(new byte[]{}, result.getData());
	}
	
	@Test
	public void settingAConfigWithAWhitespacePayloadIsAllowed() throws UnsupportedEncodingException {
		
				
		ts.createService(serviceName);
		setConfig(ts, serviceName, configName, Examples.WHITESPACE);
		StringSource result = StringSourceConverters.fromResponse(ts.getConfig(serviceName, configName));
		assertEquals(Examples.WHITESPACE.getString(), result.getString());
	}
	
	@Test (expected=IllegalContentTypeException.class)
	public void settingAConfigWithAnInvalidContentTypeIsForbidden() {			
		ts.createService(serviceName);
		ts.setConfig(serviceName, "config0", Examples.CONFIG.getData(), "notavalidcontenttype");
	}
	
	@Test (expected=IllegalContentTypeException.class)
	public void settingAConfigWithAnInvalidCharsetIsForbidden() {			
		ts.createService(serviceName);
		ts.setConfig(serviceName, "config0", Examples.CONFIG.getData(), "text/xml; charset=badcharsetname");
	}
	
	@Test
	public void settingAConfigWithAnEmptyCharsetTriggersCharsetDetection() {
		ts.createService(serviceName);
		ts.setConfig(serviceName, configName, Examples.CONFIG.getData(), "text/xml");
		StringSource rt = StringSourceConverters.fromResponse(ts.getConfig(serviceName, configName));
		assertEquals(Examples.CONFIG.getCharset(), rt.getCharset());		
	}
	
	@Test (expected=IllegalResourceNameException.class)
	public void settingAConfigWithABadNameIsForbidden() {
		ts.createService(serviceName);
		setConfig(ts, serviceName, "***", Examples.CONFIG);	
	}
	
	@Test
	public void getConfigShouldReturnCorrectHeaders() {
		
		String serviceName = "service0";
		String resourceName = "config0";
		ts.createService(serviceName);
		setConfig(ts, serviceName, "config0", Examples.SCHEMA);
		
		javax.ws.rs.core.Response r = ts.getConfig(serviceName, resourceName);
		assertEquals(serviceName, r.getHeaderString(HttpHeader.SERVICE_NAME));
		assertEquals(resourceName, r.getHeaderString(HttpHeader.RESOURCE_NAME));
		assertEquals("config", r.getHeaderString(HttpHeader.REL));
		
	}
	
}
