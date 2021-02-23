package data;

import java.io.Serializable;

public class MoveSignal implements Serializable {

	private static final long serialVersionUID = 20;
	
	UserInfo ui;

	public MoveSignal(UserInfo ui) {
		super();
		this.ui = ui;
	}
	
}
