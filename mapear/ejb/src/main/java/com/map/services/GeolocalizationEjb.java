package com.map.services;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.map.entities.Geolocalization;
import com.map.utils.GenericDAOImpl;

@Stateless
@LocalBean
public class GeolocalizationEjb extends GenericDAOImpl<Geolocalization, Integer>{

	public GeolocalizationEjb(){
		
	}
}
