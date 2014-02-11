package gov.nist.ixe.templates.exception;

public class InfSplitterException extends RuntimeException {
	
	private String _message;
	public String getMessage() { return _message; }
	
	public InfSplitterException() {}
	
	public InfSplitterException(Exception e) {
		super(e);
	}
	
	public static InfSplitterException CannotOverwriteExistingTemplate() {
		InfSplitterException ex = new InfSplitterException();
		ex._message = CANNOT_OVERWRITE_EXISTING_TEMPLATE;
		return ex;
	}
	
	
	public static final String CANNOT_OVERWRITE_EXISTING_TEMPLATE = 
			"Cannot overwrite existing template.";
	
	private static final long serialVersionUID = -2618591351584770954L;

}
