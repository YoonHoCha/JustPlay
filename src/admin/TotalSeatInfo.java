package admin;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import admin.TotalSeatInfo.SeatArrange.AdminTimer;
import data.DataBox;
import data.LogOutSignal;
import data.SeatMovingSignal;
import data.UserInfo;
import db.CustomerDAO;
import server.AdminMessage;
import user.MsgCloseChk;
import user.UserUI;

public class TotalSeatInfo extends Thread {

	int num;
	String userSep;

	Socket socket;
	ObjectOutputStream oos;
	ObjectInputStream ois;

	DataBox db;
	UserUI user;

	UserInfo ui;

	ArrayList<OneSeat> osArr;

	Vector<UserInfo> uiVector;
	
	SeatMovingSignal sms;

	private static TotalSeatInfo seatData;

	ChoiceUser cu;
	
	HashMap<Integer, AdminMessage> msgMap;
		
	HashMap<String, AdminTimer> atMap;
	
	AdminMsgCloseChk msgChk;

	private TotalSeatInfo(String userSep, Socket socket, Vector<UserInfo> uiVector, ObjectOutputStream oos, ObjectInputStream ois, AdminMsgCloseChk msgChk, HashMap<Integer, AdminMessage> msgMap) {

		this.userSep = userSep;	
		this.socket = socket;
		this.uiVector = uiVector;
		this.oos = oos;
		this.ois = ois;
		this.msgChk = msgChk;
		this.msgMap = msgMap;
		System.out.println("ts.msgChk"+msgChk);
		osArr = new ArrayList<TotalSeatInfo.OneSeat>();
		atMap = new HashMap<String, TotalSeatInfo.SeatArrange.AdminTimer>();

	}

	private TotalSeatInfo(String userSep, Socket socket, ObjectOutputStream oos, ObjectInputStream ois, AdminMsgCloseChk msgChk, HashMap<Integer, AdminMessage> msgMap) {

		this.userSep = userSep;	
		this.socket = socket;
		this.oos = oos;
		this.ois = ois;
		this.msgChk = msgChk;
		this.msgMap = msgMap;
		System.out.println("ts.msgChk"+msgChk);
		osArr = new ArrayList<TotalSeatInfo.OneSeat>();
		atMap = new HashMap<String, TotalSeatInfo.SeatArrange.AdminTimer>();

	}

	public static TotalSeatInfo giveSeat(String userSep, Socket socket, Vector<UserInfo> uiVector, ObjectOutputStream oos, ObjectInputStream ois, AdminMsgCloseChk msgChk, HashMap<Integer, AdminMessage> msgMap) {

		if(seatData == null) {
			seatData = new TotalSeatInfo(userSep, socket, uiVector, oos, ois, msgChk, msgMap);
		}

		return seatData;
	}

	public static TotalSeatInfo giveSeat(String userSep, Socket socket, ObjectOutputStream oos, ObjectInputStream ois, AdminMsgCloseChk msgChk, HashMap<Integer, AdminMessage> msgMap) {


		if(seatData == null) {
			seatData = new TotalSeatInfo(userSep, socket, oos, ois, msgChk, msgMap);
		}

		return seatData;
	}

	public void catchData(DataBox db) {
		this.db = db;
	}

	public class SeatArrange extends JPanel {
		
		boolean endChk = false;
		ArrayList<UserInfo> userArr;
		
