package gov.nist.ixe.stringsource;

import gov.nist.ixe.EncodingUtil;
import gov.nist.ixe.IOUtil;
import gov.nist.ixe.StringUtil;
import gov.nist.ixe.templates.UnicodeBOMInputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class StringSource {
	
//	public StringSource(String s) {
//		this.data = s.getBytes();
//		this.charset = Charset.defaultCharset().displayName();
//	}
//	
	public StringSource(byte[] data, String charsetOrContentType) {
		this.data = data;
		if (StringUtil.isBlank(charsetOrContentType) ||
			StringUtil.isBlank(EncodingUtil.contentTypeToCharset(charsetOrContentType))) {
			this.charset = EncodingUtil.detectCharset(data);
		} else {
			this.charset = EncodingUtil.getSupportedCharsetName(charsetOrContentType);
		}
	}
	

	
	private byte[] data;
	private String charset;
		
	
	public byte[] getData() { return data; }
	public String getCharset() { return charset; }
	

	public String getString() throws UnsupportedEncodingException { 
		return new String(data, charset);
	}
	
	public String getString(String charset) throws UnsupportedEncodingException { 
		return new String(data, charset);
	}
	
	public static StringSource infer(byte[] data) {
		StringSource result = new StringSource(data, EncodingUtil.detectCharset(data));
		return result;
	}
	
	public static StringSource infer(InputStream is) throws IOException {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length = 0;
		while ((length = is.read(buffer)) != -1) {
			baos.write(buffer, 0, length);
		}
		byte[] data = baos.toByteArray();
		String charset = EncodingUtil.detectCharset(data);
		return new StringSource(data, charset);
			
	}
	
	public String getContentType(String subType) {
		return EncodingUtil.charsetToTextContentType(subType, charset);
	}
	
	public List<String> getLines(String newline) throws UnsupportedEncodingException {
		List<String> result = new ArrayList<String>(); 	
		String[] source = getString().split(Pattern.quote(newline));
		for (String s : source) {
			result.add(s);
		}
		return result;		
	}
	
	public StringSource getCopyWithoutBOM() throws NullPointerException, IOException {
		return new StringSource(stripBOM(data), charset);
	}
	
	
	private static byte[] stripBOM(byte[] input) throws NullPointerException, IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(input);
		UnicodeBOMInputStream ubis = new UnicodeBOMInputStream(bais);
		ubis.skipBOM();
		return IOUtil.readAll(ubis);
	}

	
	
	
	
}
