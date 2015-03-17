package gov.nist.ixe.templates.exception;

import gov.nist.ixe.StringUtil;
import gov.nist.ixe.templates.Constants;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

@Provider public class UnreachableServiceExceptionMapper 
	extends TemplateServiceExceptionMapper<UnreachableServiceException> {

	@Override
	public Status getStatusCode() {
		// Does not get called since this method is only used when we use the
		// default implementation of toReponse()
		//
		return Status.INTERNAL_SERVER_ERROR;
	}

	@Override
	public Class<UnreachableServiceException> getClassLiteral() {
		// Does not get called since this method is only used when we use the
		// default implementation of toReponse()	
		//
		return UnreachableServiceException.class;
	}

	public Response toResponse(UnreachableServiceException ex) {
		
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
				header(Constants.HttpHeader.EXCEPTION_TYPE, ex.getOriginalExceptionSimpleName()).
				header(Constants.HttpHeader.EXCEPTION_MESSAGE, StringUtil.removeNewlinesAndTabs(ex.getOriginalException().getMessage())).
				build();
	
				
	}
	

}

