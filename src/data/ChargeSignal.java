package data;

import java.io.Serializable;

public class ChargeSignal implements Serializable {

	private static final long serialVersionUID = 14;
	
	public String time;

	public ChargeSignal(String time) {
		super();
		this.time = time;
	}

}
