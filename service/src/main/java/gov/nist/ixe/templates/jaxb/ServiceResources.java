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

import gov.nist.ixe.templates.Constants;
import gov.nist.ixe.templates.IStorageProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "serviceResources", namespace = Constants.NAMESPACE)
@XmlType(name = "ServiceResourcesType", namespace = Constants.NAMESPACE)
public class ServiceResources extends ResourceList {

	public ServiceResources() {
	}

	public ServiceResources(IStorageProvider storage, String rootUri,
			String serviceName) {

		boolean hasPrimarySchema = false;
		String[] schemaNames = storage.getSchemaNames(serviceName);
		for (String name : schemaNames) {
			if (name.equals(Constants.PRIMARY_SCHEMA_NAME)) {
				hasPrimarySchema = true;
				break;
			}
		}

		boolean hasTemplate = storage.hasTemplate(serviceName);
		if (hasTemplate) {
			resources.add(Link.template(storage, rootUri, serviceName));
		}

		// Populate 'PROCESS' links
		//
		boolean hasDefaultConfig = false;
		String[] configNames = storage.getConfigNames(serviceName);
		for (String name : configNames) {
			if (name.equals(Constants.DEFAULT_CONFIGURATION_NAME)) {
				hasDefaultConfig = true;
				break;
			}
		}
		if (hasTemplate && hasPrimarySchema && hasDefaultConfig) {

			resources.add(Link.process(storage, rootUri, serviceName));
			for (String configName : storage.getConfigNames(serviceName)) {
				resources.add(Link.namedProcess(storage, rootUri, serviceName,
						configName));
			}

		}

		for (String schemaName : storage.getSchemaNames(serviceName)) {
			resources.add(Link
					.schema(storage, rootUri, serviceName, schemaName));
		}

		for (String configName : storage.getConfigNames(serviceName)) {
			resources.add(Link
					.config(storage, rootUri, serviceName, configName));
		}

	}

	public List<Link> getSchemaLinks() {
		return getFilteredLinks(Constants.Rel.SCHEMA);
	}

	public List<Link> getConfigLinks() {
		return getFilteredLinks(Constants.Rel.CONFIG);
	}

	public List<Link> getProcessLinks() {
		return getFilteredLinks(Constants.Rel.PROCESS);
	}

	public boolean hasConfig(String configName) {
		List<Link> links = getFilteredLinks(Constants.Rel.CONFIG);
		for (Link link : links) {
			if (link.getName().equals(configName))
				return true;
		}
		return false;
	}

	public List<Link> getTemplateLinks() {
		return getFilteredLinks(Constants.Rel.TEMPLATE);
	}

	public Link getPrimarySchemaLink() {
		Link result = null;
		for (Link link : getSchemaLinks()) {
			if (Constants.PRIMARY_SCHEMA_NAME.equals(link.getName())) {
				result = link;
				break;
			}
		}
		return result;
	}

	private List<Link> getFilteredLinks(String rel) {
		sort();
		List<Link> links = new ArrayList<Link>();
		for (Link link : resources) {
			if (link.getRel().equalsIgnoreCase(rel)) {
				links.add(link);
			}
		}
		return links;
	}

	private Map<String, Link> nameRelToLinkMap = null; // maps name+rel to link

	public Link getLinkByNameAndRel(String name, String rel) {
		Link result = null;
		if (nameRelToLinkMap == null) {
			nameRelToLinkMap = new TreeMap<String, Link>();
			for (Link link : resources) {
				nameRelToLinkMap.put(link.getName() + link.getRel(), link);
			}
		}
		String key = name + rel;
		if (nameRelToLinkMap.containsKey(key)) {
			result = nameRelToLinkMap.get(key);
		}
		return result;
	}

}