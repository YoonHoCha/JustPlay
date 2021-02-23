package user;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

//192.168.1.39  172 30 1 15
class Stock_chk{
	String menu;


	public Stock_chk(String menu) {
		super();
		this.menu=menu;
	}
	@Override
	public String toString() {
		// TODO Auto-generated metshod stub
		return  menu;
	}

}

class cash_chk{ 
	int cash; 

	public cash_chk(int cash) {
		// TODO Auto-generated constructor stub
		super();
		this.cash=cash;

	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return cash+"";
	}
}
class cash_chk_DB{//fee ������ 
	cash_chk cc;
	ArrayList<cash_chk> clist =new ArrayList<cash_chk>();

	public cash_chk_DB(String sql) {

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

			Connection con = DriverManager.getConnection(
					"jdbc:oracle:thin:@192.168.1.41:1521:xe",
					"hr",
					"hr");

			Statement stmt = con.createStatement();

			ResultSet rs= stmt.executeQuery(sql);
			while(rs.next()) {
				cc= new cash_chk(rs.getInt("cash"));

				//db���� ������ �� : �̸� ���̵� �ð� 
				clist.add(cc);
			}
	//		for (cash_chk chk : clist) {
//				System.out.println(chk);
	//		}
			rs.close();		
			stmt.close();
			con.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	ArrayList<cash_chk> getslist(){
		return clist;
	}
}

class New_dbconnect_3{		
	public New_dbconnect_3(String sql) {
		//172.16.101.162
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

			Connection con = DriverManager.getConnection(
					"jdbc:oracle:thin:@192.168.1.41:1521:xe", 
					"hr", 
					"hr");

			Statement stmt = con.createStatement();


			//String sql2="delete from fee where ��� = "+aa;
			int cnt = stmt.executeUpdate(sql);

			//System.out.println("���� ����: "+ cnt);

			stmt.close();
			con.close();

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
class Stock_chk_DB{//fee ������ 
	Stock_chk sc;
	ArrayList<Stock_chk> slist =new ArrayList<Stock_chk>();

	public Stock_chk_DB(String sql) {

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

			Connection con = DriverManager.getConnection(
					"jdbc:oracle:thin:@192.168.1.41:1521:xe",
					"hr",
					"hr");

			Statement stmt = con.createStatement();

			ResultSet rs= stmt.executeQuery(sql);
			while(rs.next()) {
				sc= new Stock_chk(rs.getString("menu"));

				slist.add(sc);
			}
		//	for (Stock_chk pchk : slist) {
		//		System.out.println(pchk);
		//	}
			rs.close();		
			stmt.close();
			con.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	ArrayList<Stock_chk> getslist(){
		return slist;
	}
}

class calc_chk{
	String time, id;


	public calc_chk(String time, String id) {
		super();
		this.time=time;
		this.id=id;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return  time + id ;
	}

}

class calc_DB{//calc ������ 
	calc_chk cc;
	ArrayList<calc_chk> cclist =new ArrayList<calc_chk>();
	SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd hh:mm:ss");

	public calc_DB(String sql) {

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

			Connection con = DriverManager.getConnection(
					"jdbc:oracle:thin:@192.168.1.41:1521:xe",
					"hr",
					"hr");

			Statement stmt = con.createStatement();

			ResultSet rs= stmt.executeQuery(sql);
			while(rs.next()) {
				cc= new calc_chk(sdf.format(rs.getTimestamp("time")),rs.getString("id"));

				cclist.add(cc);
			}
	//		for (calc_chk chk : cclist) {
	//			System.out.println(chk);
	//		}
			rs.close();		
			stmt.close();
			con.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	ArrayList<calc_chk> getslist(){
		return cclist;
	}
}
class Cla_Flame extends JFrame{
	JTable calc_jp_jt;

	CardLayout card;
	JPanel in_jp;
	JButton calc_bt;
	JTextField id_input;
	ArrayList<Stock_chk> slist;
	ArrayList<cash_chk> clist;
	String Pname;//��ǰ�̸�
	DefaultTableModel model,model2;
	JLabel cash_chk;  
	
	
	class calc_act implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub

			card.show(in_jp, "�����г�");
			slist = new Stock_chk_DB("select * from stock").getslist();

			clist = new cash_chk_DB("select * from vault").getslist();
			//System.out.println(clist);//�޴� ������ 
			cash_chk.setText("�ݰ�: "+clist.get(0).cash);

			//���̺� ���� �̸��� ���� �� ++ 
			TreeMap<String, Integer> category_index = new TreeMap<String, Integer>(); 
			int c = 0;

			for (int i = 0; i < slist.size(); i++) {

				if(category_index.containsKey(slist.get(i).menu)) {
					c = category_index.get(slist.get(i).menu);

				}
				for (int j = 0; j < slist.size(); j++) {

					if(slist.get(i).menu.equals(slist.get(j).menu)) {
					}
				}
				model.setNumRows(0);//�ʱ�ȭ

				c++;

				category_index.put(slist.get(i).menu, c);
				c=0;
			}

			for (Entry<String, Integer> cc : category_index.entrySet()) {

				model.addRow(new Object [] {cc.getKey(),cc.getValue()});

			}



		}

	}
	String adminId="admin";
	class calc_act2 implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(adminId.equals("aaa")) {
			card.show(in_jp, "������Ȯ���г�");
			}else {
				JOptionPane.showMessageDialog(null, "���������� �����ϴ�.", "Msg", JOptionPane.ERROR_MESSAGE);
			}
			}
	}

