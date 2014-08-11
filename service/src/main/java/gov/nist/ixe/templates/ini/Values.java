package gov.nist.ixe.templates.ini;

import static gov.nist.ixe.Logging.trace;
import gov.nist.ixe.StringUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class Values extends ArrayList<Object> {
	
	private Values() {

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

	public static Values parse(String rhs) {
		trace();		
		return parse(rhs, 0);
	}
	
	private String[] trimValuesAsStrings = null;
	public static Values parse(String rhs, int lineNumber) {
		
		if (rhs == null) rhs = "";
		rhs = rhs.trim();
		
		String[] values, commaValues, spaceValues;
		commaValues = commaPattern.split(rhs, -1);
		
		if (commaValues.length <= 1) {
			spaceValues = spacePattern.split(rhs);
			if (spaceValues.length <= 1) {				
				values = new String[] { rhs };
			} else {
				values = spaceValues;				
			}			
		} else {
			values = commaValues;
		}
		
		Values result = new Values();
		
		result.trimValuesAsStrings = new String[values.length];
		for (int i = 0; i < values.length; i++) {
			Object newValue = recastValue(values[i]); 
			result.add(newValue);
			result.trimValuesAsStrings[i] = newValue.toString(); 
		}
		
		result.populateMostDetailedCommonType();
		result.populateDelimiters();
		
		
		return result;
	}
	
	private static Object recastValue(String value) {
		
		if (value == null) value = "";
		value = value.trim();
		
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
		
	//result.commonLeftDelimiter = StringUtil.longestCommonPrefix(values, maxStringLength/2);
	//result.commonRightDelimiter = StringUtil.longestCommonSuffix(values, maxStringLength/2);
	
	
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
	
	private void populateDelimiters() {
		if (trimValuesAsStrings.length > 1) {		
			
			int maxStringLength = 0;
			for (int i=0; i < this.trimValuesAsStrings.length; i++) {
				maxStringLength = Math.max(maxStringLength, trimValuesAsStrings[i].length());
			}
			
			if (getMostDetailedCommonType().equals(String.class)) {
				int maxDelimiterLength = Math.min(maximumLeftOrRightDelimiterLength, maxStringLength/2);
				commonLeftDelimiter = StringUtil.longestCommonPrefix(trimValuesAsStrings, maxDelimiterLength);
				commonRightDelimiter = StringUtil.longestCommonSuffix(trimValuesAsStrings, maxDelimiterLength);
			} else {
				commonLeftDelimiter = "";
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
	
	public static String unquote(String string, String leftDelim, String rightDelim) {
		trace();

		String result = "";

		if (string.length() > 1 &&
			string.startsWith(leftDelim) &&
			string.endsWith(rightDelim)) {
			result = string.substring(leftDelim.length(), string.length() - rightDelim.length());
		}
		
		
		return result;
	}

	private static final long serialVersionUID = 6123488480724541595L;

}
