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

package gov.nist.ixe.templates;


public class Constants {
	
	public static final String NAMESPACE                  = "http://templates.xe.nist.gov";
	public static final String DEFAULT_CONFIGURATION_NAME = Atom.DEFAULT + "." + Atom.XML;
	public static final String PRIMARY_SCHEMA_NAME        = Atom.MAIN + "." + Atom.XSD;
	public static final String NAMED_PROCESS_NAME_PREFIX  = ResourceName.PROCESS + Atom.NAME_PREFIX_DELIMITER;
	public static final String NAMED_HISTORY_NAME_PREFIX  = Atom.HISTORY + Atom.NAME_PREFIX_DELIMITER;
	
	public static class ResourceName {		
		public static final String PROCESS  = Atom.PROCESS;
		public static final String RENAME   = Atom.RENAME;
		public static final String TEMPLATE = Atom.TEMPLATE;
		
	}
	
	static final String[] RESERVED_NAMES = {				
		ResourceName.PROCESS,
		Rel.CONFIG,
		Rel.CONFIG_HISTORY,
		Atom.HISTORY,		
		Atom.HISTORIC,
		Rel.INI_SPLITTER,
		Atom.NULL,
		Rel.PROCESS,
		Rel.SERVICE,
		Rel.SCHEMA,
		Rel.SCHEMA_HISTORY,
		Atom.TEST_CONNECTION,
		Rel.TEMPLATE,
		Rel.TEMPLATE_HISTORY,	
		Rel.UPLOAD,
		Atom.UNDEFINED,
		Atom.VERSION,
	};
	
	public static class HttpHeader {
		public static final String EXCEPTION_TYPE = "X-COAT-Exception-Type";
		public static final String EXCEPTION_MESSAGE = "X-COAT-Exception-Message";
		public static final String HISTORIC_REL_OF = "X-COAT-Historic-Rel-Of";
		public static final String HISTORIC_VERSION_OF = "X-COAT-Historic-Version-Of";		
		public static final String NEW_NAME = "X-COAT-New-Name";
		public static final String OLD_NAME = "X-COAT-Old-Name";
		public static final String RESOURCE_NAME = "X-COAT-ResourceName";
		public static final String REL = "X-COAT-Rel";
		public static final String SERVER = "Server";
		public static final String SERVICE_NAME = "X-COAT-ServiceName";
		public static final String UPLOADED_REL = "X-COAT-Uploaded-Rel";
	}
	
	static final String[] CUSTOM_HTTP_HEADERS = {
		HttpHeader.EXCEPTION_TYPE,
		HttpHeader.EXCEPTION_MESSAGE,
		HttpHeader.HISTORIC_REL_OF,
		HttpHeader.HISTORIC_VERSION_OF,		
		HttpHeader.NEW_NAME,
		HttpHeader.OLD_NAME,
		HttpHeader.RESOURCE_NAME,
		HttpHeader.REL,
		HttpHeader.SERVICE_NAME,
		HttpHeader.UPLOADED_REL,
	};

	
	public static class Rel {
		
		private static String compoundRel(String rel1, String rel2) {
			return rel1 + Atom.COMPOUND_RESOURCE_DELIMITER + rel2;
		}
		
		public static final String CONFIG            = Atom.CONFIG;
		public static final String CONFIG_HISTORY    = compoundRel(Atom.HISTORY, Rel.CONFIG);
		public static final String CONFIG_RENAMER 	 = compoundRel(Atom.RENAMER, Rel.CONFIG); 
		
		public static final String HISTORIC_CONFIG   = compoundRel(Atom.HISTORIC, Rel.CONFIG);
		public static final String HISTORIC_SCHEMA   = compoundRel(Atom.HISTORIC, Rel.SCHEMA);
		public static final String HISTORIC_TEMPLATE = compoundRel(Atom.HISTORIC, Rel.TEMPLATE);
		
		public static final String INI_SPLITTER      = compoundRel(Atom.SPLITTER, Atom.INI); 
		
		public static final String PROCESS           = Atom.PROCESS;
		
		public static final String SCHEMA            = Atom.SCHEMA;
		public static final String SCHEMA_HISTORY    = compoundRel(Atom.HISTORY, Rel.SCHEMA);
		public static final String SCHEMA_RENAMER    = compoundRel(Atom.RENAMER, Rel.SCHEMA);
		
		public static final String SERVICE           = Atom.SERVICE;
		public static final String SERVICE_LIST      = Atom.SERVICE_LIST;
		
		public static final String TEST_CONNECTION   = Atom.TEST_CONNECTION;
		
		public static final String TEMPLATE          = Atom.TEMPLATE;
		public static final String TEMPLATE_HISTORY  = compoundRel(Atom.HISTORY, Rel.TEMPLATE);

		public static final String UPLOAD            = Atom.UPLOAD;
		
		public static final String VERSION 			 = Atom.VERSION;
		public static final String SERVICE_RENAMER   = compoundRel(Atom.RENAMER, Rel.SERVICE);
	}
	
	 static final String[] DOCUMENT_RESOURCE_TYPES = {
		Rel.CONFIG, Rel.SCHEMA, Rel.TEMPLATE, Rel.INI_SPLITTER
	};
	

	
	public static class ContentType {
		 		
		public static class SubType {
			public static final String PLAIN = Atom.PLAIN;
			public static final String XML = Atom.XML;
			
		}
	}
	
	
	public static class Uri {
		
