package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import data.InventoryOrderInfo2;


public class InvenOrderDB {
	public ArrayList<InventoryOrderInfo2> arr;
	
	public InvenOrderDB() {
		arr = new ArrayList<InventoryOrderInfo2>();
		
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
			ResultSet rs = stmt.executeQuery("select * from item");
			
			//5.���� ���� ��� ���
			while(rs.next()) {
				InventoryOrderInfo2 invenOrder;
				invenOrder = new InventoryOrderInfo2(rs.getString("category"),
						rs.getString("menu"), rs.getString("serial"));
				
				arr.add(invenOrder);
//				System.out.println(rs.getString("job_id"));
//				System.out.println(rs.getString("job_title"));
//				System.out.println(rs.getString("min_salary"));
//				System.out.println(rs.getString("max_salary"));
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
	
	
	public static void main(String[] args) {
		System.out.println(new InvenOrderDB().arr);
	}
}
