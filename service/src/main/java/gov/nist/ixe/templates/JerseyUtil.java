package gov.nist.ixe.templates;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.ProcessingException;

import org.glassfish.jersey.grizzly2.servlet.GrizzlyWebContainerFactory;

public class JerseyUtil {
	
	
	public static org.glassfish.grizzly.http.server.HttpServer createGrizzlyServer(URI uri)  {
		
		org.glassfish.grizzly.http.server.HttpServer server = null;
		try {		
			Map<String,String> initParams = new HashMap<String,String>();
			
			// initParams.put("jersey.config.server.tracing","ALL"); // Jersey 2.7
			// initParams.put("jersey.config.server.tracing.type", "ALL"); // Jersey 2.8+			
			// initParams.put("jersey.config.server.tracing.threshold", "TRACE");
			
			
			// Somewhere between Jersey 2.7 and 2.8, setting this to 'true' became necessary
			// for responses to be left as-is. Comment this out, and grizzly will return HTML
			// when the result is not 200. Strangely, the Jersey 2.11 documentation states that this
			// parameter was introduced in 2.5. 
			//
			initParams.put("jersey.config.server.response.setStatusOverSendError", "true");			
			
			initParams.put("jersey.config.server.provider.packages", "gov.nist.ixe.templates");
			server = GrizzlyWebContainerFactory.create(uri, initParams);
			
			//server = GrizzlyHttpServerFactory.createHttpServer(uri,createResourceConfig());
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (ProcessingException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		
		return server;
	}
	

	

}
