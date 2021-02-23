package user;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import admin.GameUseStats;
import data.GameInfo;
import db.StaticIP;

class GameDB_Select{

	Connection con;
	Statement stmt;
	ResultSet rs;
	ArrayList<NameTime> gameDb = new ArrayList<NameTime>();

	public GameDB_Select(String sql) {

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@"+StaticIP.ip+":1521:xe",
					"hr",
					"hr");

			stmt = con.createStatement();

			rs = stmt.executeQuery(sql);

			while(rs.next()) {

				NameTime info = new NameTime(rs.getString("kind"),rs.getString("category"),rs.getString("time"));

				gameDb.add(info);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			close();
		}

	}

	public ArrayList<NameTime> getGameDb() {
		return gameDb;
	}

	public void close() {
		if(rs!=null) try {rs.close();} catch (SQLException e) {}
		if(stmt!=null) try {stmt.close();} catch (SQLException e) {}
		if(con!=null) try {con.close();} catch (SQLException e) {}
	}

}

class NameTime{
	String name;
	String time;
	String category;

	public NameTime(String name, String category ,String time) {
		super();
		this.name = name;
		this.category = category;
		this.time = time;
	}


}

class GameDB_Conn {
	
	public GameDB_Conn(String sql) {

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection(
					"jdbc:oracle:thin:@"+StaticIP.ip+":1521:xe",
					"hr",
					"hr");

			Statement stmt = con.createStatement();

			int cnt = stmt.executeUpdate(sql);

			con.close();
			stmt.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}


public class BestGame extends JFrame {
	JButton j;
	String category;
	ArrayList<NameTime> nt = new GameDB_Select("select * from game").getGameDb();

	boolean other = false;
	boolean chk2;
	TimeGame tg = null;	
	JFrame openF = null;
	String gname;
	TimeGo ti;


	class bestBntAct implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			j = (JButton)e.getSource();
			System.out.println(j.getText());

			if(openF!=null) {
				openF.dispose();
				openF=null;
			}

			switch (j.getText()) {

			case "인기게임":
				openF = new UserTGame();

				break;
			case "어드벤처":
				openF = new Game1();

				break;
			case "시뮬레이션":
				openF = new Game2();

				break;
			case "RPG":
				openF = new Game3();

				break;
			case "스포츠":
				openF = new Game4();

				break;
			case "퍼즐":
				openF = new Game5();

				break;
			}

			if(!j.getText().equals("인기게임"))
				category = j.getText();

