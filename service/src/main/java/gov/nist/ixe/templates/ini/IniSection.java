package gov.nist.ixe.templates.ini;

import static gov.nist.ixe.Logging.trace;

import gov.nist.ixe.templates.ini.exception.IniParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class IniSection {
	private String name = null;
	private Map<String, Values> valueMap = new TreeMap<String, Values>();
	private Map<String, KeyValueLine> sourceMap = new TreeMap<String, KeyValueLine>();
	
	private int startingLine; 
	private int endingLine;
	
	public static List<IniSection> parse(List<String> sourceData, boolean strict) {
		trace();
		
		ArrayList<IniSection> sections = new ArrayList<IniSection>();
		boolean done = false;
		int startingLine = 0;
		
		do {
			IniSection section = IniSection.parse(sourceData, startingLine, strict);
			
			if (section != null) {
				sections.add(section);
				startingLine = section.getEndingLine();
				//System.out.println(section.startingLine+1 + " " + (section.endingLine+1));
			} else  {
				done = true;
			}
			
		} while (!done);
		
		return sections;
	}
	
	public static IniSection parse(List<String> sourceData, int startingLine, boolean strict) {
		trace();
		
		IniSection result = new IniSection();
		
		if (startingLine >= sourceData.size())
			return null;
		
		result.startingLine = startingLine;
		result.endingLine = startingLine;

		// The section name (header) being null also indicates that we have not yet seen
		// a substantive line.
		//
		result.name = null;
		
		for (int i=startingLine; i < sourceData.size(); i++) {
			String line = sourceData.get(i);
			result.endingLine = i+1;
						
			// Ignore non-substantive lines
			if (line == null ||	line.trim().equals("") || !Parse.isSubstantiveLine(line))
				continue;
			
			String nameCandidate = Parse.getHeader(line);
			
			if (result.name == null) {		
				// The first substantive line should be a header.
				if (nameCandidate == null) {			 
					throw new IniParseException(IniParseException.ErrorMessage.NO_SECTION_HEADER, line, i);
				} else {
					result.name  = nameCandidate;
					continue;
				}
			} else if (nameCandidate != null) {
				// If we have another header line, then this section is done. Backup the ending line
				result.endingLine = i; 
				break;
			}
			
			KeyValueLine keyValueLineCandidate = KeyValueLine.parse(line, i);
			
			if (keyValueLineCandidate == null) {
				if (strict) {
					throw new IniParseException(IniParseException.ErrorMessage.EXPECTED_KEY_VALUE_LINE, line, i);	
				} else {
					continue;
				}
			} 
			
			String key = keyValueLineCandidate.getKey();
			result.sourceMap.put(key, keyValueLineCandidate);
			
			Values values = Values.parse(keyValueLineCandidate.getValues(), i);
			result.valueMap.put(key, values);		
		}
		
		return result;
	}
	
	public Map<String, Values> getSingleValueKeys() {
		trace();
		
		TreeMap<String, Values> result = new TreeMap<String,Values>();
		
		for (Entry<String, Values> entry : result.entrySet()) {
			if (entry.getValue().size() == 1) {
				result.put(entry.getKey(), entry.getValue());
			}
		}
		
		return result;
	}
	
	public Map<String, Values> getMultiValueKeys() {
		trace();
		
		TreeMap<String, Values> result = new TreeMap<String,Values>();
		
		for (Entry<String, Values> entry : valueMap.entrySet()) {
			if (entry.getValue().size() > 1) {
				result.put(entry.getKey(), entry.getValue());
			}
		}
		
		return result;
	}

	public int getStartingLine() { 
		return startingLine;
	}
	
	public int getEndingLine() {
		trace();
		return endingLine; 
	}
	
	public String getName() {
		trace();
		return name; 
	}
	
	public Set<String> getKeyNames() {
		trace();
		return valueMap.keySet(); 
	}
	
	public Map<String, Values> getKeyValuesMap() {
		trace();
		return valueMap; 
	}
	
	public Values getValues(String key) {
		trace();
		return valueMap.get(key); 
	}
	
	public KeyValueLine getKeyValuesLine(String key) {
		trace();
		return sourceMap.get(key); 
	}
}
