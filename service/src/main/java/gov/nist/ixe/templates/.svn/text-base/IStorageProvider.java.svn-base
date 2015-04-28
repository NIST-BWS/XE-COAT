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

import gov.nist.ixe.stringsource.StringSource;

import java.text.ParseException;
import java.util.Date;

public interface IStorageProvider {

	String[] getServiceNames();
	void addService(String serviceName);
	boolean hasService(String serviceName);
	boolean isServiceDeletable(String serviceName);
	void deleteService(String serviceName);
	void renameService(String serviceName, String newName);

	StringSource getTemplate(String serviceName);
	boolean hasTemplate(String serviceName);

	void setTemplate(String serviceName, StringSource template);
	void deleteTemplate(String serviceName);

	String[] getSchemaNames(String serviceName);
	void addSchema(String serviceName, String schemaName, StringSource schema);
	StringSource getSchema(String serviceName, String schemaName);
	void deleteSchema(String serviceName, String schemaName);
	void renameSchema(String serviceName, String oldSchemaName, String newConfigName);

	String[] getConfigNames(String serviceName);
	void addConfig(String serviceName, String configName, StringSource config);
	StringSource getConfig(String serviceName, String configName);
	void deleteConfig(String serviceName, String configName);
	void renameConfig(String serviceName, String oldConfigName, String newConfigName);

	boolean supportsHistory();

	StringSource getHistoricSchema(String serviceName, String schemaName, Date timestamp);
	HistoryInfo[] getSchemaHistoryInfo(String serviceName, String schemaName);

	StringSource getHistoricConfig(String serviceName, String configurationName, Date timestamp);
	HistoryInfo[] getConfigHistoryInfo(String serviceName, String configurationName);

	StringSource getHistoricTemplate(String serviceName, Date timestamp);
	HistoryInfo[] getTemplateHistoryInfo(String serviceName);

	public Date fromStringToDate(String timestamp) throws ParseException;
	public String fromDateToString(Date date);

}