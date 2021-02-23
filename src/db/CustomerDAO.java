package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import data.UserListData;


public class CustomerDAO {

	Connection con = null;
	Statement stmt = null;
	ResultSet rs;
	String sql;

	public CustomerDAO() {

		String url = "jdbc:oracle:thin:@"+StaticIP.ip+":1521:xe";
		String id = "hr", pw = "hr";

		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");

			con = DriverManager.getConnection(url, id, pw);
			stmt = con.createStatement();

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public ArrayList<UserListData> list() {

		sql = "select * from customer";

		ArrayList<UserListData> res = new ArrayList<>();

		try {

			rs = stmt.executeQuery(sql);

			while(rs.next()) {

				UserListData uld = new UserListData();
				uld.setId(rs.getString("id"));
				uld.setName(rs.getString("name"));
				uld.setBirth(rs.getDate("birth"));
				uld.setTel(rs.getString("tel"));
				uld.setTimer(rs.getString("timer"));
				res.add(uld);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			close();
		}

		return res;

	}

	public String getUseTime(String id) {

		sql = "select usetime from customer where id = '"+id+"'";

		String res = "";

		try {

			rs = stmt.executeQuery(sql);

			while(rs.next()) {
				res = rs.getString("usetime");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			close();
		}

		return res;

	}

	public UserListData detail(String id) {

		UserListData res = null;

		sql = "select * from customer where id = '"+id+"'";

		try {
			rs = stmt.executeQuery(sql);

			if(rs.next()) {

				res = new UserListData();

				res.setId(rs.getString("id"));
				res.setName(rs.getString("name"));
				res.setBirth(rs.getDate("birth"));
				res.setTel(rs.getString("tel"));
				res.setTimer(rs.getString("timer"));
				res.setUseTime(rs.getString("usetime"));			
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			close();
		}


		return res;

	}

	public void updateTime(String id, String timer) {

		sql = "update customer set timer = '"+timer+"' where id = '"+id+"'";

		try {
			stmt.executeUpdate(sql);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			close();
		}
	}

	public String getTimer(String id) {
		String res = "";

		sql = "select usetime from customer where id = '"+id+"'";

		try {
			rs = stmt.executeQuery(sql);

			if(rs.next()) {
				if(rs.getString("usetime") == null)
					res = "0:0";
				else
					res = rs.getString("usetime");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			close();
		}

		return res;
	}

	public void setTimer(String time, String id) {

		sql = "update customer set usetime = '"+time+"' where id = '"+id+"'";

		try {
			stmt.executeUpdate(sql);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			close();
		}

	}

	public void setPhone(String phone, String id) {
		sql = "update customer set tel = '"+phone+"' where id = '"+id+"'";

		try {
			stmt.executeUpdate(sql);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			close();
		}
	}

	public void setPw(String pw, String id) {
		sql = "update customer set pw = '"+pw+"' where id = '"+id+"'";

		try {
			stmt.executeUpdate(sql);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			close();
		}
	}

	public String getPw(String id) {
		String res = "";

		sql = "select pw from customer where id = '"+id+"'";

		try {
			rs = stmt.executeQuery(sql);

			if(rs.next()) {
				res = rs.getString("pw");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			close();
		}

		return res;
	}

	public UserListData idSearch(String name, String tel) {

		UserListData res = null;

		sql = "select id from customer where name = '"+name+"' and tel = '"+ tel+ "'";

		try {
			rs = stmt.executeQuery(sql);

			if(rs.next()) {

				res = new UserListData();

				res.setId(rs.getString("id"));

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			close();
		}

		return res;

	}

	public UserListData pwSearch(String name, String tel) {

		UserListData res = null;

		sql = "select pw from customer where name = '"+name+"' and tel = '"+ tel+ "'";

		try {
			rs = stmt.executeQuery(sql);

			if(rs.next()) {

				res = new UserListData();

				res.setPw(rs.getString("pw"));

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			close();
		}

		return res;

	}
	
	public String getBirthYear(String id) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		
		String res = "";

		sql = "select birth from customer where id = '"+id+"'";

		try {
			rs = stmt.executeQuery(sql);

			if(rs.next()) {
				res = sdf.format(rs.getDate("birth"));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			close();
		}

		return res;
	}
	
	public String getTime(String id) {
		String res = "";

		sql = "select timer from customer where id = '"+id+"'";

		try {
			rs = stmt.executeQuery(sql);

			if(rs.next()) {
				if(rs.getString("timer") == null)
					res = "0:0";
				else
					res = rs.getString("timer");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			close();
		}

		return res;
	}

	public void close() {

		if(rs!=null) try {rs.close();} catch (SQLException e) {}
		if(stmt!=null) try {stmt.close();} catch (SQLException e) {}
		if(con!=null) try {con.close();} catch (SQLException e) {}

	}


}
