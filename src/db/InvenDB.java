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
			//1.JDBS ����̹� ����
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			//2.�����ͺ��̽� Ŀ�ټ� �õ�
			Connection con = DriverManager.getConnection(
					"jdbc:oracle:thin:@"+StaticIP.ip+":1521:xe",
					"hr",
					"hr");
			
			//3.�������� �����ϱ����� ��ü����
			Statement stmt = con.createStatement();
			
			//4.��������
			ResultSet rs = stmt.executeQuery("select * from stock");
			
			//5.���� ���� ��� ���
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
			
			//6.������ ���ఴü ����
			rs.close();
			stmt.close();
			
			//7.�����ͺ��̽� Ŀ�ټ� ����
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
