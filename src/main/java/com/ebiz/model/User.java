package com.ebiz.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@Document(indexName = "contact", type = "users")
public class User {
	@Id
	private String id;
	private String username;
	private String password;
	private int age;
	private String phone;
	private String mail;
	private List<Adress> adresses;

	public User(String username, String id, String password, int age, String phone, List<Adress> adresses, String mail) {
		this.username = username;
		this.id = id;
		this.password = password;
		this.age = age;
		this.phone = phone;
		this.adresses = adresses;
		this.mail = mail;
	}

	public User() {
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
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
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}

	public List<Adress> getAdresses() {
		return adresses;
	}
	public void setAdresses(List<Adress> adresses) {
		//System.out.println("liste adresse taille : " + adresses.size());
		this.adresses = adresses;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password="
				+ password + ", age=" + age + ", phone=" + phone + ", mail="
				+ mail + ", adresses=" + adresses + "]";
	}
}
