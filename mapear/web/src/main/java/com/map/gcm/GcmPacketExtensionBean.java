package com.map.gcm;

import java.io.IOException;

import org.jivesoftware.smack.packet.DefaultExtensionElement;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.provider.ExtensionElementProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.util.StringUtils;
import org.xmlpull.v1.XmlPullParser;

import com.map.GCMinterfaces.GcmInterface;

public class GcmPacketExtensionBean extends DefaultExtensionElement implements
		GcmInterface {
	/**
	 * XMPP Packet Extension for GCM Cloud Connection Server.
	 */
	static {
		ProviderManager.addExtensionProvider(GCM_ELEMENT_NAME, GCM_NAMESPACE,
				new ExtensionElementProvider<ExtensionElement>() {
					@Override
					public DefaultExtensionElement parse(XmlPullParser parser,
							int initialDepth)
							throws org.xmlpull.v1.XmlPullParserException,
							IOException {
						String json = parser.nextText();
						return new GcmPacketExtensionBean(json);
					}
				});
	}
	private final String json;

	public GcmPacketExtensionBean(String json) {
		super(GCM_ELEMENT_NAME, GCM_NAMESPACE);
		this.json = json;
	}

	public String getJson() {
		return json;
	}

	@Override
	public String toXML() {
		return String
				.format("<%s xmlns=\"%s\">%s</%s>", GCM_ELEMENT_NAME,
						GCM_NAMESPACE, StringUtils.escapeForXML(json),
						GCM_ELEMENT_NAME);
	}

	public Stanza toPacket() {
		Message message = new Message();
		message.addExtension(this);
		return message;
	}

}
