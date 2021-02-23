package user;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFrame;

import data.DataBox;
import data.OffSignal;
import data.RegistSignal;
import data.SetSeat;
import data.UserInfo;

public class OnAndOff extends JFrame {
	int seat = 12;
	Socket socket;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	
	boolean end = false;
	Time tt;
	
	public OnAndOff() {
		try {
			socket = new Socket("127.0.0.1",7777);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			
			oos.writeObject(new DataBox(null, seat+"", new RegistSignal()));
			
			setBounds(500, 500, 300, 300);
			setLayout(new BorderLayout());
			
			JButton btn = new JButton("ON");
			btn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
					tt = new Time();
					end = true;
					try {
						Thread.sleep(500);
						oos.close();
						socket.close();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					new JoinId_gui(seat, tt);
				}
			});
			
			new reci().start();
			
			add(btn);
			setVisible(true);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} 
	}
	
	class reci extends Thread {
		
		@Override
		public void run() {
			try {
				while(ois != null) {
					DataBox box = (DataBox)ois.readObject();
					
					if(box.data instanceof UserInfo) {
						tt = new Time();
						UserInfo user = (UserInfo)box.data;
						user.useChk = true;
						
						Socket uisock = new Socket("127.0.0.1", 7777);
						oos.writeObject(new DataBox("관리자", box.receiver, user));
						
						new UserUI(uisock,
								(UserInfo)box.data,
								new ObjectOutputStream(uisock.getOutputStream()),
								new ObjectInputStream(uisock.getInputStream()),
								Calendar.getInstance(),
								tt);
						
						oos.flush();
						oos.reset();
						
						oos.writeObject(new DataBox("서버", seat+"", new OffSignal()));
					} else if(box.data instanceof OffSignal) {
						
						sleep(100);
						
						end = true;
						dispose();
						return;
					}
				}
			} catch (ClassNotFoundException e) {
				
			} catch (Exception e) {
				
			} finally {
				try {
					ois.close();
					oos.close();
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		new OnAndOff();
	}

}
