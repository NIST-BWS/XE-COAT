package gov.nist.ixe.templates.templateservices.tests.parsing;

import gov.nist.ixe.templates.templateservices.tests.HostlessTemplateServicesTestFixture;
import gov.nist.ixe.templates.templateservices.tests.ITemplateServicesTestFixture;

public class Hostless extends Tests {

	public Hostless(ProcessStyle style, String resourceName) {
		super(style, resourceName);
	}

	private HostlessTemplateServicesTestFixture fixture;
	
	public ITemplateServicesTestFixture getTestFixture() {
		if (fixture == null) {
			fixture = new HostlessTemplateServicesTestFixture();
		}
		return fixture;
	}
	
}
