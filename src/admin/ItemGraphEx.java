package admin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class ItemGraphEx {
	
	private ArrayList<String> orderMenuArr, orderCntArr;
	String year, month;
		
	DrawGraph dg;
	
	int dep;
	
	ArrayList<Color> color = new ArrayList<Color>();
	
	public ItemGraphEx(ArrayList<String> orderMenuArr, ArrayList<String> orderCntArr, String year, String month) {
		
		this.orderMenuArr = orderMenuArr;
		this.orderCntArr = orderCntArr;
		this.year = year;
		this.month = month;
				
		new Init();
				
		new GraphGUI();
	}
	
	class Init {
		
		public Init() {
			
			color.add(Color.red);
			color.add(Color.orange);
			color.add(Color.yellow);
			color.add(Color.green);
			color.add(Color.cyan);
			color.add(Color.blue);
			color.add(Color.magenta);
			
			if(orderMenuArr.size()>15) {
				dep = (orderMenuArr.size()-15)*48;
			}else {
				dep = 0;
			}

			dg = new DrawGraph();
						
		}
		
	}
	
	class GraphGUI extends JFrame {
		
		private JPanel contentPane;
		
		public GraphGUI() {
			
			setTitle("±×·¡ÇÁ");
						
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setBounds(650, 100, 850, 700);
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			setContentPane(contentPane);
			contentPane.setLayout(null);
			
			JPanel panel = new JPanel();
			panel.setBounds(5, 5, 820, 65);
			contentPane.add(panel);
			panel.setLayout(null);
			
			Dimension d = new Dimension(820+dep, 590);
			dg.setMinimumSize(d);
			dg.setMaximumSize(d);
			dg.setPreferredSize(d);	
			
			JScrollPane jsp = new JScrollPane(dg);
			
			jsp.setBounds(5, 60, 850, 590);

			contentPane.add(jsp);
			
			JLabel yearLabel = new JLabel(year+"/"+month);
			yearLabel.setBounds(325, 5, 200, 50);
			yearLabel.setFont(new Font("µ¸¿ò", Font.BOLD, 40));
			panel.add(yearLabel);
												
			setVisible(true);
			
		}
		
	}
	
	class DrawGraph extends JPanel {
		
		public DrawGraph() {
			
			setBounds(5, 60, 820+dep, 570);
			
		}

		@Override
		public void paintComponent(Graphics grphcs) {
			
			super.paintComponent(grphcs);
			
			Graphics2D g = (Graphics2D) grphcs;
			
			g.clearRect(5, 0, 820+dep, 600);

			g.drawLine(50, 480, 800+dep, 480);
			
			if(orderMenuArr.size()!=0) {
				
				if(Integer.parseInt(orderCntArr.get(0))/50>0) {
					
					for(int cnt =1; cnt<=Integer.parseInt(orderCntArr.get(0))/50; cnt++)
					{
						g.drawString(cnt *50 +"",25,480-50*cnt);
						g.drawLine(50, 480-50*cnt, 800+dep,480-50*cnt);
					}
					
				}else {
					g.drawString(50 +"",25,480-50);
					g.drawLine(50, 480-50, 800+dep,480-50);	
				}
				
				for(int cnt=0; cnt<orderMenuArr.size(); cnt++) {
					
					for (int num = 0; num < orderMenuArr.get(cnt).length(); num++) {
						
						g.drawString(orderMenuArr.get(cnt).charAt(num)+"", 70+(cnt*50), 500+(num*12));
						
					}
									
					g.drawString(orderCntArr.get(cnt), 70+(50*cnt), 
							(470-Integer.parseInt(orderCntArr.get(0)))
							+Integer.parseInt(orderCntArr.get(0))
							-Integer.parseInt(orderCntArr.get(cnt)));
												
					g.setColor(color.get(cnt%color.size()));		
						
					g.fillRect(70+(50*cnt), 
							(480-Integer.parseInt(orderCntArr.get(0)))
							+Integer.parseInt(orderCntArr.get(0))
							-Integer.parseInt(orderCntArr.get(cnt)), 
							10, 
							Integer.parseInt(orderCntArr.get(cnt)));
					
					g.setColor(Color.black);
										
				}
				
			}
					
		}

	}
		
}	
