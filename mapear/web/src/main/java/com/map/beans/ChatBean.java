package com.map.beans;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.net.ssl.SSLSocketFactory;

import java.util.logging.Logger;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.primefaces.context.RequestContext;

import com.google.maps.GeoApiContext;
import com.google.maps.RoadsApi;
import com.google.maps.model.SnappedPoint;
import com.map.GCMinterfaces.GcmInterface;
import com.map.entities.Coordinates;
import com.map.entities.Message;
import com.map.entities.User;
import com.map.entities.UserRole;

import com.map.gcm.GcmPacketExtensionBean;
import com.map.gcm.StanzaFilterBean;
import com.map.gcm.StanzaInterceptorBean;
import com.map.gcm.UpStreamMessageBean;
import com.map.gcm.downStreamMessageBean;
import com.map.gcm.eventBusApp;
import com.map.services.CoordinatesEjb;
//
import com.map.services.MessageEjb;
import com.map.services.UserEjb;

import com.map.services.UserRoleEjb;

@ManagedBean(name = "chatBean")
@ViewScoped
public class ChatBean implements Serializable, GcmInterface, StanzaListener {

	private static final long serialVersionUID = -8990014463069759372L;
	private User user = new User();
	// ChatConnectionBean connection = new ChatConnectionBean();
	private UserRole userRole = new UserRole();
	private UserRole persona = new UserRole();
	private Coordinates coordenadas = new Coordinates();
	private Message message = new Message();

	private String messageText = new String();
	private Message receivedMessage = new Message();
	private List<Message> lista = new ArrayList<Message>();
	private List<UserRole> userRoleList = new ArrayList<UserRole>();
	public static final Logger logger = Logger.getLogger("chatBean");
	private String json = new String();
	static XMPPTCPConnection connectionXMPP;
	private String lookupUsers = "java:global/mapear-ear/mapear-ejb/UserEjb!com.map.services.UserEjb";
	private String lookupMessages = "java:global/mapear-ear/mapear-ejb/MessageEjb!com.map.services.MessageEjb";
	private String lookupCoordinates = "java:global/mapear-ear/mapear-ejb/CoordinatesEjb!com.map.services.CoordinatesEjb";
	private String lookupUserRoles = "java:global/mapear-ear/mapear-ejb/UserRoleEjb!com.map.services.UserRoleEjb";

	String messagerecieved = new String();
	String messagesend = new String();
	eventBusApp prueba = new eventBusApp();
	eventBusApp prueba2 = new eventBusApp();
	eventBusApp prueba3 = new eventBusApp();
	protected volatile boolean connectionDraining = false;
	Test myTask;
	String messageT = new String();
	static RequestContext RC;
	static ExternalContext ec;
	// coordinates conversion
	GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyCBkWwk7936J35ACBf9-qjXGUb2MebfqAo");
	SnappedPoint[] results;

	@EJB
	UserRoleEjb userRoleAction;

	@EJB
	MessageEjb messageAction;

	@EJB
	UserEjb userAction;

	@EJB
	CoordinatesEjb coorAction;

	@SuppressWarnings("deprecation")
	public void onAppointmentTypeChange() {
		ec = FacesContext.getCurrentInstance().getExternalContext();
		user = userAction.findByName(ec.getUserPrincipal().getName());
		persona = userRole;
		findMessages(true, user, persona.getUser());
		System.out.println("entro");
	}

