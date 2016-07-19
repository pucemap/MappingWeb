package com.map.utils;

//import java.io.UnsupportedEncodingException;
//import java.security.NoSuchAlgorithmException;

import org.jboss.security.auth.spi.Util;

public class SHA {
	
	public SHA(){
		
	}
	
	public String encrypt(String password){
		
		return Util.createPasswordHash("SHA-256", "BASE64", null,null, password);
		
	}

}
