package server;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import admin.AdminMsgCloseChk;
import data.DataBox;
import data.MsgBox;
import data.UserInfo;
import user.MsgCloseChk;

public class AdminMessage extends JFrame implements WindowListener {
	JButton send;
	JTextArea msgSendArea;
	JTextArea msgViewArea;
	
	Socket socket;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	
	DataBox msg;
	String id = "관리자"; 
	MsgBox text = null;
	boolean chk = true;
	
	int seat;
	
	AdminMsgCloseChk msgChk;
	
	UserInfo dst;
	
	public AdminMessage(UserInfo id, Socket socket, ObjectOutputStream oos, ObjectInputStream ois, AdminMsgCloseChk msgChk) {
		
		this.socket = socket;
		this.oos = oos;
		this.ois = ois;
		dst = id;
		this.msgChk = msgChk;
		
		addWindowListener(this);
		
		setTitle(dst.id);
		
		setBounds(882, 100, 420, 500);
		setLayout(null);

		msgViewArea = new JTextArea();
		msgViewArea.setLineWrap(true);
		msgViewArea.setEditable(false);

		JScrollPane msgViewView = new JScrollPane(msgViewArea);
		msgViewView.setBounds(0, 0, 400, 300);
		msgViewView.setBackground(Color.white);

		msgSendArea = new JTextArea();
		msgSendArea.setLineWrap(true);

		JScrollPane msgSendview = new JScrollPane(msgSendArea);
		msgSendview.setBounds(0, 300, 300, 200);
		msgSendview.setBackground(Color.white);

		send = new JButton("보내기");
		send.setBounds(300, 375, 100, 30);
		
		new ClientSender().start();
		new TcpReceiver().start();
		
		add(send);
		add(msgViewView);
		add(msgSendview);
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

	}
	
	class ClientSender extends Thread implements ActionListener {

		@Override
		public void run() {
			send.addActionListener(this);    
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			try {
				
				String str = msgSendArea.getText();
				
				MsgBox box = new MsgBox();
				box.setMsg(str);
				
				msg = new DataBox(dst.id, id, box);
				msgViewArea.append(id+": "+str+"\n");
				oos.writeObject(msg);
				
				msgSendArea.setText("");
				msgSendArea.setFocusable(true);

			} 
			catch (Exception e1) {
				e1.printStackTrace();
			}
		}    
	}

	class TcpReceiver extends Thread {

		@Override
		public void run() {
			try {

				while(true) {
					
					sleep(10);
					
					if(text != null) {						
					
						msgViewArea.append(dst.id+": "+text.getMsg()+"\n");
					}
					
					text = null;
				}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
		}    
	}
	
	public void setMsg(String sender, MsgBox msgBox) {
		
		dst.id = sender;
		text = msgBox;
		dst.seat = msgBox.getUi().seat;
		
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		msgChk.msgChkArr[dst.seat-1] = false;
	}
	@Override
	public void windowOpened(WindowEvent arg0) {
		msgChk.msgChkArr[dst.seat-1] = true;
	}
	@Override
	public void windowClosed(WindowEvent arg0) {}
	@Override
	public void windowActivated(WindowEvent arg0) {}
	@Override
	public void windowDeactivated(WindowEvent arg0) {}
	@Override
	public void windowDeiconified(WindowEvent arg0) {}
	@Override
	public void windowIconified(WindowEvent arg0) {}
	 
}
