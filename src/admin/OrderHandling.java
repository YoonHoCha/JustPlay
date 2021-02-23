package admin;

import java.util.ArrayList;
import java.util.Vector;

import data.OrderSendArray;
import db.OrderHandlingDB;

public class OrderHandling {
	ArrayList<Vector<String>> arr;
	String id; 
	boolean cardOrCash;
	
	public OrderHandling(ArrayList<Vector<String>> arr, String id, boolean cardOrCash) {
		super();
		this.arr = arr;
		this.id = id;
		this.cardOrCash = cardOrCash;
		
	}
	
	void stockCalc() {
		
		for (Vector<String> vv : arr) {
			
			for (OrderSendArray os : new OrderHandlingDB().stocklist()) {
				if(vv.get(1).equals(os.mercName)) {
					new OrderHandlingDB().deleteStock(os.mercSerial);
					
					if(!cardOrCash) {
						int cash = new OrderHandlingDB().getVault();
						cash += new OrderHandlingDB().getItem(os.mercName);
						
						new OrderHandlingDB().setVault(cash);
					}
					
					break;
				}
			}
		}
	}

}
