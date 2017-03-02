package com.map.gcm;

import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

import com.map.entities.Message;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;

@ApplicationScoped
public class eventBusApp {

	public eventBusApp() {

	}

	public void showMessage(Message message, boolean alerta, String information) {
		EventBus eventBus = EventBusFactory.getDefault().eventBus();
		if (!alerta) {
			eventBus.publish("/counter",
					new FacesMessage(
							message.getUser2().getUsrName() + " " + message.getUser2().getUsrLastname() + " dice: ",
							message.getMesText()));
		} else {
			if("inside".equals(information)){
				eventBus.publish("/counter", new FacesMessage(message.getUser2().getUsrName() + " "
						+ message.getUser2().getUsrLastname() + "Se encuentra fuera del area"));
			}		

		}

	}

	public void chargeMessages() {
		EventBus eventBus = EventBusFactory.getDefault().eventBus();
		eventBus.publish("/chargeMessages");
	}
}
