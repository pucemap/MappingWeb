package com.map.beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import com.map.entities.Message;
import com.map.entities.User;
import com.map.entities.UserRole;
import com.map.services.MessageEjb;
import com.map.services.UserEjb;
import com.map.services.UserRoleEjb;

@ManagedBean(name="chatBean")
@SessionScoped
public class ChatBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8990014463069759372L;
	
	
	private User user = new User();
	private UserRole userRole = new UserRole();
	private UserRole persona = new UserRole();
	private Message message = new Message();
	private String messageText = new String();
	private List<Message> lista = new ArrayList<Message>();
	private List<UserRole> userRoleList = new ArrayList<UserRole>();
	RequestContext RC;
	ExternalContext ec;
	
	@EJB
	UserRoleEjb userRoleAction;
	
	@EJB
	MessageEjb messageAction;
	
	@EJB
	UserEjb userAction;
	
	@SuppressWarnings("deprecation")
	public void onAppointmentTypeChange() {
		findMessages();
	}
	
	public void findMessages(){
		ec = FacesContext.getCurrentInstance().getExternalContext();
		RC = RequestContext.getCurrentInstance();
		user = userAction.findByName(ec.getUserPrincipal().getName());
		persona = userRole;
		lista = messageAction.getMessagesByUsers(user,persona.getUser());
		RequestContext.getCurrentInstance().update("chatForm");
	}
	
	public void send(){
		Date date = new Date(System.currentTimeMillis());
	    //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");	       
		message = new Message(date, date, "S", messageText, persona.getUser(), user);
		
		try {
			messageAction.persist(message);
			message = new Message();
			messageText = new String();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		findMessages();
		RequestContext.getCurrentInstance().update("chatForm:area");
		RequestContext.getCurrentInstance().update("chatForm:scroll");
		
	}
	
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

	public UserRole getPersona() {
		return persona;
	}

	public void setPersona(UserRole persona) {
		this.persona = persona;
	}

	public List<Message> getLista() {
		
         
		return lista;
	}

	public void setLista(List<Message> lista) {
		this.lista = lista;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}
}
