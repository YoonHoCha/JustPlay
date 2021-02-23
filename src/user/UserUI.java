package user;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import data.ChargeSignal;
import data.DataBox;
import data.EndSignal;
import data.LogOutSignal;
import data.MoveSignal;
import data.MsgBox;
import data.OffSignal;
import data.RegistSignal;
import data.TransData;
import data.UserInfo;
import data.UserMoveSignal;
import db.CustomerDAO;
import server.Message;

class windowListnerAdapter implements WindowListener{

	JButton userEnd;

	public windowListnerAdapter(JButton userEnd) {
		super();
		this.userEnd = userEnd;
	}

	@Override
	public void windowClosing(WindowEvent e) {
		userEnd.doClick();
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

public class UserUI extends Thread {
	JLabel userTimer;
	JButton message, userEnd, seatMove;
	Calendar start;
	UserInfo user;
	Timer timer;
	Time tt;

	Socket socket;
	ObjectOutputStream oos;
	ObjectInputStream ois;

	Message msg;
	UI ui;
	MsgCloseChk msgChk;

	UserMoveSignal ums;
	TcpReceiver re;

	ArrayList<UserInfo> userArr;
	boolean shutDownChk = false, timeEnd = false, moveChk = false;
	static String lastTime;
	
	boolean serchChk, bgChk, orderChk, msgOpenChk;
	UserViewUserInfo vui;
	orderGUI or;
	BestGame bg;
	
	public UserUI(Socket socket, UserInfo user, ObjectOutputStream oos, ObjectInputStream ois, Calendar start, Time tt) {
		try {
			this.oos = oos;
			this.ois = ois;
			this.socket = socket;
			this.start = start;
			this.tt = tt;

			msgChk = new MsgCloseChk();
			this.oos.writeObject(new DataBox(null, user.id, new RegistSignal()));

			this.user = user;
			lastTime = user.userTime;
			ui = new UI();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	class MyPanel extends JPanel {
		Image img;
		
		public MyPanel(Image img) {
			super();
			this.img = img;
		}

		@Override
		public void paint(Graphics g) {
			g.drawImage(img, 0, 0, null);
		}
	}
	
	class UI extends JFrame {

		windowListnerAdapter wc;

		public UI() {
			super("User 개인 PC 화면");
			
			setBounds(50, 70, 1000, 800);
			setLayout(null);
			
			ImageIcon icon = new ImageIcon("img/배경화면.PNG");
			Image img = icon.getImage();
			
			
			/////1
//			MyPanel screen = new MyPanel(img);
			JPanel screen = new JPanel();
			screen.setLayout(null);
			screen.setBounds(0, 0, 1000, 800);
			screen.setVisible(true);
			screen.setOpaque(false);
			
			//2
			JPanel userMenu = new JPanel();
			userMenu.setBounds(582, 0, 400, 170);
			userMenu.setLayout(null);
			userMenu.setBackground(Color.gray);
			userMenu.setOpaque(false);
			
			JLabel seatNo = new JLabel("자리 번호 : NO."+user.seat);
			seatNo.setBounds(20, 0, 120, 30);

			userEnd = new JButton("사용종료");
			userEnd.setBounds(290, 0, 100, 30);
			userEnd.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//유저 시간 정보 전달 userTime
					//로그아웃

					try {
						user.shutDownChk = true;
						user.useChk = false;
						Calendar end = Calendar.getInstance();
						long startTime = start.getTimeInMillis();
						long endTime = end.getTimeInMillis();

						long useTime = endTime - startTime;
						int useHour = (int)useTime/(1000*60*60);
						int useMinute = (int)useTime/(1000*60);

						String [] res = new CustomerDAO().getTimer(user.id).split(":");
						int t_hour = Integer.parseInt(res[0]) + useHour;
						int t_min = Integer.parseInt(res[1]) + useMinute;
						
						if(t_min > 60) {
							t_min -= 60;
							t_hour++;
						}
						
						String useTimeDB = t_hour+":"+t_min;

						new CustomerDAO().setTimer(useTimeDB, user.id); 

						oos.writeObject(new DataBox("관리자", user.id, new LogOutSignal(user)));
						
						oos.flush();
						oos.reset();
						
						oos.writeObject(new DataBox("서버", user.id, new OffSignal()));
						
						sleep(500);
						
						tt.timeClose();
						dispose();
						new OnAndOff();

					} catch (Exception e1) {
						e1.printStackTrace();
					}

				}
			});

			seatMove = new JButton("회원 정보 검색");
			seatMove.setBounds(150, 0, 130, 30);
			seatMove.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if(!serchChk) {
						vui = new UserViewUserInfo(user.id);
						serchChk = true;
					}else {
						vui.uig.dispatchEvent(new WindowEvent(vui.uig, WindowEvent.WINDOW_CLOSING));
						vui = new UserViewUserInfo(user.id);
					}
					
				}
			});

			

			JLabel userID = new JLabel(user.id);
			userID.setBounds(20, 40, 100, 30);

			userTimer = new JLabel("남은 시간 : "+user.userTime);
			userTimer.setBounds(20, 80, 150, 30);

			timer = new Timer(userEnd);
			timer.start();

