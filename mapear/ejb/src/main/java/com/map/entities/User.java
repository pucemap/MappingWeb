package com.map.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@Table(name="user")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="USR_ID")
	private int usrId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="USR_CREATION_DATE")
	private Date usrCreationDate;

	@Column(name="USR_EMAIL")
	private String usrEmail;

	@Column(name="USR_IS_LOG")
	private String usrIsLog;

	@Column(name="USR_LASTNAME")
	private String usrLastname;

	@Column(name="USR_NAME")
	private String usrName;

	@Column(name="USR_NICKNAME")
	private String usrNickname;

	@Column(name="USR_PASSWORD")
	private String usrPassword;

	@Lob
	@Column(name="USR_PHOTO")
	private byte[] usrPhoto;

	@Column(name="USR_REGISTRATION_CODE")
	private BigDecimal usrRegistrationCode;

	@Column(name="USR_STATE")
	private String usrState;

	@Column(name="USR_TOKEN")
	private String usrToken;

	//bi-directional many-to-one association to Geolocalization
	@OneToMany(mappedBy="user")
	private List<Geolocalization> geolocalizations;

	//bi-directional many-to-one association to Group
	@OneToMany(mappedBy="user1")
	private List<Group> groups1;

	//bi-directional many-to-one association to Group
	@OneToMany(mappedBy="user2")
	private List<Group> groups2;

	//bi-directional many-to-one association to Message
	@OneToMany(mappedBy="user1")
	private List<Message> messages1;

	//bi-directional many-to-one association to Message
	@OneToMany(mappedBy="user2")
	private List<Message> messages2;

	//bi-directional many-to-one association to Enterprise
	@ManyToOne
	@JoinColumn(name="ENT_ID")
	private Enterprise enterprise;

	//bi-directional many-to-one association to UserRole
	@OneToMany(mappedBy="user")
	private List<UserRole> userRoles;

	public User() {
	}

	public int getUsrId() {
		return this.usrId;
	}

	public void setUsrId(int usrId) {
		this.usrId = usrId;
	}

	public Date getUsrCreationDate() {
		return this.usrCreationDate;
	}

	public void setUsrCreationDate(Date usrCreationDate) {
		this.usrCreationDate = usrCreationDate;
	}

	public String getUsrEmail() {
		return this.usrEmail;
	}

	public void setUsrEmail(String usrEmail) {
		this.usrEmail = usrEmail;
	}

	public String getUsrIsLog() {
		return this.usrIsLog;
	}

	public void setUsrIsLog(String usrIsLog) {
		this.usrIsLog = usrIsLog;
	}

	public String getUsrLastname() {
		return this.usrLastname;
	}

	public void setUsrLastname(String usrLastname) {
		this.usrLastname = usrLastname;
	}

	public String getUsrName() {
		return this.usrName;
	}

	public void setUsrName(String usrName) {
		this.usrName = usrName;
	}

	public String getUsrNickname() {
		return this.usrNickname;
	}

	public void setUsrNickname(String usrNickname) {
		this.usrNickname = usrNickname;
	}

	public String getUsrPassword() {
		return this.usrPassword;
	}

	public void setUsrPassword(String usrPassword) {
		this.usrPassword = usrPassword;
	}

	public byte[] getUsrPhoto() {
		return this.usrPhoto;
	}

	public void setUsrPhoto(byte[] usrPhoto) {
		this.usrPhoto = usrPhoto;
	}

	public BigDecimal getUsrRegistrationCode() {
		return this.usrRegistrationCode;
	}

	public void setUsrRegistrationCode(BigDecimal usrRegistrationCode) {
		this.usrRegistrationCode = usrRegistrationCode;
	}

	public String getUsrState() {
		return this.usrState;
	}

	public void setUsrState(String usrState) {
		this.usrState = usrState;
	}

	public String getUsrToken() {
		return this.usrToken;
	}

	public void setUsrToken(String usrToken) {
		this.usrToken = usrToken;
	}

	public List<Geolocalization> getGeolocalizations() {
		return this.geolocalizations;
	}

	public void setGeolocalizations(List<Geolocalization> geolocalizations) {
		this.geolocalizations = geolocalizations;
	}

	public Geolocalization addGeolocalization(Geolocalization geolocalization) {
		getGeolocalizations().add(geolocalization);
		geolocalization.setUser(this);

		return geolocalization;
	}

	public Geolocalization removeGeolocalization(Geolocalization geolocalization) {
		getGeolocalizations().remove(geolocalization);
		geolocalization.setUser(null);

		return geolocalization;
	}

	public List<Group> getGroups1() {
		return this.groups1;
	}

	public void setGroups1(List<Group> groups1) {
		this.groups1 = groups1;
	}

	public Group addGroups1(Group groups1) {
		getGroups1().add(groups1);
		groups1.setUser1(this);

		return groups1;
	}

	public Group removeGroups1(Group groups1) {
		getGroups1().remove(groups1);
		groups1.setUser1(null);

		return groups1;
	}

	public List<Group> getGroups2() {
		return this.groups2;
	}

	public void setGroups2(List<Group> groups2) {
		this.groups2 = groups2;
	}

	public Group addGroups2(Group groups2) {
		getGroups2().add(groups2);
		groups2.setUser2(this);

		return groups2;
	}

	public Group removeGroups2(Group groups2) {
		getGroups2().remove(groups2);
		groups2.setUser2(null);

		return groups2;
	}

	public List<Message> getMessages1() {
		return this.messages1;
	}

	public void setMessages1(List<Message> messages1) {
		this.messages1 = messages1;
	}

	public Message addMessages1(Message messages1) {
		getMessages1().add(messages1);
		messages1.setUser1(this);

		return messages1;
	}

	public Message removeMessages1(Message messages1) {
		getMessages1().remove(messages1);
		messages1.setUser1(null);

		return messages1;
	}

	public List<Message> getMessages2() {
		return this.messages2;
	}

	public void setMessages2(List<Message> messages2) {
		this.messages2 = messages2;
	}

	public Message addMessages2(Message messages2) {
		getMessages2().add(messages2);
		messages2.setUser2(this);

		return messages2;
	}

	public Message removeMessages2(Message messages2) {
		getMessages2().remove(messages2);
		messages2.setUser2(null);

		return messages2;
	}

	public Enterprise getEnterprise() {
		return this.enterprise;
	}

	public void setEnterprise(Enterprise enterprise) {
		this.enterprise = enterprise;
	}

	public List<UserRole> getUserRoles() {
		return this.userRoles;
	}

	public void setUserRoles(List<UserRole> userRoles) {
		this.userRoles = userRoles;
	}

	public UserRole addUserRole(UserRole userRole) {
		getUserRoles().add(userRole);
		userRole.setUser(this);

		return userRole;
	}

	public UserRole removeUserRole(UserRole userRole) {
		getUserRoles().remove(userRole);
		userRole.setUser(null);

		return userRole;
	}

}