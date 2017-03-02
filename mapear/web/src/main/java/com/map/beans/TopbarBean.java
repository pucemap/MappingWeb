package com.map.beans;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import com.map.entities.User;
import com.map.services.UserEjb;

@ManagedBean(name = "topbarBean")
@SessionScoped

public class TopbarBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2059715526870008138L;
	private User usr = new User();
	private Boolean isFirstTime = new Boolean(Boolean.TRUE);

	@EJB
	UserEjb userAction;

	@PostConstruct
	public void init() {
		
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		usr = userAction.findByName(ec.getUserPrincipal().getName());
		setIsFirstTime(usr.isUsrFirstTime());
		System.out.println(isFirstTime + "TopBar");
		RequestContext.getCurrentInstance().update("notificationForm"); 

	}

	public Boolean getIsFirstTime() {
		return isFirstTime;
	}

	public void setIsFirstTime(Boolean isFirstTime) {
		this.isFirstTime = isFirstTime;
	}

}
