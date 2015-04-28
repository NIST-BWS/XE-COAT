package gov.nist.ixe.templates.exception;

import gov.nist.ixe.StringUtil;
import gov.nist.ixe.templates.Constants;
import gov.nist.ixe.templates.jaxb.ExceptionResult;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

public abstract class TemplateServiceExceptionMapper<E extends Throwable> 
implements ExceptionMapper<E> {

	
	public abstract Status getStatusCode();
	public abstract Class<E> getClassLiteral();

	public Response toResponse(E ex) {

		return Response.status(getStatusCode()).				
				header(Constants.HttpHeader.EXCEPTION_MESSAGE, StringUtil.removeNewlinesAndTabs(ex.getMessage())).
				header(Constants.HttpHeader.EXCEPTION_TYPE, getClassLiteral().getSimpleName()).
				entity(new ExceptionResult<E>(getClassLiteral(), ex)).
				build();
	}

}
