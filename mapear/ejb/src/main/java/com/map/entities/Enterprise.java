//package com.map.entities;
//
//import java.io.Serializable;
//import javax.persistence.*;
//import java.util.List;
//
//
///**
// * The persistent class for the enterprise database table.
// * 
// */
//@Entity
//@Table(name="enterprise")
//public class Enterprise implements Serializable {
//	private static final long serialVersionUID = 1L;
//
//	@Id
//	@GeneratedValue(strategy=GenerationType.IDENTITY)
//	@Column(name="ENT_ID")
//	private int entId;
//
//	@Column(name="ENT_DESCRIPTION")
//	private String entDescription;
//
//	@Column(name="ENT_NAME")
//	private String entName;
//
//	//bi-directional many-to-one association to User
//	@OneToMany(mappedBy="enterprise")
//	private List<User> users;
//
//	public Enterprise() {
//	}
//
//	public int getEntId() {
//		return this.entId;
//	}
//
//	public void setEntId(int entId) {
//		this.entId = entId;
//	}
//
//	public String getEntDescription() {
//		return this.entDescription;
//	}
//
//	public void setEntDescription(String entDescription) {
//		this.entDescription = entDescription;
//	}
//
//	public String getEntName() {
//		return this.entName;
//	}
//
//	public void setEntName(String entName) {
//		this.entName = entName;
//	}
//
//	public List<User> getUsers() {
//		return this.users;
//	}
//
//	public void setUsers(List<User> users) {
//		this.users = users;
//	}
//
//	public User addUser(User user) {
//		getUsers().add(user);
//		user.setEnterprise(this);
//
//		return user;
//	}
//
//	public User removeUser(User user) {
//		getUsers().remove(user);
//		user.setEnterprise(null);
//
//		return user;
//	}
//
//}