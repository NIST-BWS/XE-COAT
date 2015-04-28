package gov.nist.ixe.templates.jaxb;


import java.util.ArrayList;
import java.util.List;

import gov.nist.ixe.templates.Constants;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="renameResult", namespace=Constants.NAMESPACE)
@XmlType(name="RenameResultType", namespace=Constants.NAMESPACE)
public class RenameResult {
	
	public RenameResult() {}
	
	private Link oldLink;
	private Link newLink;
	
	@XmlElement(name="oldLink") 
	public Link getOldLink() {
		return oldLink;
	}
	public void setOldLink(Link oldLink) {
		this.oldLink = oldLink;
	}
	
	@XmlElement(name="newLink") 
	public Link getNewLink() {
		return newLink;
	}
	public void setNewLink(Link newLink) {
		this.newLink = newLink;
	}
	
	@XmlElement(name="renamedResource")
	public List<RenameResult> getRenamedResources() { return this.renamedResources; }
	
	protected List<RenameResult> renamedResources = new ArrayList<RenameResult>();
	
}
