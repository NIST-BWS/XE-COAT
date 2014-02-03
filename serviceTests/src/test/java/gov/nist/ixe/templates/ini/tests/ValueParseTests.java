package gov.nist.ixe.templates.ini.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import gov.nist.ixe.templates.ini.Values;

import org.junit.Test;

public class ValueParseTests {
	
	private String dq(String s) {
		return "\"" + s + "\"";
	}
	
	private String sq(String s) {
		return "'" + s + "'";
	}
	
	private String paren(String s) {
		return "(" + s + ")";
	}
	
	private String square(String s) {
		return "[" + s + "]";
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
	
	@Test
	public void ValuesAreExtractedCorrectlyFromSimpleStrings() {
		assertValuesEquals(new Object[]{"single"}, Values.parse("single"));
	}
	
	@Test
	public void ValuesAreExtractedCorrectlyFromDoubleQuotedStrings() {
		assertValuesEquals(new Object[]{sq("double quoted")}, Values.parse(sq("double quoted")));
	}
	
	@Test
	public void ValuesAreExtractedCorrectlyFromSingleQuotedStrings() {
		assertValuesEquals(new Object[]{sq("single quoted")}, Values.parse(sq("single quoted")));
	}
	
	@Test
	public void ValuesAreExtractedCorrectlyFromParenthsizedStrings() {
		assertValuesEquals(new Object[]{paren("quoted")}, Values.parse(paren("quoted")));
	}
	
	@Test
	public void ValuesAreExtractedCorrectlyFromSquareBracketedQuotedStrings() {
		assertValuesEquals(new Object[]{square("quoted")}, Values.parse(square("quoted")));
	}

	
	@Test
	public void ValuesAreExtractedCorrectlyFromSimpleStringLists() {
		
		assertValuesEquals(new Object[]{"one", "two"}, Values.parse("one,two"));
		assertValuesEquals(new Object[]{"one", "two"}, Values.parse(" one , two "));
		assertValuesEquals(new Object[]{"one", "two"}, Values.parse(" one, two"));
		assertValuesEquals(new Object[]{"one", "two"}, Values.parse("one, two "));
		assertValuesEquals(new Object[]{"one", "two"}, Values.parse(" one,two"));
		
		assertValuesEquals(new Object[]{"one", "two"}, Values.parse("one,two;COMMENT"));
		assertValuesEquals(new Object[]{"one", "two"}, Values.parse(" one , two ;XYZ"));
		assertValuesEquals(new Object[]{"one", "two"}, Values.parse(" one, two;;;"));
		assertValuesEquals(new Object[]{"one", "two"}, Values.parse("one, two ; ; ; ; one, two"));
		assertValuesEquals(new Object[]{"one", "two"}, Values.parse(" one,two ; \"quoted comment\""));
		
					
	}
		
		
		
	@Test
	public void ValuesAreExtractedCorrectlyFromTrailingDanglingCommas() {
		
		assertValuesEquals(new Object[]{"dangle", null}, Values.parse("dangle,"));
		assertValuesEquals(new Object[]{"dangle", null}, Values.parse("dangle ,"));
		assertValuesEquals(new Object[]{"dangle", null}, Values.parse("dangle , "));
		assertValuesEquals(new Object[]{"dangle", null}, Values.parse(" dangle , "));
		assertValuesEquals(new Object[]{"dangle", null}, Values.parse(" dangle, "));
		assertValuesEquals(new Object[]{"dangle", null}, Values.parse(" dangle,"));
		
		assertValuesEquals(new Object[]{"dangle", null, null}, Values.parse("dangle,,;comment"));
		assertValuesEquals(new Object[]{"dangle", null, null}, Values.parse("dangle , ,;;"));
		assertValuesEquals(new Object[]{"dangle", null, null}, Values.parse("dangle ,  ,; comment"));
		assertValuesEquals(new Object[]{"dangle", null, null}, Values.parse(" dangle ,,;,,,"));
		assertValuesEquals(new Object[]{"dangle", null, null}, Values.parse(" dangle,,; "));
		assertValuesEquals(new Object[]{"dangle", null, null}, Values.parse(" dangle,,;"));
		
	}
	
	@Test
	public void ValuesAreExtractedCorrectlyFromCommaOnlyLists() {
		
		assertValuesEquals(new Object[]{null,null}, Values.parse(","));
		assertValuesEquals(new Object[]{null,null}, Values.parse(", "));
		assertValuesEquals(new Object[]{null,null}, Values.parse(" , "));
		assertValuesEquals(new Object[]{null,null}, Values.parse(" ,"));
		assertValuesEquals(new Object[]{null,null}, Values.parse(",;,"));
		assertValuesEquals(new Object[]{null,null}, Values.parse(", ;;"));
		assertValuesEquals(new Object[]{null,null}, Values.parse(" , ;;;;"));
		assertValuesEquals(new Object[]{null,null}, Values.parse(" ,;"));
		
		assertValuesEquals(new Object[]{null,null,null}, Values.parse(",,"));
		assertValuesEquals(new Object[]{null,null,null}, Values.parse(", ,"));
		assertValuesEquals(new Object[]{null,null,null}, Values.parse(" ,, "));
		assertValuesEquals(new Object[]{null,null,null}, Values.parse(" ,,"));
		assertValuesEquals(new Object[]{null,null,null}, Values.parse(",,;a,b,c"));
		assertValuesEquals(new Object[]{null,null,null}, Values.parse(", ,;d,e,f"));
		assertValuesEquals(new Object[]{null,null,null}, Values.parse(" ,,;\\;"));
		
	}
	
	@Test
	public void ValueListsExtractCorrectlyFromEscapedCharacters() {
		String B = "\\"; String D = "\"";
		assertValuesEquals(new Object[]{B}, Values.parse(B));
		assertValuesEquals(new Object[]{B+D}, Values.parse(B+D)); // \"
		assertValuesEquals(new Object[]{D+B+D+D}, Values.parse(D+B+D+D)); //   "\""
		assertValuesEquals(new Object[]{D+B+D+D}, Values.parse(D+B+D+D)); // " "\"" "
	}
	
	@Test
	public void ValueListsExtactCorrectlyFromDoubleQuotesThatImplicitlyDelimitMultipleValues() {
		String B = "\\"; String D = "\"";
		assertValuesEquals(new Object[]{D+B+D+D, D+B+D+D}, Values.parse(D+B+D+D + D+B+D+D)); //   "\"""\""
	}
	
	@Test
	public void ValueListsExtactCorrectlyFromCommasProtectedByDoubleQuotes() {
		String C= ","; String qC = dq(","); // comma, quoted comma
		assertValuesEquals(new Object[]{qC , qC}, Values.parse(qC + C + qC));
		assertValuesEquals(new Object[]{null, qC, null}, Values.parse(C + qC + C));
		
	}
	
	@Test
	public void ValueListsExtactCorrectlyFromSemicolonsProtectedByDoubleQuotes() {
		String S= ";"; String qS = dq(";"); // comma, quoted comma
		assertValuesEquals(new Object[]{qS, qS}, Values.parse(qS+","+qS +"; comment"));
		assertValuesEquals(new Object[]{qS}, Values.parse(qS+S+qS + "; comment"));
	}
	
	@Test
	public void ValueListsExtactCorrectlyFromQuotedAndNonQuotedValues() {
		assertValuesEquals(new Object[]{null,null,null}, Values.parse(" ,,;\\;"));
	}
	
	
	@Test
	public void ValueListsPreserveWhitespaceInDoubleQuotedStrings() {
		assertValuesEquals(new Object[]{dq(" one"), dq("two "), dq(" three ")}, 
				Values.parse(dq(" one") + "," + dq("two ") + "," + dq(" three ")));
	}
	
	@Test
	public void ValueListsPreserveWhitespaceInDoubleQuotedNumbers() {
		assertValuesEquals(new Object[]{dq(" 1"), dq("2 "), dq(" 3 ")}, 
				Values.parse(dq(" 1") + "," + dq("2 ") + "," + dq(" 3 ")));
	}
	
	@Test
	public void ValueListsPreserveWhitespaceInSingleQuotedStrings() {
		assertValuesEquals(new Object[]{sq(" one"), sq("two "), sq(" three ")}, 
				Values.parse(sq(" one") + "," + sq("two ") + "," + sq(" three ")));
	}
	
	@Test
	public void ValueListsPreserveWhitespaceInSingleQuotedNumbers() {
		assertValuesEquals(new Object[]{sq(" 1"), sq("2 "), sq(" 3 ")}, 
				Values.parse(sq(" 1") + "," + sq("2 ") + "," + sq(" 3 ")));
	}
	
	@Test
	public void ValueListsEliminiateWhitespaceOnUnquotedElements() {
		assertValuesEquals(new Object[]{"one", "two", "three"}, Values.parse("  one,two , three "));
	}
	
	@Test
	public void ValueListsEliminiateWhitespaceOutsideDoubleQuotedElements() {
		assertValuesEquals(new Object[]{dq("one"), dq("two"), dq("three")}, 
				Values.parse("   " +dq("one") + "  , " + "   " +dq("two") + "  , " + dq("three") + " "));
	}
	
	@Test
	public void ValueListsEliminiateWhitespaceOutsideSingleQuotedElements() {
		assertValuesEquals(new Object[]{sq("one"), sq("two"), sq("three")}, 
				Values.parse("   " +sq("one") + "  , " + "   " +sq("two") + "  , " + sq("three") + " "));
	}
	
	
	
	@Test
	public void ValueListsOfStringsYieldCorrectCommonType() {
		assertEquals(String.class, Values.parse("a,b,c").getMostDetailedCommonType());
	}
	
	@Test
	public void ValueListsOfIntegersYieldCorrectCommonType() {
		assertEquals(Integer.class, Values.parse("1,2,3,4").getMostDetailedCommonType());
	}
	
	@Test
	public void ValueListsOfDoubleYieldCorrectCommonType() {
		assertEquals(Double.class, Values.parse("1.0,2.0,3.0,4.0").getMostDetailedCommonType());
	}
	
	@Test
	public void ValueListOfMixedTypesYieldCorrectCommonType() {
		assertEquals(Object.class, Values.parse("1,2,abc,def").getMostDetailedCommonType());
	}
	

}
