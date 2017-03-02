package com.map.gcm;

import org.jivesoftware.smack.SmackException.NotConnectedException;

import com.map.beans.ChatBean;

public class downStreamMessageBean extends ChatBean {

	public boolean sendDownstreamMessage(String jsonRequest,
			boolean connectionDraining) throws NotConnectedException {
		if (!connectionDraining) {
			send(jsonRequest);
			return true;
		}
		logger.info("Dropping downstream message since the connection is draining");
		return false;
	}
}
