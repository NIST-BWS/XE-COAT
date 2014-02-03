package gov.nist.ixe.templates;

import gov.nist.ixe.templates.jaxb.Link;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.SAXParseException;

import com.sun.tools.xjc.api.ErrorListener;

public class SchemaCompilerErrorListener implements ErrorListener {
	
	public SchemaCompilerErrorListener(Link linkToOriginalSchema) {
		this.linkToOriginalSchema = linkToOriginalSchema;
	}
	
	private List<SAXParseException> errors = new ArrayList<SAXParseException>();
	public List<SAXParseException> getErrors() {
		return errors;
	}
	public void error(SAXParseException arg0) {
		errors.add(arg0);
	}

	public void fatalError(SAXParseException arg0) {
		errors.add(arg0);
	}

	private List<SAXParseException> info = new ArrayList<SAXParseException>(); ;
	public List<SAXParseException> getInfo() {
		return this.info;
	}
	public void info(SAXParseException arg0) {
		this.info.add(arg0);
	}

	private List<SAXParseException> warnings = new ArrayList<SAXParseException>();
	public List<SAXParseException> getWarnings() {
		return warnings;
	}
	public void warning(SAXParseException arg0) {
		warnings.add(arg0);
	}
	
	private Link linkToOriginalSchema;
	public Link getLink() {
		return linkToOriginalSchema;
	}
	

}
