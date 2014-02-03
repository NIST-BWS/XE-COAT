package gov.nist.ixe.templates.jaxb;

import gov.nist.ixe.templates.Constants;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name="HistoricLinkType", namespace=Constants.NAMESPACE)
public class HistoricLink extends Link {
	public HistoricLink() {
		
	}
		
	private long milliseconds;
	private long sizeInBytes;
	
	@XmlElement(name="milliseconds")
	public long getTimestamp() { return this.milliseconds; }
	public void setTimestamp(long timestamp) { this.milliseconds = timestamp; }
	
	
	@XmlElement(name="sizeInBytes")
	public long getSizeInBytes() { return this.sizeInBytes; }
	public void setSizeInBytes(long sizeInBytes) { this.sizeInBytes = sizeInBytes; }

	
	
}
