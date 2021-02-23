package admin;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import data.AdminInfo;
import db.StaticIP;

class LoginDB_Conn {

	ArrayList<AdminInfo> uc = new ArrayList<AdminInfo>();
	Statement stmt;
	ResultSet rs;
	Connection con;

	public LoginDB_Conn() {
		
	}
	
	public LoginDB_Conn(String sql) {
		// TODO Auto-generated constructor stub

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@"+StaticIP.ip+":1521:xe",
					"hr",
					"hr");

			stmt = con.createStatement();

			rs = stmt.executeQuery(sql);
//			System.out.println("실행확인");
			while(rs.next()) {

				AdminInfo info = new AdminInfo();
				info.id = rs.getString("id");
				uc.add(info);

			}


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			close();
		}

	}
	
	AdminInfo loginAdmin(String id) {
		AdminInfo ui = new AdminInfo();
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@"+StaticIP.ip+":1521:xe",
					"hr",
					"hr");

			stmt = con.createStatement();

			rs = stmt.executeQuery("select id from admin where id ='"+id+"'");
			
			while(rs.next()) {
				ui.id = rs.getString("id");

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			close();
		}
		
		return ui;
	}
	
	ArrayList<AdminChk> getAll() {
		
		ArrayList<AdminChk> res = new ArrayList<AdminChk>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@"+StaticIP.ip+":1521:xe",
					"hr",
					"hr");

			stmt = con.createStatement();
			
			rs = stmt.executeQuery("select id, pw from admin");
			
			while(rs.next()) {

				AdminChk info = new AdminChk(
						rs.getString("id"),
						rs.getString("pw")
						);
				
				res.add(info);

			}

		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}

		return res;
	}


	public ArrayList<AdminInfo> AdminChk() {
		return uc;
	}



	public void close() {
		if(rs!=null) try {rs.close();} catch (SQLException e) {}
		if(stmt!=null) try {stmt.close();} catch (SQLException e) {}
		if(con!=null) try {con.close();} catch (SQLException e) {}
	}

}

class AdminChk {
	String id, pw;
	
	public AdminChk(String id, String pw) {
		super();
		this.id = id;
		this.pw = pw;

	}
	
}

public class AdminId_gui extends JFrame {
	JTextField id_gui;
	JPasswordField pw_gui;
	JLabel jl;

	Socket socket;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	public AdminInfo ui;
	
	
	AdminInfo ai;
	
	ArrayList<AdminInfo> arr = new LoginDB_Conn("select id from admin").AdminChk();
	public boolean chk = true;
	class JoinBntAct implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {

			String id = id_gui.getText();
			String pw = pw_gui.getText();
			String pp = "";
			
			ArrayList<AdminChk> uArr = new LoginDB_Conn().getAll();
			
		
			for (int i = 0; i < uArr.size(); i++) {

				if(uArr.get(i).id!=null&&uArr.get(i).pw!=null) {
					if(id.equals(uArr.get(i).id) && pw.equals(uArr.get(i).pw)) {
						//System.out.println("로그인");
						pp="로그인 성공";
						new PCBangAdmin(id);
						dispose();
						break;
					}else {
						pp="다시 확인해주세요";

					}

				}
			}
			jl.setText(pp);


		}
	}
	
	class AdminJoinBntAct implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			new AdminJoin(0);
			dispose();
		}
	}


	public AdminId_gui() {
		super("로그인");

		this.socket = socket;
		this.oos = oos;
		this.ois = ois;
		
		setBounds(300, 300, 300, 300);
		setLayout(null);

		jl = new JLabel("+++");
		jl.setBounds(20, 20, 200, 30);
		add(jl);


		JLabel idjl = new JLabel("아이디:");
		idjl.setBounds(50, 50, 80, 30);
		add(idjl);
		id_gui = new JTextField();
		id_gui.setBounds(100, 50, 100, 30);
		add(id_gui);

		JLabel pwjl = new JLabel("비밀번호:");
		pwjl.setBounds(40, 120, 80, 30);
		add(pwjl);

		pw_gui = new JPasswordField();
		pw_gui.setBounds(100, 120, 100, 30);
		add(pw_gui);

		JButton joinBnt = new JButton("로그인");
		joinBnt.setBounds(100, 180, 100, 30);
		joinBnt.addActionListener(new JoinBntAct());
		add(joinBnt);

		JButton member = new JButton("회원가입");
		member.setBounds(100, 220, 100, 30);
		member.addActionListener(new AdminJoinBntAct());
		add(member);

		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
	}
	
	public static void main(String[] args) {
		new AdminId_gui();
	}

}

