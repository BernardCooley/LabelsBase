package labelsbaseGrp.labelsbaseArt;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

public class EmailBuilder {

	private static List<XWPFParagraph> paragraphList = new ArrayList<XWPFParagraph>();

	public EmailBuilder() {

	}

	public static List<XWPFParagraph> buildEmail(String docFile) {

		try {
			FileInputStream fis = new FileInputStream(docFile);

			XWPFDocument docx = new XWPFDocument(fis);

			paragraphList = docx.getParagraphs();

		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
		return paragraphList;
	}
}
