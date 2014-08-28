package gov.nist.ixe;

import java.io.File;

public class FileOperationException extends RuntimeException {

	public static FileOperationException couldNotDeleteFile(File file) {
		FileOperationException ex = new FileOperationException();
		ex.message = ErrorMessage.COULD_NOT_DELETE_FILE;
		ex.targetFile = file;
		return ex;
	}
	public static FileOperationException couldNotCreateDirectory(File file) {
		FileOperationException ex = new FileOperationException();
		ex.message = ErrorMessage.COULD_CREATE_DIRECTORY;
		ex.targetFile = file;
		return ex;
	}
	
	
	
	private String message;
	public String getMessage() {
		return message;
	}

	private static final long serialVersionUID = -897088946849302708L;

	public static class ErrorMessage {
		public static final String COULD_NOT_DELETE_FILE = 
				"The target file could not be deleted.";
		public static final String COULD_CREATE_DIRECTORY = 
				"The target directory could not created.";
	}
	
	private File targetFile = null;
	public File getTargetFile() {
		return targetFile;
	}
	
}
