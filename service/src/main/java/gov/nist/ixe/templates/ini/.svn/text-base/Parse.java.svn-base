package gov.nist.ixe.templates.ini;

import static gov.nist.ixe.Logging.trace;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parse {
	//private static final Pattern nonSubstantiveLinePattern = Pattern.compile("^(?:^\\s*(;.*)?$)*$");
	private static final Pattern nonSubstantiveLinePattern = Pattern.compile("^(?:^\uFEFF?\\s*(;.*)?$)*$");

	private static final Pattern headerPattern = Pattern.compile(
			        "(?:\n" +
					"^\uFEFF?\\s*\\[\\s*       # Open square bracket\n" +
					"(                  # Start capture\n" +
					"[^\\s;]+           # One or more non-ws chars\n" +
					"(?:\\s+[^\\s;]+)*  # Zero or more groups of ws chars and then non-ws chars\n" +
					")                  # End capture\n" +
					"\\s*\\]\\s*        # Close square bracket\n" +
					"(?:;.*)?$          # Non-ws chars must be part of a comment\n" +
					")", 
					Pattern.COMMENTS);
	
	public static boolean isSubstantiveLine(String line) {
		trace();
		
		Matcher matcher = nonSubstantiveLinePattern.matcher(line);
		return !matcher.matches();
	}

	public static String getHeader(String line) {
		trace();
		
		String header = null;
		Matcher matcher = headerPattern.matcher(line);
		
		if (matcher.find() && matcher.group(1) != null) {
			header = matcher.group(1);
		}
		
		return header;
	}
}