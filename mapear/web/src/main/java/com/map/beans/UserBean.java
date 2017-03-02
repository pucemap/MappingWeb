package com.map.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

//
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;


import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.map.entities.Coordinates;
import com.map.entities.CoordinatesRestructured;
import com.map.entities.Message;
import com.map.entities.Role;
import com.map.entities.User;
import com.map.entities.UserRole;
import com.map.resources.CodeGenerator;
import com.map.services.CoordinatesEjb;
import com.map.services.CoordinatesRestructuredEjb;
import com.map.services.MessageEjb;
import com.map.services.RoleEjb;
import com.map.services.UserEjb;
import com.map.services.UserRoleEjb;
import com.map.utils.SHA;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;


@ManagedBean(name = "userBean")
@SessionScoped
public class UserBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7927820995066852655L;
	/**
	 * 
	 */
	
	private User user = new User();
	private User userFirstTime = new User();
	private User userSearch = new User();
	private String passwordWSSH1 = new String();
	private SHA sha = new SHA();
	private Calendar cal = Calendar.getInstance();
	private UserRole userRole = new UserRole();
	private ChatBean thereIsTimer = new ChatBean();
	static ExternalContext ec;
	private String regexText = new String();
	// private int hours =0;
	// private int minutes=0;
	private int seconds = 0;
	private List<Role> roleList = new ArrayList<Role>();
	private List<User> userList = new ArrayList<User>();
	private String password = new String();
	private String confirmPassword = new String();
	private String connectedUserRole = new String();
	private UploadedFile photoImage;

	private Boolean isFirstTime = new Boolean(Boolean.TRUE);
	private Boolean isNew = new Boolean(Boolean.TRUE);

	@EJB
	UserEjb userAction;

	@EJB
	RoleEjb roleAction;

	@EJB
	UserRoleEjb userRoleAction;

	@EJB
	MessageEjb messageAction;

	@EJB
	CoordinatesEjb coordinatesAction;

	@EJB
	CoordinatesRestructuredEjb coordinatesResAction;

	@PostConstruct
	public void init() {
		try {
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			
			userFirstTime = userAction.findByName(ec.getUserPrincipal().getName());
			isFirstTime = userFirstTime.isUsrFirstTime();
			userList = userAction.findAll();
			userRole = userRoleAction.findByUser(userFirstTime);
			connectedUserRole = userRole.getRole().getRolName();
			if (isFirstTime) {
				RequestContext.getCurrentInstance().update("userForm");
				RequestContext.getCurrentInstance().update("confirmationPasswordForm");
				RequestContext.getCurrentInstance().update("notificationForm");
				RequestContext.getCurrentInstance().update("layoutForm");
				System.out.println(isFirstTime + "userBean");
			}			
			RequestContext.getCurrentInstance().update("userForm:userTable");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		userRole = new UserRole();
		

	}

	
	public String generateRandomColor(){
		Random r = new Random();		
        StringBuffer sb = new StringBuffer();
        while(sb.length() < 6){
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.toString().substring(0, 6);
        
	}

	public void confirmationPassword() {
		if (password.compareTo(confirmPassword) == 0) {
			userFirstTime.setUsrPassword(sha.encrypt(confirmPassword));
			userFirstTime.setUsrFirstTime(false);
			isFirstTime = false;
			try {
				userAction.merge(userFirstTime);
				RequestContext.getCurrentInstance().update("userForm");
				RequestContext.getCurrentInstance().update("confirmationPasswordForm");				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
	public void handleFileUpload(FileUploadEvent event){
		if(event.getFile() != null){
			photoImage = event.getFile();			
		}		
	}
	
	public void test(){
		if(photoImage != null){
			System.out.println(photoImage.getSize());
		}
	}

	public void save() {
		if(photoImage!=null){
			user.setUsrPhoto(photoImage.getContents());			
		}
		user.setUsrTimerFrequency(seconds);
		if (isNew) {
			try {
				if (userRole.getRole().getRolName().equals("Invitado") || userRole.getRole().getRolName().equals("Usuario"))
					user.setUsrRegistrationCode(CodeGenerator.generate());
				user.setUsrFirstTime(true);				
				user.setUsrPassword(sha.encrypt(user.getUsrIdentificationNumber()));
				user.setUsrState("C");
				user.setUsrCreationDate(cal.getTime());
				user.setUsrColor("#" + generateRandomColor());
				user.setUsrSecondaryColor("#" + generateRandomColor());				
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
		// PIRUJA
		else {
			try {
				userAction.merge(user);
				userRole.setUser(user);
				userRole.setUsrRolCreationDate(cal.getTime());
				userRole.setUsrRolState("U");
				userRoleAction.merge(userRole);
				isNew = Boolean.TRUE;
				ec = FacesContext.getCurrentInstance().getExternalContext();
				User webUser = userAction.findByName(ec.getUserPrincipal().getName());
				thereIsTimer.sendTimerMessage(user, webUser);
				user = new User();
				userRole = new UserRole();
				RequestContext.getCurrentInstance().update("userForm:insertDialog");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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

	// public boolean verifyPassword(){
	// if(passwordWSSH1.isEmpty()||!passwordWSSH1.equals(passwordWSSH2)){
	// RequestContext.getCurrentInstance().update("userForm:insertDialog");
	// return false;
	// }
	// else
	// return true;
	// }

	public void search() {
		try {
			userList = userAction.findByUserName(userSearch);
			RequestContext.getCurrentInstance().update("userForm:userTable");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void delete(User usr) {
		List<Message> messageList = new ArrayList<Message>();
		List<Coordinates> coordinatesList = new ArrayList<Coordinates>();
		List<CoordinatesRestructured> coorResList = new ArrayList<CoordinatesRestructured>();

		UserRole usrole = new UserRole();
		try {
			usrole = userRoleAction.findByUserNickname(usr);

			messageList = messageAction.getMessagesByUser(usr);
			for (Message aux : messageList)
				messageAction.remove(aux);

			coordinatesList = coordinatesAction.getCoordinatesByUser(usr);
			for (Coordinates aux : coordinatesList)
				coordinatesAction.remove(aux);

			coorResList = coordinatesResAction.getCoordinatesRestructured(usr);
			for (CoordinatesRestructured aux : coorResList)
				coordinatesResAction.remove(aux);

			userRoleAction.remove(usrole);
			userAction.remove(usr);
			userList = userAction.findAll();
			RequestContext.getCurrentInstance().update("userForm:userTable");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void clean() {
		userList = new ArrayList<User>();
		RequestContext.getCurrentInstance().update("userForm:userTable");
	}

	public void edit(User usr) {
		isNew = Boolean.FALSE;
		user = new User();
		user = usr;
		userRole = userRoleAction.findByUserNickname(usr);
		seconds = usr.getUsrTimerFrequency();

		passwordWSSH1 = new String();
		passwordWSSH1 = new String();
		RequestContext.getCurrentInstance().update("userForm:insertDialog");
		RequestContext.getCurrentInstance().execute("PF('dlg2').show();");
	}
	
	public void resetPassword(User usr){	
		
		FacesContext context = FacesContext.getCurrentInstance();
		
		usr.setUsrPassword(sha.encrypt(usr.getUsrIdentificationNumber()));
		usr.setUsrFirstTime(true);
		try {
			userAction.merge(usr);
			context.addMessage(null, new FacesMessage("Administrador:","Contraseña Reestablecida" ) );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			context.addMessage(null, new FacesMessage("Administrador:","Error al reestablecer contraseña" ) );
			e.printStackTrace();
		}
		
		
		 
		
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

	// public String getPasswordWSSH2() {
	// return passwordWSSH2;
	// }
	//
	// public void setPasswordWSSH2(String passwordWSSH2) {
	// this.passwordWSSH2 = passwordWSSH2;
	// }

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
			roleList = roleAction.findAll();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	// public int getMinutes() {
	// return minutes;
	// }
	//
	// public void setMinutes(int minutes) {
	// this.minutes = minutes;
	// }

	public int getSeconds() {
		return seconds;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}

	public Boolean getIsFirstTime() {
		return isFirstTime;
	}

	public void setIsFirstTime(Boolean isFirstTime) {
		this.isFirstTime = isFirstTime;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getConnectedUserRole() {
		return connectedUserRole;
	}

	public void setConnectedUserRole(String connectedUserRole) {
		this.connectedUserRole = connectedUserRole;
	}


	public UploadedFile getPhotoImage() {
		return photoImage;
	}


	public void setPhotoImage(UploadedFile photoImage) {
		this.photoImage = photoImage;
	}

	// public int getHours() {
	// return hours;
	// }
	//
	// public void setHours(int hours) {
	// this.hours = hours;
	// }

}
