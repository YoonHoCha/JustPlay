package admin;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.Thread.State;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import db.StaticIP;


class ItemDB_Conn {


	ArrayList<ProductDataInFo> proDb = new ArrayList<ProductDataInFo>();
	Connection con;
	Statement stmt;
	ResultSet rs;


	public ItemDB_Conn(String sql) {
		// TODO Auto-generated constructor stub

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection(
					"jdbc:oracle:thin:@"+StaticIP.ip+":1521:xe",
					"hr",
					"hr");

			stmt = con.createStatement();

			rs = stmt.executeQuery(sql);

			while(rs.next()) {

				ProductDataInFo info = new ProductDataInFo(rs.getString("menu"),rs.getString("category"),rs.getInt("price"),
						rs.getString("image"),rs.getString("serial"));

				proDb.add(info);

			}


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			close();
		}



	}



	public ArrayList<ProductDataInFo> getSaleDb() {
		return proDb;
	}



	public void close() {
		if(rs!=null) try {rs.close();} catch (SQLException e) {}
		if(stmt!=null) try {stmt.close();} catch (SQLException e) {}
		if(con!=null) try {con.close();} catch (SQLException e) {}
	}


}


class ProductDB_Conn {



	public ProductDB_Conn(String sql) {
		// TODO Auto-generated constructor stub

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection(
					"jdbc:oracle:thin:@"+StaticIP.ip+":1521:xe",
					"hr",
					"hr");

			Statement stmt = con.createStatement();

			int cnt = stmt.executeUpdate(sql);


			con.close();
			stmt.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

}

class ProductDataInFo{

	public String proName;
	String proKind;
	public int proPri;
	String imgPath;
	String serial;

	public ProductDataInFo(String proName, String proKind, int proPri, String imgPath,String serial) {
		super();
		this.proName = proName;
		this.proKind = proKind;
		this.proPri = proPri;
		this.imgPath = imgPath;
		this.serial = serial;
	}



	@Override
	public String toString() {
		return proName+","+proKind+","+proPri+","+imgPath+","+serial;
	}

}


class Product_ extends JFrame{

	DefaultTableModel model;	////상품리스트 표
	ImageIcon img;		///상품 이미지 등록 아이콘
	JLabel jl4;			///상품 이미지 배경
	Vector data;		

	ArrayList<ProductDataInFo> al = new ItemDB_Conn("select * from item").getSaleDb();
	ArrayList totData = new ArrayList();	///상품 정보를 가지고 있음
	String imgPath;	//이미지 주소

	String proName;	//상품등록 이름
	String proKind;	//상품 분류
	int proPri;	//상품가격
	String serial;

	boolean proPro;
	boolean rePro;

	Product_Register pr ;
	ReProduct rp;

	class Product_Register extends JFrame{	

		JTextField name_gui;	///상품등록 상품명 gui
		JTextField price_gui;	///상품등록 가격 gui
		JComboBox<String> box;	/////분류 박스 gui
		JTextField serial_gui;


		class Img_Register implements ActionListener{//이미지 등록 Act

			@Override
			public void actionPerformed(ActionEvent e) {

				//// 파일 부르기
				FileDialog fd = new FileDialog(Product_Register.this, "이미지 파일 열기",FileDialog.LOAD);
				fd.setDirectory("C:\\Users\\admin\\Pictures");				
				fd.setVisible(true);

				/////이미지 파일 경로
				imgPath = fd.getDirectory()+fd.getFile();
				img = new ImageIcon(imgPath);


				jl4.setIcon(img);

			}
		}

		@Override
		public String toString() {
			return  proName + "," + proKind + "," + proPri ;
		}

		class RegisterBntAct implements ActionListener{

			boolean nameChk() {
				boolean chk = false;

				if(al.size() == 0)
					return true;

				for (int i = 0; i < data_gui.getRowCount(); i++) {

					if(!data_gui.getValueAt(i, 0).equals(name_gui.getText())&&!data_gui.getValueAt(i,4).equals(serial_gui.getText())) {

						chk = true;
					}else {

						chk = false;
						break;
					}
				}
				return chk;
			}



