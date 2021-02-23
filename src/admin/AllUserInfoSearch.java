package admin;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import data.UserListData;
import db.CustomerDAO;

class UserInfoPrint {
	
	UserListData uld;
	
	Vector<String> userColumn;
    DefaultTableModel model;
    Vector<String> userCol;  //열 Vector
    Vector<Vector> userRow;  //행 Vector
    
    ArrayList<String> userIdArr;
    ArrayList<String> userNameArr;
    
    JTextField jfSearch;
    
    String id;
    
    ViewUserInfo vui;
    
    boolean frameChk;
    
    UserListGUI ul;
    
	public UserInfoPrint(ArrayList<String> userIdArr, ArrayList<String> userNameArr) {

		this.userIdArr = userIdArr;
		this.userNameArr = userNameArr;
		
		userColumn = new Vector<String>();
		
		userRow= new Vector<Vector>();
			
	}
	
	class UserListGUI extends JFrame implements MouseListener, ActionListener {
		
		public UserListGUI() {
			
			setTitle("회원 정보 검색");
			setBounds(600, 50, 500, 500);
			setLayout(null);
			
			JPanel jpTop = new JPanel();
			jpTop.setLayout(null);
			jpTop.setBounds(0, 0, 480, 100);
			
			JLabel jlSearch = new JLabel("아이디 검색");
			jlSearch.setBounds(40, 25, 150, 50);
			jlSearch.setFont(new Font("바탕체", Font.BOLD, 20));
			jpTop.add(jlSearch);
			
			
			jfSearch = new JTextField();
			jfSearch.setBounds(200, 25, 150, 50);
			jfSearch.setFont(new Font("바탕체", Font.PLAIN, 20));
			jpTop.add(jfSearch);
			
			JButton jbSearch = new JButton("검색");
			jbSearch.setBounds(380, 25, 85, 50);
			jbSearch.setFont(new Font("바탕체", Font.BOLD, 20));
			
			jbSearch.addActionListener(this);
			
			jpTop.add(jbSearch);
			
			add(jpTop);
			
			JPanel jpBot = new JPanel();
			jpBot.setLayout(null);
			jpBot.setBounds(0, 100, 480, 350);
			jpBot.setBackground(Color.yellow);
			
			userColumn.addElement("ID");
		    userColumn.addElement("성명");
		    
		    model = new DefaultTableModel(userColumn,0) {
		    	
		    	public boolean isCellEditable(int i, int c){
		            return false;
		    	}
		    	
		    }; 
		    			
			JTable UserInfoTable = new JTable(model);
			
			for (int i = 0; i < userIdArr.size(); i++) {
				userCol = new Vector<String>();
				userCol.addElement(userIdArr.get(i));
				userCol.addElement(userNameArr.get(i));
				userRow.addElement(userCol);
			}
			
			Iterator<Vector> it = userRow.iterator();
			
			while (it.hasNext()) {
				
				model.addRow(it.next());

			}
			
			JScrollPane jsp = new JScrollPane(UserInfoTable);
			jsp.setBounds(0, 0, 480, 350);
			jpBot.add(jsp);
			
			UserInfoTable.addMouseListener(this);
			
			add(jpBot);
			
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setVisible(true);
			
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			
			JTable jt = (JTable)e.getSource();
		
			id = (String) jt.getModel().getValueAt(jt.getSelectedRow(), 0);
						
			if(!frameChk) {
				vui = new ViewUserInfo(new CustomerDAO().detail(id));
				vui.uig = vui.new UserInfoGUI();
				frameChk = true;
			}
			else {
				if(vui.uig != null)
					vui.uig.dispatchEvent(new WindowEvent(vui.uig, WindowEvent.WINDOW_CLOSING));
				vui = new ViewUserInfo(new CustomerDAO().detail(id));
				vui.uig = vui.new UserInfoGUI();
			}
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			
			id = jfSearch.getText();
			
			if(!id.equals("")) {
				
				if(!frameChk) {
					vui = new ViewUserInfo(new CustomerDAO().detail(id));
					if(vui.uld != null) {
						vui.uig = vui.new UserInfoGUI();
						frameChk = true;
					}else {
						JOptionPane.showMessageDialog(this, "존재하지 않는 회원입니다.");
						jfSearch.setText("");
						
					}
						
				}
				else {
					if(vui.uig != null)
						vui.uig.dispatchEvent(new WindowEvent(vui.uig, WindowEvent.WINDOW_CLOSING));
					
					vui = new ViewUserInfo(new CustomerDAO().detail(id));
					
					if(vui.uld != null) 	
						vui.uig = vui.new UserInfoGUI();
					else {
						JOptionPane.showMessageDialog(this, "존재하지 않는 회원입니다.");
						jfSearch.setText("");
					}
						
				}
			
			}else
				JOptionPane.showMessageDialog(this, "아이디를 입력하세요.");
			
		}
		
	}

}

public class AllUserInfoSearch {
	
	UserInfoPrint uip;
	
	public AllUserInfoSearch() {
		
		ArrayList<String> userIdArr = new ArrayList<String>();
		ArrayList<String> userNameArr = new ArrayList<String>();
		
		try {

			ArrayList<UserListData> list = new CustomerDAO().list();

			for (UserListData uld : list) {
				userIdArr.add(uld.getId());
				userNameArr.add(uld.getName());
			}

		} catch (Exception e1) {
			// TODO: handle exception
		}
		
		uip = new UserInfoPrint(userIdArr, userNameArr);
		
	}
		
}
