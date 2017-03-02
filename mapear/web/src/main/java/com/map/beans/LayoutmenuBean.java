package com.map.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import com.map.entities.User;
import com.map.entities.UserRole;
import com.map.services.UserEjb;
import com.map.services.UserRoleEjb;

@ManagedBean (name = "layoutBean")
@SessionScoped
public class LayoutmenuBean implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4668768282550280893L;
	private User usr = new User();
	private Boolean isFirstTime = new Boolean(Boolean.TRUE);
	private String userConnectedRol = new String();
	private UserRole usrRole = new UserRole();
	@EJB
	UserEjb userAction;
	@EJB
	UserRoleEjb userRoleAction;
	
	
	@PostConstruct
	public void init(){
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		usr = userAction.findByName(ec.getUserPrincipal().getName());
		usrRole = userRoleAction.findByUser(usr);		
		setIsFirstTime(usr.isUsrFirstTime());
		setUserConnectedRol(usrRole.getRole().getRolName());
		RequestContext.getCurrentInstance().update("layoutForm");
		RequestContext.getCurrentInstance().update("notificationForm");
	}


	public Boolean getIsFirstTime() {
		return isFirstTime;
	}


	public void setIsFirstTime(Boolean isFirstTime) {
		this.isFirstTime = isFirstTime;
	}


	public String getUserConnectedRol() {
		return userConnectedRol;
	}


	public void setUserConnectedRol(String userConnectedRol) {
		this.userConnectedRol = userConnectedRol;
	}

}
