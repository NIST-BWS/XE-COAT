 /*----------------------------------------------------------------------------------------------------+
 |                             National Institute of Standards and Technology                          |
 +-----------------------------------------------------------------------------------------------------+
  
  File author(s):
  	   Ross J. Micheals (ross.micheals@nist.gov)
        
 +-----------------------------------------------------------------------------------------------------+
 | NOTICE & DISCLAIMER                                                                                 |
 |                                                                                                     |
 | The research software provided on this web site ("software") is provided by NIST as a public        |
 | service. You may use, copy and distribute copies of the software in any medium, provided that you   |
 | keep intact this entire notice. You may improve, modify and create derivative works of the software |
 | or any portion of the software, and you may copy and distribute such modifications or works.        |
 | Modified works should carry a notice stating that you changed the software and should note the date |
 | and nature of any such change.  Please explicitly acknowledge the National Institute of Standards   |
 | and Technology as the source of the software.                                                       |
 |                                                                                                     |
 | The software is expressly provided "AS IS."  NIST MAKES NO WARRANTY OF ANY KIND, EXPRESS, IMPLIED,  |
 | IN FACT OR ARISING BY OPERATION OF LAW, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTY OF      |
 | MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, NON-INFRINGEMENT AND DATA ACCURACY.  NIST        |
 | NEITHER REPRESENTS NOR WARRANTS THAT THE OPERATION OF THE SOFTWARE WILL BE UNINTERRUPTED OR         |
 | ERROR-FREE, OR THAT ANY DEFECTS WILL BE CORRECTED.  NIST DOES NOT WARRANT OR MAKE ANY               |
 | REPRESENTATIONS REGARDING THE USE OF THE SOFTWARE OR THE RESULTS THEREOF, INCLUDING BUT NOT LIMITED |
 | TO THE CORRECTNESS, ACCURACY, RELIABILITY, OR USEFULNESS OF THE SOFTWARE.                           |
 |                                                                                                     |
 | You are solely responsible for determining the appropriateness of using and distributing the        |
 | software and you assume all risks associated with its use, including but not limited to the risks   |
 | and costs of program errors, compliance with applicable laws, damage to or loss of data, programs   |
 | or equipment, and the unavailability or interruption of operation.  This software is not intended   |
 | to be used in any situation where a failure could cause risk of injury or damage to property.  The  |
 | software was developed by NIST employees.  NIST employee contributions are not subject to copyright |
 | protection within the United States.                                                                |
 |                                                                                                     |
 | Specific hardware and software products identified in this open source project were used in order   |
 | to perform technology transfer and collaboration. In no case does such identification imply         |
 | recommendation or endorsement by the National Institute of Standards and Technology, nor            |
 | does it imply that the products and equipment identified are necessarily the best available for the |
 | purpose.                                                                                            |
 +----------------------------------------------------------------------------------------------------*/

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