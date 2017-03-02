//package com.map.gcm;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.logging.Level;
//
//import javax.ejb.EJB;
//
//import org.jivesoftware.smack.StanzaListener;
//import org.jivesoftware.smack.packet.Message;
//import org.jivesoftware.smack.packet.Stanza;
//import org.json.simple.JSONValue;
//import org.json.simple.parser.ParseException;
//
//import com.map.GCMinterfaces.GcmInterface;
//import com.map.entities.User;
//import com.map.services.MessageEjb;
//import com.map.services.UserEjb;
//
//public class StanzaListenerBean extends ChatConnectionBean implements
//		StanzaListener {
//
//	Test recieveMessage;
//	Message receivedMessage = new Message();
//	
//	
//	
//	
//
//	public StanzaListenerBean() {
//	}
//
//	public StanzaListenerBean(Test recieveMessage) {
//		this.recieveMessage = recieveMessage;
//	}
//
//	public interface RecievedMessage {
//		public void recieved(String text, Boolean a);
//	}
//
//	@Override
//	public void processPacket(Stanza packet) {
//		
//		logger.log(Level.INFO, "Received: " + packet.toXML());
//		Message incomingMessage = (Message) packet;
//		GcmPacketExtensionBean gcmPacket = (GcmPacketExtensionBean) incomingMessage
//				.getExtension(GCM_NAMESPACE);
//		String json = gcmPacket.getJson();		
//		try {
//			@SuppressWarnings("unchecked")
//			Map<String, Object> jsonObject = (Map<String, Object>) JSONValue
//					.parseWithException(json);
//			Object messageType = jsonObject.get("message_type");
//			Date date = new Date(System.currentTimeMillis());
//			
//			
//			//sacar el mensaje y solo el booleano
//			Map<String,String> Information = new HashMap<String, String>();
//			String JsonData = jsonObject.get("data").toString();
//			JsonData = JsonData.substring(1, JsonData.length()-1);
//			JsonData= JsonData.replace("{", "");
//			JsonData= JsonData.replace("}","");
//			JsonData=JsonData.replaceAll("\\\\","");
//			JsonData=JsonData.replace("\"", "");
//			String[] JsonPairs = JsonData.split(",");
//			for(String pair : JsonPairs){
//				String[] entry = pair.split(":");
//				Information.put(entry[0].trim(), entry[1].trim());
//			}
//			// user 2 es el sender y user 1 es el receiver
//			//public Message(Date mesReceivedDate,Date mesSendDate,String mesState,String mesText,User user1,User user2)
//			String senderId = jsonObject.get("from").toString();
//			//User sender  = new User();
//			//	User receiver  = new User();						
//			//sender = (User) userAction.findByUsrToken(sender);			
//			//receiver = (User) userAction.findByUsrToken(receiver);
//			//receivedMessage = new Message(date, date, "S", Information.get("message"), receiver, sender);			
//			// present for "ack"/"nack", null otherwise
//			
//			if (messageType == null) {
//				// Normal upstream data message
//				new UpStreamMessageBean().handleUpstreamMessage(jsonObject);
//				// Send ACK to CCS
//				String messageId = (String) jsonObject.get("message_id");
//				String from = (String) jsonObject.get("from");
//				String ack = createJsonAck(from, messageId);
//				send(ack);
//
//			} else if ("ack".equals(messageType.toString())) {
//				// Process Ack
//				handleAckReceipt(jsonObject);
//			} else if ("nack".equals(messageType.toString())) {
//				// Process Nack
//				handleNackReceipt(jsonObject);
//			} else if ("control".equals(messageType.toString())) {
//				// Process control message
//				handleControlMessage(jsonObject);
//			} else {
//				 logger.log(Level.WARNING,
//				 "Unrecognized message type (%s)",messageType.toString());
//			}
//		} catch (ParseException e) {
//			logger.log(Level.SEVERE, "Error parsing JSON " + json, e);
//		} catch (Exception e) {
//			logger.log(Level.SEVERE, "Failed to process packet", e);
//		}
//	
//	}		
//
//
//	/**
//	 * Handles an ACK.
//	 *
//	 * <p>
//	 * Logs a INFO message, but subclasses could override it to properly handle
//	 * ACKs.
//	 */
//	public void handleAckReceipt(Map<String, Object> jsonObject) {
//		String messageId = (String) jsonObject.get("message_id");
//		String from = (String) jsonObject.get("from");
//		logger.log(Level.INFO, "handleAckReceipt() from: " + from
//				+ ",messageId: " + messageId);
//	}
//
//	/**
//	 * Handles a NACK.
//	 *
//	 * <p>
//	 * Logs a INFO message, but subclasses could override it to properly handle
//	 * NACKs.
//	 */
//	public void handleNackReceipt(Map<String, Object> jsonObject) {
//		String messageId = (String) jsonObject.get("message_id");
//		String from = (String) jsonObject.get("from");
//		logger.log(Level.INFO, "handleNackReceipt() from: " + from
//				+ ",messageId: " + messageId);
//	}
//
//	public void handleControlMessage(Map<String, Object> jsonObject) {
//		logger.log(Level.INFO, "handleControlMessage(): " + jsonObject);
//		String controlType = (String) jsonObject.get("control_type");
//		if ("CONNECTION_DRAINING".equals(controlType)) {
//			connectionDraining = true;
//		} else {
//			logger.log(Level.INFO,
//					"Unrecognized control type: %s. This could happen if new features are "
//							+ "added to the CCS protocol.", controlType);
//		}
//	}
//
//	public static String createJsonAck(String to, String messageId) {
//		Map<String, Object> message = new HashMap<String, Object>();
//		message.put("message_type", "ack");
//		message.put("to", to);
//		message.put("message_id", messageId);
//		return JSONValue.toJSONString(message);
//	}
//
//}
