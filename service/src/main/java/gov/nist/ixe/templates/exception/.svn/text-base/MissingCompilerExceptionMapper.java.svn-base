package gov.nist.ixe.templates.exception;

import javax.ws.rs.core.Response.Status;

public class MissingCompilerExceptionMapper 
	extends TemplateServiceExceptionMapper<MissingCompilerException> {

		public Status getStatusCode() { return Status.SERVICE_UNAVAILABLE; }
		public Class<MissingCompilerException> getClassLiteral() {	return MissingCompilerException.class;	}
}

	
	