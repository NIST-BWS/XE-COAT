package gov.nist.ixe.templates.jaxb;

import static gov.nist.ixe.Logging.warn;
import gov.nist.ixe.templates.Constants;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.velocity.exception.ParseErrorException;
import org.xml.sax.SAXParseException;

import com.sun.xml.xsom.XSElementDecl;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name="ParseErrorType", namespace=Constants.NAMESPACE)
public class ParseError {
	
	private Link link;
	private int lineNumber;
	private int columnNumber;
	private String message;
	
	@XmlElement(name="link")
	public Link getLink() {
		return link;
	}
	public void setLink(Link link) {
		this.link = link;
	}
	
	@XmlElement(name="lineNumber")
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	
	@XmlElement(name="columnNumber")
	public int getColumnNumber() {
		return columnNumber;
	}
	public void setColumnNumber(int columnNumber) {
		this.columnNumber = columnNumber;
	}
	
	@XmlElement(name="message")
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public ParseError(Link link, SAXParseException spe, boolean doNotWarnOnUriMismatch) {
		
		this.link = link;
		this.columnNumber = spe.getColumnNumber();
		this.lineNumber = spe.getLineNumber();
		this.message = spe.getLocalizedMessage();
		if (link != null && !link.getUri().equals(spe.getSystemId()) && !doNotWarnOnUriMismatch) {
			warn("Link/SAXParseException URI mismatch. link=%s, spe=%s", link.getUri(), spe.getSystemId());
		}
		
	}
	
	public ParseError(Link link, ParseErrorException pee) {
		this.link = link;
		this.lineNumber = pee.getLineNumber();
		this.columnNumber= pee.getColumnNumber();
		this.message = pee.getLocalizedMessage();		
	}
	
	
	public static ParseError MainSchemaHasMultipleRootElements(Link link, XSElementDecl offendingElement) 
	{
		ParseError pe = new ParseError();
		pe.link = link;
		pe.lineNumber = offendingElement.getLocator().getLineNumber();
		pe.columnNumber = offendingElement.getLocator().getColumnNumber();
		pe.message = Constants.PRIMARY_SCHEMA_NAME + " already has a root element declared.";
		return pe;
	}
	
	
	public ParseError() {}
	
	
}
