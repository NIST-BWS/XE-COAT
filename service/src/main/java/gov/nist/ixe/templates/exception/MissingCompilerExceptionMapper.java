package gov.nist.ixe.templates.exception;

public class MissingCompilerExceptionMapper 
	extends TemplateServiceExceptionMapper<MissingCompilerException> {

		public int getStatusCode() { return 503; }
		public Class<MissingCompilerException> getClassLiteral() {	return MissingCompilerException.class;	}
}

	
	