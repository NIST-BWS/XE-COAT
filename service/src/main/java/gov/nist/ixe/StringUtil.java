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

}
