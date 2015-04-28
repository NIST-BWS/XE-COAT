package gov.nist.ixe.templates.tests;

import static org.junit.Assert.assertEquals;
import gov.nist.ixe.templates.BuildUri;
import gov.nist.ixe.templates.Constants;

import org.junit.Test;

public class BuildUriTests {

	public static final String rootUri = "http://example.com/";
	
	@Test public void getVersionUri_buildsCorrectly() {
		String uri = BuildUri.getVersionUri(rootUri);
		assertEquals("http://example.com/version", uri);
	}
	
	@Test public void getTestConnectionUri_buildsCorrectly() {
		String uri = BuildUri.getTestConnectionUri(rootUri);
		assertEquals("http://example.com/test-connection", uri);
	}


	@Test public void getServiceUri_buildsCorrectly() {
		String uri = BuildUri.getServiceUri(rootUri, "service0");
		assertEquals("http://example.com/service0", uri);
	}


	@Test public void getTemplateUri_buildsCorrectly() {
		String uri = BuildUri.getTemplateUri(rootUri, "service0");
		assertEquals("http://example.com/service0/template", uri);
	}

	@Test public void getTemplateHistoryUri_buildsCorrectly() {
		String uri = BuildUri.getTemplateHistoryUri(rootUri, "service0");
		assertEquals("http://example.com/history/service0/template", uri);
	}


	@Test public void getProcessUri_buildsCorrectly() {
		String uri = BuildUri.getProcessUri(rootUri, "service0");
		assertEquals("http://example.com/service0/process", uri);
	}


	@Test public void getProcessPayloadUri_buildsCorrectly() {
		String uri = BuildUri.getProcessPayloadUri(rootUri, "service0");
		assertEquals("post://example.com/service0/process", uri);
	}


	@Test public void getSchemaUri_buildsCorrectly() {
		String uri = BuildUri.getSchemaUri(rootUri, "service0", "schema0.xsd");
		assertEquals("http://example.com/service0/schema/schema0.xsd", uri);
	}

	@Test public void getSchemaHistoryUri_buildsCorrectly() {
		String uri = BuildUri.getSchemaHistoryUri(rootUri, "service0", "schema0.xsd");
		assertEquals("http://example.com/history/service0/schema/schema0.xsd", uri);
	}


	@Test public void getConfigUri_buildsCorrectly() {
		String uri = BuildUri.getConfigUri(rootUri, "service0", "example0.xml");
		assertEquals("http://example.com/service0/config/example0.xml", uri);
	}

	@Test public void getConfigHistoryUri_buildsCorrectly() {
		String uri = BuildUri.getConfigHistoryUri(rootUri, "service0", "example0.xml");
		assertEquals("http://example.com/history/service0/config/example0.xml", uri);
	}


	@Test public void getNamedProcessUri_buildsFromNameCorrectly() {
		String uri = BuildUri.getNamedProcessUri(rootUri, "service0", "example0.xml");
		assertEquals("http://example.com/service0/process/example0.xml", uri);
	}

	@Test public void getNamedProcessUri_buildsFromDefaultCorrectly() {
		String uri = BuildUri.getNamedProcessUri(rootUri, "service0", Constants.DEFAULT_CONFIGURATION_NAME);
		assertEquals("http://example.com/service0/process/" + Constants.DEFAULT_CONFIGURATION_NAME, uri);
	}


	@Test public void getHistoricTemplateUri_buildsCorrectly() {
		String uri = BuildUri.getHistoricTemplateUri(rootUri, "service0", "12345");
		assertEquals("http://example.com/historic/12345/service0/template", uri);
	}

	@Test public void getHistoricSchemaUri_buildsCorrectly() {
		String uri = BuildUri.getHistoricSchemaUri(rootUri, "service0", "schema0", "12345");
		assertEquals("http://example.com/historic/12345/service0/schema/schema0", uri);
	}


	@Test public void getHistoricConfigUri_buildsCorrectly() {
		String uri = BuildUri.getHistoricConfigUri(rootUri, "service0", "config0", "12345");
		assertEquals("http://example.com/historic/12345/service0/config/config0", uri);
	}


	@Test public void getBaseRenameServiceUri_buildsCorrectly() {
		String uri = BuildUri.getRenameServiceUri(rootUri, "service0");
		assertEquals("http://example.com/rename/service/service0", uri);
	}

	@Test public void getRenameServiceUri_buildsCorrectly() {
		String uri = BuildUri.getRenameServiceUri(rootUri, "service0", "service1");
		assertEquals("http://example.com/rename/service/service0?newName=service1", uri);
	}



	@Test public void getBaseRenameSchemaUri_buildsCorrectly() {
		String uri = BuildUri.getRenameSchemaUri(rootUri, "service0", "schema0");
		assertEquals("http://example.com/rename/service0/schema/schema0", uri);
	}

	@Test public void getRenameSchmeaUri_buildsCorrectly() {
		String uri = BuildUri.getRenameSchemaUri(rootUri, "service0", "schema0", "schema1");
		assertEquals("http://example.com/rename/service0/schema/schema0?newName=schema1", uri);
	}


	@Test public void getBaseRenameConfigUri_buildsCorrectly() {
		String uri = BuildUri.getRenameConfigUri(rootUri, "service0", "config0");
		assertEquals("http://example.com/rename/service0/config/config0", uri);
	}

	@Test public void getRenameConfigUri_buildsCorrectly() {
		String uri = BuildUri.getRenameConfigUri(rootUri, "service0", "config0", "config1");
		assertEquals("http://example.com/rename/service0/config/config0?newName=config1", uri);
	}

	@Test public void getBaseUploadUri_buildsCorrectly() {
		String uri = BuildUri.getUploadUri(rootUri, "service0");
		assertEquals("http://example.com/service0/upload", uri);
	}

	@Test public void getUploadUri_buildsCorrectly() {
		String uri = BuildUri.getUploadUri(rootUri, "service0", "schema", "schema0");
		assertEquals("http://example.com/service0/upload?rel=schema&name=schema0", uri);
	}

	@Test public void getNamespaceUri_buildsCorrectly() {
		String uri = BuildUri.getNamespaceUri("foo", 1234567890L);
		assertEquals("http://templates.xe.nist.gov/foo/1234567890", uri);
	}
	
	@Test public void getBaseIniSplitterUri_buildsCorrectly() {
		String uri = BuildUri.getIniSplitterUri(rootUri);
		assertEquals("http://example.com/splitter/ini", uri);
	}
	
	@Test public void getIniSplitterUri_buildsCorrectly() {
		String uri = BuildUri.getIniSplitterUri(rootUri, "service0");
		assertEquals("http://example.com/splitter/ini?serviceName=service0", uri);
	}
	
	@Test public void getBaseCloneServiceUri_buildsCorrectly() {
		String uri = BuildUri.getCloneServiceUri(rootUri, "service0");
		assertEquals("http://example.com/cloneService/service0", uri);
	}

	@Test public void getCloneServiceUri_buildsCorrectly() {
		String uri = BuildUri.getCloneServiceUri(rootUri, "service0", "newService");
		assertEquals("http://example.com/cloneService/service0?newName=newService", uri);
	}
}
