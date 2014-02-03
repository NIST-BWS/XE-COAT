package gov.nist.ixe;

import static gov.nist.ixe.Logging.trace;

import java.io.File;

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
	

}
