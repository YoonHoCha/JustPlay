package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import data.PCUseInfo;

public class PCUseCalcDB {
	
	Connection con;
	Statement stmt;
	ResultSet rs;
	
	String sql = null;
	
	public PCUseCalcDB() {
		String url = "jdbc:oracle:thin:@"+StaticIP.ip+":1521:xe";
		String id = "hr",  pw = "hr";
		
		try {
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			con = DriverManager.getConnection(url, id, pw);
			stmt = con.createStatement();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<PCUseInfo> list() {
		ArrayList<PCUseInfo> res = new ArrayList<PCUseInfo>();
		sql = "select * from log order by time desc";
		
		try {
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				
				PCUseInfo tmp = new PCUseInfo(
						rs.getDate("time"),
						rs.getString("id"),
						rs.getString("login"),
						rs.getString("item"),
						rs.getInt("fee"),
						rs.getString("pay")
						);
				
				res.add(tmp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return res;
		
	}
	
	public ArrayList<PCUseInfo> selectDaylist(String date1, String date2) {
		ArrayList<PCUseInfo> res = new ArrayList<PCUseInfo>();
		sql = "select * from log where time between '"+date1+"' and '"+date2+"'";
		
		try {
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				
				PCUseInfo tmp = new PCUseInfo(
						rs.getDate("time"),
						rs.getString("id"),
						rs.getString("login"),
						rs.getString("item"),
						rs.getInt("fee"),
						rs.getString("pay")
						);
				
				res.add(tmp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return res;
		
	}
	
	public PCUseInfo detail(String id) {
		PCUseInfo res = null;
		sql = "select * from log where id = '"+id+"'";
		
		try {
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				
				res = new PCUseInfo(
						rs.getDate("time"),
						rs.getString("id"),
						rs.getString("login"),
						rs.getString("item"),
						rs.getInt("fee"),
						rs.getString("pay")
						);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return res;
		
	}
	
	public void close() {
		if(rs != null) try {rs.close();} catch (SQLException e) {}
		if(stmt != null) try {stmt.close();} catch (SQLException e) {}
		if(con != null) try {con.close();} catch (SQLException e) {}
	}
}
