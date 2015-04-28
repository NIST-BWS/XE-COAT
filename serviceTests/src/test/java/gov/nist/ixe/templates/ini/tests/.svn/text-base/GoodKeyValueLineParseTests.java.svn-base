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
	private int keyStart, keyEnd;
	private int valuesStart, commentStart;

	public GoodKeyValueLineParseTests(String source,			
			int keyStart, int keyEnd,
			int valuesStart, int commentStart) {
		this.source = source;		
		this.keyStart = keyStart;
		this.keyEnd = keyEnd;
		this.valuesStart = valuesStart;
		this.commentStart = commentStart;		
	}

	private static String DQ = "\"";
	private static String BS = "\\"; 

	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] {
				{ "airport = lax ; australia?", 	         0,  7,  9, 14},
				{ " Path=WINPATH; comment",                  1,  5,  6, 13},
				{ "a=",                                      0,  1,  2,  2},
				{ "quote=" + DQ+BS+DQ+DQ + "  ;c",           0,  5,  6, 12},
				{ "no_value=;comment",                       0,  8,  9,  9},
				{ "d = \";\", ';', (;), [;], {;} ; comment", 0,  1,  3, 28}
		};
		return Arrays.asList(data);
	}

	@Test
	public void KeyValueLinesParseCorrectly() {

		assertKeyValueLineEquals(source,				
				keyStart, keyEnd,
				valuesStart, commentStart);
		}

	private void assertKeyValueLineEquals(
			String source,			
			int keyStart, int keyEnd,
			int valuesStart, int commentStart) {

		KeyValueLine kvl = KeyValueLine.parse(source, 0);
		assertEquals(keyStart, kvl.getKeyStart());
		assertEquals(keyEnd, kvl.getKeyEnd());
		assertEquals(valuesStart, kvl.getValuesStart());
		assertEquals(commentStart, kvl.getCommentStart());


	}



}
