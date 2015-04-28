package gov.nist.ixe.templates;

import gov.nist.ixe.templates.jaxb.Link;

public class CodeGenException extends Exception {

	public CodeGenException(Link link, SchemaCompilerErrorListener scel, boolean doNotWarnOnUriMismatch) {
		this.link = link;
		this.scel = scel;
		this.doNotWarnOnUriMismatch = doNotWarnOnUriMismatch;
	}
	
	private Link link;
	public Link getLink() {
		return link;
	}
	
	private SchemaCompilerErrorListener scel;
	public SchemaCompilerErrorListener getSchemaCompilerErrorListener() {
		return scel;
	}
	
	private boolean doNotWarnOnUriMismatch;
	public boolean getDoNotWarnOnUriMismatch() {
		return doNotWarnOnUriMismatch;
	}
	private static final long serialVersionUID = -3952524194470837180L;

}
