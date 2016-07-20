package com.map.services;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.map.entities.Message;
import com.map.utils.GenericDAOImpl;

@Stateless
@LocalBean
public class MessageEjb extends GenericDAOImpl<Message, Integer>{

	public MessageEjb(){
		
	}
}
