package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

import data.GameInfo;

public class GameCalcDB {
	
	Connection con;
	Statement stmt;
	ResultSet rs;
	
	public GameCalcDB() {
		
		try {
			String url = "jdbc:oracle:thin:@"+StaticIP.ip+":1521:xe";
			String id = "hr", pw = "hr";
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			con = DriverManager.getConnection(url, id, pw);
			stmt = con.createStatement();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<GameInfo> list(){
		ArrayList<GameInfo> res = new ArrayList<GameInfo>();
		
		try {
			rs = stmt.executeQuery("select * from game");
			
			while(rs.next()) {
				res.add(new GameInfo(
						rs.getString("kind"),
						rs.getString("category"),
						rs.getString("time")
						));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return res;
	}
	
	public GameInfo detail(String kind){
		GameInfo res = null;
		
		try {
			rs = stmt.executeQuery("select * from game where kind = '"+kind+"'");
			
			while(rs.next()) {
				res = new GameInfo(
						rs.getString("kind"),
						rs.getString("category"),
						rs.getString("time")
						);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return res;
	}
	
	void close() {
		if(rs != null) try {rs.close();} catch (SQLException e) {}
		if(stmt != null) try {stmt.close();} catch (SQLException e) {}
		if(con != null) try {con.close();} catch (SQLException e) {}
	}
	
}