	class calc_chk_act implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(adminId.equals("aaa")) {
			card.show(in_jp, "������Ȯ���г�");
			}
			adminId="admin";
			
			SimpleDateFormat format1 = new SimpleDateFormat ( "yy/MM/dd hh:mm:ss");


			Date time = new Date();
			String time1 = format1.format(time);


			model2.addRow(new Object [] {time1,adminId});
			//			chk_date.add(new New_dbconnect("select * from calc"));
			//db�� ������ 
		new New_dbconnect("insert into calc (time, id) values ('"+time1+"','"+adminId+"')");
		//	}
			}

	}

	public Cla_Flame() {

		super("����");
		setBounds(600, 20, 600, 600);
		setLayout(null); 



		setVisible(true);


		cash_chk = new JLabel();
		cash_chk.setBounds(380, 20, 130, 40);
		cash_chk.setText(("�ݰ� : "+""));
		add(cash_chk);

		JButton calc = new JButton("����");
		calc.setBounds(20, 20,70, 30);
		add(calc);
		calc.addActionListener(new calc_act());



		JButton calc_chk = new JButton("������Ȯ��");
		calc_chk.setBounds(160, 20, 130, 30);

		add(calc_chk);
		calc_chk.addActionListener(new calc_act2());

		in_jp = new JPanel();
		in_jp.setBounds(40, 60, 500, 480);
		in_jp.setBackground(Color.pink);
		in_jp.setLayout(card = new CardLayout());
		in_jp.add("�����г�", new calc_jp());
		in_jp.add("������Ȯ���г�", new calc_chk_jp());
		add(in_jp);


	}


	class calc_jp extends JPanel{
		public calc_jp() {
			// TODO Auto-generated constructor stub
			super(); 
			setBounds(40, 80, 500, 500);

			//setBackground(Color.white);

			Object [] index= {"�����Ȳ","����"};
			model = new DefaultTableModel(index, 0) {	
			public boolean isCellEditable (int i, int c) {
				return false;
			}
		};
			JTable calc_jp_jt = new JTable(model);
			JScrollPane calc_jp_jtSP = new JScrollPane(calc_jp_jt);
			calc_jp_jtSP.setBounds(30,50, 300, 300);
			add(calc_jp_jtSP);
			calc_bt = new JButton("����Ȯ��");
			calc_bt.setBounds(230, 430, 30, 30);
			//d���� ������ 
			calc_bt.addActionListener(new calc_chk_act());
			add(calc_bt);
			setVisible(false);


		}
	}


	class calc_chk_jp extends JPanel{
		public calc_chk_jp() {
			// TODO Auto-generated constructor stub
			super(); 
			setBounds(40, 80, 500, 450);
			setBackground(Color.blue);
			ArrayList <calc_chk> cclist = new calc_DB("select * from calc").cclist;
			Object [] index= {"����ð�","������ ID"};
			model2 = new DefaultTableModel(index, 0) {	
			public boolean isCellEditable (int i, int c) {
				return false;
			}
		};
		

			for (calc_chk aa : cclist) {
				Vector in = new Vector();
				in.add(aa.time);
				in.add(aa.id);
				model2.addRow(in);
			}

			calc_jp_jt = new JTable(model2);
			JScrollPane calc_jp_jtSP = new JScrollPane(calc_jp_jt);
			calc_jp_jtSP.setBounds(30,50, 300, 300);

			add(calc_jp_jtSP);

			setVisible(false);
		}
	}
}

public class calu_Gui {

	public static void main(String[] args) {//���� 
		new Cla_Flame();

	}

}
