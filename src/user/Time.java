package user;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import data.ChargeSignal;
import data.DataBox;
import data.FeeData;
import data.FeeSignal;
import data.OffSignal;
import data.RegistSignal;
import data.UserInMap;
import db.StaticIP;
import user.Time.ClientReceiver;
import user.Time.ClientSender;

class windowListenerAdapter implements WindowListener {
	Time tt;

	public windowListenerAdapter(Time tt) {
		super();
		this.tt = tt;
	}

	@Override
	public void windowClosing(WindowEvent e) {
		try {
			System.out.println("���� �ݾҴ�!");
			tt.oos.writeObject(new DataBox("����", tt.name, new OffSignal()));
			tt.dispose();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}

	@Override
	public void windowOpened(WindowEvent e) {}
	@Override
	public void windowClosed(WindowEvent e) {
		
		System.out.println("������");
	}
	@Override
	public void windowIconified(WindowEvent e) {}
	@Override
	public void windowDeiconified(WindowEvent e) {}
	@Override
	public void windowActivated(WindowEvent e) {}
	@Override
	public void windowDeactivated(WindowEvent e) {}

}

class ID_chk{
	String id, name,timer,time; 
	int price;

	public ID_chk(String id, String name,String timer) {
		super();
		this.id = id;
		this.name=name;
		this.timer=timer;
	}

	@Override
	public String toString() {
		return  "id=" + id +name+ ", �����ð� :"+timer;
	}

}


class Price_chk{
	String time;
	int price;

	public Price_chk(int price, String time) {
		super();
		this.price=price;
		this.time=time;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return  price+","+time;
	}

}

class Price_chk_DB{//fee ������ 
	Price_chk pc;
	ArrayList<Price_chk> plist =new ArrayList<Price_chk>();

	public Price_chk_DB(String sql) {

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

			Connection con = DriverManager.getConnection(
					"jdbc:oracle:thin:@"+StaticIP.ip+":1521:xe",
					"hr",
					"hr");

			Statement stmt = con.createStatement();

			ResultSet rs= stmt.executeQuery(sql);
			while(rs.next()) {
				pc= new Price_chk(rs.getInt("price"),rs.getString("time"));

				//db���� ������ �� : �̸� ���̵� �ð� 
				plist.add(pc);
			}
			for (Price_chk pchk : plist) {
				System.out.println(pchk);
			}
			rs.close();		
			stmt.close();
			con.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	ArrayList<Price_chk> getPlist(){
		return plist;
	}
}


class ID_chk_DB{//ȸ�� ���� ������
	ID_chk st;
	ArrayList<ID_chk> list = new ArrayList<ID_chk>();

	public ID_chk_DB(String sql) {


		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

			Connection con = DriverManager.getConnection(
					"jdbc:oracle:thin:@"+StaticIP.ip+":1521:xe",
					"hr",
					"hr");

			Statement stmt = con.createStatement();

			ResultSet rs= stmt.executeQuery(sql);

			while(rs.next()) {
				st= new ID_chk(rs.getString("id"), rs.getString("name"),rs.getString("timer")); 

				list.add(st);
			}

			rs.close();		
			stmt.close();
			con.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	ArrayList<ID_chk> getlist(){
		return list;
	}
}

class New_dbconnect{		
	public New_dbconnect(String sql) {
		//172.16.101.162
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

			Connection con = DriverManager.getConnection(
					"jdbc:oracle:thin:@"+StaticIP.ip+":1521:xe", 
					"hr", 
					"hr");

			Statement stmt = con.createStatement();


			int cnt = stmt.executeUpdate(sql);

			stmt.close();
			con.close();

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
public class Time extends JFrame{//�����ð����� 
	JTextField id_input;
	ArrayList<ID_chk> list;
	ArrayList<Price_chk> plist;
	ArrayList<JButton> bg;
	JLabel ms;
	JLabel ms2;
	JLabel time;
	JButton time_1;
	JButton clear;
	JPanel jp;
	String payTime;
	String id_i;
	JButton btn;
	int btnFee=0 ;
	String real_id;
	
	Socket socket;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	
	DataBox db;
	String name = "������2";

	boolean isIn;

	boolean chk = true;

	class Chk_bt implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			// System.out.println(id_input.getText());
			plist = new Price_chk_DB("select * from fee order by price").getPlist();

			list = new ID_chk_DB("select * from customer").getlist();
			for (int i = 0; i < list.size(); i++) {
				if(!id_input.getText().equals(list.get(i).id)) {
					payTime = null;
					ms2.setText("");
					ms.setText("���̵� Ȯ�����ּ���");
				}else {
					real_id=list.get(i).id;
					ms.setText("ȸ��: "+list.get(i).id+" �ð��� ���� ���ּ���");

					//	if(id_input.getText().equals(list.get(i).id)) {
					time.setText(list.get(i).timer);


					if(list.get(i).timer==null)
						list.get(i).timer="0:0";
					
					time.setText(list.get(i).timer);
					//payTime = null;
					chk=false;
					return;
				}
				time.setText("");
				//time_1.setSelected(false);
			}
			//payTime = null;
			
		}

	}

