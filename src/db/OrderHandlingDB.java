package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import data.GameInfo;
import data.OrderInfo;
import data.OrderSendArray;

public class OrderHandlingDB {
	
	Connection con;
	Statement stmt;
	ResultSet rs;
	
	public OrderHandlingDB() {
		
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
	
	public ArrayList<OrderSendArray> stocklist() {
		ArrayList<OrderSendArray> res = new ArrayList<OrderSendArray>();
		
		try {
			rs = stmt.executeQuery("select * from stock");
			
			while(rs.next()) {
				res.add(new OrderSendArray(
						rs.getString("serial"),
						rs.getString("menu"),
						rs.getString("category")
						));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return res;
	}
	
	public int getItem(String name) {
		int res = 0;
		
		try {
			rs = stmt.executeQuery("select price from item where menu = '"+name+"'");
			
			while(rs.next()) {
				res = rs.getInt("price");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return res;
	}
	
	public void deleteStock(String serial) {
		try {
			rs = stmt.executeQuery("delete stock where serial = '"+serial+"'");
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	public int getVault() {
		int res = 0;
		
		try {
			rs = stmt.executeQuery("select cash from vault");
			
			while(rs.next()) {
				res = rs.getInt("cash");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return res;
	}
	
	public void setVault(int cash) {
		try {
			stmt.executeUpdate("update vault set cash = "+cash);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	void close() {
		if(rs != null) try {rs.close();} catch (SQLException e) {}
		if(stmt != null) try {stmt.close();} catch (SQLException e) {}
		if(con != null) try {con.close();} catch (SQLException e) {}
	}
}
