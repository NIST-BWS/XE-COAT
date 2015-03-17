/*-----------------------------------------------------------------------------------------------------+
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

import gov.nist.ixe.EncodingUtil;
import gov.nist.ixe.jaxb.BindingContext;
import gov.nist.ixe.templates.exception.IllegalContentTypeException;
import gov.nist.ixe.templates.exception.IllegalPayloadException;
import gov.nist.ixe.templates.exception.IllegalResourceNameException;
import gov.nist.ixe.templates.exception.ResourceNotEmptyException;
import gov.nist.ixe.templates.exception.ResourceNotFoundException;
import gov.nist.ixe.templates.exception.TemplateGenerationException;
import gov.nist.ixe.templates.exception.TemplateServiceClientException;
import gov.nist.ixe.templates.exception.TemplateServiceExceptionMapper;
import gov.nist.ixe.templates.jaxb.RenameResult;
import gov.nist.ixe.templates.jaxb.ResourceHistory;
import gov.nist.ixe.templates.jaxb.ResourceList;
import gov.nist.ixe.templates.jaxb.ServiceList;
import gov.nist.ixe.templates.jaxb.ServiceResources;
import gov.nist.ixe.templates.jaxb.TemplateGenerationError;

import java.io.InputStream;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class TemplateServicesClient implements ITemplateServices {

	private static final String GET = "GET";
	private static final String POST = "POST";
	private static final String DELETE = "DELETE";

	private String _rootUri;
	public String getRootUri() {
		return _rootUri;
	}

	public TemplateServicesClient(String rootUri) {
		_rootUri = rootUri;
	}
	
	public void makeRequest(String uri, String method) {
		// No payload, no return type
		makeRequest(uri, method, null, null, null);
	}

	public void makeRequest(String uri, String method, byte[] payload, String payloadContentType) {
		// Has a payload, but no return type
		makeRequest(uri, method, null, payload, payloadContentType);
	}

	public <T> T makeRequest(String uri, String method, Class<T> returnTypeClass) { 
		// No payload, but has a return value
		return makeRequest(uri, method, returnTypeClass, null, null);

	}
	
	public <T> T makeRequest(String uri, String method, Class<T> returnTypeClass, byte[] payload, String payloadContentType) {
		return makeRequest(uri, method, returnTypeClass, payload, payloadContentType, null);	
	}
		
	@SuppressWarnings("unchecked")
	public <T> T makeRequest(String uri, String method, Class<T> returnTypeClass, byte[] payload, String payloadContentType,
			MultivaluedMap<String,Object> headers) {
		T result = null;
		
		Invocation.Builder ib = ClientBuilder.newClient().target(uri).request();
		
		if (headers != null) {
			ib.headers(headers);
		}
		
		Response response;
		if (payload != null) {
			EncodingUtil.assertContentTypeIsValid(payloadContentType);
			response = ib.method(method, Entity.entity(payload, payloadContentType));			
		} else {
			response = ib.method(method);
		}	
	
		reverseMapException(response);
		
		if (returnTypeClass == Response.class) {
			result = (T) response;
		} else if (returnTypeClass != null) {
			result = response.readEntity(returnTypeClass);
		}	
				 
		return result;
	}
	
	private void reverseMapException(Response response) {
		
		StatusType status = response.getStatusInfo();
		
		if (status.getFamily() == Status.Family.SUCCESSFUL) return;
		
		if (status.getStatusCode() == Status.NOT_FOUND.getStatusCode()) {
			throw new ResourceNotFoundException(status.getReasonPhrase());
		} else if (status.getStatusCode() == Status.CONFLICT.getStatusCode()) {
			throw new ResourceNotEmptyException();
		} else {
	
		
			String message = status.getReasonPhrase();

			String exceptionType = response.getHeaderString(Constants.HttpHeader.EXCEPTION_TYPE);
			String exceptionMessage = response.getHeaderString(Constants.HttpHeader.EXCEPTION_MESSAGE);

			if (IllegalResourceNameException.class.getSimpleName().equals(exceptionType)) {
				throw new IllegalResourceNameException(exceptionMessage);
			} else if (IllegalPayloadException.class.getSimpleName().equals(exceptionType)) {
				throw new IllegalPayloadException();
			} else if (IllegalContentTypeException.class.getSimpleName().equals(exceptionType)) {
				throw new IllegalContentTypeException(message);
			} else if (TemplateGenerationException.class.getSimpleName().equals(exceptionType)) {

				try {
					TemplateGenerationError tge = 
							this.unmarshall(TemplateGenerationError.class, response.readEntity(InputStream.class));
					String serviceName = response.getHeaderString(Constants.HttpHeader.SERVICE_NAME);
					String resourceName = response.getHeaderString(Constants.HttpHeader.RESOURCE_NAME);
					throw new TemplateGenerationException(serviceName, resourceName, tge);
				} catch (JAXBException je) {
					throw new TemplateServiceClientException(message);
				}


			} else {
				throw new TemplateServiceClientException(message);
			}
		}
		
	}

	@SuppressWarnings("unchecked")
	private <T> T unmarshall(Class<T> cls, InputStream is) throws JAXBException {
		Unmarshaller um = BindingContext.getBindingContext(cls).createUnmarshaller();
		T result = (T) um.unmarshal(is);
		return result;
	}
	
	public String getVersion() {		
		return makeRequest(BuildUri.getVersionUri(getRootUri()), GET, String.class);
	}	
	public Response getVersionAsResponse() {
		return makeRequest(BuildUri.getVersionUri(getRootUri()), GET, Response.class);
	}

	public ServiceList getServiceList() throws ResourceNotFoundException {
		return makeRequest(getRootUri(), GET, ServiceList.class);
	}
	public Response getServiceListAsResponse() throws ResourceNotFoundException {
		return makeRequest(getRootUri(), GET, Response.class);
	}	
	
	public ServiceResources getServiceResources(String serviceName) {
		return makeRequest(BuildUri.getServiceUri(getRootUri(), serviceName), GET, ServiceResources.class);
	}
	public Response getServiceResourcesAsResponse(String serviceName) { 
		return makeRequest(BuildUri.getServiceUri(getRootUri(), serviceName), GET, Response.class);
	}
	public void createService(String serviceName) {
		makeRequest(BuildUri.getServiceUri(getRootUri(), serviceName), POST);
	}
	public Response createServiceAsResponse(String serviceName) {
		return makeRequest(BuildUri.getServiceUri(getRootUri(), serviceName), POST, Response.class);
	}
	


	public Response processDefaultTemplate(String serviceName) {
		return makeRequest(BuildUri.getProcessUri(getRootUri(), serviceName), GET, Response.class);
	}
	public Response processTemplateByName(String serviceName, String configName) {
		return makeRequest(BuildUri.getNamedProcessUri(getRootUri(), serviceName, configName), GET, Response.class);
	}
	public Response processTemplate(String serviceName, byte[] payload, String contentType) {
		return makeRequest(BuildUri.getProcessUri(getRootUri(), serviceName), POST, Response.class, payload, contentType);
	}


	public Response getTemplate(String serviceName) {
		return makeRequest(BuildUri.getTemplateUri(getRootUri(), serviceName), GET, Response.class);
	}
	public Response getSchema(String serviceName,	String schemaName) {
		return makeRequest(BuildUri.getSchemaUri(getRootUri(), serviceName, schemaName), GET, Response.class);
	}
	public Response getConfig(String serviceName, String configName) {
		return makeRequest(BuildUri.getConfigUri(getRootUri(), serviceName, configName), GET, Response.class);
	}


	public void setTemplate(String serviceName, byte[] payload, String contentType) {
		makeRequest(BuildUri.getTemplateUri(getRootUri(), serviceName), POST, payload, contentType);
	}
	public Response setTemplateAsResponse(String serviceName, byte[] payload, String contentType) {
		return makeRequest(BuildUri.getTemplateUri(getRootUri(), serviceName), POST, Response.class, payload, contentType);
	}
	public void setSchema(String serviceName, String schemaName, byte[] payload, String contentType) {
		makeRequest(BuildUri.getSchemaUri(getRootUri(), serviceName, schemaName), POST, payload, contentType);
	}
	public Response setSchemaAsResponse(String serviceName, String schemaName, byte[] payload, String contentType) {
		return makeRequest(BuildUri.getSchemaUri(getRootUri(), serviceName, schemaName), POST, Response.class, payload, contentType);
	}
	public void setConfig(String serviceName, String configName, byte[] payload, String contentType) {
		makeRequest(BuildUri.getConfigUri(getRootUri(), serviceName, configName), POST, payload, contentType);
	}
	public Response setConfigAsResponse(String serviceName, String configName, byte[] payload, String contentType) {
		return makeRequest(BuildUri.getConfigUri(getRootUri(), serviceName, configName), POST, Response.class, payload, contentType);
	}

	public void deleteTemplate(String serviceName) {
		makeRequest(BuildUri.getTemplateUri(getRootUri(), serviceName), DELETE);		
	}
	public Response deleteTemplateAsResponse(String serviceName) {
		return makeRequest(BuildUri.getTemplateUri(getRootUri(), serviceName), DELETE, Response.class);		
	}
	public void deleteService(String serviceName) {
		makeRequest(BuildUri.getServiceUri(getRootUri(), serviceName), DELETE);
	}
	public Response deleteServiceAsResponse(String serviceName) {
		return makeRequest(BuildUri.getServiceUri(getRootUri(), serviceName), DELETE, Response.class);
	}
	public void deleteSchema(String serviceName, String schemaName) {
		makeRequest(BuildUri.getSchemaUri(getRootUri(), serviceName, schemaName), DELETE, null);
	}
	public Response deleteSchemaAsResponse(String serviceName, String schemaName) {
		return makeRequest(BuildUri.getSchemaUri(getRootUri(), serviceName, schemaName), DELETE, Response.class);
	}
	public void deleteConfig(String serviceName, String configName) {
		makeRequest(BuildUri.getConfigUri(getRootUri(), serviceName, configName), DELETE, null);
	}
	public Response deleteConfigAsResponse(String serviceName, String configName) {
		return makeRequest(BuildUri.getConfigUri(getRootUri(), serviceName, configName), DELETE, Response.class);
	}

	public ResourceHistory getTemplateHistory(String serviceName) throws ResourceNotFoundException {
		return makeRequest(BuildUri.getTemplateHistoryUri(getRootUri(), serviceName), GET, ResourceHistory.class);		
	}
	public Response getTemplateHistoryAsResponse(String serviceName) throws ResourceNotFoundException {
		return makeRequest(BuildUri.getTemplateHistoryUri(getRootUri(), serviceName), GET, Response.class);		
	}

	public Response getHistoricTemplate(String serviceName, String timestamp)
			throws ResourceNotFoundException {
		return makeRequest(BuildUri.getHistoricTemplateUri(getRootUri(), serviceName, timestamp), GET, Response.class);
	}


	public ResourceHistory getSchemaHistory(String serviceName, String schemaName) 
			throws ResourceNotFoundException {
		return makeRequest(BuildUri.getSchemaHistoryUri(getRootUri(), serviceName, schemaName), GET, ResourceHistory.class);
	}
	public Response getSchemaHistoryAsResponse(String serviceName, String schemaName) throws ResourceNotFoundException {
		return makeRequest(BuildUri.getSchemaHistoryUri(getRootUri(), serviceName, schemaName), GET, Response.class);		
	}


	public Response getHistoricSchema(String serviceName, String schemaName, String timestamp)
			throws ResourceNotFoundException {
		return makeRequest(BuildUri.getHistoricSchemaUri(getRootUri(), serviceName, schemaName, timestamp), GET, Response.class);
	}	


	public ResourceHistory getConfigHistory(String serviceName, String configName) 
			throws ResourceNotFoundException {
		return makeRequest(BuildUri.getConfigHistoryUri(getRootUri(), serviceName, configName), GET, ResourceHistory.class);
	}
	public Response getConfigHistoryAsResponse(String serviceName, String schemaName) throws ResourceNotFoundException {
		return makeRequest(BuildUri.getConfigHistoryUri(getRootUri(), serviceName, schemaName), GET, Response.class);		
	}

	public Response getHistoricConfig(String serviceName, String configName, String timestamp)
			throws ResourceNotFoundException {
		return makeRequest(BuildUri.getHistoricConfigUri(getRootUri(), serviceName, configName, timestamp), GET, Response.class);

	}


	public RenameResult renameService(String serviceName, String newName) 
			throws ResourceNotFoundException {
		return makeRequest(BuildUri.getRenameServiceUri(getRootUri(), serviceName, newName), POST, RenameResult.class);		
	}
	public Response renameServiceAsResponse(String serviceName, String newName) 
			throws ResourceNotFoundException {
		return makeRequest(BuildUri.getRenameServiceUri(getRootUri(), serviceName, newName), POST, Response.class);		
	}	

	public RenameResult renameSchema(String serviceName, String schemaName, String newName) {
		return makeRequest(BuildUri.getRenameSchemaUri(getRootUri(), serviceName, schemaName, newName), POST, RenameResult.class);
	}
	public Response renameSchemaAsResponse(String serviceName, String schemaName, String newName) {
		return makeRequest(BuildUri.getRenameSchemaUri(getRootUri(), serviceName, schemaName, newName), POST, Response.class);
	}

	public RenameResult renameConfig(String serviceName, String configName, String newName) {
		return makeRequest(BuildUri.getRenameConfigUri(getRootUri(), serviceName, configName, newName), POST, RenameResult.class);
	}
	public Response renameConfigAsResponse(String serviceName, String configName, String newName) {
		return makeRequest(BuildUri.getRenameConfigUri(getRootUri(), serviceName, configName, newName), POST, Response.class);
	}

	public ResourceList upload(String serviceName, String rel, String name, byte[] payload, String contentType) {
		return makeRequest(BuildUri.getUploadUri(getRootUri(), serviceName, rel, name), POST, ResourceList.class, payload, contentType);
	}
	public Response uploadAsResponse(String serviceName, String rel, String name, byte[] payload, String contentType) {
		return makeRequest(BuildUri.getUploadUri(getRootUri(), serviceName, rel, name), POST, Response.class, payload, contentType);
	}

	@Override
	public ServiceList splitInf(byte[] payload, String contentType, String serviceName) {
		return makeRequest(BuildUri.getIniSplitterUri(getRootUri(), serviceName), POST, ServiceList.class, payload, contentType); 
	}

	@Override
	public String testConnectionViaGet() {
		return makeRequest(BuildUri.getTestConnectionUri(getRootUri()), GET, String.class);
	}
	public Response testConnectionViaGetAsResponse() {
		return makeRequest(BuildUri.getTestConnectionUri(getRootUri()), GET, Response.class);
	}

	@Override
	public String testConnectionViaPost() {
		return makeRequest(BuildUri.getTestConnectionUri(getRootUri()), POST, String.class);
	}
	public Response testConnectionViaPostAsResponse() {
		return makeRequest(BuildUri.getTestConnectionUri(getRootUri()), POST, Response.class);
	}

	@Override
	public String testConnectionViaDelete() { 
		return makeRequest(BuildUri.getTestConnectionUri(getRootUri()), DELETE, String.class);
	}
	public Response testConnectionViaDeleteAsResponse() {
		return makeRequest(BuildUri.getTestConnectionUri(getRootUri()), DELETE, Response.class);
	}


}

