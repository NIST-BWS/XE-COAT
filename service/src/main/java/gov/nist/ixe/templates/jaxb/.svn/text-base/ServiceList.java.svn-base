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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="serviceList", namespace=Constants.NAMESPACE)
@XmlType(name="ServiceListType", namespace=Constants.NAMESPACE)
public class ServiceList {

	public ServiceList() {}
	
	public ServiceList(IStorageProvider storage, String rootUri) {
		for (String serviceName : storage.getServiceNames()) {
			services.add(Link.service(storage, rootUri, serviceName));
		}
		iniSplitter = Link.iniSplitter(storage, rootUri);
	}
	
	public static ServiceList newlyCreatedService
	(IStorageProvider storage, String rootUri, String serviceName) {
		ServiceList result = new ServiceList();
		result.services.add(Link.service(storage, rootUri, serviceName));
		return result;
	}
	
	@XmlElement(name="link")
	public List<Link> getServices() { sort(); return services; }
	
	public int servicesSize() { return services.size(); };
	public Link getService(int index) { return services.get(index); }
	
	private List<Link> services = new ArrayList<Link>();
	
	public void sort() {
		Collections.sort(services, 
				new Comparator<Link>() { public int compare(Link l1, Link l2)
				{ return l1.getName().compareTo(l2.getName());}
		});
	}
	
	private Link iniSplitter;
	@XmlElement(name="iniSplitter") 
	public Link getIniSplitter() { 
		return iniSplitter;
	}

	public void setIniSplitter(Link link) {
		iniSplitter = link;
	}
}
