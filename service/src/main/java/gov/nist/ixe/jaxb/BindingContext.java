package gov.nist.ixe.jaxb;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

public final class BindingContext {
	private BindingContext() { }
	
	static {
		contextMap = new HashMap<Class<?>, JAXBContext>();
	}
	
	public static Map<Class<?>, JAXBContext> contextMap;
	
	public static JAXBContext getBindingContext(Class<?> c) throws JAXBException {
		if (!contextMap.containsKey(c))
			contextMap.put(c, JAXBContext.newInstance(c));
		
		return contextMap.get(c);
	}
}