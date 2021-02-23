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
			//1. JDBC 드라이버 로딩
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			//2. 데이터베이스 커넥션 시도   
			Connection con = DriverManager.getConnection(
					"jdbc:oracle:thin:@"+StaticIP.ip+":1521:xe", 
					"hr",
					"hr");
			
			//3. 쿼리문을 실행하기위한 객체생성
			Statement stmt = con.createStatement();
			
			//4. 쿼리 실행
			ResultSet rs = stmt.executeQuery("select * from customer where id = 'aaa123'");
			
			//5. 쿼리 실행 결과 사용
			while(rs.next()) {
				user.id = rs.getString("id");
				user.name = rs.getString("name");
				user.userTime = rs.getString("timer");
			}
			
			//6. 쿼리문 실행객체 종료
			rs.close();
			stmt.close();
			
			//7. 데이터베이스 커넥션 종료
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
