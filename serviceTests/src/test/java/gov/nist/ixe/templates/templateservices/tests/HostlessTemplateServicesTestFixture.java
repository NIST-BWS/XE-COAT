package gov.nist.ixe.templates.templateservices.tests;

import gov.nist.ixe.templates.ITemplateServices;
import gov.nist.ixe.templates.TemplateServices;

public class HostlessTemplateServicesTestFixture 
	implements ITemplateServicesTestFixture {

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
		return "file:/";
	}
	
	@Override
	public String getServerRootUri() {
		return "file:/";
	}

	@Override
	public ITemplateServices getTemplateServices() {
		return templateServices;
	}
	
	private static ITemplateServices templateServices = new TemplateServices();


}
