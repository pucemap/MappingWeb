package com.map.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the geolocalization database table.
 * 
 */
@Entity
@NamedQuery(name="Geolocalization.findAll", query="SELECT g FROM Geolocalization g")
public class Geolocalization implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="GEO_ID")
	private int geoId;

	@Column(name="GEO_LATITUDE")
	private BigDecimal geoLatitude;

	@Column(name="GEO_LENGHT")
	private BigDecimal geoLenght;

	//bi-directional many-to-one association to Form
	@OneToMany(mappedBy="geolocalization")
	private List<Form> forms;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="USR_ID")
	private User user;

	public Geolocalization() {
	}

	public int getGeoId() {
		return this.geoId;
	}

	public void setGeoId(int geoId) {
		this.geoId = geoId;
	}

	public BigDecimal getGeoLatitude() {
		return this.geoLatitude;
	}

	public void setGeoLatitude(BigDecimal geoLatitude) {
		this.geoLatitude = geoLatitude;
	}

	public BigDecimal getGeoLenght() {
		return this.geoLenght;
	}

	public void setGeoLenght(BigDecimal geoLenght) {
		this.geoLenght = geoLenght;
	}

	public List<Form> getForms() {
		return this.forms;
	}

	public void setForms(List<Form> forms) {
		this.forms = forms;
	}

	public Form addForm(Form form) {
		getForms().add(form);
		form.setGeolocalization(this);

		return form;
	}

	public Form removeForm(Form form) {
		getForms().remove(form);
		form.setGeolocalization(null);

		return form;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}