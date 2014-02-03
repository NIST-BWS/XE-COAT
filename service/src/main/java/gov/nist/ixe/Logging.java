package gov.nist.ixe;

import java.util.GregorianCalendar;

public class Logging {
	private static boolean LOG = true;
	private static boolean DEBUG = false;
	private static boolean INFO = true;
	private static boolean TRACE = false;
	private static boolean WARN = true;
	private static boolean HTTP = true;
	
	private static boolean SHOW_TIMESTAMP = true;
	private static boolean SHOW_THREAD_ID = false;
	private static boolean SHOW_LEVEL = true;

	public static void log(String level, String format, Object... parameters) {
		if (LOG) {
			String formatString = "";
			
			if (SHOW_THREAD_ID)
				formatString += "[%1$03d] ";
			
			if (SHOW_TIMESTAMP)
				formatString += "[%2$tY-%2$tm-%2$td %2$tH:%2$tM:%2$tS.%2$tL] ";
			
			if (SHOW_LEVEL)
				formatString += "[%3$s] ";
			
			formatString += "%4$s";		//message
			
			formatString += "%n";		//duh
			
			GregorianCalendar gc = new GregorianCalendar();
			System.out.printf(formatString, Thread.currentThread().getId(), gc, level, String.format(format, parameters));
		}
	}
	
	public static void debug(String format, Object... parameters) {
		if (DEBUG && LOG) {
			log("DEBUG", format, parameters);
		}
	}
	
	public static void warn(String format, Object... parameters) {
		if (WARN && LOG) {
			log("WARNING", format, parameters);
		}
	}
	
	public static void info(String format, Object... parameters) {
		if (INFO && LOG) {
			log("INFO", format, parameters);
		}
	}
	
	public static void http(String format, Object... paramaters) {
		if (HTTP && LOG) {
			log("HTTP", format, paramaters);
		}
	}
	
	public static void trace() {
		if (TRACE && LOG) {
			StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
			log("TRACE", "[%04d] %s.%s()", ste.getLineNumber(), ste.getClassName(), ste.getMethodName());
		}
	}
}