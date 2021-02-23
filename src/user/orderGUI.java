package user;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.table.DefaultTableModel;

import admin.InventoryManageMain;
import data.DataBox;
import data.InventoryInfo;
import data.UserInfo;
import db.InvenDB;
import db.StaticIP;


class Order{

	String menuphoto,menuCate,menuName,serial;
	String menuPrice;
	public Order(String menuName,String menuCate ,String menuphoto,String menuPrice,String serial) {
		super();
		this.menuphoto = menuphoto;
		this.menuPrice = menuPrice;
		this.menuName = menuName;
		this.menuCate = menuCate;
		this.serial = serial;
	}
	@Override
	public String toString() {
		return "Order [menuphoto=" + menuphoto + ", menuCate=" + menuCate + ", menuName=" + menuName + ", serial="
				+ serial + ", menuPrice=" + menuPrice + "]";
	}
}


public class orderGUI extends JFrame implements ActionListener{

	ArrayList<Vector<String>> sendArr = new ArrayList<Vector<String>>();
	JPanel titleBtns, resP;
	JLabel pPL;
	JPanel priPen2;
	JPanel priLa;
	ArrayList<Order> list;
	ArrayList<InventoryInfo> invenList;
	JTable orderLT;

	InventoryManageMain ivm;


	DefaultTableModel dtm;

	UserInfo ui; 
	ObjectOutputStream oos;
	DataBox db;

