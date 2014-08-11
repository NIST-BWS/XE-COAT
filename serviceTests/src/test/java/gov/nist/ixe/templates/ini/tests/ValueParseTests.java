package gov.nist.ixe.templates.ini.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import gov.nist.ixe.templates.ini.Values;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class ValueParseTests {
	
	@Parameters
	public static Collection<Object[]> testData() {
		Object[][] data = new Object[][] {
				{ String.class,  new Object[]{""}, 	  null},
				{ String.class,  new Object[]{""}, 	  " "},
				{ String.class,  new Object[]{""}, 	  "  "},
				{ String.class,  new Object[]{"simple"}, "simple"},
				{ String.class,  new Object[]{"simple"}, " simple "},
				{ String.class,  new Object[]{"simple"}, "  simple "},
				{ String.class,  new Object[]{"simple"}, "simple   "},
				{ Integer.class, new Object[]{1},        "1"},
				{ Integer.class, new Object[]{0},        "0"},
				
				{ String.class,  new Object[]{"one","two"},  "one,two" },
				{ String.class,  new Object[]{"one","two"},  " one, two" },
				{ String.class,  new Object[]{"one","two"},  " one , two " },
				{ String.class,  new Object[]{"one","two"},  " one,two " },
				
				{ String.class, new Object[]{"one","two"},  "one two" },
				{ String.class, new Object[]{"one","two"},  " one two" },
				{ String.class, new Object[]{"one","two"},  " one  two " },
				{ String.class, new Object[]{"one","two"},  " one two " },
				
				{ String.class, new Object[]{"(a)","[b]","{c}","<d>"},  "(a) [b] {c} <d>" },
				{ String.class, new Object[]{"(a)","[b]","{c}","<d>"},  "(a), [b], {c}, <d>" },
				
				{ String.class, new Object[]{"(a,)","[,b]","{c}","<,>"},  "(a,) [,b] {c} <,>" },
				{ String.class, new Object[]{"(a,)","[,b]","{c}","<,>"},  "(a,), [,b], {c}, <,>" },
				
				{ String.class, new Object[]{"",""},  "," },
				{ String.class, new Object[]{"",""},  " ," },
				{ String.class, new Object[]{"",""},  ", " },
				{ String.class, new Object[]{"","",""},  ",," },
				{ String.class, new Object[]{"","",""},  " ,," },
				{ String.class, new Object[]{"","",""},  ", ," },
				{ String.class, new Object[]{"","",""},  ",, " },
				
				{ String.class, new Object[]{"a",""},  "a," },				
				{ String.class, new Object[]{"","a"},  ",a" },
				{ String.class, new Object[]{"a",""},  " a," },
				{ String.class, new Object[]{"","a"},  " ,a" },
				{ String.class, new Object[]{"a",""},  "a, " },
				{ String.class, new Object[]{"","a"},  ",a " },
				
				{ String.class, new Object[]{"a","b",""},  "a,b ," },
				{ String.class, new Object[]{"a","b",""},  " a,b ," },
				{ String.class, new Object[]{"a","b",""},  " a, b ," },
				{ String.class, new Object[]{"a","b",""},  " a, b," },
				
				{ String.class, new Object[]{"a","","b"},  "a,,b" },
				{ String.class, new Object[]{"a","","b"},  "a ,,b" },
				{ String.class, new Object[]{"a","","b"},  "a,,b " },
				
				{ String.class, new Object[]{quote("")},  quote("") },
				{ String.class, new Object[]{quote(",")},  quote(",") },
				{ String.class, new Object[]{dubQuote("")}, dubQuote("") },
				{ String.class, new Object[]{dubQuote(",")}, dubQuote(",") },
				{ String.class, new Object[]{paren("")},  paren("") },
				{ String.class, new Object[]{paren(",")},  paren(",") },
				{ String.class, new Object[]{square("")},  square("") },
				{ String.class, new Object[]{square(",")},  square(",") },
				{ String.class, new Object[]{curly("")},  curly("") },
				{ String.class, new Object[]{curly(",")},  curly(",") },
				{ String.class, new Object[]{angle("")},  angle("") },
				{ String.class, new Object[]{angle(",")},  angle(",") },
				
				{ String.class, new Object[]{quote(dubQuote(""))},quote(dubQuote(""))},
				{ String.class, new Object[]{dubQuote(quote(""))}, dubQuote(quote(""))},
				{ String.class, new Object[]{dubQuote(dubQuote(""))},dubQuote(dubQuote(""))},
								
				{ String.class, new Object[]{"John Q. Public","Jane Doe"},  "John Q. Public, Jane Doe" },
				{ String.class, new Object[]{"John","Q.","Public","Jane","Doe"},  "John Q. Public Jane Doe" },
				
				{ Integer.class, new Object[] { 2, 4, 6, 8 }, "2, 4, 6, 8" },
				{ Double.class, new Object[] { 2.0, 4.0, 6.0, 8.0 }, "2.0, 4.0, 6.0, 8.0" },
				{ Object.class, new Object[] { 2, 4.0, 6, 8 }, "2, 4.0, 6, 8" },
				{ Double.class, new Object[] {100.0, 200.0, 300.0}, "100.0, 200.0, 300.0" }
				
				
		};
		return Arrays.asList(data);
	}
	
	private Class<?> expectedMostCommonType;
	private Object[] expectedValues;	
	private String source;
	
	public ValueParseTests(Class<?> expectedMostCommonType, Object[] expectedValues, String source) {
		this.expectedMostCommonType = expectedMostCommonType;
		this.expectedValues = expectedValues;
		this.source = source;						
	}
	
	
	@Test
	public void ValuesParseCorrectly() {
		Values values = Values.parse(source, 0);
		assertValuesEquals(expectedValues, values);
		assertEquals(expectedMostCommonType, values.getMostDetailedCommonType());
	}


	
	private static String dubQuote(String s) {
		return "\"" + s + "\"";
	}
	
	private static String quote(String s) {
		return "'" + s + "'";
	}
	
	private static String paren(String s) {
		return "(" + s + ")";
	}
	
	private static String square(String s) {
		return "[" + s + "]";
	}
	
	private static String curly(String s) {
		return "{" + s + "}";
	}
	
	private static String angle(String s) {
		return "<" + s + ">";
	}
	
	private void assertValuesEquals(Object[] expected, Values actual) {
		assertEquals(expected.length, actual.size());
		for (int i = 0; i < expected.length; i++) {
			if (expected[i] == null) {
				assertNull(actual.get(i));
			} else {
				assertEquals(expected[i], actual.get(i));
			}
		}		
	}
	
	
	
	
}
