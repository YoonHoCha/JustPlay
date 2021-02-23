package user;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JToggleButton;

import admin.GameUseStats;
import data.GameInfo;
import db.StaticIP;


class UserGameDB_Conn{
	
	Connection con;
	Statement stmt;
	ResultSet rs;
	
	public UserGameDB_Conn(String sql) {
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

class GameData{ 
	
	String kind, category, time;

	public GameData(String kind, String category, String time) {
		super();
		this.kind = kind;
		this.category = category;
		this.time = time;
	}
	
	
}


class UserGame extends JFrame{
	boolean chk = false;
	ArrayList<GameInfo> gameDb = new GameUseStats(false).getPop();
	String tt;
	String gameName;
	boolean other = false;
class Timego extends JFrame{
	JLabel gName;
	
	class EndBntAct implements ActionListener{

		@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
			
			String pt = null;
			System.out.println(tt);
			ArrayList<Integer> time = new ArrayList<Integer>();
			
			ArrayList<Integer> dbTime = new ArrayList<Integer>();
			for (int i = 0; i < gameDb.size(); i++) {
				if(gameDb.get(i).info.get(0).equals(gameName)) {
					pt = gameDb.get(i).info.get(2);
				}
			}

			for (String  str : tt.split(":")) {
				time.add(Integer.parseInt(str));
			}
			
			
			for (String str : pt.split(":")) {
				dbTime.add(Integer.parseInt(str));
			}
			
			int h = time.get(0)+dbTime.get(0);
			int m = +dbTime.get(1)+time.get(1);
			String totTime = h+":"+m;
			
			new UserGameDB_Conn("update game set time = '"+totTime+"' where kind = '"+gameName+"'");
			other = false;
			chk = true;
			setVisible(false);
			
			}
	}

	
	
	public Timego() {
		// TODO Auto-generated constructor stub
		setBounds(600, 300, 300, 300);
		setLayout(new GridLayout());
		
		gName = new JLabel("**");
		gName.setBackground(Color.pink);
		gName.setOpaque(true);
		add(gName);
		
		
		JButton end = new JButton("³¡³»±â");
		end.setBounds(130, 200, 100, 30);
		end.addActionListener(new EndBntAct());
		add(end);
		
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		
	}

	public JLabel getgName() {
		return gName;
	}
	
	
}

class TimeRun extends Thread{
	JLabel gName;
	String name;
	public TimeRun(JLabel gName,String name) {
		// TODO Auto-generated constructor stub
		this.gName = gName;
		this.name = name;
	}
	
	@Override
	public void run() {
		
		int m = 0, h = 0;
		try {
			
			while(true) {
				m++;
				sleep(1000);
			
			
			if(m%60==0&& m!=0) {
				h++;
				m = 0;
			}
			
			
				tt = h+":"+m;	
				System.out.println(tt);
				gName.setText(name+"  "+tt);
				
				if(chk) 
					break;
				
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}

	class GameBntAct implements ActionListener{
	
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			JToggleButton jb = (JToggleButton)e.getSource();
			
			chk = false;
			Timego tg = new Timego();

			if(other) {
				tg.setVisible(false);
			}
			gameName = jb.getText();
			
			TimeRun tr = new TimeRun(tg.getgName(), jb.getText());
			other = true;
			
			if(tg.isShowing()) {
				tr.start();
			}
			
		}
		
	}

	
	public UserGame() {
		// TODO Auto-generated constructor stub
		setBounds(100, 300, 600, 600);
		setLayout(new GridLayout(3,3));
		ButtonGroup bg = new ButtonGroup();
		for (GameInfo info : gameDb) {
			JToggleButton jb = new JToggleButton(info.info.get(0));
			bg.add(jb);
			jb.addActionListener(new GameBntAct());
			add(jb);
		}
	
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
}


public class UserGame_gui {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new UserGame();
	}

}
