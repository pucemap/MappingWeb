package com.map.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.context.RequestContext;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Polyline;

import com.google.maps.GeoApiContext;
import com.google.maps.RoadsApi;
import com.google.maps.GeocodingApi;
import com.google.maps.PendingResult;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.SnappedPoint;
import com.map.entities.Coordinates;
import com.map.entities.CoordinatesRestructured;
import com.map.entities.Geolocalization;
import com.map.entities.User;
import com.map.entities.UserRole;
import com.map.services.CoordinatesEjb;
import com.map.services.CoordinatesRestructuredEjb;
import com.map.services.GeolocalizationEjb;
import com.map.services.UserRoleEjb;

@ManagedBean(name = "geoBean")
@ViewScoped
public class GeoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8019054376020551686L;

	private User user = new User();

	private Geolocalization geo = new Geolocalization();
	private List<Geolocalization> geoList = new ArrayList<Geolocalization>();

	private List<User> userList = new ArrayList<User>();
	private List<User> userListCoordinates = new ArrayList<User>();
	private List<Coordinates> coorList = new ArrayList<Coordinates>();
	private List<CoordinatesRestructured> coorResList = new ArrayList<CoordinatesRestructured>();
	private MapModel emptyModel;
	private String title;
	private double lat;
	private double lng;
	private Polyline polylines = new Polyline();
	private Date date = new Date();
	private CoordinatesRestructured coorRes = new CoordinatesRestructured();

	@EJB
	GeolocalizationEjb geoAction;

	@EJB
	UserRoleEjb userRoleAction;

	@EJB
	CoordinatesEjb coorAction;

	@EJB
	CoordinatesRestructuredEjb coorResAction;

	@PostConstruct
	public void init() {
		emptyModel = new DefaultMapModel();		
		
		

		if (user.getUsrName() == null) {
			userListCoordinates = userRoleAction.findUsers();
			
			
			
			for(User aux : userListCoordinates){
				int count = coorAction.selectCountCoordinatesByUserDate(date,aux).size();								
				recursiveFunction(count,aux);		
			}
			
			for(User aux : userListCoordinates){
				
				drawPolylineByUser(aux);
			}
		
		}

	}
	
	
	
	public int recursiveFunction(int n ,User usr){
		
		if(n==0){
			return n;
		}else{
			if(n<=98){
				
				coorList = coorAction.findCoordinatesByDate(date,usr);
				restructureCoordinates(coorList,usr);
				return recursiveFunction(n-n,usr);
			}
			else{
				coorList = coorAction.findCoordinatesByDate(date,usr);
				restructureCoordinates(coorList,usr);
				return recursiveFunction(n-100,usr);
			}
		}
		
	}
	
	
	public void drawPolylineByUser(User usr){
//		emptyModel = new DefaultMapModel();
		Polyline polyline = new Polyline();
		coorResList = coorResAction.findCoordinatesByUser(date,usr);
		
		for(CoordinatesRestructured aux : coorResList){
			polyline.getPaths().add(new LatLng(aux.getCooResLatitude().doubleValue(), aux.getCooResLongitude().doubleValue()));			
		}
		polyline.setStrokeWeight(10);
		polyline.setStrokeColor(usr.getUsrSecondaryColor());
		polyline.setStrokeOpacity(0.7);
		emptyModel.addOverlay(polyline);
		
		List<Coordinates> lista = new ArrayList<Coordinates>();
		Polyline polyline2 = new Polyline();
		try {
			lista = coorAction.selectCoordinatesByUserDateReal(date,usr);
			for(Coordinates aux : lista){
				polyline2.getPaths().add(new LatLng(aux.getCooLatitude().doubleValue(), aux.getCooLongitude().doubleValue()));			
			}
			polyline2.setStrokeWeight(10);
			polyline2.setStrokeColor(usr.getUsrColor());
			polyline2.setStrokeOpacity(0.7);
			emptyModel.addOverlay(polyline2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RequestContext.getCurrentInstance().update("geoForm:gmap");
		
		
		
	}
	

	public void restructureCoordinates(List<Coordinates> coordinatesList,User usr) {
		GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyCBkWwk7936J35ACBf9-qjXGUb2MebfqAo");
		SnappedPoint[] results;
		List<com.google.maps.model.LatLng> googleCoord = new ArrayList<com.google.maps.model.LatLng>();
		Date date = new Date();
		
		CoordinatesRestructured cR = new CoordinatesRestructured();
		cR = coorResAction.maxCoordinatesByIdUser(date, usr);
		
		if(cR!=null){
		googleCoord.add(new com.google.maps.model.LatLng(cR.getCooResLatitude().doubleValue(),
				cR.getCooResLongitude().doubleValue()));
		}

		for (Coordinates aux : coordinatesList) {
			googleCoord.add(new com.google.maps.model.LatLng(aux.getCooLatitude().doubleValue(),
					aux.getCooLongitude().doubleValue()));
			aux.setCooState(1);
			date = aux.getCooCreationDate();
			try {
				coorAction.merge(aux);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			results = RoadsApi.snapToRoads(context, true,
					googleCoord.toArray(new com.google.maps.model.LatLng[googleCoord.size()])).await();

			for (SnappedPoint snappedPoint : results) {
				coorRes.setCooResLatitude(new BigDecimal(snappedPoint.location.lat));
				coorRes.setCooResLongitude(new BigDecimal(snappedPoint.location.lng));
				coorRes.setUser(usr);
				coorRes.setCooResCreationDate(date);
				coorResAction.persist(coorRes);
				coorRes = new CoordinatesRestructured();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		date = new Date();

	}

	public void search() {
		
		emptyModel = new DefaultMapModel();	 
		
		if (user == null) {
			
			userListCoordinates = userRoleAction.findUsers();
			
			for(User aux : userListCoordinates){
				System.out.println("entro");
				int count = coorAction.selectCountCoordinatesByUserDate(date,aux).size();								
				recursiveFunction(count,aux);		
			}
			
			for(User aux : userListCoordinates){
				drawPolylineByUser(aux);
			}
		}
		else{
			emptyModel = new DefaultMapModel();
			int count = coorAction.selectCountCoordinatesByUserDate(date,user).size();								
			recursiveFunction(count,user);	
			drawPolylineByUser(user);			
		}

	}

	public void onPolylineSelect(OverlaySelectEvent event) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Polyline Selected", null));
	}

	public MapModel getemptyModel() {
		return emptyModel;
	}

	public void setemptyModel(MapModel emptyModel) {
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

		if (savePosition(lat, lng)) {
			Marker marker = new Marker(new LatLng(lat, lng), title);
			emptyModel.addOverlay(marker);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Marker Added", "Lat:" + lat + ", Lng:" + lng));
		}
	}

	public Boolean savePosition(double latitude, double longitude) {
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<CoordinatesRestructured> getCoorResList() {
		return coorResList;
	}

	public void setCoorResList(List<CoordinatesRestructured> coorResList) {
		this.coorResList = coorResList;
	}

	public CoordinatesRestructured getCoorRes() {
		return coorRes;
	}

	public void setCoorRes(CoordinatesRestructured coorRes) {
		this.coorRes = coorRes;
	}

}
