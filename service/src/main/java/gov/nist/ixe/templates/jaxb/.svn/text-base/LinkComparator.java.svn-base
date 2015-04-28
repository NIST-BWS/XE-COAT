package gov.nist.ixe.templates.jaxb;

import java.io.Serializable;
import java.util.Comparator;

public class LinkComparator implements Comparator<Link>, Serializable {


	@Override
	public int compare(Link l1, Link l2) {
		int result = l1.getServiceName().compareTo(l2.getServiceName());
		if (result == 0) {
			result = l1.getRel().compareTo(l2.getRel());
			if (result == 0) {
				result = l1.getName().compareTo(l2.getName());
			}
		}
		return result;
	}

	private static final long serialVersionUID = -8892444348149571482L;
	
}
