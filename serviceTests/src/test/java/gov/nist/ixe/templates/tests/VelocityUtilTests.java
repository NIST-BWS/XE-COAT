package gov.nist.ixe.templates.tests;

import static org.junit.Assert.*;
import gov.nist.ixe.StringUtil;
import gov.nist.ixe.stringsource.StringSource;
import gov.nist.ixe.templates.VelocityUtil;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.junit.Test;

public class VelocityUtilTests {

	@Test
	public void VelocityTemplatesExecuteCorrectly() 
			throws ResourceNotFoundException, ParseErrorException, IOException {
		Integer[] data = {0,2,4,6,8,1,3,5,7,9};
		
		StringSource template = new StringSource(
				"#foreach ($v in $ROOT)\"$v\"#end".getBytes(),
				Charset.defaultCharset().name());
		
		StringSource output = VelocityUtil.processTemplate(template, "ROOT", data);
		
		StringBuilder builder = new StringBuilder();
		for (int i : data) { builder.append(StringUtil.quote(Integer.toString(i))); }
		assertEquals(builder.toString(), output.getString());
		
	}

}
