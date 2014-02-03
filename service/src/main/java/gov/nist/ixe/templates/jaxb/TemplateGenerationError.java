package gov.nist.ixe.templates.jaxb;

import java.util.ArrayList;
import java.util.List;

import gov.nist.ixe.templates.Constants;
import gov.nist.ixe.templates.exception.TemplateGenerationException;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="templateGenerationError", namespace=Constants.NAMESPACE)
@XmlType(name="TemplateGenerationErrorType", namespace=Constants.NAMESPACE)
public class TemplateGenerationError {
	
	public TemplateGenerationError() {}
	
	public TemplateGenerationError(TemplateGenerationException tge) {
		errors = tge.getParseErrors();
	}
	
	protected List<ParseError> errors = new ArrayList<ParseError>();

	@XmlElement(name="error")
	public List<ParseError> getErrors() { return this.errors; }
	
}
