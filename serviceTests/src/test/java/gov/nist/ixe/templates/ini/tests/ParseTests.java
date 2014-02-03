package gov.nist.ixe.templates.ini.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import gov.nist.ixe.templates.ini.Parse;

import org.junit.Test;

public class ParseTests {
	
	@Test
	public void SubstantiveLinesAreDetectedCorrectly() {
		assertTrue(Parse.isSubstantiveLine("[SQLSERVER2008]"));
		assertTrue(Parse.isSubstantiveLine("PID=\"12345-ABCDE-67890-FABCD-12345\""));
		assertTrue(Parse.isSubstantiveLine("FEATURES=SQLENGINE,RS,SSMS,ADV_SSMS"));
		assertTrue(Parse.isSubstantiveLine("HELP=False ; comment"));
		assertTrue(Parse.isSubstantiveLine(" [ s e c t i o n ] "));
		
		assertFalse(Parse.isSubstantiveLine(";SQLSERVER2008 Configuration File"));
		assertFalse(Parse.isSubstantiveLine(";;;"));
		assertFalse(Parse.isSubstantiveLine(" ;comment"));
		assertFalse(Parse.isSubstantiveLine(""));
		assertFalse(Parse.isSubstantiveLine(" "));
		assertFalse(Parse.isSubstantiveLine("     "));
		assertFalse(Parse.isSubstantiveLine("     \r\n\t"));
		
	}
	
	@Test 
	public void HeadersAreExtractedCorrectly() {
		assertEquals("SQLSERVER2008", Parse.getHeader("[SQLSERVER2008]"));
		assertEquals("SQLSERVER2008", Parse.getHeader(" [ SQLSERVER2008 ] ; comment"));
		assertEquals("SQLSERVER2008", Parse.getHeader(" [ SQLSERVER2008 ];comment"));
	
		assertEquals("s e c t i o n", Parse.getHeader("[ s e c t i o n ]"));
		assertEquals("s e c t i o n", Parse.getHeader("[ s e c t i o n ];;;"));
		assertEquals("s e c t i o n", Parse.getHeader("[ s e c t i o n ] ; comment"));
	
		assertEquals(null, Parse.getHeader("[]"));
		assertEquals(null, Parse.getHeader("[SQLSERVER2008"));
		assertEquals(null, Parse.getHeader("[SQLSERVER2008;]"));
		assertEquals(null, Parse.getHeader(""));
		assertEquals(null, Parse.getHeader(" "));
				
	}
	

	
	
	
}
