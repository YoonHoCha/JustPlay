package admin;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import data.DataBox;
import data.FeeSignal;
import db.StaticIP;


class Main_Table {
	String price, time;


	public Main_Table(String price, String time) {
		super();
		this.price=price;
		this.time=time;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return  price + time;
	}

}

class Main_Table_DB{//fee 데이터 
	Main_Table mt;
	ArrayList<Main_Table> ta_list =new ArrayList<Main_Table>();

	public Main_Table_DB(String sql) {

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

			Connection con = DriverManager.getConnection(
					"jdbc:oracle:thin:@"+StaticIP.ip+":1521:xe",
					"hr",
					"hr");

			Statement stmt = con.createStatement();

			ResultSet rs= stmt.executeQuery(sql);//select * from fee
			while(rs.next()) {
				mt= new Main_Table(rs.getString("price"),rs.getString("time"));

				//db에서 가져올 것 : 요금 시간 
				ta_list.add(mt);
			}
			for (Main_Table pchk : ta_list) {

			}
			rs.close();		
			stmt.close();
			con.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	ArrayList<Main_Table> getslist(){
		return ta_list;
	}
}

class New_dbconnect{		
	public New_dbconnect(String sql) {
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

class FeeInfo {
	int fee;
	String time;

	public FeeInfo(int fee, String time) {
		super();
		this.fee = fee;
		this.time = time;
	}

	@Override
	public String toString() {
		return fee+","+time;
	}

}

class setting_Gui extends JFrame {//처음 세팅 창 화면 설정 
	Set<FeeInfo> ss= new HashSet<FeeInfo>();
	ArrayList<FeeInfo> Feeinfodata = new ArrayList<FeeInfo>() ;
	JTable set_list;
	ArrayList<Main_Table> ta_list = new Main_Table_DB("select * from fee").getslist();
	DefaultTableModel model;
	int fee = 0;
	String time = null;
	JTextField fee_set;
	JTextField hour_set;
	JComboBox min_box;
	JButton change_bt ;
	JButton bt;
	JFrame reprice_set;
	JFrame price_set;
	repricingSet_Gui rps;
	boolean reChk;

	Socket socket ; 
	ObjectOutputStream oos;

	class change_bt_act implements ActionListener {//change_bt_act버튼 액션리스너 
		@Override
		public void actionPerformed(ActionEvent e) {

			int cl =set_list.getSelectedRow();

			if(reprice_set!= null) {
				reprice_set.dispose();
				reprice_set = null;
			}

			if(cl!=-1) {
				new repricingSet_Gui(); 

			}else {
				JOptionPane.showMessageDialog(null, "수정할 요금을 선택하세요.", "Msg", JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	class new_bt_act implements ActionListener {//new_bt버튼 액션리스너 
		@Override
		public void actionPerformed(ActionEvent e) {

			if(price_set!= null) {
				price_set.dispose();
				price_set = null;
			}



			if(set_list.getRowCount() <5) {

				new pricingSet_Gui();

			}else {
				JOptionPane.showMessageDialog(null, "등록갯수가 초과되었습니다.", "Msg", JOptionPane.ERROR_MESSAGE);
			}
		}
	}


	class del_bt_act implements ActionListener{//del_bt버튼 액션리스너 
		@Override
		public void actionPerformed(ActionEvent e) {
			//내가 원하는 위치를 가져와 

			int cl = set_list.getSelectedRow();

			if(cl!=-1) {

				String bb = ""+model.getValueAt(set_list.getSelectionModel().getLeadSelectionIndex(), 0);

				model.removeRow(cl);

				new New_dbconnect("delete from fee where price ='"+bb+"'");

				JOptionPane.showMessageDialog(null, "요금이 삭제되었습니다.", "Msg", JOptionPane.ERROR_MESSAGE);


				try {

					Thread.sleep(100);

					socket = new Socket("127.0.0.1",7777);
					oos = new ObjectOutputStream(socket.getOutputStream());

					oos.writeObject(new DataBox("충전기", null, new FeeSignal()));

					oos.flush();
					oos.reset();

					Thread.sleep(100);

					oos.close();
					socket.close();

				} catch (Exception e2) {
					// TODO: handle exception
				}


			}else 
				JOptionPane.showMessageDialog(null, "삭제할 요금을 선택하세요.", "Msg", JOptionPane.ERROR_MESSAGE);
		}
	}

	class repricingSet_Gui {//요굼 수정

		class Change_in_bt implements ActionListener{

			boolean cchk() {
				boolean chk= false;

				if(set_list.getRowCount() == 0) 
					return true;

				for (int i = 0; i < set_list.getRowCount(); i++) {

					{
						if(!set_list.getValueAt(i, 0).equals(fee_set.getText())&& !set_list.getValueAt(i, 1).equals( hour_set.getText()+":"+min_box.getSelectedItem())) {

							chk= true;
						}else {
							chk=false;
							break;
						}
					}
				}
				return chk;

			}


			@Override
			public void actionPerformed(ActionEvent e) {
				String cc= ""+model.getValueAt(set_list.getSelectionModel().getLeadSelectionIndex(), 1);
				String bb = ""+model.getValueAt(set_list.getSelectionModel().getLeadSelectionIndex(), 0);

				Pattern p = Pattern.compile("^[0-9]*$");
				Matcher m = p.matcher(fee_set.getText());


				if(cchk()) {
					if(!hour_set.getText().equals("") && !fee_set.getText().equals("")) {
						if(m.find()) {
							if(hour_set.getText().equals("") && fee_set.getText().equals("")) {
								hour_set.setText(cc.split(":")[0]);
								fee_set.setText(bb);
								min_box.setSelectedItem(cc.split(":")[1]);
							}
							m=p.matcher(hour_set.getText());
							if(m.find()) {

								String time2 = hour_set.getText()+":"+min_box.getSelectedItem();////

								int fee2 = Integer.parseInt(fee_set.getText());
								int aa= set_list.getSelectedRow();//선택 열 

								model.setValueAt(fee2, aa,0);
								model.setValueAt(time2, aa,1);

								new New_dbconnect("update fee set price = "+fee2+",time = '"+time2+"' where price="+bb);
								reprice_set.setVisible(false);

								JOptionPane.showMessageDialog(null, "수정이 완료되었습니다.", "Msg", JOptionPane.ERROR_MESSAGE);

								try {

									Thread.sleep(100);

									socket = new Socket("127.0.0.1",7777);
									oos = new ObjectOutputStream(socket.getOutputStream());

									oos.writeObject(new DataBox("충전기", null, new FeeSignal()));

									oos.flush();
									oos.reset();

									Thread.sleep(100);

									oos.close();
									socket.close();

								} catch (Exception e2) {

								}
							}else {
								JOptionPane.showMessageDialog(null, "숫자만 입력 가능합니다.", "Msg", JOptionPane.ERROR_MESSAGE);
							}
						}else {
							JOptionPane.showMessageDialog(null, "숫자만 입력 가능합니다.", "Msg", JOptionPane.ERROR_MESSAGE);
						}
					}else {
						JOptionPane.showMessageDialog(null, "공백을 채워주세요.", "Msg", JOptionPane.ERROR_MESSAGE);
					}
				}else {
					JOptionPane.showMessageDialog(null, "이미 등록된 요금제 입니다.", "Msg", JOptionPane.ERROR_MESSAGE);
				}

			}
		}

		public repricingSet_Gui() {

			reprice_set = new JFrame("요금 수정");
			reprice_set.setBounds(600, 100, 300, 400);
			reprice_set.setResizable(false);
			reprice_set.setLayout(null);

			JLabel jb = new JLabel("요금 수정"); 
			jb.setBounds(20,10, 100, 30);
			reprice_set.add(jb);


			JLabel jb2= new JLabel("시간 수정");
			jb2.setBounds(20, 80, 100, 30);
			reprice_set.add(jb2);

			JLabel hour = new JLabel("시"); 
			hour.setBounds(60,110, 30, 30);
			reprice_set.add(hour);

			JLabel min = new JLabel("분"); 
			min.setBounds(120,110, 30, 30);
			reprice_set.add(min);


			fee_set= new JTextField();
			fee_set.setText(""+model.getValueAt(set_list.getSelectionModel().getLeadSelectionIndex(), 0)); 
			fee_set.setBounds(20, 40, 100, 25);
			fee_set.setBackground(Color.white);
			reprice_set.add(fee_set);

			hour_set= new JTextField();

			String aa= ""+model.getValueAt(set_list.getSelectionModel().getLeadSelectionIndex(), 1);

			hour_set.setText(aa.split(":")[0]);///시간넣기
			hour_set.setBounds(20, 110, 35, 25);
			hour_set.setBackground(Color.white);
			reprice_set.add(hour_set);

			Vector<Integer> remin_set = new Vector<Integer>();
			for (int i = 0; i <= 60; i++) {
				remin_set.add(i);
			}

			min_box = new JComboBox(remin_set);
			min_box.setBounds(80, 110, 40, 25);
			min_box.setSelectedItem(Integer.parseInt(aa.split(":")[1]));

			reprice_set.add(min_box);

			bt= new JButton("완료");
			bt.setBounds(85, 300, 100, 30);

			reprice_set.add(bt);
			bt.addActionListener(new Change_in_bt());

			reprice_set.setVisible(true);
			reprice_set.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}

	}

	public setting_Gui() {
		super("요금제 설정");
		setBounds(200, 100, 400, 500);
		setLayout(null);

		new Main_Table_DB("select * from fee");
		Object [] index= {"요금","시간"};

		model = new DefaultTableModel(index, 0) {

			public boolean isCellEditable (int i, int c) {
				return false;
			}
		};

		//db에서 바로 가져오기 
		for (Main_Table ff : ta_list) {

			Vector in = new Vector();
			in.add(ff.price);
			in.add(ff.time);
			model.addRow(in);
		}

		set_list= new JTable(model);
		JScrollPane set_listSP = new JScrollPane(set_list);
		set_listSP.setBounds(40,50, 300, 300);

		add(set_listSP);

		JButton new_bt = new JButton("등록");
		new_bt.setBounds(25, 400, 100, 30);

		add(new_bt);
		change_bt = new JButton("수정");
		change_bt.setBounds(145, 400, 100, 30);
		add(change_bt);
		JButton del_bt = new JButton("삭제");
		del_bt.setBounds(265, 400, 100, 30);
		add(del_bt);


		new new_bt_act();
		new del_bt_act();
		del_bt.addActionListener(new del_bt_act());
		change_bt.addActionListener(new change_bt_act());
		new_bt.addActionListener(new new_bt_act());

		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	class pricingSet_Gui {//요금 등록

		class settingact implements ActionListener {

			boolean cchk() {
				boolean chk= false;

				if(set_list.getRowCount() == 0) 
					return true;

				for (int i = 0; i < set_list.getRowCount(); i++) {

					{
						if(!set_list.getValueAt(i, 0).equals(fee_set.getText()) && !set_list.getValueAt(i, 1).equals( hour_set.getText()+":"+min_box.getSelectedItem())) {

							chk= true;
						}else {
							chk=false;
							break;
						}
					}
				}
				return chk;

			}
			@Override
			public void actionPerformed(ActionEvent e) {

				Pattern p = Pattern.compile("^[0-9]*$");
				Matcher m = p.matcher(fee_set.getText());
				if (cchk()) {
					if(!fee_set.getText().equals("") && !hour_set.getText().equals("")) {
						if (m.find()) {
							m = p.matcher(hour_set.getText());
							if(m.find()) {
								if(!hour_set.getText().equals("") && fee >= 0) {
									fee = Integer.parseInt(fee_set.getText());
									time = hour_set.getText()+":"+min_box.getSelectedItem();

									model.addRow(new Object[] {fee,time});

									Feeinfodata.add(new FeeInfo(fee, time));

									price_set.setVisible(false);

									//DB로 fee, time 전송
									new New_dbconnect( "insert into fee (price,time) "+ "values ('"+fee+"','"+time+"')");

									JOptionPane.showMessageDialog(null, "등록되었습니다.", "Msg", JOptionPane.OK_OPTION);

									try {

										Thread.sleep(100);

										socket = new Socket("127.0.0.1",7777);
										oos = new ObjectOutputStream(socket.getOutputStream());

										oos.writeObject(new DataBox("충전기", null, new FeeSignal()));

										oos.flush();
										oos.reset();

										Thread.sleep(100);

										oos.close();
										socket.close();

									} catch (Exception e2) {
										// TODO: handle exception
									}



								}

							} else {
								JOptionPane.showMessageDialog(null, "숫자를 입력해주세요.", "Msg", JOptionPane.OK_OPTION);
							}

						}	
						else {
							JOptionPane.showMessageDialog(null, "숫자를 입력해주세요.", "Msg", JOptionPane.OK_OPTION);
						}
					}else {
						JOptionPane.showMessageDialog(null, "등록할 요금 및 시간을 설정해주세요.", "Msg", JOptionPane.OK_OPTION);
					}
				} 
				else {
					JOptionPane.showMessageDialog(null, "이미 등록된 요금제 입니다.", "Msg", JOptionPane.OK_OPTION);

				}

			}
		}

		@Override
		public String toString() {
			return fee+"won"+","+ time;
		}

		public pricingSet_Gui() {//요금설정 등록

			price_set = new JFrame("요금 등록");
			price_set.setBounds(600, 100, 300, 400);
			price_set.setResizable(false);
			price_set.setLayout(null);

			JLabel jb = new JLabel("요금 설정"); 
			jb.setBounds(20,10, 100, 30);
			price_set.add(jb);


			JLabel jb2= new JLabel("시간 설정");
			jb2.setBounds(20, 80, 100, 30);
			price_set.add(jb2);

			JLabel hour = new JLabel("시"); 
			hour.setBounds(60,110, 30, 30);
			price_set.add(hour);

			JLabel min = new JLabel("분"); 
			min.setBounds(120,110, 30, 30);
			price_set.add(min);


			fee_set= new JTextField();
			fee_set.setBounds(20, 40, 100, 25);
			fee_set.setBackground(Color.white);
			price_set.add(fee_set);

			hour_set= new JTextField();
			hour_set.setBounds(20, 110, 35, 25);
			hour_set.setBackground(Color.white);
			price_set.add(hour_set);

			Vector<Integer> min_set = new Vector<Integer>();
			for (int i = 0; i <= 60; i++) {
				min_set.add(i);
			}

			min_box = new JComboBox(min_set);
			min_box.setBounds(80, 110, 40, 25);
			min_box.setSelectedIndex(0); 
			price_set.add(min_box);


			bt= new JButton("완료");
			bt.setBounds(85, 300, 100, 30);
			price_set.add(bt);
			bt.addActionListener(new settingact());


			price_set.setVisible(true);
			price_set.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}
	}
}