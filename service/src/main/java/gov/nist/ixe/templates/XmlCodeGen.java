package gov.nist.ixe.templates;

import static gov.nist.ixe.Logging.trace;
import gov.nist.ixe.StringUtil;
import gov.nist.ixe.stringsource.StringSource;
import gov.nist.ixe.stringsource.StringSourcePersistence;
import gov.nist.ixe.templates.exception.MissingCompilerException;
import gov.nist.ixe.templates.jaxb.Link;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.InputSource;

import com.sun.codemodel.JCodeModel;
import com.sun.tools.xjc.api.S2JJAXBModel;
import com.sun.tools.xjc.api.SchemaCompiler;
import com.sun.tools.xjc.api.XJC;

public class XmlCodeGen {
	
	public static void generateSchemaFromClasses(
			final File outputDir, final String outputFilename,
			@SuppressWarnings("rawtypes") Class... classes) throws JAXBException, IOException {
		
		class MySchemaOutputResolver extends SchemaOutputResolver {			
		    public Result createOutput( String namespaceUri, String suggestedFileName) throws IOException {
		        return new StreamResult(new File(outputDir, outputFilename));
		    }
		}
		
		JAXBContext context = JAXBContext.newInstance(classes);
		context.generateSchema(new MySchemaOutputResolver());
		
	}

	public static File generateCodeFromSchema(
			String packageName, String schemaName, StringSource schema, 
			File schemaDir, File compilationDir, Link schemaLink) throws IOException, CodeGenException {
		
		trace();
		
		// Write the schema contents to a scratch file
		File schemaFile = new File(schemaDir, schemaName);
		StringSourcePersistence.save(schema, schemaFile);
				
		SchemaCompiler sc = XJC.createSchemaCompiler();
		SchemaCompilerErrorListener scel = new SchemaCompilerErrorListener(schemaLink);
		sc.setErrorListener(scel);
		sc.forcePackageName(packageName);
		
		// Get the real backing store for the schema file
		schemaFile = StringSourcePersistence.getRealFile(schemaFile);
		InputSource is = new InputSource(new FileInputStream(schemaFile));
		is.setSystemId(schemaFile.getAbsolutePath().replace("\\", "/"));
		
		sc.parseSchema(is);		
		if (scel.getErrors().size() > 0) {
			throw new CodeGenException(schemaLink, scel, true);
		}
		
		S2JJAXBModel model = sc.bind();
		if (scel.getErrors().size() > 0) {
			throw new CodeGenException(schemaLink, scel, true);
		}
		
		JCodeModel codeModel = model.generateCode(null, null);
		
		compilationDir.mkdirs();
		codeModel.build(compilationDir);
		return schemaFile;
		
	}
	
	public static String getUniquePackageName() {
		return String.format("xe%s", System.nanoTime());
	}
	
	public static void compile(File start, String classpath) {
		trace();
		if (start.isDirectory()) {
			for (File s : start.listFiles()) {
				compile(s, classpath);
			}
		} else {
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			if (compiler == null) throw new MissingCompilerException();
			compiler.run(null,  null,  null, "-cp", classpath, start.getAbsolutePath());
		}
	}
	
	
	public static Class<?> loadClass(File compilationDirectory, String packageName, String className) throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		trace();
		
		URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { compilationDirectory.toURI().toURL() });
		return Class.forName(String.format("%s.%s", packageName, StringUtil.capitalize(className)), true, classLoader);
	}
	
}
