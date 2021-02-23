package admin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JMonthChooser;
import com.toedter.calendar.JYearChooser;

import data.LogData;
import db.PCUseDB;

class ItemStats {
	
	private Vector<String> statsColumn;
	private DefaultTableModel model;
	private Vector<String> statsCol;  //열 Vector
	private Vector<Vector> statsRow;  //행 Vector
    
	private ArrayList<String> orderMenuArr;
	private ArrayList<String> orderCntArr;

	private String year;
	private String month;
    
	private Date now;
    
	private SimpleDateFormat sdf;
    
	private JPanel contentPane;
    
	private TablePanel tp;
		
	ItemGraphEx ige;
		
	public ItemStats() {
		
		now = new Date();
		
		sdf = new SimpleDateFormat("YYYY");
		year = sdf.format(now);
		
		sdf = new SimpleDateFormat("MM");
		month = sdf.format(now);
		
		new SelectedDate();
	
		tp = new TablePanel();
				
	}
	
	class ItemStatsGUI extends JFrame implements ActionListener {
		
		public ItemStatsGUI() {
			
			setTitle("상품별 통계");
			
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setBounds(50, 100, 619, 348);
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			setContentPane(contentPane);
			contentPane.setLayout(null);
			
			JPanel panel = new JPanel();
			panel.setBounds(14, 12, 573, 53);
			contentPane.add(panel);
			panel.setLayout(null);
			
			JButton btnGraph = new JButton("그래프");
			btnGraph.setBounds(454, 12, 105, 27);
			btnGraph.addActionListener(this);
			panel.add(btnGraph);
			
			JYearChooser yearChooser = new JYearChooser();
			yearChooser.setBounds(14, 15, 55, 24);
			panel.add(yearChooser);
			
			JSpinner spinner = (JSpinner)yearChooser.getSpinner();
			((JTextField)spinner.getEditor()).setEditable(false);
			
			add(tp);
						
			// Get year 
			yearChooser.addPropertyChangeListener(new PropertyChangeListener() {
				
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					
					year = yearChooser.getYear()+"";
					
					remove(tp);
					
					new SelectedDate();
					
					tp = new TablePanel();
					add(tp);
					
				}
			});
			
			JMonthChooser monthChooser = new JMonthChooser();
			monthChooser.setBounds(83, 15, 80, 24);
			
			// Get month
			monthChooser.addPropertyChangeListener(new PropertyChangeListener() {
						
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					
					
					if(monthChooser.getMonth()+1>=10)
						month = (monthChooser.getMonth()+1)+"";
					else
						month = "0"+(monthChooser.getMonth()+1);
						
					remove(tp);
					
					new SelectedDate();
					
					tp = new TablePanel();
					add(tp);
					
				}
				
			});
			
			panel.add(monthChooser);
							
			setVisible(true);
		}
		

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(orderMenuArr.size() != 0) {
				ige = new ItemGraphEx(orderMenuArr, orderCntArr, year, month);
				
			}				
			else
				JOptionPane.showMessageDialog(this, "해당 날짜의 판매 상품이 없습니다.");
			
		}
		
	}
	
	class TablePanel extends JPanel {
		
		
		private JTable table;
		
		public TablePanel() {
					
			setBounds(14, 77, 573, 212);
				
			setLayout(null);
			
			statsColumn.addElement("메뉴");
			statsColumn.addElement("판매 개수");
			
			model = new DefaultTableModel(statsColumn,0) {
		    	
		    	public boolean isCellEditable(int i, int c){
		            return false;
		    	}
		    	
		    };
		    		    
		    for (int i = 0; i < orderMenuArr.size(); i++) {
				statsCol = new Vector<String>();
				statsCol.addElement(orderMenuArr.get(i));
				statsCol.addElement(orderCntArr.get(i)+"");
				statsRow.addElement(statsCol);
			}
			
			Iterator<Vector> it = statsRow.iterator();
			
			while (it.hasNext()) {
				
				model.addRow(it.next());

			}
				
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(14, 12, 545, 188);
			add(scrollPane);
			
			table = new JTable(model);
			scrollPane.setViewportView(table);
			
		}
		
	}
	
	class SelectedDate {
		
		public SelectedDate() {
				
			try {
								
				orderMenuArr = new ArrayList<String>(); 
				orderCntArr = new ArrayList<String>();
				
				statsColumn = new Vector<String>();
				statsRow = new Vector<Vector>();
																						
				ArrayList<LogData> list = new PCUseDB().itemStats(year+"-"+month);

				for (LogData ld : list) {

					if(!ld.getItem().equals("null")) {
						orderMenuArr.add(ld.getItem());
						orderCntArr.add(ld.getSum());

					}

				}

			} catch (Exception e1) {
				
			}
					
		}
		
	}
	
}