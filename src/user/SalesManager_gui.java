package user;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import db.StaticIP;

class SaleDB_Conn {

	ArrayList<SalesDataInFo> saleDb = new ArrayList<SalesDataInFo>();
	ArrayList<ProDataInFo> proDb;
	ArrayList<Integer> res;
	Connection con;
	Statement stmt;
	ResultSet rs;

	public SaleDB_Conn(String sql) {

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection(
					"jdbc:oracle:thin:@"+StaticIP.ip+":1521:xe",
					"hr",
					"hr");

			stmt = con.createStatement();

			rs = stmt.executeQuery(sql);

			while(rs.next()) {

				SalesDataInFo info = new SalesDataInFo(rs.getDate("TIME"), rs.getString("ID"), 
						rs.getString("item"),
						rs.getInt("FEE"), rs.getString("PAY"));

				saleDb.add(info);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			close();
		}

	}

	public ArrayList<SalesDataInFo> getSaleDb() {
		return saleDb;
	}

	public void close() {
		if(rs!=null) try {rs.close();} catch (SQLException e) {}
		if(stmt!=null) try {stmt.close();} catch (SQLException e) {}
		if(con!=null) try {con.close();} catch (SQLException e) {}
	}

}

class ProDB_Conn{

	ArrayList<ProDataInFo> proDb = new ArrayList<ProDataInFo>();
	Connection con;
	Statement stmt;
	ResultSet rs;
	
	public ProDB_Conn(String sql) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection(
					"jdbc:oracle:thin:@"+StaticIP.ip+":1521:xe",
					"hr",
					"hr");

			stmt = con.createStatement();

			rs = stmt.executeQuery(sql);

			while(rs.next()) {

				ProDataInFo info = new ProDataInFo(rs.getString("menu"),
						rs.getInt("price"));

				proDb.add(info);
			}

			rs.close();
			con.close();
			stmt.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			close();
		}

	}
	
	public ArrayList<ProDataInFo> getProDb() {
		return proDb;
	}


	public void close() {
		if(rs!=null) try {rs.close();} catch (SQLException e) {}
		if(stmt!=null) try {stmt.close();} catch (SQLException e) {}
		if(con!=null) try {con.close();} catch (SQLException e) {}
	}

}

class SalesDataInFo{   ///일일

	int fee,proPri;
	String item,id,pay,proName;
	Date time;
	ArrayList<SalesDataInFo> saleDb;
	ArrayList<ProDataInFo> proDb;

	public SalesDataInFo(Date time,String id,String item,int fee, String pay) {
		super();
		this.fee = fee;
		this.item = item;
		this.id = id;
		this.pay = pay;
		this.time = time;

	}

	@Override
	public String toString() {
		return fee + ", " + item + ", " + id + ", " + pay + ", " + time ;
	}

}

class ProDataInFo{

	String proName,proKind,imgPath;
	int proPri;

	public ProDataInFo(String proName, int proPri) {
		super();
		this.proName = proName;
		this.proPri = proPri;
		this.proKind = proKind;
		this.imgPath = imgPath;
	}

	@Override
	public String toString() {
		return "ProDataInFo [proName=" + proName + ", proPri=" + proPri + "]";
	}

}

class SaleManage extends JFrame{
	DefaultTableModel model1;
	DefaultTableModel model2;
	TreeMap<String, ArrayList<Integer>> tm1;
	TreeMap<String, ArrayList<Integer>> tm2;
	ArrayList<TreeMap<String, ArrayList<Integer>>> arr;
	HashMap <String, ArrayList<TreeMap<String, ArrayList<Integer>>>> dateTm;
	CardLayout card;
	JPanel jp;

	Date ymd1;
	Date ymd2;

	JDateChooser dateChooser;
	JDateChooser dateChooser_1;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	ArrayList<ProDataInFo> proDb;
	ArrayList<SalesDataInFo> saleDb;
	ArrayList<SalesDataInFo> saleTm;

	JTextField chkchk;
	JTextField jf1;

	boolean totUserChk;
	boolean totUserTable;
	boolean timeUserChk;
	boolean timeUserTable;

	TimeUserTable tud; // 시간테이블
	TimeUserData tut;	// 시간 검색
	TotUserSales tus = null;	//일일 테이블
	UserTotChk utc = null; // 일일 검색
	JTable userTb;
	JTable userDb;

