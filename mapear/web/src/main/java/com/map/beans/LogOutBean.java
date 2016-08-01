package com.map.beans;


/*
 * @Autor
 * @Fecha:
 * @Descripción:
 */

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabCloseEvent;

import com.map.entities.User;
import com.map.services.UserEjb;
import com.map.gcm.ChatConnectionBean;
import com.map.gcm.eventBusApp;
import com.map.GCMinterfaces.GcmInterface;

@ManagedBean(name = "loginBean")
@SessionScoped
public class LogOutBean implements Serializable, GcmInterface {

	private static final long serialVersionUID = 5722616030335589264L;
	ChatConnectionBean connection = new ChatConnectionBean();	
	User user = new User();	
	User userTable = new User();
	private List<User> userNotificationList = new ArrayList<User>();
	ExternalContext ec;

	@EJB
	UserEjb userAction;
	
	public void log() {
		//chargeList();
		ec = FacesContext.getCurrentInstance()
				.getExternalContext();
		//RC = RequestContext.getCurrentInstance();
		user = userAction.findByName(ec.getUserPrincipal().getName());
		user.setUsrIsLog("true");		

		try {
			userAction.merge(user);
			connection.connect(YOUR_PROJECT_ID, YOUR_API_KEY);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	RequestContext.getCurrentInstance().execute("PF('bar').show();");
	}

	public void logout() throws IOException {

		ExternalContext ec = FacesContext.getCurrentInstance()
				.getExternalContext();
		ec.invalidateSession();
		user = userAction.findByName(ec.getUserPrincipal().getName());
		user.setUsrIsLog("false");
		try {
			userAction.merge(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ec.redirect(ec.getRequestContextPath() + "/pages/user/user.xhtml");
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<User> getUserNotificationList() {
		return userNotificationList;
	}

	public void setUserNotificationList(List<User> userNotificationList) {
		this.userNotificationList = userNotificationList;
	}

	public User getUserTable() {
		return userTable;
	}

	public void setUserTable(User userTable) {
		this.userTable = userTable;
	}

}

