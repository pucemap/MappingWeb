package com.map.resources;

import java.io.Serializable;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.map.entities.User;

public class SenderEmails implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String item;
	private static String to ;
	private static String from ;
	private static String nombre = new String();
	private static String email = new String();
	private static String theme = new String();
	private static String text = new String();
	
	public static void send (User user){
		
		
			Properties props = null;
	        if (props == null) {
	            props = new Properties();
	            props.put("mail.smtp.host", "smtp.gmail.com");
	            props.put("mail.smtp.port", "587");
	            props.put("mail.smtp.user", "ayni.code@gmail.com");
	            props.put("mail.smtp.pwd", "Ayni2014");
	            props.put("mail.smtp.starttls.enable", "true");
	            
	        }
	        Session session = Session.getInstance(props, null);
	        session.setDebug(true);
	        Message msg = new MimeMessage(session);
	        try {
				msg.setFrom(new InternetAddress("ayni.code@gmail.com"));
				msg.setSubject(item);
		        msg.setContent("<h1>DE:"+user.getUsrName()+"</h1><br></br>"+"<h1>EMAIL:"+user.getUsrEmail()+"</h1><br></br>"+"<h1>"+user.getUsrRegistrationCode().toString()+"</h1><br></br>","text/html");
				msg.setRecipient(Message.RecipientType.TO, new InternetAddress(user.getUsrEmail()));
		        Transport transport = session.getTransport("smtp");
		        transport.connect("smtp.gmail.com", 587, "ayni.code@gmail.com", "Ayni2014");
		        transport.sendMessage(msg, msg.getAllRecipients());
		        System.out.println("Mail sent successfully at");
		        transport.close();
			} catch (AddressException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
    }
	


	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getTheme() {
		return theme;
	}



	public void setTheme(String theme) {
		this.theme = theme;
	}



	public String getText() {
		return text;
	}



	public void setText(String text) {
		this.text = text;
	}
	
}
