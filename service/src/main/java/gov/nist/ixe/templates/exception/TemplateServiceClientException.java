package gov.nist.ixe.templates.exception;

public class TemplateServiceClientException extends RuntimeException {

	public TemplateServiceClientException(String message) {
		super(message);
	}
	
	public TemplateServiceClientException(Exception e) {
		super(e);	//call getCause() to get the "inner exception"
	}
	
	private static final long serialVersionUID = 3574941263163549136L;
}