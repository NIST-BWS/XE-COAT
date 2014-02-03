package gov.nist.ixe.templates.jaxb;

import gov.nist.ixe.templates.BuildUri;
import gov.nist.ixe.templates.Constants;
import gov.nist.ixe.templates.HistoryInfo;
import gov.nist.ixe.templates.IStorageProvider;

import java.util.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlRootElement(name="resourceHistory", namespace=Constants.NAMESPACE)
@XmlType(name="ResourceHistoryType", namespace=Constants.NAMESPACE)
public class ResourceHistory {
	public ResourceHistory() {}

	public ResourceHistory(IStorageProvider storage, String rootUri, String serviceName, String rel, String resourceName) {
		originalRel = rel;
		
		if (rel == Constants.Rel.TEMPLATE) {
			originalUri = BuildUri.getTemplateUri(rootUri, serviceName);
			for (HistoryInfo hi : storage.getTemplateHistoryInfo(serviceName)) {
				HistoricLink link = new HistoricLink();
				String timestamp = storage.fromDateToString(hi.getTimestampAsDate());
				link.setName(Constants.NAMED_HISTORY_NAME_PREFIX + Constants.DEFAULT_CONFIGURATION_NAME);
				link.setRel(Constants.Rel.TEMPLATE);
				link.setUri(BuildUri.getHistoricTemplateUri(rootUri, serviceName, timestamp));
				link.setServiceName(serviceName);
				link.setTimestamp(hi.getTimestampInMs());
				link.setSizeInBytes(hi.getSizeInBytes());
				historicLinks.add(link);
			}
		}

		if (rel == Constants.Rel.SCHEMA) {
			originalUri = BuildUri.getSchemaUri(rootUri, serviceName, resourceName);
			for (HistoryInfo hi : storage.getSchemaHistoryInfo(serviceName, resourceName)) {
				HistoricLink link = new HistoricLink();
				String timestamp = storage.fromDateToString(hi.getTimestampAsDate());

				link.setName(Constants.NAMED_HISTORY_NAME_PREFIX + resourceName);
				link.setRel(Constants.Rel.SCHEMA);
				link.setUri(BuildUri.getHistoricSchemaUri(rootUri, serviceName, resourceName, timestamp));
				link.setServiceName(serviceName);
				link.setTimestamp(hi.getTimestampInMs());
				link.setSizeInBytes(hi.getSizeInBytes());
				historicLinks.add(link);
			}
		}

		if (rel == Constants.Rel.CONFIG) {
			originalUri = BuildUri.getConfigUri(rootUri, serviceName, resourceName);
			for (HistoryInfo hi : storage.getConfigHistoryInfo(serviceName, resourceName)) {
				HistoricLink link = new HistoricLink();
				String timestamp = storage.fromDateToString(hi.getTimestampAsDate());

				link.setName(Constants.NAMED_HISTORY_NAME_PREFIX + resourceName);
				link.setRel(Constants.Rel.CONFIG);
				link.setUri(BuildUri.getHistoricConfigUri(rootUri, serviceName, resourceName, timestamp));
				link.setServiceName(serviceName);
				link.setTimestamp(hi.getTimestampInMs());
				link.setSizeInBytes(hi.getSizeInBytes());

				historicLinks.add(link);
			}

		}
	}

	@XmlElement(name="originalUri")
	public String getOriginalUri() { return originalUri; }
	public void setOriginalUri(String uri) { originalUri = uri; }
	
	@XmlElement(name="originalRel")
	public String getOriginalRel() { return originalRel; }
	public void setOriginalRel(String rel) { originalRel = rel; }
	
	@XmlElement(name="historicLink")
	public List<HistoricLink> getHistoricLinks() { sort(); return historicLinks; }

	public void sort() {
		Collections.sort(historicLinks, 
				new Comparator<HistoricLink>() { public int compare(HistoricLink l1, HistoricLink l2) {
					int result;
					int nameCompareResult = l1.getName().compareTo(l2.getName());  
					if (nameCompareResult == 0) {
						result = (int) (l2.getTimestamp() - l1.getTimestamp()); //l2.getTimestamp().compareTo(l1.getTimestamp());
					} else {
						result = nameCompareResult;
					}
					return result;
				}
		});
	}

	private List<HistoricLink> historicLinks = new ArrayList<HistoricLink>();
	private String originalUri;
	private String originalRel;

}
