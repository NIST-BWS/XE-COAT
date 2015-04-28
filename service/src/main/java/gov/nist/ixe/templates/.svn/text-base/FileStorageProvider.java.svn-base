/*-----------------------------------------------------------------------------------------------------+
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

package gov.nist.ixe.templates;
import static gov.nist.ixe.Logging.trace;
import static gov.nist.ixe.stringsource.StringSourcePersistence.getFakeFile;
import static gov.nist.ixe.stringsource.StringSourcePersistence.getRealFile;
import static gov.nist.ixe.stringsource.StringSourcePersistence.readFrom;
import static gov.nist.ixe.stringsource.StringSourcePersistence.save;
import static gov.nist.ixe.stringsource.StringSourcePersistence.tryGettingRealFile;
import gov.nist.ixe.FileUtil;
import gov.nist.ixe.Logging;
import gov.nist.ixe.stringsource.StringSource;
import gov.nist.ixe.templates.exception.IllegalResourceNameException;
import gov.nist.ixe.templates.exception.ResourceNotEmptyException;
import gov.nist.ixe.templates.exception.ResourceNotFoundException;
import gov.nist.ixe.templates.exception.StorageProviderException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileStorageProvider implements IStorageProvider {

	private static final String TEMPLATE_FILENAME="template.vm";
	//private static final String tombstonePattern = "\\.\\d{15}+\\..*";
	private static Pattern tombstonePattern = 
			Pattern.compile("\\.\\d{20}\\.\\b[A-F0-9]{32}\\.(.*)", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

	private String _location;
	
	public FileStorageProvider() {
		this(FileUtil.getRandomTempDirectoryName("FileStorageProvider"));
	}

	public FileStorageProvider(String location) {
		trace();

		File root = new File(location);
		Logging.info(location);
		if (!root.exists()) { FileUtil.mkdirs(root); }
		_location = root.getAbsolutePath();
	}

	public String getLocation() {
		trace();
		return _location;
	}



	public void clear() {
		trace();
		clear(getLocation());

	}

	public static void clear(String path) {
		trace();		
		FileUtil.clear(new File(path));
	}




	public File getServiceFile(String serviceName) {
		trace();

		return new File(new File(getLocation()), serviceName);
	}

	private File getServiceSubDir(String serviceName, String subDirName) {
		trace();

		File service = getServiceFile(serviceName);
		if (!service.exists()) {
			throw ResourceNotFoundException.createFromNameOfMissingResource(serviceName);
		}

		File subDir = new File(getServiceFile(serviceName), subDirName);

		if (!subDir.exists()) {
			//
			// We throw a StorageProviderException here (as opposed to
			// a ResourceNotFoundException, since a service that lacks the 
			// appropriate sub-directories is indicative of a corrupt data
			// store.
			// 
			throw new StorageProviderException(
					StorageProviderException.ErrorMessage.SUBDIRECTORY_NOT_FOUND);
		}

		if (!subDir.isDirectory()) {
			throw new StorageProviderException(
					StorageProviderException.ErrorMessage.DIRECTORY_EXPECTED);

		}

		return subDir;
	}

	public File getTemplateDir(String serviceName) {
		trace();
		return getServiceSubDir(serviceName, TEMPLATE_SUBDIR_NAME);
	}

	public File getSchemaDir(String serviceName) {
		trace();
		return getServiceSubDir(serviceName, SCHEMAS_SUBDIR_NAME);
	}

	public File getConfigDir(String serviceName) {
		trace();
		return getServiceSubDir(serviceName, CONFIGS_SUBDIR_NAME);
	}

	public String getTombstonePath(String originalPath) {
		trace();
		return getTombstonePath(originalPath, new Date(), UUID.randomUUID());
	}

	public String getTombstonePath(File source) {
		trace();
		return getTombstonePath(source.getAbsolutePath(), new Date(), UUID.randomUUID());
	}

	
	public String getTombstonePath(String originalPath, Date date, UUID uuid) {
		trace();

		String tombstoneName = String.format(".%020d.%s.%s", 
				date.getTime(), 
				uuid.toString().replace("-", ""),
				new File(originalPath).getName());
		String parentPath = new File(originalPath).getParent();

		if (parentPath != null) {
			tombstoneName = new File(new File(parentPath), tombstoneName).getPath();
		}

		return tombstoneName;
	}

	private File[] getResourceFiles(File start) {
		trace();

		ArrayList<File> resources = new ArrayList<File>();
		
		File[] files = start.listFiles();
		if (files != null) {
			for (File f : files) {
				if (f.isFile() && !isTombstonePath(f.getAbsolutePath())) {
					resources.add(f);
				}
			}
		}

		return resources.toArray(new File[resources.size()]);
	}




	private File[] getResourceTombstoneFiles(String serviceName, String subDirName, String resourceName) {
		ArrayList<File> resources = new ArrayList<File>();
		File resourceDir = this.getServiceSubDir(serviceName, subDirName);
		File[] files = resourceDir.listFiles();
		if (files != null) {
			for (File f : files) {

				if (isTombstonePath(f.getAbsolutePath())) {	
					String extractedName = this.extractResourceName(f.getAbsolutePath());
					if (extractedName != null && extractedName.equals(resourceName)) {
						resources.add(getFakeFile(f));
					}
				}

			}
		}
		return resources.toArray(new File[resources.size()]);
	}


	private File uncheckedGetResourceFile(String serviceName, String subDirName, String resourceName) {
		trace();

		return new File(getServiceSubDir(serviceName, subDirName), resourceName);
	}



//	private File getResourceFile(String serviceName, String subDirName, String resourceName) {
//		trace();
//
//		File resource = uncheckedGetResourceFile(serviceName, subDirName, resourceName);
//
//		if (!resource.exists()) {
//			throw ResourceNotFoundException.CreateFromNameOfMissingResource(resourceName);
//		}
//		if (!resource.isFile()) {
//			throw new StorageProviderFailureException(
//					StorageProviderFailureException.ErrorMessage.FILE_EXPECTED);
//		}
//		return resource;
//	}

	public boolean isTombstonePath(String path) {
		trace();
		String fileName = new File(path).getName();
		return tombstonePattern.matcher(fileName).matches();
	}
	
	public String extractResourceName(String tombstonePath) {
		trace();
		String result = null;
		String fileName = new File(tombstonePath).getName();
		Matcher matcher = tombstonePattern.matcher(fileName);
		if (matcher.find() && matcher.groupCount() == 1) {
			result = matcher.group(1);
			// Strip off the encoding suffix
			result = result.substring(0, result.lastIndexOf('.'));
		}
		return result;
	}

	@Override
	public String[] getServiceNames() {
		trace();

		ArrayList<String> services = new ArrayList<String>();
		
		File[] files = new File(getLocation()).listFiles();
		if (files != null) {
			for (File f : files ) {
				if (f.isDirectory() && !isTombstonePath(f.getAbsolutePath())) {
					services.add(f.getName());
				}	
			}
		}

		Collections.sort(services);
		return services.toArray(new String[services.size()]);
	}
	
	public boolean hasService(String serviceName) {
		boolean result = false;
		for (String name : getServiceNames()) {
			if (name.equals(serviceName)) {
				result = true;
				break;
			}		
		}
		return result;
	}

	@Override
	public void addService(String serviceName) {
		trace();

		forbidTombstoneName(serviceName);
		forbidReservedName(serviceName);
		createServiceDirIfNeeded(serviceName);
		createServiceSubDirIfNeeded(serviceName, SCHEMAS_SUBDIR_NAME);
		createServiceSubDirIfNeeded(serviceName, CONFIGS_SUBDIR_NAME);
		createServiceSubDirIfNeeded(serviceName, TEMPLATE_SUBDIR_NAME);
	}
	
	@Override
	public void renameService(String serviceName, String newName) {
		// Don't bother doing anything if the name hasn't actually changed
		if (serviceName.equals(newName)) return;
		
		File serviceFile = getServiceFile(serviceName);
		File newServiceFile = getServiceFile(newName);
		if (newServiceFile.exists()) {
			throw IllegalResourceNameException.nameAlreadyExists(newName);
		}
		FileUtil.renameTo(serviceFile, newServiceFile);		
	}
			

	private void forbidTombstoneName(String name) throws IllegalResourceNameException {
		trace();

		if (isTombstonePath(name)) {
			throw IllegalResourceNameException.reservedName(name);
		}
	}
	
	private void forbidReservedName(String name) throws IllegalResourceNameException {
		
		if (Arrays.asList(Constants.RESERVED_NAMES).contains(name)) {
			throw IllegalResourceNameException.reservedName(name);
		}		
	}

	private void createServiceDirIfNeeded(String serviceName) {
		trace();
		File service = getServiceFile(serviceName);

		if (service.exists()) {
			if (!service.isDirectory()) {
				throw new StorageProviderException(StorageProviderException.ErrorMessage.DIRECTORY_EXPECTED);
			}
		} else {
			FileUtil.mkdirs(service);
			//service.mkdirs();	 		
		}	
	}

	private static final String SCHEMAS_SUBDIR_NAME = "schemas";
	private static final String CONFIGS_SUBDIR_NAME = "configurations";
	private static final String TEMPLATE_SUBDIR_NAME = "template";
	private void createServiceSubDirIfNeeded(String serviceName, String subdirName) {
		trace();
		File subDir = new File(getServiceFile(serviceName), subdirName);

		if (subDir.exists()) {
			if (!subDir.isDirectory()) {
				throw new StorageProviderException(StorageProviderException.ErrorMessage.DIRECTORY_EXPECTED);
			}
		} else {
			FileUtil.mkdir(subDir);
		}
	}



	private boolean subDirHasResources(String serviceName, String subDirName) {
		trace();
		return getResourceNames(serviceName, subDirName).length > 0;
	}

	private boolean serviceHasResources(String serviceName) {
		trace();
		return subDirHasResources(serviceName, SCHEMAS_SUBDIR_NAME) || subDirHasResources(serviceName, CONFIGS_SUBDIR_NAME) || subDirHasResources(serviceName, TEMPLATE_SUBDIR_NAME);
	}
	
	public boolean isServiceDeletable(String serviceName) {
		trace();
		return !serviceHasResources(serviceName);
	}

	@Override
	public void deleteService(String serviceName) throws ResourceNotEmptyException {
		trace();

		if (serviceHasResources(serviceName)) {
			throw new ResourceNotEmptyException();
		} else {
			File service = getServiceFile(serviceName);
			File tombstone;
			tombstone = new File(getTombstonePath(service));//.getName()));
			FileUtil.renameTo(service, tombstone);
		}
	}

	public File getTemplateFile(String serviceName) {
		trace();

		File template = null;
		File[] templates = getResourceFiles(getServiceSubDir(serviceName, TEMPLATE_SUBDIR_NAME));
		if (templates.length == 1) {
			template = templates[0];
		} else if (templates.length > 1) {
			throw new StorageProviderException(
					StorageProviderException.ErrorMessage.BAD_RESOURCE_COUNT);
		}
		return template;
	}

	public boolean hasTemplate(String serviceName) {
		trace();
		return getTemplateFile(serviceName) != null;
	}

	private StringSource getResource(String serviceName, String subDirName, String resourceName) {
		trace();

		File resourceFile = uncheckedGetResourceFile(serviceName, subDirName, resourceName);

		StringSource result;

		try {
			result = readFrom(resourceFile);
		} catch (FileNotFoundException ex) {
			throw ResourceNotFoundException.createFromNameOfMissingResource(resourceName);
		} catch (IOException ex) {
			throw new StorageProviderException(StorageProviderException.ErrorMessage.IO_ERROR);
		}

		return result;
	}


	//private static Object setResourcesLock = new Object();
	private void setResource(String serviceName, String subDirName, String resourceName, StringSource resourceContents) {
		
		try {
			// Sleep for one millisecond to prevent resources from being tombstoned to the same timestamp.
			Thread.sleep(1);
		} catch (InterruptedException ie) {
			// If this is interrupted, do nothing.
		}
		
		// First, see if there is a real file exists under a different encoding.
		File resourceFile = uncheckedGetResourceFile(serviceName, subDirName, resourceName);
		File realFile = tryGettingRealFile(resourceFile);
		
		// If that didn't work, see if there is one for the intended encoding.
		if (realFile == null) {
			realFile = getRealFile(resourceContents, resourceFile);
		}
		
		
		if (realFile.exists()) {
			File tombstone = new File(getTombstonePath(realFile));
			FileUtil.renameTo(realFile, tombstone);			
		}

		try {
			save(resourceContents, resourceFile);
		} catch (IOException e) {
			throw new StorageProviderException(StorageProviderException.ErrorMessage.IO_ERROR);
		}

	}

	private String[] getResourceNames(String serviceName, String subDirName) {
		trace();

		ArrayList<String> names = new ArrayList<String>();
		File[] resourceFiles = getResourceFiles(getServiceSubDir(serviceName, subDirName));

		for (File f : resourceFiles) {
			if (f.isFile() && !isTombstonePath(f.getAbsolutePath())) {
				names.add(getFakeFile(f).getName()); 
			}
		}

		Collections.sort(names);
		return names.toArray(new String[names.size()]);
	}



	private void deleteResource(String serviceName, String subDirName, String resourceName) {

		File service = getServiceFile(serviceName);

		if (!service.exists())
			throw ResourceNotFoundException.createFromNameOfMissingResource(serviceName);

		File resource = uncheckedGetResourceFile(serviceName, subDirName, resourceName);
		File realFile = null;
		try {
			realFile = getRealFile(resource);
		} catch (FileNotFoundException e) {
			// Do nothing if the file is already gone.
		}

		if (realFile != null) {
			File tombstone = new File(getTombstonePath(realFile.getAbsolutePath()));
			FileUtil.renameTo(realFile, tombstone);
		}

	}

	private HistoryInfo[] getResourceHistoryInfo(String serviceName, String subDirName, String resourceName) {
		ArrayList<HistoryInfo> historyInfo = new ArrayList<HistoryInfo>();
		for (File f : getResourceTombstoneFiles(serviceName, subDirName, resourceName)) {
			try {
				long ms = Long.parseLong(f.getName().substring(1, f.getName().indexOf('.', 1)));
				long sz = getRealFile(f).length();
				historyInfo.add(new HistoryInfo(ms, sz));
			} catch (NumberFormatException pe) {
				throw new StorageProviderException(pe);
			} catch (FileNotFoundException fnfe) {
				throw new StorageProviderException(fnfe);
			}

		}
		Comparator<HistoryInfo> comparator = new Comparator<HistoryInfo>() {
			public int compare(HistoryInfo hi1, HistoryInfo hi2) {
				return (int) (hi1.getTimestampInMs() - hi2.getTimestampInMs());
			}
		};
		Collections.sort(historyInfo, comparator);
		return historyInfo.toArray(new HistoryInfo[historyInfo.size()]);

	}
	
	
	
	private Date[] getResourceHistoryTimestamps(String serviceName, String subDirName, String resourceName) {
		ArrayList<Date> timestamps = new ArrayList<Date>();
		for (File f : getResourceTombstoneFiles(serviceName, subDirName, resourceName)) {
			try {
				long ms = Long.parseLong(f.getName().substring(1, f.getName().indexOf('.', 1)));
				timestamps.add(new Date(ms));
			} catch (NumberFormatException pe) {
				throw new StorageProviderException(pe);
			}
	
		}
		Collections.sort(timestamps);
		return timestamps.toArray(new Date[timestamps.size()]);
	
	}

	private StringSource getHistoricResource(String serviceName, String subDirName, String resourceName, Date timestamp) {
		trace();
		
		StringSource result = null;
		File resourceFile = null;
		for (File f : getResourceTombstoneFiles(serviceName, subDirName, resourceName)) {
			if (f.getName().startsWith("." + String.format("%020d", timestamp.getTime()))) {
				resourceFile = f.getAbsoluteFile();
			}
		}
		
		
		try {
			if (resourceFile != null) {
				result = readFrom(resourceFile);
			}
		} catch (FileNotFoundException ex) {
			throw ResourceNotFoundException.createFromNameOfMissingResource(resourceName);
		} catch (IOException ex) {
			throw new StorageProviderException(StorageProviderException.ErrorMessage.IO_ERROR);
		}

		return result;
	}
	
	private void setHistoricResource(StringSource resource, String serviceName, String subDirName, String resourceName, Date date) 
			throws IOException {
		String resourcePath = uncheckedGetResourceFile(serviceName, subDirName, resourceName).getAbsolutePath();
		String historicPath = getTombstonePath(resourcePath, date, UUID.randomUUID()); 
		File historicFile = new File(historicPath);
		save(resource, historicFile);			
	}
	
	private void renameResource(String serviceName, String subDirName, String oldResourceName, String newResourceName) {
		
		try {
			
			// Don't bother doing anything if the name isn't actually changed
			if (oldResourceName.equals(newResourceName)) return;
			
			// Make sure the new resource does not already exist
			File newResourceFile = uncheckedGetResourceFile(serviceName, subDirName, newResourceName);
			File realNewResourceFile = tryGettingRealFile(newResourceFile);
			if (realNewResourceFile != null && realNewResourceFile.exists()) {
				throw IllegalResourceNameException.nameAlreadyExists(newResourceName);
			}
			
			
			
			// Copy the resource history
			Date[] dates = this.getResourceHistoryTimestamps(serviceName, subDirName, oldResourceName);
			for (Date d : dates) {
				StringSource resource = getHistoricResource(serviceName, subDirName, oldResourceName, d);
				setHistoricResource(resource, serviceName, subDirName, newResourceName, d);
			}

			// Get the old resource and save it as a new one
			StringSource newResource = getResource(serviceName, subDirName, oldResourceName);
			
			save(newResource, newResourceFile);

			// Delete the old resource
			FileUtil.delete(getRealFile(uncheckedGetResourceFile(serviceName, subDirName, oldResourceName)));
	
			// Delete the old resource history
			for (File f : getResourceTombstoneFiles(serviceName, subDirName, oldResourceName)) {
				FileUtil.delete(getRealFile(f));
			}		

		} catch (IOException ex) {
			throw new StorageProviderException(ex);
		}
		
	}

	@Override
	public StringSource getTemplate(String serviceName) throws ResourceNotFoundException {
		trace();

		return getResource(serviceName, TEMPLATE_SUBDIR_NAME, TEMPLATE_FILENAME);
	}

	@Override
	public void setTemplate(String serviceName, StringSource template) {
		trace();

		setResource(serviceName, TEMPLATE_SUBDIR_NAME, TEMPLATE_FILENAME, template);
	}

	@Override
	public void deleteTemplate(String serviceName) {
		trace();

		deleteResource(serviceName, TEMPLATE_SUBDIR_NAME, TEMPLATE_FILENAME);
	}

	@Override
	public String[] getSchemaNames(String serviceName) {
		trace();

		return getResourceNames(serviceName, SCHEMAS_SUBDIR_NAME);
	}

	@Override
	public void addSchema(String serviceName, String schemaName, StringSource schema) {
		trace();

		forbidTombstoneName(schemaName);
		forbidReservedName(schemaName);
		setResource(serviceName, SCHEMAS_SUBDIR_NAME, schemaName, schema);
	}

	@Override
	public StringSource getSchema(String serviceName, String schemaName) {
		trace();

		return getResource(serviceName, SCHEMAS_SUBDIR_NAME, schemaName);
	}

	@Override
	public void deleteSchema(String serviceName, String schemaName) {
		trace();

		deleteResource(serviceName, SCHEMAS_SUBDIR_NAME, schemaName);
	}
	
	public void renameSchema(String serviceName, String oldSchemaName, String newSchemaName) {
		trace();
		renameResource(serviceName, SCHEMAS_SUBDIR_NAME, oldSchemaName, newSchemaName);
	}
	
	public void renameConfig(String serviceName, String oldConfigName, String newConfigName) {
		trace();
		renameResource(serviceName, CONFIGS_SUBDIR_NAME, oldConfigName, newConfigName);
	}


	@Override
	public String[] getConfigNames(String serviceName) {
		trace();

		return getResourceNames(serviceName, CONFIGS_SUBDIR_NAME);
	}

	@Override
	public void addConfig(String serviceName, String configurationName, StringSource configuration) {
		trace();

		forbidTombstoneName(configurationName);
		forbidReservedName(configurationName);
		setResource(serviceName, CONFIGS_SUBDIR_NAME, configurationName, configuration);
	}

	@Override
	public StringSource getConfig(String serviceName, String configurationName) {
		trace();
		return getResource(serviceName, CONFIGS_SUBDIR_NAME, configurationName);	
	}

	@Override
	public void deleteConfig(String serviceName, String configurationName) {
		trace();
		deleteResource(serviceName, CONFIGS_SUBDIR_NAME, configurationName);		
	}

//	public String getTemplateCharset(String serviceName) {
//		String result = null; 
//		try {
//			result = EncodingUtil.detectCharset(this.getResourceFile(serviceName, TEMPLATE_SUBDIR_NAME, TEMPLATE_FILENAME));
//		} catch (IOException e) {
//			result = null;
//		}
//		return result;
//	}

	@Override
	public StringSource getHistoricSchema(String serviceName,
			String schemaName, Date timestamp) {
		return this.getHistoricResource(serviceName, SCHEMAS_SUBDIR_NAME, schemaName, timestamp);
	}

	/*@Override
	public Date[] getSchemaHistoryTimestamps(String serviceName,
			String schemaName) {
		return this.getResourceHistoryTimestamps(serviceName, SCHEMAS_SUBDIR_NAME, schemaName);
	}*/
	
	@Override
	public HistoryInfo[] getSchemaHistoryInfo(String serviceName, String schemaName) {
		return this.getResourceHistoryInfo(serviceName, SCHEMAS_SUBDIR_NAME, schemaName);
	}
		
	@Override
	public StringSource getHistoricConfig(String serviceName, String configName, Date timestamp) {
		return this.getHistoricResource(serviceName, CONFIGS_SUBDIR_NAME, configName, timestamp);
	}

	@Override
	public HistoryInfo[] getConfigHistoryInfo(String serviceName, String configName) {
		return this.getResourceHistoryInfo(serviceName, CONFIGS_SUBDIR_NAME, configName);
	}

	@Override
	public StringSource getHistoricTemplate(String serviceName, Date timestamp) {
		return this.getHistoricResource(serviceName, TEMPLATE_SUBDIR_NAME, TEMPLATE_FILENAME, timestamp);
	}

	@Override
	public HistoryInfo[] getTemplateHistoryInfo(String serviceName) {
		return this.getResourceHistoryInfo(serviceName, TEMPLATE_SUBDIR_NAME, TEMPLATE_FILENAME);
	}

	@Override
	public boolean supportsHistory() {
		return true;
	}

	@Override
	public Date fromStringToDate(String timestamp) throws ParseException {
		return new Date(Long.parseLong(timestamp));
	}

	@Override
	public String fromDateToString(Date date) {
		return Long.toString(date.getTime());
	}




}