	ID_chk idchk;
	class Price_bt1 implements ActionListener{

		//������         ����    ȸ������ ->  ���� ������   
		//     �α���  -> �����ͼ� �� 
		@Override
		public void actionPerformed(ActionEvent e) {
			btn = (JButton)e.getSource();
			System.out.println("���, �ð� : "+btn.getText());
			int  btnHour = 0, btnMinute = 0;


			String [] arr = btn.getText().split("��");
			btnFee = Integer.parseInt(arr[0]);

			String [] timeArr = arr[1].split("�ð�");
			btnHour = Integer.parseInt(timeArr[0]);

			btnMinute = Integer.parseInt(timeArr[1].substring(0, timeArr[1].length()-1));
			if(list != null) {
				for (int i = 0; i < list.size(); i++) {
					if(id_input.getText().equals(list.get(i).id)) {
						
						if(list.get(i).timer.equals("null")) {
							list.get(i).timer = "0:0";
						}
						
						String [] userTime = list.get(i).timer.split(":");//�������� �� �ð� 
						int hour = Integer.parseInt(userTime[0]);
						int minute = Integer.parseInt(userTime[1]);

						hour += btnHour;
						minute += btnMinute;

						if(minute >= 60) {
							minute -= 60;
							hour++;
						}
						payTime = hour+":"+minute;
						ms2.setText("-> "+payTime);//���� �� �ð� 
						ms2.setForeground(Color.red);
						break;
					}else {
						//ms2.setText("");
						ms.setText("");
						//time.setText("");
					}
				}
			}else
				btn.getText();

		}

	}
	class Clear_bt1 implements ActionListener{


		boolean cchk() {
			boolean chk= false;

			for (int i = 0; i < list.size(); i++) {

				{
					if(!id_input.getText().equals(real_id)) {

						chk= true;
					}else {
						chk=false;
						break;
					}
				}
			}
			return chk;

		}
		//��ġ���� ����� �����ϴ� ��ư�� �ʱ�ȭ 

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(!chk) {	
				if(id_input.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "���̵� �Է����ּ���.", "Msg", JOptionPane.OK_OPTION);
				}
				else{

					if(!cchk()) {
						if(payTime!=null) {
							new New_dbconnect("update customer set timer = '"+payTime+"' where id = '"+id_i+"'");
							JOptionPane.showMessageDialog(null, "������ �Ϸ�Ǿ����ϴ�.", "Msg", JOptionPane.OK_OPTION);

							id_input.setText("");
							ms.setText("");
							time.setText("");
							ms2.setText("");


						}else{
							JOptionPane.showMessageDialog(null, "����� �������ּ���.", "Msg", JOptionPane.OK_OPTION);
						}

					}else {
						JOptionPane.showMessageDialog(null, "���̵� Ȯ�����ּ���.", "Msg", JOptionPane.OK_OPTION);
					}
				}
			}else {
				JOptionPane.showMessageDialog(null, "���̵� Ȯ�����ּ���.", "Msg", JOptionPane.OK_OPTION);
			}
			//new New_dbconnect("insert into log (fee) values('"+btnFee+"')");
		}

	}//��ǰ�̸� ã�Ƽ� �߰��ϴ� ���� ��ŭ ������Ʈ 
	public Time() {		//frame s. 
		super("�ð�����");
		addWindowListener(new windowListenerAdapter(this));
		
		try {
			socket = new Socket("127.0.0.1", 7777);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());

			oos.writeObject(new DataBox(null, name, new RegistSignal()));

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		

		setBounds(980, 20, 300, 600);
		setLayout(null);


		JLabel jb = new JLabel("ID"); 
		jb.setBounds(40,10, 100, 30);
		add(jb);

		id_input = new JTextField();
		id_input.setBounds(40, 40, 130, 26);
		id_input.setBackground(Color.white);
		add(id_input);

		ms=new JLabel();
		ms.setBounds(45, 60,200, 40);
		add(ms);

		ms2=new JLabel();
		ms2.setBounds(180, 95, 80, 40);
		add(ms2);

		time = new JLabel();
		time.setBounds(150, 95, 50, 40);
		add(time);


		JButton id_search = new JButton("search");
		id_search.setBounds(170, 40, 80, 25);
		id_search.setBackground(Color.gray); 
		add(id_search);

		id_search.addActionListener(new Chk_bt());


		JLabel jb2 = new JLabel("�ð� �����ϱ�"); 
		jb2.setBounds(40,100, 100, 30);
		add(jb2);
		
		jp = new JPanel();
		jp.setBounds(0, 120, 300, 360);
		jp.setLayout(null);
		
		plist = new Price_chk_DB("select * from fee order by price").getPlist();
		
		int i = 0;
		ButtonGroup bg1= new ButtonGroup();
		bg= new ArrayList<JButton>();
		for (Price_chk pc : plist) {
			String [] tmp = pc.time.split(":");
			time_1 = new JButton(pc.price+"��"+tmp[0]+"�ð�"+tmp[1]+"��");
			time_1.setBackground(Color.white);
			bg.add(time_1);
			bg1.add(time_1);
			time_1.setBounds(40,20+i, 200, 50);
			jp.add(time_1);
			i += 70;
			time_1.addActionListener(new Price_bt1());
		}
		
		add(jp);
		clear = new JButton("�����ϱ� ");

		clear.setBounds(70,480, 140, 50);
		add(clear);
		clear.setBackground(Color.white);
		clear.addActionListener(new Clear_bt1());
		
		new ClientSender().start();
		new ClientReceiver().start();

		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);	
	}
	
