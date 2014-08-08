package gov.nist.ixe.templates.ini;

import static gov.nist.ixe.Logging.trace;
import gov.nist.ixe.templates.ini.exception.IniParseException;

import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Values extends ArrayList<Object> {

	public boolean isMultiple() {
		trace();
		return super.size() > 1;
	}
	public Class<?> getMostDetailedCommonType(Class<?> classToUseForNullValues) {
		trace();
		
		// If there are no values, then default to a string instead
		// of a null reference, which JAXB has a hard time dealing
		// nicely with
		//
		if (size() == 0) return String.class;
		
		Set<Class<?>> types = new HashSet<Class<?>>();
		Class<?> result = Object.class;

		for (int i=0; i < this.size(); i++) {
			// We assume (perhaps incorrectly) that a null element 
			// should be an empty string.
			//
			Class<?> c = classToUseForNullValues;
			if (this.get(i) != null) c = this.get(i).getClass();
			types.add(c);
		}

		if (types.size() == 1) {
			result = types.iterator().next();
		} 

		return result;
	}

	public void add(int i) {
		trace();
		
		super.add(i);
	}

	public void add(String s) {
		trace();
		
		super.add(s);
	}

	public static Values parse(String rhs) {
		trace();
		
		return parse(rhs, 0);
	}

	public static Values parse(String rhs, int lineNumber) {
		trace();
		
		Values values = new Values();
		Matcher matcher = individualValuesPattern.matcher(rhs);

		int[] groupCounts = new int[NUMBER_OF_GROUPS+1];
		
		while (matcher.find()) {
			
			String value = null;
		    int group = 0;
			for (int i=1; i <= NUMBER_OF_GROUPS; i++) {
				if (matcher.group(i) == null) continue;
				
				if (value != null) {
					
					// The structure of the regex is such that only one group
					// should be non-null for a given match. If we have a group that is
					// not null, and the value has already been assigned, this is a
					// problem.
					//
					throw new IniParseException(
							IniParseException.ErrorMessage.UNEXPECTED_REGEX_STATE,
							rhs, lineNumber);
				}
				
				groupCounts[i]++;					
				value = matcher.group(i);
				group = i;
			}

			if (value != null) {
				boolean isInteger = false;
				boolean isLong = false;
				boolean isDouble = false;

				if (group == NON_QUOTED_STRING_GROUP) {
					try {
						values.add(Integer.parseInt(value));
						isInteger = true;
					} catch (NumberFormatException ex) {
						isInteger = false; // probably not necessary		
					}
					
					if (!isInteger) {
						try {
							values.add(Long.parseLong(value));
							isLong = true;
						} catch (NumberFormatException ex) {
							isLong = false;					
						}
					}

					if (!isInteger && !isLong) {
						try {
							values.add(Double.parseDouble(value));
							isDouble = true;
						} catch (NumberFormatException ex) {
							isDouble = false;
						}
					}
				}

				if (value.toString().equals("")) {
					//values.add(null);				
					values.add("");
				} else if (!isInteger && !isDouble && !isLong) {
					
					// Replace predefined XML entities
					//value = value.replace("&", "&amp;");				
					values.add(value.toString());					
				}
			}
		}
		
		for (int i=1; i <= NUMBER_OF_GROUPS; i++) {
			if (groupCounts[i] == values.size()) {
				values.commonLeftDelimiter = leftDelims[i];
				values.commonRightDelimiter = rightDelims[i];
			}
		}
				
		
		//
		// We have to artificially add back an null value if there were
		// only commas in the right hand side of the expression. That is,
		// 'n' commas leads to 'n+1' entries. For example, "," is two blanks,
		// even though there is only one comma.
		//

		boolean nullsOnly = true;
		Object nullObject = "";
		for (Object value : values) {
			if (value != nullObject) {
				nullsOnly = false; break;
			}
		}
		
		if (values.size() > 0 && nullsOnly) {
			values.add(nullObject);
		}

		return values;
	}
	
	private static final Pattern individualValuesPattern = Pattern.compile(
			"(?:\n" +
					"  (?:^,())                                           | # Group 1; leading comma\n" +
					"  (?:,?\\s*(\"[^\"\\\\]*(?:\\\\.[^\"\\\\]*)*\"))     | # Group 2; double-quoted string\n" +
					"  (?:,?\\s*('[^'\\\\]*(?:\\\\.[^'\\\\]*)*'))         | # Group 3; single-quoted string\n" +
					"  (?:,?\\s*(\\([^(\\\\]*(?:\\\\.[^)\\\\]*)*\\)))     | # Group 4; parenthesized string\n" +
					"  (?:,?\\s*(\\[[^\\[\\\\]*(?:\\\\.[^\\]\\\\]*)*\\])) | # Group 5; square bracketed string\n" +
					"  (?:,?\\s*(\\{[^{\\\\]*(?:\\\\.[^}\\\\]*)*\\}))     | # Group 6; curly bracketed string\n" +
					"  (?:,?\\s*([^,;\\s]+(?:[^,;\\s]+)*))            | # Group 7; non-quoted string\n" +
					"  (?:,()\\s*)                                          # Group 8; trailing comma\n" +
					")\n" +
					"(?:\\s*(?:;.*$)?)                                      # Trailing comment", 
					Pattern.COMMENTS);
	
	//private static final int LEADING_COMMA = 1;
	//private static final int TRAILING_COMMA = 8;
	private static final int DOUBLE_QUOTED_STRING_GROUP = 2;
	private static final int SINGLE_QUOTED_STRING_GROUP = 3;
	private static final int PAREN_QUOTED_STRING_GROUP = 4;
	private static final int SQUARE_QUOTED_STRING_GROUP = 5;
	private static final int CURLY_QUOTED_STRING_GROUP = 6;
	private static final int NON_QUOTED_STRING_GROUP = 7;
	private static final int NUMBER_OF_GROUPS = 8;
	
	private static String[] leftDelims;
	private static String[] rightDelims;
	
	static {
		leftDelims = new String[NUMBER_OF_GROUPS+1];
		rightDelims = new String[NUMBER_OF_GROUPS+1];
		for (int i=0; i <= NUMBER_OF_GROUPS; i++) {
			leftDelims[i]=""; rightDelims[i]="";
		}
						
		leftDelims[DOUBLE_QUOTED_STRING_GROUP] = "\""; rightDelims[DOUBLE_QUOTED_STRING_GROUP] = "\"";
		leftDelims[SINGLE_QUOTED_STRING_GROUP] = "'";  rightDelims[SINGLE_QUOTED_STRING_GROUP] = "'";
		leftDelims[PAREN_QUOTED_STRING_GROUP]  = "(";  rightDelims[PAREN_QUOTED_STRING_GROUP]  = ")";
		leftDelims[SQUARE_QUOTED_STRING_GROUP] = "[";  rightDelims[SQUARE_QUOTED_STRING_GROUP] = "]";
		leftDelims[CURLY_QUOTED_STRING_GROUP]  = "{";  rightDelims[CURLY_QUOTED_STRING_GROUP]  = "}";		

	}
	
	private String commonLeftDelimiter = "";

	public String getCommonLeftDelimiter(boolean escaped) {
		String result = this.commonLeftDelimiter;
		if (escaped && !"".equals(commonLeftDelimiter))
			result = "\\" + result;
		return result;
	}
	
	private String commonRightDelimiter = "";
	public String getCommonRightDelimiter(boolean escaped) {
		String result =	this.commonRightDelimiter;
		if (escaped && !"".equals(commonRightDelimiter))
			result = "\\" + result;
		return result;
	}
	
	public String getUnquotedString(int i) {
		String result = "";
		if (get(i) == null) {
			result = "";
		} else if (commonLeftDelimiter.isEmpty() && commonRightDelimiter.isEmpty()) {
			result = get(i).toString();
		} else {
			result = unquote(get(i).toString());
		}		
		return result;
	}
	
	public static String unquote(String string) {
		trace();

		String result = "";

		final String leftDelims[] = new String[] { "\"", "'", "(", "[", "{", "<" };
		final String rightDelims[] = new String[] { "\"", "'", ")", "]", "}", ">" };

		for (int i=0; i < leftDelims.length; i++) {
			if (string.length() > 1 &&
				string.startsWith(leftDelims[i]) &&
				string.endsWith(rightDelims[i])) {
				result = string.substring(1, string.length() - 1);
				break;
			}
		}
		return result;
	}

	private static final long serialVersionUID = 6123488480724541595L;

}
