/*----------------------------------------------------------------------------------------------------+
|                             National Institute of Standards and Technology                          |
|                                        Biometric Clients Lab                                        |
+-----------------------------------------------------------------------------------------------------+
 File author(s):
 	   Ross J. Micheals (ross.micheals@nist.gov)
 	   Kevin Mangold (kevin.mangold@nist.gov)
       
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


package gov.nist.ixe.templates.ini.exception;

public class IniParseException extends RuntimeException {
	
	


	public IniParseException(String message, String sourceData, int lineNumber) {
		super(message);
	}
	
	
	public static IniParseException malformedKeyValueLine(String sourceData, int lineNumber) {
		IniParseException ex = new IniParseException(MALFORMED_KEY_VALUE_LINE, sourceData, lineNumber);
		return ex;
	}
	
	public static IniParseException missingOrMalformedKey(String sourceData, int lineNumber) {
		IniParseException ex = new IniParseException(MISSING_OR_MALFORMED_KEY, sourceData, lineNumber);
		return ex;
	}
	 
	public static class ErrorMessage {
		public static final String UNEXPECTED_REGEX_STATE =
				"A regex reached a state that should not be possible, yet here it is.";
		public static final String NO_SECTION_HEADER =
				"A section header could not be found.";
		public static final String EXPECTED_KEY_VALUE_LINE =
				"A key-value line was expected, but it was not found.";
	
		
	}
	
	private static final String MALFORMED_KEY_VALUE_LINE =
			"A key-value line is not well formed.";
	
	private static final String MISSING_OR_MALFORMED_KEY =
			"A key-value line has a missing or malformed key.";
	
	private static final long serialVersionUID = 7544289917649885726L;

}







