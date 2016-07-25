package com.map.services;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.map.entities.Enterprise;
import com.map.utils.GenericDAOImpl;

@Stateless
@LocalBean
public class EnterpriseEjb extends GenericDAOImpl<Enterprise, Integer>{
	
	public EnterpriseEjb(){
		
	}
	
	public List<Enterprise> testFunction(){
		List<Enterprise> test = new ArrayList<Enterprise>();
		return test;
	}
	
	public List<Enterprise> findByEnterpriseName(Enterprise ent){
		List<Enterprise> list = new ArrayList<Enterprise>();
		
		if(ent.getEntName().equals(new String("")))
		{
				try {
					list = findAll();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		else
		{
			String query = "SELECT e FROM Enterprise e where ";
			query+= "e.entName like '%"+ent.getEntName()+"%'";			
			try {
				list = find(query);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return list;
		}
		return list;
	}
	
	public Enterprise findByName(String enterprise){
		Enterprise enterpriseFinded = new Enterprise();
		List<Enterprise> list = new ArrayList<Enterprise>();
		String query = "SELECT u FROM User u where u.usrNickName ='"+enterprise+"'";
		try {
			list = find(query);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(Enterprise aux : list){
			enterpriseFinded = aux;
		}
		return enterpriseFinded;
	}

}