			@Override
			public void actionPerformed(ActionEvent e) {	///상품정보를 표에 담음


				Pattern pp = Pattern.compile("^[0-9]*$");
				Matcher mm = pp.matcher(price_gui.getText());

				if(name_gui.getText().equals("") || price_gui.getText().equals("")
						|| serial_gui.getText().equals("") || imgPath==null) {

					JOptionPane.showMessageDialog(null, "정보를 입력하세요", "Message", JOptionPane.ERROR_MESSAGE);

				}else {

					if(nameChk()) {

						if(mm.find()) {
							if(price_gui.getText().isEmpty()) {
								proPri = 0;

							}else {

								pp = Pattern.compile("^[a-zA-Z]*$");
								mm = pp.matcher(serial_gui.getText());

								if(mm.find()) {
									proName = name_gui.getText();
									proKind = (String)box.getSelectedItem();	
									serial = serial_gui.getText();
									proPri = Integer.parseInt(price_gui.getText());

									new ProductDB_Conn("insert into item (menu,category,price,image,serial) "+"values ('"
											+proName+"','"+proKind+"','"+proPri+"','"+imgPath+"','"+serial+"')");

									Vector vv= new Vector();

									vv.add(proName);
									vv.add(proKind);
									vv.add(proPri);
									vv.add(imgPath);
									vv.add(serial);

									model.addRow(vv);

									totData.add(new ProductDataInFo(proName, proKind, proPri,imgPath,serial));

								}else {
									JOptionPane.showMessageDialog(null, "분류코드를 영문으로 작성해주세요", "Message", JOptionPane.ERROR_MESSAGE);
								}

							}

						}else {
							JOptionPane.showMessageDialog(null, "가격을 숫자로 작성해주세요", "Message", JOptionPane.ERROR_MESSAGE);
						}

					}else {
						JOptionPane.showMessageDialog(null, "등록된 상품입니다", "Message", JOptionPane.ERROR_MESSAGE);
					}

				}
				//Product_Register.this.setVisible(false);

			}
		}

		public Product_Register() {	
			////상품등록 gui
			// TODO Auto-generated constructor stub
			super("상품등록");


			setBounds(500, 100, 530, 600);
			setLayout(null);

			JLabel jl1 = new JLabel("상품명 :");
			jl1.setBounds(50, 30, 80, 30);
			add(jl1);

			name_gui = new JTextField();
			name_gui.setBounds(120, 30, 100, 30);
			add(name_gui);

			Vector<String> kind = new Vector<String>();
			kind.add("라면");
			kind.add("과자");
			kind.add("덮밥");
			kind.add("음료");
			;
			JLabel jl2 = new JLabel("분류 :");
			jl2.setBounds(50, 90, 80, 30);
			add(jl2);

			box = new JComboBox<String>(kind);
			box.setBounds(120, 90, 80, 30);
			add(box);			

			JLabel jl3 = new JLabel("가격 :");
			jl3.setBounds(50, 150, 80, 30);
			add(jl3);

			price_gui = new JTextField();
			price_gui.setBounds(120, 150, 100, 30);
			add(price_gui);

			JButton imgReg = new JButton("이미지 등록");
			imgReg.setBounds(120, 260, 120, 30);
			add(imgReg);

			imgReg.addActionListener(new Img_Register());

			jl4 = new JLabel();
			jl4.setBounds(70, 310, 200, 200);
			jl4.setLayout(null);
			jl4.setOpaque(true);
			add(jl4);

			JLabel jl5 = new JLabel("분류코드 :");
			jl5.setBounds(40, 210, 100, 30);
			add(jl5);

			serial_gui = new JTextField();
			serial_gui.setBounds(120, 210, 100, 30);
			add(serial_gui);



			JButton proBnt = new JButton("등록");
			proBnt.setBounds(330, 480, 100, 30);
			proBnt.setOpaque(true);
			proBnt.addActionListener(new RegisterBntAct());
			add(proBnt);

			setVisible(true);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);



		}
	}


	class Register_Bnt implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			////상품관리 상품등록버튼

