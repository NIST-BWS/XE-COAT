/*----------------------------------------------------------------------------------------------------+
 |                             National Institute of Standards and Technology                          |
 |                                        Biometric Clients Lab                                        |
 +-----------------------------------------------------------------------------------------------------+
  File author(s):
  	   Ross J. Micheals (ross.micheals@nist.gov)
  	   Kevin Mangold (kevin.mangold@nist.gov)

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

import static gov.nist.ixe.Logging.info;
import static gov.nist.ixe.Logging.trace;
import gov.nist.ixe.EncodingUtil;
import gov.nist.ixe.FileUtil;
import gov.nist.ixe.XmlUtil;
import gov.nist.ixe.jaxb.BindingContext;
import gov.nist.ixe.stringsource.StringSource;
import gov.nist.ixe.stringsource.StringSourceConverters;
import gov.nist.ixe.templates.exception.IllegalContentTypeException;
import gov.nist.ixe.templates.exception.IllegalResourceNameException;
import gov.nist.ixe.templates.exception.IllegalResourceTypeException;
import gov.nist.ixe.templates.exception.InfSplitterException;
import gov.nist.ixe.templates.exception.MissingCompilerException;
import gov.nist.ixe.templates.exception.ResourceNotFoundException;
import gov.nist.ixe.templates.exception.TemplateGenerationException;
import gov.nist.ixe.templates.exception.UnreachableServiceException;
import gov.nist.ixe.templates.ini.Generator;
import gov.nist.ixe.templates.ini.IniSection;
import gov.nist.ixe.templates.jaxb.Link;
import gov.nist.ixe.templates.jaxb.ParseError;
import gov.nist.ixe.templates.jaxb.RenameResult;
import gov.nist.ixe.templates.jaxb.ResourceHistory;
import gov.nist.ixe.templates.jaxb.ResourceList;
import gov.nist.ixe.templates.jaxb.ServiceList;
import gov.nist.ixe.templates.jaxb.ServiceResources;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;

import org.apache.velocity.exception.ParseErrorException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.parser.XSOMParser;

@Path("")
public class TemplateServices implements ITemplateServices {

	private @Context UriInfo uriInfo;
	
	private static StorageProviderFactory storageProviderFactory = new StorageProviderFactory();
	private static IStorageProvider storage = storageProviderFactory
			.getStorageProvider();

	public String getRootUri() {
		trace();

		String rootUri = "file:/";

		if (uriInfo != null) {
			rootUri = uriInfo.getBaseUri().toString();
		}

		return rootUri;
	}
	
	public String getVersion() {
		return Version.getVersion();
	}

	public ServiceList getServiceList() {
		trace();
		return new ServiceList(getStorageProvider(), getRootUri());
	}

	public ServiceResources getServiceResources(String serviceName)
			throws ResourceNotFoundException {
		trace();

		validateServiceOrResourceName(serviceName);

		return new ServiceResources(getStorageProvider(), getRootUri(),
				serviceName);
	}

	public void createService(String serviceName) {
		trace();

		serviceName = serviceName.trim();
		validateServiceOrResourceName(serviceName);

		getStorageProvider().addService(serviceName);

		info(String.format("Service '%s' was created.", serviceName));
	}

	public void deleteService(String serviceName) {
		trace();

		forbidEmptyName(serviceName);

		getStorageProvider().deleteService(serviceName);

		info(String.format("Service '%s' was deleted.", serviceName));
	}

	private void requirePrimarySchema(String serviceName) {
		trace();

		// getSchema() throws resource not found if the primary schema is
		// missing.
		getSchema(serviceName, Constants.PRIMARY_SCHEMA_NAME);
	}

	private void requireTemplate(String serviceName) {
		trace();

		// getTemplate() throws resource not found if there is no template
		getTemplate(serviceName);
	}

	private Response generateTemplate(String serviceName, StringSource config,
			Link configLink, Link processLink) {
		trace();
		
		String resourceName = Constants.NAMED_PROCESS_NAME_PREFIX + configLink.getName();

		requirePrimarySchema(serviceName);
		requireTemplate(serviceName);

		File scratchDir = FileUtil.getRandomTempDirectory("generateTemplate");
		File compilationDir = new File(scratchDir, "compilation");
		compilationDir.mkdirs();
		File schemaDir = new File(scratchDir, "schemas");
		schemaDir.mkdirs();

		File mainSchemaFile = null;

		try {
			String packageName = XmlCodeGen.getUniquePackageName();
			List<StringSourceUriPair> schemas = new ArrayList<StringSourceUriPair>();

			// For each schema, generate the required source code
			for (Link l : getServiceResources(serviceName).getSchemaLinks()) {
				StringSource schema = getStorageProvider().getSchema(
						serviceName, l.getName());

				StringSourceUriPair ssup = new StringSourceUriPair(schema,
						l.getUri());
				schemas.add(ssup);

				File schemaFile = XmlCodeGen.generateCodeFromSchema(packageName,
						l.getName(), schema, schemaDir, compilationDir, l);

				if (Constants.PRIMARY_SCHEMA_NAME.equals(l.getName())) {
					mainSchemaFile = schemaFile;
				}
			}

			// Compile all of the generated code
			XmlCodeGen.compile(compilationDir, compilationDir.getAbsolutePath());

			try {
				// Make (SAX) InputSources out of the StringSources so we can
				// have thorough error messages.
				//
				InputSource configIS = (new StringSourceUriPair(config,
						configLink.getUri())).getInputSource();
				InputSource[] schemaISs = new InputSource[schemas.size()];
				for (int i = 0; i < schemas.size(); i++) {
					schemaISs[i] = schemas.get(i).getInputSource();
				}
				XmlUtil.ValidateXml(schemaISs, configIS);
			} catch (SAXParseException spe) {
				ParseError pe = new ParseError(configLink, spe, false);
				TemplateGenerationException tge = new TemplateGenerationException(serviceName, resourceName);
				tge.getParseErrors().add(pe);
				throw tge;
			} catch (NullPointerException e) {
				throw new TemplateGenerationException(serviceName, resourceName, e);
			} catch (SAXException e) {
				throw new TemplateGenerationException(serviceName, resourceName, e);
			} catch (ParserConfigurationException e) {
				throw new TemplateGenerationException(serviceName, resourceName, e);
			}

			// Get the name and data type of the root element
			String rootElementType = null;
			XSOMParser parser = new XSOMParser();
			try {
				parser.parse(mainSchemaFile);
				XSSchemaSet sset = parser.getResult();
				ArrayList<XSElementDecl> elements = new ArrayList<XSElementDecl>();
				Iterator<XSElementDecl> jtr = sset.iterateElementDecls();
				while (jtr.hasNext()) {
					elements.add((XSElementDecl) jtr.next());
				}
				if (elements.size() == 0) {
					throw new UnreachableServiceException();
				} else if (elements.size() == 1) {
					rootElementType = elements.get(0).getType().getName();
				} else {

					final Comparator<XSElementDecl> XSElementDeclComparitor = new Comparator<XSElementDecl>() {
						public int compare(XSElementDecl e1, XSElementDecl e2) {
							int l1 = -1, l2 = -1, c1 = -1, c2 = -1;
							if (e1 != null && e1.getLocator() != null) {
								l1 = e1.getLocator().getLineNumber();
								c1 = e1.getLocator().getColumnNumber();
							}
							if (e2 != null && e2.getLocator() != null) {
								l2 = e2.getLocator().getLineNumber();
								c2 = e2.getLocator().getColumnNumber();
							}
							int result = l1 - l2;
							if (result == 0) {
								result = c1 - c2;
							}
							return result;
						}
					};
					Collections.sort(elements, XSElementDeclComparitor);
					Link link = getServiceResources(serviceName)
							.getPrimarySchemaLink();
					throw TemplateGenerationException
							.MainSchemaHasMultipleRootElements(serviceName, resourceName, link,
									elements.get(1));
				}

			} catch (SAXException e) {
				throw new TemplateGenerationException(serviceName, resourceName, e);
			}
			
			Class<?> cls = XmlCodeGen.loadClass(compilationDir, packageName,
					rootElementType);
			InputStream in = new ByteArrayInputStream(config.getData());

			Unmarshaller unmarshaller = BindingContext.getBindingContext(cls)
					.createUnmarshaller();
			Object configObj = unmarshaller
					.unmarshal(new StreamSource(in), cls).getValue();
			StringSource template = getStorageProvider().getTemplate(
					serviceName);

			StringSource output = VelocityUtil.processTemplate(template,
					"ROOT", configObj);
			return StringSourceConverters.toResponse(output, "plain", 
					serviceName, Constants.Rel.PROCESS, configLink.getName());

		} catch (JAXBException jaxbe) {

			TemplateGenerationException tge = null;
			if (SAXParseException.class.equals(jaxbe.getCause().getClass())) {
				SAXParseException spe = (SAXParseException) jaxbe.getCause();
				ParseError pe = new ParseError(configLink, spe, false);
				tge = new TemplateGenerationException(serviceName, resourceName);
				tge.getParseErrors().add(pe);
			} else {
				tge = new TemplateGenerationException(serviceName, resourceName, jaxbe);
			}
			throw tge;
		} catch (ParseErrorException pee) {
			Link templateLink = Link.Template(getStorageProvider(),
					getRootUri(), serviceName);
			ParseError pe = new ParseError(templateLink, pee);
			TemplateGenerationException tge = new TemplateGenerationException(serviceName, resourceName);
			tge.getParseErrors().add(pe);
			throw tge;

		} catch (MissingCompilerException mce) {
			throw new TemplateGenerationException(serviceName, resourceName, mce);
		} catch (IOException ioe) {
			throw new TemplateGenerationException(serviceName, resourceName, ioe);
		} catch (ClassNotFoundException cnfe) {
			throw new TemplateGenerationException(serviceName, resourceName, cnfe);
		} catch (InstantiationException ie) {
			throw new TemplateGenerationException(serviceName, resourceName, ie);
		} catch (IllegalAccessException iae) {
			throw new TemplateGenerationException(serviceName, resourceName, iae);
		} catch (CodeGenException cge) {
			throw new TemplateGenerationException(serviceName, resourceName, cge);
		} finally {
			FileUtil.clear(scratchDir);
		}
	}

	public Response processTemplate(String serviceName, byte[] payload,
			String contentType) throws ResourceNotFoundException {
		trace();
		StringSource config = new StringSource(payload, contentType);
		return generateTemplate(serviceName, config,
				Link.ProcessPayload(storage, getRootUri(), serviceName),
				Link.Process(storage, getRootUri(), serviceName));

	}

	public Response processDefaultTemplate(String serviceName) {
		trace();
		return processTemplateByName(serviceName,
				Constants.DEFAULT_CONFIGURATION_NAME);
	}

	public Response processTemplateByName(String serviceName, String configName) {
		trace();
		StringSource config = getStorageProvider().getConfig(serviceName,
				configName);
		Response result = generateTemplate(serviceName, config, 
				Link.Config(storage, getRootUri(), serviceName, configName),
				Link.NamedProcess(storage, getRootUri(), serviceName,
						configName));
		return result;
	}

	public Response getTemplate(String serviceName) {

		trace();
		validateServiceOrResourceName(serviceName);
		return StringSourceConverters.toResponse(getStorageProvider()
				.getTemplate(serviceName), Constants.ContentType.SubType.PLAIN, 
				serviceName, Constants.Rel.TEMPLATE, Constants.TEMPLATE_RESOURCE_NAME);
	}

	public void setTemplate(String serviceName, byte[] payload,
			String contentType) {
		trace();

		validateServiceOrResourceName(serviceName);
		if (enforceRequiredContentType) 
			requireSpecificContentType(contentType, "text/plain");


		StringSource template = new StringSource(payload, contentType);
		getStorageProvider().setTemplate(serviceName, template);
		info(String.format("The template for service '%s' was set.",
				serviceName));
	}

	public void deleteTemplate(String serviceName) {
		trace();

		validateServiceOrResourceName(serviceName);

		getStorageProvider().deleteTemplate(serviceName);

		info(String.format("The template for service '%s' was deleted.",
				serviceName));
	}

	public Response getSchema(String serviceName, String schemaName) {
		trace();

		validateServiceOrResourceName(serviceName);
		forbidEmptyName(schemaName);
		return StringSourceConverters.toResponse(getStorageProvider()
				.getSchema(serviceName, schemaName), "xml", 
				serviceName, Constants.Rel.SCHEMA, schemaName);
	}

	public void setSchema(String serviceName, String schemaName,
			byte[] payload, String contentType) {
		trace();

		serviceName = serviceName.trim();
		schemaName = schemaName.trim();
		
		validateServiceOrResourceName(serviceName);
		validateServiceOrResourceName(schemaName);
		forbidEmptyName(schemaName);
		forbidReservedName(schemaName);
		if (enforceRequiredContentType)
			requireSpecificContentType(contentType, "text/xml");

		
		StringSource schema = new StringSource(payload, contentType);
		getStorageProvider().addSchema(serviceName, schemaName, schema);
		info(String.format("Schema '%s' was added to service '%s'.",
				schemaName, serviceName));
	}

	public void deleteSchema(String serviceName, String schemaName) {
		trace();

		validateServiceOrResourceName(serviceName);
		forbidEmptyName(schemaName);

		getStorageProvider().deleteSchema(serviceName, schemaName);

		info(String.format("Schema '%s' was deleted from service '%s'.",
				schemaName, serviceName));
	}

	public Response getConfig(String serviceName, String configName) {
		trace();

		validateServiceOrResourceName(serviceName);		
		forbidEmptyName(configName);

		return StringSourceConverters.toResponse(getStorageProvider()
				.getConfig(serviceName, configName), "xml", 
				serviceName, Constants.Rel.CONFIG, configName);
	}

	public void setConfig(String serviceName, String configName,
			byte[] payload, String contentType) {
		trace();

		validateServiceOrResourceName(serviceName);
		validateServiceOrResourceName(configName);
		forbidEmptyName(configName);
		forbidReservedName(configName);
		if (enforceRequiredContentType)
			requireSpecificContentType(contentType, "text/xml");
		getStorageProvider().addConfig(serviceName, configName,
				new StringSource(payload, contentType));

		info(String.format("Configuration '%s' was added to service '%s'.",
				configName, serviceName));
	}

	public void deleteConfig(String serviceName, String configName) {
		trace();

		validateServiceOrResourceName(serviceName);
		forbidEmptyName(configName);

		getStorageProvider().deleteConfig(serviceName, configName);

		info(String.format("Configuration '%s' was deleted from service '%s'.",
				configName, serviceName));
	}

	public static IStorageProvider getStorageProvider() {
		trace();
		return storage;
	}

	public static void CleanStorage() {
		trace();
		storageProviderFactory.CleanStorage();
	}

	private static void validateServiceOrResourceName(String name)
			throws IllegalResourceNameException {
		forbidEmptyName(name);
		forbidReservedName(name);
		if (!name.matches("^[a-zA-Z0-9_. ()][a-zA-Z0-9_. ()\\\\\\- ]*")) {
			throw IllegalResourceNameException.IllegalFormat(name);
		}
	}

	private static void requireSpecificContentType(String actual,
			String expected) {
		
		
		boolean result = false;
		try {
			MediaType actualMT = MediaType.valueOf(actual);
			MediaType expectedMT = MediaType.valueOf(expected);
			result = actualMT.isCompatible(expectedMT);
		} catch (IllegalArgumentException ex) {
			result = false;
		}
		if (!result) {
			throw new IllegalContentTypeException(actual);
		}

	}

	private static void validateResourceType(String rel)
			throws IllegalResourceTypeException {
		boolean validResource = false;
		for (String resourceType : Constants.DOCUMENT_RESOURCE_TYPES) {
			if (resourceType.equals(rel)) {
				validResource = true;
				break;
			}
		}
		if (!validResource) {
			throw IllegalResourceTypeException.InvalidType(rel);
		}
	}

	private static void forbidEmptyName(String name)
			throws IllegalResourceNameException {
		trace();

		if (name == null || name.trim().equals("")) {
			throw IllegalResourceNameException.EmptyName();
		}
	}

	private static void forbidReservedName(String name)
			throws IllegalResourceNameException {
		for (String reservedName : Constants.RESERVED_NAMES) {
			if (reservedName.equals(name)) {
				throw IllegalResourceNameException.ReservedName(name);
			}
		}
	}

	public ResourceHistory getTemplateHistory(String serviceName)
			throws ResourceNotFoundException {
		trace();
		return new ResourceHistory(getStorageProvider(), getRootUri(),
				serviceName, Constants.Rel.TEMPLATE,
				Constants.TEMPLATE_RESOURCE_NAME);
	}

	public Response getHistoricTemplate(String serviceName, String timestamp)
			throws ParseException {
		trace();
		forbidEmptyName(serviceName);
		Date d = getStorageProvider().fromStringToDate(timestamp);
		StringSource template = getStorageProvider().getHistoricTemplate(
				serviceName, d);
		Response result = StringSourceConverters.toResponse(template,
				Constants.ContentType.SubType.PLAIN, 
				serviceName, Constants.Rel.TEMPLATE, Constants.TEMPLATE_RESOURCE_NAME);
		result.getHeaders().add(Constants.HttpHeader.HISTORIC_VERSION_OF, 
				BuildUri.getTemplateUri(getRootUri(), serviceName));
		return result;
	}

	public ResourceHistory getSchemaHistory(String serviceName,
			String schemaName) throws ResourceNotFoundException {
		trace();

		forbidEmptyName(serviceName);
		forbidEmptyName(schemaName);

		return new ResourceHistory(getStorageProvider(), getRootUri(),
				serviceName, Constants.Rel.SCHEMA, schemaName);
	}

	public Response getHistoricSchema(String serviceName, String schemaName,
			String timestamp) throws ResourceNotFoundException, ParseException {
		trace();

		forbidEmptyName(serviceName);
		forbidEmptyName(schemaName);

		Date d = getStorageProvider().fromStringToDate(timestamp);
		StringSource schema = getStorageProvider().getHistoricSchema(
				serviceName, schemaName, d);
		Response result = StringSourceConverters.toResponse(schema,
				Constants.ContentType.SubType.XML, 
				serviceName, Constants.Rel.SCHEMA, schemaName);
		result.getHeaders().add(Constants.HttpHeader.HISTORIC_VERSION_OF, 
				BuildUri.getSchemaUri(getRootUri(), serviceName, schemaName));
		return result;
	}

	public ResourceHistory getConfigHistory(String serviceName,
			String configName) throws ResourceNotFoundException {
		trace();

		forbidEmptyName(serviceName);
		forbidEmptyName(configName);

		return new ResourceHistory(getStorageProvider(), getRootUri(),
				serviceName, Constants.Rel.CONFIG, configName);
	}

	public Response getHistoricConfig(String serviceName, String configName,
			String timestamp) throws ResourceNotFoundException, ParseException {

		forbidEmptyName(serviceName);
		forbidEmptyName(configName);

		Date d = getStorageProvider().fromStringToDate(timestamp);
		StringSource config = getStorageProvider().getHistoricConfig(
				serviceName, configName, d);
		Response result = StringSourceConverters.toResponse(config, Constants.ContentType.SubType.XML, 
				serviceName, Constants.Rel.CONFIG, configName);
		result.getHeaders().add(Constants.HttpHeader.HISTORIC_VERSION_OF, 
				BuildUri.getConfigUri(getRootUri(), serviceName, configName));
		return result;
	}

	public RenameResult renameService(String serviceName, String newName) {
		forbidEmptyName(serviceName);
		forbidEmptyName(newName);
		forbidReservedName(newName);
		
		validateServiceOrResourceName(newName);
		
		
		ServiceResources oldResources = getServiceResources(serviceName);
		
		
		getStorageProvider().renameService(serviceName, newName);
		info(String.format("Serivce '%s' renamed to '%s'.", serviceName,
				newName));

		IStorageProvider storage = getStorageProvider();
		String rootUri = getRootUri();

		Link oldServiceLink = Link.Service(storage, rootUri, serviceName);
		Link newServiceLink = Link.Service(storage, rootUri, newName);

		RenameResult parentRR = new RenameResult();
		parentRR.setOldLink(oldServiceLink);
		parentRR.setNewLink(newServiceLink);
		
		ServiceResources newResources = getServiceResources(newName);
		for (Link oldLink : oldResources.getResources()) {
			RenameResult rr = new RenameResult();
			rr.setOldLink(oldLink);
			rr.setNewLink(newResources.getLinkByNameAndRel(oldLink.getName(), oldLink.getRel()));
			parentRR.getRenamedResources().add(rr);
		}
		
		
		return parentRR;
	}

	public RenameResult renameSchema(String serviceName, String schemaName,
			String newName) {
		forbidEmptyName(serviceName);
		forbidEmptyName(schemaName);
		forbidEmptyName(newName);
		
		validateServiceOrResourceName(newName);

		forbidReservedName(newName);
		getStorageProvider().renameSchema(serviceName, schemaName, newName);

		info(String.format("Serivce '%s' renamed schema '%s' to '%s'.",
				serviceName, schemaName, newName));

		IStorageProvider storage = getStorageProvider();
		String rootUri = getRootUri();

		Link oldLink = Link.Schema(storage, rootUri, serviceName, schemaName);
		Link newLink = Link.Schema(storage, rootUri, serviceName, newName);

		RenameResult rr = new RenameResult();
		rr.setOldLink(oldLink);
		rr.setNewLink(newLink);
		return rr;

	}

	public RenameResult renameConfig(String serviceName, String configName,
			String newName) {
		forbidEmptyName(serviceName);
		forbidEmptyName(configName);
		forbidEmptyName(newName);
		
		validateServiceOrResourceName(newName);

		forbidReservedName(newName);
		getStorageProvider().renameConfig(serviceName, configName, newName);

		info(String.format("Serivce '%s' renamed config '%s' to '%s'.",
				serviceName, configName, newName));

		IStorageProvider storage = getStorageProvider();
		String rootUri = getRootUri();

		Link oldLink = Link.Config(storage, rootUri, serviceName, configName);
		Link newLink = Link.Config(storage, rootUri, serviceName, newName);

		RenameResult rr = new RenameResult();
		rr.setOldLink(oldLink);
		rr.setNewLink(newLink);
		return rr;

	}

	public ResourceList upload(String serviceName, String rel, String name,
			byte[] payload, String contentType) {
		validateResourceType(rel);

		ResourceList result = new ResourceList();

		if (contentType == null) {
			String charset = EncodingUtil.detectCharset(payload);
			contentType = EncodingUtil.charsetToTextContentType(
					Constants.ContentType.SubType.XML, charset);
			if (Constants.Rel.TEMPLATE.equals(rel)) {
				contentType = EncodingUtil.charsetToTextContentType(
						Constants.ContentType.SubType.PLAIN, charset);
			}
		}

		if (Constants.Rel.TEMPLATE.equals(rel)) {
			setTemplate(serviceName, payload, contentType);
			result.getResources().add(
					Link.Template(storage, getRootUri(), serviceName));
		}

		if (Constants.Rel.CONFIG.equals(rel)) {
			setConfig(serviceName, name, payload, contentType);
			result.getResources().add(
					Link.Config(storage, getRootUri(), serviceName, name));
		}

		if (Constants.Rel.SCHEMA.equals(rel)) {
			setSchema(serviceName, name, payload, contentType);
			result.getResources().add(
					Link.Schema(storage, getRootUri(), serviceName, name));
		}
		
		if (Constants.Rel.INI_SPLITTER.equals(rel)) {			
			splitInf(payload, contentType, name);
		}

		return result;
	}

	

	@Override
	public ServiceList splitInf(byte[] payload, String contentType,
			String serviceName) {

		createService(serviceName);

		try {

			StringSource ini = new StringSource(payload, contentType);
			String newline = EncodingUtil.detectNewline(ini.getString());
			
			List<String> iniText = ini.getLines(newline);

			List<IniSection> iniSections = IniSection.parse(iniText, false);

			Generator g = new Generator(serviceName);
			StringSource config = g.genXmlInstance(iniSections);
			StringSource schema = g.genXsdSchema(iniSections);
			StringSource template = g.genVelocityTemplate(ini, iniSections,
					newline);
			
			config = config.getCopyWithoutBOM();
			schema = schema.getCopyWithoutBOM();

			createService(serviceName);

			setSchema(serviceName, Constants.PRIMARY_SCHEMA_NAME,
					schema.getData(), schema.getContentType("xml"));

			setTemplate(serviceName, template.getData(),
					template.getContentType("plain"));

			setConfig(serviceName, "default.xml", config.getData(),
					config.getContentType("xml"));
			

			XmlUtil.ValidateXml(
					StringSourceUriPair.InputSource(schema, "schema"),
					StringSourceUriPair.InputSource(config, "config"));


		} catch (Exception ex) {
			throw new InfSplitterException(ex);
		}

		ServiceList result = 
				ServiceList.NewlyCreatedService(storage, getRootUri(), serviceName);
		
		return result;

	}
	

	@Override
	public String testConnectionViaGet() {
		return getVersion();
	}

	@Override
	public String testConnectionViaPost() {
		return getVersion();
	}

	@Override
	public String testConnectionViaDelete() {
		return getVersion();
	}

	
	private boolean enforceRequiredContentType = true;	
	public boolean getEnforceRequiredContentType() {
		return enforceRequiredContentType;
	}
	
	public void setEnforceRequiredContentType(boolean value) {
		enforceRequiredContentType = value;		
	}

}