package data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UserListData implements Serializable {
	private static final long serialVersionUID = 11;
	
	String id, name, tel, timer, useTime, pw;
	Date birth;
	
	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}
	
	//////////////////////////////////////
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	//////////////////////////////////////
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	//////////////////////////////////////
	public String getTel() {
		return tel;
	}
	
	public void setTel(String tel) {
		this.tel = tel;
	}
	

	//////////////////////////////////////
	public String getTimer() {
		return timer;
	}
	
	public void setTimer(String timer) {
		this.timer = timer;
	}
	
	
	//////////////////////////////////////
	public Date getBirth() {
		return birth;
	}
	
	public void setBirth(Date birth) {
		this.birth = birth;
	}
	
	
	public String getUseTime() {
		return useTime;
	}

	public void setUseTime(String useTime) {
		this.useTime = useTime;
	}
	
	//////////////////////////////////////
	
	public String strBirth() {
		return new SimpleDateFormat("yyyy-MM-dd").format(birth);
	}
	
	@Override
	public String toString() {
		return "UserListData [id=" + id + ", name=" + name + ", tel=" + tel + ", timer=" + timer + ", birth=" + birth
				+ "]";
	}

	
}
