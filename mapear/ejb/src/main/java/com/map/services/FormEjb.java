package com.map.services;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.map.entities.Form;
import com.map.utils.GenericDAOImpl;

@Stateless
@LocalBean
public class FormEjb extends GenericDAOImpl<Form, Integer>{

	public FormEjb(){
		
	}
}

