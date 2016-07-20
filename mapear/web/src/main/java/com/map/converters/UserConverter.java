//package com.map.converters;
//
//import java.util.Map;
//
//import javax.faces.application.FacesMessage;
//import javax.faces.component.UIComponent;
//import javax.faces.context.FacesContext;
//import javax.faces.convert.Converter;
//import javax.faces.convert.ConverterException;
//import javax.faces.convert.FacesConverter;
//
////import com.map.beans.GeolocalizationBean;
//import com.map.entities.User;
//
//@FacesConverter("userConverter")
//public class UserConverter implements Converter{
//
////	public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
////		if(value != null && value.trim().length() > 0) {
////			User usr = new User();
////			try {
////				@SuppressWarnings("static-access")
////				Map<String, Object> viewMap = fc.getCurrentInstance().getViewRoot().getViewMap();
////				//GeolocalizationBean service = (GeolocalizationBean)viewMap.get("geoBean");
////				for(User aux: service.getUserList())
////				{
////					if(!value.equals("Seleccione Uno")){
////						if(aux.getUsrId()==Integer.parseInt(value))
////							usr = aux;
////					}
////					else
////						usr = null;
////				}
////				return usr;
////			} catch(NumberFormatException e) {
////				throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid theme."));
////			}
////		}
////		else {
////			return null;
////		}
////	}
////
////	public String getAsString(FacesContext fc, UIComponent uic, Object object) {
////		if(object != null) {
////			return String.valueOf(((User) object).getUsrId());
////		}
////		else {
////			return null;
////		}
////	}   
//}
