package admin;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

import data.PCUseInfo;
import db.PCUseCalcDB;

public class PCUseStat extends JFrame {
	
	Vector<String> gameColumn = new Vector<String>();
    DefaultTableModel model;
    
	TreeMap<String, Vector<String>> totCalc;
	TreeMap<String, Vector<String>> oneCalc;
	
	JDateChooser dateChooser;
	JDateChooser dateChooser_1;
	
	boolean graphChk, selectChk;
	PCUseGraphEx pg;
	
	JButton graphBtn;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public PCUseStat() {
		super("PC 사용자 수");
		totCalc = new TreeMap<String, Vector<String>>();
		oneCalc = new TreeMap<String, Vector<String>>();
		
		gameColumn = new Vector<String>();
		gameColumn.addElement("날짜");
		gameColumn.addElement("이용자 수");
		
		model = new DefaultTableModel(gameColumn,0) {

			public boolean isCellEditable(int i, int c){
				return false;
			}

		};
				
		setBounds(100, 100, 565, 418);
		setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setBounds(0, 0, 547, 111);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("사용자 이용 현황");
		lblNewLabel.setBounds(14, 12, 115, 26);
		panel.add(lblNewLabel);
		
		CardLayout card = new CardLayout();
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(0, 111, 547, 260);
		getContentPane().add(panel_1);
		panel_1.setLayout(card);
		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2, "오늘 보기");
		panel_2.setLayout(null);

		JPanel panel_3 = new JPanel();
		panel_1.add(panel_3, "설정 기간 보기");
		panel_3.setLayout(null);
		
		JPanel panel_4_1 = new JPanel();
		panel_4_1.setBounds(0, 0, 121, 260);
		panel_3.add(panel_4_1);
		panel_4_1.setLayout(null);
		
		dateChooser = new JDateChooser();
		dateChooser.setBounds(14, 110, 99, 24);
		dateChooser.getComponent(1).setEnabled(false);
		panel_4_1.add(dateChooser);
		
		dateChooser_1 = new JDateChooser();
		dateChooser_1.setBounds(14, 176, 99, 24);
		dateChooser_1.getComponent(1).setEnabled(false);
		panel_4_1.add(dateChooser_1);
		
		JLabel lblNewLabel_1 = new JLabel("~");
		lblNewLabel_1.setBounds(24, 146, 18, 18);
		panel_4_1.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("기간 설정하기");
		lblNewLabel_2.setBounds(14, 57, 93, 18);
		panel_4_1.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("yyyy. M. d");
		lblNewLabel_3.setBounds(14, 87, 79, 18);
		panel_4_1.add(lblNewLabel_3);
		
		model.setNumRows(0);
		JTable table_1 = addOneTable();
		
		JPanel tablePanel_1 = new JPanel();
		tablePanel_1.setLayout(new BorderLayout());
		tablePanel_1.setBounds(0, 0, 547, 260);
		panel_2.add(tablePanel_1);
		
		JScrollPane scroll_1 = new JScrollPane(table_1);
		tablePanel_1.add(scroll_1, BorderLayout.CENTER);

		JButton btnNewButton = new JButton("오늘 보기");
		btnNewButton.setBounds(14, 54, 105, 27);
		btnNewButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton btn = (JButton)e.getSource();
				
				card.show(panel_1, btn.getText());
				
			}
		});
		panel.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("설정 기간 보기");
		btnNewButton_1.setBounds(133, 54, 127, 27);
		btnNewButton_1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton btn = (JButton)e.getSource();
				
				card.show(panel_1, btn.getText());
				
			}
		});
		panel.add(btnNewButton_1);
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JButton btnNewButton_2 = new JButton("보기");
		btnNewButton_2.setBounds(14, 210, 93, 30);
		btnNewButton_2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(dateChooser.getCalendar() == null || dateChooser_1.getCalendar() == null) {
					JOptionPane.showMessageDialog(null, "날짜를 선택해 주세요.");
					return;
				}
				selectChk = true;
				
				Date d = dateChooser_1.getCalendar().getTime();
				d.setDate(d.getDate()+1);
				
				String date1 = sdf.format(dateChooser.getCalendar().getTime());
				String date2 = sdf.format(d);
				
				model.setNumRows(0);
				
				JTable table = addSelectTable(date1, date2);
				
				JPanel tablePanel = new JPanel();
				tablePanel.setLayout(new BorderLayout());
				tablePanel.setBounds(122, 0, 425, 260);
				
				JScrollPane scroll = new JScrollPane(table);
		        tablePanel.add(scroll, BorderLayout.CENTER);
		        panel_3.add(tablePanel);
		        
		        revalidate();
		        repaint();
			}
		});
		panel_4_1.add(btnNewButton_2);
		
		graphBtn = new JButton("그래프");
		graphBtn.setBounds(14, 10, 93, 30);
		//graphBtn.
		graphBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(dateChooser.getCalendar() == null || dateChooser_1.getCalendar() == null) {
					JOptionPane.showMessageDialog(null, "날짜를 선택해 주세요.");
					return;
				}
				if(!selectChk) {
					JOptionPane.showMessageDialog(null, "보기버튼을 눌러주세요.");
					return;
				}
				
				if(!graphChk) {
					pg = new PCUseGraphEx(totCalc);
					graphChk = true;
				}else {
					pg.ggui.dispatchEvent(new WindowEvent(pg.ggui, WindowEvent.WINDOW_CLOSING));
					pg = new PCUseGraphEx(totCalc);
				}
				selectChk = false;
			}
		});
		panel_4_1.add(graphBtn);
		
	}
	
	JTable addSelectTable(String date1, String date2) {
		
		totCalc = new TreeMap<String, Vector<String>>();
		
		JTable table = new JTable(model);

		for (PCUseInfo game : new PCUseCalcDB().selectDaylist(date1, date2)) {
			String date = game.timeStr();
			int cnt = 1;
			
			if(totCalc.containsKey(date))
				cnt += Integer.parseInt(totCalc.get(date).get(1));
			
			Vector<String> tmp = new Vector<String>();
			tmp.add(date);
			tmp.add(cnt+"");
			totCalc.put(date, tmp);
		}
		
		for (Entry<String, Vector<String>> en : totCalc.entrySet()) {
			model.addRow(en.getValue());
		}
		
		return table;
	}
	
	JTable addOneTable() {
		
		oneCalc = new TreeMap<String, Vector<String>>();
		
		DefaultTableModel model = new DefaultTableModel(gameColumn,0) {

			public boolean isCellEditable(int i, int c){
				return false;
			}

		}; 
		
		JTable table = new JTable(model);
		
		Date d = new Date();
		d.setDate(new Date().getDate()+1);
		
		String date1 = sdf.format(new Date());
		String date2 = sdf.format(d);
		
		for (PCUseInfo game : new PCUseCalcDB().selectDaylist(date1, date2)) {
			String date = game.timeStr();
			int cnt = 1;
			
			if(oneCalc.containsKey(date))
				cnt += Integer.parseInt(oneCalc.get(date).get(1));
			
			Vector<String> tmp = new Vector<String>();
			tmp.add(date);
			tmp.add(cnt+"");
			oneCalc.put(date, tmp);
		}
		
		for (Entry<String, Vector<String>> en : oneCalc.entrySet()) {
			
			model.addRow(en.getValue());
		}
		
		return table;
	}
	
}
