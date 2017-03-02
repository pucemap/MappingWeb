package com.map.gcm;

import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.packet.Stanza;

import com.map.beans.ChatBean;

import java.util.logging.Level;

public class StanzaInterceptorBean extends ChatBean  implements StanzaListener{
	
		@Override
		public void processPacket(Stanza packet) {
			logger.log(Level.INFO, "Sent: {0}", packet.toXML());
			}
		
}
