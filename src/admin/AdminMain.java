package admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

import admin.UserInfoPrint.UserListGUI;
import admin.ViewUserInfo.UserInfoGUI;
import data.DataBox;
import data.FeeData;
import data.LogData;
import data.LogOutSignal;
import data.MoveSignal;
import data.MsgBox;
import data.OrderProcessingData;
import data.RegistSignal;
import data.SeatMovingSignal;
import data.TransData;
import data.UserInfo;
import data.UserListData;
import db.CustomerDAO;
import db.PCUseDB;
import server.AdminMessage;
import user.LogOut;
import user.MsgCloseChk;

class PCBangAdmin {

	public String adminId;
	AdminName an;

	Socket socket;
	ObjectOutputStream oos;
	ObjectInputStream ois;

	TotalSeatInfo tsi;
	RobbyUseUserInfo ruui;
	RobbyOrderCheck roc;
	RobbyUsingLog rul;
	String userSep;
	TransData td;
	FeeData fd;
	
	SeatMovingSignal sms;

	UserInfo ui;
	UserInfo uiOut;
	
	AdminMsgCloseChk msgChk;
	AdminMessage msg;
	boolean [] msgOpenChk = new boolean[39];
	
	HashMap<Integer, AdminMessage> msgMap;

	DataBox db;

	public JLabel idLabel;
	
	// 정산
	Cla_Flame cf;
	
	// 매출 확인
	SaleManage sm;
	
	// 금고 확인
	CashChk cc;
	
	// 금고 설정
	SetVault sv;
	SetVault.SetVaultGUI svg;
	
	// 재고 확인
	InventoryManageMain imm;
	
	// 재고 입고
	InventoryOrderMain iom;
	
	// 상품 등록
	Product_ pd;
		
	// 회원정보 검색
	AllUserInfoSearch ais;
	UserInfoPrint.UserListGUI ulg;
	
	//요금제 설정
	setting_Gui sg;
	
	// 게임 통계
	GameUseStats gus;
	
	// 상품통계
	ItemStats is;
	ItemStats.ItemStatsGUI isg;
	
	// 사용 현황 통계
	PCUseStat pus;
	
	// 관리자 회원가입
	AdminJoin aj;
	
	public PCBangAdmin(String adminId) {
		
		an.id = adminId;

		userSep = "관리자";

		try {

			socket = new Socket("127.0.0.1", 7777);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());

			oos.writeObject(new DataBox(null, userSep, new RegistSignal()));

			td = new TransData();
			td.idArr = new ArrayList<String>();
			td.seatArr = new ArrayList<Integer>();
			td.userArr = new ArrayList<UserInfo>();
			td.uiVector = new Vector<UserInfo>();
			msgChk = new AdminMsgCloseChk();
			msgMap = new HashMap<Integer, AdminMessage>();
			tsi = TotalSeatInfo.giveSeat(userSep, socket, td.uiVector, oos, ois, msgChk, msgMap);
			
			new TcpReceiver(socket).start();

		} catch (IOException e) {
			e.printStackTrace();
		}

