package gov.nist.ixe.templates;

import gov.nist.ixe.templates.jaxb.Link;

public class CodeGenException extends Exception {

	
	public CodeGenException(Link link, SchemaCompilerErrorListener scel, boolean doNotWarnOnUriMismatch) {
		_link = link;
		_scel = scel;
		_doNotWarnOnUriMismatch = doNotWarnOnUriMismatch;
	}
	
	private Link _link;
	public Link getLink() {
		return _link;
	}
	
	private SchemaCompilerErrorListener _scel;
	public SchemaCompilerErrorListener getSchemaCompilerErrorListener() {
		return _scel;
	}
	
	private boolean _doNotWarnOnUriMismatch;
	public boolean getDoNotWarnOnUriMismatch() {
		return _doNotWarnOnUriMismatch;
	}
	private static final long serialVersionUID = -3952524194470837180L;

}
