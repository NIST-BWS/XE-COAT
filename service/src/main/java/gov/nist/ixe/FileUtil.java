 /*----------------------------------------------------------------------------------------------------+
 |                             National Institute of Standards and Technology                          |
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

package gov.nist.ixe;

import static gov.nist.ixe.Logging.trace;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {
	
	public static void clear(File start) {
		trace();
		
		
		if (start.isDirectory()) { 
			for (File f : start.listFiles()) {
				clear(f);
			}		
		}
		
		if (!start.exists()) return;
		
		boolean deleteOK = start.delete();
		if (!deleteOK) {
			Logging.warn("File %s not deleted", start.getAbsolutePath());
			throw FileOperationException.couldNotDeleteFile(start);			
		} else {
			Logging.info("File/directory '%s' deleted", start.getAbsolutePath());
		}
	}
	
	
	public static String getRandomTempDirectoryName(String prefix) {
		trace();
		return System.getProperty("java.io.tmpdir") + File.separator + "IXE" + File.separator + prefix + Long.toString(System.nanoTime());
	}
	
	public static File getRandomTempDirectory(String prefix) {
		trace();		
		File tempDir = new File(getRandomTempDirectoryName(prefix));
		boolean mkdirOK = tempDir.mkdirs();
		if (!mkdirOK) {
			throw FileOperationException.couldNotCreateDirectory(tempDir);
		}
		return tempDir;
	}
	
	public static void copyDirectory(File source, File target)
	throws IOException {
		if (source.isDirectory()) {			
			if (!target.exists()) {
				target.mkdir();	
			}			
			for (String child : source.list()) {
				copyDirectory(new File(source, child), new File(target, child));
			}
		} else {			
			InputStream is = new FileInputStream(source);
			OutputStream os = new FileOutputStream(target);
			
			byte[] buffer = new byte[1024];
			int length;
			try {
				while ((length = is.read(buffer)) > 0) {
					os.write(buffer, 0, length);							
				}
			} finally {
				os.close();
			}

			is.close();
			
		}
	}
}
