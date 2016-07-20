package com.map.services;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.map.entities.Group;
import com.map.utils.GenericDAOImpl;

@Stateless
@LocalBean
public class GropuEjb extends GenericDAOImpl<Group, Integer>{

	public GropuEjb(){
		
	}
}
