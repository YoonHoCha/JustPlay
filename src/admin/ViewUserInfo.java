package admin;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import data.UserListData;

public class ViewUserInfo {
	
	UserListData uld;
	
	UserInfoGUI uig;
	
	public ViewUserInfo(UserListData uld) {
		
		this.uld = uld;
		
	}
	
	class UserInfoGUI extends JFrame implements ActionListener {
		
		public UserInfoGUI() {
			
			setTitle("È¸¿ø Á¤º¸");
			setBounds(600, 250, 700, 600);
			setLayout(null);
			
			JPanel jp = new JPanel();
			jp.setLayout(null);
			jp.setBounds(5, 5, 670, 540);
			jp.setBackground(Color.white);
			add(jp);
			
			//////////////////////////////////////////
			JLabel jlId = new JLabel("ID:");
			jlId.setBounds(10, 30, 70, 50);
			jlId.setFont(new Font("µ¸¿ò", Font.ITALIC+Font.BOLD, 30));
			jp.add(jlId);
			
			JLabel idInfo = new JLabel(uld.getId());
			idInfo.setBounds(200, 30, 450, 50);
			idInfo.setFont(new Font("µ¸¿ò", Font.ITALIC+Font.BOLD, 30));
			jp.add(idInfo);
			
			//////////////////////////////////////////
			JLabel jlName = new JLabel("ÀÌ¸§:");
			jlName.setBounds(10, 100, 80, 50);
			jlName.setFont(new Font("µ¸¿ò", Font.ITALIC+Font.BOLD, 30));
			jp.add(jlName);
			
			JLabel nameInfo = new JLabel(uld.getName());
			nameInfo.setBounds(200, 100, 450, 50);
			nameInfo.setFont(new Font("µ¸¿ò", Font.ITALIC+Font.BOLD, 30));
			jp.add(nameInfo);
			
			///////////////////////////////////////////
			JLabel jlBirth = new JLabel("»ý³â¿ùÀÏ:");
			jlBirth.setBounds(10, 170, 150, 50);
			jlBirth.setFont(new Font("µ¸¿ò", Font.ITALIC+Font.BOLD, 30));
			jp.add(jlBirth);
			
			JLabel birthInfo = new JLabel(uld.getBirth()+"");
			birthInfo.setBounds(200, 170, 450, 50);
			birthInfo.setFont(new Font("µ¸¿ò", Font.ITALIC+Font.BOLD, 30));
			jp.add(birthInfo);
			
			///////////////////////////////////////////
			JLabel jlTel = new JLabel("ÀüÈ­¹øÈ£:");
			jlTel.setBounds(10, 240, 150, 50);
			jlTel.setFont(new Font("µ¸¿ò", Font.ITALIC+Font.BOLD, 30));
			jp.add(jlTel);
			
			JLabel telInfo = new JLabel(uld.getTel());
			telInfo.setBounds(200, 240, 450, 50);
			telInfo.setFont(new Font("µ¸¿ò", Font.ITALIC+Font.BOLD, 30));
			jp.add(telInfo);
			
			///////////////////////////////////////////
			
			JLabel jlTimer = new JLabel("³²Àº ½Ã°£:");
			jlTimer.setBounds(10, 310, 160, 50);
			jlTimer.setFont(new Font("µ¸¿ò", Font.ITALIC+Font.BOLD, 30));
			jp.add(jlTimer);
			
			JLabel timerInfo = new JLabel(uld.getTimer());
			timerInfo.setBounds(200, 310, 450, 50);
			timerInfo.setFont(new Font("µ¸¿ò", Font.ITALIC+Font.BOLD, 30));
			jp.add(timerInfo);
			
			//////////////////////////////////////////////////////////////
			
			JLabel jlUseTime = new JLabel("»ç¿ë ½Ã°£:");
			jlUseTime.setBounds(10, 380, 160, 50);
			jlUseTime.setFont(new Font("µ¸¿ò", Font.ITALIC+Font.BOLD, 30));
			jp.add(jlUseTime);
			
			JLabel useTimeInfo = new JLabel(uld.getUseTime());
			useTimeInfo.setBounds(200, 380, 450, 50);
			useTimeInfo.setFont(new Font("µ¸¿ò", Font.ITALIC+Font.BOLD, 30));
			jp.add(useTimeInfo);
			
			///////////////////////////////////////////
			JButton btn = new JButton("È®ÀÎ");
			btn.setBounds(250, 450, 150, 50);
			btn.setFont(new Font("µ¸¿ò", Font.ITALIC+Font.BOLD, 30));
			jp.add(btn);
			
			btn.addActionListener(this);
			
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setVisible(true);
			
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			
			dispose();
			
		}
		
	}

}
