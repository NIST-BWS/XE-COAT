package gov.nist.ixe.templates.exception;

import javax.ws.rs.ext.Provider;

@Provider
public class InfSplitterExceptionMapper extends
		TemplateServiceExceptionMapper<InfSplitterException> {

	public int getStatusCode() {
		return 500;
	}

	public Class<InfSplitterException> getClassLiteral() {
		return InfSplitterException.class;
	}

}