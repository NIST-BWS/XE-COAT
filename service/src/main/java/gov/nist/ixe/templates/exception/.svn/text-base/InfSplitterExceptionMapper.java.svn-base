package gov.nist.ixe.templates.exception;

import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

@Provider
public class InfSplitterExceptionMapper extends
		TemplateServiceExceptionMapper<InfSplitterException> {

	public Status getStatusCode() {
		return Status.INTERNAL_SERVER_ERROR;
	}

	public Class<InfSplitterException> getClassLiteral() {
		return InfSplitterException.class;
	}

}