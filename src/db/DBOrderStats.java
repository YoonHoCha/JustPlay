package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import data.DataStatsMenu;

public class DBOrderStats {

	public DBOrderStats(DataStatsMenu dsm) {
		
		dsm.menuNameArr = new ArrayList<String>();
		dsm.menuImgArr = new ArrayList<String>();
		
		try {
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			Connection con = DriverManager.getConnection(
					"jdbc:oracle:thin:@"+StaticIP.ip+":1521:xe", 
					"hr", 
					"hr");
			
			
			Statement stmt = con.createStatement();
			
			ResultSet rs = stmt.executeQuery("select menu, image from item order by category desc");
			
			while(rs.next()) {
				dsm.menuNameArr.add(rs.getString("menu"));
				dsm.menuImgArr.add(rs.getString("image"));			
			}
			
			rs.close();
			stmt.close();
			con.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
