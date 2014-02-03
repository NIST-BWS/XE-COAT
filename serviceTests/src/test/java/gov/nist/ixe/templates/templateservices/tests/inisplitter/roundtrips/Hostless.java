package gov.nist.ixe.templates.templateservices.tests.inisplitter.roundtrips;

import gov.nist.ixe.templates.templateservices.tests.HostlessTemplateServicesTestFixture;
import gov.nist.ixe.templates.templateservices.tests.ITemplateServicesTestFixture;

public class Hostless extends Tests {
	
	public Hostless(String iniFilename, String serviceName) {
		super(iniFilename, serviceName);
	}
	
	private HostlessTemplateServicesTestFixture fixture;
	
	public ITemplateServicesTestFixture getTestFixture() {
		if (fixture == null) {
			fixture = new HostlessTemplateServicesTestFixture();
		}
		return fixture;
	}
	
}
