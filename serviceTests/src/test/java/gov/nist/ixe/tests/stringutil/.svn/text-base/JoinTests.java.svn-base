package gov.nist.ixe.tests.stringutil;

import static org.junit.Assert.assertEquals;
import gov.nist.ixe.StringUtil;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class JoinTests {


	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] {
				{ "a",    new String[] { "a" },      "XYZ" },
				{ "a, b", new String[] { "a", "b" }, ", " },
				{ "a|b",  new String[] { "a", "b" }, "|" },
		};
		return Arrays.asList(data);
	}

	private String expected;

	private String[] source;
	private String delim;
	
	
	public JoinTests(String expected, String[] source, String delim) {
		this.expected = expected;
		this.source = source;	
		this.delim = delim;
	}
	
	@Test
	public void capitalizeWorksCorrectly() {
		assertEquals(expected, StringUtil.join(source, delim));
	}


}

