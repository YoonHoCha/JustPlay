package data;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogData implements Serializable {
	
	private static final long serialVersionUID = 10;
	
	Date time;
	String id, login, item, pay;
	String sum;
	int fee, price;
	
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		
		this.time = time;
		
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	
	
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	
	public String getSum() {
		return sum;
	}
	public void setSum(String sum) {
		this.sum = sum;
	}
	
	public String getPay() {
		return pay;
	}
	public void setPay(String pay) {
		this.pay = pay;
	}
		
	public int getFee() {
		return fee;
	}
	public void setFee(int fee) {
		this.fee = fee;
	}
	
	public String strTime() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
	}
	
	public String logForm() {
		
		String res="";
		
		if(login !=null) {
			
			switch(login) {
			
			case "true":
				res += "로그인";
				break;
			case "false":
				res += "로그아웃";
				break;
			default:
				res += "";
				
			}
			
		}
		

		if(item !=null) {
			
			switch(item) {
			
			case "null":
				res += "";
				break;
			default:
				res += item +"\t"+pay;

			}
		}
		
		
		switch(fee) {
		
			case 0:
				res += "";
				break;
				
			default:
				res += "충전"+"("+fee+")";
				break;
		
		}
		
		
		return res;
		
	}
	
	@Override
	public String toString() {
		return "["+strTime()+"]" + "  "+ id+ "\t"+ logForm();
	}
	
	
	
	

}
