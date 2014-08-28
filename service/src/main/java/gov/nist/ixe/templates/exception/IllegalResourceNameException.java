package gov.nist.ixe.templates.exception;

public class IllegalResourceNameException extends RuntimeException {

	private String _message;
	public String getMessage() { return _message; }

	private IllegalResourceNameException() {}

	public IllegalResourceNameException(String message) {
		super(message);
		_message = message;
	}
	
	public static IllegalResourceNameException reservedName(String name) {
		IllegalResourceNameException ex = new IllegalResourceNameException();
		ex._message = "'" + name + "' is a reserved name.";
		return ex;
	}
	
	public static IllegalResourceNameException nameAlreadyExists(String name) {
		IllegalResourceNameException ex = new IllegalResourceNameException();
		ex._message = "'" + name + "' already exists.";
		return ex;
	}
	
	public static IllegalResourceNameException illegalFormat(String name) {
		IllegalResourceNameException ex = new IllegalResourceNameException();
		ex._message = "'" + name + "' is not a valid name for a service.";
		return ex;
	}
	
	public static IllegalResourceNameException emptyName() {
		IllegalResourceNameException ex = new IllegalResourceNameException();
		ex._message = "Serivce names must not be whitespace.";
		return ex;
	}

	private static final long serialVersionUID = 925233903904613737L;

}
