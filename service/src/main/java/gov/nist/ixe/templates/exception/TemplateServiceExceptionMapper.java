package gov.nist.ixe.templates.exception;

import gov.nist.ixe.StringUtil;
import gov.nist.ixe.templates.jaxb.ExceptionResult;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public abstract class TemplateServiceExceptionMapper<E extends Throwable> 
implements ExceptionMapper<E> {

	public static final String COAT_EXCEPTION_TYPE = "X-COAT-Exception-Type";
	public static final String COAT_EXCEPTION_MESSAGE = "X-COAT-Exception-Message";

	public abstract int getStatusCode();
	public abstract Class<E> getClassLiteral();

	public Response toResponse(E ex) {

		return Response.status(getStatusCode()).				
				header(COAT_EXCEPTION_MESSAGE, StringUtil.removeNewlinesAndTabs(ex.getMessage())).
				header(COAT_EXCEPTION_TYPE, getClassLiteral().getSimpleName()).
				entity(new ExceptionResult<E>(getClassLiteral(), ex)).
				build();
	}

}
