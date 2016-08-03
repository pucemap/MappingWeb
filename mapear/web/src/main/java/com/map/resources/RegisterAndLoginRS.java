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

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.map.entities.User;
import com.map.services.UserEjb;

@RequestScoped
@Path("")
@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })
public class RegisterAndLoginRS {

	private List<User> lista = new ArrayList<User>();
	
	@EJB
	UserEjb userAction;
	
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
	public List<User> getUsers() {
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
	public String registerWithToken(String json) {
		String text = json.replace("\"", "\\\"");
		text = text.replace("{", "\"{");
		text = text.replace("}", "}\"");
		JSONObject jsonObj;
		System.out.println(text);
		try {
			System.out.println(text);
			//jsonObj =(JSONObject)new JSONParser().parse("{\"nickname\":\"quesfg\",\"name\":\"si\",\"lastname\":\"si\",\"email\":\"shhhhh\",\"password\":\"ñghgg\",\"registerCode\":\"1250\",\"token\":\"fD5j3OnzjMo:APA91bE2Dvz_lniKfjeWVF2S4IA9ZadfRQwKdkYUOWC-YtcW8XwQiQYdfNLa8Dm5kCm77znXMo8H15I-l2GK-u-hkm_KSkiBTW-iq8yvrZTQbw3FAjgAk1A6gZhioh4Zx19tVpaUIbYx\"}");
			jsonObj =(JSONObject)new JSONParser().parse(json);
			if(userAction.findByTokenAndEmail(jsonObj.get("email").toString(),jsonObj.get("token").toString())!=null)
				return "{\"registration:\"\"true\"}";
		
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return "{\"registration:\"\"false\"}";
	}
	
	
	
}
