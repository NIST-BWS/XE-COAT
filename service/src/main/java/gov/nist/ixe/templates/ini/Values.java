package gov.nist.ixe.templates.ini;

import static gov.nist.ixe.Logging.trace;
import gov.nist.ixe.StringUtil;
import gov.nist.ixe.UnreachableCodeException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Values extends ArrayList<Object> {
	
	private Values() {
		// Forbid instantiation through the default constructor
	}
	
	private static String finderSuffix = "(" +
			"(?=(?:[^']*'[^']*')*[^']*$)" +  
			"(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)" +
			"(?![^()]*+\\))" +
			"(?![^\\[\\]]*+\\])" +
			"(?![^{}]*+\\})" +
			"(?![^<>]*+>)" +
			")"; 
		
	private static Pattern commaPattern = Pattern.compile("," + finderSuffix);
	private static Pattern spacePattern = Pattern.compile("\\ +" + finderSuffix);
	
	
	private static int maximumLeftOrRightDelimiterLength = 3;

	public boolean isMultiple() {
		trace();
		return super.size() > 1;
	}
	
	private Class<?> mostDetailedCommonType = null;
	private void populateMostDetailedCommonType() {
		
		// If there are no values, then default to a string instead
		// of a null reference, which JAXB has a hard time dealing
		// nicely with
		//
		if (size() == 0) {
			this.mostDetailedCommonType = String.class;	
		}

		Set<Class<?>> types = new HashSet<Class<?>>();
		this.mostDetailedCommonType = Object.class;

		for (int i=0; i < this.size(); i++) {

			Class<?> c = String.class; // We assume that a null element should be an empty string.
			if (this.get(i) != null) c = this.get(i).getClass();
			types.add(c);
		}

		if (types.size() == 1) {
			this.mostDetailedCommonType = types.iterator().next();
		} 
	}
	
	public Class<?> getMostDetailedCommonType() {	
		return this.mostDetailedCommonType;
	}


	public void add(int i) {
		trace();
		
		super.add(i);
	}

	public void add(String s) {
		trace();
		
		super.add(s);
	}

	public static Values parse(String unparsedValues) {
		trace();		
		return parse(unparsedValues, 0);
	}
	
	public static Values parse(String source, int lineNumber) {
		return parse(source, lineNumber, KeyValueLineValuesSplitterStyle.CommasOrSpaces);
	}
	
	private String originalSource = null;
	
	public static Values parse(String source, int lineNumber, KeyValueLineValuesSplitterStyle style) {
				
		
		if (source == null) source = "";
		source = source.trim();
		
		String[] values = {}, commaValues, spaceValues;
		
		if (style == KeyValueLineValuesSplitterStyle.CommasOnly) {
			commaValues = commaPattern.split(source, -1);
			values = commaValues;
		} else if (style == KeyValueLineValuesSplitterStyle.SpacesOnly) {
			spaceValues = spacePattern.split(source, -1);
			values = spaceValues;
		} else if (style == KeyValueLineValuesSplitterStyle.CommasOrSpaces) {
			commaValues = commaPattern.split(source, -1);
			if (commaValues.length <= 1) {
				spaceValues = spacePattern.split(source);
				if (spaceValues.length <= 1) {				
					values = new String[] { source };
				} else {
					values = spaceValues;				
				}			
			} else {
				values = commaValues;
			}
		} 
		
		Values result = new Values();		
		
		result.originalSource = source;
		
		String[] trimValuesAsStrings = new String[values.length];
		for (int i = 0; i < values.length; i++) {
			trimValuesAsStrings[i] = values[i].trim(); 
		}
		result.populateLeftRightDelimiters(trimValuesAsStrings);
		
		for (int i = 0; i < values.length; i++) {
			Object newValue = recastValue(values[i], result.commonLeftDelimiter, result.commonRightDelimiter); 
			result.add(newValue);
			
		}
		
		result.populateMostDetailedCommonType();
		result.populateValuesDelimiter(style);
		
		
		return result;
	}
	
	private static Object recastValue(String value, String leftDelim, String rightDelim) {
		
		if (value == null) value = "";
		value = value.trim();
		value = Values.unquote(value, leftDelim, rightDelim);
		
		Object result = null;
				
		try {
			result = Integer.parseInt(value);
		} catch (NumberFormatException ex) {
			/* swallow */
		}
		
		if (result == null) { try {
			result = Long.parseLong(value);
		} catch (NumberFormatException ex) {
			/* swallow */						
		} }
		

		if (result == null) { try {
			result = Double.parseDouble(value);
	
		} catch (NumberFormatException ex) {
			/* swallow */						
		} }

		if (result == null) {
			result = value;
		}
		
		return result;
	}
			
	private String commonLeftDelimiter = null;
	public String getCommonLeftDelimiter(boolean escaped) {
		
		String result = this.commonLeftDelimiter;
		if (escaped && !"".equals(commonLeftDelimiter))
			result = "\\" + result;
		return result;
	}
	
	private String commonRightDelimiter = null;
	public String getCommonRightDelimiter(boolean escaped) {
			
		String result =	this.commonRightDelimiter;
		if (escaped && !"".equals(commonRightDelimiter))
			result = "\\" + result;
		return result;
	}
	

	private static Pattern nonWordPrefix = Pattern.compile("^[^-\\w]*"); 
	private static Pattern nonWordSuffix = Pattern.compile("[^\\w]*$");

	private void populateLeftRightDelimiters(String[] trimmedValues) {
		if (trimmedValues.length > 1) {		
			
			int maxStringLength = 0;
			for (int i=0; i < trimmedValues.length; i++) {
				maxStringLength = Math.max(maxStringLength, trimmedValues[i].length());
			}

			int maxDelimiterLength = Math.min(maximumLeftOrRightDelimiterLength, maxStringLength/2);
			String lcp = StringUtil.longestCommonPrefix(trimmedValues, maxDelimiterLength);
			Matcher prefixMatcher = nonWordPrefix.matcher(lcp);
			if (prefixMatcher.find()) {
				commonLeftDelimiter = prefixMatcher.group();
			} else {
				commonLeftDelimiter = "";
			}
			String lcs = StringUtil.longestCommonSuffix(trimmedValues, maxDelimiterLength);
			Matcher suffixMatcher = nonWordSuffix.matcher(lcs);
			if (suffixMatcher.find()) {
				commonRightDelimiter = suffixMatcher.group();
			} else {
				commonRightDelimiter = "";
			}
		} else {
			// If there is only one string, don't bother with a prefix
			commonLeftDelimiter = "";
			commonRightDelimiter = "";
		}
	}
	
	
	
	public String getUnquotedString(int i) {
		String result = "";
		if (get(i) == null) {
			result = "";
		} else if (getCommonLeftDelimiter(false).isEmpty() && 
				   getCommonRightDelimiter(false).isEmpty()) {
			result = get(i).toString();
		} else {
			result = unquote(get(i).toString(), commonLeftDelimiter, commonRightDelimiter);
		}		
		return result;
	}
	
	public static String unquote(String str, String leftDelim, String rightDelim) {
		trace();

		String result = "";

		if (str.length() > 1 &&
				str.startsWith(leftDelim) &&
				str.endsWith(rightDelim)) {
			result = str.substring(leftDelim.length(), str.length() - rightDelim.length());
		} else {
			result = str;
		}
		
		
		return result;
	}

	private static final long serialVersionUID = 6123488480724541595L;

		
	private static String getDelimiterDetectionRegex(
			ArrayList<Object> recastObjects, 
			boolean ignoreWhitespace, String leftDelim, String rightDelim) {
		trace();
		
		String result = null;
		StringBuilder builder = new StringBuilder();
		
		for (int i=0; i < recastObjects.size(); i++) {
			String value = "";
			if (recastObjects.get(i) != null) {
				value = recastObjects.get(i).toString();
			}

			// We get a more robust delimiter detection routine
			// if we accept whitespace before or after the token
			
			if (ignoreWhitespace) builder.append("[\\s]*"); 
			builder.append(Pattern.quote(leftDelim + value + rightDelim));
			if (ignoreWhitespace) builder.append("[\\s]*");
			
			if (i == 0) {
				builder.append("([^\\w]*)");
			} else if (i < recastObjects.size()-1) {
				builder.append("\\1+");
			}
		}
		
		if (recastObjects.size() > 1) {
			result = builder.toString();
		}
		return result;
	}
	
	
	public String getValuesDelimiter() {
		return valuesDelimiter;		
	}
	
	private String valuesDelimiter;
	private void populateValuesDelimiter(KeyValueLineValuesSplitterStyle style) {
		if (this.size() > 1) { 
			valuesDelimiter = extractValuesDelimiter(this, originalSource, commonLeftDelimiter, commonRightDelimiter);			
		} else {
			valuesDelimiter = "";
		}
		
		if (valuesDelimiter == null) {
			
			// Sometime we can't determine the values delimiter. This can happen
			// if formatting is lost when we try to infer the values from the string;
			// for example, '01' becomes '1', and therefore, we can't infer
			// the values delimiter based on the way we build the dynamic regex 
			// that seeks the values delimiter.
			//
			if (style == KeyValueLineValuesSplitterStyle.SpacesOnly) {
				valuesDelimiter = " ";
			} else if (style == KeyValueLineValuesSplitterStyle.CommasOnly) {
				valuesDelimiter = ",";
			} else if (style == KeyValueLineValuesSplitterStyle.CommasOrSpaces) {
				Matcher unquotedCommaMatcher = commaPattern.matcher(originalSource);
				if (unquotedCommaMatcher.find()) {
					valuesDelimiter = ",";
				} else {
					valuesDelimiter = " ";	
				}
			} else {
				throw new UnreachableCodeException();
			}
			
		}
		
	}
	
	private static String extractValuesDelimiter(ArrayList<Object> recastObjects, String source, String leftDelim, String rightDelim) {
		
		String result = null;
		
		String ddr = getDelimiterDetectionRegex(recastObjects, false, leftDelim, rightDelim);
		if (ddr != null) {
			Pattern pattern = Pattern.compile(ddr);
			Matcher matcher = pattern.matcher(source.trim());
			if (matcher.find()) { 
				result = matcher.group(1); 
			}
		}

		if (result == null) {
			ddr = getDelimiterDetectionRegex(recastObjects, true, leftDelim, rightDelim);
			Pattern pattern = Pattern.compile(ddr);
			Matcher matcher = pattern.matcher(source.trim());
			if (matcher.find()) { 
				result = matcher.group(1); 
			}
		}
		
		
	
		
		return result;
		
	}
	
}
