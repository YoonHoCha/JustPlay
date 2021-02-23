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

import data.DataBox;
import data.MsgBox;
import data.UserInfo;
import user.MsgCloseChk;

public class Message extends JFrame implements WindowListener{
	JButton send;
	JTextArea msgSendArea;
	JTextArea msgViewArea;
	
	Socket socket;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	
	DataBox msg;
	String dst = "관리자", text = null;
	boolean chk = true;
	MsgCloseChk msgChk;
	UserInfo ui;
	
	public Message(UserInfo ui, Socket socket, ObjectOutputStream oos, ObjectInputStream ois, MsgCloseChk msgChk) {
		this.socket = socket;
		this.oos = oos;
		this.ois = ois;
		this.ui = ui;
		this.msgChk = msgChk;
		
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
		new Receiver().start();
		
		add(send);
		add(msgViewView);
		add(msgSendview);
		
		addWindowListener(this);
		
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		msgChk.msgChk = false;
	}

	@Override
	public void windowOpened(WindowEvent e) {
		msgChk.msgChk = true;
	}
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
	
	class ClientSender extends Thread implements ActionListener{

		@Override
		public void run() {
			send.addActionListener(this);    
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			try {
				
				String str = msgSendArea.getText();
				
				MsgBox box = new MsgBox();
				box.setUi(ui);
				box.setMsg(str);
				
				msg = new DataBox(dst, ui.id, box);
				
				msgViewArea.append(ui.id+": "+str+"\n");
				oos.writeObject(msg);
				
				msgSendArea.setText("");
				msgSendArea.setFocusable(true);

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}    
	}

	class Receiver extends Thread{
		
		@Override
		public void run() {
			try {
				while(true) {
					sleep(10);
					if(text != null) {
						msgViewArea.append(dst+": "+text+"\n");
					}
					text = null;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}    
	}

	public void setMsg(String msg) {
		text = msg;
	}

}
