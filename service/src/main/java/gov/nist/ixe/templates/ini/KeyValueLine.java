package gov.nist.ixe.templates.ini;

import static gov.nist.ixe.Logging.trace;
import gov.nist.ixe.templates.ini.exception.IniParseException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeyValueLine {

	private KeyValueLine() {
		trace();
	}

	private static String finderSuffix = "(" +
			"(?=(?:[^']*'[^']*')*[^']*$)" +  
			"(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)" +
			"(?![^()]*+\\))" +
			"(?![^\\[\\]]*+\\])" +
			"(?![^{}]*+\\})" +
			"(?![^<>]*+>)" +
			")"; 
	
	private static Pattern lhsPattern = Pattern.compile("(^\\s*[^=\\s]+\\s*=)");
	private static Pattern keyPattern = Pattern.compile("^\\s*([^=\\s]+)\\s*=");
	private static Pattern semicolonPattern = Pattern.compile(";" + finderSuffix); 
	
	
	public static KeyValueLine parse(String line, int lineNumber) {
		trace();
		
		// A static method lets us return a null if the input is not a valid key-value line.
		
		KeyValueLine result = null;
		
		
		Matcher keyMatch = keyPattern.matcher(line);
		if (keyMatch.find()) {
			
			result = new KeyValueLine();
			result.lineNumber = lineNumber;
			result.originalLine = line;
			result.key = keyMatch.group(1);
			result.keyStart = keyMatch.start(1);
			
			Matcher lhsMatch = lhsPattern.matcher(line);
			Matcher semicolonMatch = semicolonPattern.matcher(line);
			
			if (lhsMatch.find()) {
				result.lhs = lhsMatch.group(1);
				result.valuesStart = lhsMatch.end(1);
			}
			
			if (semicolonMatch.find()) {
				result.commentStart = semicolonMatch.start(0); // Note the use of group 0, not group 1
				result.values = line.substring(result.valuesStart, result.commentStart);
				result.comment = line.substring(result.commentStart);
			} else {
				result.values = line.substring(result.valuesStart);	
				result.commentStart = line.length();
			}
			
			String lhs = lhsMatch.group(1);
			
			if (!line.equals(lhs + result.values + result.comment)) {
				throw IniParseException.malformedKeyValueLine(line, lineNumber);
			}
			
		} else {			
			throw IniParseException.missingOrMalformedKey(line, lineNumber);			
		}
		
		
		return result;
	}

	public String getOriginalLine() {
		return originalLine;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public String getKey()   { return key; }	
	public int getKeyStart() { return keyStart; }
	public int getKeyEnd()   { return keyStart + key.length(); }
	
	public String getValues()   { return values; }
	public int getValuesStart() { return valuesStart; }
	public int getValuesEnd()   { return valuesStart + values.length(); }
	
	public String getComment()   { return comment; }
	public int getCommentStart() { return commentStart; }
	
	public String getLhs() { return lhs; };

	private String originalLine;
	private int lineNumber;

	private String key = "";   	
	private int keyStart = -1;	
	
	private String values = ""; 	
	private int valuesStart = -1;       
	
	private String comment = ""; 
	private int commentStart = -1;
	
	private String lhs = "";

	/*
	private enum DelimiterDetectionStyle {
		RespectWhitespace, IgnoreWhitespace
	}
	
	private String getDelimiterDetectionRegex(DelimiterDetectionStyle dds) {
		trace();
		
		String result = null;
		Values values = Values.parse(this.values.trim());
		StringBuilder builder = new StringBuilder();
		
		for (int i=0; i < values.size(); i++) {
			String value = "";
			if (values.get(i) != null) {
				value = values.get(i).toString();
			}

			// We get a more robust delimiter detection routine
			// if we accept whitespace before or after the token
			
			if (dds == DelimiterDetectionStyle.IgnoreWhitespace)
				builder.append("[\\s]*"); 
			builder.append(Pattern.quote(value));
			if (dds == DelimiterDetectionStyle.IgnoreWhitespace)
				builder.append("[\\s]*");
			
			if (i == 0) {
				builder.append("(.*)");
			} else if (i < values.size()-1) {
				builder.append("\\1+");
			}
		}
		
		if (values.size() > 1) {
			result = builder.toString();
		}
		return result;
	}
	
	public String getDelimiter() {
		trace();
		
		String result = null;
		
		String ddr = getDelimiterDetectionRegex(DelimiterDetectionStyle.RespectWhitespace);
		if (ddr != null) {
			Pattern pattern = Pattern.compile(ddr);
			Matcher matcher = pattern.matcher(values.trim());
			if (matcher.find()) { 
				result = matcher.group(1); 
			}
		}

		if (result == null) {
			ddr = getDelimiterDetectionRegex(DelimiterDetectionStyle.IgnoreWhitespace);
			Pattern pattern = Pattern.compile(ddr);
			Matcher matcher = pattern.matcher(values.trim());
			if (matcher.find()) { 
				result = matcher.group(1); 
			}
		}
		
		
		if (result == null) {
			// If we can't determine the delimiter, then just use a space. 
			result = " ";
		}
		
		return result;
	}
	*/

	



}
