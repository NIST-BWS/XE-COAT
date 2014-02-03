package gov.nist.ixe.templates.ini;

import static gov.nist.ixe.Logging.trace;

import gov.nist.ixe.StringUtil;
import gov.nist.ixe.stringsource.StringSource;
import gov.nist.ixe.templates.BuildUri;
import gov.nist.ixe.templates.VelocityUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Generator {

	public Generator(String rootElementNameToUseForMultisectionFiles) {
		trace();

		rootElementName = rootElementNameToUseForMultisectionFiles;
		targetNamespace = BuildUri.getNamespaceUri(rootElementName, (new Date().getTime()));
	}

	public static final String INPUT_ROOT_MODEL_NAME = "m";
	public static final String OUTPUT_ROOT_MODEL_NAME = "ROOT";

	
	private String rootElementName;
	private String targetNamespace;

	public String getRootElementName() {
		trace();
		return rootElementName;
	}

	public StringSource genXsdSchema(List<IniSection> iniSections) throws IOException {
		trace();
		return processXmlTemplate(iniSections, rootElementName, "/templates/ini-schema.vm", targetNamespace);
	}

	public StringSource genXmlInstance(List<IniSection> iniSections) throws IOException {
		trace();
		return processXmlTemplate(iniSections, rootElementName, "/templates/ini-instance.vm", targetNamespace);
	}

	public StringSource genVelocityTemplate(StringSource sourceText, List<IniSection> iniSections, String newline) 
			throws UnsupportedEncodingException {
		trace();

		// Copy the input text
		List<String> output = new ArrayList<String>();

		for (String line : sourceText.getLines(newline)) { 
			output.add(line);
		}

		for (IniSection section : iniSections) {
			for (String keyName : section.getKeyNames()) {
				KeyValueLine kvl = section.getKeyValuesLine(keyName);
				int lineNumber = kvl.getLineNumber();
				String originalLine = kvl.getOriginalLine();

				String startOfLine = originalLine.substring(0, kvl.getValuesStart());
				String endOfLine = originalLine.substring(kvl.getCommentStart(), kvl.getCommentEnd());
				
				String vv = getVelocityVariable(iniSections, section, rootElementName, kvl.getKey());
				
				// Note we add a trailing space so that the comment is not directly against the variable
				if (!("".equals(endOfLine.trim()))) {
					endOfLine = " " + endOfLine;
				}
				String line = startOfLine + vv + endOfLine;
				
				// If the line ends with a velocity foreach macro, then place a dummy
				// marker to prevent Velocity from swallowing up the newlines (for example, when
				// two lines in a row have a macro).
				//
				if (vv.startsWith("#foreach") && line.endsWith(vv)) { 
					line = line + "$eol";
				}

				output.set(lineNumber, line);
			}
		}
		
		String charset = sourceText.getCharset();
		StringSource result = new StringSource(StringUtil.flatten(output, newline).getBytes(charset), charset);
		
		//StringSource result = new StringSource(Util.flatten(output, newline).getBytes(), sourceText.getCharset());

		return result;
	}

	private static String getVelocityVariable(List<IniSection> iniSections, IniSection section, String rootElementName, String keyName) {
		trace();

		String result = "";
		String rootPart = "$!{" + OUTPUT_ROOT_MODEL_NAME + ".";
		String sectionPart = "";

		if (iniSections.size() > 1) {
			sectionPart = XmlModel.ElementName.fromSectionName(section.getName()) + ".";
		}

		String keyPart = XmlModel.cleanupVariableName(keyName); 

		result = rootPart + sectionPart + keyPart + "}";
		KeyValueLine kvl = section.getKeyValuesLine(keyName);
		 
		Values values = section.getValues(keyName);
		String leftDelim = values.getCommonLeftDelimiter(false);
		String rightDelim = values.getCommonRightDelimiter(false);
		
		if (values.size() == 1) {		
			result = leftDelim + result + rightDelim;
		}

		if (values.size() > 1) {
			String delim = kvl.getDelimiter();
			String forEachV = "$!value";
			String inlineV = forEachV;
			String forEachSource = rootPart + sectionPart + keyPart + ".Element}";

			inlineV = leftDelim + forEachV + rightDelim;

			result = "#foreach(" + forEachV + " in " + forEachSource + ")" + inlineV + "#if($velocityHasNext)" + delim + "#end#end";		
		}

		return result;
	}

	private StringSource processXmlTemplate(List<IniSection> iniSections, 
			String rootElementName, 
			String templateName, 
			String namespace) throws IOException {
		trace();

		InputStream is;

		// Get the template charset
		is = Generator.class.getResourceAsStream(templateName);
		StringSource template = StringSource.infer(is);
			
		
		return VelocityUtil.processTemplate(template, INPUT_ROOT_MODEL_NAME, 
				new XmlModel(iniSections, rootElementName, namespace));
	}
}