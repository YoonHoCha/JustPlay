package admin;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import db.OrderHandlingDB;

class SetVault {

	String adminId;
	
	JLabel viewVault;
	int cash;
	
	JButton inputBtn;
	JButton outputBtn;
	
	JTextField jtWriteCash;
	
	public SetVault(String id) {
		
		adminId = id;
			
		cash = new OrderHandlingDB().getVault();
		
	}
	
	
	class SetVaultGUI extends JFrame implements ActionListener {
		
		public SetVaultGUI() {
			
			setTitle("금고 설정");
			
			setBounds(700, 300, 500, 400);
			setLayout(null);
			
			JPanel jpTop = new JPanel();
						
			jpTop.setBounds(0, 0, 480, 150);
			jpTop.setLayout(null);
			jpTop.setBackground(Color.white);
			
			viewVault = new JLabel(cash+"");
			viewVault.setBounds(90, 50, 300, 50);
			viewVault.setFont(new Font("돋움", Font.BOLD, 40));
			viewVault.setHorizontalAlignment(SwingConstants.CENTER);
			jpTop.add(viewVault);
			
			add(jpTop);
			
			JPanel jpBot = new JPanel();
			
			jpBot.setBounds(0, 155, 480, 195);
			jpBot.setBackground(Color.yellow);
			jpBot.setLayout(new GridLayout(1,2));
			
			JPanel jpBotTF = new JPanel();
			jpBotTF.setLayout(null);
			JPanel jpBotBtn = new JPanel(new GridLayout(1,1));
			
			jtWriteCash = new JTextField();
			
			jtWriteCash.setBounds(30, 70, 180, 50);
			jtWriteCash.setFont(new Font("돋움", Font.PLAIN, 30));
			
			
			jpBotTF.add(jtWriteCash);
						
			inputBtn = new JButton("설정");
			inputBtn.setFont(new Font("돋움", Font.BOLD, 40));
			
			if(adminId.equals("admin")) {
				inputBtn.addActionListener(this);
				setVisible(true);
			}
				
			else {
				JOptionPane.showMessageDialog(this, "금고 설정 할 권한이 없습니다.");
				dispose();
				setVisible(false);
			}
			
			jpBotBtn.add(inputBtn);
			
			jpBot.add(jpBotTF);
			jpBot.add(jpBotBtn);
			
			add(jpBot);
				
						
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			JButton jb = (JButton)e.getSource();
			
			switch (jb.getText()) {
				case "설정":
					
					if(!jtWriteCash.getText().equals("")) {
						
						if(Pattern.matches("^[0-9]*$", jtWriteCash.getText())) {
							
							cash = Integer.parseInt(jtWriteCash.getText());
							
							new OrderHandlingDB().setVault(cash);
							
							viewVault.setText(cash+"");
							jtWriteCash.setText("");
							JOptionPane.showMessageDialog(this, "금고 설정이 완료되었습니다.");
						}
						else
							JOptionPane.showMessageDialog(this, "숫자만 입력하세요");
						
						
					}else 
						JOptionPane.showMessageDialog(this, "설정할 금액을 입력하세요");
					
					break;
								
				default:
					break;
			}
			
		}

	}
	
}

