package com.map.gcm;

import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Stanza;

import com.map.GCMinterfaces.GcmInterface;

public class StanzaFilterBean implements StanzaFilter,GcmInterface {

	@Override
	public boolean accept(Stanza arg0) {
		// TODO Auto-generated method stub
		if (arg0.getClass() == Stanza.class)
			return true;
		else {
			if (arg0.getTo() != null)
				if (arg0.getTo().startsWith(YOUR_PROJECT_ID))
					return true;
		}
		return false;
	}
}
