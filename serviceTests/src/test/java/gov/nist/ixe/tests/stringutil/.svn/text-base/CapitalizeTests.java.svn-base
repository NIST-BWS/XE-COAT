package gov.nist.ixe.tests.stringutil;


import gov.nist.ixe.StringUtil;

import java.util.Arrays;
import java.util.Collection;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import static org.junit.Assert.*;

@RunWith(value = Parameterized.class)
public class CapitalizeTests {


	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] {
				{ "Fluffy", "fluffy", 0 },
				{ "bunny", "bunny", 8 },
				{ "discO", "disco", 4 },
				{ "frankenStein", "frankenstein", 7},
				{ "3h n0", "3h n0", 0} 
		};
		return Arrays.asList(data);
	}

	private String expected;
	private String source;
	private int index;
	
	public CapitalizeTests(String expected, String source, int index) {
		this.expected = expected;
		this.source = source;
		this.index = index;
	}
	
	@Test
	public void capitalizeWorksCorrectly() {
		assertEquals(expected, StringUtil.capitalize(source, index));
	}


}
