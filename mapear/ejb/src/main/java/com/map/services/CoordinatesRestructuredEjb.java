package com.map.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.map.entities.CoordinatesRestructured;
import com.map.entities.User;
import com.map.utils.GenericDAOImpl;

@Stateless
@LocalBean
public class CoordinatesRestructuredEjb extends GenericDAOImpl<CoordinatesRestructured, Integer>{

	public CoordinatesRestructuredEjb(){
		
	}
	
	public CoordinatesRestructured maxCoordinatesByIdUser(Date date, User usr){
		List<CoordinatesRestructured> listCoordinates = new ArrayList<CoordinatesRestructured>();
		CoordinatesRestructured coor = new CoordinatesRestructured();
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
		String a = formatDate.format(date);
		
		String query = "SELECT max(c) FROM CoordinatesRestructured c WHERE c.user.usrId = "+usr.getUsrId()+" and c.cooResCreationDate between '"+formatDate.format(date)+" 00:00:00' and '"+formatDate.format(date)+" 23:59:59'"+" ORDER BY c.cooResId asc";
		try {
			listCoordinates = find(query);
			System.out.println("entro");
			
			for(CoordinatesRestructured aux : listCoordinates){
				coor = aux;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return coor;
	}
	
	public List<CoordinatesRestructured> findCoordinatesByUser(Date date,User usr ){
		List<CoordinatesRestructured> listCoordinates = new ArrayList<CoordinatesRestructured>();
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
		String a = formatDate.format(date);
	
		System.out.println(a);
		String query = "SELECT c FROM CoordinatesRestructured c WHERE c.user.usrId = "+usr.getUsrId()+" and c.cooResCreationDate between '"+formatDate.format(date)+" 00:00:00' and '"+formatDate.format(date)+" 23:59:59'"+" ORDER BY c.cooResId asc";
		try {
			listCoordinates = find(query);
		

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return listCoordinates;
		
	}
	
	public List<CoordinatesRestructured> getCoordinatesRestructured(User usr ){
		List<CoordinatesRestructured> listCoordinates = new ArrayList<CoordinatesRestructured>();
		
		
		String query = "SELECT c FROM CoordinatesRestructured c WHERE c.user.usrId = "+usr.getUsrId();
		try {
			listCoordinates = find(query);
		

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return listCoordinates;
		
	} 
}
