package user;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import data.UserListData;
import db.CustomerDAO;

public class IdAndPwSearch {
	
	private CardLayout card;
	
	private IdSearchGUI ig;
	private PwSearchGUI pg;
	
	private JPanel contentPanel;
	
	private UserListData uld;
	private UserListData uldSub;
	IdAndPwSearchGUI se;
	public IdAndPwSearch() {
		
		ig = new IdSearchGUI();
		pg = new PwSearchGUI();
		
		card = new CardLayout();
		
		se = new IdAndPwSearchGUI();
		
	}
	
	class IdAndPwSearchGUI extends JFrame implements ActionListener {
		
		public IdAndPwSearchGUI() {
			
			setTitle("ID/PW 찾기");
			
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setBounds(100, 100, 450, 400);
			setLayout(null);
			
			JPanel Btnpanel = new JPanel();
			Btnpanel.setBounds(0, 0, 432, 50);
			add(Btnpanel);
			Btnpanel.setLayout(null);
			
			JButton btnIdSearch = new JButton("ID 찾기");
			btnIdSearch.setBounds(93, 12, 105, 27);
			btnIdSearch.setBackground(Color.gray);
			btnIdSearch.setForeground(Color.white);
			btnIdSearch.addActionListener(this);
			Btnpanel.add(btnIdSearch);
			
			JButton btnPwSearch = new JButton("PW 찾기");
			btnPwSearch.setBounds(228, 12, 105, 27);
			btnPwSearch.setBackground(Color.gray);
			btnPwSearch.setForeground(Color.white);
			btnPwSearch.addActionListener(this);
			Btnpanel.add(btnPwSearch);
				
			contentPanel = new JPanel();
			contentPanel.setBounds(0, 47, 432, 306);
			
			contentPanel.setLayout(card);
			
			card.addLayoutComponent(ig, "ID 찾기");
			card.addLayoutComponent(pg, "PW 찾기");
			
			contentPanel.add(ig, "ID 찾기");
			contentPanel.add(pg, "PW 찾기");
			
			card.show(contentPanel,"ID 찾기");
			
			add(contentPanel);
			
			setVisible(true);
			
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			
			JButton jb = (JButton)e.getSource();
			
			if(jb.getText().equals("ID 찾기")) {
				card.show(contentPanel, "ID 찾기");
			}else {
				card.show(contentPanel, "PW 찾기");
			}
		}
	}
	
	class IdSearchGUI extends JPanel implements ActionListener {
		
		private JTextField nameFiled;
		private JComboBox<String> num1;
		private JTextField num2;
		private JTextField num3;
		private JButton btnSearch;
		
		private String name;
		private String tel;
		
		public IdSearchGUI() {
				
			setLayout(null);
			setBounds(5, 5, 400, 290);
			
			JLabel contentLabel = new JLabel("ID 찾기");
			contentLabel.setBounds(30, 30, 150, 45);
			contentLabel.setFont(new Font("돋움", Font.PLAIN, 20));
			
			add(contentLabel);
			
			JLabel nameLabel = new JLabel("이름");
			nameLabel.setBounds(70, 100, 150, 30);
			nameLabel.setFont(new Font("돋움", Font.PLAIN, 15));
			add(nameLabel);
			
			nameFiled = new JTextField();
			nameFiled.setBounds(140, 100, 100, 30);
			nameFiled.setFont(new Font("돋움", Font.PLAIN, 15));
			add(nameFiled);
			
			JLabel telLabel = new JLabel("전화번호");
			telLabel.setBounds(60, 150, 150, 30);
			telLabel.setFont(new Font("돋움", Font.PLAIN, 15));
			add(telLabel);
			
			String [] tele = {"010","011","012","013","014","015","016","017","018","019"};
			num1 = new JComboBox<String>(tele);
			num1.setBounds(140, 150, 80, 30);
			num1.setSelectedIndex(0);
			add(num1);

			JLabel aa = new JLabel("-");
			aa.setBounds(230, 150, 10, 30);
			add(aa);

			num2 = new JTextField();
			num2.setBounds(240, 150, 80, 30);
			add(num2);

			JLabel bb = new JLabel("-");
			bb.setBounds(330, 150, 10, 30);
			add(bb);

			num3 = new JTextField();
			num3.setBounds(340, 150, 80, 30);
			add(num3);
			
			btnSearch = new JButton("확인");
			btnSearch.setBounds(180, 260, 70, 30);
			btnSearch.addActionListener(this);
			add(btnSearch);
			
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			
			name = nameFiled.getText();
			tel = num1.getSelectedItem()+"-"+num2.getText()+"-"+num3.getText();
			
			uld = new CustomerDAO().idSearch(name, tel);
			
			if(!name.equals("")) {
				
				if(!num2.getText().equals("") && !num3.getText().equals("")) {
					if(uld == null)
						JOptionPane.showMessageDialog(this, "회원 정보에 일치하는 ID가 없습니다.");
					else
						JOptionPane.showMessageDialog(this, uld.getId());
				}else
					JOptionPane.showMessageDialog(this, "전화번호를 정확히 입력하세요.");
						
			}else
				JOptionPane.showMessageDialog(this, "이름을 입력해 주세요.");		
		}
		
	}
	