			if(tg!=null) {
				tg.dispose();
				tg = null;
			}

		}

	}


	public BestGame() {
		super("게임 보기");

		setBounds(300, 100, 1000, 700);
		setLayout(null);

		JLabel jl = new JLabel("게임 선택");
		jl.setBounds(450, 0, 300, 200);
		jl.setOpaque(true);
		add(jl);

		JPanel jp = new JPanel();
		jp.setBackground(Color.pink);
		jp.setLayout(new GridLayout());
		jp.setBounds(100, 300, 800, 200);


		for (String str : "인기게임,어드벤처,시뮬레이션,RPG,스포츠,퍼즐".split(",")) {
			JButton bestBnt = new JButton(str);
			bestBnt.addActionListener(new bestBntAct());
			jp.add(bestBnt);
		}

		add(jp);
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);


	}

	String tt ="";
	String gameName;
	JLabel ing;
	boolean chk = false;

	void DbTime () {
		chk = true;		
		ArrayList<String> al = new ArrayList<String>();
		int hour = 0;
		int min =0;

		for (NameTime nn : nt) {
			if(nn.time==null) {
				nn.time = "0:0";
				if(gameName.equals(nn.name)) {
					for (String str : nn.time.split(":")) {
						al.add(str);

					}
				}
			}
		}

		ArrayList<String> tmp = new ArrayList<String>();
		for (String time : tt.split(":")) {
			tmp.add(time);
		}

		for (int i = 0; i < al.size(); i++) {

			hour = Integer.parseInt(al.get(i))+Integer.parseInt(tmp.get(i));
			min = Integer.parseInt(al.get(i))+Integer.parseInt(tmp.get(i));

		}

		other = false;

		new GameDB_Conn("update game set time = '"+hour+":"+min+"' where kind ='"+gameName+"'");

		if(tg!=null) {
			tg.dispose();
			tg = null;
		}

	}

	class TimeGame extends JFrame {

		class EndActBnt implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				ti.stop();
				DbTime();
				setVisible(false);

			}
		}

		public TimeGame() {
			super("타이머");

			setBounds(300, 100, 300, 300);
			setLayout(null);
			setVisible(true);

			ing = new JLabel();
			ing.setBounds(30, 100, 200, 40);
			ing.setBackground(Color.pink);
			ing.setOpaque(true);
			add(ing);

			JButton end = new JButton("끝내기");
			end.setBounds(100, 150, 100, 30);
			end.setEnabled(true);
			end.addActionListener(new EndActBnt());
			add(end);
			addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent e) {
					DbTime();
					chk = true;
				}
			});

			setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		}

		public JLabel getIng() {

			return ing;
		}

	}


	class TimeGo extends Thread {

		JLabel ing;
		String gName;

		public TimeGo(JLabel ing, String gName) {
			super();
			this.ing = ing;
			this.gName = gName;

		}

		@Override
		public void run() {
			try {
				int m = 0 ,h = 0; 

				while(true) {

					m++;

					if(chk)
						break;

					sleep(1000);

					if(m%60==0 && m!=0) {
						h++;
						m = 0;
					}

					tt = h+":"+m;
					ing.setText(gName+"    "+tt);

				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}

	}

	String game;
	String kind;

	class Game1BntTime  implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {

			JButton tmp = (JButton)e.getSource();

			chk = false;

			tg = new TimeGame();

			ti = new TimeGo(ing, tmp.getText());

			if(other) {
				tg.setVisible(false);
			}
			other = true;

			if(tg.isShowing()) {
				ti.start();
			}else {
				ti.stop();
			}

			gameName = tmp.getText();
			ing.setText(ing.getText()+"\t"+tmp.getText());

		}

	}

	class Game1 extends JFrame{

		public Game1() {
			super("어드벤처");
			setBounds(800, 400, 600, 600);
			setLayout(new GridLayout(3,3));

			int cnt = 0;
			for (NameTime nn : nt) {

				if(nn.category.equals(getTitle())) {
					cnt ++;
					JButton game1Bnt = new JButton(nn.name);
					game1Bnt.addActionListener(new Game1BntTime());
					add(game1Bnt);	
					if(cnt>=9)
						break;

				}
			}

			addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent e) {
					chk = true;
					DbTime();
				}
			});

			setVisible(true);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		}
	}

	class Game2 extends JFrame{
		public Game2() {
			super("시뮬레이션");
			setBounds(800, 400, 600, 600);
			setLayout(new GridLayout(3,3));

			int cnt = 0;
			for (NameTime nn : nt) {
				if(nn.category.equals(getTitle())) {
					JButton game1Bnt = new JButton(nn.name);
					cnt++;
					game1Bnt.addActionListener(new Game1BntTime());
					add(game1Bnt);	
					if(cnt>=9) 
						break;

				}
			}

			addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent e) {
					chk = true;
					DbTime();
				}
			});

			setVisible(true);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		}
	}

	class Game3 extends JFrame{
		public Game3() {
			super("RPG");
			setBounds(800, 400, 600, 600);
			setLayout(new GridLayout(3,3));
			int cnt = 0;
			for (NameTime nn : nt) {
				if(nn.category.equals(getTitle())) {
					cnt++;
					JButton game1Bnt = new JButton(nn.name);
					game1Bnt.addActionListener(new Game1BntTime());
					add(game1Bnt);	
					if(cnt>=9) 
						break;

				}
			}

			addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent e) {
					chk = true;
					DbTime();
				}
			});

			setVisible(true);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		}
	}

	class Game4 extends JFrame{
		public Game4() {
			super("스포츠");
			setBounds(800, 400, 600, 600);
			setLayout(new GridLayout(3,3));
			int cnt = 0;

			for (NameTime nn : nt) {
				if(nn.category.equals(getTitle())) {
					cnt ++;
					JButton game1Bnt = new JButton(nn.name);
					game1Bnt.addActionListener(new Game1BntTime());
					add(game1Bnt);	

					if(cnt>=9) 
						break;

				}
			}

			addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent e) {
					chk = true;
					DbTime();
				}
			});

			setVisible(true);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		}
	}

	class Game5 extends JFrame{
		public Game5() {
			super("퍼즐");
			setBounds(800, 400, 600, 600);
			setLayout(new GridLayout(3,3));
			int cnt = 0;
			for (NameTime nn : nt) {
				if(nn.category.equals(getTitle())) {
					cnt++;
					JButton game1Bnt = new JButton(nn.name);
					game1Bnt.addActionListener(new Game1BntTime());
					add(game1Bnt);	
					if(cnt>=9) 
						break;

				}
			}

			addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent e) {
					chk = true;
					DbTime();
				}
			});

			setVisible(true);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		}
	}

	class UserTGame extends JFrame{
		ArrayList<GameInfo> gameDb = new GameUseStats(false).getPop();
		public UserTGame() {
			setBounds(100, 300, 600, 600);
			setLayout(new GridLayout(3,3));
			for (GameInfo info : gameDb) {
				JButton jb = new JButton(info.info.get(0));
				jb.addActionListener(new Game1BntTime());
				add(jb);
			}

			setVisible(true);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		}
	}
	
	public static void main(String[] args) {
		new BestGame();
	}
}
