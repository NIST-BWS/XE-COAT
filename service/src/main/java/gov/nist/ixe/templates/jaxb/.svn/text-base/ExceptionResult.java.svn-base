package gov.nist.ixe.templates.jaxb;

import gov.nist.ixe.templates.Constants;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="exceptionResult", namespace=Constants.NAMESPACE)
@XmlType(name="ExceptionResultType", namespace=Constants.NAMESPACE)
public class ExceptionResult<E extends Throwable> {
	
	public ExceptionResult() {}

	public ExceptionResult(Class<E> classLiteral, E e) {
		this.classLiteral = classLiteral.getSimpleName();
		this.message = e.getMessage();
		
	}
	
	private String classLiteral;
	private String message;
	
	@XmlElement(name="classLiteral")
	public String getClassLiteral() {
		return classLiteral;
	}
	
	@XmlElement(name="message")
	public String getMessage() {
		return message;
	}
	
}
