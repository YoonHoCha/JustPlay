package data;

public class InventoryInfo {
	public String kind, name,serial;

	
	public InventoryInfo(String kind, String name, String serial) {
		super();
		this.kind = kind;
		this.name = name;
		this.serial = serial;
	}
	
	@Override
	public String toString() {
		return kind+","+name+","+serial;
	}
}
