package admin;

import java.awt.Color;
import java.awt.Font;
import java.awt.Label;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.NumberFormat;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import db.StaticIP;


class DBCash {
	
	String cashChk;
	
	public DBCash() {
		
		cashChk = "";

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
			ResultSet rs = stmt.executeQuery("select * from vault");

			//5.���� ���� ��� ���
			while(rs.next()) {

				Chk cc = new Chk(rs.getInt("cash"));

				cashChk = cc.cash+"";
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


	class Chk {

		int cash;

		public Chk(int cash) {
			super();
			this.cash = cash;
		}


		@Override
		public String toString() {
			return cash+"";
		}
	
	}
	
}

public class CashChk {
	
	JFrame cashChkFrame;
	
	public CashChk() {
		
		DBCash dbCash = new DBCash();
				
		Font ff = new Font("�޸�����ü", Font.BOLD+Font.ITALIC, 26);
		
		String str = dbCash.cashChk;
		String dd = "";
		NumberFormat nf = NumberFormat.getInstance();
		dd = nf.format(Integer.parseInt(str));
		
		cashChkFrame = new JFrame();
		cashChkFrame.setBounds(200, 200, 500, 400);
		cashChkFrame.setLayout(null);
		
		JPanel jp = new JPanel();
		jp.setLayout(null);
		jp.setBounds(0, 0, 500, 400);
		cashChkFrame.add(jp);
		jp.setBackground(Color.white);
		
		JLabel jl = new JLabel(dd+"��",SwingConstants.CENTER);
		
		jl.setFont(ff);
		
		jl.setBounds(0, 100, 500, 100);
		
		
		jp.add(jl);
				
		cashChkFrame.setVisible(true);
		cashChkFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	}
	
}
