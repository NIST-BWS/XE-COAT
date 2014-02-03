package gov.nist.ixe.templates.exception;

import gov.nist.ixe.StringUtil;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider public class UnreachableServiceExceptionMapper 
	extends TemplateServiceExceptionMapper<UnreachableServiceException> {

	@Override
	public int getStatusCode() {
		// Does not get called since this method is only used when we use the
		// default implementation of toReponse()
		//
		return 0;
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
				header(COAT_EXCEPTION_TYPE, ex.getOriginalExceptionSimpleName()).
				header(COAT_EXCEPTION_MESSAGE, StringUtil.removeNewlinesAndTabs(ex.getOriginalException().getMessage())).
				build();
	
				
	}
	

}

