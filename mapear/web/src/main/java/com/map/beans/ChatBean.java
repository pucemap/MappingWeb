package com.map.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.map.entities.UserRole;
import com.map.services.UserRoleEjb;

@ManagedBean(name="chatBean")
@SessionScoped
public class ChatBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8990014463069759372L;
	
	private List<UserRole> userRoleList = new ArrayList<UserRole>();
	private UserRole userRole = new UserRole();
	
	@EJB
	UserRoleEjb userRoleAction;

	public List<UserRole> getUserRoleList() {
		userRoleList = userRoleAction.findByUserRole();
		return userRoleList;
	}

	public void setUserRoleList(List<UserRole> userRoleList) {
		this.userRoleList = userRoleList;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}
	

}
