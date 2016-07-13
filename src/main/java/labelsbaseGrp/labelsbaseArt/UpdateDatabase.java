package labelsbaseGrp.labelsbaseArt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import FrameworkUtils.CommonFunctions;
import FrameworkUtils.CopyDBFile;
import FrameworkUtils.DBConnection;
import UiMap.LBPageElements;

public class UpdateDatabase {

	public UpdateDatabase() {

	}

	public static void updateDatabase(WebDriver driver) {
		ArrayList<String> labelURLListST = new ArrayList<String>();
		StringBuilder currAmountSt = new StringBuilder();
		StringBuilder prevAmountSt = new StringBuilder();
		String cAm = null;
		int currAmountInt = 0;
		int prevAmountInt = 0;
		Connection con = null;
		con = DBConnection.dbConnector();
		PreparedStatement pst = null;

		// Open labelsbase URL in browser
		driver.get("http://labelsbase.net/labels");

		// Get amount of labels on the website
		cAm = CommonFunctions.getElementText(driver, LBPageElements.totalLabelAmount);
		currAmountSt.append(CommonFunctions.getElementText(driver, LBPageElements.totalLabelAmount));
		currAmountSt.replace(1, 2, "");
		currAmountInt = Integer.parseInt(currAmountSt.toString());
		// Get amount of labels previously in website
		String sqlSelect = "SELECT Last_Update_Amount FROM LabelsDBTable";

		try {
			pst = con.prepareStatement(sqlSelect);
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				prevAmountSt.setLength(0);
				prevAmountSt.append(rs.getString(1));
				prevAmountSt.replace(1, 2, "");
				prevAmountInt = Integer.parseInt(prevAmountSt.toString());
			}

			if (currAmountInt > prevAmountInt) {
				CopyDBFile.copyDBFile();
				// Collect all label's URLs
				if (driver.findElements(LBPageElements.maxPages).size() > 0) {
					// Find maximum number of pages
					int maxPages = ((currAmountInt - prevAmountInt) / 10) + 1;
					System.out.println("Max Pages: " + maxPages);

					// Collect URL for each label, page at a time
					for (int i = 0; i < maxPages; i++) {
						if (driver.findElements(LBPageElements.nextButton).size() > 0) {
							if (driver.findElements(LBPageElements.labelLink).size() > 0) {
								// System.out.println("Labels Found");
								List<WebElement> labelURLListWE = driver.findElements(LBPageElements.labelLink);
								for (WebElement w : labelURLListWE) {
									labelURLListST.add(w.getAttribute("href"));
								}
							}
							CommonFunctions.clickButton(driver, LBPageElements.nextButton);
							try {
								Thread.sleep(2000);
							} catch (InterruptedException e) {
							}
						}
					}

					// Collect all label URLs from DB and remove those from
					// what was collected from website
					sqlSelect = "SELECT LB_URL FROM LabelsDBTable";
					pst = con.prepareStatement(sqlSelect);
					ResultSet rs1 = pst.executeQuery();
					ArrayList<String> DBURLs = new ArrayList<String>();

					while (rs1.next()) {
						DBURLs.add(rs1.getString(1));
					}

					ArrayList<String> toRemove = new ArrayList<String>();

					for (String s : DBURLs) {
						for (String str : labelURLListST) {
							if (s.equals(str)) {
								toRemove.add(str);
							}
						}
					}
					labelURLListST.removeAll(toRemove);

				}

				// Navigate to each URL that was collected and scrape label info
				for (String s : labelURLListST) {
					// Instantiate variables to hold collected info
					String labelName = null;
					String demoEmail = null;
					StringBuilder genre = new StringBuilder();
					String country = null;
					String[] cityArray = null;
					String city = null;
					String location = null;
					String fBURL = null;
					String fBLikes = null;
					StringBuilder labelArtists = new StringBuilder();

					driver.get(s);
					CommonFunctions.wait(1);
					
					// Get email address
					if (driver.findElements(LBPageElements.demoEmail1).size() > 0) {
						demoEmail = CommonFunctions.getElementText(driver, LBPageElements.demoEmail1);
					} else {
						demoEmail = CommonFunctions.getElementText(driver, LBPageElements.demoEmail2);
					}

					// Get full location
					if (driver.findElements(LBPageElements.location).size() > 0) {
						WebElement locElement = driver.findElement(LBPageElements.location);
						location = locElement.findElement(By.xpath("..")).getText();
					}

					// Break full location into country and city
					if (location != null) {
						cityArray = location.split(" , ");
						if (cityArray.length == 2) {
							city = cityArray[1];
							country = cityArray[0];
						} else {
							city = "N/A";
							country = location;
						}
					}

					// Get genre
					if (driver.findElements(LBPageElements.genre).size() > 0) {
						List<WebElement> genresListWE = driver.findElements(LBPageElements.genre);

						for (WebElement w : genresListWE) {
							genre.append(w.getText() + ", ");
						}
						genre.replace(genre.length() - 2, genre.length(), "");
					}

					// Get affiliated artists
					if (driver.findElements(LBPageElements.labelArtists).size() > 0) {
						List<WebElement> labelArtistsWE = driver.findElements(LBPageElements.labelArtists);
						for (WebElement w : labelArtistsWE) {
							String[] artistSplit = w.getText().split("\\n");
							if (artistSplit.length > 1) {
								labelArtists.append(artistSplit[1] + ", ");
							} else {
								labelArtists.append(artistSplit[0] + ", ");
							}
						}
						labelArtists.replace(labelArtists.length() - 2, labelArtists.length(), "");
					} else {
						labelArtists.append("N/A");
					}

					// Get label name
					labelName = CommonFunctions.getElementText(driver, LBPageElements.labelName);

					// Get Fabecook URL
					if (driver.findElements(LBPageElements.facebookLink1).size() > 0) {
						if (driver.findElement(LBPageElements.facebookLink1).getAttribute("href").contains("facebook")) {
							fBURL = driver.findElement(LBPageElements.facebookLink1).getAttribute("href");
						}
					}
					if (driver.findElements(LBPageElements.facebookLink2).size() > 0) {
						if (driver.findElement(LBPageElements.facebookLink2).getAttribute("href").contains("facebook")) {
							fBURL = driver.findElement(LBPageElements.facebookLink2).getAttribute("href");
						}
					}
					if (driver.findElements(LBPageElements.facebookLink3).size() > 0) {
						if (driver.findElement(LBPageElements.facebookLink3).getAttribute("href").contains("facebook")) {
							fBURL = driver.findElement(LBPageElements.facebookLink3).getAttribute("href");
						}
					}
					
					// Get Facebook likes
					if (driver.findElements(LBPageElements.facebookLikes1).size() > 0) {
						fBLikes = CommonFunctions.getElementText(driver, LBPageElements.facebookLikes1);
					}
					if (driver.findElements(LBPageElements.facebookLikes2).size() > 0) {
						fBLikes = CommonFunctions.getElementText(driver, LBPageElements.facebookLikes2);
					}
					if (driver.findElements(LBPageElements.facebookLikes3).size() > 0) {
						fBLikes = CommonFunctions.getElementText(driver, LBPageElements.facebookLikes3);
						if (fBLikes.contains(",")) {
							fBLikes = fBLikes.replace(",", "");
						}
					}

					// Insert new labels and their info into DB
					String sqlUpdate = "INSERT INTO LabelsDBTable VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

					pst = con.prepareStatement(sqlUpdate);
					pst.setString(1, labelName);
					pst.setString(2, demoEmail);
					pst.setString(3, country);
					pst.setString(4, city);
					pst.setString(5, genre.toString());
					pst.setString(6, labelArtists.toString());
					pst.setString(7, s);
					pst.setString(8, cAm);
					pst.setString(10, "no");
					pst.setString(11, fBURL);
					pst.setString(12, fBLikes);
					pst.executeUpdate();

					labelArtists.setLength(0);
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		// Remove unwanted records from DB
		PurgeDB.purgeDB(cAm);
		// Set labels that should be ignored when emailing
		SetLabelsToIgnore.setLabelsToIgnore();
		JunoInfo.getJunoInfo(driver);
		driver.close();
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
