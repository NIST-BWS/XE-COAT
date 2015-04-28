package gov.nist.ixe.templates.tests;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import gov.nist.ixe.IOUtil;
import gov.nist.ixe.stringsource.StringSource;
import gov.nist.ixe.stringsource.StringSourcePersistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Test;

public class StringSourceTests {

	@Test
	public void StringSourceCanBeGeneratredFromAnEmptyFile() throws IOException, URISyntaxException {		
		StringSource ss = StringSourcePersistence.inferFromSystemResource("txt/empty.txt");
		assertArrayEquals(new byte[]{}, ss.getData());
		assertEquals("UTF-8", ss.getCharset());
	}
		
	
	@Test
	public void ANullCharsetForcesCharsetDetection() throws IOException, URISyntaxException {
		URL url = ClassLoader.getSystemResource("txt/ucs2be.ini");
		File f = new File(url.toURI());		
		byte[] data = IOUtil.readAll(new FileInputStream(f));
		StringSource ss = new StringSource(data, null);
		assertEquals("UTF-16BE", ss.getCharset());
	}
	
	
	@Test (expected=FileNotFoundException.class)
	public void InferingAStringSourceFromAFileThatDoesNotExistThrowsException() throws IOException {
		StringSourcePersistence.infer(new File("this_file_does_not_exist.txt"));
	}
}