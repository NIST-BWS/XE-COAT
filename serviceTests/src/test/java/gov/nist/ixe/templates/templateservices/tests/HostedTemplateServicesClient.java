package gov.nist.ixe.templates.templateservices.tests;

import gov.nist.ixe.templates.TemplateServicesClient;

import java.util.ArrayList;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.junit.runner.Description;

public class HostedTemplateServicesClient extends TemplateServicesClient 
{

	public HostedTemplateServicesClient(String rootUri) {
		super(rootUri);	
	}

	private Description description;
	public void setDescription(Description description) {
		this.description = description;
	}
	
	public <T>
	T makeRequest(String uri, String method, Class<T> returnTypeClass, byte[] payload, 
			String payloadContentType) {
		
		MultivaluedMap<String,Object> headers = new MultivaluedHashMap<String,Object>();
		
		ArrayList<Object> testClass = new ArrayList<Object>();
		testClass.add(description.getClassName());
		headers.put("X-COAT-JUnitTestClass", testClass);
		
		ArrayList<Object> testMethod = new ArrayList<Object>();
		testMethod.add(description.getMethodName());
		headers.put("X-COAT-JUnitTestMethod", testMethod);
		
				
		return makeRequest(uri, method, returnTypeClass, payload, payloadContentType, headers);
	}


}
