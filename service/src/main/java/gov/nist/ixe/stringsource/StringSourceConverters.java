package gov.nist.ixe.stringsource;

import gov.nist.ixe.EncodingUtil;
import gov.nist.ixe.templates.Constants;

import javax.ws.rs.core.Response;

public class StringSourceConverters {

	public static Response toResponse(StringSource ss, String contentSubtype, String rel) {
		String contentType = EncodingUtil.charsetToTextContentType(
				contentSubtype, ss.getCharset());
		Response result = Response.ok(ss.getData()).type(contentType).header(Constants.HttpHeader.REL,  rel).build();
		return result;
	}

//	public static StringSource fromResponse2(Response response) {
//		byte[] data = response.readEntity(byte[].class);
//		String charset = (String) (response.getMetadata().get("Content-Type")
//				.get(0).toString());
//		return new StringSource(data, charset);
//	}

	public static StringSource fromResponse(Response response) {
		StringSource result = null;
		byte[] data = null;
		if (byte[].class.isInstance(response.getEntity())) {
			data = (byte[]) response.getEntity(); // Jersey 1.1		
		} else {
			data = response.readEntity(byte[].class);
		}	 
			String charset = (String) (response.getMetadata()
					.get("Content-Type").get(0).toString());
			result = new StringSource(data, charset);
		
		
		return result;
	}

}