	void genBtn() {
		for (JButton jb : bg) {
			jp.remove(jb);
		}
			
		plist = new Price_chk_DB("select * from fee order by price").getPlist();
		
		int i = 0;
		ButtonGroup bg1= new ButtonGroup();
		bg= new ArrayList<JButton>();
		for (Price_chk pc : plist) {
			String [] tmp = pc.time.split(":");
			time_1 = new JButton(pc.price+"��"+tmp[0]+"�ð�"+tmp[1]+"��");
			time_1.setBackground(Color.white);
			bg.add(time_1);
			bg1.add(time_1);
			time_1.setBounds(40,20+i, 200, 50);
			jp.add(time_1);
			i += 70;
			time_1.addActionListener(new Price_bt1());
		}

		revalidate();
		repaint();
	}

	public void timeClose() {
		try {
			System.out.println("timeClose");
			oos.writeObject(new DataBox("����", name, new OffSignal()));
			oos.flush();
			oos.reset();
			
		} catch (Exception e) {
			System.out.println("timeclose ����");
			e.printStackTrace();
		}
	}

	class ClientSender extends Thread implements ActionListener{

		@Override
		public void run() {

			clear.addActionListener(this);    
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				UserInMap uim = new UserInMap();
				uim.user = id_input.getText();
				System.out.println("ID - "+uim.user);
				oos.writeObject(new DataBox("����", name, uim));
				oos.flush();
				oos.reset();

				sleep(500);
				System.out.println("isIn - "+isIn);
				if(isIn) {
					System.out.println("chargesignal");
					ChargeSignal charge = new ChargeSignal(payTime);
					db = new DataBox(id_input.getText(), name, charge);
					oos.writeObject(db);

					oos.flush();
					oos.reset();

					FeeData fd = new FeeData();
					fd.id = id_input.getText();
					fd.fee = btnFee;
					fd.userTime = payTime;

					new New_dbconnect("update customer set timer = '"+payTime+"' where id = '"+real_id+"'");

					db = new DataBox("������", name, fd);

					oos.writeObject(db);

					oos.flush();
					oos.reset();
				}else {
					new New_dbconnect("update customer set timer = '"+payTime+"' where id = '"+real_id+"'");
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		}


	}

	class ClientReceiver extends Thread {

		@Override
		public void run() {

			try {
				while(ois != null) {
					
					DataBox db = (DataBox)ois.readObject();

					if(db.data instanceof ChargeSignal) {

						FeeData fd = new FeeData();
						fd.id = id_input.getText();
						fd.fee = btnFee;

						new New_dbconnect("update customer set timer = '"+payTime+"' where id = '"+id_i+"'");

						db = new DataBox("������", name, fd);

						oos.writeObject(db);

						oos.flush();
						oos.reset();
						
					} else if(db.data instanceof OffSignal) {
						ois.close();
						oos.close();
						socket.close();
						
						return;
					} else if(db.data instanceof UserInMap) {
						System.out.println("�������� �� ��ȣ");
						isIn = ((UserInMap)db.data).isIn;
					} else if(db.data instanceof FeeSignal) {
						System.out.println("�ð������ȣ!!");
						genBtn();
						
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				dispose();
				System.out.println(name+" ����");
			}
			
			
			System.out.println(name+"����");
		}

		
		 
	}

}