			JButton foodOrder = new JButton("먹거리 주문");
			foodOrder.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					//주문 화면 띄우기
					if(!orderChk) {
						or = new orderGUI(user, oos);
						orderChk = true;
					}else {
						or.dispatchEvent(new WindowEvent(or, WindowEvent.WINDOW_CLOSING));
						or = new orderGUI(user, oos);
					}
					//new orderGUI(user, oos);
				}
			});
			foodOrder.setBounds(20, 120, 150, 30);

			message = new JButton("메세지");
			message.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					msgChk.msgChk = true;
					if(!msgOpenChk) {
						msg = new Message(user, socket, oos, ois, msgChk);
						msgOpenChk = true;
					}else {
						msg.dispatchEvent(new WindowEvent(msg, WindowEvent.WINDOW_CLOSING));
						msg = new Message(user, socket, oos, ois, msgChk);
					}

				}
			});
			message.setBounds(200, 120, 150, 30);

			//3
			JButton gameBtn = new JButton("게임");
			gameBtn.setBounds(50, 10, 100, 100);
			gameBtn.setBackground(Color.white);
			gameBtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if(!bgChk) {
						bg = new BestGame();
						bgChk = true;
					}else {
						bg.dispatchEvent(new WindowEvent(bg, WindowEvent.WINDOW_CLOSING));
						bg = new BestGame();
					}
					//bg = new BestGame();
				}
			});

			userMenu.add(seatNo);
			userMenu.add(seatMove);
			userMenu.add(userEnd);
			userMenu.add(userID);
			userMenu.add(userTimer);
			userMenu.add(foodOrder);
			userMenu.add(message);
			screen.add(userMenu);
			
			add(screen);
			screen.add(gameBtn);
			
			re = new TcpReceiver();
			re.start();

			wc = new windowListnerAdapter(userEnd);
			addWindowListener(wc);

			setVisible(true);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}

	}

	class TcpReceiver extends Thread {

		@Override
		public void run() {

			try {
				while(ois!=null && !shutDownChk) {

					Object oo = ois.readObject();
					System.out.println(oo.getClass());
					if(oo instanceof DataBox) {
						DataBox tsi = (DataBox)oo;
						System.out.println(tsi);

						if(tsi.data instanceof MsgBox) {
							System.out.println(msgChk.msgChk);
							if(!msgChk.msgChk)
								message.doClick();

							msg.setMsg(((MsgBox)tsi.data).getMsg());
						} else if(tsi.data instanceof UserInfo){
							user = (UserInfo)tsi.data;
							if(user.moveChk) {
								shutDown();
								ui.dispose();

								Socket socket = new Socket("127.0.0.1", 7777);
								new CustomerDAO().updateTime(user.id, lastTime);

								return ;
							}else {

							}

						} else if(tsi.data instanceof TransData) {
							TransData td = (TransData)tsi.data;
							userArr = td.userArr;
						} else if(tsi.data instanceof MoveSignal) {
							System.out.println("관리자 자리이동");
							try {
								user.shutDownChk = true;
								user.useChk = false;
								Calendar end = Calendar.getInstance();
								long startTime = start.getTimeInMillis();
								long endTime = end.getTimeInMillis();

								long useTime = endTime - startTime;
								int useHour = (int)useTime/(1000*60*60);
								int useMinute = (int)useTime/(1000*60);
								
								String [] res = new CustomerDAO().getTimer(user.id).split(":");
								int calcMinute = Integer.parseInt(res[1]) + useMinute;
								int calcHour = Integer.parseInt(res[0]) + useHour;
								
								if(calcMinute > 60) {
									calcMinute -= 60;
									calcHour++;
								}
								
								String useTimeDB = calcHour+":"+calcMinute;

								new CustomerDAO().setTimer(useTimeDB, user.id); 

								oos.writeObject(new DataBox("관리자", user.id, new LogOutSignal(user)));
								oos.flush();
								oos.reset();
								
								oos.writeObject(new DataBox("서버", user.id, new OffSignal()));
								
								sleep(100);
								ui.dispose();
								tt.timeClose();
								new OnAndOff();
							} catch (Exception e1) {
								e1.printStackTrace();
							} finally {
								stop();
							}

							
						} else if(tsi.data instanceof ChargeSignal) {
							lastTime = ((ChargeSignal)tsi.data).time;
							new CustomerDAO().setTimer(lastTime, user.id);
						
						} else if(tsi.data instanceof OffSignal) {
							System.out.println("유저  - 서버에서 온 종료 신호");
							new CustomerDAO().updateTime(user.id, lastTime);
							shutDown();
							timeEnd = true;
							
//							tt.timeClose();
							return ;
						}

					}
				}
				return ;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				stop();
			}
		}    

	}

	public class Timer extends Thread {
		boolean chk = true;
		JButton userEnd;
		public Timer(JButton userEnd) {
			super();
			this.userEnd = userEnd;
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

					if(time.equals("-1") || !chk) {
						userTimer.setText("");
						chk = true;
						return;
					}
					
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
					userTimer.setText("남은 시간 : "+res);

					if(lastTime.equals("0:0"))
						timeEnd = true;

					if(tmp.equals(lastTime))
						lastTime = res;
					
				}
				if(lastTime.charAt(0) == '-')
					lastTime = "0:0";
				
				new CustomerDAO().updateTime(user.id, lastTime);
				userEnd.doClick();
			} catch (InterruptedException e1) {
				
			}
		}

	}

	void shutDown() {
		try {
			shutDownChk = true;
			timer.chk = false;
			ui.dispose();

			ois.close();
			oos.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
