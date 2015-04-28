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
public class LongestCommonPrefixTests {

	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] {
				{ "abc", new String[] { "abcd", "abcc", "abce"}, 4 },			
				{ "abc", new String[] { "abcd", "abcc", "abce"}, 3 },
				{ "ab",  new String[] { "abcd", "abcc", "abce"}, 2 },
				{ "a",   new String[] { "abcd", "abcc", "abce"}, 1 },
				{ "",    new String[] { "abcd", "abcc", "abce"}, 0 },
				{ "ab",  new String[] { "ab" }, 2 },
				{ "",    new String[] { "ab", "cd", "ef"}, 2 }				
				
		};
		return Arrays.asList(data);
	}
	
	private String expected;
	private String[] strings;
	private int max;
	
	public LongestCommonPrefixTests(String expected, String[] strs, int max) {
		this.expected = expected;
		this.strings = strs;
		this.max = max;
	}
	
	@Test
	public void LongestCommonPrefixWorksCorrectly() {
		assertEquals(expected, StringUtil.longestCommonPrefix(strings, max));
	}
	
	@Test(expected=IllegalArgumentException.class)	
	public void LongestCommonPrefixRequiresNonNegativeMax() {
		StringUtil.longestCommonPrefix(new String[] {"abc"} , -1);
	}
}

