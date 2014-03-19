package gov.nist.ixe.templates.jaxb;

import gov.nist.ixe.templates.XmlCodeGen;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;

public class SchemaGen {
	
	public static void main(String... args) throws JAXBException, IOException  {
		File tmp = new File("C:/tmp");
		XmlCodeGen.generateSchemaFromClasses(tmp, "Link.xsd", Link.class);
		XmlCodeGen.generateSchemaFromClasses(tmp, "ExceptionResult.xsd", ExceptionResult.class);
		XmlCodeGen.generateSchemaFromClasses(tmp, "ServiceList.xsd", ServiceList.class);
		XmlCodeGen.generateSchemaFromClasses(tmp, "ServiceResources.xsd", ServiceResources.class);
		XmlCodeGen.generateSchemaFromClasses(tmp, "TemplateGenerationError.xsd", TemplateGenerationError.class);
		XmlCodeGen.generateSchemaFromClasses(tmp, "ServiceList.xsd", ServiceList.class);
		XmlCodeGen.generateSchemaFromClasses(tmp, "ParseError.xsd", ParseError.class);
		XmlCodeGen.generateSchemaFromClasses(tmp, "RenameResult.xsd", RenameResult.class);
		XmlCodeGen.generateSchemaFromClasses(tmp, "ResourceHistory.xsd", ResourceHistory.class);
		XmlCodeGen.generateSchemaFromClasses(tmp, "ResourceList.xsd", ResourceList.class);
		
		
	}

}
