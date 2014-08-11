package gov.nist.ixe.templates.ini.tests;

import gov.nist.ixe.templates.ini.KeyValueLine;
import gov.nist.ixe.templates.ini.exception.IniParseException;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class BadKeyValueLineParseTests {
	
	private String badLine;
	
	public BadKeyValueLineParseTests(String badLine) {
		this.badLine = badLine;
	}
	
	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] {
				{ "key with space = value" },
				{ "[section]" }							
		};
		return Arrays.asList(data);
	}

	@Test (expected=IniParseException.class)
	public void BadKeyValueLinesThrowExceptions() {
		KeyValueLine.parse(badLine, 0);				
	}

}
