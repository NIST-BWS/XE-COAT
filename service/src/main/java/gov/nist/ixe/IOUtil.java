package gov.nist.ixe;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IOUtil {
	
	public static byte[] readAll(InputStream is) throws IOException {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int read = 0;
		while ((read = is.read(buffer)) > 0) {
			baos.write(buffer, 0, read);
		}
		baos.close();
		is.close();
		
		return baos.toByteArray();
	}

	
}

