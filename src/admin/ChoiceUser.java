package admin;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.w3c.dom.NameList;

import admin.TotalSeatInfo.OneSeat;
import data.DataBox;
import data.MoveSignal;
import data.SeatMovingSignal;
import data.UserInfo;
import server.AdminMessage;
import user.MsgCloseChk;

public class ChoiceUser {
	
	JButton btnMsg;
	JButton btnSeatMove;
	

	String id; 
	Socket socket ; 
	ObjectOutputStream oos; 
	ObjectInputStream ois; 
	OneSeat os;
	UserInfo ui;
	SeatMovingSignal sms;
	AdminMessage msg;
	DataBox db;
	MoveSignal ms;
	JLabel idLabel;
	
	HashMap<Integer, AdminMessage> msgMap;
	
	AdminMsgCloseChk msgChk;
	
	public ChoiceUser(UserInfo ui, Socket socket, ObjectOutputStream oos, ObjectInputStream ois, 
			AdminMsgCloseChk msgChk, HashMap<Integer, AdminMessage> msgMap) {
		
		this.ui = ui;
		this.id = ui.id;
		this.socket = socket;
		this.oos = oos;
		this.ois = ois;
		this.msgChk = msgChk;
		this.msgMap = msgMap;
		new ChoiceUserGUI();
		
	}
	
	class ChoiceUserGUI extends JFrame implements ActionListener {
		
		public ChoiceUserGUI() {
			
			setTitle("선택");
			setLayout(new GridLayout(3,1));
			
			setBounds(800, 300, 300, 300);
			
			idLabel = new JLabel(ui.id+"("+ui.name+")");
			idLabel.setFont(new Font("바탕체", Font.PLAIN, 20));
			idLabel.setHorizontalAlignment(JLabel.CENTER);
			add(idLabel);
			
			btnMsg = new JButton("메세지");
			btnMsg.setFont(new Font("바탕체", Font.PLAIN, 30));
			btnMsg.setForeground(Color.white);
			btnMsg.setBackground(Color.gray);
			add(btnMsg);
			
			btnMsg.addActionListener(this);
			
			btnSeatMove = new JButton("자리 이동");
			btnSeatMove.setFont(new Font("바탕체", Font.PLAIN, 30));
			btnSeatMove.setForeground(Color.white);
			btnSeatMove.setBackground(Color.gray);
			add(btnSeatMove);
			
			btnSeatMove.addActionListener(this);
			
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setVisible(true);
			
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			
			JButton jb = (JButton)e.getSource();

			if(jb.getText().equals("메세지")) {
							
				msg = new AdminMessage(ui, socket, oos, ois, msgChk);
				
				System.out.println(msgMap+".>>>>>.");
				
				msgMap.put(ui.seat, msg);
				
				dispose();
			}
			
			else if(jb.getText().equals("자리 이동")) {
				
				sms.signal = true;
				
				ms = new MoveSignal(ui);
				
				db = new DataBox(ui.id, "관리자", ms);
				
				try {
				
					Thread.sleep(10);
					
					oos.writeObject(db);
					
					oos.flush();
					oos.reset();
					
				} 
				catch (Exception e1) {
									
					e1.printStackTrace();
				}
				
				dispose();
			}
			
		}
		
		
	}
		
}
