package com.map.services;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.map.entities.Role;
import com.map.entities.UserRole;
import com.map.utils.GenericDAOImpl;

@Stateless
@LocalBean
public class RoleEjb extends GenericDAOImpl<Role, Integer>{
	
	public RoleEjb(){
		
	}
	
	public Role findByUserRole(UserRole usrRole){
		Role rol = new Role();
		List<Role> list = new ArrayList<Role>();
		
		//String query = "SELECT r FROM Role r where r.user.usrName ='"+usrRole.getRole().ge/+"'";
		return rol;
	}
	
}
