package gov.nist.ixe.templates.ini.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import gov.nist.ixe.templates.ini.KeyValueLineValuesSplitterStyle;
import gov.nist.ixe.templates.ini.Values;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class ValueParseTests {
	
	private static String e = "";

	@Parameters
	public static Collection<Object[]> testData() {
		Object[][] data = new Object[][] {

				
				{ KeyValueLineValuesSplitterStyle.CommasOnly,     String.class, new Object[] { "simple" }, e, e, e, "simple" },
				{ KeyValueLineValuesSplitterStyle.SpacesOnly,     String.class, new Object[] { "simple" }, e, e, e, "simple" },
				{ KeyValueLineValuesSplitterStyle.CommasOrSpaces, String.class, new Object[] { "simple" }, e, e, e, "simple" },
				
				{ KeyValueLineValuesSplitterStyle.CommasOnly,     Integer.class, new Object[] { 0 }, e, e, e, "0" },
				{ KeyValueLineValuesSplitterStyle.SpacesOnly,     Integer.class, new Object[] { 0 }, e, e, e, "0" },
				{ KeyValueLineValuesSplitterStyle.CommasOrSpaces, Integer.class, new Object[] { 0 }, e, e, e, "0" },
				
				
				{ KeyValueLineValuesSplitterStyle.CommasOnly,     String.class, new Object[] { e }, e, e, e, null },
				{ KeyValueLineValuesSplitterStyle.SpacesOnly,     String.class, new Object[] { e }, e, e, e, null },
				{ KeyValueLineValuesSplitterStyle.CommasOrSpaces, String.class, new Object[] { e }, e, e, e, null },
				
				{ KeyValueLineValuesSplitterStyle.CommasOnly,     String.class, new Object[] { e }, e, e, e, " " },
				{ KeyValueLineValuesSplitterStyle.SpacesOnly,     String.class, new Object[] { e }, e, e, e, " " },
				{ KeyValueLineValuesSplitterStyle.CommasOrSpaces, String.class, new Object[] { e }, e, e, e, " " },
				
				{ KeyValueLineValuesSplitterStyle.CommasOnly,     String.class, new Object[] { e }, e, e, e, "  " },
				{ KeyValueLineValuesSplitterStyle.SpacesOnly,     String.class, new Object[] { e }, e, e, e, "  " },
				{ KeyValueLineValuesSplitterStyle.CommasOrSpaces, String.class, new Object[] { e }, e, e, e, "  " },
				
				{ KeyValueLineValuesSplitterStyle.CommasOnly,     String.class, new Object[] { "one", "two" },  e, ", ",  e, "one, two"    },
				{ KeyValueLineValuesSplitterStyle.CommasOnly,     String.class, new Object[] { "one", "two" },  e, ", ",  e, " one, two"   },
				{ KeyValueLineValuesSplitterStyle.CommasOnly,     String.class, new Object[] { "one", "two" },  e, ", ",  e, " one, two "  },
				{ KeyValueLineValuesSplitterStyle.CommasOnly,     String.class, new Object[] { "one", "two" },  e, " , ", e, " one , two " },
				
				{ KeyValueLineValuesSplitterStyle.SpacesOnly,     String.class, new Object[] { "one,two" },     e, e,     e, "one,two"    },
				{ KeyValueLineValuesSplitterStyle.SpacesOnly,     String.class, new Object[] { "one,", "two" }, e, " ",   e, "one, two"   },
				{ KeyValueLineValuesSplitterStyle.SpacesOnly,     String.class, new Object[] { "one,", "two" }, e, " ",   e, "one, two"   },
				{ KeyValueLineValuesSplitterStyle.SpacesOnly,     String.class, new Object[] { "one,", "two" }, e, " ",   e, "one, two "  },
				{ KeyValueLineValuesSplitterStyle.SpacesOnly,     String.class, new Object[] { "one,", "two" }, e, " ",   e, " one, two " },
				
				{ KeyValueLineValuesSplitterStyle.CommasOrSpaces, String.class, new Object[] { "one", "two" },  e, ", ",  e, "one, two"    },
				
				{ KeyValueLineValuesSplitterStyle.CommasOnly,     String.class, new Object[] { "one two" },    e, e,   e, "one two" },
				{ KeyValueLineValuesSplitterStyle.SpacesOnly,     String.class, new Object[] { "one", "two" }, e, " ", e, "one two" },
				{ KeyValueLineValuesSplitterStyle.CommasOrSpaces, String.class, new Object[] { "one", "two" }, e, " ", e, "one two" },
				
				{ KeyValueLineValuesSplitterStyle.CommasOnly,     Integer.class, new Object[] { 2, 4, 6, 8 },         e, ", ", e, "2, 4, 6, 8" },
				{ KeyValueLineValuesSplitterStyle.CommasOrSpaces, Integer.class, new Object[] { 2, 4, 6, 8 },         e, ", ", e, "2, 4, 6, 8" },
				
				{ KeyValueLineValuesSplitterStyle.CommasOnly,     Double.class, new Object[] { 2.0, 4.0, 6.0, 8.0 },  e, ", ", e, "2.0, 4.0, 6.0, 8.0" },
				
				{ KeyValueLineValuesSplitterStyle.CommasOnly,     Long.class, new Object[] { 1234567890123L, 1234567890123L },  e, ", ", e, " 1234567890123, 1234567890123" },
								
				{ KeyValueLineValuesSplitterStyle.SpacesOnly,     Object.class,  new Object[] { "2,","4,","6,", 8 },  e, " ",  e, "2, 4, 6, 8" },
				{ KeyValueLineValuesSplitterStyle.SpacesOnly,     Object.class,  new Object[] { "2,","4,","6,", 8 },  e, "  ",  e, " 2,  4,  6,  8 " },
				
				{ KeyValueLineValuesSplitterStyle.SpacesOnly,     Integer.class, new Object[] { 2, 4, 6, 8 }, e, " ", e, "2 4 6 8" },
				{ KeyValueLineValuesSplitterStyle.SpacesOnly,     Integer.class, new Object[] { 2, 4, 6, 8 }, e, "  ", e, "2  4  6  8" },
				
				{ KeyValueLineValuesSplitterStyle.CommasOrSpaces, String.class, new Object[] { "a", "b" }, e,    ", ",   e,    "a, b"    },
				{ KeyValueLineValuesSplitterStyle.CommasOrSpaces, String.class, new Object[] { "a", "b" }, "{",  ", ", "}",    "{a}, {b}"  },
				{ KeyValueLineValuesSplitterStyle.CommasOrSpaces, String.class, new Object[] { "a", "b" }, "*(", ", ", ")",    "*(a), *(b)"  },
				{ KeyValueLineValuesSplitterStyle.CommasOrSpaces, String.class, new Object[] { "a", "b" }, "'",  ", ",   "'",  quote("a") + ", " + quote("b") },
				{ KeyValueLineValuesSplitterStyle.CommasOrSpaces, String.class, new Object[] { "a", "b" }, "\"", ", ",  "\"",  dubQuote("a") + ", " + dubQuote("b") },
				
				{ KeyValueLineValuesSplitterStyle.CommasOnly,     String.class, new Object[] { "John Q. Public", "Jane Doe" },      e, ", ",  e, "John Q. Public, Jane Doe" },
				{ KeyValueLineValuesSplitterStyle.SpacesOnly,     String.class, new Object[] { "John","Q.","Public,","Jane","Doe"}, e, " ",   e, "John Q. Public, Jane Doe" },
				{ KeyValueLineValuesSplitterStyle.CommasOrSpaces, String.class, new Object[] { "John Q. Public", "Jane Doe" },      e, ", ",   e, "John Q. Public, Jane Doe" },
				
				{ KeyValueLineValuesSplitterStyle.CommasOnly,     Integer.class, new Object[] { 222, 222, 222, 222 },  e,   ", ", e,   "222, 222, 222, 222" },
				{ KeyValueLineValuesSplitterStyle.CommasOnly,     Integer.class, new Object[] { 222, 222, 222, 222 },  "!", ", ", "%", "!222%, !222%, !222%, !222%" },
				{ KeyValueLineValuesSplitterStyle.SpacesOnly,     Integer.class, new Object[] { 222, 222, 222, 222 },  e,   " ", e,   "222 222 222 222" },
				{ KeyValueLineValuesSplitterStyle.SpacesOnly,     Integer.class, new Object[] { 222, 222, 222, 222 },  "!", "  ", "%", "!222%  !222%  !222%  !222%" },
				
				{ KeyValueLineValuesSplitterStyle.CommasOnly,     Integer.class, new Object[] { -2, -4, -6, -8 }, e, ", ", e, " -2, -4,  -6, -8" },
				
				// In these test, the values delimiter can't be determined, so we fall back to the defaults
				{ KeyValueLineValuesSplitterStyle.CommasOnly,     Integer.class, new Object[] { 2, 4, 6, 8 }, e, ",", e, "02, 04, 06, 08" },
				{ KeyValueLineValuesSplitterStyle.SpacesOnly,     Integer.class, new Object[] { 2, 4, 6, 8 }, e, " ", e, "02 04 06 08" },
				{ KeyValueLineValuesSplitterStyle.SpacesOnly,     Integer.class, new Object[] { 2, 4, 6, 8 }, e, " ", e, "02  04  06  08" },
				{ KeyValueLineValuesSplitterStyle.CommasOrSpaces, Integer.class, new Object[] { 2, 4, 6, 8 }, e, ",", e, "02, 04, 06, 08" },
				{ KeyValueLineValuesSplitterStyle.CommasOrSpaces, Integer.class, new Object[] { 2, -4, 6, 8 }, e, " ", e, "02 -04 06 08" }
				
				
		};
		return Arrays.asList(data);
	}
	
	private KeyValueLineValuesSplitterStyle splitterStyle;

	private Class<?> expectedMostCommonType;
	private Object[] expectedValues;
	private String expectedLeftDelimiter;
	private String expectedValueDelimiter;
	private String expectedRightDelimiter;
	private String source;
	
	public ValueParseTests(
			KeyValueLineValuesSplitterStyle style,
			Class<?> expectedMostCommonType,
			Object[] expectedValues,
			String expectedLeftDelimiter,
			String expectedValueDelimiter,
			String expectedRightDelimiter,
			String source) {
		this.splitterStyle = style;
		this.expectedMostCommonType = expectedMostCommonType;
		this.expectedValues = expectedValues;
		this.expectedLeftDelimiter = expectedLeftDelimiter;
		this.expectedValueDelimiter = expectedValueDelimiter;
		this.expectedRightDelimiter = expectedRightDelimiter;
		this.source = source;						
	}
	
	
	@Test
	public void ValuesParseCorrectly() {
		Values values = Values.parse(source, 0, splitterStyle);
		assertEquals(expectedMostCommonType, values.getMostDetailedCommonType());
		assertValuesEquals(expectedValues, values);
		assertEquals(expectedLeftDelimiter, values.getCommonLeftDelimiter(false));
		assertEquals(expectedValueDelimiter, values.getValuesDelimiter());
		assertEquals(expectedRightDelimiter, values.getCommonRightDelimiter(false));
		
		
	}


	
	private static String dubQuote(String s) {
		return "\"" + s + "\"";
	}
	
	private static String quote(String s) {
		return "'" + s + "'";
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
