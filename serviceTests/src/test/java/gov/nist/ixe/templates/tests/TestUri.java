package gov.nist.ixe.templates.tests;

import java.lang.management.ManagementFactory;

import org.junit.Ignore;

@Ignore
public final class TestUri {
	
	// We have separate client and server URLs in case we want to 
	// use an HTTP debugging proxy (like Fiddler). 
	
	private TestUri(){}
	
	
	private static int getProcessId() {
		return Integer.parseInt(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
	}
	
	private static int getProcessUniquePort() {
		int min = 1024;
		int max = 65535;
		
		return (getProcessId() % (max - min + 1)) + min;
	}
	
	
	private static final int uniquePort = getProcessUniquePort();
	
	
	public static String getServerUri() {
		return "http://localhost:" + uniquePort + "/";
	}
	
	public static String getClientUri() {
		// return "http://localhost:8888/"; // Comment this line out for, say Fiddler
		return "http://localhost:" + uniquePort + "/";
	}
}
