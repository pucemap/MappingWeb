package com.map.beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

import com.map.entities.Message;
import com.map.entities.User;
import com.map.entities.UserRole;
import com.map.gcm.ChatConnectionBean;
import com.map.gcm.eventBusApp;
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
	ChatConnectionBean connection = new ChatConnectionBean();
	private UserRole userRole = new UserRole();
	private UserRole persona = new UserRole();
	private Message message = new Message();
	private String messageText = new String();
	private String messagearrived = new String();
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
		System.out.println("entro");
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
		RC = RequestContext.getCurrentInstance();		
		connection.sendMessage(messageText, connection.nextMessageId(),user.getUsrName());
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
	
	
	
	
	public void showMessages() {

		connection = new ChatConnectionBean(letMeKnow);

	}

private ChatConnectionBean.Test letMeKnow = new ChatConnectionBean.Test() {

		@Override
		public void showMessage(String a, boolean x) {
			
			messagearrived = a;
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("Message", a);
			data.put("Type", x);
			new eventBusApp().showMessage(data);			
		}
	};	
	
	
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
	
	
	///
	private volatile int count;
	 
    public int getCount() {
        return count;
    }
 
    public void setCount(int count) {
        this.count = count;
    }
     
    public void increment() {
    	findMessages();
        count++;
         
        EventBus eventBus = EventBusFactory.getDefault().eventBus();
        eventBus.publish("/counter", String.valueOf(count));
    }
}
