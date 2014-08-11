package gov.nist.ixe.templates.ini.tests;

import static org.junit.Assert.assertEquals;
import gov.nist.ixe.EncodingUtil;
import gov.nist.ixe.StringUtil;
import gov.nist.ixe.stringsource.StringSource;
import gov.nist.ixe.stringsource.StringSourcePersistence;
import gov.nist.ixe.templates.ini.IniSection;
import gov.nist.ixe.templates.ini.Values;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.Test;

public class IniSectionTests {
	
	

	@Test
	public void SimpleIniFileParsesCorrectly() throws IOException, URISyntaxException {
		StringSource ini = StringSourcePersistence.inferFromSystemResource("txt/dummy.ini");
		String newline = EncodingUtil.detectNewline(ini.getString());
		List<IniSection> sections = IniSection.parse(ini.getLines(newline), true);
		
		String keys[] = null;
		IniSection section = null;
				
		assertEquals(3, sections.size());		
		
		section = sections.get(0);
		assertEquals("planes", section.getName());
		keys = section.getKeyNames().toArray(new String[0]);
		assertEquals(3, keys.length);
		assertEquals("airport", keys[0]);
		assertEquals("flight", keys[1]);
		assertEquals("status", keys[2]);
		assertEquals("lax", section.getValues("airport").get(0));
		assertEquals(StringUtil.quote("Oceanic 815"), section.getValues("flight").get(0));
		assertEquals("LOST", section.getValues("status").get(0));

			
		section = sections.get(1);
		assertEquals("trains", section.getName());
		keys = section.getKeyNames().toArray(new String[0]);
		assertEquals(2, keys.length);
		assertEquals("name", keys[0]);
		assertEquals("numbers", keys[1]);
		assertEquals(StringUtil.quote("thomas"), section.getValues("name").get(0));
		Values values = section.getValues("numbers");
		assertEquals(2, values.get(0));
		assertEquals(4, values.get(1));
		assertEquals(6, values.get(2));
		assertEquals(8, values.get(3));
		
		section = sections.get(2);
		assertEquals("automobiles", section.getName());
		keys = section.getKeyNames().toArray(new String[0]);
		assertEquals(4, keys.length);
		assertEquals("costs", keys[0]);
		assertEquals("mixed", keys[1]);
		assertEquals(StringUtil.quote("chitty chitty bang bang"), section.getValues("name").get(0));
		assertEquals("c:\\tmp\\oooahh.exe", section.getValues("runCommand").get(0));
						
	}
	
	
	
	
	@Test
	public void SQLConfigForSccmIniParsesCorrectly() throws IOException, URISyntaxException {

		StringSource ini = StringSourcePersistence.inferFromSystemResource("txt/SQLConfigForSccm.ini");
		String newline = EncodingUtil.detectNewline(ini.getString());
		List<String> iniLines = ini.getLines(newline);
	

		List<IniSection> sections = IniSection.parse(iniLines, true);
		
		IniSection section = sections.get(0);
								
		assertEquals(1, sections.size());	
		assertEquals("SQLSERVER2008", section.getName());
		assertEquals("\"XXXXX-XXXXX-XXXXX-XXXXX-XXXXX\"", section.getValues("PID").get(0));
		assertEquals("\"True\"", section.getValues("IACCEPTSQLSERVERLICENSETERMS").get(0));
		
		
		assertEquals("\"FilesOnlyMode\"", section.getValues("RSINSTALLMODE").get(0));
			
	}
	
	@Test
	public void IniParsesCorrectlyWhenFirstLineOfUnicodeFileIsHeader() throws IOException, URISyntaxException {
				
		StringSource ini = StringSourcePersistence.inferFromSystemResource("txt/ucs2be-first-line-is-header.ini");
		String newline = EncodingUtil.detectNewline(ini.getString());
		List<String> iniLines = ini.getLines(newline);
		
		List<IniSection> sections = IniSection.parse(iniLines, true);
		
		IniSection section = sections.	get(0);
								
		assertEquals(1, sections.size());	
		assertEquals("UnicodeAndFirstLineIsAHeader", section.getName());

	}

}
