package user;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

class CustomerDB_Conn{

	Connection con;
	Statement stmt;
	ResultSet rs;
	ArrayList<CustomerData> userDb = new ArrayList<CustomerData>();

	public CustomerDB_Conn(String sql) {

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@"+StaticIP.ip+":1521:xe",
					"hr",
					"hr");

			stmt = con.createStatement();

			rs = stmt.executeQuery(sql);
			System.out.println("Ω««‡ »Æ¿Œ");
			while(rs.next()) {

				CustomerData info = new CustomerData(rs.getString("id"),rs.getString("pw"),rs.getString("name")
						,rs.getDate("birth"),rs.getString("tel"));

				userDb.add(info);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			close();
		}

	}

	public ArrayList<CustomerData> getGameDb() {
		return userDb;
	}

	public void close() {
		if(rs!=null) try {rs.close();} catch (SQLException e) {}
		if(stmt!=null) try {stmt.close();} catch (SQLException e) {}
		if(con!=null) try {con.close();} catch (SQLException e) {}
	}

}


class CustomerDB_Insert{

	Connection con;
	Statement stmt;
	ResultSet rs;

	public CustomerDB_Insert(String sql) {
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@"+StaticIP.ip+":1521:xe",
					"hr",
					"hr");

			stmt = con.createStatement();

