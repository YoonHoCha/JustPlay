package data;

import java.io.Serializable;

public class UserInfo implements Serializable{
	private static final long serialVersionUID = 1;
	
	public int seat;
	public String id, name, userTime;
	public boolean useChk, moveChk;
	public boolean shutDownChk;
	
	@Override
	public String toString() {
		return "UserInfo [seat=" + seat + ", id=" + id + "]";
	}
	
}
