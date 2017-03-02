package com.map.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.arjuna.ats.internal.jdbc.drivers.modifiers.list;
import com.map.entities.Coordinates;
import com.map.entities.User;
import com.map.utils.GenericDAOImpl;

@Stateless
@LocalBean
public class CoordinatesEjb extends GenericDAOImpl<Coordinates, Integer> {

	public CoordinatesEjb() {

	}

	public List<Coordinates> findAllOrderByTimeAndUserId() {
		List<Coordinates> information = new ArrayList<Coordinates>();
		//String query = "SELECT c FROM Coordinates c WHERE cooTransformedLatitude is not null";
		String query = "SELECT c FROM Coordinates c ORDER BY c.user, c.cooCreationDate asc";

		try {
			information = find(query);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return information;
	}

	/**
	 * @return
	 */
	public List<Coordinates> findLastCoordinate(int cod) {
		List<Coordinates> data = new ArrayList<Coordinates>();
		List<Coordinates> cord = new ArrayList<Coordinates>();
		String query = "SELECT c FROM Coordinates c WHERE c.user.usrId =" + cod + "ORDER BY c.cooCreationDate asc";
		try {
			data = find(query);
			
			if(data.size()==1){
				return data;
			}
			if(data.size()==2){
				return data;
			}
			if(data.size()==3){
				return data;
			}
			if(data.size()==4){
				return data;
			}
			if(data.size()==5){
				return data;
			}
			if(data.size()==6){
				return data;
			}
			if(data.size()>=7){
				Coordinates[] arrayCoor = data.toArray(new Coordinates[data.size()]);
				int a = data.size();
				cord.add(arrayCoor[a-7]);
				cord.add(arrayCoor[a-6]);
				cord.add(arrayCoor[a-5]);
				cord.add(arrayCoor[a-4]);
				cord.add(arrayCoor[a-3]);
				cord.add(arrayCoor[a-2]);
				cord.add(arrayCoor[a-1]);
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		return cord;
	}
	
	public List<Coordinates> selectCountCoordinatesByUserDate(Date date,User usr){
		List<Coordinates> listCoordinates = new ArrayList<Coordinates>();
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
		String a = formatDate.format(date);
		
		String query = "SELECT c FROM Coordinates c WHERE c.cooCreationDate between '"+formatDate.format(date)+" 00:00:00' and '"+formatDate.format(date)+" 23:59:59' and c.cooState = 0 and c.user.usrId = "+usr.getUsrId();
		try {
			listCoordinates = find(query);
		

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return listCoordinates;
		
	}
	

	
	public List<Coordinates> findCoordinatesByDate(Date date, User usr){
		List<Coordinates> listCoordinates = new ArrayList<Coordinates>();
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
		String a = formatDate.format(date);
		System.out.println(a);
		String query = "SELECT c FROM Coordinates c WHERE c.cooCreationDate between '"+formatDate.format(date)+" 00:00:00' and '"+formatDate.format(date)+" 23:59:59' and c.cooState = 0 and c.user.usrId = "+usr.getUsrId();
		try {
			listCoordinates = findForGeo(query);
		

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return listCoordinates;
		
	}
	
	public List<Coordinates> selectCoordinatesByUserDateReal(Date date,User usr){
		List<Coordinates> listCoordinates = new ArrayList<Coordinates>();
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
		String a = formatDate.format(date);
		
		String query = "SELECT c FROM Coordinates c WHERE c.cooCreationDate between '"+formatDate.format(date)+" 00:00:00' and '"+formatDate.format(date)+" 23:59:59' and c.cooState = 1 and c.user.usrId = "+usr.getUsrId();
		try {
			listCoordinates = find(query);
		

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return listCoordinates;
		
	}
	
	public List<Coordinates> getCoordinatesByUser(User usr){
		List<Coordinates> listCoordinates = new ArrayList<Coordinates>();
		
		String query = "SELECT c FROM Coordinates c WHERE c.user.usrId = "+usr.getUsrId();
		try {
			listCoordinates = find(query);
		

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return listCoordinates;
		
	}
}
