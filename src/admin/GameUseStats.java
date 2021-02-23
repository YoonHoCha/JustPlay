
package admin;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import data.GameInfo;
import db.GameCalcDB;


public class GameUseStats extends JFrame {

	Vector<String> gameColumn = new Vector<String>();
	DefaultTableModel model;
	ArrayList<GameInfo> pop;
	ArrayList<GameInfo> arr;
	
	boolean chk, graphChk;
	GameGraph gg;
	
	public GameUseStats(boolean chk) {
		super("게임 순위");
		this.chk = chk;
		
		pop = new ArrayList<GameInfo>();

		gameColumn.addElement("게임");
		gameColumn.addElement("카테고리");
		gameColumn.addElement("플레이 시간");

		model = new DefaultTableModel(gameColumn, 0) {
			
			public boolean isCellEditable(int i, int c) {
				return false;
			}
		};
		
		arr = new GameCalcDB().list();

		for (int i = 0; i < arr.size(); i++) {
			for (int j = 0; j < arr.size()-1; j++) {
				if(arr.get(j).info.get(2) != null && arr.get(j+1).info.get(2) != null) {
					String [] str1 = arr.get(j).info.get(2).split(":");
					int hour1 = Integer.parseInt(str1[0]);
					int minute1 = Integer.parseInt(str1[1]);

					String [] str2 = arr.get(j+1).info.get(2).split(":");
					int hour2 = Integer.parseInt(str2[0]);
					int minute2 = Integer.parseInt(str2[1]);

					if(hour1 < hour2) {
						sort(j);
					}else if(hour1 == hour2 && minute1 < minute2) {
						sort(j);
					}

				}
			}
		}
		
		for (int i = 0; i < 9; i++) {
			pop.add(arr.get(i));
		}

		System.out.println(pop);

		for (GameInfo game : arr) {

			model.addRow(game.info);
		}

		setBounds(300, 100, 420, 450);
		setLayout(null);

		JLabel title = new JLabel("게임 플레이 타임");
		title.setBounds(30, 0, 150, 30);
		
		JButton graphBtn = new JButton("그래프");
		graphBtn.setBounds(300, 0, 100, 30);
		graphBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!graphChk) {
					gg = new GameGraph(arr);
					graphChk = true;
				}else {
					gg.gGui.dispatchEvent(new WindowEvent(gg.gGui, WindowEvent.WINDOW_CLOSING));
					gg = new GameGraph(arr);
				}
			}
		});
		add(graphBtn);
		
		JTable table = new JTable(model);
		table.setRowSorter(new TableRowSorter<TableModel>(model));

		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new BorderLayout());
		tablePanel.setBounds(0, 100, 400, 300);

		JScrollPane scroll = new JScrollPane(table);
		tablePanel.add(scroll, BorderLayout.CENTER);

		add(title);
		add(tablePanel);

		setVisible(chk);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	void sort(int i) {
		GameInfo tmp = arr.get(i+1);
		arr.remove(i+1);
		arr.add(i, tmp);
	}
	
	public ArrayList<GameInfo> getPop(){
		return pop;
	}
	
	public static void main(String[] args) {
		new GameUseStats(true);
	}


}
