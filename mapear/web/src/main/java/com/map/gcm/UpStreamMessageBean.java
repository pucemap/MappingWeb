package com.map.gcm;

import java.util.Map;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import java.util.logging.Level;

import com.map.beans.ChatBean;

public class UpStreamMessageBean extends ChatBean {
	public void handleUpstreamMessage(Map<String, Object> jsonObject) {
		// PackageName of the application that sent this message.
		// String category = (String) jsonObject.get("category");
		String from = (String) jsonObject.get("from");
		@SuppressWarnings("unchecked")
		Map<String, String> payload = (Map<String, String>) jsonObject.get("data");
		// payload.put("ECHO", "Application: " + category);

		// Send an ECHO response back
		String echo = createJsonMessage(from, nextMessageId(), payload, "echo:CollapseKey", null, false);

		try {
			new downStreamMessageBean().sendDownstreamMessage(echo, getConnectionDraining());
		} catch (NotConnectedException e) {
			logger.log(Level.WARNING, "Not connected anymore, echo message is not sent", e);
		}
	}

}