		public SeatArrange() {
//			this.userArr = userArr;

			setLayout(null);
			setBounds(200, 100, 600, 400);
			setBackground(Color.white);

			JPanel jpSeatLineTop = new JPanel(new GridLayout(1,9));
			jpSeatLineTop.setBounds(40, 20, 450, 60);
			jpSeatLineTop.setBackground(Color.gray);

			for (int i = 0; i <= 8; i++) {
				num = i+1;
				osArr.add(new OneSeat(num,userSep));
				OneSeat.SeatGUI osg;
				osg = osArr.get(i).new SeatGUI();
				jpSeatLineTop.add(osg);
			}


			JPanel jpSeatLineCenter1 = new JPanel(new GridLayout(1,7));
			jpSeatLineCenter1.setBounds(90, 120, 350, 60);
			jpSeatLineCenter1.setBackground(Color.gray);

			for (int i = 9; i <= 15; i++) {
				num = i+1;
				osArr.add(new OneSeat(num,userSep));
				OneSeat.SeatGUI osg;
				osg = osArr.get(i).new SeatGUI();
				jpSeatLineCenter1.add(osg);
			}

			JPanel jpSeatLineCenter2 = new JPanel(new GridLayout(1,9));
			jpSeatLineCenter2.setBounds(40, 220, 450, 60);
			jpSeatLineCenter2.setBackground(Color.gray);

			for (int i = 16; i <= 24; i++) {
				num = i+1;
				osArr.add(new OneSeat(num,userSep));
				OneSeat.SeatGUI osg;
				osg = osArr.get(i).new SeatGUI();
				jpSeatLineCenter2.add(osg);
			}

			JPanel jpSeatLineBot = new JPanel(new GridLayout(1, 9));
			jpSeatLineBot.setBounds(40, 320, 450, 60);
			jpSeatLineBot.setBackground(Color.gray);

			for (int i = 25; i <= 33; i++) {
				num = i+1;
				osArr.add(new OneSeat(num,userSep));
				OneSeat.SeatGUI osg;
				osg = osArr.get(i).new SeatGUI();
				jpSeatLineBot.add(osg);
			}

			JPanel jpSeatLineRight = new JPanel(new GridLayout(5,1));
			jpSeatLineRight.setBounds(530, 80, 50, 250);
			jpSeatLineRight.setBackground(Color.gray);

			for (int i = 34; i <= 38; i++) {
				num = i+1;
				osArr.add(new OneSeat(num,userSep));
				OneSeat.SeatGUI osg;
				osg = osArr.get(i).new SeatGUI();
				jpSeatLineRight.add(osg);
			}
			
			add(jpSeatLineTop);
			add(jpSeatLineCenter1);
			add(jpSeatLineCenter2);
			add(jpSeatLineBot);
			add(jpSeatLineRight);

			new ClientSender(socket).start();
			new receive().start();

		}
		
		public boolean getEndChk() {
			return endChk;
		}

		class ClientSender extends Thread implements MouseListener {


