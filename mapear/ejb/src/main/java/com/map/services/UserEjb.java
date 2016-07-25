package com.map.services;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.map.entities.User;
import com.map.utils.GenericDAOImpl;

@Stateless
@LocalBean
public class UserEjb extends GenericDAOImpl<User, Integer>{

	public UserEjb(){
		
	}
	
	public List<User> findByUserName(User usr) throws Exception{
		List<User> list = new ArrayList<User>();
		
		if(usr.getUsrName().equals(new String(""))&&usr.getUsrLastname().equals(new String("")))
		{
				list = findAll();
		}
		else
		{
			String query = "SELECT u FROM user u where ";
			if(!usr.getUsrName().equals(new String("")))
				query+= "u.usrName like '%"+usr.getUsrName()+"%'";
			else{
				if(!usr.getUsrLastname().equals(new String("")))
					query+= "u.usrLastname like '%"+usr.getUsrLastname()+"%'";
				else{
					query+= " and u.usrLastname like '%"+usr.getUsrLastname()+"%'";
				}
			}
			list = find(query);
			
			return list;
		}
		return list;
	}
	
	public User findByName(String user){
		User userFinded = new User();
		List<User> list = new ArrayList<User>();
		String query = "SELECT u FROM User u where u.usrNickname like '"+user+"'";
		
		try {
			list = find(query);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(User aux : list){
			userFinded = aux;
		}
		return userFinded;
	}
	
	public List<User> findByUsrToken(User usr){		
		List<User> list = new ArrayList<User>();
		String query = "SELECT u FROM User u where u.usrToken like '%"+usr.getUsrToken()+"%'";
		try {
			list=find(query);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	public List<User> findByLog(){
		List<User> list = new ArrayList<User>();		
		String query = "SELECT u FROM User u where u.usrIsLog like '%true%'";		
		try {
			list = find(query);
		} catch (Exception e) {			
			// TODO Auto-generated catch block
			e.printStackTrace();			
		}		
		return list;
	}
	
	
}
