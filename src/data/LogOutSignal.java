package data;

import java.io.Serializable;

public class LogOutSignal implements Serializable{

	private static final long serialVersionUID = 30;
	
	public UserInfo ui;

	public LogOutSignal(UserInfo ui) {
		super();
		this.ui = ui;
	}
	
}
