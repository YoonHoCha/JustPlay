package data;

import java.io.Serializable;

public class MsgBox implements Serializable {

	private static final long serialVersionUID = 50;
	
	UserInfo ui;
	String msg;

	public UserInfo getUi() {
		return ui;
	}
	public void setUi(UserInfo ui) {
		this.ui = ui;
	}
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
