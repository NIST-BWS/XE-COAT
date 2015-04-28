package gov.nist.ixe.templates;

import gov.nist.ixe.stringsource.StringSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;


	
public class Version {
	
	public static String getVersion()  {
		String result = "0.0.0.$SVN_REPOSITORY";
		InputStream is = TemplateServices.class.getResourceAsStream("/VERSION");
		try { 
			try { 
				result = StringSource.infer(is).getString();
			} finally {is.close();}
		} catch (UnsupportedEncodingException e) {
			// Do nothing
		} catch (IOException e) {
			// Do nothing
		}
		return result;
	}
	
	public static void main(String... args) {		
		System.out.println(getVersion());
	}
}
