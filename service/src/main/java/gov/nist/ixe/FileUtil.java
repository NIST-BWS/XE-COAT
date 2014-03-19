package gov.nist.ixe;

import static gov.nist.ixe.Logging.trace;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {
	
	public static void clear(File start) {
		trace();
		if (start.isDirectory()) { 
			for (File f : start.listFiles()) {
				clear(f);
			}
		}
		start.delete();
	}
	
	
	public static String getRandomTempDirectoryName(String prefix) {
		trace();
		return System.getProperty("java.io.tmpdir") + File.separator + "IXE" + File.separator + prefix + Long.toString(System.nanoTime());
	}
	
	public static File getRandomTempDirectory(String prefix) {
		trace();		
		File tempDir = new File(getRandomTempDirectoryName(prefix));
		tempDir.mkdirs();
		return tempDir;
	}
	
	public static void copyDirectory(File source, File target)
	throws IOException {
		if (source.isDirectory()) {			
			if (!target.exists()) {
				target.mkdir();	
			}			
			for (String child : source.list()) {
				copyDirectory(new File(source, child), new File(target, child));
			}
		} else {			
			InputStream is = new FileInputStream(source);
			OutputStream os = new FileOutputStream(target);
			
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);				
			}
			is.close();
			os.close();
		}
	}
}
