package labelsbaseGrp.labelsbaseArt;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import FrameworkUtils.CopyDBFile;
import FrameworkUtils.DBConnection;

public class SetLabelsToIgnore {

	public static void setLabelsToIgnore() {

		ArrayList<String> emailsList = new ArrayList<String>();
		ArrayList<String> fbURLList = new ArrayList<String>();
		ArrayList<Integer> likesList = new ArrayList<Integer>();
		ArrayList<String> cityList = new ArrayList<String>();
		Connection con = DBConnection.dbConnector();
		PreparedStatement pst = null;

		try {
			Charset charset = Charset.forName("ISO-8859-1");
			for (String line : Files.readAllLines(
					Paths.get("C:\\Eclipse Workspace\\LabelsBase\\Labels to ignore", "Labels to ignore.txt"),
					charset)) {
				emailsList.add(line);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		CopyDBFile.copyDBFile();
		String sqlUpdate = "UPDATE LabelsDBTable SET Ignore='yes', City='Ignored' WHERE Email=?";
		for (String s : emailsList) {
			try {
				pst = con.prepareStatement(sqlUpdate);
				pst.setString(1, s);
				pst.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		String sqlSelect = "SELECT * FROM LabelsDBTable";
		try {
			pst = con.prepareStatement(sqlSelect);
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				if (rs.getString(12) != null) {
					fbURLList.add(rs.getString(11));
					String s = rs.getString(12);
					s = s.replace(",", "");
					likesList.add(Integer.parseInt(s));
					cityList.add(rs.getString(4));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		for (Integer i : likesList) {
			if (i < 500) {
				sqlUpdate = "UPDATE LabelsDBTable SET Ignore='yes' WHERE FB_URL=?";
				try {
					pst = con.prepareStatement(sqlUpdate);
					pst.setString(1, fbURLList.get(likesList.indexOf(i)));
					pst.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (cityList.get(likesList.indexOf(i)).contains("London")) {
				sqlUpdate = "UPDATE LabelsDBTable SET Ignore='no' WHERE FB_URL=?";
				try {
					pst = con.prepareStatement(sqlUpdate);
					pst.setString(1, fbURLList.get(likesList.indexOf(i)));
					pst.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
