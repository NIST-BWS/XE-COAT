package gov.nist.ixe;

import static gov.nist.ixe.Logging.trace;

import java.io.StringWriter;
import java.util.List;

public class StringUtil {

	public static String quote(String string) {
		trace();

		return "\"" + string + "\"";
	}

	
	public static String removeNewlinesAndTabs(String string) {
		String result = null;
		if (string != null) {
			result = string.replace("\n", "").replace("\r", "")
					.replace("\t", "");
		}
		return result;
	}

	public static String capitalize(String string) {
		return Character.toUpperCase(string.charAt(0)) + string.substring(1);
	}

	public static String capitalize(String string, int index) {
		String result = string;

		if (index >= 0 && index <= string.length() - 1) {
			result = string.substring(0, index)
					+ Character.toUpperCase(string.charAt(index))
					+ string.substring(index + 1);
		}
		return result;
	}

	public static String flatten(List<String> lines, String newline) {
		StringWriter writer = new StringWriter();
		for (String line : lines) {
			if (line == null) {
				continue;
			}
			writer.write(line + newline);
		}

		return writer.toString();
	}
	
	public static boolean isBlank(String str) {
		return str == null || str.isEmpty() || str.trim().isEmpty();
	}
	
	public static String longestCommonPrefix(String[] strs, int max) {

		// Adapted from http://tinyurl.com/nhzjrnb (Runhe Tian)
		String prefix = new String();
		if(strs.length > 0)
			prefix = strs[0];
		for(int i = 1; i < strs.length; ++i) {
			String s = strs[i];
			int j = 0;
			for(; j < Math.min(prefix.length(), Math.min(max, s.length())); ++j) {
				if(prefix.charAt(j) != s.charAt(j)) {
					break;
				}
			}
			prefix = prefix.substring(0, j);
		}
		return prefix;
	}
	
	public static String longestCommonSuffix(String[] strs, int max) {
		
		String[] reverseStrs = new String[strs.length];
		
		for (int i =0; i < strs.length; i++) {
			reverseStrs[i] = new StringBuilder(strs[i]).reverse().toString();			
		}
		return new StringBuilder(longestCommonPrefix(reverseStrs, max)).reverse().toString();

	}

}
