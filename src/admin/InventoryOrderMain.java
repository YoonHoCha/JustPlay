package admin;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import data.InventoryOrderInfo;
import data.InventoryOrderInfo2;
import db.InvenOrderDB;
import db.StaticIP;




class DBCheck {
	ArrayList<Integer>arr;

	public DBCheck() {
		arr = new ArrayList<Integer>();

		try {
			//1.JDBS ����̹� ����
			Class.forName("oracle.jdbc.driver.OracleDriver");

			//2.�����ͺ��̽� Ŀ�ټ� �õ�
			Connection con = DriverManager.getConnection(
					"jdbc:oracle:thin:@"+StaticIP.ip+":1521:xe",
					"hr",
					"hr");

			//3.�������� �����ϱ����� ��ü����
			Statement stmt = con.createStatement();

			//4.��������
			ResultSet rs = stmt.executeQuery("select * from stock");

			//5.���� ���� ��� ���
			while(rs.next()) {
				int ss;
				ss = Integer.parseInt(rs.getString("serial"));
				//				Integer.parseInt(ss);
				arr.add(ss);
				//				
			}

			Collections.sort(arr);
			Collections.reverse(arr);


			//6.������ ���ఴü ����
			rs.close();
			stmt.close();

			//7.�����ͺ��̽� Ŀ�ټ� ����
			con.close();


		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		//		System.out.println(new InvenDB().arr);
	}
}




class insert {

	ArrayList<InventoryOrderInfo> arrLi = new ArrayList<InventoryOrderInfo>();

	public insert(String sql) {

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			//1. JDBC ����̹� �ε�  -- ���� ����
			//Class.forName("oracle.jdbc.driver.OracleDriver");

			//2. �����ͺ��̽� Ŀ�ؼ� �õ�   
			Connection con = DriverManager.getConnection(
					"jdbc:oracle:thin:@"+StaticIP.ip+":1521:xe", 
					"hr", 
					"hr");

			//3. �������� �����ϱ����� ��ü����
			Statement stmt = con.createStatement();


			int cnt = stmt.executeUpdate(sql);

			//6. ������ ���ఴü ����
			//rs.close();
			stmt.close();

			//7. �����ͺ��̽� Ŀ�ؼ� ����
			con.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

//
public class InventoryOrderMain extends JFrame implements ActionListener{
	InventoryOrderInfo itoi;
	ArrayList<InventoryOrderInfo> arrLi = new ArrayList<InventoryOrderInfo>();
	ArrayList<InventoryOrderInfo2> arrLi2 = new InvenOrderDB().arr;
	JTextField tf1,tf2,tf3,tf4;
	JComboBox ramenBox,riceBox,beverageBox,snackBox;
	JLabel kind1,kind2,kind3,kind4;
	JTable jt;
	DBCheck dc;
	InvenOrderDB iodb;
	


	public InventoryOrderMain() {
		dc = new DBCheck();
		iodb = new InvenOrderDB();
		Object [] index = {"��ǰ ��","����","�߰� ����"};
		DefaultTableModel dtm = new DefaultTableModel(index,0);

		class MercAdd implements ActionListener{

			boolean chk  = true;

			JTextField text;
			JComboBox box;
			JLabel label;

			public MercAdd(JLabel label,JComboBox box,JTextField text) {
				super();
				this.text = text;
				this.box = box;
				this.label = label;
			}

			//�߰���ư ������
			@Override
			public void actionPerformed(ActionEvent e) {


				Pattern p = Pattern.compile("^[0-9]*$");
		        Matcher m = p.matcher(text.getText());
		        
		           if(!text.getText().isEmpty()) {
		              if (m.find()) {
		            	  
		                	Vector invenList = new Vector();
							//�� = ī�װ� / �޴��̸� = �ڽ� / �ؽ�Ʈ = �ø���
							arrLi.add(new InventoryOrderInfo( box.getSelectedItem()+"",label.getText(),text.getText()));
		
							invenList.add(box.getSelectedItem()+"");
							invenList.add(label.getText());
							invenList.add(text.getText());
		
							dtm.addRow(invenList);

		                    }else {
		                    JOptionPane.showMessageDialog(null, "���ڸ� �Է����ּ���.", "Msg", JOptionPane.OK_OPTION);
		                 }

		           }else
		        	   JOptionPane.showMessageDialog(null, "�԰��� ������ �����ּ���.", "Msg", JOptionPane.OK_OPTION);

			}

		}



		setBounds(0, 0,1000, 700);
		//		setBackground(Color.white);
		setLayout(null);

		JPanel jp1 = new JPanel(); //�ֹ� �߰��Ǵ� �г�
		jp1.setBounds(500, 0,500, 700);
		jp1.setLayout(null);
		jp1.setBackground(Color.white);
		add(jp1);

		JButton orderBtn = new JButton("��� �԰�");
		orderBtn.setBounds(280,540,130,70);
		jp1.add(orderBtn);
		orderBtn.addActionListener(this);
		
		
		JButton deletBtn = new JButton("���û�ǰ ����");
		deletBtn.setBounds(90,540,130,70);
		jp1.add(deletBtn);
		deletBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				int tsr = jt.getSelectedRow();
				if(tsr != -1) {
					dtm.removeRow(jt.getSelectionModel().getLeadSelectionIndex());
					
					for (InventoryOrderInfo ii : arrLi) {
						if(dtm.getValueAt(jt.getSelectionModel().getLeadSelectionIndex(), 0).equals(ii.mercName) && 
								dtm.getValueAt(jt.getSelectionModel().getLeadSelectionIndex(), 2).equals(ii.mercCnt)) {
							arrLi.remove(ii);
							break;
						}
					}
				}else {
					JOptionPane.showMessageDialog(null, "���� �� ��ǰ�� ������ �ּ���");
				}
			}
		});