			int cnt = stmt.executeUpdate(sql);
			System.out.println("Ω««‡ »Æ¿Œ"+cnt);


		} catch (Exception e) {
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

class CustomerData{

	String id,pw,name,tel;
	Date birth;

	public CustomerData(String id, String pw, String name, Date birth, String tel) {
		super();

		this.id = id;
		this.pw = pw;
		this.name = name;
		this.birth = birth;
		this.tel = tel;
	}

}

public class CustomerJoin extends JFrame{

	JLabel jl0;

	JTextField id_gui, name_gui,num2,num3;
	JPasswordField pw_gui,pwchk_gui;
	JComboBox<String> year_gui,mon_gui,day_gui;
	JButton join_gui;
	String pp;
	ArrayList<CustomerData> cData = new CustomerDB_Conn("select * from customer").getGameDb();
	String input_id;
	JComboBox<String> num1;
	
	class JoinBntAct implements ActionListener{

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

		@Override
		public void actionPerformed(ActionEvent e) {

			String id = id_gui.getText();
			System.out.println(id);
			String pw = pw_gui.getText();
			System.out.println(pw);
			String pwChk = pwchk_gui.getText();
			System.out.println(pwChk);

			String name = name_gui.getText();
			System.out.println(name);
			String year = (String)year_gui.getSelectedItem(); 
			String mon =(String)mon_gui.getSelectedItem();
			String dd = (String)day_gui.getSelectedItem();
			System.out.println(year+","+mon+","+dd);

			String tel = num1.getSelectedItem()+"-"+num2.getText()+"-"+num3.getText();
			System.out.println(tel);

			Date birth = new Date();
			
			
			try {
				if(!id.equals(input_id)) {
					JOptionPane.showMessageDialog(null, "æ∆¿Ãµ∏¶ ¡ﬂ∫π»Æ¿Œ «ÿ¡÷ººø‰.");
					return;
				}
					
				
				birth = sdf.parse(year+"/"+mon+"/"+dd);
				
				if(chk) {
					if(id_chk) {
						if(pw.equals(pwChk)) {
							try {
								
								chkSpace(pw, "∫Òπ– π¯»£");
								chkEmpty(pw, "∫Òπ– π¯»£");
								
								if(pw.length() < 7) {
									JOptionPane.showMessageDialog(null, "∫Òπ– π¯»£¿« √÷º“ ±€¿⁄ºˆ¥¬ 7±€¿⁄ ¿‘¥œ¥Ÿ.");
									throw new Exception();
								}
								
								
								
								chkName(name);
								
								chkPhone(num2.getText(), num3.getText());
								chkSpace(num2.getText(), "«⁄µÂ∆˘π¯»£");
								chkSpace(num3.getText(), "«⁄µÂ∆˘π¯»£");
							} catch (Exception e1) {
								return;
							}
							
							new CustomerDB_Insert("insert into customer (id, pw, name, birth, tel) values ('"
									+id+"','"+pw+"','"+name+"','"+sdf.format(birth)+"','"+tel+"')");
							JOptionPane.showMessageDialog(null, "»∏ø¯ ∞°¿‘ øœ∑·");
							dispose();
						}else {
							if(pwChk.equals("")) {
								JOptionPane.showMessageDialog(null, "∫Òπ–π¯»£»Æ¿Œ¿Ã ∞¯πÈ¿‘¥œ¥Ÿ.");
								return;
							}
							JOptionPane.showMessageDialog(null, "∫Òπ–π¯»£∞° ¿œƒ°«œ¡ˆ æ Ω¿¥œ¥Ÿ.");
						}
					}else {
						try {
							chkEmpty(id, "æ∆¿Ãµ");
							chkId(id);
						} catch (Exception e1) {}
					}
				}else{
					JOptionPane.showMessageDialog(null, "æ∆¿Ãµ∏¶ ¡ﬂ∫π»Æ¿Œ «ÿ¡÷ººø‰");
				}
				
			} catch (ParseException e1) {}
			

		}
	}

	boolean chk = false, id_chk;

	class IDBntAct implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			id_chk = false;
			chk = true;
			String id = id_gui.getText().trim();
			input_id = id_gui.getText().trim();
			pp = "";
			
			try {
				chkId(id);
				chkEmpty(id, "æ∆¿Ãµ");
			} catch (Exception e1) {
				return;
			}
			
			for (CustomerData cd : cData) {

				if(id.equals(cd.id)) {
					id_chk = false;
					pp = "¡ﬂ∫πµ» æ∆¿Ãµ ¿‘¥œ¥Ÿ.";
					break;
				}else {
					id_chk = true;
					pp="¿ÃøÎ ∞°¥…«— æ∆¿Ãµ ¿‘¥œ¥Ÿ.";
				}
			}
			JOptionPane.showMessageDialog(null, pp);
			pp = "";

		}

	}

	void chkName(String str) throws Exception {
		chkEmpty(str, "¿Ã∏ß");
		chkSpe(str);
		chkSpace(str, "¿Ã∏ß");
		
		String chk1 = "^[a-zA-Z0-9∞°-∆R]*$";
		
		Pattern pattern1 = Pattern.compile(chk1);
		Matcher matcher1 = pattern1.matcher(str);
		
		chkSpe(str);
		chkSpace(str, "¿Ã∏ß");
		
		if(!matcher1.matches()) {
			JOptionPane.showMessageDialog(null, "¿Ã∏ß¿ª »Æ¿Œ«ÿ ¡÷ººø‰.");	
			throw new Exception();
		}
		if(str.length() < 2) {
			JOptionPane.showMessageDialog(null, "¿Ã∏ß¿« √÷º“ ±Ê¿Ã¥¬ 2±€¿⁄ ¿‘¥œ¥Ÿ.");	
			throw new Exception();
		}
	}
	
	void chkSpace(String str, String title) throws Exception {
		String chk = "\\s";
		
		Pattern pattern = Pattern.compile(chk);
		Matcher matcher = pattern.matcher(str);
		
		while(matcher.find()) {
			JOptionPane.showMessageDialog(null, title+"ø° ∂ÁæÓæ≤±‚∏¶ ¡¶∞≈«ÿ ¡÷ººø‰.");	
			throw new Exception();
		}
		
	}
	
	void chkEmpty(String str, String title) throws Exception {
		if(str.equals("")) {
			JOptionPane.showMessageDialog(null, title + "¿« ∞¯πÈ¿ª √§øˆ¡÷ººø‰.");	
			throw new Exception();
		}
	}
	
	void chkSpe(String str) throws Exception {
		String chk = "[!@#$%^&*(),.?\\\":{}|<>]";
		
		Pattern pattern = Pattern.compile(chk);
		Matcher matcher = pattern.matcher(str);
		
		while(matcher.find()) {
			JOptionPane.showMessageDialog(null, "∆Øºˆ±‚»£¥¬ ªÁøÎ«œΩ« ºˆ æ¯Ω¿¥œ¥Ÿ.");	
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
		chkSpace(str, "æ∆¿Ãµ");
		
		if(!matcher2.matches()) {
			JOptionPane.showMessageDialog(null, "æ∆¿Ãµ∏¶ øµπÆ¿∏∑Œ Ω√¿€«ÿ ¡÷ººø‰");	
			throw new Exception();
		}
		
		if(!matcher1.matches()) {
			JOptionPane.showMessageDialog(null, "æ∆¿Ãµ∏¶ »Æ¿Œ«ÿ ¡÷ººø‰.");	
			throw new Exception();
		}
		
		if(str.length() > 10) {
			JOptionPane.showMessageDialog(null, "æ∆¿Ãµ¿« √÷¥Î ±Ê¿Ã¥¬ 10±€¿⁄ ¿‘¥œ¥Ÿ.");	
			throw new Exception();
		}else if(str.length() < 5) {
			JOptionPane.showMessageDialog(null, "æ∆¿Ãµ¿« √÷º“ ±Ê¿Ã¥¬ 5±€¿⁄ ¿‘¥œ¥Ÿ.");	
			throw new Exception();
		}
		
	}
	
	void chkPhone(String str1, String str2) throws Exception {
		String chk1 = "(?:\\d{3}|\\d{4})";
		String chk2 = "\\d{4}$";
		
		Pattern pattern1 = Pattern.compile(chk1);
		Matcher matcher1 = pattern1.matcher(str1);
		
		Pattern pattern2 = Pattern.compile(chk2);
		Matcher matcher2 = pattern2.matcher(str2);
		
		if(!matcher1.matches()) {
			JOptionPane.showMessageDialog(null, "«⁄µÂ∆˘ π¯»£∏¶ »Æ¿Œ«ÿ ¡÷ººø‰.");	
			throw new Exception();
		}
		
		if(!matcher2.matches()) {
			JOptionPane.showMessageDialog(null, "«⁄µÂ∆˘ π¯»£∏¶ »Æ¿Œ«ÿ ¡÷ººø‰.");	
			throw new Exception();
		}
	}
	
	public CustomerJoin() {
		super("»∏ø¯∞°¿‘");

		setBounds(300, 100, 500, 700);
		setLayout(null);

		jl0 = new JLabel();
		jl0.setBounds(100, 30, 300, 30);
		add(jl0);

		JLabel jl1 = new JLabel("æ∆¿Ãµ");
		jl1.setBounds(100, 80, 80, 40);
		add(jl1);

		id_gui = new JTextField();
		id_gui.setBounds(200, 80, 120, 40);
		add(id_gui);

		JButton idBnt = new JButton("¡ﬂ∫π»Æ¿Œ");
		idBnt.setBounds(330, 80, 100, 40);
		idBnt.addActionListener(new IDBntAct());
		add(idBnt);


		JLabel jl2 = new JLabel("∫Òπ–π¯»£");
		jl2.setBounds(100, 150, 80, 40);
		add(jl2);

		pw_gui = new JPasswordField();
		pw_gui.setBounds(200, 150, 120, 40);
		add(pw_gui);

		JLabel jl4 = new JLabel("∫Òπ–π¯»£»Æ¿Œ");
		jl4.setBounds(100, 220, 80, 40);
		add(jl4);

		pwchk_gui = new JPasswordField();
		pwchk_gui.setBounds(200, 220, 120, 40);
		add(pwchk_gui);

		JLabel jl3 = new JLabel("¿Ã∏ß");
		jl3.setBounds(100, 280, 80, 40);
		add(jl3);

		name_gui = new JTextField();
		name_gui.setBounds(200, 280, 120, 40);
		add(name_gui);

		JLabel jl5 = new JLabel("TEL");
		jl5.setBounds(70, 350, 80, 40);
		add(jl5);
		
		String [] tele = {"010","011","012","013","014","015","016","017","018","019"};
		num1 = new JComboBox<String>(tele);
		num1.setBounds(140, 350, 80, 30);
		num1.setSelectedIndex(0);
		add(num1);

		JLabel aa = new JLabel("-");
		aa.setBounds(230, 350, 10, 30);
		add(aa);

		num2 = new JTextField();
		num2.setBounds(240, 350, 80, 30);
		add(num2);

		JLabel bb = new JLabel("-");
		bb.setBounds(330, 350, 10, 30);
		add(bb);

		num3 = new JTextField();
		num3.setBounds(340, 350, 80, 30);
		add(num3);

		JLabel jl6 = new JLabel("ª˝≥‚ø˘¿œ");
		jl6.setBounds(30, 480, 80, 30);
		add(jl6);

		Vector<String> ymd1 = new Vector<String>();
		for (int i = 1920; i < 2020; i++) {			
			ymd1.add(i+"");
		}

		year_gui = new JComboBox<String>(ymd1);
		year_gui.setBounds(105, 480, 100, 30);
		add(year_gui);

		Vector<String> ymd2 = new Vector<String>();
		for (int i = 1; i <= 12; i++) {			
			ymd2.add(i+"");
		}

		mon_gui = new JComboBox<String>(ymd2);
		mon_gui.setBounds(220, 480, 100, 30);
		add(mon_gui);

		Vector<String> ymd3 = new Vector<String>();
		for (int i = 1; i <= 31; i++) {			
			ymd3.add(i+"");
		}

		day_gui = new JComboBox<String>(ymd3);
		day_gui.setBounds(340, 480, 100, 30);
		add(day_gui);



		join_gui = new JButton("»∏ø¯∞°¿‘");
		join_gui.setBounds(200, 550, 100, 30);
		join_gui.addActionListener(new JoinBntAct());
		add(join_gui);
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

	}

}
