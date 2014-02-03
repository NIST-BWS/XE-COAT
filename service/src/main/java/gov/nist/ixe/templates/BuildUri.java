 /*----------------------------------------------------------------------------------------------------+
 |                             National Institute of Standards and Technology                          |
 |                                        Biometric Clients Lab                                        |
 +-----------------------------------------------------------------------------------------------------+
  File author(s):
  	   Ross J. Micheals (ross.micheals@nist.gov)
        
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

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.UriBuilder;

public class BuildUri {
	
	
	
	// ---
	// Core operation URIs
	//
	
	public static String getVersionUri(String rootUri) {
		UriBuilder builder = UriBuilder.fromUri(rootUri);
		builder.path(Constants.Uri.VERSION);
		return builder.build().toString();
	}
	
	public static String getTestConnectionUri(String rootUri) {
		UriBuilder builder = UriBuilder.fromUri(rootUri);
		builder.path(Constants.Uri.TEST_CONNECTION);
		return builder.build().toString();
	}
	
	public static String getServiceUri(String rootUri, String serviceName) {
		UriBuilder builder = UriBuilder.fromUri(rootUri);
		builder.path(Constants.Uri.SERVICE);
		Map<String,String> args = new HashMap<String,String>();
		args.put(Constants.Uri.Param.SERVICE_NAME, serviceName);
		return builder.buildFromMap(args).toString();
	}
	
	public static String getTemplateUri(String rootUri, String serviceName) {
		UriBuilder builder = UriBuilder.fromUri(rootUri);
		builder.path(Constants.Uri.TEMPLATE);
		Map<String,String> args = new HashMap<String,String>();
		args.put(Constants.Uri.Param.SERVICE_NAME, serviceName);
		return builder.buildFromMap(args).toString();
	}
		
	public static String getProcessUri(String rootUri, String serviceName) {
		UriBuilder builder = UriBuilder.fromUri(rootUri);
		builder.path(Constants.Uri.PROCESS);
		Map<String,String> args = new HashMap<String,String>();
		args.put(Constants.Uri.Param.SERVICE_NAME, serviceName);
		return builder.buildFromMap(args).toString();
	}
	
	public static String getProcessPayloadUri(String rootUri, String serviceName) {
		UriBuilder builder = UriBuilder.fromUri(rootUri);
		builder.path(Constants.Uri.PROCESS);
		Map<String,String> args = new HashMap<String,String>();
		args.put(Constants.Uri.Param.SERVICE_NAME, serviceName);
		builder.scheme("post");
		return builder.buildFromMap(args).toString();
	}
	
	public static String getNamedProcessUri(String rootUri, String serviceName, String configName) {
		UriBuilder builder = UriBuilder.fromUri(rootUri);
		builder.path(Constants.Uri.NAMED_PROCESS);
		Map<String,String> args = new HashMap<String,String>();
		args.put(Constants.Uri.Param.SERVICE_NAME, serviceName);
		args.put(Constants.Uri.Param.CONFIG_NAME, configName);
		return builder.buildFromMap(args).toString();
	}

		
	public static String getSchemaUri(String rootUri, String serviceName, String schemaName) {
		UriBuilder builder = UriBuilder.fromUri(rootUri);
		builder.path(Constants.Uri.SCHEMA);
		Map<String,String> args = new HashMap<String,String>();
		args.put(Constants.Uri.Param.SERVICE_NAME, serviceName);
		args.put(Constants.Uri.Param.SCHEMA_NAME, schemaName);
		return builder.buildFromMap(args).toString();
	}

	public static String getConfigUri(String rootUri, String serviceName, String configName) {
		UriBuilder builder = UriBuilder.fromUri(rootUri);
		builder.path(Constants.Uri.CONFIG);
		Map<String,String> args = new HashMap<String,String>();
		args.put(Constants.Uri.Param.SERVICE_NAME, serviceName);
		args.put(Constants.Uri.Param.CONFIG_NAME, configName);
		return builder.buildFromMap(args).toString();
	}
	


	
	
	
	// --- 
	// Rename URIs
	// ---
	
	public static String getRenameServiceUri(String rootUri, String serviceName) {
		UriBuilder builder = UriBuilder.fromUri(rootUri);
		builder.path(Constants.Uri.RENAME_SERVICE);
		Map<String,String> args = new HashMap<String,String>();
		args.put(Constants.Uri.Param.SERVICE_NAME, serviceName);
		return builder.buildFromMap(args).toString();
	}
	
	public static String getRenameServiceUri(String rootUri, String serviceName,String newName) {
		UriBuilder builder = UriBuilder.fromUri(rootUri);
		builder.path(Constants.Uri.RENAME_SERVICE);
		Map<String,String> args = new HashMap<String,String>();
		args.put(Constants.Uri.Param.SERVICE_NAME, serviceName);
		return builder.queryParam(Constants.Uri.Param.NEW_NAME, newName).buildFromMap(args).toString();
	}
	
	
	public static String getRenameSchemaUri(String rootUri, String serviceName, String schemaName) {
		UriBuilder builder = UriBuilder.fromUri(rootUri);
		builder.path(Constants.Uri.RENAME_SCHEMA);
		Map<String,String> args = new HashMap<String,String>();
		args.put(Constants.Uri.Param.SERVICE_NAME, serviceName);
		args.put(Constants.Uri.Param.SCHEMA_NAME, schemaName);
		return builder.buildFromMap(args).toString();
	}
	
	public static String getRenameSchemaUri(String rootUri, String serviceName, String schemaName, String newName) {
		UriBuilder builder = UriBuilder.fromUri(rootUri);
		builder.path(Constants.Uri.RENAME_SCHEMA);
		Map<String,String> args = new HashMap<String,String>();
		args.put(Constants.Uri.Param.SERVICE_NAME, serviceName);
		args.put(Constants.Uri.Param.SCHEMA_NAME, schemaName);
		return builder.queryParam(Constants.Uri.Param.NEW_NAME, newName).buildFromMap(args).toString();
	}
	
	public static String getRenameConfigUri(String rootUri, String serviceName, String configName) {
		UriBuilder builder = UriBuilder.fromUri(rootUri);
		builder.path(Constants.Uri.RENAME_CONFIG);
		Map<String,String> args = new HashMap<String,String>();
		args.put(Constants.Uri.Param.SERVICE_NAME, serviceName);
		args.put(Constants.Uri.Param.CONFIG_NAME, configName);
		return builder.buildFromMap(args).toString();
	}
	
	public static String getRenameConfigUri(String rootUri, String serviceName, String configName, String newName) {
		UriBuilder builder = UriBuilder.fromUri(rootUri);
		builder.path(Constants.Uri.RENAME_CONFIG);
		Map<String,String> args = new HashMap<String,String>();
		args.put(Constants.Uri.Param.SERVICE_NAME, serviceName);
		args.put(Constants.Uri.Param.CONFIG_NAME, configName);
		return builder.queryParam(Constants.Uri.Param.NEW_NAME, newName).buildFromMap(args).toString();
	}
	
	// ---
	// History & Historic URI builders
	// ---
	
	public static String getTemplateHistoryUri(String rootUri, String serviceName) {
		UriBuilder builder = UriBuilder.fromUri(rootUri);
		builder.path(Constants.Uri.TEMPLATE_HISTORY);
		Map<String,String> args = new HashMap<String,String>();
		args.put(Constants.Uri.Param.SERVICE_NAME, serviceName);
		return builder.buildFromMap(args).toString();
	}
	
	public static String getSchemaHistoryUri(String rootUri, String serviceName, String schemaName) {
		UriBuilder builder = UriBuilder.fromUri(rootUri);
		builder.path(Constants.Uri.SCHEMA_HISTORY);
		Map<String,String> args = new HashMap<String,String>();
		args.put(Constants.Uri.Param.SERVICE_NAME, serviceName);
		args.put(Constants.Uri.Param.SCHEMA_NAME, schemaName);
		return builder.buildFromMap(args).toString();
	}
	
	public static String getConfigHistoryUri(String rootUri, String serviceName, String configName) {
		UriBuilder builder = UriBuilder.fromUri(rootUri);
		builder.path(Constants.Uri.CONFIG_HISTORY);
		Map<String,String> args = new HashMap<String,String>();
		args.put(Constants.Uri.Param.SERVICE_NAME, serviceName);
		args.put(Constants.Uri.Param.CONFIG_NAME, configName);
		return builder.buildFromMap(args).toString();
	}
	
	public static String getHistoricTemplateUri(String rootUri, String serviceName, String timestamp) {
		UriBuilder builder = UriBuilder.fromUri(rootUri);
		builder.path(Constants.Uri.HISTORIC_TEMPLATE);
		Map<String,String> args = new HashMap<String,String>();
		args.put(Constants.Uri.Param.SERVICE_NAME, serviceName);
		args.put(Constants.Uri.Param.TIMESTAMP, timestamp);
		return builder.buildFromMap(args).toString();
	}
	
	public static String getHistoricSchemaUri(String rootUri, String serviceName, String schemaName, String timestamp) {
		UriBuilder builder = UriBuilder.fromUri(rootUri);
		builder.path(Constants.Uri.HISTORIC_SCHEMA);
		Map<String,String> args = new HashMap<String,String>();
		args.put(Constants.Uri.Param.SERVICE_NAME, serviceName);
		args.put(Constants.Uri.Param.SCHEMA_NAME, schemaName);
		args.put(Constants.Uri.Param.TIMESTAMP, timestamp);
		return builder.buildFromMap(args).toString();
	}
	
	public static String getHistoricConfigUri(String rootUri, String serviceName, String configName, String timestamp) {
		UriBuilder builder = UriBuilder.fromUri(rootUri);
		builder.path(Constants.Uri.HISTORIC_CONFIG);
		Map<String,String> args = new HashMap<String,String>();
		args.put(Constants.Uri.Param.SERVICE_NAME, serviceName);
		args.put(Constants.Uri.Param.CONFIG_NAME, configName);
		args.put(Constants.Uri.Param.TIMESTAMP, timestamp);
		return builder.buildFromMap(args).toString();
	}
	
	// ---
	// Upload 
	// ---
	
	public static String getUploadUri(String rootUri, String serviceName) {
		UriBuilder builder = UriBuilder.fromUri(rootUri);
		builder.path(Constants.Uri.UPLOAD);
		Map<String,String> args = new HashMap<String,String>();
		args.put(Constants.Uri.Param.SERVICE_NAME, serviceName);
		return builder.buildFromMap(args).toString();
	}
	
	public static String getUploadUri(String rootUri, String serviceName, String rel, String name) {
		UriBuilder builder = UriBuilder.fromUri(rootUri);
		builder.path(Constants.Uri.UPLOAD);
		Map<String,String> args = new HashMap<String,String>();
		args.put(Constants.Uri.Param.SERVICE_NAME, serviceName);
		return builder
				.queryParam(Constants.Uri.Param.REL, rel)
				.queryParam(Constants.Uri.Param.NAME, name)
				.buildFromMap(args).toString();
	}
	
	public static String getNamespaceUri(String rootElementName, Long milliseconds) {
		
		String rEN = "rootElementName";
		String ms = "milliseconds";
		UriBuilder builder = UriBuilder.fromUri(Constants.NAMESPACE);
		builder.path("{"+rEN+"}/{"+ms+"}");
		Map<String,String> args = new HashMap<String,String>();
		args.put(rEN, rootElementName);
		args.put(ms, Long.toString(milliseconds));
		return builder.buildFromMap(args).toString();
				
	}
	
	// ---
	// Splitters 
	// ---
	
	public static String getIniSplitterUri(String rootUri, String serviceName) {
		UriBuilder builder = UriBuilder.fromUri(rootUri);
		builder.path(Constants.Uri.INI_SPLITTER);
		Map<String,String> args = new HashMap<String,String>();
		args.put(Constants.Uri.Param.SERVICE_NAME, serviceName);
		return builder.queryParam(Constants.Uri.Param.SERVICE_NAME, serviceName).buildFromMap(args).toString();
		
	}
		

}
