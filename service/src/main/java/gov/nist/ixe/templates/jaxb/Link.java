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

package gov.nist.ixe.templates.jaxb;

import gov.nist.ixe.templates.BuildUri;
import gov.nist.ixe.templates.Constants;
import gov.nist.ixe.templates.IStorageProvider;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name="LinkType", namespace=Constants.NAMESPACE)
public class Link {
	
	public Link() {
		
	}
	

	@XmlElement(name="serviceName", namespace=Constants.NAMESPACE) private String serviceName;
	@XmlElement(name="rel", namespace=Constants.NAMESPACE) private String rel;
	@XmlElement(name="name", namespace=Constants.NAMESPACE) private String name;
	@XmlElement(name="uri", namespace=Constants.NAMESPACE) private String uri;
	@XmlElement(name="historyUri", namespace=Constants.NAMESPACE) private String historyUri;
	@XmlElement(name="renameUri", namespace=Constants.NAMESPACE) private String renameUri;
	@XmlElement(name="uploadUri", namespace=Constants.NAMESPACE) private String uploadUri;
	@XmlElement(name="isDeletable", namespace=Constants.NAMESPACE) private Boolean isDeletable;
	
	public String getUri() {
		return this.uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	public String getRel() {
		return this.rel;
	}
	public void setRel(String rel) {
		this.rel = rel;
	}
	
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getHistoryUri() {
		return this.historyUri;
	}
	public void setHistoryUri(String historyUri) {
		this.historyUri = historyUri;
	}
	
	public String getRenameUri() {
		return this.renameUri;
	}
	public void setRenameUri(String renameUri) {
		this.renameUri = renameUri;
	}
	
	public String getUploadUri() {
		return this.uploadUri;
	}
	public void setUploadUri(String uploadUri) {
		this.uploadUri = uploadUri;
	}
	
//	public String getConfigNameSuggestUri() { 
//		return this.configNameSuggestUri;
//	}
//	public void setConfigNameSuggestUri(String configNameSuggestUri) {
//		this.configNameSuggestUri = configNameSuggestUri;
//	}
	public String getServiceName() { 
		return this.serviceName; 
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName; 
	}
	public Boolean getIsDeletable() {
		return this.isDeletable; 
	}
	public void setIsDeletable(Boolean isDeletable) { 
		this.isDeletable = isDeletable;
	}
/*
	public Integer getDocumentResourceCount() {
		return this.documentResourceCount; 
	}
	public void setDocumentResourceCount(Integer documentResourceCount) {
		this.documentResourceCount = documentResourceCount; 
	}
*/
	
	public static Link Schema(IStorageProvider storage, String rootUri, String serviceName, String schemaName) {
		Link result = new Link();
		result.setServiceName(serviceName);
		result.setRel(Constants.Rel.SCHEMA);
		result.setName(schemaName);
		result.setUri(BuildUri.getSchemaUri(rootUri, serviceName, schemaName));
		result.setRenameUri(BuildUri.getRenameSchemaUri(rootUri, serviceName, schemaName));
		if (storage.supportsHistory()) {
			result.setHistoryUri(BuildUri.getSchemaHistoryUri(rootUri, serviceName, schemaName));
		}
		result.setIsDeletable(true);
		return result;
		
	}
	
	public static Link Config(IStorageProvider storage, String rootUri, String serviceName, String configName) {
		Link result = new Link();
		result.setServiceName(serviceName);
		result.setRel(Constants.Rel.CONFIG);
		result.setName(configName);
		result.setUri(BuildUri.getConfigUri(rootUri, serviceName, configName));
		result.setRenameUri(BuildUri.getRenameConfigUri(rootUri, serviceName, configName));
		if (storage.supportsHistory()) {
			result.setHistoryUri(BuildUri.getConfigHistoryUri(rootUri, serviceName, configName));
		}
		result.setIsDeletable(true);
		return result;
	}
	
	public static Link Process(IStorageProvider storage, String rootUri, String serviceName) {
		Link result = new Link();
		result.setServiceName(serviceName);
		result.setRel(Constants.Rel.PROCESS);
		result.setName(Constants.PROCESS_RESOURCE_NAME);
		result.setUri(BuildUri.getProcessUri(rootUri, serviceName));
		return result;	
	}
	
	public static Link ProcessPayload(IStorageProvider storage, String rootUri, String serviceName) {
		Link result = new Link();
		result.setServiceName(serviceName);
		result.setRel(Constants.Rel.PROCESS);
		result.setName(Constants.PROCESS_RESOURCE_NAME);
		result.setUri(BuildUri.getProcessPayloadUri(rootUri, serviceName));
		return result;	
	}
	
	public static Link NamedProcess(IStorageProvider storage, String rootUri, String serviceName, String configName) {
		Link result = new Link();
		result.setServiceName(serviceName);
		result.setRel(Constants.Rel.PROCESS);
		result.setName(Constants.NAMED_PROCESS_NAME_PREFIX + configName);
		result.setUri(BuildUri.getNamedProcessUri(rootUri, serviceName, configName));
		return result;
	}
	
	
	public static Link Template(IStorageProvider storage, String rootUri, String serviceName) {
		Link result = new Link();
		result.setServiceName(serviceName);
		result.setRel(Constants.Rel.TEMPLATE);
		result.setName(Constants.TEMPLATE_RESOURCE_NAME);
		result.setUri(BuildUri.getTemplateUri(rootUri, serviceName));
		if (storage.supportsHistory()) {
			result.setHistoryUri(BuildUri.getTemplateHistoryUri(rootUri, serviceName));
		}
		result.setIsDeletable(true);	
		return result;
	
	}
	
	public static Link Service(IStorageProvider storage, String rootUri, String serviceName) {
		Link result = new Link();
		
		result.setRel(Constants.Rel.SERVICE);
		result.setName(serviceName);
		result.setServiceName(serviceName);
		result.setUri(BuildUri.getServiceUri(rootUri, serviceName));
		result.setRenameUri(BuildUri.getRenameServiceUri(rootUri, serviceName));
		result.setUploadUri(BuildUri.getUploadUri(rootUri, serviceName));
		if (storage.hasService(serviceName)) {
			result.setIsDeletable(storage.isServiceDeletable(serviceName));
		}
		return result;
	}
		
}
