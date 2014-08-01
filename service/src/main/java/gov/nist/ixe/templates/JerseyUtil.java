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
			
			initParams.put("jersey.config.server.tracing","ALL"); // Jersey 2.7
			initParams.put("jersey.config.server.tracing.type", "ALL");
			
			initParams.put("jersey.config.server.tracing.threshold", "TRACE");
			
			
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