			if(!proPro) {
				pr = new Product_Register();
				proPro = true;

			}else {

				pr.dispatchEvent(new WindowEvent(pr, WindowEvent.WINDOW_CLOSING));
				pr = new Product_Register();
			}

		}


	}

	JTable data_gui;
	boolean chk = true;

	class ChangePro implements ActionListener{	
		///// 상품관리 수정버튼

		@Override
		public void actionPerformed(ActionEvent e) {
			int num = data_gui.getSelectedRow();

			if(num!=-1) {
				if(!rePro) {
					rp = new ReProduct();
					rePro = true;

				}else {
					rp.dispatchEvent(new WindowEvent(rp, WindowEvent.WINDOW_CLOSING));
					rp = new ReProduct();
				}

			}else {
				JOptionPane.showMessageDialog(null, "수정할 상품을 선택하세요", "Message", JOptionPane.ERROR_MESSAGE);
			}

		}

	}

	class ReProduct extends JFrame {

		JTextField reName_gui;
		JTextField rePri_gui;
		JComboBox<String> reKind_gui;
		JLabel rej;
		JTextField reSerial_gui;

		ImageIcon reImg;
		String reName;
		int rePri;
		String reKind;
		String reImgPath;
		String reSerial;


		class ReAct implements ActionListener{
			int num = data_gui.getSelectionModel().getLeadSelectionIndex();

			boolean nameChk() {
				boolean chk = false;

				if(data_gui.getRowCount() == 0)
					return true;



				for (int i = 0; i < data_gui.getRowCount(); i++) {

					if(!data_gui.getValueAt(i, 0).equals(reName_gui.getText())&&!data_gui.getValueAt(i, 4).equals(reSerial_gui.getText())) {

						chk = true;
					}else if(reName_gui.getText().equals((String)model.getValueAt(num, 0))
							&&reSerial_gui.getText().equals((String)model.getValueAt(num, 4))){

						chk = true;
					}else {
						if(reName_gui.getText().equals(data_gui.getValueAt(num, 0))&&reSerial_gui.getText().equals(data_gui.getValueAt(num, 4)))
							return true;
						chk = false;
						break;
					}
				}
				return chk;
			}

			//수정 이후 수정완료버튼
			@Override
			public void actionPerformed(ActionEvent e) {	

				int num = data_gui.getSelectionModel().getLeadSelectionIndex();


				Pattern pp = Pattern.compile("^[0-9]*$");
				Matcher mm = pp.matcher(rePri_gui.getText());
				//	reKind_gui.setSelectedItem(proKind);


				if(reName_gui.getText().isEmpty()) {
					reName_gui.setText((String)model.getValueAt(num, 0));
				}
				if(rePri_gui.getText().isEmpty()) {
					rePri_gui.setText((String)model.getValueAt(num, 2));

				}
				if(reSerial_gui.getText().isEmpty()) {
					reSerial_gui.setText((String)model.getValueAt(num, 4));

				}
				if(reImgPath==null||reImgPath.equals("")) {
					reImgPath = (String)model.getValueAt(num, 3);

				}

				if(nameChk()) {



					if(mm.find()) {
						if(rePri_gui.getText().isEmpty()) {
							proPri = 0;

						}else {

							pp = Pattern.compile("^[a-zA-Z]*$");
							mm = pp.matcher(reSerial_gui.getText());

							if(mm.find()) {
								reName = reName_gui.getText();
								reKind = (String)reKind_gui.getSelectedItem();	
								rePri = Integer.parseInt(rePri_gui.getText());
								reSerial = reSerial_gui.getText();


								String delName = (String)model.getValueAt(num, 0);

								new ProductDB_Conn("update item set menu = '"+reName+"',"
										+ " category = '"+reKind+"',price = "+rePri+", image = '"+reImgPath+"' "
										+ ", serial = '"+reSerial+"' "+"where menu = '"+delName+"'");


								model.setValueAt(reName, num, 0);
								model.setValueAt(reKind, num, 1);
								model.setValueAt(rePri, num, 2);
								model.setValueAt(reImgPath, num, 3);
								model.setValueAt(reSerial, num, 4);

								totData.add(new ProductDataInFo(proName, proKind, proPri,imgPath,serial));

							}else {
								JOptionPane.showMessageDialog(null, "분류코드를 영문으로 작성해주세요", "Message", JOptionPane.ERROR_MESSAGE);
							}

						}

					}else {
						JOptionPane.showMessageDialog(null, "가격을 숫자로 작성해주세요", "Message", JOptionPane.ERROR_MESSAGE);
					}

				}else {
					JOptionPane.showMessageDialog(null, "등록된 상품입니다", "Message", JOptionPane.ERROR_MESSAGE);
				}


				setVisible(false);			


			}
		}


		class ReImgAct implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {

				//// 파일 부르기
				FileDialog fd = new FileDialog(ReProduct.this, "이미지 파일 열기",FileDialog.LOAD);
				fd.setDirectory("C:\\Users\\admin\\Pictures");				
				fd.setVisible(true);


				/////이미지 파일 경로
				reImgPath = fd.getDirectory()+fd.getFile();
				reImg = new ImageIcon(reImgPath);
				rej.setIcon(reImg);


			}
		}


		//// 상품수정 gui
		public ReProduct() {
			super("상품수정");
			setBounds(500, 100, 530, 600);
			setLayout(null);

			int num = data_gui.getSelectionModel().getLeadSelectionIndex();

			JLabel jl1 = new JLabel("상품명 :");
			jl1.setBounds(50, 30, 80, 30);
			add(jl1);

			reName_gui = new JTextField((String)model.getValueAt(num, 0));
			reName_gui.setBounds(120, 30, 100, 30);
			add(reName_gui);


			Vector<String> kind = new Vector<String>();
			kind.add("라면");
			kind.add("과자");
			kind.add("덮밥");
			kind.add("음료");

			JLabel jl2 = new JLabel("분류 :");
			jl2.setBounds(50, 90, 80, 30);
			add(jl2);

			reKind_gui = new JComboBox<String>(kind);
			reKind_gui.setBounds(120, 90, 80, 30);
			add(reKind_gui);			

			reKind_gui.setSelectedItem((String)model.getValueAt(num, 1));


			JLabel jl3 = new JLabel("가격 :");
			jl3.setBounds(50, 150, 80, 30);
			add(jl3);

			rePri_gui = new JTextField(model.getValueAt(num, 2)+"");
			rePri_gui.setBounds(120, 150, 100, 30);
			add(rePri_gui);
			//	rePri_gui.setText((String)model.getValueAt(num, 2));


			JLabel jl5 = new JLabel("분류코드 :");
			jl5.setBounds(50, 210, 100, 30);
			add(jl5);

			reSerial_gui = new JTextField((String)model.getValueAt(num, 4));
			reSerial_gui.setBounds(120, 210, 100, 30);
			add(reSerial_gui);
			//	reSerial_gui.setText((String)model.getValueAt(num, 4));


			rej = new JLabel();
			rej.setBounds(70, 310, 200, 200);
			rej.setLayout(null);
			rej.setOpaque(true);
			add(rej);

			JButton reImg = new JButton("이미지 등록");
			reImg.setBounds(100, 260, 130, 30);
			img = new ImageIcon((String)model.getValueAt(num, 3));
			rej.setIcon(img);
			reImg.addActionListener(new ReImgAct());
			add(reImg);


			JButton ReBnt = new JButton("수정");
			ReBnt.setBounds(330, 480, 100, 30);
			ReBnt.addActionListener(new ReAct());
			add(ReBnt);




			setVisible(true);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		}

	}

	class ProRemove implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {

			int cl = data_gui.getSelectedRow();
			//	int num = data_gui.getSelectionModel().getLeadSelectionIndex();

			if(cl!=-1) {

				String delName = (String)model.getValueAt(data_gui.getSelectionModel().getLeadSelectionIndex(), 0);
				new ProductDB_Conn("delete from item where menu='"+delName+"'");
				model.removeRow(cl);

			}else {
				JOptionPane.showMessageDialog(null, "삭제할 상품을 선택하세요", "Message", JOptionPane.ERROR_MESSAGE);

			}

		}
	}

	public Product_() {
		// TODO Auto-generated constructor stub

		super("상품관리");
		setBounds(300, 100, 500, 700);
		setLayout(null);

		String [] index = {"상품명","분류","가격","이미지 경로","분류번호"};

		model = new DefaultTableModel(index, 0) {
			public boolean isCellEditable(int i, int c) {
				return false;
			}
		};

		for (ProductDataInFo aa : al) {

			data = new Vector();
			data.add(aa.proName);
			data.add(aa.proKind);
			data.add(aa.proPri);
			data.add(aa.imgPath);
			data.add(aa.serial);

			model.addRow(data);
		}


		data_gui = new JTable(model);


		JScrollPane sp = new JScrollPane(data_gui);
		sp.setBounds(50, 50, 400, 500);
		add(sp);

		JButton regBnt = new JButton("등록");
		regBnt.setBounds(50, 600, 100, 30);
		regBnt.addActionListener(new Register_Bnt());
		add(regBnt);

		JButton ee = new JButton("수정");
		ee.setBounds(200, 600, 100, 30);
		ee.addActionListener(new ChangePro());
		add(ee);

		JButton deBnt = new JButton("삭제");
		deBnt.setBounds(350, 600, 100, 30);
		deBnt.addActionListener(new ProRemove());
		add(deBnt);

		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

	}

}