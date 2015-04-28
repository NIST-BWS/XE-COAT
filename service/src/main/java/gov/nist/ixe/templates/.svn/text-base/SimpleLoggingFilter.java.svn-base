package gov.nist.ixe.templates;

import gov.nist.ixe.Logging;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

@Provider
public class SimpleLoggingFilter implements ContainerResponseFilter {

	private @Context HttpServletRequest request;

	@Override
	public void filter(ContainerRequestContext requestContext,
					   ContainerResponseContext responseContext) throws IOException {

		if (request != null) {
			Logging.http("%s %d %s %s %d", 
					request.getRemoteHost(),
					request.getLocalPort(),
					requestContext.getUriInfo().getPath(),
					requestContext.getMethod(),
					responseContext.getStatus());
		}
	

		
		
	}

}
