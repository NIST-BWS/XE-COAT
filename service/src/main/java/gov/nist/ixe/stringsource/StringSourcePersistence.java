package gov.nist.ixe.stringsource;

import static gov.nist.ixe.Logging.info;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;

public class StringSourcePersistence {

	public static void save(StringSource ss, File destination) throws IOException {
		writeAll(getRealFile(ss, destination), ss.getData());
	}

	public static StringSource readFrom(File f) throws IOException {
		File realFile = getRealFile(f);
		String filename = realFile.getName();
		String charset = filename.substring(filename.lastIndexOf(".")+1);
		return new StringSource(readAll(realFile), charset);	

	}
	
	public static StringSource infer(File f) throws IOException {
		byte[] data = readAll(f);
		StringSource result = StringSource.infer(data);
		info(String.format("File '%s' appears to be encoded with charset '%s'", f.getAbsolutePath(), result.getCharset()));
		return result;
	}
	
	public static StringSource inferFromSystemResource(String resourceName) throws IOException, URISyntaxException {
		File f = new File(ClassLoader.getSystemResource(resourceName).toURI());
		byte[] data = readAll(f);
		StringSource result = StringSource.infer(data);
		info(String.format("File '%s' appears to be encoded with charset '%s'", f.getAbsolutePath(), result.getCharset()));
		return result;
	}


	private static File[] seekRealFiles(File f) {
		File seekDir = f.getParentFile();
		if (seekDir == null) { 
			throw new StringSourceFilePersistenceException
			(StringSourceFilePersistenceException.ErrorMessage.FILENAME_DOES_NOT_IMPLY_DIRECTORY);
		}
		return seekDir.listFiles((new StringSourcePersistence()).new StringSourceFileFilter(f));
	}

	public static File getRealFile(File f) throws FileNotFoundException {
		File[] candidates = seekRealFiles(f);
		if (candidates.length == 0) {
			throw new FileNotFoundException();
		} else if (candidates.length > 1) {
			throw new StringSourceFilePersistenceException
			(StringSourceFilePersistenceException.ErrorMessage.TOO_MANY_CANDIDATES);
		}
		return candidates[0];

	}
	
	public static File tryGettingRealFile(File f) {
		File[] candidates = seekRealFiles(f);
		if (candidates.length > 1) {
			throw new StringSourceFilePersistenceException
			(StringSourceFilePersistenceException.ErrorMessage.TOO_MANY_CANDIDATES);
		}
		File result = null;
		if (candidates != null && candidates.length == 1) {
			result = candidates[0];
		}
		return result;
	}


	public static File getRealFile(StringSource ss, File f) {
		return new File(f.getAbsolutePath() + "." + ss.getCharset()); 
	}
	
	public static File getFakeFile(File realFile) {
		String filename = realFile.getAbsolutePath();
		return new File(filename.substring(0, filename.lastIndexOf(".")));
	}

	private class StringSourceFileFilter implements FileFilter {

		protected File f;
		public StringSourceFileFilter(File f) {	this.f = f;	}

		@Override
		public boolean accept(File pathname) {
			return (pathname.getAbsolutePath().length() > f.getAbsolutePath().length()) &&
					(pathname.getAbsolutePath() + ".").startsWith(f.getAbsolutePath() + ".");
		}

	}
	
	private static byte[] readAll(File f) throws IOException {
		
		RandomAccessFile file = new RandomAccessFile(f, "r");
		
		byte[] b = new byte[(int)file.length()];
		file.read(b);
		file.close();
		return b;
	}

	private static void writeAll(File f, byte[] data) throws IOException {
		RandomAccessFile file = new RandomAccessFile(f, "rw");
		file.write(data);
		file.close();
	}


}