		jt = new JTable(dtm) {
			public boolean isCellEditable(int i, int c) {
				return false;
			}
		};
		JScrollPane jsc = new JScrollPane(jt);
		jsc.setBounds(50, 0, 400, 500);

		jp1.add(jsc);


		//////////////////////////////////////////////////////////////////////
		JLabel kind1 = new JLabel("���");
		kind1.setBounds(20, 70,150, 30);
		add(kind1);

		JLabel cate1 = new JLabel("����");
		cate1.setBounds(190, 70,150, 30);
		add(cate1);

		Vector<String> ramen = new Vector<String>();
		for (InventoryOrderInfo2 ordLi : arrLi2) {
			if(ordLi.kind.equals("���")) {

				ramen.add(ordLi.name);
			}
		}

		ramenBox = new JComboBox(ramen);
		ramenBox.setBounds(20, 100,150, 30);
		ramenBox.setSelectedItem("�Ŷ��");
		ramenBox.setOpaque(true);
		add(ramenBox);

		JLabel kind2 = new JLabel("����");
		kind2.setBounds(20, 170,150, 30);
		add(kind2);

		Vector<String> beverage = new Vector<String>();

		for (InventoryOrderInfo2 ordLi : arrLi2) {
			if(ordLi.kind.equals("����")) {

				beverage.add(ordLi.name);
			}
		}


		beverageBox = new JComboBox(beverage);
		beverageBox.setBounds(20, 200,150, 30);
		beverageBox.setSelectedItem("�󶼴¸��̾�");
		beverageBox.setOpaque(true);
		add(beverageBox);

		JLabel kind3 = new JLabel("����");
		kind3.setBounds(20, 270,150, 30);
		add(kind3);

		Vector<String> rice = new Vector<String>();
		for (InventoryOrderInfo2 ordLi : arrLi2) {
			if(ordLi.kind.equals("����")) {

				rice.add(ordLi.name);
			}
		}

		riceBox = new JComboBox(rice);
		riceBox.setBounds(20, 300,150, 30);
		riceBox.setSelectedItem("ȣ���󵤹�");
		riceBox.setOpaque(true);
		add(riceBox);

		JLabel kind4 = new JLabel("����");
		kind4.setBounds(20, 370,150, 30);
		add(kind4);

		Vector<String> snack = new Vector<String>();
		for (InventoryOrderInfo2 ordLi : arrLi2) {
			if(ordLi.kind.equals("����")) {

				snack.add(ordLi.name);
			}
		}
		
		snackBox = new JComboBox(snack);
		snackBox.setBounds(20, 400,150, 30);
		snackBox.setSelectedItem("��Ƽ����");
		snackBox.setOpaque(true);
		add(snackBox);



		tf1 = new JTextField();
		tf1.setBounds(190, 100,100, 30);
		add(tf1);

		tf2 = new JTextField();
		tf2.setBounds(190, 200,100, 30);
		add(tf2);

		tf3 = new JTextField();
		tf3.setBounds(190, 300,100, 30);
		add(tf3);

		tf4 = new JTextField();
		tf4.setBounds(190, 400,100, 30);
		add(tf4);


		JButton btn1 = new JButton("�߰�");
		btn1.setBounds(300, 100,100, 30);
		add(btn1);
		btn1.addActionListener(new MercAdd(kind1, ramenBox, tf1));

		JButton btn2 = new JButton("�߰�");
		btn2.setBounds(300, 200,100, 30);
		btn2.addActionListener(new MercAdd(kind2, beverageBox, tf2));
		add(btn2);

		JButton btn3 = new JButton("�߰�");
		btn3.setBounds(300, 300,100, 30);
		btn3.addActionListener(new MercAdd(kind3, riceBox, tf3));
		add(btn3);

		JButton btn4 = new JButton("�߰�");
		btn4.setBounds(300, 400,100, 30);
		btn4.addActionListener(new MercAdd(kind4, snackBox, tf4));
		add(btn4);

		setVisible(true);
		

	}	

	//�ֹ���ư ������
	@Override
	public void actionPerformed(ActionEvent e) {

		for (int i = 0; i < arrLi.size(); i++) {

			int num = new DBCheck().arr.get(0)+1;
			for (int j = num; j < (num)+Integer.parseInt(arrLi.get(i).mercCnt) ; j++) {

				new insert("insert into stock (serial,menu,category) values ('"+j+"','"+arrLi.get(i).mercName+"','"+arrLi.get(i).mercCate+"')");

			}
			Collections.sort(dc.arr);
			Collections.reverse(dc.arr);
		}

		new DBCheck();

		if(arrLi.size()==0) {

			JOptionPane.showMessageDialog(null, "�ֹ� �� ��ǰ�� �����ϴ�.");

		}else {

			dispose();
			JOptionPane.showMessageDialog(null, "��� �ֹ��� �Ϸ� �Ǿ����ϴ�.");
		}

		//			System.out.println(dc.arr);
	}

}
