 /*----------------------------------------------------------------------------------------------------+
 |                             National Institute of Standards and Technology                          |
 |                                        Biometric Clients Lab                                        |
 +-----------------------------------------------------------------------------------------------------+
  File author(s):
  	   Ross J. Micheals (ross.micheals@nist.gov)
       Kevin Mangold (kevin.mangold@nist.gov)
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

// About the IXE Template Service
// --------------------------
//
// The template service uses HATEOAS --- i.e., clients should use the service hyperlinks
// as opposed to a URI convention.  
//
// A template service comprises:
//		- 0..1 template resource(s)
//	    - 0..n schema resources(s)
//	    - 0..n configuration resources(s)
//
// A template service that is operating correctly comprises:
//		- 1 template resource
//		- 1..n schema resources(s)
//		- 0..n configuration resource(s)
//
// Schemas are uniquely identified by a combination of the service name and the schema name.
//
// Configurations are uniquely identified by a combination of the service name and the 
// configuration name.
//
// Configuration resources can be examples, or may be operational configurations that are 
// stored in the service; the service has no inherent capability or features designed to 
// distinguish between the two.
//
// --------------------------
//
// The template service uses HATEOAS --- i.e., clients *should* use the service hyperlinks
// as opposed to a URI convention. This is not strictly necessary, however, since the hyperlinks
// are exposed via a regular pattern.;
//
// A template service comprises:
// 		- 0..1 template resource(s)
//	    - 0..n schema resources(s)
//	    - 0..n configuration resources(s)
//
// A template service that is operating correctly comprises:
//		- 1 template resource
//		- 1..n schema resources(s)
//		- 0..n configuration resource(s)
//
// Schemas are uniquely identified by a combination of the service name and the schema name.
//
// Configurations are uniquely identified by a combination of the service name and the 
// configuration name.
//
// Configuration resources can be examples, or may be operational configurations that are 
// stored in the service; the service has no inherent capability or features designed to 
// distinguish between the two.
//

import gov.nist.ixe.templates.exception.ResourceNotFoundException;
import gov.nist.ixe.templates.jaxb.RenameResult;
import gov.nist.ixe.templates.jaxb.ResourceHistory;
import gov.nist.ixe.templates.jaxb.ResourceList;
import gov.nist.ixe.templates.jaxb.ServiceList;
import gov.nist.ixe.templates.jaxb.ServiceResources;

import java.text.ParseException;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

public interface ITemplateServices {
	
	@GET
	@Produces("text/plain")
	@Path(Constants.Uri.VERSION)
	public String getVersion();
	
	@GET
	@Produces("text/plain")
	@Path(Constants.Uri.TEST_CONNECTION)
	public String testConnectionViaGet();
	
	@POST
	@Produces("text/plain")
	@Path(Constants.Uri.TEST_CONNECTION)
	public String testConnectionViaPost();
	
	@DELETE
	@Produces("text/plain")
	@Path(Constants.Uri.TEST_CONNECTION)
	public String testConnectionViaDelete();
	
	// ----
	// SERVICE_LIST / GET
	// ----
	// Calling GET on the 'service list' resource returns a list of services
	//
	@GET
	@Produces("text/xml")
	//@Path(Constants.Uri.SERVICE_LIST) 	
	public ServiceList getServiceList();
	
	// ----
	// SERVICE / GET
	// ----
	// Calling GET on the 'service' resource returns a list of service resources
	//
	@GET
	@Produces("text/xml")
	@Path(Constants.Uri.SERVICE)
	public ServiceResources getServiceResources(
			@PathParam(Constants.Uri.Param.SERVICE_NAME) String serviceName
			) throws ResourceNotFoundException;
	
	
	// ----
	// SERVICE / POST
	// ----
	// Calling POST on a 'service' resource creates a service with that name.
	//
	@POST
	@Path(Constants.Uri.SERVICE)
	public void createService(
			@PathParam(Constants.Uri.Param.SERVICE_NAME) String serviceName
			);
	
	// ----
	// SERVICE / DELETE
	// ----
	// Calling DELETE on a 'service' resource removes a service (if it has 
	// no resources).
	//
	@DELETE	
	@Path(Constants.Uri.SERVICE)
	public void deleteService(
			@PathParam(Constants.Uri.Param.SERVICE_NAME) String serviceName
			);
	
	// ----
	// PROCESS / POST
	// ----
	// Calling POST on the 'process' resource processes the template against the
	// posted XML
	//
	@POST
	@Path(Constants.Uri.PROCESS)
	public Response processTemplate(
			@PathParam(Constants.Uri.Param.SERVICE_NAME) String serviceName,	
			byte[] payload,
			@HeaderParam("Content-Type") String contentType
			);
	
	// ----
    // PROCESS / GET
	// ----
	// Calling GET on the 'process' resource processes the template against the
	// configuration default.xml
	//
	@GET
	@Path(Constants.Uri.PROCESS)
	public Response processDefaultTemplate(
				@PathParam(Constants.Uri.Param.SERVICE_NAME) String serviceName);
				

	// ----
    // named PROCESS / GET
	// ----
	// Calling GET on a named 'process' resource processes the template against the
	// the supplied name.
	//
	@GET
	@Path(Constants.Uri.NAMED_PROCESS)
	public Response processTemplateByName(
				@PathParam(Constants.Uri.Param.SERVICE_NAME) String serviceName,
				@PathParam(Constants.Uri.Param.CONFIG_NAME) String configName
				);
	
			
	// ----
	// TEMPLATE / GET
	// ----
	//
	// Calling GET on a 'template' resource returns the service's template.
	//
	@GET
	@Path(Constants.Uri.TEMPLATE)
	public Response getTemplate(
			@PathParam(Constants.Uri.Param.SERVICE_NAME) String serviceName
			);
	
	// ----
	// TEMPLATE / POST
	// ----
	//
	// Calling POST on a 'template' resources creates (or overwrites) the
	// a service's template.
	//
	@POST
	@Path(Constants.Uri.TEMPLATE)
	public void setTemplate(
			@PathParam(Constants.Uri.Param.SERVICE_NAME) String serviceName,
			byte[] payload,
			@HeaderParam("Content-Type") String contentType
			);
	
	
	// ----
	// TEMPLATE / DELETE
	// ----
	//
	// Calling DELETE on a 'template' resources removes that resource
	//
	@DELETE
	@Path(Constants.Uri.TEMPLATE)
	public void deleteTemplate(
			@PathParam(Constants.Uri.Param.SERVICE_NAME) String serviceName
			);

	// ----
	// SCHEMA / GET
	// ----
	// Calling GET on a named 'schema' resource returns a schema (XSD).
	@GET
	@Path(Constants.Uri.SCHEMA)
	public Response getSchema(
			@PathParam(Constants.Uri.Param.SERVICE_NAME) String serviceName, 
			@PathParam(Constants.Uri.Param.SCHEMA_NAME) String schemaName
			);
	
	// ----
	// SCHEMA / POST
	// ----
	//
	// Calling POST on a named 'schema' resource creates (or overwrites)
	// that schema from the uploaded payload.
	//
	@POST
	@Path(Constants.Uri.SCHEMA)
	public void setSchema(
			@PathParam(Constants.Uri.Param.SERVICE_NAME) String serviceName, 
			@PathParam(Constants.Uri.Param.SCHEMA_NAME) String schemaName,
			byte[] payload,
			@HeaderParam("Content-Type") String contentType
			);

	// ----
	// SCHEMA / DELETE
	// ----
	//
	// Calling DELETE on a named 'schema' resource deletes that schema
	//
	@DELETE
	@Path(Constants.Uri.SCHEMA)
	public void deleteSchema(
			@PathParam(Constants.Uri.Param.SERVICE_NAME) String serviceName, 
			@PathParam(Constants.Uri.Param.SCHEMA_NAME) String schemaName
			);
	
	
	// ----
	// CONFIG / GET
	// ----
	// Calling GET on a named 'configuration' resource returns a configuration (XSD).
	@GET
	@Produces("text/xml")
	@Path(Constants.Uri.CONFIG)
	public Response getConfig(
			@PathParam(Constants.Uri.Param.SERVICE_NAME) String serviceName, 
			@PathParam(Constants.Uri.Param.CONFIG_NAME) String configName
			);
	
	// ----
	// CONFIG / POST
	// ----
	//
	// Calling POST on a named 'configuration' resource creates (or overwrites)
	// that configuration from the uploaded payload.
	//
	@POST
	@Path(Constants.Uri.CONFIG)
	public void setConfig(
			@PathParam(Constants.Uri.Param.SERVICE_NAME) String serviceName, 
			@PathParam(Constants.Uri.Param.CONFIG_NAME) String configName,
			byte[] payload,
			@HeaderParam("Content-Type") String contentType
			);
	
	// ----
	// CONFIG / DELETE
	// ----
	//
	// Calling DELETE on a named 'schema' resource deletes that schema
	//
	@DELETE
	@Path(Constants.Uri.CONFIG)
	public void deleteConfig(
			@PathParam(Constants.Uri.Param.SERVICE_NAME) String serviceName, 
			@PathParam(Constants.Uri.Param.CONFIG_NAME) String schemaName
			);

	
	// ----
	// TEMPLATE_HISTORY / GET
	// ----
	// Calling GET on the 'template history' resource returns a list of historic templates
	//
	@GET
	@Produces("text/xml")
	@Path(Constants.Uri.TEMPLATE_HISTORY)
	public ResourceHistory getTemplateHistory(
			@PathParam(Constants.Uri.Param.SERVICE_NAME) String serviceName
			) throws ResourceNotFoundException;

	// ----
	// HISTORIC_TEMPLATE / GET
	// ----
	// Calling GET on a 'historic template' resource gets an older version of the template
	//
	@GET
	@Produces("text/xml")
	@Path(Constants.Uri.HISTORIC_TEMPLATE)
	public Response getHistoricTemplate(
			@PathParam(Constants.Uri.Param.SERVICE_NAME) String serviceName,
			@PathParam(Constants.Uri.Param.TIMESTAMP) String timestamp
			) throws ResourceNotFoundException, ParseException;
	
	

	// ----
	// SCHEMA_HISTORY / GET
	// ----
	// Calling GET on the 'schema history' resource returns a list of historic schemas
	//
	@GET
	@Produces("text/xml")	
	@Path(Constants.Uri.SCHEMA_HISTORY)
	public ResourceHistory getSchemaHistory(
			@PathParam(Constants.Uri.Param.SERVICE_NAME) String serviceName,
			@PathParam(Constants.Uri.Param.SCHEMA_NAME) String schemaName
			) throws ResourceNotFoundException;

	// ----
	// HISTORIC_SCHEMA / GET
	// ----
	// Calling GET on a 'historic schema' resource gets an older version of the schema
	//
	@GET
	@Produces("text/xml")
	@Path(Constants.Uri.HISTORIC_SCHEMA)
	public Response getHistoricSchema(
			@PathParam(Constants.Uri.Param.SERVICE_NAME) String serviceName,
			@PathParam(Constants.Uri.Param.SCHEMA_NAME) String schemaName,
			@PathParam(Constants.Uri.Param.TIMESTAMP) String timestamp
			) throws ResourceNotFoundException, ParseException;
	
	
	// ----
	// CONFIG_HISTORY / GET
	// ----
	// Calling GET on the 'config history' resource returns a list of historic configurations
	//
	@GET
	@Produces("text/xml")
	@Path(Constants.Uri.CONFIG_HISTORY)
	public ResourceHistory getConfigHistory(
			@PathParam(Constants.Uri.Param.SERVICE_NAME) String serviceName,
			@PathParam(Constants.Uri.Param.CONFIG_NAME) String configName
			) throws ResourceNotFoundException;

	// ----
	// HISTORIC_CONFIG / GET
	// ----
	// Calling GET on a 'historic config' resource gets an older version of the configuration
	//
	@GET
	@Produces("text/xml")
	@Path(Constants.Uri.HISTORIC_CONFIG)
	public Response getHistoricConfig(
			@PathParam(Constants.Uri.Param.SERVICE_NAME) String serviceName,
			@PathParam(Constants.Uri.Param.CONFIG_NAME) String configName,
			@PathParam(Constants.Uri.Param.TIMESTAMP) String timestamp
			) throws ResourceNotFoundException, ParseException;
	
	
	
	// ---
	// RENAME_SERVICE / POST
	// ---
	// Calling POST on a 'rename' resource renames a service to the name specified 
	// by the 'newName' query parameter. If the resource is successfully renamed, then
	// the service returns the location of the renamed service (in the form of an XML document)
	//
	@POST
	@Path(Constants.Uri.RENAME_SERVICE)
	public RenameResult renameService(
			@PathParam(Constants.Uri.Param.SERVICE_NAME) String serviceName,
			@QueryParam("newName") String newName);
	
	// ---
	// RENAME_SCHEMA / POST
	// ---
	// Calling POST on a 'rename schema' resource renames that schema to the name 
	// specified by the 'newName' query parameter
	//
	@POST
	@Path(Constants.Uri.RENAME_SCHEMA)
	public RenameResult renameSchema(
			@PathParam(Constants.Uri.Param.SERVICE_NAME) String serviceName,
			@PathParam(Constants.Uri.Param.SCHEMA_NAME) String schemaName,
			@QueryParam("newName") String newName);
	

	// ---
	// RENAME_CONFIG / POST
	// ---
	// Calling POST on a 'rename schema' renames that schema to the name specified by the 'newName'
	// query parameter
	//
	@POST
	@Path(Constants.Uri.RENAME_CONFIG)
	public RenameResult renameConfig(
			@PathParam(Constants.Uri.Param.SERVICE_NAME) String serviceName,
			@PathParam(Constants.Uri.Param.CONFIG_NAME) String configName,
			@QueryParam("newName") String newName);

	
	// ---
	// UPLOAD / POST
	// ---
	// Calling POST on the 'upload' resource creates a resources of type specified by the 'rel'
	// query parameter and the name 'name' parameter. If the 'rel' parameter is 'template' then
	// the 'name' parameter is ignored. Returns a list of the created resource(s).
	//
	@POST
	@Path(Constants.Uri.UPLOAD)
	public ResourceList upload(
			@PathParam(Constants.Uri.Param.SERVICE_NAME) String serviceName,
			@QueryParam(Constants.Uri.Param.REL) String rel,
			@QueryParam(Constants.Uri.Param.NAME) String name,
			byte[] payload, 
			@HeaderParam(Constants.Uri.Param.CONTENT_TYPE) String contentType);

	
	// ---
	// INI_SPLITTER / POST
	// ---
	// Calling POST on the 'ini splitter' resource creates a service based on the INI/INF
	// provided in the payload. Returns a list of the resources available in the created service.
	//
	//
	@POST
	@Path(Constants.Uri.INI_SPLITTER)
	ServiceResources splitInf(byte[] payload,
			@HeaderParam(Constants.Uri.Param.CONTENT_TYPE) String contentType,
			@QueryParam(Constants.Uri.Param.SERVICE_NAME) String serviceName); 
			
	

	
}
