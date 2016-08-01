package com.map.gcm;

import java.util.Map;

import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

import javax.faces.bean.ApplicationScoped;

@ApplicationScoped
public class eventBusApp {

	public eventBusApp(){
		
	}
	
	public void showMessage(Map<String, Object> message){
		EventBus eventBus = EventBusFactory.getDefault().eventBus();
		eventBus.publish("/counter",message.toString());		
	}
}
