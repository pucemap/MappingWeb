package com.map.services;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.map.entities.Geolocalization;
import com.map.utils.GenericDAOImpl;

@Stateless
@LocalBean
public class GeolocalizationEjb extends GenericDAOImpl<Geolocalization, Integer>{

	public GeolocalizationEjb(){
		
	}
	
	public List<Geolocalization> findByUsrGeo(String nickname){		
		System.out.println("nickname: "+nickname);
		List<Geolocalization> list = new ArrayList<Geolocalization>();
		String query = "SELECT u FROM Geolocalization u where u.user.usrNickname like '%"+nickname+"%'";
		try {
			list=find(query);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
}
