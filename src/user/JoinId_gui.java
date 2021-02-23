package user;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import data.DataBox;
import data.MapReq;
import data.OffSignal;
import data.RegistSignal;
import data.StartSignal;
import data.UserInfo;
import db.CustomerDAO;
import db.StaticIP;

class LoginDB_Conn {

	ArrayList<UserInfo> uc = new ArrayList<UserInfo>();
	Statement stmt;
	ResultSet rs;
	Connection con;

	public LoginDB_Conn() {

	}

	public LoginDB_Conn(String sql) {

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@"+StaticIP.ip+":1521:xe",
					"hr",
					"hr");

			stmt = con.createStatement();

			rs = stmt.executeQuery(sql);
			while(rs.next()) {

				UserInfo info = new UserInfo();
				info.id = rs.getString("id");
				info.name = rs.getString("name");
				info.userTime = rs.getString("timer");
				uc.add(info);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			close();
		}

	}

	UserInfo loginUser(String id) {
		UserInfo ui = new UserInfo();

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@"+StaticIP.ip+":1521:xe",
					"hr",
					"hr");

			stmt = con.createStatement();

			rs = stmt.executeQuery("select id, name, timer from customer where id ='"+id+"'");

			while(rs.next()) {
				ui.id = rs.getString("id");
				ui.name = rs.getString("name");
				ui.userTime = rs.getString("timer");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			close();
		}

		return ui;
	}

	ArrayList<UserChk> getAll() {

		ArrayList<UserChk> res = new ArrayList<UserChk>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@"+StaticIP.ip+":1521:xe",
					"hr",
					"hr");

			stmt = con.createStatement();

			rs = stmt.executeQuery("select id, pw, name, birth from customer");

			while(rs.next()) {

				UserChk info = new UserChk(
						rs.getString("id"),
						rs.getString("pw"),
						rs.getString("name"),
						sdf.format(rs.getDate("birth"))
						);

				res.add(info);

			}

		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return res;
	}


	public ArrayList<UserInfo> userChk() {
		return uc;
	}

	public void close() {
		if(rs!=null) try {rs.close();} catch (SQLException e) {}
		if(stmt!=null) try {stmt.close();} catch (SQLException e) {}
		if(con!=null) try {con.close();} catch (SQLException e) {}
	}

}

class UserChk {
	String id, pw, name, birth;

	public UserChk(String id, String pw, String name, String birth) {
		super();
		this.id = id;
		this.pw = pw;
		this.name = name;
		this.birth = birth;
	}

}

class WindowClose implements WindowListener {
	Time tt;
	JoinId_gui join;

	public WindowClose(Time tt, JoinId_gui join) {
		super();
		this.tt = tt;
		this.join = join;
	}

	@Override
	public void windowClosing(WindowEvent e) {
		tt.timeClose();
		join.dispose();
		new OnAndOff();
	}

	@Override
	public void windowOpened(WindowEvent e) {}
	@Override
	public void windowClosed(WindowEvent e) {}
	@Override
	public void windowIconified(WindowEvent e) {}
	@Override
	public void windowDeiconified(WindowEvent e) {}
	@Override
	public void windowActivated(WindowEvent e) {}
	@Override
	public void windowDeactivated(WindowEvent e) {}

}

public class JoinId_gui extends JFrame{
	JTextField id_gui;
	JPasswordField pw_gui;
	JLabel jl;

	Socket socket;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	public UserInfo ui;
	DataBox db;

	int seat;
	Time tt;

	ArrayList<UserInfo> arr = new LoginDB_Conn("select id, name, timer from customer").userChk();
	public boolean chk = true, startChk = false, serchChk, joinChk, login;
	IdAndPwSearch ipser;
	CustomerJoin cj;

	class JoinBntAct implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {

			String id = id_gui.getText();
			String pw = pw_gui.getText();
			String pp = "";
			ArrayList<String> chlid= new ArrayList<String>();
			ArrayList<UserChk> uArr = new LoginDB_Conn().getAll();

			for (UserChk s : uArr) {
				if(2020 - Integer.parseInt(s.birth.split("-")[0]) < 20) {
					chlid.add(s.id); //미성년자 
				}  
			}

