package com.map.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the groups database table.
 * 
 */
@Entity
@Table(name="groups")
public class Group implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="GRP_ID")
	private int grpId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="GRP_CREATION_DATE")
	private Date grpCreationDate;

	@Column(name="GRP_DESCRIPTION")
	private String grpDescription;

	@Column(name="GRP_NAME")
	private String grpName;

	@Column(name="GRP_STATE")
	private String grpState;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="USR_ID")
	private User user1;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="ENT_ID")
	private User user2;

	public Group() {
	}

	public int getGrpId() {
		return this.grpId;
	}

	public void setGrpId(int grpId) {
		this.grpId = grpId;
	}

	public Date getGrpCreationDate() {
		return this.grpCreationDate;
	}

	public void setGrpCreationDate(Date grpCreationDate) {
		this.grpCreationDate = grpCreationDate;
	}

	public String getGrpDescription() {
		return this.grpDescription;
	}

	public void setGrpDescription(String grpDescription) {
		this.grpDescription = grpDescription;
	}

	public String getGrpName() {
		return this.grpName;
	}

	public void setGrpName(String grpName) {
		this.grpName = grpName;
	}

	public String getGrpState() {
		return this.grpState;
	}

	public void setGrpState(String grpState) {
		this.grpState = grpState;
	}

	public User getUser1() {
		return this.user1;
	}

	public void setUser1(User user1) {
		this.user1 = user1;
	}

	public User getUser2() {
		return this.user2;
	}

	public void setUser2(User user2) {
		this.user2 = user2;
	}

}