	JFrame openF = null;

	boolean tot1;
	boolean tot2;
	boolean time1;
	boolean time2;
	class todayBntAct implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			card.show(jp, "일일매출");

		}
	}

	class timeBntAct implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			card.show(jp, "기간매출");

		}
	}

	public SaleManage() {
		super("매출관리");
		proDb = new ProDB_Conn("select * from item").getProDb();
		Date y1 = new Date();
		y1.setHours(0);
		y1.setMinutes(0);
		y1.setSeconds(0);
		Date y2 = new Date();
		y2.setDate(y2.getDate());
		y2.setHours(23);
		y2.setMinutes(59);
		y2.setSeconds(59);


		saleDb = new SaleDB_Conn("select * from log where TIME between '"+sdf.format(y1)+"' and '"+sdf.format(y2)+"'").getSaleDb();
		setBounds(300, 100, 1000, 700);
		setLayout(null);

		card = new CardLayout();
		jp = new JPanel();
		jp.setBounds(50, 80, 900, 650);
		jp.setLayout(card);

		jp.add("일일매출",new SaleToday());
		jp.add("기간매출",new SaleTime());
		add(jp);

		JButton todayBnt = new JButton("일일");
		todayBnt.setBounds(30, 30, 60, 30);
		todayBnt.addActionListener(new todayBntAct());
		add(todayBnt);

		JButton timeBnt = new JButton("기간");
		timeBnt.setBounds(100, 30, 60, 30);
		timeBnt.addActionListener(new timeBntAct());
		add(timeBnt);


		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

	}

	class TotUserSales extends JFrame{
		public TotUserSales() {
			super ("사용정보");	

			addWindowListener(new WindowAdapter() {
				@Override
				public void windowOpened(WindowEvent e) {
					tot2 = true;
				}

				@Override
				public void windowClosed(WindowEvent e) {
					// TODO Auto-generated method stub
					tot2 = false;
				}
			});

			setBounds(100, 300, 300, 300);
			setLayout(null);
			JLabel jm = new JLabel();
			jm.setBounds(20, 0, 200, 30);
			add(jm);
			JLabel j0 = new JLabel();
			j0.setBounds(20, 30, 200, 30);
			add(j0);
			JLabel jl = new JLabel();
			jl.setBounds(20, 60, 200, 30);
			add(jl);

			if(!tot1) {
				setVisible(true);
			}else {
				dispose();
			}

			ArrayList<String> item = new ArrayList<String>();
			for (SalesDataInFo sdi : saleDb) {
				if(sdi.id.equals(userTb.getModel().getValueAt(userTb.getSelectedRow(), 0))){
					jm.setText("아이디 :"+sdi.id);
					Date now = (Date)sdi.time.clone();
					now.setDate(sdi.time.getDate());
					j0.setText("날짜  :"+now);

					for (ProDataInFo pro : proDb) {
						if(sdi.item==null)
							continue;
						if(sdi.item.equals(pro.proName)) {
							item.add(pro.proName+"\t"+sdi.time);      
						}
					}
				}
			}
			jl.setText("주문금액  : "+userTb.getModel().getValueAt(userTb.getSelectedRow(), 2)+"원");

			JTextArea ja = new JTextArea();
			JScrollPane jsp = new JScrollPane(ja);
			jsp.setBounds(20, 100, 240, 100);
			String ttt = "";
			for (String str : item) {
				if(str==null) {

				}else {
					ttt += str+"\n";

				}
			}
			ja.setText(ttt);
			ja.setEditable(false);

			add(jsp);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);			

		}
	}

	class UserTotChk extends JFrame{
		public UserTotChk() {
			super("검색결과");

			addWindowListener(new WindowAdapter() {
				@Override
				public void windowOpened(WindowEvent e) {
					tot1 = true;

				}

				@Override
				public void windowClosed(WindowEvent e) {
					tot1 = false;
				}

			});

			ArrayList<String> item = new ArrayList<String>();
			setBounds(100, 300, 300, 300);
			setLayout(null);
			JLabel jm = new JLabel();
			jm.setBounds(20, 0, 200, 30);
			add(jm);
			JLabel j0 = new JLabel();
			j0.setBounds(20, 30, 200, 30);
			add(j0);
			JLabel jl = new JLabel();
			jl.setBounds(20, 60, 200, 30);
			add(jl);

			boolean chk = false;

			for (SalesDataInFo sdi : saleDb) {
				if(sdi.id.equals(jf1.getText())) {
					jm.setText("아이디 :"+sdi.id);
					Date now = (Date)sdi.time.clone();
					now.setDate(sdi.time.getDate());
					j0.setText("날짜  :"+now);
					chk = true;

					for (ProDataInFo pro : proDb) {
						if(sdi.item==null)
							continue;
						if(sdi.item.equals(pro.proName)) {
							item.add(pro.proName+"\t"+sdi.time);      
							setVisible(true);
						}
					}
				}
			}

			if(!tot2) {
				if(!chk) {
					setVisible(false);
					JOptionPane.showMessageDialog(null, "없는 아이디 입니다", "Message", JOptionPane.ERROR_MESSAGE);
				}else {
					setVisible(true);
				}
			}else {
				dispose();
			}

			JTextArea ja = new JTextArea();
			JScrollPane jsp = new JScrollPane(ja);
			jsp.setBounds(20, 100, 240, 100);
			String ttt = "";
			for (String str : item) {	
				if(str==null) {

				}else {
					ttt += str+"\n";

				}
			}
			ja.setText(ttt);

			add(jsp);
			ja.setEditable(false);

			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			jf1.setText("");
		}

	}

	class SaleToday extends JPanel implements MouseListener{

		JLabel totSale;
		JLabel totCard;
		JLabel totCash;

		class BntAct implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(!totUserChk) {
					utc = new UserTotChk();
					totUserChk = true;
				}else {
					utc.dispatchEvent(new WindowEvent(utc, WindowEvent.WINDOW_CLOSING));
					utc = new UserTotChk();
				}			

			}
		}

		public SaleToday() {
			setBounds(50, 80, 900, 650);
			setLayout(null);
			setOpaque(true);

			JLabel totLb = new JLabel("총매출");
			totLb.setBounds(30, 100, 60, 30);
			add(totLb);

			totSale = new JLabel("");
			totSale.setBounds(30, 130, 60, 30);
			add(totSale);

			JLabel cardLb  = new JLabel("카드");
			cardLb.setBounds(30, 200, 60, 30);
			add(cardLb);

			totCard  = new JLabel("");
			totCard.setBounds(30, 230, 60, 30);
			add(totCard);

			JLabel cashLb = new JLabel("현금");
			cashLb.setBounds(30, 300, 60, 30);
			add(cashLb);

			totCash = new JLabel("");
			totCash.setBounds(30, 330, 60, 30);
			add(totCash);

			Object [] index = {"아이디","충전금액","주문금액"};

			model1 =  new DefaultTableModel(index,0) {

				public boolean isCellEditable(int i, int c) {
					return false;
				}

			};

			totCalc();

			userTb = new JTable(model1);
			userTb.addMouseListener(this);
			JScrollPane jsp = new JScrollPane(userTb);
			jsp.setBounds(200, 50, 650, 500);
			jsp.setOpaque(true);
			add(jsp);

			JLabel jj = new JLabel("아이디 검색");
			jj.setBounds(20, 400, 100, 20);
			add(jj);

			jf1 = new JTextField();
			jf1.setBounds(20, 430, 80, 30);
			add(jf1);

			JButton jb = new JButton("검색");
			jb.setBounds(20, 470, 80, 30);
			jb.addActionListener(new BntAct());
			add(jb);

		}

		void totCalc() {
			Vector tot = new Vector();
			int cardPP = 0;
			int cashPP = 0;
			tm1 = new TreeMap<String, ArrayList<Integer>>();

			for (int i = 0; i < saleDb.size(); i++) {

				ArrayList<Integer> tmp = new ArrayList<Integer>();
				if(tm1.containsKey(saleDb.get(i).id))
					tmp = tm1.get(saleDb.get(i).id);

				else {
					tmp.add(0);
					tmp.add(0);
				}
				if(saleDb.get(i).pay==null) 
					continue;

				if(saleDb.get(i).pay.equals("card")) {
					if(saleDb.get(i).fee != 0){
						tmp.set(1, tmp.get(1)+saleDb.get(i).fee);
						cardPP += saleDb.get(i).fee;
					}else {
						for (int j = 0; j < proDb.size(); j++) {
							if(saleDb.get(i).item != null &&saleDb.get(i).item.equals(proDb.get(j).proName)) {
								tmp.set(0, tmp.get(0)+proDb.get(j).proPri);
								cardPP += proDb.get(j).proPri;
							}
						}
					}
				} else {
					if(saleDb.get(i).fee != 0){
						tmp.set(1, tmp.get(1)+saleDb.get(i).fee);
						cashPP += saleDb.get(i).fee;
					}else {
						for (int j = 0; j < proDb.size(); j++) {
							if(saleDb.get(i).item != null && saleDb.get(i).item.equals(proDb.get(j).proName)) {
								tmp.set(0, tmp.get(0)+proDb.get(j).proPri);
								cashPP += proDb.get(j).proPri;
							}
						}
					}
				}
				tm1.put(saleDb.get(i).id, tmp);
			}
			System.out.println(tm1);

			for (Entry<String, ArrayList<Integer>> info : tm1.entrySet()) {

				System.out.println(info);
				tot = new Vector();

				tot.add(info.getKey());
				tot.add(info.getValue().get(1));
				tot.add(info.getValue().get(0));
				model1.addRow(tot);

			}

			totSale.setText(""+(cardPP+cashPP));
			totCard.setText(""+cardPP);
			totCash.setText(""+cashPP);
		}

		@Override
		public void mouseClicked(MouseEvent e) {

			if(!totUserTable) {
				tus = new TotUserSales();
				totUserTable = true;
			}else {
				tus.dispatchEvent(new WindowEvent(tus, WindowEvent.WINDOW_CLOSING));
				tus = new TotUserSales();
			}

		}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
		@Override
		public void mousePressed(MouseEvent e) {}
		@Override
		public void mouseReleased(MouseEvent e) {}

	}

	class TimeUserData extends JFrame{
		public TimeUserData() {

			super("검색결과");			

			ymd1 = new Date(dateChooser.getCalendar().getTimeInMillis());
			ymd1.setHours(0);
			ymd1.setMinutes(0);
			ymd1.setSeconds(0);
			ymd2 = new Date(dateChooser_1.getCalendar().getTimeInMillis());
			ymd2.setHours(23);
			ymd2.setMinutes(59);
			ymd2.setSeconds(59);

			ArrayList<SalesDataInFo> arr = new SaleDB_Conn("select * from log where time between '"
					+sdf.format(ymd1)+"' and '"+sdf.format(ymd2)+"'").getSaleDb();
			proDb = new ProDB_Conn("select * from item").getProDb();


			addWindowListener(new WindowAdapter() {
				@Override
				public void windowOpened(WindowEvent e) {
					time1 = true;
				}

				@Override
				public void windowClosed(WindowEvent e) {
					time1 = false;
				}

			});

			setBounds(100, 300, 300, 300);
			setLayout(null);
			JLabel jm = new JLabel();
			jm.setBounds(20, 0, 200, 30);
			add(jm);
			JLabel j0 = new JLabel();
			j0.setBounds(20, 30, 200, 30);
			add(j0);
			JLabel jl = new JLabel();
			jl.setBounds(20, 60, 200, 30);
			add(jl);

			ArrayList<String> item = new ArrayList<String>();
			boolean chk = false;

			if(arr.size()!=0) {
				for (SalesDataInFo sdi : arr) {
					if(sdi.id.equals(chkchk.getText())) {
						jm.setText("아이디 :"+sdi.id);
						Date now = (Date)sdi.time.clone();
						now.setDate(sdi.time.getDate());
						j0.setText("날짜  :"+now);
						chk = true;
						for (ProDataInFo pro : proDb) {
							if(sdi.item==null)
								continue;
							if(sdi.item.equals(pro.proName)) {
								item.add(pro.proName+"\t"+sdi.time);      
							}
						}
					}
				}
			}else {
				setVisible(false);
			}


			JTextArea ja = new JTextArea();
			JScrollPane jsp = new JScrollPane(ja);
			jsp.setBounds(20, 100, 240, 100);
			String ttt = "";
			for (String str : item) {
				if(str==null) {

				}else {
					ttt += str+"\n";

				}
			}
			ja.setText(ttt);

			add(jsp);

			if(time2) {
				dispose();

			}else {
				if(!chk) {
					setVisible(false);
					JOptionPane.showMessageDialog(null, "없는 아이디 입니다", "Message", JOptionPane.ERROR_MESSAGE);
				}else {
					setVisible(true);
				}
			}
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			chkchk.setText("");

		}
	}

	class TimeUserTable extends JFrame{
		/////// 테이블 누르면 뜨는 정보
		public TimeUserTable() {
			super("사용정보");			

			addWindowListener(new WindowAdapter() {
				@Override
				public void windowOpened(WindowEvent e) {
					time2 = true;
				}

				@Override
				public void windowClosed(WindowEvent e) {
					time2 = false;
				}
			});

			setBounds(100, 300, 300, 300);
			setLayout(null);
			JLabel jm = new JLabel();
			jm.setBounds(20, 0, 200, 30);
			add(jm);
			JLabel j0 = new JLabel();
			j0.setBounds(20, 30, 200, 30);
			add(j0);
			JLabel jl = new JLabel();
			jl.setBounds(20, 60, 200, 30);
			add(jl);

			if(!time1) {
				setVisible(true);
			}else {
				dispose();
			}

			System.out.println(userDb.getModel().getValueAt(userDb.getSelectedRow(), 0));
			ArrayList<String> item = new ArrayList<String>();
			for (SalesDataInFo sdi : saleTm) {
				if(sdi.id.equals(userDb.getModel().getValueAt(userDb.getSelectedRow(), 0))){

					jm.setText("아이디 :"+sdi.id);
					Date now = (Date)sdi.time.clone();
					now.setDate(sdi.time.getDate());
					j0.setText("마지막 접속 날짜  :"+now);

					for (ProDataInFo pro : proDb) {
						if(sdi.item==null)
							continue;
						if(sdi.item.equals(pro.proName)) {
							item.add(pro.proName+"\t"+sdi.time);      
						}
					}
				}
			}
			jl.setText("주문금액  : "+userDb.getModel().getValueAt(userDb.getSelectedRow(), 2)+"원");

			JTextArea ja = new JTextArea();
			JScrollPane jsp = new JScrollPane(ja);
			jsp.setBounds(20, 100, 240, 100);
			String ttt = "";
			for (String str : item) {
				if(str==null) {

				}else {
					ttt += str+"\n";

				}
			}
			ja.setText(ttt);
			ja.setEditable(false);
			add(jsp);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		}

	}
	class SaleTime extends JPanel implements MouseListener{

		boolean timeChk;
		boolean timeUserChk;

		Vector vv;
		JButton chk;

		JLabel tmCash;
		JLabel tmTot;
		JLabel tmCard;

		class ChkAct implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent e) {

				model2.setNumRows(0);					

				ymd1 = new Date(dateChooser.getCalendar().getTimeInMillis());
				ymd1.setHours(0);
				ymd1.setMinutes(0);
				ymd1.setSeconds(0);
				ymd2 = new Date(dateChooser_1.getCalendar().getTimeInMillis());
				ymd2.setHours(23);
				ymd2.setMinutes(59);
				ymd2.setSeconds(59);

				saleTm = new SaleDB_Conn("select * from log where time between '"
						+sdf.format(ymd1)+"' and '"+sdf.format(ymd2)+"'").getSaleDb();

				arr = new ArrayList<TreeMap<String, ArrayList<Integer>>>();
				dateTm = new HashMap<String, ArrayList<TreeMap<String, ArrayList<Integer>>>>();
				tm2= new TreeMap<String, ArrayList<Integer>>();

				int cardTm = 0;
				int cashTm = 0;         

				for (int i = 0; i < saleTm.size(); i++) {

					ArrayList<Integer> tmp = new ArrayList<Integer>();
					if(tm2.containsKey(saleTm.get(i).id))
						tmp = tm2.get(saleTm.get(i).id);

					else {
						tmp.add(0);
						tmp.add(0);
					}
					if(saleTm.get(i).pay==null) 
						continue;

					if(saleTm.get(i).pay.equals("card")) {
						if(saleTm.get(i).fee != 0){
							tmp.set(1, tmp.get(1)+saleTm.get(i).fee);
							cardTm += saleTm.get(i).fee;
						}else {
							for (int j = 0; j < proDb.size(); j++) {
								if(saleTm.get(i).item != null &&saleTm.get(i).item.equals(proDb.get(j).proName)) {
									tmp.set(0, tmp.get(0)+proDb.get(j).proPri);
									cardTm += proDb.get(j).proPri;
								}
							}
						}
					} else {
						if(saleTm.get(i).fee != 0){
							tmp.set(1, tmp.get(1)+saleTm.get(i).fee);
							cashTm += saleTm.get(i).fee;
						}else {
							for (int j = 0; j < proDb.size(); j++) {
								if(saleTm.get(i).item != null && saleTm.get(i).item.equals(proDb.get(j).proName)) {
									tmp.set(0, tmp.get(0)+proDb.get(j).proPri);
									cashTm += proDb.get(j).proPri;
								}
							}
						}
					}
					tm2.put(saleTm.get(i).id, tmp);
				}

				for (Entry<String, ArrayList<Integer>> info : tm2.entrySet()) {

					vv = new Vector();

					vv.add(info.getKey());
					vv.add(info.getValue().get(1));
					vv.add(info.getValue().get(0));
					model2.addRow(vv);

				}

				tmTot.setText(""+(cardTm+cashTm));
				tmCard.setText(""+cardTm);
				tmCash.setText(""+cashTm);

			}

		}

		class TimeBntAct implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent e) {
				if(ymd1!=null&&ymd2!=null) {
					if(!timeUserChk) {
						tut = new TimeUserData();
						timeUserChk = true;
					}else {
						tut.dispatchEvent(new WindowEvent(tut, WindowEvent.WINDOW_CLOSING));
						tut = new TimeUserData();
					}
				}else {
					JOptionPane.showMessageDialog(null, "날짜를 선택해주세요", "Message", JOptionPane.ERROR_MESSAGE);
				}

			}

		}


		public SaleTime() {
			setBounds(50, 80, 900, 650);
			setLayout(null);
			setOpaque(true);


			JLabel totLb = new JLabel("총매출");
			totLb.setBounds(30, 100, 60, 30);
			add(totLb);

			tmTot = new JLabel("");
			tmTot.setBounds(30, 130, 60, 30);
			add(tmTot);

			JLabel cardLb  = new JLabel("카드");
			cardLb.setBounds(30, 200, 60, 30);
			add(cardLb);

			tmCard  = new JLabel("");
			tmCard.setBounds(30, 230, 60, 30);
			add(tmCard);

			JLabel cashLb = new JLabel("현금");
			cashLb.setBounds(30, 300, 60, 30);
			add(cashLb);

			tmCash = new JLabel("");
			tmCash.setBounds(30, 330, 60, 30);
			add(tmCash);

			JLabel jj = new JLabel("아이디 검색");
			jj.setBounds(20, 400, 100, 20);
			add(jj);

			chkchk = new JTextField();
			chkchk.setBounds(20, 430, 80, 30);
			add(chkchk);

			Object [] index = {"아이디","충전금액","주문금액"};
			model2 =  new DefaultTableModel(index,0) {
				public boolean isCellEditable(int i, int c) {
					return false;
				}

			};

			userDb = new JTable(model2);
			userDb.addMouseListener(this);
			JScrollPane jsp = new JScrollPane(userDb);
			jsp.setBounds(200, 80, 650, 470);
			jsp.setOpaque(true);
			add(jsp);

			dateChooser = new JDateChooser();
			dateChooser.getComponent(1).setEnabled(false);
			dateChooser.setBounds(10, 20, 100, 30);			
			add(dateChooser);

			dateChooser_1 = new JDateChooser();
			dateChooser_1.getComponent(1).setEnabled(false);
			dateChooser_1.setBounds(130, 20, 100, 30);
			add(dateChooser_1);

			JButton jb = new JButton("검색");
			jb.setBounds(20, 470, 80, 30);
			jb.addActionListener(new TimeBntAct());
			add(jb);

			JButton sul = new JButton("설정");
			sul.setBounds(250, 20, 70, 30);
			sul.addActionListener(new ChkAct());
			add(sul);   
		}

		@Override
		public void mouseClicked(MouseEvent e) {

			if(!timeUserTable) {
				tud = new TimeUserTable();
				timeUserTable = true;
			}else {
				tud.dispatchEvent(new WindowEvent(tud, WindowEvent.WINDOW_CLOSING));
				tud = new TimeUserTable();
			}

		}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
		@Override
		public void mousePressed(MouseEvent e) {}
		@Override
		public void mouseReleased(MouseEvent e) {}   
	}

}

public class SalesManager_gui {

	public static void main(String[] args) {
		new SaleManage();

	}

}