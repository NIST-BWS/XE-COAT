package gov.nist.ixe.templates.tests;

import org.junit.Ignore;

@Ignore
public final class TestConstants {
	
	// We have separate client and server URLs in case we want to 
	// use an HTTP debugging proxy (like Fiddler). The CLIENT_URI would be the URI
	// that the client uses to talk to the true server, which would
	// be running on SERVER_URI. 

	public static final String SERVER_URI = "http://localhost:12345/";
	
	public static final String CLIENT_URI = "http://localhost:12345/";
	//public static final String CLIENT_URI = "http://localhost:8888/";
	
	private TestConstants(){}
}
