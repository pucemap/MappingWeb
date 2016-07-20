package com.map.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the message database table.
 * 
 */
@Entity
@Table(name="message")
public class Message implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="MES_ID")
	private int mesId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MES_RECEIVED_DATE")
	private Date mesReceivedDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MES_SEND_DATE")
	private Date mesSendDate;

	@Column(name="MES_STATE")
	private String mesState;

	@Column(name="MES_TEXT")
	private String mesText;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="USR_ID_RECEIVER")
	private User user1;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="USR_ID_SENDER")
	private User user2;

	public Message() {
	}

	public int getMesId() {
		return this.mesId;
	}

	public void setMesId(int mesId) {
		this.mesId = mesId;
	}

	public Date getMesReceivedDate() {
		return this.mesReceivedDate;
	}

	public void setMesReceivedDate(Date mesReceivedDate) {
		this.mesReceivedDate = mesReceivedDate;
	}

	public Date getMesSendDate() {
		return this.mesSendDate;
	}

	public void setMesSendDate(Date mesSendDate) {
		this.mesSendDate = mesSendDate;
	}

	public String getMesState() {
		return this.mesState;
	}

	public void setMesState(String mesState) {
		this.mesState = mesState;
	}

	public String getMesText() {
		return this.mesText;
	}

	public void setMesText(String mesText) {
		this.mesText = mesText;
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