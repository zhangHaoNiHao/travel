package com.Bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class UserBean {

	private Integer id;
	private String username;
	private String password;
	private String email;
	private String juese;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getJuese() {
		return juese;
	}

	public void setJuese(String juese) {
		this.juese = juese;
	}

	public UserBean() {}
	public UserBean(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public UserBean(int id,String username, String password) {
		this.username = username;
		this.password = password;
		this.id = id;
	}

	public UserBean(int id, String username, String password, String email) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
	}

	public UserBean(int id, String username, String password, String email, String juese) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.juese = juese;
	}

	/*@Override
	public String toString() {
		return JSON.toJSONString(this);
	}*/

	@Override
	public String toString() {
		return "UserBean{id:'"+id+"',username:'"+username+"', password:'" + password +"', email:'" + email +"', juese:'" + juese + "'}";
	}
}
