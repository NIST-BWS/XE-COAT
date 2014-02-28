package gov.nist.ixe.templates.templateservices.tests.templates;

import static org.junit.Assert.assertEquals;
import gov.nist.ixe.stringsource.StringSource;
import gov.nist.ixe.stringsource.StringSourceConverters;
import gov.nist.ixe.templates.BuildUri;
import gov.nist.ixe.templates.Constants;
import gov.nist.ixe.templates.exception.IllegalContentTypeException;
import gov.nist.ixe.templates.exception.ResourceNotFoundException;
import gov.nist.ixe.templates.jaxb.Link;
import gov.nist.ixe.templates.templateservices.tests.TemplateServicesTests;
import gov.nist.ixe.templates.tests.Examples;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.junit.Test;

public abstract class Tests extends TemplateServicesTests {
	

	@Test (expected=ResourceNotFoundException.class)
	public void gettingATemplateOnAnEmptyServiceShouldThrowResourceNotFound() {
		
		ts.createService("service0");
		ts.getTemplate("service0");
	}
	

	@Test (expected=ResourceNotFoundException.class)
	public void gettingATemplateOnAServiceThatDoesNotExistShouldThrowResourceNotFound() {
		ts.getTemplate("service0");	
	}
	
		
	@Test
	public void settingAndGettingATemplateRoundtripsProperly() throws UnsupportedEncodingException {
		
		ts.createService("service0");
		ts.setTemplate("service0", Examples.TEMPLATE.getData(),  Examples.TEMPLATE.getContentType("plain"));
		String roundTrip = StringSourceConverters.fromResponse(ts.getTemplate("service0")).getString();
		assertEquals(Examples.TEMPLATE.getString(), roundTrip);
	}
	
	@Test
	public void settingAndGettingATemplateSetsRelHeaderProperly() throws UnsupportedEncodingException {		
		ts.createService("service0");
		ts.setTemplate("service0", Examples.TEMPLATE.getData(),  Examples.TEMPLATE.getContentType("plain"));		
		assertEquals(ts.getTemplate("service0").getHeaderString(Constants.HttpHeader.REL), Constants.Rel.TEMPLATE);
	}

	
	
	@Test
	public void settingATemplateProvidesAResourceLink() {
		
		String serviceName = "service0";
		ts.createService(serviceName);
		ts.setTemplate(serviceName, Examples.TEMPLATE.getData(), Examples.TEMPLATE.getContentType("plain"));
		List<Link> templates = ts.getServiceResources(serviceName).getTemplateLinks();
		
		assertEquals(1, templates.size());
		assertEquals(Constants.TEMPLATE_RESOURCE_NAME, templates.get(0).getName());
		assertEquals(Constants.Rel.TEMPLATE, templates.get(0).getRel());
		assertEquals(BuildUri.getTemplateUri(serverRootUri, serviceName), templates.get(0).getUri());
			
	}
	
	@Test
	public void deletingATemplateThatDoesNotExistIsAllowed() {
		
		ts.createService("service0");
		ts.deleteTemplate("service0");		
	}
	
	@Test
	public void deletingATemplateWorks() {
		String serviceName = "service0";
		
		ts.createService(serviceName);
		ts.setTemplate(serviceName, Examples.TEMPLATE.getData(), Examples.TEMPLATE.getContentType("plain"));
		
		int template_count_before_delete = ts.getServiceResources(serviceName).getTemplateLinks().size();
		ts.deleteTemplate(serviceName);		
		int template_count_after_delete = ts.getServiceResources(serviceName).getTemplateLinks().size();
		
		assertEquals(1, template_count_before_delete);
		assertEquals(0, template_count_after_delete);
	}
	
	
	
	@Test (expected=IllegalContentTypeException.class)
	public void settingATemplateWithAnInvalidContentTypeIsForbidden() {
		String serviceName = "service0";			
		ts.createService(serviceName);
		ts.setTemplate(serviceName, Examples.TEMPLATE.getData(), "notavalidcontenttype");
	}
	
	@Test (expected=IllegalContentTypeException.class)
	public void settingATemplateWithAnInvalidCharsetIsForbidden() {		
		String serviceName = "service0";	
		ts.createService(serviceName);
		ts.setTemplate(serviceName, Examples.TEMPLATE.getData(), "text/plain; charset=badcharsetname");
	}
	
	@Test
	public void settingATemplateWithAnEmptyCharsetTriggersCharsetDetection() {
		String serviceName = "service0";	
		ts.createService(serviceName);
		ts.setTemplate(serviceName, Examples.TEMPLATE.getData(), "text/plain");
		StringSource rt = StringSourceConverters.fromResponse(ts.getTemplate(serviceName));		
		assertEquals(Examples.TEMPLATE.getCharset(), rt.getCharset());		
	}
	
}
