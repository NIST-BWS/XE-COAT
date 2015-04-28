package gov.nist.ixe.templates;

import static gov.nist.ixe.Logging.trace;
import gov.nist.ixe.FileUtil;

// We use another layer of indirection so that the TemplateServices service
// (and their test classes) not need to have any direct information about
// what storage provider is being used.
//

public class StorageProviderFactory {
	private static String defaultFileStorageProviderLocation = null;
	private FileStorageProvider storage;
	
	public static void setDefaultFileStorageProviderLocation(String location) {
		trace();
		defaultFileStorageProviderLocation = location;
	}
	
	public static String getDefaultFileStorageProviderLocation() {
		if (defaultFileStorageProviderLocation == null) {
			defaultFileStorageProviderLocation = 
					FileUtil.getRandomTempDirectoryName("StorageProviderFactory");
		} 
		return defaultFileStorageProviderLocation;
	}
	
	public StorageProviderFactory() {
		trace();
		storage = new FileStorageProvider(getDefaultFileStorageProviderLocation());
	}
	
	public IStorageProvider getStorageProvider() {
		trace();
		return storage;
	}
	
	public void cleanStorage() {
		trace();
		storage.clear();
	}
	
	/*private static void printFiles(File start, String prepend) {	
		if (start.isDirectory()) {
			for (File f:start.listFiles()) {
				printFiles(f, prepend + prepend);
			}
		}
		System.out.println(prepend + start.getAbsolutePath());
	}*/
}
 