package admin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class PCUseGraphEx {
	
	private TreeMap<String, Vector<String>> totCalc;
		
	DrawGraph dg;
	
	int dep;
	
	ArrayList<Color> color = new ArrayList<Color>();
	GraphGUI ggui;
	public PCUseGraphEx(TreeMap<String, Vector<String>> totCalc) {
		
		this.totCalc = totCalc;
				
		new Init();
				
		ggui = new GraphGUI();
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
			
			if(totCalc.size() > 4) {
				dep = (totCalc.size()-4)*190;
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
			setBounds(650, 100, 870, 700);
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
			
			//System.out.println(totCalc);
			if(totCalc.size()!=0) {
				int num = 0;
				int pre_x = 0;
				int pre_y = 0;
				
				for(Entry<String, Vector<String>> en : totCalc.entrySet()) {
					g.setColor(Color.black);
					g.drawString(en.getValue().get(0), 70+(num*200), 500);
					
					int x = 110+(num*200);
					int y = 450 - (Integer.parseInt(en.getValue().get(1))/2);
					
					g.drawString(en.getValue().get(1), x-5, y-20);
					g.setColor(Color.red);
					g.fillArc(x-5, y-5, 10, 10, 0, 360);
					
					num++;
					
					
					if(pre_x != 0 && pre_y != 0)
						g.drawLine(x, y, pre_x, pre_y);
					
					pre_x = x;
					pre_y = y;
					
					
								
				}
				
			}
					
		}

	}
		
}	
