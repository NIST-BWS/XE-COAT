package gov.nist.ixe.tests.stringutil;

import static org.junit.Assert.*;
import gov.nist.ixe.StringUtil;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class LongestCommonSuffixTests {

	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] {
				{ "bcd", new String[] { "abcd", "bbcd", "cbcd"}, 4 },			
				{ "bcd", new String[] { "abcd", "bbcd", "cbcd"}, 3 },
				{ "cd",  new String[] { "abcd", "bbcd", "cbcd"}, 2 },
				{ "d",   new String[] { "abcd", "bbcd", "cbcd"}, 1 },
				{ "",    new String[] { "abcd", "bbcd", "cbcd"}, 0 },
				{ "ab",  new String[] { "ab" }, 2 },
				{ "",    new String[] { "ab", "cd", "ef"}, 2 }
		};
		return Arrays.asList(data);
	}
	
	private String expected;
	private String[] strings;
	private int max;
	
	public LongestCommonSuffixTests(String expected, String[] strs, int max) {
		this.expected = expected;
		this.strings = strs;
		this.max = max;
	}
	
	@Test
	public void LongestCommonPrefixWorksCorrectly() {
		assertEquals(expected, StringUtil.longestCommonSuffix(strings, max));
	}
	
	@Test(expected=IllegalArgumentException.class)	
	public void LongestCommonSuffixRequiresNonNegativeMax() {
		StringUtil.longestCommonSuffix(new String[] {"abc"} , -1);
	}
}

