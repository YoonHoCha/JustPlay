package admin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import db.StaticIP;

class AdminDB_Conn{

	Connection con;
	Statement stmt;
	ResultSet rs;
	ArrayList<AdminData> userDb = new ArrayList<AdminData>();

	public AdminDB_Conn(String sql) {
		// TODO Auto-generated constructor stub

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@"+StaticIP.ip+":1521:xe",
					"hr",
					"hr");

			stmt = con.createStatement();

			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {

				AdminData info = new AdminData(rs.getString("id"),rs.getString("pw"));

				userDb.add(info);

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			close();
		}

	}

	public ArrayList<AdminData> getGameDb() {
		return userDb;
	}

	public void close() {
		if(rs!=null) try {rs.close();} catch (SQLException e) {}
		if(stmt!=null) try {stmt.close();} catch (SQLException e) {}
		if(con!=null) try {con.close();} catch (SQLException e) {}
	}

}

class AdminDB_Insert {

	Connection con;
	Statement stmt;
	ResultSet rs;

	public AdminDB_Insert(String sql) {
		// TODO Auto-generated constructor stub


		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@"+StaticIP.ip+":1521:xe",
					"hr",
					"hr");

			stmt = con.createStatement();

			int cnt = stmt.executeUpdate(sql);
			


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			close();
		}

	}

	public void close() {
		if(rs!=null) try {rs.close();} catch (SQLException e) {}
		if(stmt!=null) try {stmt.close();} catch (SQLException e) {}
		if(con!=null) try {con.close();} catch (SQLException e) {}
	}

}


class AdminData{

	String id,pw;

	public AdminData(String id, String pw) {
		super();

		this.id = id;
		this.pw = pw;

	}
}

class AdminJoin extends JFrame implements WindowListener {

	JLabel jl0;

	JTextField id_gui, name_gui,num2,num3;
	JPasswordField pw_gui,pwchk_gui;
	JComboBox<String> year_gui,mon_gui,day_gui;
	JButton join_gui;
	String pp;
	ArrayList<AdminData> aData = new AdminDB_Conn("select * from Admin").getGameDb();
	String input_id;
	
	int dis;

	class JoinBntAct implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub

