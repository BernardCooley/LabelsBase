package labelsbaseGrp.labelsbaseArt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.openqa.selenium.WebDriver;

import FrameworkUtils.CommonFunctions;
import FrameworkUtils.CopyDBFile;
import FrameworkUtils.DBConnection;
import UiMap.JDPageElements;
import UiMap.LBPageElements;

public class UpdateLikesAndReleases {

	public static void updateLikesAndReleases(WebDriver driver) {
		CopyDBFile.copyDBFile();
		Connection con = null;
		con = DBConnection.dbConnector();
		PreparedStatement pst = null;
		ArrayList<String> JunoURLList = new ArrayList<String>();
		ArrayList<String> LabelsBaseURLList = new ArrayList<String>();
		String sqlUpdate = null;
		String likes = null;
		String sqlSelect = "SELECT * FROM LabelsDBTable";
		try {
			pst = con.prepareStatement(sqlSelect);
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				JunoURLList.add(rs.getString(13));
				LabelsBaseURLList.add(rs.getString(7));
			}

//			for (String s : JunoURLList) {
//
//				if (s != null) {
//					if (!s.equalsIgnoreCase("N/A")) {
//						driver.get(s);
//
//						System.out.println(CommonFunctions.getElementText(driver, JDPageElements.releaseAmount));
//
//						sqlUpdate = "UPDATE LabelsDBTable SET Juno_Release_Amt=? WHERE Juno_URL=?";
//						pst = con.prepareStatement(sqlUpdate);
//						pst.setString(1, CommonFunctions.getElementText(driver, JDPageElements.releaseAmount));
//						pst.setString(2, s);
//						pst.executeUpdate();
//					}
//				}
//			}

			for (String s : LabelsBaseURLList) {

				if (s != null) {

					if (!s.equalsIgnoreCase("N/A")) {
						driver.get(s);

						if (CommonFunctions.elementExists(driver, LBPageElements.facebookLikes1)) {
							likes = CommonFunctions.getElementText(driver, LBPageElements.facebookLikes1);
						} else if (CommonFunctions.elementExists(driver, LBPageElements.facebookLikes2)) {
							likes = CommonFunctions.getElementText(driver, LBPageElements.facebookLikes2);
						} else if (CommonFunctions.elementExists(driver, LBPageElements.facebookLikes3)) {
							likes = CommonFunctions.getElementText(driver, LBPageElements.facebookLikes3);
						}
						
						if (likes != null) {
							likes = likes.replace(",", "");
						}

						System.out.println(likes);

						 sqlUpdate = "UPDATE LabelsDBTable SET FB_Likes=? WHERE LB_URL=?";
						 pst = con.prepareStatement(sqlUpdate);
						 pst.setString(1, likes);
						 pst.setString(2, s);
						 pst.executeUpdate();
						 
						 likes = null;
					}
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
