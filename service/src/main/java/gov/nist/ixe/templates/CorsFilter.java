package gov.nist.ixe.templates;

import gov.nist.ixe.templates.exception.TemplateServiceExceptionMapper;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class CorsFilter implements ContainerResponseFilter {

	
	private String ALLOW_HEADERS = 
			TemplateServiceExceptionMapper.COAT_EXCEPTION_TYPE + ", " + 
			Constants.HttpHeader.REL + ", " +
			Constants.HttpHeader.SERVICE_NAME + ", " +
			Constants.HttpHeader.RESOURCE_NAME + ", " +
			Constants.HttpHeader.HISTORIC_VERSION_OF + ", " +
			TemplateServiceExceptionMapper.COAT_EXCEPTION_MESSAGE;

	@Override
	public void filter(ContainerRequestContext request,
					   ContainerResponseContext response) throws IOException {

		
		String origin = request.getHeaderString("Origin");
		if (origin == null) origin = "*";
		response.getHeaders().add("Access-Control-Allow-Origin", origin);
		response.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS, DELETE, HEAD");
		response.getHeaders().add("Access-Control-Expose-Headers", ALLOW_HEADERS);
		if (null != request && null != request.getHeaders()) {
			String requestHeaders = request.getHeaderString("Access-Control-Request-Headers");
			response.getHeaders().add("Access-Control-Allow-Headers", requestHeaders);
		}
	}

}