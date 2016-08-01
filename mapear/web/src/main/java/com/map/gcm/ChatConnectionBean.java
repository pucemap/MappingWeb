package com.map.gcm;

/*
 * @Autor
 * @Fecha:
 * @Descripción:
 */

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.net.ssl.SSLSocketFactory;

import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.json.simple.JSONValue;

import com.map.GCMinterfaces.GcmInterface;
import com.map.entities.Message;
import com.map.entities.User;
import com.map.services.MessageEjb;
import com.map.services.UserEjb;

public class ChatConnectionBean implements GcmInterface {
	public static final Logger logger = Logger.getLogger("chatConnectionBean");
	User user = new User();
	static XMPPTCPConnection connection;
	String messagerecieved = new String();
	String messagesend = new String();
	protected volatile boolean connectionDraining = false;
	Message message = new Message();
	Test myTask;
	String messageT = new String();

	public interface Test {
		public void showMessage(String a, boolean x);
	}

	public ChatConnectionBean() {
		
	};

	public ChatConnectionBean(Test myTask) {
		this.myTask = myTask;
	}

	@EJB
	MessageEjb messageAction;
	@EJB
	UserEjb userAction;

	public boolean sendMessage(String Message, String messageID, String userConnected) {
		String messageId = messageID;
		Map<String, String> payload = new HashMap<String, String>();
		Map<String, Object> json = new HashMap<String, Object>();
		// modificar json en base a lo necesario para la base de datos
		payload.put("Message", Message);
		payload.put("User", userConnected);
		payload.put("EmbeddedMessageId", messageId);
		payload.put("Response", "true");
		payload.put("sentDate", new Date().toString());
		String collapseKey = "sample";
		Long timeToLive = 10000L;
		messageT = createJsonMessage(YOUR_PHONE_REG_ID, messageId, payload, collapseKey, timeToLive, true);
		try {
			json.putAll(payload);
			new downStreamMessageBean().sendDownstreamMessage(messageT, getConnectionDraining());
			//myTask.showMessage(Message, false);

		} catch (NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Message sent.");
		return true;
	}

	public void setConnectionDraining(boolean conn) {
		connectionDraining = conn;
	}

	public boolean getConnectionDraining() {
		return connectionDraining;
	}

//	public void saveMessage(Map<String, Object> json, boolean typeMessage) {
//		User user = new User();
//		Date sendDate = new Date();
//		Date receiveDate = new Date();
//		user.setUsrId(Integer.parseInt(json.get("senderID").toString()));
//		user = userAction.findByUsrToken(user).get(0);
//		User receiver = new User();
//		receiver.setUsrId(Integer.parseInt(json.get("receiverID").toString()));
//		receiver = userAction.findByUsrToken(receiver).get(0);
//		Calendar cal = Calendar.getInstance();
//		cal.set(Calendar.YEAR, Integer.parseInt(json.get("sentDate").toString().substring(0, 4)));
//		cal.set(Calendar.MONTH, Integer.parseInt(json.get("sentDate").toString().substring(5, 7)));
//		cal.set(Calendar.DAY_OF_MONTH, Integer
//				.parseInt(json.get("sentDate").toString().substring(8, json.get("sentDate").toString().length())));
//
//		sendDate = cal.getTime();
//
//		if (typeMessage)/*
//						 * true si es mensaje de llegada al servidor false si
//						 * elservidor envia un mensaje
//						 */ {
//			messagerecieved = json.get("data").toString();
//		} else {
//			messagesend = json.get("Message").toString();
//		}
//		message.setUser2(user);
//		message.setUser1(receiver);
//		message.setMesText(json.get("messageText").toString());
//		message.setMesSendDate(sendDate);
//		message.setMesReceivedDate(receiveDate);
//		message.setMesState("");
//		try {
//			messageAction.persist(message);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	public void connect(String senderId, String apiKey) throws XMPPException, IOException, SmackException {
		XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder().setServiceName(GCM_SERVER)
				.setHost(GCM_SERVER).setCompressionEnabled(false).setPort(GCM_PORT).setConnectTimeout(30000)
				.setSecurityMode(SecurityMode.disabled).setSendPresence(false)
				.setSocketFactory(SSLSocketFactory.getDefault()).build();

		connection = new XMPPTCPConnection(config);

		// disable Roster as I don't think this is supported by GCM
		Roster roster = Roster.getInstanceFor(connection);
		roster.setRosterLoadedAtLogin(false);

		logger.info("Connecting...");
		connection.connect();
		connection.addConnectionListener(new LoggingConnectionListener());
		// Handle incoming packets
		connection.addAsyncStanzaListener(new StanzaListenerBean(myTask), new StanzaFilterBean());
		// Log all outgoing packets
		connection.addPacketInterceptor(new StanzaInterceptorBean(), new StanzaFilterBean());
		connection.login(senderId + "@gcm.googleapis.com", apiKey);
	}

	public void send(String jsonRequest) {
		Stanza request = new GcmPacketExtensionBean(jsonRequest).toPacket();
		try {
			connection.sendStanza(request);
		} catch (NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static final class LoggingConnectionListener implements ConnectionListener {

		@Override
		public void connected(XMPPConnection xmppConnection) {
			logger.info("Connected.");
		}

		@Override
		public void reconnectionSuccessful() {
			logger.info("Reconnecting..");
		}

		@Override
		public void reconnectionFailed(Exception e) {
			logger.log(Level.INFO, "Reconnection failed.. ", e);
		}

		@Override
		public void reconnectingIn(int seconds) {
			logger.log(Level.INFO, "Reconnecting in %d secs", seconds);
		}

		@Override
		public void connectionClosedOnError(Exception e) {
			logger.info("Connection closed on error.");
		}

		@Override
		public void connectionClosed() {
			logger.info("Connection closed.");
		}

		@Override
		public void authenticated(XMPPConnection arg0, boolean arg1) {
			// TODO Auto-generated method stub
		}

	}

	public String nextMessageId() {
		return "m-" + UUID.randomUUID().toString();
	}

	public static String createJsonMessage(String to, String messageId, Map<String, String> payload, String collapseKey,
			Long timeToLive, Boolean delayWhileIdle) {
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("to", to);
		if (collapseKey != null) {
			message.put("collapse_key", collapseKey);
		}
		if (timeToLive != null) {
			message.put("time_to_live", timeToLive);
		}
		if (delayWhileIdle != null && delayWhileIdle) {
			message.put("delay_while_idle", true);
		}
		message.put("message_id", messageId);
		message.put("data", payload);
		return JSONValue.toJSONString(message);
	}

	public String getMessagerecieved() {
		return messagerecieved;
	}

	public void setMessagerecieved(String messagerecieved) {
		this.messagerecieved = messagerecieved;
	}

	public String getMessagesend() {
		return messagesend;
	}

	public void setMessagesend(String messagesend) {
		this.messagesend = messagesend;
	}

}