	public orderGUI(UserInfo ui, ObjectOutputStream oos) {
		this.ui = ui;
		this.oos = oos;

		list = new OrderDB().getList();
		invenList = new InvenDB().getList();

		setBounds(0, 100, 1000, 700); //JFrame
		setLayout(null);
		///////////////////////////////////////////////////////////////////카테고리 부분	
		JPanel catePen = new JPanel();//카테고리 페널
		catePen.setBounds(0,0,640,80);
		catePen.setLayout(new GridLayout(1,4));
		add(catePen);

		ArrayList<JToggleButton> cateBtn = new ArrayList<JToggleButton>();

		ButtonGroup bug= new ButtonGroup();

		JToggleButton cate2 = new JToggleButton("라면");
		JToggleButton cate3 = new JToggleButton("덮밥");
		JToggleButton cate4 = new JToggleButton("과자");
		JToggleButton cate5 = new JToggleButton("음료");

		cateBtn.add(cate2);
		cateBtn.add(cate3);
		cateBtn.add(cate4);
		cateBtn.add(cate5);

		bug.add(cate2);
		bug.add(cate3);
		bug.add(cate4); 
		bug.add(cate5);

		catePen.add(cate2);
		catePen.add(cate3);
		catePen.add(cate4);
		catePen.add(cate5);

		cate2.setBackground(Color.white);
		cate3.setBackground(Color.white);
		cate4.setBackground(Color.white);
		cate5.setBackground(Color.white);

		///////////////////////////////////////////////////////////////////메뉴판 부분
		CardLayout card = new CardLayout();
		
		JPanel menuPanel2 = new JPanel();
		menuPanel2.setLayout(null);
		JScrollPane jsPan2 = new JScrollPane(menuPanel2);
		jsPan2.setBounds(0,0,640,590);
		Dimension size2 = new Dimension();
		size2.setSize(640, 800);
		menuPanel2.setPreferredSize(size2);
		jsPan2.setViewportView(menuPanel2);

		JPanel menuPanel3 = new JPanel();
		menuPanel3.setLayout(null);
		JScrollPane jsPan3 = new JScrollPane(menuPanel3);
		jsPan3.setBounds(0,0,640,590);
		Dimension size3 = new Dimension();
		size3.setSize(640, 800);
		menuPanel3.setPreferredSize(size3);
		jsPan3.setViewportView(menuPanel3);

		JPanel menuPanel4 = new JPanel();
		menuPanel4.setLayout(null);
		JScrollPane jsPan4 = new JScrollPane(menuPanel4);
		jsPan4.setBounds(0,0,640,590);
		Dimension size4 = new Dimension();
		size4.setSize(640, 950);
		menuPanel4.setPreferredSize(size4);
		jsPan4.setViewportView(menuPanel4);

		JPanel menuPanel5 = new JPanel();
		menuPanel5.setLayout(null);
		JScrollPane jsPan5 = new JScrollPane(menuPanel5);
		jsPan5.setBounds(0,0,640,590);
		Dimension size5 = new Dimension();
		size5.setSize(640, 800);
		menuPanel5.setPreferredSize(size5);
		jsPan5.setViewportView(menuPanel5);





		JPanel mePan = new JPanel(card);
		mePan.setBounds(0,110,640,590);
		mePan.add(jsPan2,"라면");
		mePan.add(jsPan3,"덮밥");
		mePan.add(jsPan4,"과자");
		mePan.add(jsPan5,"음료");
		add(mePan);

		cate2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JToggleButton btn = (JToggleButton)e.getSource();
				card.show(mePan, btn.getText());

			}
		});
		cate3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JToggleButton btn = (JToggleButton)e.getSource();
				card.show(mePan, btn.getText());

			}
		});
		cate4.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JToggleButton btn = (JToggleButton)e.getSource();
				card.show(mePan, btn.getText());

			}
		});
		cate5.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JToggleButton btn = (JToggleButton)e.getSource();
				card.show(mePan, btn.getText());

			}
		});



		ArrayList<JPanel> pArr2 = new ArrayList<JPanel>();
		ArrayList<JPanel> pArr3 = new ArrayList<JPanel>();
		ArrayList<JPanel> pArr4 = new ArrayList<JPanel>();
		ArrayList<JPanel> pArr5 = new ArrayList<JPanel>();

		int x = 10, y =20, cnt = 0;

		for (int i = 0; i < 25; i++) {
			cnt++;
			JPanel p2 = new JPanel();
			JPanel p3 = new JPanel();
			JPanel p4 = new JPanel();
			JPanel p5 = new JPanel();

			p2.setBounds(x,y,116,220);
			p3.setBounds(x,y,116,220);
			p4.setBounds(x,y,116,220);
			p5.setBounds(x,y,116,220);

			menuPanel2.add(p2);
			menuPanel3.add(p3);
			menuPanel4.add(p4);
			menuPanel5.add(p5);

			pArr2.add(p2);
			pArr3.add(p3);
			pArr4.add(p4);
			pArr5.add(p5);

			p2.setLayout(null);
			p3.setLayout(null);
			p4.setLayout(null);
			p5.setLayout(null);

			x+=126;
			if(cnt==5) {
				y+=250;
				x=10;
				cnt=0;
			}
		}


		ArrayList<JLabel> priArr = new ArrayList<JLabel>();
		ArrayList<JLabel> nameArr = new ArrayList<JLabel>();

		int ra=0,ri=0,sn=0,be=0;

		for (JToggleButton cateBL : cateBtn) {
			for (Order dbList : list) {

				if(dbList.menuCate.equals(cateBL.getText())) {

					JLabel price = new JLabel(dbList.menuPrice+"원");
					JLabel name = new JLabel(dbList.menuName);
					priArr.add(price);
					nameArr.add(name);
					name.setBounds(0,120,116,30);// 상품 당 이름,가격
					name.setHorizontalAlignment(JLabel.CENTER);
					price.setBounds(0,150,116,30);
					price.setHorizontalAlignment(JLabel.CENTER);

					String imgRoad = dbList.menuphoto;

					ImageIcon imgIc1 = new ImageIcon(imgRoad);


					HashSet nameSet = new HashSet();	
					ArrayList chkArr = new ArrayList();
					JButton btnLa = null;

					for (InventoryInfo nameArr1 : invenList) {
						nameSet.add(nameArr1.name);
					}
					for (Object ns : nameSet) {
						if(dbList.menuName.equals(ns)){
							btnLa = new JButton( dbList.serial,imgIc1);
							btnLa.addActionListener(new PricePan());
							break;
						}
						btnLa = new JButton( dbList.serial,new ImageIcon("img/매진.jpg"));
					}


					for (Object ns : nameSet) {

					}

					btnLa.setOpaque(true);
					btnLa.setBounds(0, 0, 116, 116);


					switch(dbList.menuCate) {

					case "라면":
						pArr2.get(ra).add(btnLa);
						pArr2.get(ra).add(name);
						pArr2.get(ra).add(price);
						break;
					case "덮밥":
						pArr3.get(ri).add(btnLa);
						pArr3.get(ri).add(name);
						pArr3.get(ri).add(price);
						break;
					case "과자":
						pArr4.get(sn).add(btnLa);
						pArr4.get(sn).add(name);
						pArr4.get(sn).add(price);
						break;
					case "음료":
						pArr5.get(be).add(btnLa);
						pArr5.get(be).add(name);
						pArr5.get(be).add(price);
						break;

					}

					if(dbList.menuCate.equals("라면")) {
						ra++;
					}else if(dbList.menuCate.equals("덮밥")) {
						ri++;
					}else if(dbList.menuCate.equals("과자")) {
						sn++;
					}else if(dbList.menuCate.equals("음료")) {
						be++;
					}
				}
			}
		}

		JPanel priPen = new JPanel();//주문 상품 추가 첫번째 패널
		priPen.setBounds(640,0,360,400);
		priPen.setLayout(null);
		add(priPen);

		Object [] index = {"종류","주문 상품 명"};
		dtm = new DefaultTableModel(index,0) {

			public boolean isCellEditable(int i, int c) {
				return false;
			}
		};

		orderLT = new JTable(dtm);
		orderLT.setAutoCreateRowSorter(true);

		JScrollPane listScroll = new JScrollPane(orderLT);
		listScroll.setBounds(0,0,360,400);

		priPen.add(listScroll);

		priPen2 = new JPanel();//가격정보 페널
		priPen2.setBounds(640,400,360,300);
		priPen2.setLayout(null);
		priPen2.setBackground(Color.white);
		add(priPen2);

		pPL = new JLabel();//총 금액 출력 라벨
		pPL.setBounds(10,90,200,60);
		pPL.setOpaque(true);
		priPen2.add(pPL);

		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


		Vector<String> payChk = new Vector<String>();
		payChk.add("카드결제");
		payChk.add("현금결제");


		JComboBox combo = new JComboBox(payChk);
		combo.setBounds(10,20,200,40);
		combo.setSelectedItem("카드결제");
		priPen2.add(combo);


		JButton payBtn = new JButton("결제");
		payBtn.setBounds(180,200,150,40);
		payBtn.addActionListener(new ActionListener() {


			@Override
			public void actionPerformed(ActionEvent e) {
				InvenDB iidb = new InvenDB();
				HashMap<String, ArrayList<InventoryInfo>> merchCnt = new HashMap<String, ArrayList<InventoryInfo>>();
				for (InventoryInfo ii : iidb.arr) {
					ArrayList<InventoryInfo> tmp = new ArrayList<InventoryInfo>();
					if(merchCnt.containsKey(ii.name))
						tmp = merchCnt.get(ii.name);
					tmp.add(ii);
					merchCnt.put(ii.name, tmp);
				}
				HashMap<String, ArrayList<Vector<String>>> orderCnt = new HashMap<String, ArrayList<Vector<String>>>();
				for (Vector<String> ii : sendArr) {
					ArrayList<Vector<String>> tmp = new ArrayList<Vector<String>>();
					if(orderCnt.containsKey(ii.get(1)))
						tmp = orderCnt.get(ii.get(1));
					tmp.add(ii);
					orderCnt.put(ii.get(1), tmp);
				}
				if(orderCnt.size() == 0) {
					JOptionPane.showMessageDialog(null, "주문 목록이 비어 있습니다.");
				}else {
					for (Entry<String, ArrayList<Vector<String>>> oc : orderCnt.entrySet()) {
						if(oc.getValue().size() <= merchCnt.get(oc.getKey()).size()) {
						}else {
							JOptionPane.showMessageDialog(null, oc.getKey()+"의 재고 수량이 부족합니다.\n"+"남은 재고 수량 :"+merchCnt.get(oc.getKey()).size());
							return;
						}
					}
					try {
								db = new DataBox("관리자", ui.id, sendArr);
								if(combo.getSelectedItem().equals("카드결제")){
									db.cashOrCard = true;
								}else {
									db.cashOrCard = false;
								}
								oos.writeObject(db);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
					dispose();
					JOptionPane.showMessageDialog(null, "상품 주문이 완료되었습니다.");
				}
			}
		});


		priPen2.add(payBtn);

		JButton deletBtn = new JButton("선택상품 삭제");
		deletBtn.setBounds(10,200,150,40);
		priPen2.add(deletBtn);
		deletBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int tsr = orderLT.getSelectedRow();
				
				if(tsr != -1) {
					
					for (Vector<String> ii : sendArr) {
						
						if(dtm.getValueAt(orderLT.getSelectionModel().getLeadSelectionIndex(), 1).equals(ii.get(1))) {
							sendArr.remove(ii);
							break;
						}
					}
					String aa = ""+orderLT.getValueAt(orderLT.getSelectedRow(),1);
					dtm.removeRow(orderLT.getSelectionModel().getLeadSelectionIndex());
					for (Order order : list) {
						int price = 0;
						if(order.menuName.equals(aa)) {
							price = Integer.parseInt(order.menuPrice);
							TpP.tot -= price;
							pPL.setText("총 결제금액 : "+ TpP.tot);
							break;
						}
					}
					
				}else {
					JOptionPane.showMessageDialog(null, "삭제 할 상품을 선택해 주세요");
				}
				
			}

		});


	}

	TotPrice TpP = new TotPrice();

	class PricePan implements ActionListener {

		HashMap<String, Vector<String>> map = new HashMap<String, Vector<String>>();
		Order od;
		int price ;
		String menu = "ff";
		int cnt;

		Vector<Vector> row = new Vector<Vector>();

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton p = (JButton)e.getSource();
			Component [] cc = p.getComponents();
			Vector<String> choiceLT = new Vector<String>();
			int cnt = 1;
			for (Order odL : list) {
				if(odL.serial.equals(p.getText())) {
					if(map.containsKey(odL.menuName)) {
						choiceLT.add(cnt+"");
						break;
					}else {
						choiceLT.add(odL.menuCate);
						choiceLT.add(odL.menuName);
						price += Integer.parseInt(odL.menuPrice);
						map.put(odL.menuName, choiceLT);
					}
				}
			}
			TpP.pPTot(price);
			pPL.setText("총 결제금액 : "+TpP.tot);
			for (Entry<String, Vector<String>> en : map.entrySet()) {
				dtm.addRow((Vector)en.getValue().clone());
				sendArr.add(en.getValue());
			}
		}
	}

	class TotPrice{

		int tot;

		void pPTot(int price) {

			tot+= price;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}
}

class OrderDB {
	ArrayList<Order>list;


	public OrderDB() {



		list = new ArrayList<Order>();

		try {


			Class.forName("oracle.jdbc.driver.OracleDriver");

			Connection con = DriverManager.getConnection(
					"jdbc:oracle:thin:@"+StaticIP.ip+":1521:xe", 
					"hr", 
					"hr");

			Statement stmt = con.createStatement();

			ResultSet it = stmt.executeQuery("select * from item");

			while(it.next()) {


				Order od = new Order(
						it.getString("menu"),
						it.getString("category"),
						it.getString("image"),
						it.getString("price"),
						it.getString("serial")
						);

				list.add(od);

			}

			it.close();
			stmt.close();

			con.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	ArrayList<Order> getList(){

		return list;

	}
}








