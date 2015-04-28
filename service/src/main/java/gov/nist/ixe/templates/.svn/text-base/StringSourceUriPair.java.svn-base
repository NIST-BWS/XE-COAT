package gov.nist.ixe.templates;


import java.io.ByteArrayInputStream;

import org.xml.sax.InputSource;

import gov.nist.ixe.stringsource.StringSource;

public class StringSourceUriPair {
	private StringSource ss;
	private String uri;
	public StringSourceUriPair(StringSource ss, String uri) {
		this.ss = ss;
		this.uri = uri;
	}


	public InputSource getInputSource() {
		InputSource is = new InputSource(new ByteArrayInputStream(ss.getData()));
		is.setEncoding(ss.getCharset());
		is.setSystemId(uri);
		return is;
	}
	
	public static InputSource inputSource(StringSource ss, String uri) {
		StringSourceUriPair ssup = new StringSourceUriPair(ss, uri);
		return ssup.getInputSource();
	}

}
