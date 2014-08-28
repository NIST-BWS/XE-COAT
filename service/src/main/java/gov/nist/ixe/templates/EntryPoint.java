package gov.nist.ixe.templates;

import static gov.nist.ixe.Logging.trace;
import gov.nist.ixe.GetOpt;
import gov.nist.ixe.GetOpt.Parameter;

import java.io.BufferedReader;
import java.net.URI;
import java.util.Map;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

public class EntryPoint {
	
	@SuppressWarnings("all")
	public static void main(String... args) throws Exception {
		trace();
		
		System.setProperty("jersey.config.server.tracing", "ALL");
		System.setProperty("jersey.config.server.tracing.threshold", "VERBOSE");		 
		
		String uri = "http://0.0.0.0:7774/";
		String storageLocation = null;
		
		Parameter p1 = new Parameter('u', "uri", true, "URI to host service upon");
		Parameter p2 = new Parameter('l', "location", true, "Location of the service data store");
		
		GetOpt go = new GetOpt(p1, p2);
		Map<Character, String> parameters = null;
		
		try {
			parameters = go.parse(args);
			
			//if (parameters != null) {
				if (parameters.containsKey('u')) {
					uri = parameters.get('u');
				}
				
				if (parameters.containsKey('l')) {
					storageLocation = parameters.get('l');
				}
			//}
			
			if (storageLocation != null) {
				StorageProviderFactory.setDefaultFileStorageProviderLocation(storageLocation);
			}
			storageLocation = StorageProviderFactory.getDefaultFileStorageProviderLocation();
			
			
			ResourceConfig rc = new ResourceConfig().packages("gov.nist.ixe");
			rc.property(ServerProperties.TRACING, "ALL");
			rc.property(ServerProperties.TRACING_THRESHOLD, "VERBOSE");
					
			System.out.println("Hosting service at endpoint " + uri);
			System.out.println("Data store location  " + storageLocation);
			
			HttpServer server = JerseyUtil.createGrizzlyServer(new URI(uri));
			
			System.out.println("Type 'quit' to terminate");
			String line = null;
			BufferedReader reader = new BufferedReader(System.console().reader());
			
			do {
				line = reader.readLine();	
			} while(!"quit".equalsIgnoreCase(line));
				
			server.shutdownNow();
			
			System.out.println("Service terminated. Exiting...");
			
			
		} catch (IllegalArgumentException iae) {
			System.out.println(iae.getMessage());
			go.printHelp();
		}
	}
}