package admin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import data.GameInfo;

public class GameGraph {
	
	private ArrayList<GameInfo> arr;
		
	DrawGraph dg;
	
	int dep;
	GraphGUI gGui;
	
	ArrayList<Color> color = new ArrayList<Color>();
	
	public GameGraph(ArrayList<GameInfo> arr) {
		this.arr = arr;
				
		new Init();
		gGui = new GraphGUI();
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
			
			if(arr.size()>15) {
				dep = (arr.size()-15)*48;
			}else {
				dep = 0;
			}

			dg = new DrawGraph();
						
		}
		
	}
	
	class GraphGUI extends JFrame {
		
		private JPanel contentPane;
		
		public GraphGUI() {
			
			setTitle("게임 그래프");
						
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setBounds(650, 100, 875, 700);
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
			
			g.clearRect(5, 0, 820+dep, 570);

			g.drawLine(50, 480, 800+dep, 480);
			
			if(arr.size()!=0) {
				
				if(Integer.parseInt(arr.get(0).info.get(2).split(":")[0])/50>0) {
					
					for(int cnt = 1; cnt <= Integer.parseInt(arr.get(0).info.get(2).split(":")[0])/100; cnt++)
					{
						//g.drawString(cnt *100 +"",25,480-50*cnt);
						//g.drawLine(50, 480-50*cnt, 800+dep,480-50*cnt);
					}
					
				}else {
					//g.drawString(100 +"",25,480-100);
					//g.drawLine(50, 480-100, 800+dep,480-50);	
				}
				
				for(int cnt = 0; cnt < arr.size(); cnt++) {
					
					for (int num = 0; num < arr.get(cnt).info.get(0).length(); num++) {
						
						g.drawString(arr.get(cnt).info.get(0).charAt(num)+"", 70+(cnt*50), 500+(num*12));
						
					}
									
					g.drawString(arr.get(cnt).info.get(2).split(":")[0], 70+(50*cnt), 
							(470-Integer.parseInt(arr.get(0).info.get(2).split(":")[0]))
							+(Integer.parseInt(arr.get(0).info.get(2).split(":")[0]))
							-(Integer.parseInt(arr.get(cnt).info.get(2).split(":")[0])/3));
												
					g.setColor(color.get(cnt%color.size()));		
						
					g.fillRect(70+(50*cnt), 
							(480-Integer.parseInt(arr.get(0).info.get(2).split(":")[0]))
							+(Integer.parseInt(arr.get(0).info.get(2).split(":")[0]))
							-(Integer.parseInt(arr.get(cnt).info.get(2).split(":")[0])/3), 
							10, 
							(Integer.parseInt(arr.get(cnt).info.get(2).split(":")[0]))/3);
					
					g.setColor(Color.black);
					
								
				}
				
			}
					
		}

	}
		
}	
