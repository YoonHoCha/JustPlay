package data;

public class InventoryOrderInfo{
	
	public String mercName,mercCate,mercCnt;
	
	
	
	public InventoryOrderInfo(String mercName, String mercCate, String mercCnt) {
		super();
		this.mercName = mercName;
		this.mercCate = mercCate;
		this.mercCnt = mercCnt;
	}

	@Override
	public String toString() {
		return "InventoryOrderInfo [mercName=" + mercName + ", mercCate=" + mercCate + ", mnercCnt=" + mercCnt + "]";
	}
	
	
	
}