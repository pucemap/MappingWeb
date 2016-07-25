

//@Descripción Bean de gestión para crud de tabla Enterprise 
package com.map.beans;

import java.io.Serializable;
import java.util.ArrayList;

import java.util.List;


import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;


import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;

import com.map.entities.Enterprise;


import com.map.services.EnterpriseEjb;


@ManagedBean(name="EnterpriseBean")
@SessionScoped
public class EnterpriseBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7927820995066852655L;
	/**
	 * 
	 */
	
	
	private Enterprise enterpriseSearch = new Enterprise();
	
	private List<Enterprise> enterpriseList = new ArrayList<Enterprise>();
	
	private Enterprise enterprise = new Enterprise();
	private Boolean isNew = new Boolean (Boolean.TRUE);
	
	

	
	@EJB
	EnterpriseEjb enterpriseAction;
	
	@PostConstruct
	public void initialize(){
		
		
		enterpriseList = new ArrayList<Enterprise>();
		try {
			enterpriseList = enterpriseAction.findAll();
			} catch (Exception e) {
				e.printStackTrace();
				}
		}
	
	//Método para insertar datos
	
	public void save() {
		
		
		
			if(isNew){

				RequestContext.getCurrentInstance().update("enterpriseForm:insertDialog");
				
				try {					
					enterpriseAction.persist(enterprise);
					
					enterprise = new Enterprise();
					
					RequestContext.getCurrentInstance().update("enterpriseForm:insertDialog");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			else{
				
				try {
				
					enterpriseAction.merge(enterprise);
					
					isNew = Boolean.TRUE;
					enterprise = new Enterprise();
					
					RequestContext.getCurrentInstance().update("enterpriseForm:insertDialog");
				
				} catch(Exception e){
					e.printStackTrace();
				}

				
			}
			
			try{
				
			enterpriseList = enterpriseAction.findAll();
			RequestContext.getCurrentInstance().update("enterpriseForm:enterpriseTable");
			
			}catch(Exception ex){
				ex.printStackTrace();
			}
	}
	
	
	//Método para buscar determinado registro en la tabla objeto
	public void search(){
		try {
		
			//enterpriseList = enterpriseAction.findByEnterpriseName(enterpriseSearch);
			enterpriseList = enterpriseAction.findByEnterpriseName(enterpriseSearch);		
			RequestContext.getCurrentInstance().update("enterpriseForm:enterpriseTable");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Método para borrar determinado campo en la tabla objeto
		public void delete(Enterprise ent){
			
			
			try {
				enterpriseAction.remove(ent);
				enterpriseList = enterpriseAction.findAll();
				RequestContext.getCurrentInstance().update("enterpriseForm:enterpriseTable");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//Método para limpiar y refrescar la lista objeto
		public void clean(){
			enterpriseList = new ArrayList<Enterprise>();
			enterpriseSearch= new Enterprise();
			RequestContext.getCurrentInstance().update("enterpriseForm:enterpriseTable");
			RequestContext.getCurrentInstance().update("enterpriseForm:enterpriseSearchInput");
		}
		
		//Método para editar determinado campo en la lista objeto
		public void edit(Enterprise ent){
			isNew = Boolean.FALSE;
			enterprise = new Enterprise();
			enterprise= ent;
			//prueba
			RequestContext.getCurrentInstance().update("enterpriseForm:insertDialog");
			RequestContext.getCurrentInstance().execute("PF('dlg2').show();");
		}
		
	    //Método para cerrar la instancia de la operación o de la conexión
		public void handleClose(CloseEvent event) {
			enterprise = new Enterprise();
			
			isNew = Boolean.TRUE;
			RequestContext.getCurrentInstance().update("enterpriseForm:insertDialog");
	    }

		
		
		public Boolean getIsNew() {
			return isNew;
		}

		public void setIsNew(Boolean isNew) {
			this.isNew = isNew;
		}

				

		public Enterprise getEnterprise() {
			return enterprise;
		}




		public void setEnterprise(Enterprise enterprise) {
			this.enterprise = enterprise;
		}




		public Enterprise getEnterpriseSearch() {
			return enterpriseSearch;
		}




		public void setEnterpriseSearch(Enterprise enterpriseSearch) {
			this.enterpriseSearch = enterpriseSearch;
		}

		public List<Enterprise> getEnterpriseList() {
			return enterpriseList;
		}
		
		public void setEnterpriseList(List<Enterprise> enterpriseList) {
			this.enterpriseList = enterpriseList;
		}

	
	
		

		
		

		
		

	
}