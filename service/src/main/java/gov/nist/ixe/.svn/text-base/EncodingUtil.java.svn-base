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
		String result = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(f);
			result = detectNewline(fis, charset);
		} finally {
			if (fis != null) fis.close();
		}
		
		
		
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
			MediaType.valueOf(contentType);
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
		return (new MediaType("text", subType, params)).toString().replace(" ", "");
	}
	
	public static String defaultPlainTextContentType() {
		return charsetToTextContentType("plain", Charset.defaultCharset().displayName());
	}
	
	public static String defaultXmlContentType() {
		return charsetToTextContentType("xml", Charset.defaultCharset().displayName());
	}
	
}

