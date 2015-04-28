package gov.nist.ixe.tests;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gov.nist.ixe.GetOpt;
import gov.nist.ixe.GetOpt.Parameter;

import java.util.Map;




import org.junit.Test;

public class GetOptTests {
	
	@Test
	public void AllParameterPermutationsAreSuccessful() {
		Parameter p1 = new Parameter('a', "alpha", true, "This is a description");
		Parameter p2 = new Parameter('b', "bravo", false, "This is a description");
		Parameter p3 = new Parameter('c', true, "This is a description");
		Parameter p4 = new Parameter('d', false, "This is a description");
		
		GetOpt go = new GetOpt(p1, p2, p3, p4);
		
		String[] args = new String[] {
				"-a", "aTest",
				"-b",
				"-c", "cTest",
				"-d"
		};
		
		Map<Character, String> parameters = go.parse(args);
		assertEquals(4, parameters.size());
		assertTrue(parameters.containsKey('a'));
		assertTrue(parameters.containsKey('b'));
		assertTrue(parameters.containsKey('c'));
		assertTrue(parameters.containsKey('d'));
		assertEquals("aTest", parameters.get('a'));
		assertEquals("true", parameters.get('b'));
		assertEquals("cTest", parameters.get('c'));
		assertEquals("true", parameters.get('d'));
		
		args = new String[] {
				"--alpha=alphaTest",
				"--bravo",
				"-c", "cTest",
				"-d"
		};
		
		parameters = go.parse(args);
		assertEquals(4, parameters.size());
		assertTrue(parameters.containsKey('a'));
		assertTrue(parameters.containsKey('b'));
		assertTrue(parameters.containsKey('c'));
		assertTrue(parameters.containsKey('d'));
		assertEquals("alphaTest", parameters.get('a'));
		assertEquals("true", parameters.get('b'));
		assertEquals("cTest", parameters.get('c'));
		assertEquals("true", parameters.get('d'));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void SecondParameterWithSameLongKeyFails() {
		Parameter p1 = new Parameter('a', "test", "This is a description");
		Parameter p2 = new Parameter('b', "test", "This is a description");
		GetOpt go = new GetOpt(p1, p2);
		assertNotNull(go); // prevent warning
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void SecondParameterWithSameShortKeyFails() {
		Parameter p1 = new Parameter('a', "test1", "This is a description");
		Parameter p2 = new Parameter('a', "test2", "This is a description");
		GetOpt go = new GetOpt(p1, p2);
		assertNotNull(go); // prevent warning
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void LengthyShortKeyFails() {
		char key = 't';
		String value = "true";
		
		GetOpt.Parameter p = new GetOpt.Parameter(key, true, "This is a description");
		GetOpt go = new GetOpt(p);
		
		Map<Character, String> params = go.parse(String.format("-asdf", key));
		assertTrue(params.size() == 1);
		assertTrue(params.containsKey(key));
		assertEquals(value, params.get(key));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void ShortKeyWithExpectedValueFails() {
		char key = 't';
		String value = "true";
		
		GetOpt.Parameter p = new GetOpt.Parameter(key, true, "This is a description");
		GetOpt go = new GetOpt(p);
		
		Map<Character, String> params = go.parse(String.format("-%s", key));
		assertTrue(params.size() == 1);
		assertTrue(params.containsKey(key));
		assertEquals(value, params.get(key));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void LongKeyWithExpectedValueFails() {
		char shortKey = 't';
		String longKey = "test";
		String value = "testValue";
		
		GetOpt.Parameter p = new GetOpt.Parameter(shortKey, longKey, true, "This is a description");
		GetOpt go = new GetOpt(p);
		
		Map<Character, String> params = go.parse(String.format("--%s", longKey, value));
		assertTrue(params.size() == 1);
		assertTrue(params.containsKey(shortKey));
		assertEquals(value, params.get(shortKey));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void LongKeyWithExpectedValueFails2() {
		char shortKey = 't';
		String longKey = "test";
		String value = "testValue";
		
		GetOpt.Parameter p = new GetOpt.Parameter(shortKey, longKey, true, "This is a description");
		GetOpt go = new GetOpt(p);
		
		Map<Character, String> params = go.parse(String.format("--%s=", longKey, value));
		assertTrue(params.size() == 1);
		assertTrue(params.containsKey(shortKey));
		assertEquals(value, params.get(shortKey));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void LongKeyWithoutExpectedValueFails() {
		char shortKey = 't';
		String longKey = "test";
		String value = "testValue";
		
		GetOpt.Parameter p = new GetOpt.Parameter(shortKey, longKey, false, "This is a description");
		GetOpt go = new GetOpt(p);
		
		Map<Character, String> params = go.parse(String.format("--%s=", longKey, value));
		assertTrue(params.size() == 1);
		assertTrue(params.containsKey(shortKey));
		assertEquals(value, params.get(shortKey));
	}
	
	@Test
	public void CanProperlyParseShortKeyWithoutValue() {
		char key = 't';
		String value = "true";
		
		GetOpt.Parameter p = new GetOpt.Parameter(key, false, "This is a description");
		GetOpt go = new GetOpt(p);
		
		Map<Character, String> params = go.parse(String.format("-%s", key));
		assertTrue(params.size() == 1);
		assertTrue(params.containsKey(key));
		assertEquals(value, params.get(key));
	}
	
	@Test
	public void CanProperlyParseShortKeyWithValue() {
		char key = 't';
		String value = "testValue";
		
		GetOpt.Parameter p = new GetOpt.Parameter(key, "This is a description");
		GetOpt go = new GetOpt(p);
		
		Map<Character, String> params = go.parse(String.format("-%s", key), value);
		assertTrue(params.size() == 1);
		assertTrue(params.containsKey(key));
		assertEquals(value, params.get(key));
	}
	
	@Test
	public void CanProperlyParseShortKeyWithMultiwordValue() {
		char key = 't';
		String value = "\"This is a sentence\"";
		
		GetOpt.Parameter p = new GetOpt.Parameter(key, "This is a description");
		GetOpt go = new GetOpt(p);
		
		Map<Character, String> params = go.parse(String.format("-%s", key), value);
		assertTrue(params.size() == 1);
		assertTrue(params.containsKey(key));
		assertEquals(value, params.get(key));
	}
	
	@Test
	public void CanPropertlyParseLongKeyWithoutValue() {
		char shortKey = 't';
		String longKey = "test";
		String value = "true";
		
		GetOpt.Parameter p = new GetOpt.Parameter(shortKey, longKey, false, "This is a description");
		GetOpt go = new GetOpt(p);
		
		Map<Character, String> params = go.parse("--test");
		assertTrue(params.size() == 1);
		assertTrue(params.containsKey(shortKey));
		assertEquals(value, params.get(shortKey));
	}
	
	@Test
	public void CanProperlyParseLongKeyWithValue() {
		char shortKey = 't';
		String longKey = "test";
		String value = "testValue";
		
		GetOpt.Parameter p = new GetOpt.Parameter(shortKey, longKey, true, "This is a description");
		GetOpt go = new GetOpt(p);
		
		Map<Character, String> params = go.parse(String.format("--%s=%s", longKey, value));
		assertTrue(params.size() == 1);
		assertTrue(params.containsKey(shortKey));
		assertEquals(value, params.get(shortKey));
	}
	
	@Test
	public void CanProperlyParseLongKeyWithMultiWordValue() {
		char shortKey = 't';
		String longKey = "test";
		String value = "\"This is a sentence.\"";
		
		GetOpt.Parameter p = new GetOpt.Parameter(shortKey, longKey, true, "This is a description");
		GetOpt go = new GetOpt(p);
		
		Map<Character, String> params = go.parse(String.format("--%s=%s", longKey, value));
		assertTrue(params.size() == 1);
		assertTrue(params.containsKey(shortKey));
		assertEquals(value, params.get(shortKey));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void LongKeyMustMeMoreThanOneCharacter() {
		GetOpt.Parameter p = new GetOpt.Parameter('t', "t", "This is a description");
		assertNotNull(p); // prevent warning
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void ShortKeyMustOnlyContainAlphaNumericCharacters() {
		GetOpt.Parameter p = new GetOpt.Parameter('+', "This is a description");
		assertNotNull(p); // prevent warning
	}

	@Test(expected=IllegalArgumentException.class)
	public void LongKeyCannotContainWhiteSpace() {
		GetOpt.Parameter p = new GetOpt.Parameter('s', "sp ace", "This is a description");
		assertNotNull(p); // prevent warning
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void LongKeyCannotContainSpecialCharacters() {
		GetOpt.Parameter p = new GetOpt.Parameter('s', "sp+ace", "This is a description");
		assertNotNull(p); // prevent warning
	}
	
	@Test
	public void NoLongKeyIsValid() {
		GetOpt.Parameter p = new GetOpt.Parameter('s', "This is a description");
		GetOpt go = new GetOpt(p);
		assertNotNull(go); // prevent warning
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void LongKeyCannotBeBlank() {
		GetOpt.Parameter p = new GetOpt.Parameter('s', "", "This is a description");
		assertNotNull(p); // prevent warning
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void DescriptionMustBePresent() {
		GetOpt.Parameter p = new GetOpt.Parameter('s', null);
		assertNotNull(p); // prevent warning
	}
}