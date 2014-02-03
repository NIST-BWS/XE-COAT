package gov.nist.ixe.templates.exception;

import gov.nist.ixe.templates.SchemaCompilerErrorListener;
import gov.nist.ixe.templates.jaxb.Link;
import gov.nist.ixe.templates.jaxb.ParseError;
import gov.nist.ixe.templates.jaxb.TemplateGenerationError;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import org.apache.velocity.exception.ParseErrorException;
import org.xml.sax.SAXParseException;

import com.sun.xml.xsom.XSElementDecl;

public class TemplateGenerationException extends RuntimeException {
	
	public TemplateGenerationException() {} 
	
	public TemplateGenerationException(Exception e) {
		super(e);
	}
	
	public TemplateGenerationException(SchemaCompilerErrorListener scel) {
		constructorImpl(scel, false);
	}
	
	public TemplateGenerationException(SchemaCompilerErrorListener scel, boolean doNotWarnOnUriMismatch) {
		constructorImpl(scel, doNotWarnOnUriMismatch);
	}
	
	public static TemplateGenerationException MainSchemaHasMultipleRootElements(Link link, XSElementDecl offendingElement) {
		TemplateGenerationException ex = new TemplateGenerationException();
		ex.parseErrors.add(ParseError.MainSchemaHasMultipleRootElements(link, offendingElement));
		
		return ex;
	}
	
	private void constructorImpl(SchemaCompilerErrorListener scel, boolean doNotWarnOnUriMismatch) {
		for (SAXParseException spe : scel.getErrors()) {
			parseErrors.add(new ParseError(scel.getLink(), spe, doNotWarnOnUriMismatch));
		}
	}

	public TemplateGenerationException(TemplateGenerationError tge) {
		for (ParseError pe : tge.getErrors()) {
			parseErrors.add(pe);
		}
	}
	
	public TemplateGenerationException(Link link, ParseErrorException pee) {
		ParseError pe = new ParseError(link, pee);
		parseErrors.add(pe);
	}
	
	
	@XmlElement(name="parseError")
	public List<ParseError> getParseErrors() { return this.parseErrors; }
	
	private List<ParseError> parseErrors = new ArrayList<ParseError>();
	private String _message;
	public String getMessage() { return _message; }

		private static final long serialVersionUID = -8319988350710918994L;
}
