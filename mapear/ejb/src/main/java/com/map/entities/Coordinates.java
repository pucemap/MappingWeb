package com.map.entities;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;


/**
 * The persistent class for the coordinates database table.
 * 
 */
@Entity
@Table(name="coordinates")
public class Coordinates implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="COO_ID")
	private int cooId;

	@Column(name="COO_LATITUDE")
	private BigDecimal cooLatitude;

	@Column(name="COO_LONGITUDE")
	private BigDecimal cooLongitude;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="COO_DATE_CREATION")
	private Date cooCreationDate;
	
	@Column(name="COO_TRANSFORMED_LATITUDE")
	private BigDecimal cooTransformedLatitude;
	
	@Column(name="COO_TRANSFORMED_LONGITUDE")
	private BigDecimal cooTransformedLongitude;
	
	@Column(name="COO_STATE")
	private int cooState;
	
	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="USR_ID")
	private User user;

	public Coordinates() {
	}

	
	public int getCooId() {
		return cooId;
	}



	public void setCooId(int cooId) {
		this.cooId = cooId;
	}



	public BigDecimal getCooLatitude() {
		return cooLatitude;
	}



	public void setCooLatitude(BigDecimal cooLatitude) {
		this.cooLatitude = cooLatitude;
	}



	public BigDecimal getCooLongitude() {
		return cooLongitude;
	}



	public void setCooLongitude(BigDecimal cooLongitude) {
		this.cooLongitude = cooLongitude;
	}



	public Date getCooCreationDate() {
		return cooCreationDate;
	}



	public void setCooCreationDate(Date cooCreationDate) {
		this.cooCreationDate = cooCreationDate;
	}



	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}


	public BigDecimal getCooTransformedLatitude() {
		return cooTransformedLatitude;
	}


	public void setCooTransformedLatitude(BigDecimal cooTransformedLatitude) {
		this.cooTransformedLatitude = cooTransformedLatitude;
	}


	public BigDecimal getCooTransformedLongitude() {
		return cooTransformedLongitude;
	}


	public void setCooTransformedLongitude(BigDecimal cooTransformedLongitude) {
		this.cooTransformedLongitude = cooTransformedLongitude;
	}


	public int getCooState() {
		return cooState;
	}


	public void setCooState(int cooState) {
		this.cooState = cooState;
	}
}
