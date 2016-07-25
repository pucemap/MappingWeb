package com.map.beans;

import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.List;

import javax.annotation.PostConstruct;
//import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
//import javax.faces.bean.SessionScoped;
//import javax.faces.context.ExternalContext;
//import javax.faces.context.FacesContext;
import javax.faces.bean.SessionScoped;

//import org.primefaces.context.RequestContext;
//import org.primefaces.event.CloseEvent;

//import com.map.entities.Role;
//import com.map.entities.User;
//import com.map.entities.UserRole;
//import com.map.services.RoleEjb;
//import com.map.services.UserEjb;
//import com.map.services.UserRoleEjb;
//import com.map.utils.SHA;

import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

@ManagedBean(name="GeolocationBean")
@SessionScoped
public class GeolocationBean implements Serializable{


	private static final long serialVersionUID = -291697196322721844L;
	private MapModel simpleModel;
	  
	    @PostConstruct
	    public void init() {
	        simpleModel = new DefaultMapModel();
	          
	        //Shared coordinates
	        LatLng coord1 = new LatLng(-0.15316468116318321, -78.484752997756);
	        LatLng coord2 = new LatLng(-0.17668219221300427, -78.48150417208672);
	        LatLng coord3 = new LatLng(-0.2033324742003041, -78.49818550050259);
	        LatLng coord4 = new LatLng(-0.1637647306334853, -78.47426019608974);
	          
	        //Basic marker
	        simpleModel.addOverlay(new Marker(coord1, "Parque Bicentenario"));
	        simpleModel.addOverlay(new Marker(coord2, "Naciones Unidas"));
	        simpleModel.addOverlay(new Marker(coord3, "10 de agosto "));
	        simpleModel.addOverlay(new Marker(coord4, "Rio Coca"));
	    }
	  
	    public MapModel getSimpleModel() {
	        return simpleModel;
	    }
	
	

}
