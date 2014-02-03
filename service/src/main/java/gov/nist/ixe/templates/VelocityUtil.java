package gov.nist.ixe.templates;

import static gov.nist.ixe.Logging.trace;
import gov.nist.ixe.stringsource.StringSource;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.log4j.BasicConfigurator;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;

public class VelocityUtil {
	public static final String TEMPLATE_RESOURCE_NAME = "templateContents";
	
	static {
		BasicConfigurator.configure();
	}
	
	private static VelocityEngine getEngine() {
		trace();

		
		VelocityEngine engine = new VelocityEngine();

		// String logName = "COAT";
		//engine.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, Log4JLogChute.class.getName());
		//engine.setProperty("runtime.log.logsystem.log4j.logger", logName);
		
        engine.setProperty(Velocity.RESOURCE_LOADER, "string");
        engine.addProperty("string.resource.loader.class", StringResourceLoader.class.getName());
        engine.addProperty("string.resource.loader.repository.static", "false");
        engine.addProperty("string.resource.loader.modificationCheckInterval", "0");

        engine.init();
       
        return engine;
	}

	
	public static StringSource processTemplate(StringSource templateContents, String rootModelName, Object rootModel) 
			throws ResourceNotFoundException, ParseErrorException, IOException {
		trace();
		
		
		VelocityEngine engine = getEngine();
				
		engine.init();
		
		StringResourceRepository repo = (StringResourceRepository) engine.getApplicationAttribute(StringResourceLoader.REPOSITORY_NAME_DEFAULT);
		repo.setEncoding(templateContents.getCharset());
		repo.putStringResource(TEMPLATE_RESOURCE_NAME, templateContents.getString());
				
		VelocityContext context = new VelocityContext();
		
				context.put(rootModelName, rootModel);
		context.put("eol", "");
	
		Template template = engine.getTemplate(TEMPLATE_RESOURCE_NAME, templateContents.getCharset());
		
		
		Writer writer = new StringWriter();
		template.merge(context, writer);
		String result = writer.toString();
			
		return new StringSource(result.getBytes(templateContents.getCharset()), templateContents.getCharset());
	}
}