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
		ArrayList<Object> values = new ArrayList<Object>();
		values.add(description.getClassName() + "." + description.getMethodName());
		headers.put("X-COAT-UnitTestName", values);
				
		return makeRequest(uri, method, returnTypeClass, payload, payloadContentType, headers);
	}


}
