package gov.nist.ixe.templates.ini.tests;

import static org.junit.Assert.*;
import gov.nist.ixe.templates.ini.Values;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class ValuesLeftRightDelimiterTests {
	
	private String input;
	private String expectedLeftDelim;
	private String expectedRightDelim;
	
	public ValuesLeftRightDelimiterTests(			
			String expectedLeftDelim, 
			String expectedRightDelim,
			String input) {
		
		this.expectedLeftDelim = expectedLeftDelim;
		this.expectedRightDelim = expectedRightDelim;
		this.input = input;		
	}
	
	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] {
				{ "[", "]", "[a], [b], [c], [d]" },
				{ "E[", "]", "E[a], E[b], E[c], E[d]" },
				{ "(", ")", "(abc), (def), (g), (h)"},
				{ "{", "}", "{this}, {is}, {a}, {1234}"},
				{ "" , "" , "1.0, 2.0, 3.0. 4.0"},
				{ "{", "}", "{[()]}, {([])}, {<>}, {<{}>}"}				 
				
		};
		return Arrays.asList(data);
	}

	@Test 
	public void ValuesExtractCorrectLeftOrRightDelimiter() {
		assertEquals(expectedLeftDelim, Values.parse(input).getCommonLeftDelimiter(false));
		assertEquals(expectedRightDelim, Values.parse(input).getCommonRightDelimiter(false));
		
	}

}
