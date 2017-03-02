package com.map.resources;

import org.primefaces.push.annotation.OnMessage;
import org.primefaces.push.annotation.PushEndpoint;
import org.primefaces.push.impl.JSONEncoder;

@PushEndpoint("/chargeMessages")
public class ShowMessagesResource {
	@OnMessage(encoders = { JSONEncoder.class })
	public String onMessage(String count) {
        return count;
    }
}
