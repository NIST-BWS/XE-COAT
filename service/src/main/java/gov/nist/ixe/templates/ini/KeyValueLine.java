package gov.nist.ixe.templates.ini;

import static gov.nist.ixe.Logging.trace;

import gov.nist.ixe.templates.ini.exception.IniParseException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeyValueLine {

	private KeyValueLine() {
		trace();
	}

	public static KeyValueLine parse(String line, int lineNumber) {
		trace();
		
		//
		// We use a static method instead of a constructor so that we can return a null if 
		// the input is not a valid key-value line.
		//
		KeyValueLine result = null;
		Matcher matcher = keyValueLinePattern.matcher(line);
		if (matcher.find()) {

			String lhs = matcher.group(LHS_GROUP);
			String key = matcher.group(KEY_GROUP);
			String values = matcher.group(VALUES_GROUP);
			String comment = matcher.group(COMMENT_GROUP);

			if (!line.equals(lhs + values + comment)) {
				throw new IniParseException
				(IniParseException.ErrorMessage.MALFORMED_KEY_VALUE_LINE, line, lineNumber);
			}


			if (key != null && values != null) {
				result = new KeyValueLine();
				result.originalLine = line;
				result.lineNumber = lineNumber;

				result.key = key;
				result.keyStart = matcher.start(KEY_GROUP);
				result.keyEnd = matcher.end(KEY_GROUP);

				result.values = values;
				result.valuesStart = matcher.start(VALUES_GROUP); 
				result.valuesEnd = matcher.end(VALUES_GROUP);

				result.comment = comment;
				result.commentStart = matcher.start(COMMENT_GROUP);
				result.commentEnd = matcher.end(COMMENT_GROUP);

				result.lhs = lhs;
				result.lhsStart = matcher.start(LHS_GROUP);
				result.lhsEnd = matcher.end(LHS_GROUP);

			}
			
			
		} else {
			throw new IniParseException
			(IniParseException.ErrorMessage.MALFORMED_KEY_VALUE_LINE, line, lineNumber);
		}

		return result;
	}

	public String getOriginalLine() {
		return originalLine;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public String getKey() { return key; }	
	public int getKeyStart() { return keyStart; }
	public int getKeyEnd() { return keyEnd; }

	public String getValues() {	return values; }
	public int getValuesStart() { return valuesStart; }
	public int getValuesEnd() { return valuesEnd; }

	public String getComment() { return comment; }
	public int getCommentStart() { return commentStart; }
	public int getCommentEnd() { return commentEnd; }

	public String getLhs() { return lhs; }
	public int getLhsStart() { return lhsStart; }
	public int getLhsEnd() { return lhsEnd; }

	public String getValuesDelimiter() { return valuesDelimiter; }
	

	private String originalLine;
	private int lineNumber;

	private String key;   	private int keyStart;	       private int keyEnd;
	private String lhs;   	private int lhsStart;	       private int lhsEnd;
	private String values; 	private int valuesStart;       private int valuesEnd;
	private String comment; private int commentStart = -1; private int commentEnd; 
	private String valuesDelimiter = ",";

	private static final int LHS_GROUP = 1;
	private static final int KEY_GROUP = 2;
	private static final int VALUES_GROUP = 3;
	private static final int COMMENT_GROUP = 4;
	
	private static final Pattern keyValueLinePattern = Pattern.compile(
					         "^(\\s*([^=\\s]+)\\s*=\\s*)                               # Groups 1 & 2 / LHS & Key\n"
							+ "(\n"
							+ "(?:                                                      # Group 3 / Values\n"
							+ "  \\s*\n"
							+ "  (?:^,)                                               | # Leading comma\n"
							+ "  (?:,?\\s*\"(?:[^\"\\\\]*(?:\\\\.[^\"\\\\]*)*)\")     | # Double-quoted strings\n"
							+ "  (?:,?\\s*'(?:[^'\\\\]*(?:\\\\.[^'\\\\]*)*)')         | # Single-quoted strings\n"
							+ "  (?:,?\\s*\\((?:[^(\\\\]*(?:\\\\.[^)\\\\]*)*)\\))     | # Parenthesized strings\n"
							+ "  (?:,?\\s*\\[(?:[^\\[\\\\]*(?:\\\\.[^\\]\\\\]*)*)\\]) | # Square-bracketed strings\n"
							+ "  (?:,?\\s*\\{(?:[^{\\\\]*(?:\\\\.[^}\\\\]*)*)\\})     | # Curly-bracketd strings\n"
							+ "  (?:,?\\s*(?:[^,;\\s]+(?:\\s+[^,;\\s]+)*))            | # Undelimited strings\n"
							+ "  (?:,\\s*)                                              # Trailing comma\n"
							+ ")*\\s*)\n"
							+ "((?:;.*$)?)                                              # Group 3 / Comment",
					Pattern.COMMENTS);

	
	private String getDelimiterDetectionRegex() {
		trace();
		
		String result = null;
		Values values = Values.parse(this.values.trim());
		StringBuilder builder = new StringBuilder();
		
		for (int i=0; i < values.size(); i++) {
			builder.append(Pattern.quote(values.get(i).toString()));
			
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
		
		String result = "";
		String delimiterDetectionRegex = getDelimiterDetectionRegex();
		
		if (delimiterDetectionRegex != null) {
			Pattern pattern = Pattern.compile(delimiterDetectionRegex);
			Matcher matcher = pattern.matcher(values.trim());
			
			if (matcher.find()) { 
				result = matcher.group(1); 
			}
		}
		
		return result;
	}
	

	



}