			long time = System.currentTimeMillis();
			SimpleDateFormat time_1= new SimpleDateFormat("HH");
			String now_time = time_1.format(time); // hh시
			int t_time = Integer.parseInt(now_time);

			AAA : for (int i = 0; i < uArr.size(); i++) {

				if(uArr.get(i).id!=null&&uArr.get(i).pw!=null) {
					if(id.equals(uArr.get(i).id) && pw.equals(uArr.get(i).pw)) {
						for (int j = 0; j < chlid.size(); j++) {
							if((t_time >= 22 || t_time < 9) && chlid.get(j).equals(uArr.get(i).id)) {
								pp="10시 이후 로그인 불가";
								break AAA;
							}else {
								pp="로그인 성공";
								
								try {
									ui = new LoginDB_Conn().loginUser(uArr.get(i).id);
									ui.seat = seat;
									ui.useChk = true;
									
									if(ui.userTime == null || ui.userTime.equals("0:0")) {
										JOptionPane.showMessageDialog(null, "사용시간이 부족합니다.");
										return;
									}
									
									int birthYear = Integer.parseInt(new CustomerDAO().getBirthYear(ui.id));
									
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
									Date now = new Date();
									int thisYear = Integer.parseInt(sdf.format(now));
									
									System.out.println(thisYear+","+birthYear);
									
									if(thisYear - birthYear <= 20 && (t_time >= 22 || t_time < 9)) {
										JOptionPane.showMessageDialog(null, "미성년자는 이용할 수 없습니다.");
										return;
									}
									
									db = new DataBox("서버", "로그인"+seat, new MapReq());
									oos.writeObject(db);
									oos.flush();
									oos.reset();
									
								} catch (Exception e1) {
									e1.printStackTrace();
								}
								
								break AAA;
								
							}
						}
						
					}else {
						pp="다시 확인해주세요";

					}

				}
			}
			jl.setText(pp);


		}
	}

	class UserJoinBntAct implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(!joinChk) {
				cj = new CustomerJoin();
				joinChk = true;
			}else {
				cj.dispatchEvent(new WindowEvent(cj, WindowEvent.WINDOW_CLOSING));
				cj = new CustomerJoin();
			}

		}
	}

	public JoinId_gui(int seat, Time tt) {
		super("로그인");
		this.seat = seat;
		this.tt = tt;
		addWindowListener(new WindowClose(tt, this));

		try {
			socket = new Socket("127.0.0.1", 7777);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			
			oos.writeObject(new DataBox(null, "로그인"+seat, new RegistSignal()));
			oos.flush();
			oos.reset();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		setBounds(300, 300, 300, 400);
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
		member.addActionListener(new UserJoinBntAct());
		add(member);

		JButton serch = new JButton("아이디/비밀번호 검색");
		serch.setBounds(50, 260, 200, 30);
		serch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if(!serchChk) {
					ipser = new IdAndPwSearch();
					serchChk = true;
				}else {
					ipser.se.dispatchEvent(new WindowEvent(ipser.se, WindowEvent.WINDOW_CLOSING));
					ipser = new IdAndPwSearch();
				}


			}
		});
		add(serch);
		
		new Receiver().start();
		
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

	}

	class Receiver extends Thread {
		
		
		@Override
		public void run() {
			try {
				while(ois != null) {
					DataBox box = (DataBox)ois.readObject();

					if(box.data instanceof ArrayList) {
						ArrayList<String> arr = (ArrayList)box.data;
						if(!arr.contains(ui.id)) {
							db = new DataBox("관리자", ui.id, ui);
							oos.writeObject(db);
							oos.flush();
							oos.reset();
							
							db = new DataBox("서버", "로그인"+seat, new OffSignal());
							oos.writeObject(db);
							oos.flush();
							oos.reset();
							
						}else {
							JOptionPane.showMessageDialog(null, "이미 로그인된 회원입니다.");
						}
					}else if(box.data instanceof OffSignal) {
						chk = false;
						dispose();
						new UserUI(socket, ui, oos, ois, Calendar.getInstance(), tt);
						stop();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}

