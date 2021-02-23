package data;

import java.io.Serializable;
import java.util.Vector;

public class GameInfo implements Serializable{
	private static final long serialVersionUID = 3;
	
	public Vector<String> info;

	public GameInfo(String name, String category, String time) {
		super();
		info = new Vector<String>();
		info.add(name);
		info.add(category);
		info.add(time);

	}
	
}
