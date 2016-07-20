package com.map.converters;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import com.map.beans.UserBean;
import com.map.entities.Role;

@FacesConverter("roleConverter")
public class RoleConverter implements Converter{

	public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
		if(value != null && value.trim().length() > 0) {
			Role car = new Role();
			try {
				UserBean service = (UserBean)fc.getExternalContext().getSessionMap().get("userBean");
				for(Role aux: service.getRoleList())
				{
					if(!value.equals("Seleccione Uno")){
						if(aux.getRolId()==Integer.parseInt(value))
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
			return String.valueOf(((Role) object).getRolId());
		}
		else {
			return null;
		}
	}   
}
