package gov.nist.ixe;

import java.io.File;

public class FileOperationException extends RuntimeException {

	public static FileOperationException couldNotDeleteFile(File file) {
		FileOperationException ex = new FileOperationException();
		ex.message = ErrorMessage.COULD_NOT_DELETE;
		ex.targetFile = file;
		return ex;
	}
	
	public static FileOperationException couldNotCreateDirectory(File file) {
		FileOperationException ex = new FileOperationException();
		ex.message = ErrorMessage.COULD_NOT_CREATE_DIRECTORY;
		ex.targetFile = file;
		return ex;
	}

	public static FileOperationException couldNotRenameFile(File from, File to) {
		FileOperationException ex = new FileOperationException();
		ex.message = String.format(ErrorMessage.COULD_NOT_RENAME_TO, to.getAbsolutePath());
		ex.targetFile = from;
		return ex;
	}
	
	
	private String message;
	public String getMessage() {
		return message;
	}

	private static final long serialVersionUID = -897088946849302708L;

	public static class ErrorMessage {
		public static final String COULD_NOT_DELETE = 
				"The target could not be deleted.";
		public static final String COULD_NOT_CREATE_DIRECTORY = 
				"The target directory could not created.";
		public static final String COULD_NOT_RENAME_TO = 
				"The target could not be renamed to %s.";
	}
	
	private File targetFile = null;
	public File getTargetFile() {
		return targetFile;
	}
	
}
