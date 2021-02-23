package db;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import data.UserInfo;
import user.UserUI;

public class DBConnect implements Serializable{
	
	public DBConnect() {
		
	}
	
	public DBConnect(UserInfo user) {
		try {
			//1. JDBC ����̹� �ε�
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			//2. �����ͺ��̽� Ŀ�ؼ� �õ�   
			Connection con = DriverManager.getConnection(
					"jdbc:oracle:thin:@"+StaticIP.ip+":1521:xe", 
					"hr",
					"hr");
			
			//3. �������� �����ϱ����� ��ü����
			Statement stmt = con.createStatement();
			
			//4. ���� ����
			ResultSet rs = stmt.executeQuery("select * from customer where id = 'aaa123'");
			
			//5. ���� ���� ��� ���
			while(rs.next()) {
				user.id = rs.getString("id");
				user.name = rs.getString("name");
				user.userTime = rs.getString("timer");
			}
			
			//6. ������ ���ఴü ����
			rs.close();
			stmt.close();
			
			//7. �����ͺ��̽� Ŀ�ؼ� ����
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
