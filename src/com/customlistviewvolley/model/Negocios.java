package com.customlistviewvolley.model;
import java.util.ArrayList;


public class Negocios 
{
	private String nombreNegocio, eslogan,telefono,urlNegocio,direccion,tagsNegocio;
    private int id_icono;
    private double latitud,longitud;
    private ArrayList<String> menu;
    private ArrayList<String> horarios;
    private String thumbnailUrl;

public Negocios()
    {
    	
    }
   public Negocios(String nn,String elg,String tel,String urlN,String dir,String tags,int idC,double lat,double lon, ArrayList<String> horarios,ArrayList<String> menu)
    {
    	this.nombreNegocio = nn;
    	this.eslogan= elg;
    	this.telefono=tel;
    	this.urlNegocio = urlN;
    	this.direccion = dir;
    	this.tagsNegocio = tags;
    	this.id_icono = idC;
    	this.latitud = lat;
    	this.longitud = lon;
    	this.menu = menu;
    	this.horarios = horarios;
    }
    
    public String getNombreNegocio() {
		return nombreNegocio;
	}
	public void setNombreNegocio(String nombreNegocio) {
		this.nombreNegocio = nombreNegocio;
	}
	public String getEslogan() {
		return eslogan;
	}
	public void setEslogan(String eslogan) {
		this.eslogan = eslogan;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getUrlNegocio() {
		return urlNegocio;
	}
	public void setUrlNegocio(String urlNegocio) {
		this.urlNegocio = urlNegocio;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getTagsNegocio() {
		return tagsNegocio;
	}
	public void setTagsNegocio(String tagsNegocio) {
		this.tagsNegocio = tagsNegocio;
	}
	public int getId_icono() {
		return id_icono;
	}
	public void setId_icono(int id_icono) {
		this.id_icono = id_icono;
	}
	public double getLatitud() {
		return latitud;
	}
	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}
	public double getLongitud() {
		return longitud;
	}
	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}
	public ArrayList<String> getMenu() {
		return menu;
	}
	public void setMenu(ArrayList<String> menu) {
		this.menu = menu;
	}
	public ArrayList<String> getHorarios() {
		return horarios;
	}
	public void setHorarios(ArrayList<String> horarios) {
		this.horarios = horarios;
	}
    
	public String getThumbnailUrl() 
    {
		return thumbnailUrl;
	}
	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}
}
