package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import data.ChargeSignal;
import data.DataBox;
import data.EndSignal;
import data.FeeSignal;
import data.MapReq;
import data.MoveSignal;
import data.MsgBox;
import data.OffSignal;
import data.RegistSignal;
import data.SetSeat;
import data.StartSignal;
import data.TransData;
import data.UserInMap;
import data.UserInfo;
import data.UserMoveSignal;

public class MulServerMain {

	HashMap<String ,ObjectOutputStream> arr;
	HashMap<String, ObjectOutputStream> moveUser;
	DataBox tmp;
	boolean startChk = false;
	
	public MulServerMain() {
		arr = new HashMap<String, ObjectOutputStream>();
		moveUser = new HashMap<String, ObjectOutputStream>();
		Collections.synchronizedMap(arr);

		try {
			ServerSocket server = new ServerSocket(7777);
			System.out.println("���� ����");
			while(true) {
				Socket client = server.accept();

				new MulReceiver(client).start();
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	class MulReceiver extends Thread {
		String name;
		ObjectOutputStream dos;
		ObjectInputStream dis;


		public MulReceiver(Socket client) {

			try {
				dos = new ObjectOutputStream(client.getOutputStream());
				dis = new ObjectInputStream(client.getInputStream());

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {

			try {

				while(dis != null) {
					DataBox box = (DataBox)dis.readObject();
					name = box.sender;
					System.out.println(box.data);
					if(box.data != null) {
						
						if(box.data instanceof MsgBox) {
							if(box.receiver.equals("ALL")){
								sendToAll(box);
							}else {
								if(arr.containsKey(box.receiver))
									sendToOne(box, box.receiver);
								else {
									MsgBox str = ((MsgBox)box.data);
									str.setMsg("���� ����� �Դϴ�.");
									box.data = str;
									
									sendToOne(box, box.sender);
								}
							}
						} else if(box.data instanceof ChargeSignal) {
							if(arr.containsKey(box.receiver)) {
								sendToOne(box, box.receiver);
							}else {
								sendToOne(box, "������");
							}
						} else if(box.data instanceof RegistSignal) {
							System.out.println(name+" ���");
							arr.put(box.sender, dos);
							System.out.println("��� - "+arr);
						} else if(box.data instanceof OffSignal) {
							sendToOne(box, box.sender);
							System.out.println("�����ȣ");
							System.out.println(name+" ����");
							arr.remove(name);
							System.out.println(arr);
						} else if(box.data instanceof UserInMap) {
							System.out.println("������� ���� �� ��ȣ");
							if(arr.containsKey(((UserInMap)box.data).user)) {
								System.out.println("�־�!");
								((UserInMap)box.data).isIn = true;
							}else {
								System.out.println("����!");
								((UserInMap)box.data).isIn = false;
							}
							System.out.println(((UserInMap)box.data).isIn+" ���¾�!");
							sendToOne(box, box.sender);
						} else if(box.data instanceof MapReq){
							System.out.println("�� ��û!");
							ArrayList<String> list = new ArrayList<String>();
							for (Entry<String, ObjectOutputStream> en : arr.entrySet()) {
								list.add(en.getKey());
							}
							
							DataBox res = new DataBox(box.sender, "����", list);
							System.out.println(box.sender);
							sendToOne(res, box.sender);
						}else if(box.data instanceof MoveSignal) {
							if(arr.containsKey(box.receiver))
								sendToOne(box, box.receiver);
							else {
								sendToOne(box, box.sender);
							}
						}else if(box.data instanceof FeeSignal) {
							System.out.println("��� ����!!");
							for (Entry<String, ObjectOutputStream> en : arr.entrySet()) {
								if(en.getKey().contains("������")) {
									System.out.println(en.getKey()+"���� ���´�!");
									sendToOne(box, en.getKey());
								}
							}
						}else {
							sendToOne(box, box.receiver);
						}
					}

				}

			} catch (Exception e) {
				System.out.println(name+" ����");
				arr.remove(name);	
			} finally {
				try {
					dis.close();
					dos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	void sendToAll(DataBox box) {
		for (String dd : arr.keySet()) {
//			if(!dd.equals("������")) {
				try {
					arr.get(dd).writeObject(box);
				} catch (IOException e) {
					e.printStackTrace();
				}
//			}
		}

	}

	void sendToOne(DataBox box, String reci) {
		if(reci == null) {
			return;
		}
		try {
			if(box.data instanceof EndSignal) {
				System.out.println(reci+"���� �̵���ȣ ����");
				moveUser.get(reci).writeObject(box);
			}else {
				arr.get(reci).writeObject(box);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		new MulServerMain();

	}

}
