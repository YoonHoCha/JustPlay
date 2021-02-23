package admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import data.InventoryInfo;
import db.InvenDB;

public class InventoryManageMain extends JFrame {

	JTable merchanList;
	ArrayList<InventoryInfo> arr;
	InvenDB inven;
	int cnt;
	HashMap<String, ArrayList<InventoryInfo>> merchCnt;
	
	public InventoryManageMain() {
		merchCnt = new HashMap<String, ArrayList<InventoryInfo>>();

		inven = new InvenDB();

		setBounds(400, 0, 500, 700);
		setLayout(null);
		Object [] index = {"종류","상품 명","수량"};
		
		DefaultTableModel dtm = new DefaultTableModel(index,0) {

			public boolean isCellEditable(int i, int c){
				return false;
			}

		};


		///////////////////갯수작업

		for (InventoryInfo ii : inven.arr) {
			ArrayList<InventoryInfo> tmp = new ArrayList<InventoryInfo>();
			if(merchCnt.containsKey(ii.name))
				tmp = merchCnt.get(ii.name);
			tmp.add(ii);

			merchCnt.put(ii.name, tmp);
		}

		for (Entry<String, ArrayList<InventoryInfo>> it : merchCnt.entrySet()) {
			Vector invenList = new Vector();

			invenList.add(it.getValue().get(0).kind);
			invenList.add(it.getKey());
			invenList.add(it.getValue().size());

			dtm.addRow(invenList);
		}

		merchanList = new JTable(dtm);
		merchanList.setAutoCreateRowSorter(true);

		JScrollPane listScroll = new JScrollPane(merchanList);
		listScroll.setBounds(50,50,400,600);

		add(listScroll);

		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	}

}
