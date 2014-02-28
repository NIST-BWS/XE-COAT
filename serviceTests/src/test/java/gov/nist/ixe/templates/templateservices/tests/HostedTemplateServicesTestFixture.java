package gov.nist.ixe.templates.templateservices.tests;

import gov.nist.ixe.templates.ITemplateServices;
import gov.nist.ixe.templates.JerseyUtil;
import gov.nist.ixe.templates.TemplateServices;
import gov.nist.ixe.templates.TemplateServicesClient;
import gov.nist.ixe.templates.tests.TestConstants;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class HostedTemplateServicesTestFixture implements
		ITemplateServicesTestFixture {


	@Override
	public void before() {
		TemplateServices.CleanStorage();
	}

	@Override
	public void after() {		
		TemplateServices.CleanStorage();
	}

	@Override
	public String getClientRootUri() {
		return TestConstants.CLIENT_URI;
	}
	
	@Override
	public String getServerRootUri() {
		return TestConstants.SERVER_URI;
	}

	@Override
	public ITemplateServices getTemplateServices() {
		return client;
	}
	
	private static org.glassfish.grizzly.http.server.HttpServer grizzlyServer = null; 
	private static ITemplateServices client = null;
	
	@BeforeClass public static void beforeClass() {
		System.setProperty("jersey.config.server.tracing", "ALL");
		System.setProperty("jersey.config.server.tracing.threshold", "VERBOSE");
		
		try {
			grizzlyServer = JerseyUtil.createGrizzlyServer(new URI(TestConstants.SERVER_URI));
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		client = createClient();
	}
	
	@AfterClass public static void afterClass() {
		if (grizzlyServer != null) grizzlyServer.shutdownNow();
	}
	
	
	
	private static TemplateServicesClient createClient() {
		return new HostedTemplateServicesClient(TestConstants.CLIENT_URI);
	}
}
