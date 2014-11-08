package com.pvm;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.customlistviewvolley.adater.CustomListAdapter;
import com.customlistviewvolley.model.Negocios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pvm.R;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class Mapa extends Fragment
{

	private ProgressDialog pDialogg;

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
	private LinearLayout llba;
	private LinearLayout llv;
	private LinearLayout llmapa;
    //////////////////////////////////////

	// contacts JSONArray
	JSONArray contacts = null;
	JSONArray horarios = null;
	JSONArray menu = null;
	////////////////////////////////////////
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		handler = new Handler();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		View rootView = inflater.inflate(R.layout.fragment_mapa, container, false);
		try {
			String reln = ba.leerNegocios(getActivity().getBaseContext(),"iconos.txt");
			if(reln.equals(""))
			{
				comercios =getResources().getStringArray(R.array.tipoNegocios);
				Toast.makeText(getActivity(), "El archivo de respaldo no existe. \n"
						+ "Intente iniciar la aplicación con una conexión a Internet para crearlo.", Toast.LENGTH_SHORT).show();
			}
			else
			{
				CargarIconos(new JSONArray(reln));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
        initilizeMap();
		return rootView;
	}
	//MÉTODOS MAPA////////////////////////
	public void uno()
	{
		try
	    {
			settingsMaps();
	        posicionaCamera();
	    }
	    catch(Exception e)
	    {
	    	Toast.makeText(getActivity(), "Ha ocurrido un error discupe las molestias!", Toast.LENGTH_SHORT).show();
	    }
	}
    public void settingsMaps()
    {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setBuildingsEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        // Showing / hiding GPS para ubicacion
        googleMap.setMyLocationEnabled(false);
        // Enable / Disable Controles de zoom
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        // Enable / Disable GPS ubicacion actual
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        // Enable / Disable
        googleMap.getUiSettings().setCompassEnabled(true);
        // Enable / Disable rotacion
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        // Enable / Disable zoom
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
    }
    private void posicionaCamera()
    {   
    	GPSTracker gpss = new GPSTracker(getActivity().getBaseContext());
    	CameraPosition camPos;
    	if(gpss.canGetLocation && gpss.getLatitude()!= 0 && gpss.getLongitude()!=0)
	    {
	        latitude = gpss.getLatitude();
	        longitude = gpss.getLongitude();
	        LatLng miPos = new LatLng(latitude, longitude);
	    	camPos = new CameraPosition.Builder()
	        .target(miPos)   //Centramos el mapa en Madrid
	        .zoom(13)         //Establecemos el zoom en 19
	        .bearing(0)      //Establecemos la orientación con el noreste arriba
	        .tilt(90)         //Bajamos el punto de vista de la cámara 70 grados
	        .build();
	    }
    	else
    	{
    		latitude = 24.1412461;
    		longitude  = -110.3119056;
    		LatLng miPos = new LatLng(latitude, longitude);
        	camPos = new CameraPosition.Builder()
	        .target(miPos)   //Centramos el mapa en Madrid
	        .zoom(13)         //Establecemos el zoom en 19
	        .bearing(0)      //Establecemos la orientación con el noreste arriba
	        .tilt(90)         //Bajamos el punto de vista de la cámara 70 grados
	        .build();
    	}
		CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
		googleMap.animateCamera(camUpd3);
		MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude));
		marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
		markerPrincipal = googleMap.addMarker(marker);

    } 
    private void initilizeMap() 
    {
       // if (googleMap == null) 
        //{
        	android.support.v4.app.FragmentManager myFM = getActivity().getSupportFragmentManager();
        	myMAPF = (SupportMapFragment) myFM.findFragmentById(R.id.map);
        	googleMap = myMAPF.getMap();
            // check if map is created successfully or not
            if (googleMap == null) 
            {
                Toast.makeText(getActivity(), "No se pudo crear el mapa", Toast.LENGTH_SHORT).show();
            }
        //}
    }
