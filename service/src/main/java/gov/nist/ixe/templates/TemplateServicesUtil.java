package gov.nist.ixe.templates;

import gov.nist.ixe.stringsource.StringSourceConverters;
import gov.nist.ixe.stringsource.StringSource;
import gov.nist.ixe.templates.jaxb.ResourceList;

public class TemplateServicesUtil {

	public static void setConfig(ITemplateServices ts, String serviceName, String configName, StringSource config) {
		ts.setConfig(serviceName, configName, config.getData(), config.getContentType("xml"));
	}

	public static void setSchema(ITemplateServices ts, String serviceName, String schemaName, StringSource schema) {
		ts.setSchema(serviceName, schemaName, schema.getData(), schema.getContentType("xml"));
	}

	public static void setTemplate(ITemplateServices ts, String serviceName,StringSource template) {
		ts.setTemplate(serviceName, template.getData(), template.getContentType("plain"));
	}

	public static StringSource getConfig(ITemplateServices ts, String serviceName, String configName) {
		return StringSourceConverters
				.fromResponse(ts.getConfig(serviceName, configName));
	}

	public static StringSource getSchema(ITemplateServices ts, String serviceName, String schemaName) {
		return StringSourceConverters
				.fromResponse(ts.getSchema(serviceName, schemaName));
	}

	public static StringSource getTemplate(ITemplateServices ts, String serviceName) {
		return StringSourceConverters
				.fromResponse(ts.getTemplate(serviceName));
	}

	public static StringSource processTemplate(ITemplateServices ts, String serviceName) {
		return StringSourceConverters
				.fromResponse(ts.processDefaultTemplate(serviceName));
	}

	public static StringSource processTemplate(ITemplateServices ts, String serviceName, String configName) {
		return StringSourceConverters
				.fromResponse(ts.processTemplateByName(serviceName, configName));
	}

	public static StringSource processTemplate(ITemplateServices ts, String serviceName, StringSource config) {
		return StringSourceConverters
				.fromResponse(ts.processTemplate(serviceName, config.getData(), config.getContentType("xml")));
	}

	public static ResourceList upload(ITemplateServices ts, String serviceName, String rel, String name, StringSource payload) {
		String contentSubType = getContentSubType(rel);
		return ts.upload(serviceName, rel, name, payload.getData(), payload.getContentType(contentSubType));
	}

	public static String getContentSubType(String rel) {
		String result = Constants.ContentType.SubType.PLAIN;
		if (rel.equals(Constants.Rel.SCHEMA) || rel.equals(Constants.Rel.CONFIG)) {
			result = Constants.ContentType.SubType.XML;
		}
		return result;
	}


}
