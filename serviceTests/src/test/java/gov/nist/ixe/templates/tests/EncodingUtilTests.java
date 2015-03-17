package gov.nist.ixe.templates.tests;

import static org.junit.Assert.assertEquals;
import gov.nist.ixe.EncodingUtil;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;

public class EncodingUtilTests {

	
	@Test
	public void UTF8EncodingIsDetectedCorrectly() throws IOException, URISyntaxException {
		//ClassLoader.getSystemResource("").toURI()
		assertEquals("UTF-8", EncodingUtil.detectCharset(new File(ClassLoader.getSystemResource("txt/utf8.ini").toURI())));
	}
			
	@Test
	public void UTF16EEncodingIsDetectedCorrectly() throws IOException, URISyntaxException {
		assertEquals("UTF-16LE", EncodingUtil.detectCharset(new File(ClassLoader.getSystemResource("txt/ucs2le.ini").toURI())));
	}
	
	@Test
	public void UTF16BEEncodingIsDetectedCorrectly() throws IOException, URISyntaxException {
		assertEquals("UTF-16BE", EncodingUtil.detectCharset(new File(ClassLoader.getSystemResource("txt/ucs2be.ini").toURI())));
	}
	
	@Test
	public void Windows1252EncodingIsDetectedCorrectly() throws IOException, URISyntaxException {
		assertEquals("windows-1252", EncodingUtil.detectCharset(new File(ClassLoader.getSystemResource("txt/ansi.ini").toURI())));	
	}
	
//	@Test (expected=IllegalContentTypeException.class)
//	public void DetectCharsetCanThrowIllegalContentTypeException() throws IOException {
//		assertEquals(null, EncodingUtil.detectCharset(new File("txt/random.bin")));
//	}
//	
	@Test
	public void charsetToTextContentTypeBuildsContentTypeCorrectly() {
		assertEquals("text/xml;charset=abc", EncodingUtil.charsetToTextContentType("xml", "abc"));
		assertEquals("text/XML;charset=def", EncodingUtil.charsetToTextContentType("XML", "def"));
	
	}
	
	@Test
	public void contentTypeToCharsetExtractsContentType() {
		assertEquals("abc", EncodingUtil.contentTypeToCharset("foo/bar; charset=abc"));
		assertEquals("xYZ", EncodingUtil.contentTypeToCharset("foo/bar; CHARSET=xYZ"));	
	}
	

	

}
