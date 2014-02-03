package gov.nist.ixe.templates.templateservices.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nist.ixe.templates.ITemplateServices;
import gov.nist.ixe.templates.jaxb.Link;

import org.junit.After;
import org.junit.Before;

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
		rootUri = getTestFixture().getRootUri();
	}

	// protected ITemplateServices ts;
	protected ITemplateServices ts;
	protected String rootUri;

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
		};
	}

}
