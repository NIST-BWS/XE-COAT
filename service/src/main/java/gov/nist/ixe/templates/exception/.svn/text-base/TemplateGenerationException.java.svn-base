package gov.nist.ixe.templates.exception;

import gov.nist.ixe.templates.CodeGenException;
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
	
	public TemplateGenerationException(String serviceName, String resourceName) {
		super();
		_serviceName = serviceName;
		_resourceName = resourceName;	
	} 
	
	public TemplateGenerationException(String serviceName, String resourceName, Exception e) {
		super(e);
		_serviceName = serviceName;
		_resourceName = resourceName;		
	}
	
	public static TemplateGenerationException mainSchemaHasMultipleRootElements(String serviceName, String resourceName, Link link, XSElementDecl offendingElement) {
		
		TemplateGenerationException ex = new TemplateGenerationException(serviceName, resourceName);
		ex._serviceName = serviceName;
		ex._resourceName = resourceName;
		ex.parseErrors.add(ParseError.mainSchemaHasMultipleRootElements(link, offendingElement));
		
		return ex;
	}
	
	public TemplateGenerationException(String serviceName, String resourceName, CodeGenException gce) {		
		_serviceName = serviceName;
		_resourceName = resourceName;		
		for (SAXParseException se : gce.getSchemaCompilerErrorListener().getErrors()) {
			parseErrors.add(new ParseError(gce.getLink(), se, gce.getDoNotWarnOnUriMismatch()));
		}		
	}


	public TemplateGenerationException(String serviceName, String resourceName, TemplateGenerationError tge) {
		_serviceName = serviceName;
		_resourceName = resourceName;		
		for (ParseError pe : tge.getErrors()) {
			parseErrors.add(pe);
		}
	}
	
	public TemplateGenerationException(String serviceName, String resourceName, Link link, ParseErrorException pee) {
		_serviceName = serviceName;
		_resourceName = resourceName;
		ParseError pe = new ParseError(link, pee);
		parseErrors.add(pe);
	}
	
	
	@XmlElement(name="parseError")
	public List<ParseError> getParseErrors() { return this.parseErrors; }
	
	private List<ParseError> parseErrors = new ArrayList<ParseError>();
	
	private String _message;
	public String getMessage() { return _message; }
	
	@XmlElement(name="resourceName")
	private String _resourceName;
	public String getResourceName() { return _resourceName; }
	public void setResourceName(String resourceName) { _resourceName = resourceName; }
	
	private String _serviceName;
	public String getServiceName() { return _serviceName; }
		
	private static final long serialVersionUID = -8319988350710918994L;
		
		
}
