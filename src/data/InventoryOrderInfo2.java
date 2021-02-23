package data;

public class InventoryOrderInfo2 {
	
	public String kind, name,serial;

	
	public InventoryOrderInfo2(String kind, String name, String serial) {
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
