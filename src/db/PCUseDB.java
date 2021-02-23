package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import data.LogData;
import data.PCUseInfo;

public class PCUseDB {
	
	Connection con;
	Statement stmt;
	ResultSet rs;
	
	String sql = null;
	
	public PCUseDB() {
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
	
	public ArrayList<LogData> list() {
		ArrayList<LogData> res = new ArrayList<LogData>();
		sql = "select * from log order by time";
		
		try {
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				
				LogData ld = new LogData();
				ld.setTime(rs.getTimestamp("time"));
				ld.setId(rs.getString("id"));
				ld.setLogin(rs.getString("login"));
				ld.setItem(rs.getString("item"));
				ld.setFee(rs.getInt("fee"));
				ld.setPay(rs.getString("pay"));
				
				res.add(ld);
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
						rs.getTimestamp("time"),
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
	
	public ArrayList<LogData> order() {
		
		ArrayList<LogData> res = new ArrayList<LogData>();
		
		sql = "select item, count(item) from log where item not in ('null') "
				+ "group by(item) order by count(item) desc";

		try {
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				
				LogData ld = new LogData();
				
				ld.setItem(rs.getString("item"));
				ld.setSum(rs.getString("count(item)"));
				
				res.add(ld);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return res;
		
	}
	
	public int insert(LogData ld) {
		
		int res=0;
		
		sql = "insert into log (time, id, login, item, fee, pay) "
				+ "values ('"
				+ld.strTime()+"','"+
				ld.getId()+"','"+
				ld.getLogin()+"','"+
				ld.getItem()+"',"+
				ld.getFee()+",'"+
				ld.getPay()+"')";
		
		try {
			res = stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			close();
		}
		
		
		return res;
		
	}
	
	public ArrayList<LogData> itemStats(String month) {

		ArrayList<LogData> res = new ArrayList<LogData>();

		sql = "select item, count(item) from log where item not in ('null') "
				+ "and to_char(time, 'YYYY-MM') = '"+month+"' "
						+ "group by(item) order by count(item) desc";

		try {
			rs = stmt.executeQuery(sql);

			while(rs.next()) {

				LogData ld = new LogData();

				ld.setItem(rs.getString("item"));
				ld.setSum(rs.getString("count(item)"));

				res.add(ld);
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
