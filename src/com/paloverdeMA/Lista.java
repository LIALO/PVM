package com.paloverdeMA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.customlistviewvolley.adater.CustomListAdapter;
import com.customlistviewvolley.app.AppController;
import com.customlistviewvolley.model.Negocios;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.paloverdeMA.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class Lista extends Fragment 
{
//////////////////////////////////////////////////////////////////////////////////////
	
	private Negocios negEncontrado;
	private TextView vnombre;
	private TextView vdireccion;
	private TextView vslogan;
	private TextView vtelefono;
	private ProgressDialog pDialogg;
	private static List<Negocios> negList = new ArrayList<Negocios>();
	private ListView listViewB;
	private CustomListAdapter clAdapter;
	BusquedaAvanzada ba =new BusquedaAvanzada() ;
	
	private RadioGroup rgDesplegarRB;
	
	boolean preferenciasGuardadas;

	JSONObject res;
	// List view
	private ListView lvNegocios;
	// Listview Adapter
	ArrayAdapter<String> adapter;	
	// ArrayList for Listview
	ArrayList<HashMap<String, String>> negociosList;

	
	private SeekBar sbPrecios;
	private TextView tvPrecios;
	private SeekBar sbDistancia;
	private TextView tvDistancia;
	private Button btGuardarBA;
	
	/// Variables busqueda avanzada
	private static int Distancia;
	private static int Precio;	
	private static String DesplegarDatos;


    // flag for Internet connection status
    Boolean isInternetPresent = false;
     
    // Connection detector class
    DetectaConexion cd;
	
	public static Handler handler;
    // Google Map///// VARIABLE/////////////////////
    public static GoogleMap googleMap;
    public static SupportMapFragment myMAPF;
    Marker markerPrincipal;
    private static double latitude, longitude;
    GPSTracker gps;
    ///////////////////////////////////////
    //////////////varialbes list view/////
    
    String[] comercios;
    String[] idComercios;
	
	private LinearLayout llc;
	private LinearLayout lllst;
	private LinearLayout llba;
	private LinearLayout llv;
    //////////////////////////////////////

	// contacts JSONArray
	JSONArray contacts = null;
	JSONArray horarios = null;
	JSONArray menu = null;
	////////////////////////////////////////
	
//////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		View rootView = inflater.inflate(R.layout.fragment_lista, container, false);
		try {
			String reln = ba.leerNegocios(getActivity().getBaseContext(),"iconos.txt");
			if(reln.equals(""))
			{
				comercios =getResources().getStringArray(R.array.tipoNegocios);
				Toast.makeText(getActivity(), "El archivo de respaldo no existe. \n"
						+ "Intente iniciar la aplicaci�n con una conexi�n a Internet para crearlo.", Toast.LENGTH_SHORT).show();
			}
			else
			{
				CargarIconos(new JSONArray(reln));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return rootView;
	}
	
	@Override
	public void onStart() 
	{
		super.onStart();
		
		llc = (LinearLayout)getActivity().findViewById(R.id.lista_llTiposNegocios);
		lllst = (LinearLayout)getActivity().findViewById(R.id.lista_llNegociosEncontrados);
		llba = (LinearLayout)getActivity().findViewById(R.id.lista_busquedaAvanzada);
		llv=(LinearLayout)getActivity().findViewById(R.id.lista_vistaNegocio);
		mostrarContenido(true,false,false,false,false);

		lvNegocios = (ListView) getActivity().findViewById(R.id.lista_lvNegocios);
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, R.id.comercio_name, comercios);
        lvNegocios.setAdapter(adapter);

        // Listener para los itmes del Lv Tipos Negocios
        lvNegocios.setOnItemClickListener(new OnItemClickListener() 
        {
          public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
          {        	 
  			////////////////////////////////// ListView Tipos de Negocios ////////////////////////////////////////
  			////////////////////////////////////////////////////////////////////////////////////////////
  			cargarPreferencias();
			JSONArray NegiociosApp;
			try 
			{
				NegiociosApp = new JSONArray(ba.leerNegocios(getActivity().getBaseContext(),"negocios.txt"));
				CargarNegocios(NegiociosApp, position);
				mostrarContenido(false,true,false,false,false);
			} 
			catch (JSONException e) {
				e.printStackTrace();
			} 
  		
  			////////////////////////////////////////////////////////////////////////////////////////////
          }
        });
        
		listViewB = (ListView) getActivity().findViewById(R.id.lista_lvNegociosB);
		clAdapter = new CustomListAdapter(getActivity(), negList);
		listViewB.setAdapter(clAdapter);
		//Listener para en listview negocios.
		listViewB.setOnItemClickListener(new OnItemClickListener() 
        {
	          public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
	          { 
	        	  
	        	  vnombre = (TextView)getActivity().findViewById(R.id.lista_vistaNom);
	        	  vdireccion = (TextView)getActivity().findViewById(R.id.lista_vistaDireccion);
	        	  vslogan = (TextView)getActivity().findViewById(R.id.lista_vistaSlog);
	        	  vtelefono = (TextView)getActivity().findViewById(R.id.lista_vistaTelefonoNegocio);

	        	  negEncontrado = negList.get(position);
	  			  vnombre.setText(negEncontrado.getNombreNegocio());
	  			  vdireccion.setText(negEncontrado.getDireccion());
	  			  vslogan.setText(negEncontrado.getEslogan());
	  			  vtelefono.setText(negEncontrado.getUrlNegocio());
	  			  
		        	NetworkImageView thumbNail = (NetworkImageView) getActivity().findViewById(R.id.lista_vistaImg);
		            int  idIc = negEncontrado.getId_icono();
		            if(negEncontrado.getThumbnailUrl().equals(""))
		            {
		            	if(idIc==1)
		            	{
		            		thumbNail.setBackgroundResource(R.drawable.haten);
		            	}
		            	else
		            		if(idIc==2)
		            		{
		            			thumbNail.setBackgroundResource(R.drawable.tacon);
		            		}
		            		else
		            			if(idIc==3)
		            			{
		            				thumbNail.setBackgroundResource(R.drawable.expendion);
		            			}
		            			else
		            				if(idIc==8)
		            				{
		            					thumbNail.setBackgroundResource(R.drawable.barn);
		            				}
		            }
		            else
		            {
		            	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
		            	thumbNail.setImageUrl(negEncontrado.getThumbnailUrl(), imageLoader);
		            }
	  			  
	  	          // menu
	  			  ControlTabla ct = new ControlTabla(getActivity());
	  			  TableLayout tablam =(TableLayout)getActivity().findViewById(R.id.lista_TablaM);
	  			  tablam.removeAllViews();
	  			  ct.Inicializa(tablam,"Men�",1);
	  			  ct.AgregarRenglonM(negEncontrado.getMenu(), tablam);
	  			  ct.AgregarRenglon("", tablam);
	  			  //Horario
	  			  TableLayout tablah = (TableLayout)getActivity().findViewById(R.id.lista_TablaH);
	  			  tablah.removeAllViews();
	  			  ct.Inicializa(tablah,"Horario",3);
	  			  ct.AgregarRenglonH(negEncontrado.getHorarios(), tablah);


	  			  mostrarContenido(false,false,false,false,true);
	          }
	     });
		/////////// Botones Busqueda Avanzada ////////////7
		btGuardarBA = (Button)getActivity().findViewById(R.id.lista_btGuardarBA);
		btGuardarBA.setOnClickListener(new OnClickListener(){
		        @Override
		        public void onClick(View arg0) 
		        {
		        	//C�digo para guardar preferencis de B�squeda
		        	guardarPreferencias();
		        	mostrarContenido(true,false,false,false,false);
		        }
		 
		    });
		///////////////////////////////////////////////////
		////// Seek Bars //////
		sbDistancia = (SeekBar) getActivity().findViewById(R.id.lista_sbDistancia);
		tvDistancia= (TextView) getActivity().findViewById(R.id.lista_tvDistancia);
		sbDistancia.setOnSeekBarChangeListener(new OnSeekBarChangeListener() 
		{
			@Override
			public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) 
			{
				if(progresValue>=0 && progresValue<=10)
					tvDistancia.setText("Cerca");
				if(progresValue>10 && progresValue<=20)
					tvDistancia.setText("No tan lejos");
				if(progresValue>20 && progresValue<=30)
					tvDistancia.setText("Lejos");
				if(progresValue>30 && progresValue<40)
					tvDistancia.setText("Muy lejos");
				if(progresValue == 40)
					tvDistancia.setText("Todo");
				
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {			}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {			}
		});
		
		sbPrecios = (SeekBar) getActivity().findViewById(R.id.lista_sbPrecio);
		
		tvPrecios = (TextView) getActivity().findViewById(R.id.lista_tvPrecio);
		sbPrecios.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
				
				if(progresValue==50)
					tvPrecios.setText("Todo");
				else
					tvPrecios.setText("$"+Integer.toString(progresValue));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
		});
