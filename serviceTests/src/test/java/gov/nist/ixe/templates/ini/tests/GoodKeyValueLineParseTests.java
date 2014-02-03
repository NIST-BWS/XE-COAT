package gov.nist.ixe.templates.ini.tests;


import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import gov.nist.ixe.templates.ini.KeyValueLine;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class GoodKeyValueLineParseTests {


	private String source;
	private int lhsStart, lhsEnd;
	private int keyStart, keyEnd;
	private int valuesStart, valuesEnd;
	private int commentStart, commentEnd;

	public GoodKeyValueLineParseTests(String source,
			int lhsStart, int lhsEnd,
			int keyStart, int keyEnd,
			int valuesStart, int valuesEnd,
			int commentStart, int commentEnd) {
		this.source = source;
		this.lhsStart = lhsStart;
		this.lhsEnd = lhsEnd;
		this.keyStart = keyStart;
		this.keyEnd = keyEnd;
		this.valuesStart = valuesStart;
		this.valuesEnd = valuesEnd;
		this.commentStart = commentStart;
		this.commentEnd = commentEnd;
	}

	private static String DQ = "\"";
	private static String BS = "\\"; 

	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] {
				{ "airport = lax ; australia?", 	         0, 10,  0,  7, 10, 14, 14, 26},
				{ " Path=WINPATH; comment",                  0,  6,  1,  5,  6, 13, 13, 22},
				{ "a=",                                      0,  2,  0,  1,  2,  2,  2,  2},
				{ "quote=" + DQ+BS+DQ+DQ + "  ;c",           0,  6,  0,  5,  6, 12, 12, 14},
				{ "no_value=;comment",                       0,  9,  0,  8,  9,  9,  9, 17},
				{ "d = \";\", ';', (;), [;], {;} ; comment", 0,  4,  0,  1,  4, 28, 28, 37}
		};
		return Arrays.asList(data);
	}

	@Test
	public void KeyValueLinesParseCorrectly() {

		assertKeyValueLineEquals(source, 
				lhsStart, lhsEnd,
				keyStart, keyEnd,
				valuesStart, valuesEnd,
				commentStart, commentEnd);
		}

	private void assertKeyValueLineEquals(
			String source,
			int lhsStart, int lhsEnd,
			int keyStart, int keyEnd,
			int valuesStart, int valuesEnd,
			int commentStart, int commentEnd) {

		KeyValueLine kvl = KeyValueLine.parse(source, 0);
		assertEquals(lhsStart, kvl.getLhsStart());
		assertEquals(lhsEnd, kvl.getLhsEnd());

		assertEquals(keyStart, kvl.getKeyStart());
		assertEquals(keyEnd, kvl.getKeyEnd());

		assertEquals(valuesStart, kvl.getValuesStart());
		assertEquals(valuesEnd, kvl.getValuesEnd());

		assertEquals(commentStart, kvl.getCommentStart());
		assertEquals(commentEnd, kvl.getCommentEnd());


	}



}
