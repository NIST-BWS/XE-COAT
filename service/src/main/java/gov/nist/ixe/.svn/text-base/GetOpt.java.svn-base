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

import java.io.PrintStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class GetOpt {
	private Map<Character, Parameter> shortMap;
	private Map<String, Parameter> longMap;
	private final String programName;
	
	public GetOpt(Parameter... parameters) {
		this(null, parameters);
	}
	
	public GetOpt(String programName, Parameter... parameters) {
		if (programName == null) {
			this.programName = "com.example.Application";
		} else {
			this.programName = programName;
		}
		
		shortMap = new TreeMap<Character, Parameter>();
		longMap = new TreeMap<String, Parameter>();
		
		for (Parameter p : parameters) {
			if (shortMap.containsKey(p.getShortKey())) {
				throw new IllegalArgumentException(String.format("Another parameter with the short key of '%s' already exists", p.getShortKey()));
			} else {
				shortMap.put(p.getShortKey(), p);
			}
			
			if (p.getLongKey() != null) {
				if (longMap.containsKey(p.getLongKey())) {
					throw new IllegalArgumentException(String.format("Another parameter with the long key of '%s' already exists", p.getLongKey()));
				} else {
					longMap.put(p.getLongKey(), p);
				}
			}
		}
	}
	
	public Map<Character, String> parse(String... args) {
		Map<Character, String> parameters = new TreeMap<Character, String>();
		
		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith("--")) {
				String arg = args[i].substring(2);
				
				if (arg.contains("=")) {
					String key = arg.split("=")[0];
					
					if (longMap.containsKey(key)) {
						String value = arg.substring(key.length() + 1);
						
						if (value.trim().length() == 0) {
							if (longMap.get(key).isExpectsValue()) {
								throw new IllegalArgumentException(String.format("Parameter '%s' expects a value", key));
							} else {
								throw new IllegalArgumentException(String.format("Parameter '%s' does not expect a value", key));
							}
						}

						parameters.put(longMap.get(key).getShortKey(), value);
					} else {
						throw new IllegalArgumentException(String.format("Invalid parameter '%s'", key));
					}
				} else {
					if (longMap.containsKey(arg)) {
						if (longMap.get(arg).isExpectsValue()) {
							throw new IllegalArgumentException(String.format("Parameter '%s' expects a value", arg));
						}
						
						parameters.put(longMap.get(arg).getShortKey(), "true");
					} else {
						throw new IllegalArgumentException(String.format("Invalid parameter '%s'", arg));
					}
				}
			} else if (args[i].startsWith("-")) {
				if (args[i].length() != 2) {
					throw new IllegalArgumentException(String.format("Invalid shorthand parameter '%s'", args[i]));
				}
				
				char key = args[i].charAt(1);
				
				if (shortMap.containsKey(key)) {
					if (shortMap.get(key).isExpectsValue()) {
						try {
							String value = args[++i];
							parameters.put(key, value);
						} catch (ArrayIndexOutOfBoundsException aioobe) {
							throw new IllegalArgumentException("Paramter key does not contain a value");
						}
					} else {
						parameters.put(key, "true");
					}
				} else {
					throw new IllegalArgumentException(String.format("Invalid parameter '%s'", key));
				}
			} else {
				throw new IllegalArgumentException(String.format("Invalid parameter '%s'", args[i]));
			}
		}
		
		return parameters;
	}
	
	public void printHelp() {
		printHelp(System.out);
	}
	
	public void printHelp(PrintStream out) {
		out.println("USAGE: ");
		out.printf("java %s [OPTIONS]%n", programName);
		
		for (Entry<Character, Parameter> entry : shortMap.entrySet()) {
			
			GetOpt.Parameter p = shortMap.get(entry.getKey());
			
			out.printf("\t-%s", entry.getKey());
			
			out.printf(p.isExpectsValue() ? " <value>" : "\t");
			
			if (p.getLongKey() == null) {
				out.print("\t\t\t");
			} else {
				out.printf("\t--%s", p.getLongKey());
				
				out.printf(p.isExpectsValue() ? "=<value>" : "\t");
			}
			
			out.printf("   \t%s", p.getDescription());
			
			out.println();
		}
	}
	
	public static class Parameter {
		private static final boolean defaultExpectsValue = true;
		private static final String supportedKeyCharacters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		private final char shortKey;
		private final String longKey;
		private final boolean expectsValue;
		private final String description;
		
		public Parameter(char shortKey, String longKey, boolean expectsValue, String description) {
			this.shortKey = shortKey;
			this.longKey = longKey;
			this.expectsValue = expectsValue;
			this.description = description;
			
			validateShortKey(shortKey);
			validateLongKey(longKey);
			validateDescription(description);
		}
		
		public Parameter(char shortKey, String longKey, String description) {
			this(shortKey, longKey, defaultExpectsValue, description);
		}
		
		public Parameter(char shortKey, String description) {
			this(shortKey, null, defaultExpectsValue, description);
		}
		
		public Parameter(char shortKey, boolean expectsValue, String description) {
			this(shortKey, null, expectsValue, description);
		}

		public char getShortKey() {
			return shortKey;
		}

		public String getLongKey() {
			return longKey;
		}

		public boolean isExpectsValue() {
			return expectsValue;
		}

		public String getDescription() {
			return description;
		}
				
		private void validateShortKey(char shortKey) {
			if (supportedKeyCharacters.indexOf(shortKey) == -1) {
				throw new IllegalArgumentException("Short key must only contain alphanumeric characters");
			}
		}
		
		private void validateLongKey(String longKey) {
			if (longKey == null)
				return;
			
			if (longKey.trim().length() <= 1) {
				throw new IllegalArgumentException("Long keys cannot contain whitespace and must be more than one character");
			}
			
			for (char c : longKey.toCharArray()) {
				if (supportedKeyCharacters.indexOf(c) == -1) {
					throw new IllegalArgumentException("Long keys can only contain alphanumeric characters");
				}
			}
			
		}
		
		private void validateDescription(String description) {
			if (description == null || description.trim().length() == 0) {
				throw new IllegalArgumentException("Description must not be null or blank");
			}
		}
	}
}