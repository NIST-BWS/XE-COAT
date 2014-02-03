package gov.nist.ixe.templates.templateservices.tests.inisplitter.roundtrips;

import org.junit.BeforeClass;
import org.junit.AfterClass;

import gov.nist.ixe.templates.templateservices.tests.HostedTemplateServicesTestFixture;
import gov.nist.ixe.templates.templateservices.tests.ITemplateServicesTestFixture;

public class Hosted extends Tests {
	
	public Hosted(String iniFilename, String serviceName) {
		super(iniFilename, serviceName);
	}

	private HostedTemplateServicesTestFixture fixture;
		
	public ITemplateServicesTestFixture getTestFixture() {
		if (fixture == null) {
			fixture = new HostedTemplateServicesTestFixture();
		}
		return fixture;
	}
	
	@BeforeClass public static void beforeClass() {
		HostedTemplateServicesTestFixture.beforeClass();
	}
	
	
	@AfterClass public static void afterClass() {
		HostedTemplateServicesTestFixture.afterClass();
	}
	
	
}
