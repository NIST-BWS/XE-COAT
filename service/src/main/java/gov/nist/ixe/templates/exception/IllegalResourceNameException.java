package gov.nist.ixe.templates.exception;

public class IllegalResourceNameException extends RuntimeException {

	private String _message;
	public String getMessage() { return _message; }

	private IllegalResourceNameException() {}

	public IllegalResourceNameException(String message) {
		super(message);
		_message = message;
	}
	
	public static IllegalResourceNameException ReservedName(String name) {
		IllegalResourceNameException ex = new IllegalResourceNameException();
		ex._message = "'" + name + "' is a reserved name.";
		return ex;
	}
	
	public static IllegalResourceNameException NameAlreadyExists(String name) {
		IllegalResourceNameException ex = new IllegalResourceNameException();
		ex._message = "'" + name + "' already exists.";
		return ex;
	}
	
	public static IllegalResourceNameException IllegalFormat(String name) {
		IllegalResourceNameException ex = new IllegalResourceNameException();
		ex._message = "'" + name + "' is not a valid name for a service.";
		return ex;
	}
	
	public static IllegalResourceNameException EmptyName() {
		IllegalResourceNameException ex = new IllegalResourceNameException();
		ex._message = "Serivce names must not be whitespace.";
		return ex;
	}

	private static final long serialVersionUID = 925233903904613737L;

}
