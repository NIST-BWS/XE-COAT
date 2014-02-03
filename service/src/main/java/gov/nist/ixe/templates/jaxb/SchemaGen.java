package gov.nist.ixe.templates.jaxb;

import gov.nist.ixe.templates.CodeGen;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;

public class SchemaGen {
	
	public static void main(String... args) throws JAXBException, IOException  {
		File tmp = new File("C:/tmp");
		CodeGen.generateSchemaFromClasses(tmp, "Link.xsd", Link.class);
		CodeGen.generateSchemaFromClasses(tmp, "ExceptionResult.xsd", ExceptionResult.class);
		CodeGen.generateSchemaFromClasses(tmp, "ServiceList.xsd", ServiceList.class);
		CodeGen.generateSchemaFromClasses(tmp, "ServiceResources.xsd", ServiceResources.class);
		CodeGen.generateSchemaFromClasses(tmp, "TemplateGenerationError.xsd", TemplateGenerationError.class);
		CodeGen.generateSchemaFromClasses(tmp, "ServiceList.xsd", ServiceList.class);
		CodeGen.generateSchemaFromClasses(tmp, "ParseError.xsd", ParseError.class);
		CodeGen.generateSchemaFromClasses(tmp, "RenameResult.xsd", RenameResult.class);
		CodeGen.generateSchemaFromClasses(tmp, "ResourceHistory.xsd", ResourceHistory.class);
		CodeGen.generateSchemaFromClasses(tmp, "ResourceList.xsd", ResourceList.class);
		
		
	}

}
