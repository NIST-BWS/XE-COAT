package gov.nist.ixe.templates.templateservices.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nist.ixe.templates.ITemplateServices;
import gov.nist.ixe.templates.jaxb.Link;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public abstract class TemplateServicesTests {

	public abstract ITemplateServicesTestFixture getTestFixture();

	@Before
	public void before() {
		getTestFixture().before();
	}

	@After
	public void after() {
		getTestFixture().after();
	}

	public TemplateServicesTests() {
		ts = getTestFixture().getTemplateServices();		
		clientRootUri = getTestFixture().getClientRootUri();
		serverRootUri = getTestFixture().getServerRootUri();
	}

	// protected ITemplateServices ts;
	protected ITemplateServices ts;
	
	// We use both a client and server root URI to support the use of a
	// debugging proxy
	//
	protected String clientRootUri; 
	protected String serverRootUri;

	public static void assertLinkEquals(String expectedName,
			String expectedRel, String expectedUri, Link link) {
		assertEquals(expectedName, link.getName());
		assertEquals(expectedRel, link.getRel());
		assertEquals(expectedUri, link.getUri());
	}

	public static void assertWithin(long value, long min, long max) {
		assertTrue(min <= value && value <= max);
	}

	public static Object[][] getRoundTripFileServiceNamePairs() {
		return new Object[][] { 
				{ "txt/roundtrip/edges.ini", "Edges" },
				{ "txt/roundtrip/php.ini", "PHPINI" },
				{ "txt/roundtrip/dummy.ini", "dummy" },
				{ "txt/roundtrip/issuing-CAPolicy.inf", "issuing" },
				{ "txt/roundtrip/policy-sha1-CAPolicy.inf", "policySha1" },
				{ "txt/roundtrip/SQLConfigForSccm.ini", "SQLConfigForSccm" },
				{ "txt/roundtrip/policy-sha256-CAPolicy.inf", "policySha256" },
				{ "txt/roundtrip/multiquote.ini", "multiquote" },
				{ "txt/roundtrip/sample01.inf", "sample01" },
				{ "txt/roundtrip/sample02.inf", "sample02" },
				{ "txt/roundtrip/numbers.ini", "numbers" },
				{ "txt/roundtrip/nullTests.ini", "nullTests" },
				{ "txt/roundtrip/escapeChars.ini", "escapeChars" },
				{ "txt/roundtrip/1startsWithNumber.ini", "1startsWithNumber" }
				
		};
	}
	
	
	
	@Rule
	public TestRule watcher = new TestWatcher() {
		@Override protected void starting(Description description) {
			if (ts instanceof HostedTemplateServicesClient) {
				
				// Not the most elegant way to do this, but the test rule needs to be in the 
				// fixture, and we already have a lot of duplication. What we're doing
				// here is putting the name of the running test in the request headers.
				// This is very useful when looking at the captures of the HTTP traffic
				// with an HTTP debugging proxy (like Fiddler2).
				//
				((HostedTemplateServicesClient) ts).setDescription(description);
			}
	   }
	};

}