// //////////////////////Preferencias///////////////
    //guardar configuración aplicación Android usando SharedPreferences
    public void guardarPreferencias(){
    	sbPrecios = (SeekBar) getActivity().findViewById(R.id.sbPrecio);
    	sbDistancia = (SeekBar) getActivity().findViewById(R.id.sbDistancia);
	    SharedPreferences prefs = getActivity().getBaseContext().getSharedPreferences("preferenciasMiApp", getActivity().getBaseContext().MODE_PRIVATE);
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.putBoolean("preferenciasGuardadas", true);
	    editor.putString("Distancia", Integer.toString(sbDistancia.getProgress()));
	    editor.putString("Precio", Integer.toString(sbPrecios.getProgress()));      
	    editor.commit();
	    Toast.makeText(getActivity(), "Guardando preferencias", Toast.LENGTH_SHORT).show();
    }
    
    //cargar configuración aplicación Android usando SharedPreferences
    public void cargarPreferencias()
    {     
      SharedPreferences prefs = getActivity().getBaseContext().getSharedPreferences("preferenciasMiApp", getActivity().getBaseContext().MODE_PRIVATE);
      Distancia = Integer.parseInt(prefs.getString("Distancia", "0"));
      Precio = Integer.parseInt(prefs.getString("Precio", "0"));
      preferenciasGuardadas = prefs.getBoolean("preferenciasGuardadas", false);
    }
 ///////////////////////////////////////////////
    private void hidePDialog() {
		if (pDialogg != null) {
			pDialogg.dismiss();
			pDialogg = null;
		}
	}
    public void mostrarContenido(boolean negocios,boolean lista,boolean mapa,boolean opcAvanzadas,boolean vista)
    {
    	if(negocios)
    	{
    		llv.setVisibility(View.GONE);
			llmapa.setVisibility(View.GONE);
			llba.setVisibility(View.GONE);
    		llc.setVisibility(View.VISIBLE);
    	}
    	if(lista)
    	{
    		llv.setVisibility(View.GONE);
    		llc.setVisibility(View.GONE);
			llmapa.setVisibility(View.GONE);
			llba.setVisibility(View.GONE);
    	}

    	if(mapa)
    	{
    		llv.setVisibility(View.GONE);
    		llc.setVisibility(View.GONE);
			llba.setVisibility(View.GONE);
			llmapa.setVisibility(View.VISIBLE);
    	}
    	if(opcAvanzadas)
    	{
    		llv.setVisibility(View.GONE);
    		llc.setVisibility(View.GONE);
			llmapa.setVisibility(View.GONE);
			llba.setVisibility(View.VISIBLE);
    	}
    	if(vista)
    	{
    		llc.setVisibility(View.GONE);
			llmapa.setVisibility(View.GONE);
			llba.setVisibility(View.GONE);
    		llv.setVisibility(View.VISIBLE);
    	}
    	
    }

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

	public void CargarNegociosMapa(JSONArray jsonArrayNegocios,int idIcono)
	{
		if(pDialogg!=null)
		{
			// Showing progress dialog before making http request
			pDialogg.setMessage("Cargando...");
			pDialogg.show();
		}
		else
		{
			pDialogg = new ProgressDialog(getActivity());
			// Showing progress dialog before making http request
			pDialogg.setMessage("Cargando...");
			pDialogg.show();

		}
		// Parsing json
		for (int i = 0; i < jsonArrayNegocios.length(); i++) 
		{
			try 
			{
				JSONObject obj = jsonArrayNegocios.getJSONObject(i);
				if(Integer.parseInt(obj.getString("id_icono")) == Integer.parseInt( idComercios[idIcono]))
				{
					if(ba.distancia(latitude, longitude, Double.parseDouble( obj.getString("latitud")), Double.parseDouble( obj.getString("longitud")), Distancia) && ba.precio(obj.getJSONArray("menu"), Precio))
					{
						final MarkerOptions marker = new MarkerOptions().position(new LatLng(Double.parseDouble( obj.getString("latitud")), Double.parseDouble( obj.getString("longitud"))));
				        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
				        marker.title(obj.getString("nombre_negocio"));
				        marker.snippet(obj.getString("mensaje"));
				            
				        handler.post(new Runnable() 
				        {
				        	public void run() 
							{
								googleMap.addMarker(marker);
							}
						});

					}
				}
			} 
			catch (JSONException e) 
			{
				e.printStackTrace();
			}

		}
		hidePDialog();
	}
    ////////////////////////////////////////7
	@Override
	public void onStart() 
	{
			super.onStart();
						
			llc = (LinearLayout)getActivity().findViewById(R.id.mapa_llTiposNegocios);
			llmapa =(LinearLayout)getActivity().findViewById(R.id.contenedor_mapa);
			llba = (LinearLayout)getActivity().findViewById(R.id.mapa_busquedaAvanzada);
			llv=(LinearLayout)getActivity().findViewById(R.id.mapa_vistaNegocio);
			mostrarContenido(true,false,false,false,false);

			lvNegocios = (ListView) getActivity().findViewById(R.id.lvNegocios);
	        adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, R.id.comercio_name, comercios);
	        lvNegocios.setAdapter(adapter);

	        // Listener para los itmes del Lv Tipos Negocios
	        lvNegocios.setOnItemClickListener(new OnItemClickListener() 
	        {
	          public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
	          {        	 
	  			////////////////////////////////// ListView Negocios ////////////////////////////////////////
	  			////////////////////////////////////////////////////////////////////////////////////////////
	  			cargarPreferencias();
	  			mostrarContenido(false,false,true,false,false);
	  			if(googleMap!= null)
				{
	  				googleMap.clear();
					uno();
					   
		  			JSONArray NegiociosApp;
					try 
					{
						NegiociosApp = new JSONArray(ba.leerNegocios(getActivity().getBaseContext(),"negocios.txt"));
						CargarNegociosMapa(NegiociosApp, position);
					} 
					catch (JSONException e) {
						e.printStackTrace();
					}
				}
	  			////////////////////////////////////////////////////////////////////////////////////////////
	          }
	        });
	     
			/////////// Botones Busqueda Avanzada ////////////7
			btGuardarBA = (Button)getActivity().findViewById(R.id.btGuardarBA);
			btGuardarBA.setOnClickListener(new OnClickListener(){
			        @Override
			        public void onClick(View arg0) 
			        {
			        	//Código para guardar preferencis de Búsqueda
			        	guardarPreferencias();
			        	mostrarContenido(true,false,false,false,false);
			        }
			 
			    });

			///////////////////////////////////////////////////
			////// Seek Bars //////
			sbDistancia = (SeekBar) getActivity().findViewById(R.id.sbDistancia);
			tvDistancia= (TextView) getActivity().findViewById(R.id.tvDistancia);
			sbDistancia.setOnSeekBarChangeListener(new OnSeekBarChangeListener() 
			{
				@Override
				public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
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
			
			sbPrecios = (SeekBar) getActivity().findViewById(R.id.sbPrecio);
			
			tvPrecios = (TextView) getActivity().findViewById(R.id.tvPrecio);
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

	     ////// OnCick Marketer /////////////////// 
			if(googleMap!=null)
			{
		        googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {
		            public boolean onMarkerClick(Marker marker) 
		            {
		                Toast.makeText(
		                    getActivity(),
		                    "Latitud:\n" +
		                    marker.getPosition().latitude+
		                    "\nLongitud:\n"+marker.getPosition().longitude,
		                    Toast.LENGTH_SHORT).show();
		
		                return false;
		            }
		        });
			}
		//////////////////////////////////////////
	}
}