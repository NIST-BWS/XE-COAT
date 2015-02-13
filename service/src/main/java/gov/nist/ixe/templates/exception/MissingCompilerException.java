package gov.nist.ixe.templates.exception;

public class MissingCompilerException extends RuntimeException {

	private String _message;
	public String getMessage() { return _message; }
	
	public MissingCompilerException() {
		_message = MISSING_COMPILER_MESSAGE;
	}

	public MissingCompilerException(String message) {
		super(message);
	}
	
	public MissingCompilerException(Exception e) {
		super(e);
	}

	public static final String MISSING_COMPILER_MESSAGE = "Java compiler could not be instantiated.";
	
	private static final long serialVersionUID = 6681086793125983421L;

}
