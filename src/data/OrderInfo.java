package data;

import java.io.Serializable;

public class OrderInfo implements Serializable {

	private static final long serialVersionUID = 8;
	
	String name, cate, serial;
	int price;
	
	public OrderInfo(String name, String cate, String serial, int price) {
		super();
		this.name = name;
		this.cate = cate;
		this.serial = serial;
		this.price = price;
	}
}
