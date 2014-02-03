package gov.nist.ixe.templates.jaxb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import gov.nist.ixe.templates.Constants;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="resourceList", namespace=Constants.NAMESPACE)
@XmlType(name="ResourceListType", namespace=Constants.NAMESPACE)
public class ResourceList {
	
	public ResourceList() {}
	
	@XmlElement(name="link")
	public List<Link> getResources() { sort(); return this.resources; }
	
	protected List<Link> resources = new ArrayList<Link>();
	
	public void sort() {
		Collections.sort(this.resources, 
				new Comparator<Link>() { public int compare(Link l1, Link l2)
				{ return l1.getName().compareTo(l2.getName());}
		});
	}

}