/////////////////////////////////////////////////////////////////////	
	}

///////////////////////////// METODOS /////////////////////////////////////
    private void hidePDialog() 
    {
		if (pDialogg != null) 
		{
			pDialogg.dismiss();
			pDialogg = null;
		}
	}
    
	public void CargarNegocios(JSONArray jsonArrayNegocios,int idIcono)
	{
		hidePDialog();
		negList.clear();
		// Parsing json
		for (int i = 0; i < jsonArrayNegocios.length(); i++) 
		{
			try 
			{
				JSONObject obj = jsonArrayNegocios.getJSONObject(i);
				if(Integer.parseInt(obj.getString("id_icono")) == Integer.parseInt(  idComercios[idIcono]))
				{
					GPSTracker gpss = new GPSTracker(getActivity().getBaseContext());
					gpss.getLocation();
			    	if(gpss.canGetLocation && gpss.getLatitude()!= 0 && gpss.getLongitude()!=0)
				    {
				        latitude = gpss.getLatitude();
				        longitude = gpss.getLongitude();
				    }
			    	else
			    	{
			    		latitude = 24.1412461;
			    		longitude  = -110.3119056;
			    	}
					if(ba.distancia(latitude, longitude, Double.parseDouble( obj.getString("latitud")), Double.parseDouble( obj.getString("longitud")), Distancia) && ba.precio(obj.getJSONArray("menu"), Precio))
					{
						Negocios neg = new Negocios();
						neg.setNombreNegocio(obj.getString("nombre_negocio"));
						neg.setEslogan(obj.getString("mensaje"));
						neg.setDireccion(obj.getString("calles"));
						neg.setTelefono(obj.getString("telefono"));
						neg.setTagsNegocio(obj.getString("tags"));
						neg.setId_icono(Integer.parseInt(obj.getString("id_icono")));
						neg.setLatitud(Double.parseDouble( obj.getString("latitud")));
						neg.setLongitud(Double.parseDouble( obj.getString("longitud")));
						neg.setThumbnailUrl(obj.getString("url_img"));
						neg.setUrlNegocio(obj.getString("url_neg"));
						//Menu es json array
						JSONArray menuArry = obj.getJSONArray("menu");
						ArrayList<String> menu = new ArrayList<String>();
						if(menuArry.length()!=0)
						{
							for (int j = 0; j < menuArry.length(); j++) 
							{
								JSONObject itemM = menuArry.getJSONObject(j);
								menu.add(itemM.getString("producto")+" "+itemM.getString("descripcion")+" "+itemM.getString("precio"));
							}
						}
						neg.setMenu(menu);
		 
						//Menu es json array
						JSONArray horarioArry = obj.getJSONArray("horarios");
						ArrayList<String> horario = new ArrayList<String>();
						if(horarioArry.length()!=0)
						{
							for (int j = 0; j < horarioArry.length(); j++) 
							{
								JSONObject itemH= horarioArry.getJSONObject(j);
								
								String ha[] = itemH.getString("hora_abre").split(":");
								String haa = ha[0]+":"+ha[1];
								
								String ma[] = itemH.getString("hora_cierra").split(":");
								String maa = ma[0]+":"+ma[1];
								
								horario.add(itemH.getString("dia")+" "+haa+" "+maa);
							}
						}
						neg.setHorarios(horario);
						
						// adding negocio to movies array
						negList.add(neg);
					}
				}


			} 
			catch (JSONException e) 
			{
				e.printStackTrace();
			}

		}
		if (negList.isEmpty())
		{
			Toast.makeText(getActivity(), "No se han encontrado "+comercios[idIcono]+" con los parametros de b�squeda establecidos", Toast.LENGTH_SHORT).show();
			mostrarContenido(true,false,false,false,false);
		}
		// notifying list adapter about data changes
		// so that it renders the list view with updated data
		clAdapter.notifyDataSetChanged();

	}
	
    public void mostrarContenido(boolean negocios,boolean lista,boolean mapa,boolean opcAvanzadas,boolean vista)
    {
    	if(negocios)
    	{
    		llv.setVisibility(View.GONE);
			lllst.setVisibility(View.GONE);
			llba.setVisibility(View.GONE);
    		llc.setVisibility(View.VISIBLE);
    	}
    	if(lista)
    	{
    		llv.setVisibility(View.GONE);
    		llc.setVisibility(View.GONE);
			llba.setVisibility(View.GONE);
			lllst.setVisibility(View.VISIBLE);
    	}

    	if(mapa)
    	{
    		llv.setVisibility(View.GONE);
    		llc.setVisibility(View.GONE);
			lllst.setVisibility(View.GONE);
			llba.setVisibility(View.GONE);
    	}
    	if(opcAvanzadas)
    	{
    		llv.setVisibility(View.GONE);
    		llc.setVisibility(View.GONE);
			lllst.setVisibility(View.GONE);
			llba.setVisibility(View.VISIBLE);
    	}
    	if(vista)
    	{
    		llc.setVisibility(View.GONE);
			lllst.setVisibility(View.GONE);
			llba.setVisibility(View.GONE);
    		llv.setVisibility(View.VISIBLE);
    	}
    	
    }
    
 // //////////////////////Preferencias///////////////
    //guardar configuraci�n aplicaci�n Android usando SharedPreferences
    public void guardarPreferencias(){
    	sbPrecios = (SeekBar) getActivity().findViewById(R.id.lista_sbPrecio);
    	sbDistancia = (SeekBar) getActivity().findViewById(R.id.lista_sbDistancia);
	    SharedPreferences prefs = getActivity().getBaseContext().getSharedPreferences("preferenciasMiApp", getActivity().getBaseContext().MODE_PRIVATE);
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.putBoolean("preferenciasGuardadas", true);
	    editor.putString("Distancia", Integer.toString(sbDistancia.getProgress()));
	    editor.putString("Precio", Integer.toString(sbPrecios.getProgress()));      
	    editor.commit();
	    Toast.makeText(getActivity(), "Guardando configuraci�n", Toast.LENGTH_SHORT).show();
    }
    
    
    //cargar configuraci�n aplicaci�n Android usando SharedPreferences
    public void cargarPreferencias()
    {     
      SharedPreferences prefs = getActivity().getBaseContext().getSharedPreferences("preferenciasMiApp", getActivity().getBaseContext().MODE_PRIVATE);
      Distancia = Integer.parseInt(prefs.getString("Distancia", "0"));
      Precio = Integer.parseInt(prefs.getString("Precio", "0"));
      DesplegarDatos = prefs.getString("DespliegueResultados", "Mapa");
      preferenciasGuardadas = prefs.getBoolean("preferenciasGuardadas", false);
      
    }
 ///////////////////////////////////////////////
    public void CargarIconos(JSONArray jsonArrayIconos)
	{
		// Parsing json
		comercios = new String[jsonArrayIconos.length()];
		idComercios = new String[jsonArrayIconos.length()];
		for (int i = 0; i < jsonArrayIconos.length(); i++) 
		{
			try 
			{
				JSONObject obj = jsonArrayIconos.getJSONObject(i);
				comercios[i] = obj.getString("descripcion");
				idComercios[i] = obj.getString("id");
			} 
			catch (JSONException e) 
			{
				e.printStackTrace();
			}

		}
	}
///////////////////////////// METODOS /////////////////////////////////////
}