	@SuppressWarnings("deprecation")
	public void onAppointmentTypeChange2() {
		ec = FacesContext.getCurrentInstance().getExternalContext();
		user = userAction.findByName(ec.getUserPrincipal().getName());
		persona = userRole;
		RC = RequestContext.getCurrentInstance();
		try {
			lista = messageAction.getMessagesByUsers(user, persona.getUser());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RC.update("chatForm");
	}
	
	
	

	public boolean sendTimerMessage(User userToSend, User webUser) throws NamingException {
		userRoleAction = (UserRoleEjb) InitialContext.doLookup(lookupUserRoles);		//
		if ("Ios".equals(userToSend.getUsrSmartphoneType())) {
			sendMessageIOS("" + userToSend.getUsrTimerFrequency(), nextMessageId(),
					userToSend, webUser, "timerMessage");

		} else if ("Android".equals(userToSend.getUsrSmartphoneType())) {
			String messageId = nextMessageId();
			Map<String, String> info = new HashMap<String, String>();
			Map<String, Object> json = new HashMap<String, Object>();			
			info.put("frecuency", userToSend.getUsrTimerFrequency()+"");			
			info.put("UserName", webUser.getUsrName());
			info.put("UserLastname", webUser.getUsrLastname());
			info.put("UserNickname", webUser.getUsrNickname());
			info.put("EmbeddedMessageId", messageId);
			info.put("messageType", "timerMessage");
			info.put("sentDate", new Date().toString());
			info.put("Device", "PC");
			info.put("Response", "true");
			String collapseKey = "sample";
			Long timeToLive = 10000L;

			messageT = createJsonMessage(userToSend.getUsrToken(), messageId, info, collapseKey, timeToLive, true);
			try {
				json.putAll(info);
				new downStreamMessageBean().sendDownstreamMessage(messageT, getConnectionDraining());
				// myTask.showMessage(Message, false);

			} catch (NotConnectedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.info("Message sent.");
		}

		return true;
	}

	public void processPacket(Stanza packet) {
		User receiver = new User();
		User sender = new User();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

		logger.log(Level.INFO, "Received: " + packet.toXML());
		try {
			userAction = (UserEjb) InitialContext.doLookup(lookupUsers);
			messageAction = (MessageEjb) InitialContext.doLookup(lookupMessages);
			coorAction = (CoordinatesEjb) InitialContext.doLookup(lookupCoordinates);
			userRoleAction = (UserRoleEjb) InitialContext.doLookup(lookupUserRoles);

			try {
				if (packet.toString().contains("</message>")) {
					org.jivesoftware.smack.packet.Message incomingMessage = (org.jivesoftware.smack.packet.Message) packet;
					GcmPacketExtensionBean gcmPacket = (GcmPacketExtensionBean) incomingMessage
							.getExtension(GCM_NAMESPACE);
					json = gcmPacket.getJson();
					@SuppressWarnings("unchecked")
					Map<String, Object> jsonObject = (Map<String, Object>) JSONValue.parseWithException(json);
					Object messageType = jsonObject.get("message_type");
					String JsonData = jsonObject.get("data").toString();
					JSONObject info = (JSONObject) new JSONParser().parse(JsonData);
					Date dat = format.parse(info.get("sentDate") + "");
					String senderId = jsonObject.get("from").toString();
					sender.setUsrToken(senderId);
					sender = userAction.findByUsrToken(sender).get(0);
					receiver.setUsrNickname(info.get("nickname") + "");
					receiver = userAction.findByUsrNickname(receiver);
					// present for "ack"/"nack", null otherwisehjh
					if ("mensaje".equals(info.get("message_type").toString())) {
						Date date = new Date(System.currentTimeMillis());						
						if ("Ios".equals(sender.getUsrSmartphoneType())) {
							receivedMessage = new Message(dat, date, "R", info.get("messageText") + "", receiver,
									sender);
						} else if ("Android".equals(sender.getUsrSmartphoneType())) {
							// Normal upstream data message
							receivedMessage = new Message(dat, date, "R", info.get("messageText") + "", receiver,
									sender);
						}
						messageAction.persist(receivedMessage);
						findMessages(false, sender, receiver);
						lista = messageAction.getMessagesByUsers(sender, receiver);
						prueba.showMessage(receivedMessage, false, null);
						prueba2.chargeMessages();
						new UpStreamMessageBean().handleUpstreamMessage(jsonObject);
					} else if ("ack".equals(info.get("message_type").toString())) {
						// Process Ack
						handleAckReceipt(jsonObject);
					} else if ("nack".equals(info.get("message_type").toString())) {
						// Process Nack
						handleNackReceipt(jsonObject);
					} else if ("control".equals(info.get("message_type").toString())) {
						// Process control message
						handleControlMessage(jsonObject);
					} else if ("alert".equals(info.get("message_type").toString())) {
						userAction = (UserEjb) InitialContext.doLookup(lookupUsers);
//						List<Coordinates> lastCoords = new ArrayList<Coordinates>();
//						List<com.google.maps.model.LatLng> googleCoord = new ArrayList<com.google.maps.model.LatLng>();
//						int iterator = 0;
						System.out.println(info.get("state").toString());

						BigDecimal lat = new BigDecimal(info.get("latitude") + "");
						BigDecimal lon = new BigDecimal(info.get("longitude") + "");
						coordenadas.setCooLatitude(lat);
						coordenadas.setCooLongitude(lon);
						coordenadas.setUser(sender);
						coordenadas.setCooState(0);
						coordenadas.setCooCreationDate(new Date(System.currentTimeMillis()));
						coorAction.persist(coordenadas);
						coordenadas = new Coordinates();						
					} else {
						logger.log(Level.WARNING, "Unrecognized message type (%s)", messageType.toString());
					}
					// Send ACK to CCS
					String messageId = (String) jsonObject.get("message_id");
					String from = (String) jsonObject.get("from");
					String ack = createJsonAck(from, messageId);
					send(ack);
				} else {
					try {
						IQ IQMessage = (IQ) packet;
						System.out.println(IQMessage);
					} catch (Exception x) {
						logger.log(Level.WARNING, "Error Parsing Message" + x);
						String messageId = "Android";
						String from = "j7IXG6YBoa_TAot6DcxC2PUXL4XeCwD5sjcfVwKsn2pvdesYQzm59uz0_JU0Gua2j3NrUbcWXKAHTEeNTaBT1sc3vY6Jvvnpp_YglXrWHQ";
						String ack = createJsonAck(from, messageId);
						send(ack);
					}
				}
			} catch (Exception e) {

			}
		} catch (Exception e) {
		}
	}

	/**
	 * Handles an ACK.
	 *
	 * <p>
	 * Logs a INFO message, but subclasses could override it to properly handle
	 * ACKs.
	 */
	public void handleAckReceipt(Map<String, Object> jsonObject) {
		String messageId = (String) jsonObject.get("message_id");
		String from = (String) jsonObject.get("from");
		logger.log(Level.INFO, "handleAckReceipt() from: " + from + ",messageId: " + messageId);
	}

	/**
	 * Handles a NACK.
	 *
	 * <p>
	 * Logs a INFO message, but subclasses could override it to properly handle
	 * NACKs.
	 */
	public void handleNackReceipt(Map<String, Object> jsonObject) {
		String messageId = (String) jsonObject.get("message_id");
		String from = (String) jsonObject.get("from");
		logger.log(Level.INFO, "handleNackReceipt() from: " + from + ",messageId: " + messageId);
	}

	public void handleControlMessage(Map<String, Object> jsonObject) {
		logger.log(Level.INFO, "handleControlMessage(): " + jsonObject);
		String controlType = (String) jsonObject.get("control_type");
		if ("CONNECTION_DRAINING".equals(controlType)) {
			connectionDraining = true;
		} else {
			logger.log(Level.INFO, "Unrecognized control type: %s. This could happen if new features are "
					+ "added to the CCS protocol.", controlType);
		}
	}

	public static String createJsonAck(String to, String messageId) {
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("message_type", "ack");
		message.put("to", to);
		message.put("message_id", messageId);
		return JSONValue.toJSONString(message);
	}

	public void findMessages(boolean type, User sender, User receiver) {
		RC = RequestContext.getCurrentInstance();
		lista = messageAction.getMessagesByUsers(sender, receiver);
		if (type)
			RequestContext.getCurrentInstance().update("chatForm");
	}

	public void send() {
		RC = RequestContext.getCurrentInstance();
		sendMessage(messageText, nextMessageId(), persona.getUser(), user);
		Date date = new Date(System.currentTimeMillis());
		message = new Message(date, date, "S", messageText, persona.getUser(), user);

		try {
			messageAction.persist(message);
			message = new Message();
			messageText = new String();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		findMessages(true, user, persona.getUser());
		RequestContext.getCurrentInstance().update("chatForm:area");
		RequestContext.getCurrentInstance().update("chatForm:scroll");
	}

	// public void increment() {
	// findMessages(true, user, persona.getUser());
	// count++;
	//
	// EventBus eventBus = EventBusFactory.getDefault().eventBus();
	// eventBus.publish("/counter", String.valueOf(count));
	// }

	public List<User> returnUser(User usr) {
		return userAction.findByUsrToken(usr);
	}

	public interface Test {
		public void showMessage(String a, boolean x);
	}

	public boolean sendMessage(String Message, String messageID, User userConnectedOnline, User userConnected) {
		
		if ("Ios".equals(userConnectedOnline.getUsrSmartphoneType())) {
			sendMessageIOS(Message, messageID, userConnectedOnline, userConnected, "normalMessage");

		} else if ("Android".equals(userConnectedOnline.getUsrSmartphoneType())) {
			String messageId = messageID;
			Map<String, String> info = new HashMap<String, String>();
			Map<String, Object> json = new HashMap<String, Object>();
			info.put("Message", Message);
			info.put("UserName", userConnected.getUsrName());
			info.put("UserLastname", userConnected.getUsrLastname());
			info.put("UserNickname", userConnected.getUsrNickname());
			info.put("EmbeddedMessageId", messageId);
			info.put("messageType", "normalMessage");
			info.put("sentDate", new Date().toString());
			info.put("Device", "PC");
			info.put("Response", "true");
			String collapseKey = "sample";
			Long timeToLive = 10000L;

			messageT = createJsonMessage(userConnectedOnline.getUsrToken(), messageId, info, collapseKey, timeToLive,
					true);
			try {
				json.putAll(info);
				new downStreamMessageBean().sendDownstreamMessage(messageT, getConnectionDraining());
				// myTask.showMessage(Message, false);

			} catch (NotConnectedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.info("Message sent.");
		}

		return true;
	}

	public void sendMessageIOS(String Message, String messageID, User userConnectedOnline, User userConnected,
			String type) {

		String apiKey = YOUR_API_KEY;
		String GCM_Token = userConnectedOnline.getUsrToken();
		String notification = new String();
		String messageToSend = new String();
		if ("normalMessage".equals(type)) {
			notification = "{\"sound\":\"default\" ,\"badge\":\"1\",\"title\":\"pucemapping\",\"body\":\""
					+ Message + "\",\"UserName\":\"" + userConnected.getUsrName() + "\",\"UserLastname\":\""
					+ userConnected.getUsrLastname() + "\" ,\"UserNickname\":\"" + userConnected.getUsrNickname()
					+ "\",\"sentDate\":\"" + new Date().toString()
					+ "\" ,\"Device\":\"PC\",\"Response\":\"\",\"Type\":\"normalMessage\"}";

		} else if ("Response".equals(type)) {
			notification = "{\"sound\":\"\",\"badge\":\"0\",\"title\":\"pucemapping\",\"body\":\"\",\"UserName\":\""
					+ userConnected.getUsrName() + "\",\"UserLastname\":\"" + userConnected.getUsrLastname()
					+ "\" ,\"UserNickname\":\"" + userConnected.getUsrNickname() + "\",\"sentDate\":\""
					+ new Date().toString() + "\" ,\"Device\":\"PC\",\"Type\":\"Response\"}";

		} else if ("timerMessage".equals(type)) {			
			
			notification = "{\"sound\":\"default\",\"badge\":\"1\",\"content_avaliable\":\"1\",\"title\":\"pucemapping\","
					+ "\"timerTime\":\"" + Message + "\" ,\"UserName\":\"" + userConnected.getUsrName() + "\",\"UserLastname\":\""
					+ userConnected.getUsrLastname() + "\" ,\"UserNickname\":\"" + userConnected.getUsrNickname()
					+ "\",\"sentDate\":\"" + new Date().toString() + "\" ,\"Device\":\"PC\",\"Type\":\"timerMessage\"}";
		}
		messageToSend = "{\"content_avaliable\":\"1\",\"to\":\"" + GCM_Token + "\",\"priority\":\"high\",\"notification\":" + notification + "}";

		try {
			// URL
			URL url = new URL("https://android.googleapis.com/gcm/send");
			System.out.println(messageToSend);
			// Open connection
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// Specify POST method
			conn.setRequestMethod("POST");
			// Set the headers
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "key=" + apiKey);
			conn.setDoOutput(true);
			// Get connection output stream
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			byte[] data = messageToSend.getBytes("UTF-8");
			wr.write(data);
			// Send the request and close
			wr.flush();
			wr.close();
			// Get the response
			int responseCode = conn.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// Print result
			System.out.println(response.toString()); // this is a good place to
														// check for errors
														// using the codes in
														// http://androidcommunitydocs.com/reference/com/google/android/gcm/server/Constants.html

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void connect(String senderId, String apiKey) {
		try {
			ec = FacesContext.getCurrentInstance().getExternalContext();
			userAction = (UserEjb) InitialContext.doLookup(lookupUsers);
		} catch (NamingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (connectionXMPP == null) {
			XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder().setServiceName(GCM_SERVER)
					.setHost(GCM_SERVER).setCompressionEnabled(false).setPort(GCM_PORT).setConnectTimeout(30000)
					.setSecurityMode(SecurityMode.disabled).setSendPresence(false)
					.setSocketFactory(SSLSocketFactory.getDefault()).build();
			connectionXMPP = new XMPPTCPConnection(config);
			// disable Roster as I don't think this is supported by GCM
			Roster roster = Roster.getInstanceFor(connectionXMPP);
			roster.setRosterLoadedAtLogin(false);
			logger.info("Connecting...");
			try {
				connectionXMPP.connect();
				// connectionXMPP.addConnectionListener(new
				// LoggingConnectionListener());
				// Handle incoming packets
				connectionXMPP.addAsyncStanzaListener(this, new StanzaFilterBean());
				// Log all outgoing packets
				connectionXMPP.addPacketInterceptor(new StanzaInterceptorBean(), new StanzaFilterBean());
				connectionXMPP.login(senderId + "@gcm.googleapis.com", apiKey);
				userAction.findByName("asdasd");
				broadcastMessage(userAction.findByName(ec.getUserPrincipal().getName()),"Connected");
			} catch (SmackException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block blackandead
				e.printStackTrace();
			} catch (XMPPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void broadcastMessage(User userConnectedOnline, String type) {
		List<User> Users = new ArrayList<User>();
		

		try {
			Users = userAction.findAll();
			userRoleAction = (UserRoleEjb) InitialContext.doLookup(lookupUserRoles);
			for (User usr : Users) {				
				if ("Android".equals(usr.getUsrSmartphoneType())) {
					String messageId = nextMessageId();
					Map<String, String> info = new HashMap<String, String>();
					info.put("Message", "");
					info.put("UserName", usr.getUsrName());
					info.put("UserLastname", usr.getUsrLastname());
					info.put("UserNickname", usr.getUsrNickname());
					info.put("EmbeddedMessageId", messageId);
					info.put("messageType", type);
					info.put("sentDate", new Date().toString());
					info.put("Device", "PC");
					info.put("Response", "true");
					String collapseKey = "sample";
					Long timeToLive = 10000L;
					messageT = createJsonMessage(userConnectedOnline.getUsrToken(), messageId, info, collapseKey,
							timeToLive, true);
					try {
						new downStreamMessageBean().sendDownstreamMessage(messageT, getConnectionDraining());
					} catch (NotConnectedException e) {

					}
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void send(String jsonRequest) {
		Stanza request = new GcmPacketExtensionBean(jsonRequest).toPacket();
		try {
			connectionXMPP.sendStanza(request);
		} catch (NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// public static final class LoggingConnectionListener implements
	// ConnectionListener {
	//
	// @Override
	// public void connected(XMPPConnection xmppConnection) {
	// logger.info("Connected.");
	// }
	//
	// @Override
	// public void reconnectionSuccessful() {
	// logger.info("Reconnecting..");
	// }
	//
	// @Override
	// public void reconnectionFailed(Exception e) {
	// logger.log(Level.INFO, "Reconnection failed.. ", e);
	// }
	//
	// @Override
	// public void reconnectingIn(int seconds) {
	// logger.log(Level.INFO, "Reconnecting in %d secs", seconds);
	// }
	//
	// @Override
	// public void connectionClosedOnError(Exception e) {
	// logger.info("Connection closed on error.");
	// }
	//
	// @Override
	// public void connectionClosed() {
	// logger.info("Connection closed.");
	// }
	//
	// @Override
	// public void authenticated(XMPPConnection arg0, boolean arg1) {
	// // TODO Auto-generated method stub
	// }
	//
	// }

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

	public List<UserRole> getUserRoleList() {
		userRoleList = userRoleAction.findByUserRole();
		return userRoleList;
	}

	public void setUserRoleList(List<UserRole> userRoleList) {
		this.userRoleList = userRoleList;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	public UserRole getPersona() {
		return persona;
	}

	public void setPersona(UserRole persona) {
		this.persona = persona;
	}

	public List<Message> getLista() {

		return lista;
	}

	public void setLista(List<Message> lista) {
		this.lista = lista;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	///
	private volatile int count;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setConnectionDraining(boolean conn) {
		connectionDraining = conn;
	}

	public boolean getConnectionDraining() {
		return connectionDraining;
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
