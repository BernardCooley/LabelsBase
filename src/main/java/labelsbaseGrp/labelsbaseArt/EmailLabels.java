package labelsbaseGrp.labelsbaseArt;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

import FrameworkUtils.CommonFunctions;
import FrameworkUtils.DBConnection;
import UiMap.GMailPageElements;

public class EmailLabels {

	public EmailLabels() {

	}

	public static void emailLabels(WebDriver driver, String genre, String releaseName) {
		String thisGenre = "%" + genre + "%";
		ArrayList<String> emailAddressesList = new ArrayList<String>();
		ArrayList<String> labelNamesList = new ArrayList<String>();
		ArrayList<String> allArtistsList = new ArrayList<String>();
		ArrayList<String> artistsList = new ArrayList<String>();
		ArrayList<String> sentList = new ArrayList<String>();
		ArrayList<String> ignoreList = new ArrayList<String>();
		Connection con = DBConnection.dbConnector();
		PreparedStatement pst = null;
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		String letter = null;

		List<XWPFParagraph> paragraphList = EmailBuilder.buildEmail("Email.docx");

		String sqlSelect = "SELECT * FROM LabelsDBTable WHERE Genre LIKE ?";
		try {
			pst = con.prepareStatement(sqlSelect);
			pst.setString(1, thisGenre);
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				emailAddressesList.add(rs.getString(2));
				labelNamesList.add(rs.getString(1));
				allArtistsList.add(rs.getString(6));
				sentList.add(rs.getString(9));
				ignoreList.add(rs.getString(10));
			}

		} catch (SQLException e) {
		}

		driver.get("https://mail.google.com/mail/u/0/#inbox");

		for (int i = 0; i < emailAddressesList.size(); i++) {

			System.out.println(ignoreList.size());
			if (!ignoreList.get(i).equalsIgnoreCase("yes")) {
				if (sentList.get(i) == null || !sentList.get(i).contains(releaseName)) {

					// Copy paragraphs from word doc
					for (XWPFParagraph p : paragraphList) {
						// Add paragraphs to stringbuilder with a new line after
						// each
						if (paragraphList.indexOf(p) == 4) {
							letter = p.getText();
							letter = letter.replaceAll("labelname", labelNamesList.get(i));
						} else {
							letter = (p.getText());
						}
						sb1.append(letter + "\n");
					}

					CommonFunctions.wait(2);
					CommonFunctions.clickButton(driver, GMailPageElements.composeBtn);
					CommonFunctions.wait(1);
					CommonFunctions.clickButton(driver, GMailPageElements.recipientField);
					CommonFunctions.inputToField(driver, GMailPageElements.recipientField, emailAddressesList.get(i));
					CommonFunctions.wait(1);
					driver.findElement(GMailPageElements.recipientField).sendKeys(Keys.ENTER);
					CommonFunctions.inputToField(driver, GMailPageElements.subjectField,
							"Cooley - " + releaseName + " Demo Submission");
					CommonFunctions.clickButton(driver, GMailPageElements.bodyField);
					CommonFunctions.inputToField(driver, GMailPageElements.bodyField, sb1.toString());
					driver.findElement(GMailPageElements.bodyField).sendKeys(Keys.ENTER);
					driver.findElement(GMailPageElements.bodyField).sendKeys(Keys.CONTROL + "v");

					CommonFunctions.wait(1);
					CommonFunctions.clickButton(driver, GMailPageElements.sendBtn);

					sb1.setLength(0);
					sb2.setLength(0);

					String sqlUpdate = "UPDATE LabelsDBTable SET Sent=? WHERE Email=?";
					try {
						pst = con.prepareStatement(sqlUpdate);
						if (sentList.get(i) == null) {
							pst.setString(1, releaseName);
						} else {
							pst.setString(1, sentList.get(i) + ", " + releaseName);
						}
						pst.setString(2, emailAddressesList.get(i));
						pst.executeUpdate();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		driver.close();
	}

}
