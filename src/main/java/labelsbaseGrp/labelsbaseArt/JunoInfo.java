package labelsbaseGrp.labelsbaseArt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import FrameworkUtils.CommonFunctions;
import FrameworkUtils.CopyDBFile;
import FrameworkUtils.DBConnection;
import UiMap.JDPageElements;

public class JunoInfo {

	public JunoInfo() {

	}

	public static void getJunoInfo(WebDriver driver) {

		CopyDBFile.copyDBFile();
		Connection con = null;
		con = DBConnection.dbConnector();
		PreparedStatement pst = null;
		ArrayList<String> namesList = new ArrayList<String>();
		ArrayList<String> junoURLList = new ArrayList<String>();
		ArrayList<WebElement> namesElementList = new ArrayList<WebElement>();
		ArrayList<WebElement> releaseAmtElementList = new ArrayList<WebElement>();
		List<By> namesByList = new ArrayList<By>();
		List<By> releaseAmtByList = new ArrayList<By>();
		String name = null;
		String releaseAmount = null;
		String currentURL = "N/A";
		int listSize = 0;

		// Get all label names from DB
		String sqlSelect = "SELECT * FROM LabelsDBTable";
		try {
			pst = con.prepareStatement(sqlSelect);
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				namesList.add(rs.getString(1));
				junoURLList.add(rs.getString(13));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		int nullValues = Collections.frequency(junoURLList, null);
		
		// Open Juno, search for each name and store the url and the release
		// amount
		for (String s : namesList) {

			if (junoURLList.get(namesList.indexOf(s)) == null) {
				System.out.println(nullValues + " remaining");
				nullValues--;

				driver.get("http://www.junodownload.com/");

				CommonFunctions.inputToField(driver, JDPageElements.searchBox, s);

				CommonFunctions.wait(1);
				Select select = new Select(driver.findElement(JDPageElements.dropdown));
				// select.deselectAll();
				select.selectByVisibleText("Labels");

				CommonFunctions.pressEnter(driver, JDPageElements.searchBox);
				CommonFunctions.clickButton(driver, JDPageElements.labelSeeAll);
				CommonFunctions.wait(2);

				listSize = CommonFunctions.getListSize(driver, JDPageElements.labelsList);
				namesByList = JDPageElements.getLabelsList(listSize);
				releaseAmtByList = JDPageElements.getreleaseAmtList(listSize);

				for (int i = 0; i < listSize; i++) {
					namesElementList.add(driver.findElement(namesByList.get(i)));
				}
				
				for (int i = 0; i < listSize; i++) {
					releaseAmtElementList.add(driver.findElement(releaseAmtByList.get(i)));
				}

				for (WebElement w : namesElementList) {
					name = w.getText();
					if (name.equalsIgnoreCase(s)) {
						releaseAmount = releaseAmtElementList.get(namesElementList.indexOf(w)).getText();
						w.click();
						CommonFunctions.clickButton(driver, JDPageElements.filterBtn);
//						CommonFunctions.wait(2);
						currentURL = driver.getCurrentUrl();
						break;
					}
				}

				namesElementList.clear();
				releaseAmtElementList.clear();

				// Store url and release amount in database
				String sqlUpdate = "UPDATE LabelsDBTable SET Juno_URL=?, Juno_Release_Amt=? WHERE Name=?";
				try {
					pst = con.prepareStatement(sqlUpdate);
					pst.setString(1, currentURL);
					pst.setString(2, releaseAmount);
					pst.setString(3, s);
					pst.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				currentURL = "N/A";
				releaseAmount = null;
			}

		}
		driver.close();
	}
}
