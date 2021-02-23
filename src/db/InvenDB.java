package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import data.InventoryInfo;


public class InvenDB {
	public ArrayList<InventoryInfo> arr;
	
	public InvenDB() {
		arr = new ArrayList<InventoryInfo>();
		
		try {
			//1.JDBS 드라이버 실행
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			//2.데이터베이스 커넨션 시도
			Connection con = DriverManager.getConnection(
					"jdbc:oracle:thin:@"+StaticIP.ip+":1521:xe",
					"hr",
					"hr");
			
			//3.쿼리문을 실행하기위한 객체생성
			Statement stmt = con.createStatement();
			
			//4.쿼리실행
			ResultSet rs = stmt.executeQuery("select * from stock");
			
			//5.쿼리 실행 결과 사용
			while(rs.next()) {
				InventoryInfo inven;
				inven = new InventoryInfo(rs.getString("category"),
						rs.getString("menu"), rs.getString("serial"));
				
				//arr.add(inven);
//				System.out.println(rs.getString("job_id"));
//				System.out.println(rs.getString("job_title"));
//				System.out.println(rs.getString("min_salary"));
//				System.out.println(rs.getString("max_salary"));
				arr.add(inven);
			}
			
			//6.쿼리문 실행객체 종료
			rs.close();
			stmt.close();
			
			//7.데이터베이스 커넨션 종료
			con.close();
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	public ArrayList<InventoryInfo> getList(){
		
		return arr;
		
	}
	
	public static void main(String[] args) {
		System.out.println(new InvenDB().arr);
	}
}
