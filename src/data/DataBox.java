package data;

import java.io.Serializable;

public class DataBox implements Serializable {
	private static final long serialVersionUID = 2;
	public String receiver, sender;
	public Object data;
	public boolean cashOrCard = false;
	
	public DataBox(String receiver, String sender, Object data) {
		super();
		this.receiver = receiver;
		this.sender = sender;
		this.data = data;
	}
	
	@Override
	public String toString() {
		return data.toString();
	}
}
