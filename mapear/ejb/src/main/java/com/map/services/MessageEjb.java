package com.map.services;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.map.entities.Message;
import com.map.entities.User;
import com.map.utils.GenericDAOImpl;

@Stateless
@LocalBean
public class MessageEjb extends GenericDAOImpl<Message, Integer>{

	public MessageEjb(){
		
	}
	
	public List<Message> getMessagesByUsers(User usrSender, User usrReceiver){
		List<Message> list = new ArrayList<Message>();
		String query = "SELECT m FROM Message m where m.user2.usrId IN ("+usrSender.getUsrId()+","+usrReceiver.getUsrId()+")"+
					" and m.user1.usrId IN ("+usrSender.getUsrId()+","+usrReceiver.getUsrId()+")";
		
		try {
			list = find(query);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
		
	}
}
