package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import data.LogData;

public class UserUseCountDB {
	
	Connection con;
	Statement stmt;
	ResultSet rs;
	String sql;
	
	public UserUseCountDB() {
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
	
	public ArrayList<LogData> getUserSale(String id) {
		ArrayList<LogData> res = new ArrayList<LogData>();
		
		sql = "select * from log where id = '"+id+"' order by time";
		
		try {
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				LogData tmp = new LogData();
				
				tmp.setTime(rs.getDate("time"));
				tmp.setId(rs.getString("id"));
				tmp.setLogin(rs.getString("login"));
				tmp.setItem(rs.getString("item"));
				tmp.setFee(rs.getInt("fee"));
				tmp.setPay(rs.getString("pay"));
				
				res.add(tmp);
			}
			
			for (LogData ld : res) {
				if(ld.getItem() != null) {
					sql = "select price from item where menu = '"+ld.getItem()+"'";
					rs = stmt.executeQuery(sql);
					
					while(rs.next()) {
						ld.setPrice(rs.getInt("price"));
					}
				}
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