			String id = id_gui.getText();		
			String pw = pw_gui.getText();		
			String pwChk = pwchk_gui.getText();
			
			
			try {
				if(!id.equals(input_id)) {
					JOptionPane.showMessageDialog(null, "아이디를 중복확인 해주세요.");
					return;
				}
					
				if(chk) {
					if(id_chk) {
						if(pw.equals(pwChk)) {
							try {
								
								chkSpace(pw, "비밀 번호");
								chkEmpty(pw, "비밀 번호");
								
								if(pw.length() < 7) {
									JOptionPane.showMessageDialog(null, "비밀 번호의 최소 글자수는 7글자 입니다.");
									throw new Exception();
								}
								
							} catch (Exception e1) {
								return;
							}
							
							new AdminDB_Insert("insert into Admin (id, pw) values ('"
									+id+"','"+pw+"')");
							JOptionPane.showMessageDialog(null, "회원 가입 완료");
							new AdminId_gui();
							dispose();
						}else {
							if(pwChk.equals("")) {
								JOptionPane.showMessageDialog(null, "비밀번호확인이 공백입니다.");
								return;
							}
							JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다.");
						}
					}else {
						
						try {
							chkId(id);
							chkEmpty(id, "아이디");
						} catch (Exception e1) {
							return;
						}
					
					}
				}else{
					JOptionPane.showMessageDialog(null, "아이디를 중복확인 해주세요");
				}
				
			} catch (Exception e1) {}
			
		}
	}

	boolean chk = false, id_chk;

	class IDBntAct implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			id_chk = false;
			chk = true;
			String id = id_gui.getText().trim();
			input_id = id_gui.getText().trim();
			pp = "";

			try {
				chkId(id);
				chkEmpty(id, "아이디");
			} catch (Exception e1) {
				return;
			}

			for (AdminData ad : aData) {

				if(id.equals(ad.id)) {
					id_chk = false;
					pp = "중복된 아이디";
					break;
				}else {
					id_chk = true;
					pp="이용 가능한 아이디";
				}
			}
			JOptionPane.showMessageDialog(null, pp);
			pp = "";

		}

	}

	void chkEmpty(String str, String title) throws Exception {
		if(str.equals("")) {
			JOptionPane.showMessageDialog(null, title + "의 공백을 채워주세요.");	
			throw new Exception();
		}
	}

	void chkSpace(String str, String title) throws Exception {
		String chk = "\\s";

		Pattern pattern = Pattern.compile(chk);
		Matcher matcher = pattern.matcher(str);

		while(matcher.find()) {
			JOptionPane.showMessageDialog(null, title+"에 띄어쓰기를 제거해 주세요.");	
			throw new Exception();
		}

	}

	void chkSpe(String str) throws Exception {
		String chk = "[!@#$%^&*(),.?\\\":{}|<>]";

		Pattern pattern = Pattern.compile(chk);
		Matcher matcher = pattern.matcher(str);

		while(matcher.find()) {
			JOptionPane.showMessageDialog(null, "특수기호는 사용하실 수 없습니다.");	
			throw new Exception();
		}
	}

	void chkId(String str) throws Exception {
		String chk1 = "^[a-zA-Z0-9]*$";
		String chk2 = "^[a-zA-Z].*$";

		Pattern pattern1 = Pattern.compile(chk1);
		Matcher matcher1 = pattern1.matcher(str);

		Pattern pattern2 = Pattern.compile(chk2);
		Matcher matcher2 = pattern2.matcher(str);
		chkSpe(str);
		chkSpace(str, "아이디");

		if(!matcher2.matches()) {
			JOptionPane.showMessageDialog(null, "아이디를 영문으로 시작해 주세요");
			throw new Exception();
		}

		if(!matcher1.matches()) {
			JOptionPane.showMessageDialog(null, "아이디를 확인해 주세요.");	
			throw new Exception();
		}

		if(str.length() > 10) {
			JOptionPane.showMessageDialog(null, "아이디의 최대 길이는 10글자 입니다.");	
			throw new Exception();
		}else if(str.length() < 5) {
			JOptionPane.showMessageDialog(null, "아이디의 최소 길이는 5글자 입니다.");	
			throw new Exception();
		}

	}

	public AdminJoin(int dis) {
		// TODO Auto-generated constructor stub
		super("회원가입");
		
		this.dis = dis;

		setBounds(300, 100, 500, 400);
		setLayout(null);

		jl0 = new JLabel();
		jl0.setBounds(100, 30, 300, 30);
		add(jl0);

		JLabel jl1 = new JLabel("아이디");
		jl1.setBounds(100, 80, 80, 40);
		add(jl1);

		id_gui = new JTextField();
		id_gui.setBounds(200, 80, 120, 40);
		add(id_gui);

		JButton idBnt = new JButton("중복확인");
		idBnt.setBounds(330, 80, 100, 40);
		idBnt.addActionListener(new IDBntAct());
		add(idBnt);


		JLabel jl2 = new JLabel("비밀번호");
		jl2.setBounds(100, 150, 80, 40);
		add(jl2);

		pw_gui = new JPasswordField();
		pw_gui.setBounds(200, 150, 120, 40);
		add(pw_gui);

		JLabel jl4 = new JLabel("비밀번호확인");
		jl4.setBounds(100, 220, 80, 40);
		add(jl4);

		pwchk_gui = new JPasswordField();
		pwchk_gui.setBounds(200, 220, 120, 40);
		add(pwchk_gui);

		join_gui = new JButton("회원가입");
		join_gui.setBounds(200, 300, 100, 30);
		join_gui.addActionListener(new JoinBntAct());
		add(join_gui);
		setVisible(true);
		
		addWindowListener(this);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		
		if(dis==0) {
			new AdminId_gui();
			dispose();
		}else
			dispose();
		
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

}

