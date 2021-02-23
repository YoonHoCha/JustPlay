package data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JLabel;

public class TransData implements Serializable {
	private static final long serialVersionUID = 7;
	
	public Vector<UserInfo> uiVector;
	public JLabel nowPax;
	public UserInfo usingUi;
	public UserInfo ui;
	public UserInfo uiOut;
	public UserInfo usingUiOut;
	public UserInfo tempUi;
	public ArrayList<String> idArr;
	public ArrayList<Integer> seatArr;
	public ArrayList<UserInfo> userArr;
	
	public FeeData fd;
	
	public int temp;
	
	public ArrayList al;
	public ArrayList usingAl;
	
	public boolean cashOrCard = false;
	
	public String id;
	
}
