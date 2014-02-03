package gov.nist.ixe.templates.exception;


public class IllegalPayloadException extends RuntimeException {

	private static final long serialVersionUID = 6024803273450549305L;
	private String _message;
	public String getMessage() { return _message; }

	public IllegalPayloadException() {
		_message = ILLEGAL_PAYLOAD_MESSAGE;
	}

	public IllegalPayloadException(String message) {
		super(message);
	}
	
	public IllegalPayloadException(Exception e) {
		super(e);
	}

	public static final String ILLEGAL_PAYLOAD_MESSAGE= "Illegal payload.";

	

}
