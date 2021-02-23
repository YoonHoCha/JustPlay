package data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PCUseInfo implements Serializable{
	
	private static final long serialVersionUID = 4;
	
	public Date time;
	public String id, login, item, pay;
	public int count, fee;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public PCUseInfo(Date time, String id, String login, String item, int fee, String pay) {
		super();
		this.time = time;
		this.id = id;
		this.login = login;
		this.item = item;
		this.fee = fee;
		this.pay = pay;
	}
	
	public String timeStr() {
		return sdf.format(time);
	}
	
}
