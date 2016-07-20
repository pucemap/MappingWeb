package com.map.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;

import com.map.entities.Role;
import com.map.entities.User;
import com.map.entities.UserRole;
import com.map.services.RoleEjb;
import com.map.services.UserEjb;
import com.map.services.UserRoleEjb;
import com.map.utils.SHA;

@ManagedBean(name="userBean")
@SessionScoped
public class UserBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7927820995066852655L;
	/**
	 * 
	 */
	
	private User user = new User();
	private User userSearch = new User();
	private String passwordWSSH1 = new String();
	private String passwordWSSH2 = new String();
	private SHA sha = new SHA();
	private Calendar cal = Calendar.getInstance();
	private UserRole userRole = new UserRole();
	
	private String regexText = new String();
	
	private List<Role> roleList = new ArrayList<Role>();
	private List<User> userList = new ArrayList<User>();
	
	private Boolean isNew = new Boolean(Boolean.TRUE);
	
	@EJB
	UserEjb userAction;
	
	@EJB
	RoleEjb roleAction;
	
	@EJB
	UserRoleEjb userRoleAction;
	
	@PostConstruct
    public void init() {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		RequestContext requestContext = RequestContext.getCurrentInstance();
		requestContext.execute("PF('subscriber').connect('/" + ec.getUserPrincipal().getName() + "')");
    }
	
	
	public void save(){
		if(verifyPassword()){
			if(isNew){
				try {					
					user.setUsrPassword(sha.encrypt(passwordWSSH1));
					user.setUsrState("C");;
					user.setUsrCreationDate(cal.getTime());
					userAction.persist(user);
					userRole.setUser(user);
					userRole.setUsrRolCreationDate(cal.getTime());
					userRole.setUsrRolState("C");
					userRoleAction.persist(userRole);
					user = new User();
					userRole = new UserRole();
					RequestContext.getCurrentInstance().update("userForm:insertDialog");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//PIRUJA
			else{
				try {
					userAction.merge(user);
					userRole.setUser(user);
					userRole.setUsrRolCreationDate(cal.getTime());
					userRole.setUsrRolState("U");
					userRoleAction.merge(userRole);
					isNew = Boolean.TRUE;
					user = new User();
					userRole = new UserRole();
					RequestContext.getCurrentInstance().update("userForm:insertDialog");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			try {
				userList = userAction.findAll();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			RequestContext.getCurrentInstance().update("userForm:userTable");
		}
		
		
	}
	
	public void prueba(){
		System.out.println("prueba");
	}
	
	public boolean verifyPassword(){
		if(passwordWSSH1.isEmpty()||!passwordWSSH1.equals(passwordWSSH2)){
			RequestContext.getCurrentInstance().update("userForm:insertDialog");
			return false;
		}
		else 
			return true;
	}
	
	public void search(){
		try {
			userList = userAction.findByUserName(userSearch);
			RequestContext.getCurrentInstance().update("userForm:userTable");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void delete(User usr){
		
		UserRole usrole = new UserRole();
		try {
			usrole = userRoleAction.findByUser(usr);
			userRoleAction.remove(usrole);
			userAction.remove(usr);
			userList = userAction.findAll();
			RequestContext.getCurrentInstance().update("userForm:userTable");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void clean(){
		userList = new ArrayList<User>();
		RequestContext.getCurrentInstance().update("userForm:userTable");
	}
	
	public void edit(User usr){
		isNew = Boolean.FALSE;
		user = new User();	
		user = usr;
		userRole = userRoleAction.findByUser(usr);
		
		passwordWSSH1 = new String();
		passwordWSSH1 = new String();
		RequestContext.getCurrentInstance().update("userForm:insertDialog");
		RequestContext.getCurrentInstance().execute("PF('dlg2').show();");
	}
	
	public void handleClose(CloseEvent event) {
		user = new User();
		userRole = new UserRole();
		isNew = Boolean.TRUE;
		RequestContext.getCurrentInstance().update("userForm:insertDialog");
    }
	

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUserSearch() {
		return userSearch;
	}

	public void setUserSearch(User userSearch) {
		this.userSearch = userSearch;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public Boolean getIsNew() {
		return isNew;
	}

	public void setIsNew(Boolean isNew) {
		this.isNew = isNew;
	}

	public String getPasswordWSSH1() {
		return passwordWSSH1;
	}

	public void setPasswordWSSH1(String passwordWSSH1) {
		this.passwordWSSH1 = passwordWSSH1;
	}

	public String getPasswordWSSH2() {
		return passwordWSSH2;
	}

	public void setPasswordWSSH2(String passwordWSSH2) {
		this.passwordWSSH2 = passwordWSSH2;
	}

	public String getRegexText() {
		return regexText;
	}

	public void setRegexText(String regexText) {
		this.regexText = regexText;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	public List<Role> getRoleList() {
		
		try {
			//roleList = roleAction.findAll();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}
	
	

}
