package com.map.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="coordinates_restructured")
public class CoordinatesRestructured implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 948672507771476536L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="COO_RES_ID")
	private int cooResId;

	@Column(name="COO_RES_LATITUDE")
	private BigDecimal cooResLatitude;

	@Column(name="COO_RES_LONGITUDE")
	private BigDecimal cooResLongitude;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="COO_RES_DATE_CREATION")
	private Date cooResCreationDate;
	
	@Column(name="COO_RES_STATE")
	private String cooResState;
	
	
	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="USR_ID")
	private User user;

	public CoordinatesRestructured() {
	}

	public int getCooResId() {
		return cooResId;
	}

	public void setCooResId(int cooResId) {
		this.cooResId = cooResId;
	}

	public BigDecimal getCooResLatitude() {
		return cooResLatitude;
	}

	public void setCooResLatitude(BigDecimal cooResLatitude) {
		this.cooResLatitude = cooResLatitude;
	}

	public BigDecimal getCooResLongitude() {
		return cooResLongitude;
	}

	public void setCooResLongitude(BigDecimal cooResLongitude) {
		this.cooResLongitude = cooResLongitude;
	}

	public Date getCooResCreationDate() {
		return cooResCreationDate;
	}

	public void setCooResCreationDate(Date cooResCreationDate) {
		this.cooResCreationDate = cooResCreationDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getCooResState() {
		return cooResState;
	}

	public void setCooResState(String cooResState) {
		this.cooResState = cooResState;
	}



}
