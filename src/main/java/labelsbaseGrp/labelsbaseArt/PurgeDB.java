package labelsbaseGrp.labelsbaseArt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import FrameworkUtils.CopyDBFile;
import FrameworkUtils.DBConnection;

public class PurgeDB {

	public static void purgeDB(String currAmount) {
		CopyDBFile.copyDBFile();
		Connection con = null;
		con = DBConnection.dbConnector();
		PreparedStatement pst = null;
		String sqlDelete = null;
		String sqlUpdate = null;
		String sqlInsert = null;
		String sqlSelect = null;

		try {
			// Delete DB records where the email field is null
			sqlDelete = "DELETE FROM LabelsDBTable WHERE Email IS NULL";
			pst = con.prepareStatement(sqlDelete);
			pst.executeUpdate();

			// Delete DB records where the country field contains "%year(s)%"
			sqlDelete = "DELETE FROM LabelsDBTable WHERE Country LIKE '%year(s)%'";
			pst = con.prepareStatement(sqlDelete);
			pst.executeUpdate();

			// Delete DB records where the email field does not contain a valid
			// email address
			sqlDelete = "DELETE FROM LabelsDBTable WHERE Email NOT LIKE '%@%'";
			pst = con.prepareStatement(sqlDelete);
			pst.executeUpdate();

			// Update DB with current label amount
			sqlUpdate = "UPDATE LabelsDBTable SET Last_Update_Amount=?";
			pst = con.prepareStatement(sqlUpdate);
			pst.setString(1, currAmount);
			pst.executeUpdate();

			// Copy all unique records to new table
			sqlInsert = "INSERT INTO LabelsDBTableCopy (Name, Email, Country, City, Genre, Artists, LB_URL, Last_Update_Amount, Sent, Ignore, FB_URL, FB_Likes, Juno_URL, Juno_Release_Amt) SELECT DISTINCT Name, Email, Country, City, Genre, Artists, LB_URL, Last_Update_Amount, Sent, Ignore, FB_URL, FB_Likes, Juno_URL, Juno_Release_Amt FROM LabelsDBTable";
			pst = con.prepareStatement(sqlInsert);
			pst.executeUpdate();

			// Delete all records from original table
			sqlDelete = "DELETE FROM LabelsDBTable";
			pst = con.prepareStatement(sqlDelete);
			pst.executeUpdate();

			// Insert all unique records into original table
			sqlInsert = "INSERT INTO LabelsDBTable (Name, Email, Country, City, Genre, Artists, LB_URL, Last_Update_Amount, Sent, Ignore, FB_URL, FB_Likes, Juno_URL, Juno_Release_Amt) SELECT Name, Email, Country, City, Genre, Artists, LB_URL, Last_Update_Amount, Sent, Ignore, FB_URL, FB_Likes, Juno_URL, Juno_Release_Amt FROM LabelsDBTableCopy";
			pst = con.prepareStatement(sqlInsert);
			pst.executeUpdate();

			// Delete all records from copy table
			sqlDelete = "DELETE FROM LabelsDBTableCopy";
			pst = con.prepareStatement(sqlDelete);
			pst.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
