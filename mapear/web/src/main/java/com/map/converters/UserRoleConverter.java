package com.map.converters;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import com.map.beans.ChatBean;
import com.map.entities.UserRole;

@FacesConverter("userRoleConverter")
public class UserRoleConverter implements Converter{

	public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
		if(value != null && value.trim().length() > 0) {
			UserRole car = new UserRole();
			try {
				ChatBean service = (ChatBean)fc.getExternalContext().getSessionMap().get("chatBean");
				for(UserRole aux: service.getUserRoleList())
				{
					if(!value.equals("Seleccione Uno")){
						if(aux.getUsrRolId()==Integer.parseInt(value))
							car = aux;
					}
					else
						car = null;
				}
				return car;
			} catch(NumberFormatException e) {
				throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid theme."));
			}
		}
		else {
			return null;
		}
	}

	public String getAsString(FacesContext fc, UIComponent uic, Object object) {
		if(object != null) {
			return String.valueOf(((UserRole) object).getUsrRolId());
		}
		else {
			return null;
		}
	}   
}