		ruui = new RobbyUseUserInfo(td, db, oos, ois);
		ruui.start();
		roc = new RobbyOrderCheck(td);
		roc.start();
		rul = new RobbyUsingLog(td);
		rul.start();
		new MainRobby();

	}

	class TcpReceiver extends Thread {

		public TcpReceiver(Socket socket) {

			try {

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		@Override
		public void run() {
			try {

				while(ois!=null) {

					DataBox tsi = (DataBox)ois.readObject();

					if(tsi.data instanceof UserInfo) {

						PCBangAdmin.this.tsi.catchData(tsi);

						ui = ((UserInfo)tsi.data);
						td.ui = ui;
						td.usingUi = ui;

					}else if(tsi.data instanceof LogOutSignal) {
						
						PCBangAdmin.this.tsi.catchData(tsi);

						uiOut = ((LogOutSignal)tsi.data).ui;
						td.uiOut = ((LogOutSignal)tsi.data).ui;
						td.usingUiOut = ((LogOutSignal)tsi.data).ui;
						
					}
					
					else if(tsi.data instanceof MsgBox) {
										
						System.out.println(((MsgBox)tsi.data).getUi().seat);
						
						int seat = ((MsgBox)tsi.data).getUi().seat-1;
						
						if(!msgOpenChk[seat]) {
							if(!msgChk.msgChkArr[seat]) {
								msgMap.put(((MsgBox)tsi.data).getUi().seat, 
										new AdminMessage(((MsgBox)tsi.data).getUi(), socket, oos, ois, msgChk));
							}
//								msg = new AdminMessage(((MsgBox)tsi.data).getUi(), socket, oos, ois, msgChk);
							msgOpenChk[seat] = true;
						}else {
							if(!msgChk.msgChkArr[seat]) {
								if(msg!=null) 
									msg.dispatchEvent(new WindowEvent(msg, WindowEvent.WINDOW_CLOSING));
								
								msgMap.put(((MsgBox)tsi.data).getUi().seat, 
										new AdminMessage(((MsgBox)tsi.data).getUi(), socket, oos, ois, msgChk));
								
//								msg = new AdminMessage(((MsgBox)tsi.data).getUi(), socket, oos, ois, msgChk);
							
							}
							
						}
						
						msgMap.get(((MsgBox)tsi.data).getUi().seat).setMsg(tsi.sender, (MsgBox)tsi.data);
						
//						if(msg != null)
//							msg.setMsg(tsi.sender, (MsgBox)tsi.data);
						
						if(PCBangAdmin.this.tsi.cu != null) {
							
//							PCBangAdmin.this.tsi.msgMap = msgMap;
							PCBangAdmin.this.tsi.cu.msgMap.get(((MsgBox)tsi.data).getUi().seat).setMsg
							(PCBangAdmin.this.tsi.cu.id, (MsgBox)tsi.data);
						}
								
					}
					
					else if(tsi.data instanceof FeeData) {

						fd = ((FeeData)tsi.data);
						td.fd = fd;
						
						PCBangAdmin.this.tsi.atMap.get(fd.id).lastTime = fd.userTime;
						
					}else if(tsi.data instanceof ArrayList) {
																	
						td.al = (ArrayList)tsi.data;
						td.usingAl = (ArrayList)tsi.data;
						td.cashOrCard = tsi.cashOrCard;
						td.id = tsi.sender;

					}
					else if(tsi.data instanceof MoveSignal) {
						sms.signal = false;
						JOptionPane.showMessageDialog(null, "사용 종료된 회원입니다.");
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}    

	}

	class MainRobby extends JFrame {

		public MainRobby() {

			setTitle("Just Play");

			setBounds(500, 150, 1000, 700);
			setLayout(null);

			JMenuBar mb = new JMenuBar();

			setJMenuBar(mb);

			// MenuBar의 JMenu 생성
			JMenu jmSalesAdmin = new JMenu("매출 관리");
			JMenu jmStockAdmin = new JMenu("재고 관리");
			JMenu jmItemAdmin = new JMenu("상품 관리");
			JMenu jmMemAdmin = new JMenu("회원 관리");
			JMenu jmFeeAdmin = new JMenu("요금제 관리");
			JMenu jmStatsAdmin = new JMenu("통계");
			JMenu jmAdminInfo = new JMenu("관리자 설정");

			// 매출 관리의 JMenuItem
			JMenuItem jiCalc = new JMenuItem("정산");
			jiCalc.addActionListener(new MenuActionEvent());
			JMenuItem jiSalesChk = new JMenuItem("매출 확인");
			jiSalesChk.addActionListener(new MenuActionEvent());
			JMenuItem jiVaultChk = new JMenuItem("금고 확인");
			jiVaultChk.addActionListener(new MenuActionEvent());
			JMenuItem jiVaultSet = new JMenuItem("금고 설정");
			jiVaultSet.addActionListener(new MenuActionEvent());
			jmSalesAdmin.add(jiCalc);
			jmSalesAdmin.add(jiSalesChk);
			jmSalesAdmin.add(jiVaultChk);
			jmSalesAdmin.add(jiVaultSet);

			// 재고 관리의 JMenuItem
			JMenuItem jiStockChk = new JMenuItem("재고 확인");
			jiStockChk.addActionListener(new MenuActionEvent());
			JMenuItem jiItemSub = new JMenuItem("재고 입고");
			jiItemSub.addActionListener(new MenuActionEvent());
			jmStockAdmin.add(jiStockChk);
			jmStockAdmin.add(jiItemSub);

			// 회원 관리의 JMenuItem
			JMenuItem jiMemSearch = new JMenuItem("회원 검색");
			jiMemSearch.addActionListener(new MenuActionEvent());
			jmMemAdmin.add(jiMemSearch);

			// 상품 관리의 JMenuItem
			JMenuItem jiItemList = new JMenuItem("상품 리스트");
			jiItemList.addActionListener(new MenuActionEvent());
			jmItemAdmin.add(jiItemList);

			// 요금제 관리의 JMenuItem
			JMenuItem jiFeeList = new JMenuItem("요금제 설정");
			jiFeeList.addActionListener(new MenuActionEvent());
			jmFeeAdmin.add(jiFeeList);

			// 통계의 JMenuItem
			JMenuItem jiGameStats = new JMenuItem("게임 사용 시간 통계");
			jiGameStats.addActionListener(new MenuActionEvent());
			JMenuItem jiSalesStats = new JMenuItem("상품별 판매 통계");
			jiSalesStats.addActionListener(new MenuActionEvent());
			JMenuItem jiUseStats = new JMenuItem("사용 현황 통계");
			jiUseStats.addActionListener(new MenuActionEvent());
			jmStatsAdmin.add(jiGameStats);
			jmStatsAdmin.add(jiSalesStats);
			jmStatsAdmin.add(jiUseStats);

			// 관리자 설정의 JMenuItem
			JMenuItem jiAdminSignUp = new JMenuItem("관리자 회원가입");
			jiAdminSignUp.addActionListener(new MenuActionEvent());
			jmAdminInfo.add(jiAdminSignUp);

			mb.add(jmSalesAdmin);
			mb.add(jmStockAdmin);
			mb.add(jmItemAdmin);
			mb.add(jmMemAdmin);
			mb.add(jmFeeAdmin);
			mb.add(jmStatsAdmin);
			mb.add(jmAdminInfo);
			
			an.idLabel = new JLabel(an.id+"");
			an.idLabel.setBounds(800, 25, 200, 50);
			an.idLabel.setFont(new Font("바탕체", Font.PLAIN, 30));
			an.idLabel.setHorizontalAlignment(JLabel.CENTER);
			an.idLabel.setOpaque(true);
			
			add(an.idLabel);

			// 현재 pc방 고객의 수를 표시
			
			td.nowPax = new JLabel("인원: "+td.userArr.size());
			td.nowPax.setBounds(10,25, 140, 50);
			td.nowPax.setOpaque(true);
			td.nowPax.setForeground(Color.red);
			td.nowPax.setFont(new Font("바탕체", Font.PLAIN, 30));
			td.nowPax.setHorizontalAlignment(JLabel.CENTER);

			add(td.nowPax);

			TotalSeatInfo.SeatArrange sa;
			sa = tsi.new SeatArrange(); 

			RobbyUseUserInfo.UseUserInfoGUI uuig;
			uuig = ruui.new UseUserInfoGUI();

			RobbyOrderCheck.OrderCheckGUI ocg;
			ocg = roc.new OrderCheckGUI();

			RobbyUsingLog.UsingLogGUI ulg;
			ulg = rul.new UsingLogGUI();

			add(sa);
			add(uuig);
			add(ocg);
			add(ulg);

			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setVisible(true);

		}

	}

	class MenuActionEvent implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			JMenuItem cmd = (JMenuItem)e.getSource();
			
			switch (cmd.getText()) {
			
				case "정산":
					if(cf != null) {
						cf.dispose();
						cf = new Cla_Flame(an.id);
					}else
						cf = new Cla_Flame(an.id);
						
					break;
					
				case "매출 확인":
					
					if(sm != null) {
						sm.dispose();
						sm = new SaleManage(); 
					}else
						sm = new SaleManage();
					
					break;
					
				case "금고 확인":
					
					if(cc != null) {
						cc.cashChkFrame.dispose();
						cc = new CashChk();				
					}else
						cc = new CashChk();
					
					break;
					
				case "금고 설정":
					
					if(svg != null) {
						svg.dispose();
						sv = new SetVault(an.id);
						svg = sv.new SetVaultGUI();
					}else {
						sv = new SetVault(an.id);
						svg = sv.new SetVaultGUI();
					}
					
					break;
	
				case "재고 확인":
					
					if(imm != null) {
						imm.dispose();
						imm = new InventoryManageMain();
					}else 
						imm = new InventoryManageMain();
					
					break;
	
				case "재고 입고":
					
					if(iom != null) {
						iom.dispose();
						iom = new InventoryOrderMain();
					}else
						iom = new InventoryOrderMain();
					break;
	
				case "상품 리스트":
					
					if(pd != null) {
						pd.dispose();
						pd = new Product_();
					}else
						pd = new Product_();
					
					break;
	
				case "회원 검색":
					
					if(ulg != null) {
						ulg.dispose();
						ais = new AllUserInfoSearch();
						ulg = ais.uip.new UserListGUI();
					}else {					
						ais = new AllUserInfoSearch();
						ulg = ais.uip.new UserListGUI();
					}
					
					break;
					
				case "요금제 설정":
					
					if(sg != null) {
						sg.dispose();
						sg = new setting_Gui();
					}else
						sg = new setting_Gui();
					
					break;
	
				case "게임 사용 시간 통계":
					
					if(gus != null) {
						gus.dispose();
						gus = new GameUseStats(true);
					}else
						gus = new GameUseStats(true);
					
					break;
	
				case "상품별 판매 통계":
					
					if(isg != null) {
						isg.dispose();
						is = new ItemStats();
						isg = is.new ItemStatsGUI();
					}else {					
						is = new ItemStats();
						isg = is.new ItemStatsGUI();
					}
					
					break;
	
				case "사용 현황 통계":
					
					if(pus != null) {
						pus.dispose();
						pus = new PCUseStat();
					}else
						pus = new PCUseStat();
					
					break;
	
				case "관리자 회원가입":
					
					if(aj != null) {
						aj.dispose();
						aj = new AdminJoin(1);
					}else
						aj = new AdminJoin(1);
					
					break;
	
				default:
					break;
			}

		}

	}

}

class RobbyUseUserInfo extends Thread {

	Vector<String> userColumn = new Vector<String>();
	DefaultTableModel model;
	Vector<String> userCol;  //열 Vector
	Vector<Vector> userRow;  //행 Vector

	TransData td;

	JTextField jtUserSearch;
	JTable UserInfoTable;

	DataBox db;
	ObjectOutputStream oos;
	ObjectInputStream ois;


	String id;

	ViewUserInfo vui;

	boolean frameChk;

	public RobbyUseUserInfo(TransData td, DataBox db, ObjectOutputStream oos, ObjectInputStream ois) {

		this.db = db;
		this.oos = oos;
		this.ois = ois;
		this.td = td;

	}

	class UseUserInfoGUI extends JPanel implements ActionListener, MouseListener {

		public UseUserInfoGUI() {

			setBounds(10,100, 160, 400);
			setLayout(null);


			JPanel jpTop = new JPanel();
			jpTop.setLayout(null);
			jpTop.setBounds(0, 0, 160, 100);

			JPanel jpLabel = new JPanel();
			jpLabel.setBackground(Color.gray);
			jpLabel.setBounds(0, 0, 160, 40);

			JLabel jlUserSearch = new JLabel("검색");
			jlUserSearch.setFont(new Font("바탕체", Font.PLAIN, 30));
			jlUserSearch.setHorizontalAlignment(JLabel.CENTER);
			
			jpLabel.add(jlUserSearch);

			jpTop.add(jpLabel);

			JPanel jpTF = new JPanel();
			jpTF.setLayout(null);
			jpTF.setBounds(0, 40, 160, 60);
			jpTF.setBackground(Color.white);

			jtUserSearch = new JTextField();
			jtUserSearch.setBounds(10, 5, 80, 30);
			jtUserSearch.setFont(new Font("바탕체", Font.PLAIN, 15));
			jpTF.add(jtUserSearch);

			JButton searchBtn = new JButton("검색");
			searchBtn.setBounds(95, 5, 60, 30);
			searchBtn.addActionListener(this);
			jpTF.add(searchBtn);

			jpTop.add(jpTF);

			add(jpTop);

			//////////////////////////////////////////////////

			JPanel jpBot = new JPanel();
			jpBot.setLayout(null);
			jpBot.setBounds(0, 100, 160, 300);
			jpBot.setBackground(Color.white);

			userColumn.addElement("ID");
			userColumn.addElement("성명");

			model = new DefaultTableModel(userColumn,0) {

				public boolean isCellEditable(int i, int c){
					return false;
				}

			}; 

			UserInfoTable = new JTable(model);
			JScrollPane jsp = new JScrollPane(UserInfoTable);
			jsp.setBounds(0, 0, 160, 300);
			jpBot.add(jsp);

			UserInfoTable.addMouseListener(this);

			add(jpBot);

		}

		@Override
		public void mouseClicked(MouseEvent e) {

			JTable jt = (JTable)e.getSource();

			id = (String) jt.getModel().getValueAt(jt.getSelectedRow(), 0);

			if(!frameChk) {
				vui = new ViewUserInfo(new CustomerDAO().detail(id));
				vui.uig = vui.new UserInfoGUI();
				frameChk = true;
			}
			else {
				if(vui.uig != null)
					vui.uig.dispatchEvent(new WindowEvent(vui.uig, WindowEvent.WINDOW_CLOSING));
				vui = new ViewUserInfo(new CustomerDAO().detail(id));
				vui.uig = vui.new UserInfoGUI();
			}

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void actionPerformed(ActionEvent e) {

			id = jtUserSearch.getText();
			
			if(td.idArr.size() != 0) {
				
				for (int i = 0; i < td.idArr.size(); i++) {
					
					if(!id.equals("")) {
						
						if(!frameChk) {
								
								vui = new ViewUserInfo(new CustomerDAO().detail(id));
								if(vui.uld != null && id.equals(td.idArr.get(i))) {
									vui.uig = vui.new UserInfoGUI();
									frameChk = true;
									break;
									
								}		
						}
						else {
									
							if(vui.uig != null)
								vui.uig.dispatchEvent(new WindowEvent(vui.uig, WindowEvent.WINDOW_CLOSING));
							
							vui = new ViewUserInfo(new CustomerDAO().detail(id));
							
							if(vui.uld != null && id.equals(td.idArr.get(i))) {
								vui.uig = vui.new UserInfoGUI();
								break;
							}
														
						}
					
					}else {
						JOptionPane.showMessageDialog(this, "아이디를 입력하세요.");
						break;
					}
						
				}
				
			}else
				JOptionPane.showMessageDialog(this, "이용 중인 회원이 없습니다.");

		}

	}

	@Override
	public void run() {

		while(true) {

			if(td!=null) {

				try {
					sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if(td.ui != null) {

					if(td.ui.useChk) {

						userCol = new Vector<String>();
						userRow= new Vector<Vector>();

						if(!td.idArr.contains(td.ui.id)) {

							userCol.addElement(td.ui.id);
							userCol.addElement(td.ui.name);
							userRow.addElement(userCol);

							td.userArr.add(td.ui);
							td.idArr.add(td.ui.id);
							td.seatArr.add(td.ui.seat);

							Iterator<Vector> it = userRow.iterator();

							model.addRow(it.next());

							td.nowPax.setText("인원: "+model.getRowCount());

							td.temp = td.ui.seat;
							td.tempUi = td.ui;

							td.ui = null;

						}
						else {

							if(td.ui.seat != td.temp) {

								td.userArr.add(td.ui);

								try {
									sleep(10);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								if(td.userArr.size() != 0)
									td.userArr.remove(td.userArr.indexOf(td.tempUi));

								td.temp = td.ui.seat;
								td.tempUi = td.ui;

							}
						}

					}

				}

				if(td.uiOut != null) {

					if(td.idArr.size() != 0 && td.userArr.size() != 0) {

						for (int i = 0; i < model.getRowCount(); i++) {

							if(((Vector)model.getDataVector().get(i)).get(0).equals(td.uiOut.id)) {
								((DefaultTableModel)UserInfoTable.getModel()).removeRow(i);
								break;
							}

						}

						td.idArr.remove(td.idArr.indexOf(td.uiOut.id));

						td.nowPax.setText("인원: "+model.getRowCount());

						td.uiOut = null;
					}

				}

			}

		}

	}
}

class MenuOrder {

	String id;
	ArrayList<Vector<String>> menu;
	boolean cardOrCash;
	
	JButton btnOrder;
	JTextArea jtMenu;

	OrderHandling oh;
	
	boolean chk;
	
	public MenuOrder(String id, ArrayList<Vector<String>> menu, boolean cardOrCash) {
		super();

		this.id = id;
		this.menu = menu;
		this.cardOrCash = cardOrCash;

	}

	class MenuOrderGUI extends JPanel implements ActionListener {

		public MenuOrderGUI() {

			setLayout(new BorderLayout());

			jtMenu = new JTextArea();
			jtMenu.setEditable(false);
			
			jtMenu.append(id+"\n");
			
			for (int i = 0; i < menu.size(); i++) {
				jtMenu.append("메뉴: "+menu.get(i).get(1)+"\n");
			}
			
			if(cardOrCash)
				jtMenu.append("카드");
			else
				jtMenu.append("현금");

			btnOrder = new JButton("주문완료");
			btnOrder.addActionListener(this);

			add(jtMenu,"Center");
			add(btnOrder,"South");

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			
			oh = new OrderHandling(menu, id, cardOrCash);
			oh.stockCalc();
			
			chk = true;
			
//			 removeAll();
//             revalidate();
//             repaint();
            
		}

	}

}

class RobbyOrderCheck extends Thread {

	MenuOrder mo;
	TransData td;
	JPanel orderPanel;
	
	OrderProcessingData opd;
	
	MenuOrder.MenuOrderGUI mog;

	public RobbyOrderCheck(TransData td) {
		
		opd = new OrderProcessingData();
		opd.arr = new ArrayList<Object>();
		opd.arrPanel = new ArrayList<Object>();
	
		this.td = td;

	}

	class OrderCheckGUI extends JPanel {

		public OrderCheckGUI() {

			setBounds(830, 100, 140, 400);
			setLayout(null);
			setBackground(Color.white);

			JPanel jpTop = new JPanel();
			jpTop.setLayout(null);
			jpTop.setBounds(0, 0, 140, 40);

			JPanel jpLabel = new JPanel();
			jpLabel.setBounds(0, 0, 140, 40);
			jpLabel.setBackground(Color.gray);

			JLabel jlOrder = new JLabel("주문");
			jlOrder.setFont(new Font("바탕체", Font.PLAIN, 30));
			jlOrder.setHorizontalAlignment(JLabel.CENTER);
			jpLabel.add(jlOrder);

			jpTop.add(jpLabel);

			add(jpTop);

			JPanel jpBot = new JPanel();
			jpBot.setBounds(0, 40, 140, 358);
			jpBot.setBackground(Color.white);

			orderPanel = new JPanel();
			orderPanel.setLayout(new GridLayout(50,1));

			JScrollPane jsp = new JScrollPane(orderPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

			jsp.setPreferredSize(new Dimension(140,358));

			jpBot.add(jsp);

			add(jpBot);

		}

	}

	@Override
	public void run() {

		while(true) {

			if(td!=null) {

				try {
					sleep(10);
				} catch (Exception e) {
					// TODO: handle exception
				}

				if(td.al != null) {
					mo = new MenuOrder(td.id, td.al, td.cashOrCard);
					mog = mo.new MenuOrderGUI();
					
					opd.arr.add(mo);
					opd.arrPanel.add(mog);
					
					orderPanel.add(mog);
					
					orderPanel.revalidate();
					orderPanel.repaint();
					
				}
				
				td.al = null;

			}
			
			try {
				sleep(10);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			if(opd.arr.size() !=0) {
				
				for (int i = 0; i < opd.arr.size(); i++) {
					
					if(((MenuOrder)opd.arr.get(i)).chk) {
						orderPanel.remove(((MenuOrder.MenuOrderGUI)opd.arrPanel.get(i)));
						((MenuOrder)opd.arr.get(i)).chk = false;
						opd.arr.remove(i);
						opd.arrPanel.remove(i);
						
						orderPanel.revalidate();
						orderPanel.repaint();
						break;
						
					}
					
				}
				
			}
						
		}

	}

}

class RobbyUsingLog extends Thread {

	JTextArea usingLogArea;
	ArrayList<LogData> list;
	TransData td;

	LogData ld;

	public RobbyUsingLog(TransData td) {

		this.td = td;

		ld = new LogData();

		try {

			list = new PCUseDB().list();

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	class UsingLogGUI extends JPanel {

		public UsingLogGUI() {

			setBounds(10, 510, 960, 110);
			setLayout(null);
			setBackground(Color.white);

			JPanel jpTop = new JPanel();
			jpTop.setLayout(null);
			jpTop.setBounds(0, 0, 150, 30);

			JPanel jpLabel = new JPanel();
			jpLabel.setBounds(0, 0, 150, 30);
			jpLabel.setBackground(Color.gray);

			JLabel jlOrder = new JLabel("사용 로그");
			jlOrder.setFont(new Font("바탕체", Font.PLAIN, 20));
			jlOrder.setHorizontalAlignment(JLabel.CENTER);
			jpLabel.add(jlOrder);

			jpTop.add(jpLabel);

			add(jpTop);

			JPanel jpBot = new JPanel();
			jpBot.setLayout(null);
			jpBot.setBounds(0, 30, 950, 70);
			jpBot.setBackground(Color.white);

			usingLogArea = new JTextArea();
			usingLogArea.setFont(new Font("바탕체", Font.PLAIN, 13));
			usingLogArea.setEditable(false);
			for (LogData ld : list) {
				usingLogArea.append(ld+"\n");
			}
			JScrollPane jsp = new JScrollPane(usingLogArea);
			jsp.setBounds(10, 10, 940, 60);
			jpBot.add(jsp);

			add(jpBot);

		}

	}

	@Override
	public void run() {

		while(true) {

			if(td != null) {

				try {
					sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if(td.usingUi != null) {

					if(td.usingUi.useChk) {

						ld.setTime(new Date());
						ld.setId(td.usingUi.id);
						ld.setLogin("true");
						usingLogArea.append(ld+"\n");

						new PCUseDB().insert(ld);
						ld = new LogData();
					}

				}
				
				td.usingUi = null;
				
				if(td.usingUiOut != null) {
					
					ld.setTime(new Date());
					ld.setId(td.usingUiOut.id);
					ld.setLogin("false");
					usingLogArea.append(ld+"\n");

					new PCUseDB().insert(ld);
					ld = new LogData();
					
				}

				td.usingUiOut = null;

				if(td.fd != null) {

					ld.setTime(new Date());
					ld.setId(td.fd.id);
					ld.setFee(td.fd.fee);
					ld.setPay("card");
					usingLogArea.append(ld+"\n");

					new PCUseDB().insert(ld);
					ld = new LogData();

				}

				td.fd = null;

				if(td.usingAl != null) {

					if(td.cashOrCard) {

						for (int i = 0; i < td.usingAl.size(); i++) {
							ld.setTime(new Date());
							ld.setId(td.id);
							ld.setLogin("null");
							ld.setItem((String)((Vector)td.usingAl.get(i)).get(1));
							ld.setPay("card");
							usingLogArea.append(ld+"\n");

							try {
								sleep(50);
							} catch (InterruptedException e) {
							
								e.printStackTrace();
							}

							new PCUseDB().insert(ld);

							ld = new LogData();
						}

						td.usingAl = null;

					}else {

						for (int i = 0; i < td.usingAl.size(); i++) {
							ld.setTime(new Date());
							ld.setId(td.id);
							ld.setLogin("null");
							ld.setItem((String)((Vector)td.usingAl.get(i)).get(1));
							ld.setPay("cash");
							usingLogArea.append(ld+"\n");

							try {
								sleep(50);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							new PCUseDB().insert(ld);
							ld = new LogData();
						}

						td.usingAl = null;

					}
				}

			}

		}

	}

}

public class AdminMain {

	public static void main(String[] args) {

		new PCBangAdmin("asd22");

	}

}
