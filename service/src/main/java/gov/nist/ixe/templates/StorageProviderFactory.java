package gov.nist.ixe.templates;

import static gov.nist.ixe.Logging.trace;
import gov.nist.ixe.FileUtil;

import java.io.File;

// We use another layer of indirection so that the TemplateServices service
// (and their test classes) not need to have any direct information about
// what storage provider is being used.
//

public class StorageProviderFactory {
	private static String _defaultFileStorageProviderLocation = null;
	private FileStorageProvider _storage;
	
	public static void setDefaultFileStorageProviderLocation(String location) {
		trace();
		_defaultFileStorageProviderLocation = location;
	}
	
	public static String getDefaultFileStorageProviderLocation() {
		if (_defaultFileStorageProviderLocation == null) {
			_defaultFileStorageProviderLocation = 
					FileUtil.getRandomTempDirectoryName("StorageProviderFactory");
		} 
		return _defaultFileStorageProviderLocation;
	}
	
	public StorageProviderFactory() {
		trace();
		_storage = new FileStorageProvider(getDefaultFileStorageProviderLocation());
	}
	
	public IStorageProvider getStorageProvider() {
		trace();
		return _storage;
	}
	
	public void CleanStorage() {
		trace();
		_storage.clear();
	}
	
	private static void printFiles(File start, String prepend) {	
		if (start.isDirectory()) {
			for (File f:start.listFiles()) {
				printFiles(f, prepend + prepend);
			}
		}
		System.out.println(prepend + start.getAbsolutePath());
	}
}
 