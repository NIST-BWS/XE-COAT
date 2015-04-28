package gov.nist.ixe.templates.tests;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nist.ixe.FileUtil;
import gov.nist.ixe.templates.FileStorageProvider;
import gov.nist.ixe.templates.HistoryInfo;
import gov.nist.ixe.templates.IStorageProvider;
import gov.nist.ixe.templates.exception.IllegalResourceNameException;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class FileStorageProviderTests extends IStorageProviderTests {

	private static FileStorageProvider fsp;
	private static String _location;
	

	@Before public void beforeEachTest() {
		_location = FileUtil.getRandomTempDirectoryName("FileStorageProviderTests");		
		fsp = new FileStorageProvider(_location);		
	}
	
	@After public void afterEachTest() {
		fsp.clear();
	}
	
	public IStorageProvider getStorageProvider() {
		return fsp;
	}
	
	@Test public void directoriesExistAtStartOfTest() {
		File f = new File(_location);
		assertTrue(f.exists());
	}
	
	@Test public void repositoryIsEmptyAtStartOfTest() {
		File f = new File(_location);
		assertEquals(0, f.listFiles().length);
	}
	
	@Test public void addService_CreatesDirectories() {
		String serviceName = "service0";
		fsp.addService(serviceName);
		File service = new File(_location + File.separator + serviceName);
		assertTrue(service.exists() && service.isDirectory());
		
		File schemas = new File(service, "schemas");
		assertTrue(schemas.exists() && schemas.isDirectory());
		
		File configs = new File(service, "configurations");
		assertTrue(configs.exists() && configs.isDirectory());
		
		File template = new File(service, "template");
		assertTrue(template.exists() && template.isDirectory());
	}
	
	
	@Test public void getTombstonePath_createsCorrectPath() {
		
		Date d = new Date(1270049473321L);
		
		String serviceName = "service0";
		String location, expected;
		UUID uuid = UUID.randomUUID();
		
		String u = "." + uuid.toString().replace("-", "") + ".";
		String t = "." + String.format("%020d", d.getTime());
		
		location = serviceName;
		expected = t + u + serviceName;
		assertEquals(expected, fsp.getTombstonePath(location, d, uuid));
				
		location = fsp.getLocation() + File.separator + serviceName;
		expected = fsp.getLocation() + File.separator + t + u + serviceName;
		assertEquals(expected, fsp.getTombstonePath(location, d, uuid));
		
		location = "a" + File.separator + "b" + File.separator + "c";
		expected = "a" + File.separator + "b" + File.separator + t + u + "c";
		assertEquals(expected, fsp.getTombstonePath(location, d, uuid));
		
		location = File.separator + "a" + File.separator + "b" + File.separator + "c";
		expected = File.separator + "a" + File.separator + "b" + File.separator + t + u + "c";
		assertEquals(expected, fsp.getTombstonePath(location, d, uuid));
		
	}

	@Test public void isTombstonePath_identifiesTombstonePathsCorrectly() {
		
		String tombStonePath, notTombstonePath;
		
		Date d = new Date(1270049473321L);
		UUID uuid = UUID.randomUUID();
		
		tombStonePath = fsp.getTombstonePath(fsp.getLocation(), d, uuid);
		assertTrue(fsp.isTombstonePath(tombStonePath));
		
		notTombstonePath = fsp.getLocation() + File.separator + "abc";
		assertFalse(fsp.isTombstonePath(notTombstonePath));
				
		notTombstonePath = fsp.getServiceFile("abc").getAbsolutePath();
		tombStonePath = fsp.getTombstonePath("abc");
		assertTrue(fsp.isTombstonePath(tombStonePath));
		assertFalse(fsp.isTombstonePath(notTombstonePath));
											}
	
	@Test (expected=IllegalResourceNameException.class) 
	public void addService_forbidsTombstoneNames() {
		getStorageProvider().addService(fsp.getTombstonePath("abc"));
	}
	
	@Test (expected=IllegalResourceNameException.class) 
	public void addSchema_forbidsTombstoneNames() {
		getStorageProvider().addSchema("service0", fsp.getTombstonePath("abc"), null);
	}
	
	@Test (expected=IllegalResourceNameException.class) 
	public void addConfig_forbidsTombstoneNames() {
		getStorageProvider().addConfig("service0", fsp.getTombstonePath("abc"), null);
	}

	
	@Test
	public void deleteService_renamesEmptyServiceToATombstone() {
		String serviceName = "service0";
		IStorageProvider storage = getStorageProvider(); 
		storage.addService(serviceName);
		storage.deleteService(serviceName);
		File service = new File(fsp.getLocation());
		
		boolean singleFile = service.listFiles().length == 1;
		boolean fileIsTombstoned = fsp.isTombstonePath(service.listFiles()[0].getPath());
		assertTrue(singleFile && fileIsTombstoned);
	}
	
	
	@Test
	public void settingMultipleTemplatesCreatesATombstone() {
		
		String serviceName = "service0";
		IStorageProvider storage = getStorageProvider(); 
		storage.addService(serviceName);
		
		storage.setTemplate(serviceName, Examples.TEMPLATE);
		storage.setTemplate(serviceName, Examples.TEMPLATE);
		storage.setTemplate(serviceName, Examples.TEMPLATE);
		
		File templatePath = fsp.getTemplateDir(serviceName);
		int tombstoneCount=0, nonTombstoneCount=0;
		for (File f : templatePath.listFiles()) {
			if (fsp.isTombstonePath(f.getPath())) {
				tombstoneCount++;
			} else {
				nonTombstoneCount++;
			}
		}
		assertEquals(2, tombstoneCount);
		assertEquals(1, nonTombstoneCount);
	
		
	}

	
	@Test
	public void schemaHistoryCanBeRetrievedCorrectly() {
		String serviceName = "service0";
		fsp.addService(serviceName);
				
		fsp.addSchema(serviceName, "schema0", Examples.DUMMY0);
		Date d0 = new Date();
		fsp.addSchema(serviceName, "schema0", Examples.DUMMY1);
		Date d1 = new Date();
		fsp.addSchema(serviceName, "schema0", Examples.DUMMY2);
		Date d2 = new Date();
		
		HistoryInfo[] hi = fsp.getSchemaHistoryInfo(serviceName, "schema0");
		
		assertDateBetween(hi[0].getTimestampAsDate(), d0, d1);
		assertDateBetween(hi[1].getTimestampAsDate(), d1, d2);
		
	}
	
	@Test
	public void historicSchemasCanBeRetrievedCorrectly() throws UnsupportedEncodingException {
		String serviceName = "service0";
		String schemaName = "xsd0";
		fsp.addService(serviceName);
				
		fsp.addSchema(serviceName, schemaName, Examples.DUMMY0);
		fsp.addSchema(serviceName, schemaName, Examples.DUMMY1);
		fsp.addSchema(serviceName, schemaName, Examples.DUMMY2);
		
		HistoryInfo[] hi = fsp.getSchemaHistoryInfo(serviceName, schemaName);
		assertEquals(Examples.DUMMY0.getString(), fsp.getHistoricSchema(serviceName, schemaName, hi[0].getTimestampAsDate()).getString());
		assertEquals(Examples.DUMMY1.getString(), fsp.getHistoricSchema(serviceName, schemaName, hi[1].getTimestampAsDate()).getString());
		
	}
	
	@Test
	public void renamingASchemaPreservesHistory() throws IOException {
		
		String serviceName = "service0";
		String originalSchemaName = "xsd0";
		String newSchemaName = "xsd1";
		fsp.addService(serviceName);
				
		fsp.addSchema(serviceName, originalSchemaName, Examples.DUMMY0);
		fsp.addSchema(serviceName, originalSchemaName, Examples.DUMMY1);
		fsp.addSchema(serviceName, originalSchemaName, Examples.DUMMY2);
		HistoryInfo[] hi = fsp.getSchemaHistoryInfo(serviceName, originalSchemaName);
		
	
		fsp.renameSchema(serviceName, originalSchemaName, newSchemaName);
		assertEquals(Examples.DUMMY0.getString(), fsp.getHistoricSchema(serviceName, newSchemaName, hi[0].getTimestampAsDate()).getString());
		assertEquals(Examples.DUMMY1.getString(), fsp.getHistoricSchema(serviceName, newSchemaName, hi[1].getTimestampAsDate()).getString());
		assertEquals(Examples.DUMMY2.getString(), fsp.getSchema(serviceName, newSchemaName).getString());
	}
	
	@Test
	public void renamingAConfigPreservesHistory() throws IOException {
		
		fsp.addService("svc");
				
		fsp.addConfig("svc", "c", Examples.DUMMY0);
		fsp.addConfig("svc", "c", Examples.DUMMY1);
		fsp.addConfig("svc", "c", Examples.DUMMY2);
		HistoryInfo[] hi = fsp.getConfigHistoryInfo("svc", "c");
	
		fsp.renameConfig("svc", "c", "d");
		
		assertEquals(Examples.DUMMY0.getString(), fsp.getHistoricConfig("svc", "d", hi[0].getTimestampAsDate()).getString());
		assertEquals(Examples.DUMMY1.getString(), fsp.getHistoricConfig("svc", "d", hi[1].getTimestampAsDate()).getString());
		assertEquals(Examples.DUMMY2.getString(), fsp.getConfig("svc", "d").getString());
	}
	
	
	@Test (expected=IllegalResourceNameException.class)
	public void cannotRenameAServiceToOneThatArleadyExists() {
		fsp.addService("s0");
		fsp.addService("s1");
		fsp.renameService("s0", "s1");
	}
	
	@Test (expected=IllegalResourceNameException.class)
	public void cannotRenameAConfigToOneThatAlreadyExists() throws IOException {
		fsp.addService("s0");
		fsp.addConfig("s0", "d0", Examples.DUMMY0);
		fsp.addConfig("s0", "d1", Examples.DUMMY1);
		fsp.renameConfig("s0", "d0", "d1");		
	}
	
	@Test (expected=IllegalResourceNameException.class)
	public void cannotRenameASchemaToOneThatAlreadyExists() throws IOException {
		fsp.addService("s0");
		fsp.addSchema("s0", "d0", Examples.DUMMY0);
		fsp.addSchema("s0", "d1", Examples.DUMMY1);
		fsp.renameSchema("s0", "d0", "d1");		
	}
	
	private static void assertDateBetween(Date d, Date earliest, Date latest) {
		assertTrue((d.equals(earliest) || d.after(earliest)) &&	(d.equals(latest) || d.before(latest)));
				
	}
	

}
