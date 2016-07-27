package com.map.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import com.map.entities.Geolocalization;
import com.map.entities.User;
import com.map.services.GeolocalizationEjb;
import com.map.services.UserRoleEjb;

@ManagedBean(name="geoBean")
@ViewScoped
public class GeoBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8019054376020551686L;
	
	private User user = new User();
	
	private Geolocalization geo = new Geolocalization();
	private List<Geolocalization> geoList = new ArrayList<Geolocalization>();
	private List<User> userList = new ArrayList<User>();
	

	private MapModel emptyModel = new DefaultMapModel();    
    private String title;      
    private double lat;      
    private double lng;
    
    @EJB
    GeolocalizationEjb geoAction;
    
    @EJB
    UserRoleEjb userRoleAction;
    
    
    @PostConstruct
    public void init() {
        emptyModel = new DefaultMapModel();
        try {
			geoList = geoAction.findAll();
			for (Geolocalization aux : geoList) {
				//LatLng coord1 = new LatLng(aux.getGeoLatitude().doubleValue(),aux.getGeoLenght().doubleValue());
				
				emptyModel.addOverlay(new Marker(new LatLng(aux.getGeoLatitude().doubleValue(),aux.getGeoLenght().doubleValue()),aux.getUser().getUsrName()));
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	public MapModel getEmptyModel() {
		return emptyModel;
	}

	public void setEmptyModel(MapModel emptyModel) {
		this.emptyModel = emptyModel;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Geolocalization getGeo() {
		return geo;
	}

	public void setGeo(Geolocalization geo) {
		this.geo = geo;
	}

	public List<Geolocalization> getGeoList() {
		return geoList;
	}

	public void setGeoList(List<Geolocalization> geoList) {
		this.geoList = geoList;
	}

	

	public List<User> getUserList() {
		userList = userRoleAction.findUsers();
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public void addMarker() {
		
		if(savePosition(lat,lng)){
	        Marker marker = new Marker(new LatLng(lat, lng), title);
	        emptyModel.addOverlay(marker);
	        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Marker Added", "Lat:" + lat + ", Lng:" + lng));
		}
    }
	
	public Boolean savePosition(double latitude, double longitude){
		Boolean flag = new Boolean(Boolean.TRUE);
		
		geo.setGeoLatitude(BigDecimal.valueOf(latitude));
		geo.setGeoLenght(BigDecimal.valueOf(longitude));
		
		try {
			geoAction.persist(geo);
			geo = new Geolocalization();
		} catch (Exception e) {
			flag = Boolean.FALSE;
			e.printStackTrace();
			return flag;
		}
		
		return flag;		
		
	}


	
	
}
