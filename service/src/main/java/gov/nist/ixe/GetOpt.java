package gov.nist.ixe;

import java.io.PrintStream;
import java.util.Map;
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
		
		for (Character key : shortMap.keySet()) {
			GetOpt.Parameter p = shortMap.get(key);
			
			out.printf("\t-%s", key);
			
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