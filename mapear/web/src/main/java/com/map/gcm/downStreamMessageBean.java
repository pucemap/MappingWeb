package com.map.gcm;

import org.jivesoftware.smack.SmackException.NotConnectedException;

public class downStreamMessageBean extends ChatConnectionBean {

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
