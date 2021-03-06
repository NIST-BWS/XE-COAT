package gov.nist.ixe.templates.tests;


import static org.junit.Assert.assertEquals;
import gov.nist.ixe.FileUtil;
import gov.nist.ixe.stringsource.StringSource;
import gov.nist.ixe.stringsource.StringSourcePersistence;
import gov.nist.ixe.templates.XmlCodeGen;
import gov.nist.ixe.templates.CodeGenException;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;

public class JaxbXsdTests {
	
	@Test
	public void CodeGenFromSchemaIsSuccessful() throws IOException, URISyntaxException, CodeGenException {
		codeGenFromSchemaIsSuccessful("txt/schema0.xsd");
		codeGenFromSchemaIsSuccessful("txt/sql.xsd");
		codeGenFromSchemaIsSuccessful("txt/dummy.xsd");
	}
	
	private void codeGenFromSchemaIsSuccessful(String schemaFilename) throws IOException, URISyntaxException, CodeGenException {
		File scratchDir = FileUtil.getRandomTempDirectory("codeGenFromSchemaIsSuccessful");
		File schemaDir = new File(scratchDir, "schemas"); schemaDir.mkdirs();
		File compileDir = new File(scratchDir, "compile"); compileDir.mkdirs();
		StringSource schema = StringSourcePersistence.inferFromSystemResource(schemaFilename);
		generateCodeFromSchema("schema0", schema, schemaDir, compileDir);
		FileUtil.clear(scratchDir);
	}
	
	@Test
	public void CompileCodeIsSuccessful() throws IOException, URISyntaxException, CodeGenException {
		
		compileCodeIsSuccessful("txt/schema0.xsd");
		compileCodeIsSuccessful("txt/sql.xsd");
		compileCodeIsSuccessful("txt/dummy.xsd");
	}
	
	private void compileCodeIsSuccessful(String schemaFilename) throws IOException, URISyntaxException, CodeGenException {
		File scratchDir = FileUtil.getRandomTempDirectory("compileCodeIsSuccessful");
		
		File schemaDir = new File(scratchDir, "schemas"); schemaDir.mkdirs();
		File compileDir = new File(scratchDir, "compile"); compileDir.mkdirs();
		
		StringSource schema = StringSourcePersistence.inferFromSystemResource(schemaFilename);
		generateCodeFromSchema("schema0", schema, schemaDir, compileDir);
		
		XmlCodeGen.compile(compileDir, compileDir.getAbsolutePath());
		
		FileUtil.clear(scratchDir);
	}
	
	@Test
	
	public void emptyPayloadFailsAsExpected() throws IOException {
		File scratchDir = FileUtil.getRandomTempDirectory("compileCodeIsSuccessful");
		
		File schemaDir = new File(scratchDir, "schemas"); schemaDir.mkdirs();
		File compileDir = new File(scratchDir, "compile"); compileDir.mkdirs();
		
		StringSource schema = Examples.EMPTY;
		try {
			generateCodeFromSchema("schema0", schema, schemaDir, compileDir);
			XmlCodeGen.compile(compileDir, compileDir.getAbsolutePath());
		} catch (CodeGenException cge) {
			assertEquals(1, cge.getSchemaCompilerErrorListener().getErrors().size());	
		} finally {
			FileUtil.clear(scratchDir);	
		}
		
	}

	private void generateCodeFromSchema(String schemaName, StringSource schema, File schemaDir, File compileDir) throws IOException, CodeGenException {
		String packageName = XmlCodeGen.getUniquePackageName();
		XmlCodeGen.generateCodeFromSchema(packageName, schemaName, schema, schemaDir, compileDir, null);		
	}
}