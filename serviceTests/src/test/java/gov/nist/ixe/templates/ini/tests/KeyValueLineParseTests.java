package gov.nist.ixe.templates.ini.tests;

import static org.junit.Assert.*;
import gov.nist.ixe.templates.ini.KeyValueLine;

import org.junit.Test;

public class KeyValueLineParseTests {

	@Test
	public void DelimitersAreDetectedAsExcpected() {
		assertEquals(",", KeyValueLine.parse("commaDelim = a,b,c", 0).getDelimiter());
		assertEquals(" ", KeyValueLine.parse("spaceDelim=\"a\" \"b\" \"c\"", 0).getDelimiter());
		assertEquals(", ", KeyValueLine.parse("commaSpaceDelim=this, is, a, test", 0).getDelimiter());
		assertEquals(",", KeyValueLine.parse("commaDelim2 = (a),(b),(c)", 0).getDelimiter());
		assertEquals("", KeyValueLine.parse("spaceDelim2 = a b c", 0).getDelimiter());
		
		
	}

}
