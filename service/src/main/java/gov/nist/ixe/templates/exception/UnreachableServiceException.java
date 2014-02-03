package gov.nist.ixe.templates.exception;

public class UnreachableServiceException extends RuntimeException {
	
	// This is a wrapper for exceptions or conditions that should never 
	// be thrown from the service. If they are, then there has been an implementation logic error.
	
	public UnreachableServiceException() {} 
	
	public UnreachableServiceException(String originalExceptionSimpleName, Exception originalException) {
		this.originalException = originalException;
		this.originalExceptionSimpleName = originalExceptionSimpleName;
	}

	public Exception getOriginalException() {
		return originalException;
	}
	
	public String getOriginalExceptionSimpleName() {
		return originalExceptionSimpleName;
	}

	private Exception originalException; 
	private String originalExceptionSimpleName;
	
	private static final long serialVersionUID = 4935830976138905771L;

}
