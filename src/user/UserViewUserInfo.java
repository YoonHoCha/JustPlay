package user;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import data.LogData;
import data.UserListData;
import db.CustomerDAO;
import db.UserUseCountDB;

public class UserViewUserInfo {
	
	UserListData uld;
	UserInfoGUI uig;
	
	JFrame fi = null, frame = null, f = null;
	
	String id;
	int totFee, totPrice, totLogin;
	ArrayList<LogData> res;
	
	public UserViewUserInfo(String id) {
		this.id = id;
		this.uld = new CustomerDAO().detail(id);
		uig = new UserInfoGUI();
	}
	
	class UserInfoGUI extends JFrame implements ActionListener {
		
		public UserInfoGUI() {
			
			setTitle("회원 정보");
			setBounds(600, 250, 700, 600);
			setLayout(null);
			
			JPanel jp = new JPanel();
			jp.setLayout(null);
			jp.setBounds(5, 5, 670, 540);
			jp.setBackground(Color.white);
			add(jp);
			
			//////////////////////////////////////////
			JLabel jlId = new JLabel("ID:");
			jlId.setBounds(10, 30, 70, 30);
			jlId.setFont(new Font("돋움", Font.ITALIC+Font.BOLD, 20));
			jp.add(jlId);
			
			JLabel idInfo = new JLabel(uld.getId());
			idInfo.setBounds(200, 30, 450, 30);
			idInfo.setFont(new Font("돋움", Font.ITALIC+Font.BOLD, 20));
			jp.add(idInfo);
			
			//////////////////////////////////////////
			JLabel jlName = new JLabel("이름:");
			jlName.setBounds(10, 80, 80, 30);
			jlName.setFont(new Font("돋움", Font.ITALIC+Font.BOLD, 20));
			jp.add(jlName);
			
			JLabel nameInfo = new JLabel(uld.getName());
			nameInfo.setBounds(200, 80, 450, 30);
			nameInfo.setFont(new Font("돋움", Font.ITALIC+Font.BOLD, 20));
			jp.add(nameInfo);
			
			///////////////////////////////////////////
			JLabel jlBirth = new JLabel("생년월일:");
			jlBirth.setBounds(10, 120, 150, 30);
			jlBirth.setFont(new Font("돋움", Font.ITALIC+Font.BOLD, 20));
			jp.add(jlBirth);
			
			JLabel birthInfo = new JLabel(uld.getBirth()+"");
			birthInfo.setBounds(200, 120, 450, 30);
			birthInfo.setFont(new Font("돋움", Font.ITALIC+Font.BOLD, 20));
			jp.add(birthInfo);
			
			///////////////////////////////////////////
			JLabel jlTel = new JLabel("전화번호:");
			jlTel.setBounds(10, 160, 150, 30);
			jlTel.setFont(new Font("돋움", Font.ITALIC+Font.BOLD, 20));
			jp.add(jlTel);
			
			JLabel telInfo = new JLabel(uld.getTel());
			telInfo.setBounds(200, 160, 250, 30);
			telInfo.setFont(new Font("돋움", Font.ITALIC+Font.BOLD, 20));
			jp.add(telInfo);
			
			String [] tel = telInfo.getText().split("-");
			System.out.println(Arrays.toString(tel));
			
			JButton telBtn = new JButton("전화 번호 수정");
			telBtn.setBounds(500, 160, 150, 30);
			telBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if(fi!=null) {
						fi.dispose();
						fi = null;
					}
					
					fi = new JFrame("전화 번호 수정");
					fi.setLayout(null);
					fi.setBounds(500, 500, 500, 300);

					
					
					String [] tele = {"010","011","012","013","014","015","016","017","018","019"};
					JComboBox<String> num1 = new JComboBox<String>(tele);
					num1.setBounds(10, 100, 80, 30);
					num1.setSelectedItem(tel[0]);
					fi.add(num1);

					JLabel aa = new JLabel("-");
					aa.setBounds(110, 100, 10, 30);
					fi.add(aa);

					JTextField num2 = new JTextField();
					num2.setBounds(130, 100, 80, 30);
					num2.setText(tel[1]);
					fi.add(num2);

					JLabel bb = new JLabel("-");
					bb.setBounds(220, 100, 10, 30);
					fi.add(bb);

					JTextField num3 = new JTextField();
					num3.setBounds(240, 100, 80, 30);
					num3.setText(tel[2]);
					fi.add(num3);
					
					JButton btn = new JButton("수정하기");
					btn.setBounds(250, 150, 100, 30);
					btn.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							String chk1 = "(?:\\d{3}|\\d{4})";
							String chk2 = "\\d{4}$";
							
							Pattern pattern1 = Pattern.compile(chk1);
							Matcher matcher1 = pattern1.matcher(num2.getText());
							
							Pattern pattern2 = Pattern.compile(chk2);
							Matcher matcher2 = pattern2.matcher(num3.getText());
							
							String res = num1.getSelectedItem()+"-"+num2.getText()+"-"+num3.getText();
							
							if(uld.getTel().equals(res)) {
								JOptionPane.showMessageDialog(null, "이전과 동일한  번호는 사용할 수 없습니다.");
							}else if(!matcher1.matches()) {
								JOptionPane.showMessageDialog(null, "핸드폰 번호를 확인해 주세요.");	
							}else if(!matcher2.matches()) {
								JOptionPane.showMessageDialog(null, "핸드폰 번호를 확인해 주세요.");
							}else {
								//디비로 전화번호 저장
								JOptionPane.showMessageDialog(null, "핸드폰 번호가 변경되었습니다.");
								new CustomerDAO().setPhone(res, id);
								telInfo.setText(res);
								
								fi.dispose();
							}
		
						}
					});
					
					
					fi.add(btn);
					
					fi.setVisible(true);
					fi.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				}
			});
			jp.add(telBtn);
			
			///////////////////////////////////////////
			
			JLabel jlTimer = new JLabel("남은 시간:");
			jlTimer.setBounds(10, 200, 160, 30);
			jlTimer.setFont(new Font("돋움", Font.ITALIC+Font.BOLD, 20));
			jp.add(jlTimer);
			
			JLabel timerInfo = new JLabel(uld.getTimer());
			timerInfo.setBounds(200, 200, 450, 30);
			timerInfo.setFont(new Font("돋움", Font.ITALIC+Font.BOLD, 20));
			jp.add(timerInfo);
			
			//////////////////////////////////////////////////////////////
			
			JLabel jlUseTime = new JLabel("사용 시간:");
			jlUseTime.setBounds(10, 240, 160, 30);
			jlUseTime.setFont(new Font("돋움", Font.ITALIC+Font.BOLD, 20));
			jp.add(jlUseTime);
			
			JLabel useTimeInfo = new JLabel(uld.getUseTime());
			useTimeInfo.setBounds(200, 240, 450, 30);
			useTimeInfo.setFont(new Font("돋움", Font.ITALIC+Font.BOLD, 20));
			jp.add(useTimeInfo);
			
			calc();
			
			JLabel buyFee = new JLabel("총 충전금액 : "+totFee);
			buyFee.setBounds(10, 280, 300, 30);
			buyFee.setFont(new Font("돋움", Font.ITALIC+Font.BOLD, 20));
			jp.add(buyFee);
			
			JLabel buyPrice = new JLabel("총 상품 주문 금액 : "+totPrice);
			buyPrice.setBounds(10, 320, 300, 30);
			buyPrice.setFont(new Font("돋움", Font.ITALIC+Font.BOLD, 20));
			jp.add(buyPrice);
			
			JButton chkPrice = new JButton("주문상품 상세내역");
			chkPrice.setBounds(350, 320, 150, 30);
			chkPrice.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if(frame!=null) {
						frame.dispose();
						frame = null;
					}
					
					frame = new JFrame("상세 내역");
					frame.setBounds(500, 500, 330, 300);
					frame.setLayout(null);
					
					JLabel lb = new JLabel(id+"님의 주문 상세 내역");
					lb.setBounds(10, 10, 200, 30);
					frame.add(lb);
					
					JTextArea ta = new JTextArea();
					ta.setEditable(false);
					ta.setLineWrap(true);
					
					JScrollPane sp = new JScrollPane(ta);
					sp.setBounds(10, 50, 300, 300);
					frame.add(sp);
					
					for (LogData ld : res) {
						if(!ld.getItem().equals("null")) 
							ta.append(ld.getItem()+" - "+ld.getTime()+"\n");
					}
					
					frame.setVisible(true);
					frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				}
			});
			
			jp.add(chkPrice);
			
			JLabel loginCnt = new JLabel("총 로그인 횟수 : "+totLogin+"");
			loginCnt.setBounds(10, 360, 300, 30);
			loginCnt.setFont(new Font("돋움", Font.ITALIC+Font.BOLD, 20));
			jp.add(loginCnt);
			
			///////////////////////////////////////////
			JButton btn1 = new JButton("확인");
			btn1.setBounds(100, 450, 150, 30);
			btn1.setFont(new Font("돋움", Font.ITALIC+Font.BOLD, 20));
			btn1.addActionListener(this);
			jp.add(btn1);
			
			JButton btn2 = new JButton("비밀 번호 변경");
			btn2.setBounds(260, 450, 200, 30);
			btn2.setFont(new Font("돋움", Font.ITALIC+Font.BOLD, 20));
			btn2.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					
					if(f != null) {
						f.dispose();
						f = null;
					}
					
					String pre_pw = new CustomerDAO().getPw(id);
					
					f = new JFrame("비밀 번호 변경");
					f.setBounds(500, 500, 400, 350);
					f.setLayout(null);
					
					JLabel jl2 = new JLabel("비밀번호");
					jl2.setBounds(50, 50, 80, 40);
					f.add(jl2);

					JPasswordField pw_gui = new JPasswordField();
					pw_gui.setBounds(50, 100, 120, 40);
					f.add(pw_gui);

					JLabel jl4 = new JLabel("비밀번호확인");
					jl4.setBounds(50, 150, 80, 40);
					f.add(jl4);

					JPasswordField pwchk_gui = new JPasswordField();
					pwchk_gui.setBounds(50, 200, 120, 40);
					f.add(pwchk_gui);
					
					JButton btn = new JButton("변경");
					btn.setBounds(250, 250, 100, 40);
					btn.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							String chk1 = "\\s";
							
							Pattern pattern = Pattern.compile(chk1);
							Matcher matcher = pattern.matcher(pw_gui.getText());
							
							while(matcher.find()) {
								JOptionPane.showMessageDialog(null, "띄어쓰기를 제거해 주세요.");
								return;
							}
							
							if(pw_gui.getText().length() < 7) {
								JOptionPane.showMessageDialog(null, "최소 글자수는 7글자 입니다.");
								return;
							}else if(!pw_gui.getText().equals(pwchk_gui.getText())) {
								JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다.");
								return;
							}else if(pre_pw.equals(pw_gui.getText())) {
								JOptionPane.showMessageDialog(null, "이전 비밀번호와 일치합니다.");
								return;
							}
							
							JOptionPane.showMessageDialog(null, "비밀번호가 변경되었습니다.");
							new CustomerDAO().setPw(pw_gui.getText(), id);
							f.dispose();
						}
					});
					f.add(btn);
					
					f.setVisible(true);
					f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				}
			});
			jp.add(btn2);
			
			setVisible(true);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			
		}
		
		void calc() {
			res = new UserUseCountDB().getUserSale(id);
			
			for (LogData ld : res) {
				
				if(ld.getFee() != 0){
					totFee += ld.getFee();
				}else if(ld.getPrice() != 0){
					totPrice += ld.getPrice();
				}else if(ld.getLogin() != null) {
					if(ld.getLogin().equals("true"))
						totLogin++;
				}
			}
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			dispose();
			
		}
		
	}

}
