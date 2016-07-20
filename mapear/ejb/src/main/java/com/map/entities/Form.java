package com.map.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the form database table.
 * 
 */
@Entity
@Table(name="form")
public class Form implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="FRM_ID")
	private int frmId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FRM_CREATION_STATE")
	private Date frmCreationState;

	@Column(name="FRM_DESCRIPTION")
	private String frmDescription;

	@Column(name="FRM_STATE")
	private String frmState;

	//bi-directional many-to-one association to Geolocalization
	@ManyToOne
	@JoinColumn(name="GEO_ID")
	private Geolocalization geolocalization;

	public Form() {
	}

	public int getFrmId() {
		return this.frmId;
	}

	public void setFrmId(int frmId) {
		this.frmId = frmId;
	}

	public Date getFrmCreationState() {
		return this.frmCreationState;
	}

	public void setFrmCreationState(Date frmCreationState) {
		this.frmCreationState = frmCreationState;
	}

	public String getFrmDescription() {
		return this.frmDescription;
	}

	public void setFrmDescription(String frmDescription) {
		this.frmDescription = frmDescription;
	}

	public String getFrmState() {
		return this.frmState;
	}

	public void setFrmState(String frmState) {
		this.frmState = frmState;
	}

	public Geolocalization getGeolocalization() {
		return this.geolocalization;
	}

	public void setGeolocalization(Geolocalization geolocalization) {
		this.geolocalization = geolocalization;
	}

}