			public ClientSender(Socket socket) {
				
				try {

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			@Override
			public void run() {
				for (OneSeat os : osArr) {

					os.sg.addMouseListener(this);    
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {

				OneSeat.SeatGUI jp = (OneSeat.SeatGUI)e.getSource();
								
				if(userSep.equals("°ü¸®ÀÚ")) {
					
					for (OneSeat os : osArr) {
						if(os.num == jp.seatNum) {
							if(os.sg.getBackground() == Color.red  && !sms.signal) {
								
								cu = new ChoiceUser(os.ui, socket, oos, ois, msgChk, msgMap);
												
							}
						}
					}
										
					if(sms.signal) {
						
						UserInfo ui = cu.ui;
														
						try {
							sleep(100);
						} catch (InterruptedException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
											
						try {
									
							for (OneSeat os : osArr) {

								if(os.num == jp.seatNum) {
									
									try {
										
										if(os.sg.getBackground() == Color.white) {
											ui.seat = os.num;
											os.ui = ui;
											
											break;
										}
									} 
									catch (Exception e2) {
										ui.seat = cu.ui.seat;
										break;
									}
									
								}

							}	
							
							ui.moveChk = true;
							
							ui.shutDownChk = false;
							
							sms.signal = false;
							
							db = new DataBox(ui.seat+"", "°ü¸®ÀÚ", ui);
							
							oos.writeObject(db);
							
							oos.flush();
							oos.reset();
																		
							TotalSeatInfo.this.db = null;
							
							ui = null;			
							
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
							
					}
						
				}
				
				endChk = true;

			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

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
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

		}
		
		class receive extends Thread {

			@Override
			public void run() {
				
				while(true) {
					try {
						Thread.sleep(50);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
											
					if(TotalSeatInfo.this.db != null) {
						
						
						if(TotalSeatInfo.this.db.data instanceof UserInfo) {
																			
							AdminTimer at;
								
							UserInfo ui = (UserInfo)TotalSeatInfo.this.db.data;
							
							if(uiVector != null) 
								uiVector.add(ui);
													
							TotalSeatInfo.this.ui = ui;

							if(uiVector != null) 
								uiVector.add(ui);

							for (OneSeat os : osArr) {

								if(os.num == ui.seat) {
														
									os.sg.setBackground(Color.red);
									os.nameLabel.setText(ui.id);
									os.nameLabel.setFont(new Font("µ¸¿ò", Font.BOLD, 12));
									os.timeLabel.setText(ui.userTime);
									os.timeLabel.setFont(new Font("µ¸¿ò", Font.BOLD, 12));
									os.ui = ui;
																							
									ui.userTime = new CustomerDAO().getTime(ui.id);
																	
									at = new AdminTimer(ui.userTime,os.nameLabel, os.timeLabel, ui.seat);
//									at.lastTime = ui.userTime;
									
									atMap.put(ui.id, at);
								
									at.start();
								
									break;
								}

							}
							
						} 
						else if(TotalSeatInfo.this.db.data instanceof LogOutSignal) {
										
							UserInfo ui = ((LogOutSignal)TotalSeatInfo.this.db.data).ui;
							
							if(osArr.get(ui.seat-1).sg.getBackground() == Color.red) {
								
								osArr.get(ui.seat-1).sg.setBackground(Color.white);
								osArr.get(ui.seat-1).nameLabel.setText("");					
								osArr.get(ui.seat-1).timeLabel.setText("");				
								TotalSeatInfo.this.db = null;
								TotalSeatInfo.this.ui = null;
								
								atMap.get(ui.id).timeEnd = true;
								atMap.remove(ui.id);
						
								continue;
							}
							
						}
						else if(TotalSeatInfo.this.db.data instanceof String) {
							
						}
						
								
						TotalSeatInfo.this.db = null;
					}
				}
			}
		}

		class AdminTimer extends Thread {
			
			int seat;
			String userTime;
			JLabel timeLabel;
			JLabel nameLabel;
			
			boolean timeEnd;
			
			String lastTime;
			
			public AdminTimer(String userTime,JLabel nameLabel, JLabel timeLabel, int seat) {
				super();
				this.lastTime = userTime;
				this.timeLabel = timeLabel;
				this.nameLabel = nameLabel;
				this.seat = seat;
			}

			@Override
			public void run() {

				try {

					int i = 0;
					
					while(!timeEnd) {
										
						String tmp = lastTime;
						
						String [] time = lastTime.split(":");
						int hour = Integer.parseInt(time[0]);
						int minute = Integer.parseInt(time[1]);
								
						sleep(1000);
						
						i++;
						
						if(i == 60) {
							
							if(minute == 0) {
								hour--;
								minute = 59;
							}
							minute--;
						}
						
						
						String res = hour+":"+minute;
						timeLabel.setText(res);
						
						if(lastTime.equals("0:0"))
							timeEnd = true;
						
						if(tmp.equals(lastTime))
							lastTime = res;
						
					}
					
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}finally {
					timeLabel.setText("");
				}
			}
		}
		
	}
	
	class OneSeat {

		int num;
		SeatGUI sg;
		Color color = Color.white;
		JLabel nameLabel;
		JLabel timeLabel;

		String userSep;
		
		UserInfo ui;

		public OneSeat(int num, String userSep) {
			super();
			this.num = num;
			this.userSep = userSep;

		}

		class SeatGUI extends JPanel {

			int seatNum = num;

			public SeatGUI() {

				sg = this;

				TitledBorder seatTb = new TitledBorder(new LineBorder(Color.black));

				setLayout(new GridLayout(3, 1));
				setBorder(seatTb);
				setBackground(Color.white);

				JLabel numLabel = new JLabel(" No "+num+"");
				numLabel.setFont(new Font("µ¸¿ò", Font.BOLD, 12));
				nameLabel = new JLabel();
				timeLabel = new JLabel();

				add(numLabel);
				add(nameLabel);
				add(timeLabel);

			}

		}

	}

}

