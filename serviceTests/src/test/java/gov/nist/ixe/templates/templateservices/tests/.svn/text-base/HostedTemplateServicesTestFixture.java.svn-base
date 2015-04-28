package gov.nist.ixe.templates.templateservices.tests;

import gov.nist.ixe.templates.ITemplateServices;
import gov.nist.ixe.templates.JerseyUtil;
import gov.nist.ixe.templates.TemplateServices;
import gov.nist.ixe.templates.TemplateServicesClient;
import gov.nist.ixe.templates.tests.TestUri;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.AfterClass;
import org.junit.BeforeClass;

public class HostedTemplateServicesTestFixture implements
		ITemplateServicesTestFixture {


	@Override
	public void before() {
		TemplateServices.cleanStorage();
	}

	@Override
	public void after() {		
		TemplateServices.cleanStorage();
	}

	@Override
	public String getClientRootUri() {
		return TestUri.getClientUri();
	}
	
	@Override
	public String getServerRootUri() {
		return TestUri.getServerUri();
	}

	@Override
	public ITemplateServices getTemplateServices() {
		return client;
	}
	
	public TemplateServicesClient getTemplateServicesClient() {
		return client;
	}
	
	private static org.glassfish.grizzly.http.server.HttpServer grizzlyServer = null; 
	private static TemplateServicesClient client = null;
	
	@BeforeClass public static void beforeClass() {
		
		try {
			grizzlyServer = JerseyUtil.createGrizzlyServer(new URI(TestUri.getServerUri()));
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		client = createClient();
	}
	
	@AfterClass public static void afterClass() {
		if (grizzlyServer != null) grizzlyServer.shutdownNow();
	}
	
	
	
	private static TemplateServicesClient createClient() {
		return new HostedTemplateServicesClient(TestUri.getClientUri());
	}
}
