package com.map.resources;

import javax.faces.application.FacesMessage;

import org.primefaces.push.annotation.OnMessage;
import org.primefaces.push.annotation.PushEndpoint;
import org.primefaces.push.impl.JSONEncoder;

@PushEndpoint("/counter")
public class ChatResource {
	@OnMessage(encoders = { JSONEncoder.class })
	public FacesMessage onMessage(FacesMessage count) {
        return count;
    }
}




