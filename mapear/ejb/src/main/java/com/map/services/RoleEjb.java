package com.map.services;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.map.entities.Role;
import com.map.utils.GenericDAOImpl;

@Stateless
@LocalBean
public class RoleEjb extends GenericDAOImpl<Role, Integer>{

	
}