	class PwSearchGUI extends JPanel implements ActionListener {
		
		private JTextField idFiled;
		private JTextField nameFiled;
		private JComboBox<String> num1;
		private JTextField num2;
		private JTextField num3;
		private JButton btnSearch;
		
		private String id;
		private String name;
		private String tel;
		
		public PwSearchGUI() {
			
			setLayout(null);
			setBounds(5, 5, 400, 290);
			
			JLabel contentLabel = new JLabel("PW 찾기");
			contentLabel.setBounds(30, 30, 150, 45);
			contentLabel.setFont(new Font("돋움", Font.PLAIN, 20));
			
			add(contentLabel);
			
			JLabel idLabel = new JLabel("ID");
			idLabel.setBounds(80, 100, 150, 30);
			idLabel.setFont(new Font("돋움", Font.PLAIN, 15));
			add(idLabel);
			
			idFiled = new JTextField();
			idFiled.setBounds(140, 100, 130, 30);
			idFiled.setFont(new Font("돋움", Font.PLAIN, 15));
			add(idFiled);
			
			JLabel nameLabel = new JLabel("이름");
			nameLabel.setBounds(70, 150, 150, 30);
			nameLabel.setFont(new Font("돋움", Font.PLAIN, 15));
			add(nameLabel);
			
			nameFiled = new JTextField();
			nameFiled.setBounds(140, 150, 130, 30);
			nameFiled.setFont(new Font("돋움", Font.PLAIN, 15));
			add(nameFiled);
			
			JLabel telLabel = new JLabel("전화번호");
			telLabel.setBounds(60, 200, 150, 30);
			telLabel.setFont(new Font("돋움", Font.PLAIN, 15));
			add(telLabel);
			
			String [] tele = {"010","011","012","013","014","015","016","017","018","019"};
			num1 = new JComboBox<String>(tele);
			num1.setBounds(140, 200, 80, 30);
			num1.setSelectedIndex(0);
			add(num1);

			JLabel aa = new JLabel("-");
			aa.setBounds(230, 200, 10, 30);
			add(aa);

			num2 = new JTextField();
			num2.setBounds(240, 200, 80, 30);
			add(num2);

			JLabel bb = new JLabel("-");
			bb.setBounds(330, 200, 10, 30);
			add(bb);

			num3 = new JTextField();
			num3.setBounds(340, 200, 80, 30);
			add(num3);
			
			btnSearch = new JButton("확인");
			btnSearch.setBounds(180, 260, 70, 30);
			btnSearch.addActionListener(this);
			add(btnSearch);
			
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			
			id = idFiled.getText();
			name = nameFiled.getText();
			tel = num1.getSelectedItem()+"-"+num2.getText()+"-"+num3.getText();
			
			uld = new CustomerDAO().detail(id);
			
			uldSub = new CustomerDAO().pwSearch(name, tel);
			
			if(!id.equals("")) {
				
				if(!name.equals("")) {
					
					if(!num2.getText().equals("") && !num3.getText().equals("")) {
						
						if(uld == null)
							JOptionPane.showMessageDialog(this, "존재하는 ID가 없습니다.");
						else if(uld != null && uldSub == null)
							JOptionPane.showMessageDialog(this, "이름 또는 전화번호 잘못되었습니다.");
						else if(uld != null && uldSub != null)
							JOptionPane.showMessageDialog(this, uldSub.getPw());
						
					}else
						JOptionPane.showMessageDialog(this, "전화번호를 정확히 입력하세요.");
					
				}else
					JOptionPane.showMessageDialog(this, "이름을 입력하세요.");
				
				
			}else
				JOptionPane.showMessageDialog(this, "아이디를 입력해 주세요");
			
			
		}
		
	}

	public static void main(String[] args) {
		new IdAndPwSearch();
	}
}
