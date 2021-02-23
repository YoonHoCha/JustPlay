package data;

import java.io.Serializable;

public class OrderSendArray implements Serializable {
	
	public String mercName, mercCate, mercSerial;

	public OrderSendArray(String mercSerial, String mercName, String mercCate) {
		super();
		this.mercSerial = mercSerial;
		this.mercName = mercName;
		this.mercCate = mercCate;
	}

}