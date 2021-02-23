package data;

import java.io.Serializable;

public class FeeData implements Serializable {
	
	private static final long serialVersionUID = 12;

	public String id, userTime;
	public int fee;
	
	@Override
	public String toString() {
		return "FeeData [id=" + id + ", fee=" + fee + "]";
	}
	
}
