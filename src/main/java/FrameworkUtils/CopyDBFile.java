package FrameworkUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

public class CopyDBFile {

	public static void copyDBFile() {
		InputStream inStream = null;
		OutputStream outStream = null;

		try {
			Date date = new Date();
			date.getTime();

			File afile = new File("C:\\Eclipse Workspace\\LabelsBase\\LabelsDB.sqlite");
			File bfile = new File(
					"C:\\Eclipse Workspace\\LabelsBase\\DB Backup\\LabelsDB" + date.getTime() + ".sqlite");

			inStream = new FileInputStream(afile);
			outStream = new FileOutputStream(bfile);

			byte[] buffer = new byte[1024];

			int length;
			// copy the file content in bytes
			while ((length = inStream.read(buffer)) > 0) {

				outStream.write(buffer, 0, length);

			}

			inStream.close();
			outStream.close();

//			System.out.println("File is copied successfully!");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
