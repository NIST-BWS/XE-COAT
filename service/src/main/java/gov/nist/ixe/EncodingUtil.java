package gov.nist.ixe;

import static gov.nist.ixe.Logging.info;
import static gov.nist.ixe.Logging.trace;
import gov.nist.ixe.templates.exception.IllegalContentTypeException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.core.MediaType;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

public class EncodingUtil {
			
	public static String detectCharset(InputStream is) throws IOException {
		trace();
		
		CharsetDetector detector = new CharsetDetector();
		BufferedInputStream bis = new BufferedInputStream(is);
		detector.setText(bis);
		CharsetMatch match = detector.detect();
		return match.getName();
		
	}
	
	public static String detectCharset(byte[] data) {
		CharsetDetector detector = new CharsetDetector();
		detector.setText(data);
		return detector.detect().getName();
	}

	public static String detectNewline(InputStream is, String charset) throws IOException {
		return new String(IOUtil.readAll(is), charset);		
	}
	
	public static String detectNewline(String s) {
		String result = "\n";
		int winNewLineCount = 0;
		Pattern regex = Pattern.compile("\\r\\n", Pattern.DOTALL);
		Matcher regexMatcher = regex.matcher(s);
		
		while (regexMatcher.find()) {
			winNewLineCount++;
		}
		
		if (winNewLineCount > 1)
			result = "\r\n";
		
		return result;
	}
	
	public static String detectNewline(File f, String charset) throws IOException {
		trace();
		
		FileInputStream fis = new FileInputStream(f);
		String result = detectNewline(fis, charset);
		fis.close();
		
		return result;
	}
	
	
	public static String detectCharset(File f) throws IOException {
		trace();
		
		FileInputStream fis = new FileInputStream(f); 
		String result = detectCharset(fis);
		if (Charset.isSupported(result)) {
			info(String.format("File '%s' appears to be encoded with charset '%s'.", f.getAbsolutePath(), result));
		} else {
			throw new IllegalContentTypeException(result);
		}
		fis.close();
		
		return result;
	}
	
	public static String getSupportedCharsetName(String contentTypeOrCharset) {
		String result = null;
		
		if (charsetIsSupported(contentTypeOrCharset)) {
			result = contentTypeOrCharset;
		}
		
		String candidate = contentTypeToCharset(contentTypeOrCharset);
		if (candidate != null && charsetIsSupported(candidate)) {
			result = candidate;
		}
		
		if (result == null) {
			throw new IllegalContentTypeException(contentTypeOrCharset);
		}
					
		return result;
	}
	
	
	private static boolean charsetIsSupported(String charset) {
		boolean result;

		if (charset == null) {
			result = false;
		} else {
			try {
				result = Charset.isSupported(charset);
			} catch (IllegalCharsetNameException ice) {
				result = false;
			}
		}
		return result;
	}
	
	public static void assertContentTypeIsValid(String contentType) {
		try {
			MediaType mt = MediaType.valueOf(contentType);
		} catch (IllegalArgumentException ex) {
			throw new IllegalContentTypeException(contentType);
		} 
	}

	public static String contentTypeToCharset(String contentType) {
		String result = null;
		try {
		MediaType mt = MediaType.valueOf(contentType);
		return mt.getParameters().get("charset"); 
		} catch (IllegalArgumentException ex) {		
		}
		return result;
	}
	
	public static String charsetToTextContentType(String subType, final String charset) {
		
		@SuppressWarnings("serial")	Map<String,String> params =
				new HashMap<String, String>(){{	put("charset",charset);	}};    
		return (new MediaType("text", subType, params)).toString();
	}
	
	public static String defaultPlainTextContentType() {
		return charsetToTextContentType("plain", Charset.defaultCharset().displayName());
	}
	
	public static String defaultXmlContentType() {
		return charsetToTextContentType("xml", Charset.defaultCharset().displayName());
	}
	
}