		public static class Param {
			public static final String CONFIG        = Atom.CONFIG;
			public static final String CONFIG_NAME   = Atom.CONFIG_NAME;
			public static final String CONTENT_TYPE  = Atom.CONTENT_TYPE;
			public static final String NAME          = Atom.NAME;
			public static final String NEW_NAME      = Atom.NEW_NAME;
			public static final String REL           = Atom.REL;
			public static final String SERVICE_NAME  = Atom.SERVICE_NAME;
			public static final String SCHEMA_NAME   = Atom.SCHEMA_NAME;			
			public static final String TEMPLATE_NAME = Atom.TEMPLATE_NAME;
			public static final String TIMESTAMP     = Atom.TIMESTAMP;
		}
		
		public  static final String SERVICE_LIST = "/";
		public  static final String VERSION = Atom.VERSION;
		public  static final String TEST_CONNECTION = Atom.TEST_CONNECTION;
		public  static final String NAMED_PROCESS = "{" + Param.SERVICE_NAME + "}/" + Atom.PROCESS + "/{" + Param.CONFIG_NAME + "}";	
		
		public  static final String INI_SPLITTER = Atom.SPLITTER + "/" + Atom.INI; 
				
		public  static final String SERVICE  = "{" + Param.SERVICE_NAME + "}";
		
		private static final String SERVICE_NAME_PREFIX = SERVICE + "/";
		 
		public  static final String CONFIG        = SERVICE_NAME_PREFIX + Atom.CONFIG + "/{" + Param.CONFIG_NAME + "}";
		public  static final String PROCESS       = SERVICE_NAME_PREFIX + Atom.PROCESS;
		public  static final String SCHEMA        = SERVICE_NAME_PREFIX + Atom.SCHEMA + "/{" + Param.SCHEMA_NAME + "}";
		public  static final String TEMPLATE      = SERVICE_NAME_PREFIX + Atom.TEMPLATE;
		public  static final String UPLOAD        = SERVICE_NAME_PREFIX + Atom.UPLOAD;
		
		public  static final String TEMPLATE_HISTORY = NAMED_HISTORY_NAME_PREFIX + Uri.TEMPLATE; 
		public  static final String SCHEMA_HISTORY   = NAMED_HISTORY_NAME_PREFIX + Uri.SCHEMA;
		public  static final String CONFIG_HISTORY   = NAMED_HISTORY_NAME_PREFIX + Uri.CONFIG;
		
		private static final String HISTORIC_PREFIX   = Atom.HISTORIC + "/{" + Param.TIMESTAMP + "}/";
		public  static final String HISTORIC_TEMPLATE = HISTORIC_PREFIX  + Uri.TEMPLATE;
		public  static final String HISTORIC_CONFIG   = HISTORIC_PREFIX  + Uri.CONFIG;
		public  static final String HISTORIC_SCHEMA   = HISTORIC_PREFIX  + Uri.SCHEMA;
		
		private static final String RENAME_PREFIX  = Atom.RENAME + "/";
		public  static final String RENAME_SCHEMA  = RENAME_PREFIX + Uri.SCHEMA;
		public  static final String RENAME_CONFIG  = RENAME_PREFIX + Uri.CONFIG;
		public  static final String RENAME_SERVICE = RENAME_PREFIX + Atom.SERVICE + "/" + Uri.SERVICE;
	
		public  static final String CLONE_SERVICE = Atom.CLONE_SERVICE + "/" + Uri.SERVICE;
		
	}
	
	private static final class Atom {
		
		private static final String NAME_SUFFIX   = "Name";
		private static final String SERVICE_SUFFIX = "Service";
		
		private static final String CLONE			= "clone";
		private static final String CLONE_SERVICE   = CLONE + SERVICE_SUFFIX;
		private static final String CONFIG          = "config";
		private static final String CONFIG_NAME     = CONFIG + NAME_SUFFIX;
		private static final String CONTENT_TYPE    = "Content-Type";
		private static final String DEFAULT         = "default";
		private static final String HISTORY         = "history";
		private static final String HISTORIC        = "historic";		
		private static final String INI             = "ini";
		private static final String MAIN            = "main";
		private static final String NAME            = "name";
		private static final String NEW_NAME        = "new" + NAME_SUFFIX;
		private static final String NULL			= "null";
		private static final String PROCESS         = "process";
		private static final String PLAIN           = "plain";
		private static final String REL             = "rel";
		private static final String RENAME          = "rename";
		private static final String RENAMER         = "renamer";
		private static final String SPLITTER        = "splitter";
		private static final String SCHEMA          = "schema";
		private static final String SCHEMA_NAME     = SCHEMA + NAME_SUFFIX;
		private static final String SERVICE         = "service";
		private static final String SERVICE_LIST    = "serviceList";
		private static final String SERVICE_NAME    = SERVICE + NAME_SUFFIX;
		private static final String TEST_CONNECTION = "test-connection";
		private static final String TEMPLATE        = "template";
		private static final String TEMPLATE_NAME   = TEMPLATE + NAME_SUFFIX;
		private static final String TIMESTAMP       = "template";
		private static final String UNDEFINED       = "undefined";
		private static final String UPLOAD          = "upload";
		private static final String VERSION         = "version";
		private static final String XSD             = "xsd";
		private static final String XML             = "xml";
						
		private static final String NAME_PREFIX_DELIMITER = "/";
		private static final String COMPOUND_RESOURCE_DELIMITER = "/";

	}
	
}
