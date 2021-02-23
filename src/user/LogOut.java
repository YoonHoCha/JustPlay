package user;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import data.DataBox;
import data.UserInfo;

public class LogOut {
	UserInfo user;
	
	Socket socket;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	
	public LogOut(UserInfo user, Socket socket, ObjectOutputStream oos, ObjectInputStream ois) {
		super();
		this.user = user;
		this.socket = socket;
		this.oos = oos;
		this.ois = ois;
		
		new ClientSender().run();
	}
	
class ClientSender extends Thread {

		@Override
		public void run() {
			try {
				user.useChk = false;
				System.out.println(socket);
				DataBox msg = new DataBox("°ü¸®ÀÚ", user.id, user);
				
				oos.writeObject(msg);
				
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				try {
					oos.close();
					ois.close();
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}    
	}
   
	
}
