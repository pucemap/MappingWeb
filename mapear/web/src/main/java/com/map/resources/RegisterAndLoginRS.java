package com.map.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.map.beans.ChatBean;
import com.map.entities.Geolocalization;
import com.map.entities.User;
import com.map.services.GeolocalizationEjb;
import com.map.services.UserEjb;
import com.map.utils.SHA;

@RequestScoped
@Path("")
@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })
public class RegisterAndLoginRS {

	private List<User> lista = new ArrayList<User>();
	private SHA sha = new SHA();
	
	@EJB
	UserEjb userAction;
	
	@EJB
	GeolocalizationEjb geoAction;
	
	@GET
	@Path("/test1")
	@Produces("application/json")
	public List<User> getTest1() {
		try {
			lista = userAction.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}
	
	@POST
	@Path("/getUsers")
	@Produces("application/json")
	public List<User> getUsers(String json) {
		System.out.println("entro getUsers");
		try {
			lista = userAction.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return lista;
	}
	

		
	
	@POST
	@Path("/registerWithToken")
	@Produces("application/json")	
	public User registerWithToken(String json) {
		System.out.println("entro reg: "+json);
		String text = json.replace("\"", "\\\"");
		text = text.replace("{", "\"{");
		text = text.replace("}", "}\"");
		JSONObject jsonObj;
		User usr = new User();
		//ChatBean chatBean = new ChatBean();
		
		try {
			
			//jsonObj =(JSONObject)new JSONParser().parse("{\"nickname\":\"quesfg\",\"name\":\"si\",\"lastname\":\"si\",\"email\":\"shhhhh\",\"password\":\"ñghgg\",\"registerCode\":\"1250\",\"token\":\"fD5j3OnzjMo:APA91bE2Dvz_lniKfjeWVF2S4IA9ZadfRQwKdkYUOWC-YtcW8XwQiQYdfNLa8Dm5kCm77znXMo8H15I-l2GK-u-hkm_KSkiBTW-iq8yvrZTQbw3FAjgAk1A6gZhioh4Zx19tVpaUIbYx\"}");
			System.out.println("entro "+json);
			jsonObj =(JSONObject)new JSONParser().parse(json);				
			
			System.out.println(jsonObj.get("nickname").toString());
			System.out.println(jsonObj.get("password").toString());
			
			usr = userAction.findByPasswordAndNickname(jsonObj.get("nickname").toString(), sha.encrypt(jsonObj.get("password").toString()));
			System.out.println("sdasd"+usr.getUsrId());
			if(usr.getUsrId()!=0){
				
				try {
					usr.setUsrToken(jsonObj.get("token").toString());
					usr.setUsrSmartphoneType(jsonObj.get("device").toString());
					userAction.merge(usr);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//chatBean.broadcastMessage(usr, "updateContacts");
				//return "{\"registration\":\"true\"}";	
				return usr;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return usr;
	}
	
	
	
	
	@POST
	@Path("/resgisterPassword")
	@Produces("application/json")	
	public String registerPassword(String json) {
		SHA sha = new SHA();
		System.out.println("entro reg: "+json);
		String text = json.replace("\"", "\\\"");
		text = text.replace("{", "\"{");
		text = text.replace("}", "}\"");
		JSONObject jsonObj;
		User usr = new User();
		
		try {		
			
			jsonObj =(JSONObject)new JSONParser().parse(json);			
			usr = userAction.findByName(jsonObj.get("nickname").toString());
			usr.setUsrPassword(sha.encrypt(jsonObj.get("password").toString()));
			usr.setUsrFirstTime(false);
			System.out.println("sdasd"+usr.getUsrId());
			if(usr.getUsrId()!=0){
				
				try {					
					userAction.merge(usr);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "{\"response\":\"true\"}";	
				
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return "{\"response\":\"false\"}";	
	}
	
	
	
	
	
	@POST
	@Path("/getPushPinsbyUser")
	@Produces("application/json")
	public List<Geolocalization> getPushPinUser(String json) {
		List<Geolocalization> listGeo = new ArrayList<Geolocalization>();
		System.out.println("entro a geo");
		String text = json.replace("\"", "\\\"");
		text = text.replace("{", "\"{");
		text = text.replace("}", "}\"");
		JSONObject jsonObj;
		
		try {
			
			//jsonObj =(JSONObject)new JSONParser().parse("{\"nickname\":\"quesfg\",\"name\":\"si\",\"lastname\":\"si\",\"email\":\"shhhhh\",\"password\":\"ñghgg\",\"registerCode\":\"1250\",\"token\":\"fD5j3OnzjMo:APA91bE2Dvz_lniKfjeWVF2S4IA9ZadfRQwKdkYUOWC-YtcW8XwQiQYdfNLa8Dm5kCm77znXMo8H15I-l2GK-u-hkm_KSkiBTW-iq8yvrZTQbw3FAjgAk1A6gZhioh4Zx19tVpaUIbYx\"}");
			
			jsonObj =(JSONObject)new JSONParser().parse(json);					
			listGeo = geoAction.findByUsrGeo(jsonObj.get("nickname").toString());
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		
		
		return listGeo;
	}
	
}
