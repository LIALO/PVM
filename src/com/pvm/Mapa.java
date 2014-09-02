package com.pvm;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

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
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class Mapa extends Fragment
{
	private RadioGroup rgDesplegarRB;
    private RadioButton radioMotrarNegocios; 
	
	boolean preferenciasGuardadas;

	
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
	private static boolean DesplegarDatos;
	private static String idTipoNegocio;

	
	// URL to get contacts JSON
	private static String url = "http://paqueteubiquen.esy.es/json.json";
	
    // flag for Internet connection status
    Boolean isInternetPresent = false;
     
    // Connection detector class
    DetectaConexion cd;
	
	public static Handler handler;
    // Google Map///// VARIABLE/////////////////////
    public static GoogleMap googleMap;
    public static SupportMapFragment myMAPF;
    Marker markerPrincipal;
    double latitude, longitude;
    GPSTracker gps;
    ///////////////////////////////////////
    //////////////varialbes list view/////
    
    String[] comercios;
	
	private LinearLayout llc;
	private LinearLayout llba;
	LinearLayout mapall;
    //////////////////////////////////////
	
	/////////////////////////////////////////
	private ProgressDialog pDialog;


	// JSON Node names
	
	private static final String TAG_ID = "id";
	private static final String TAG_ICON_ID = "id_icono";
	private static final String TAG_NAME = "nombre";
	private static final String TAG_NEG_NAME = "nombre_negocio";
	private static final String TAG_MENSAJE = "mensaje";
	private static final String TAG_LAT = "latitud";
	private static final String TAG_LON = "longitud";

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
		// storing string resources into Array
        comercios = getResources().getStringArray(R.array.tipoNegocios);
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
	            	 Toast.makeText(getActivity(), "Sorry un error ocurrio!", Toast.LENGTH_SHORT).show();
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
    	if(gpss.canGetLocation)
	    {
	        latitude = gpss.getLatitude();
	        longitude = gpss.getLongitude();
	        LatLng miPos = new LatLng(latitude, longitude);
	    	camPos = new CameraPosition.Builder()
	        .target(miPos)   //Centramos el mapa en Madrid
	        .zoom(15)         //Establecemos el zoom en 19
	        .bearing(45)      //Establecemos la orientación con el noreste arriba
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
	        .bearing(45)      //Establecemos la orientación con el noreste arriba
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
                Toast.makeText(getActivity(), "Unable to create maps", Toast.LENGTH_SHORT).show();
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
		rgDesplegarRB = (RadioGroup) getActivity().findViewById(R.id.rgDesplegarRB);
		if(rgDesplegarRB.getCheckedRadioButtonId() ==R.id.rbLista)
		{
			editor.putString("DespliegueResultados", "Lista");
		}
		if(rgDesplegarRB.getCheckedRadioButtonId() ==R.id.rbMapa)
		{
			editor.putString("DespliegueResultados", "Mapa");
		}
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
      Distancia = Integer.parseInt(prefs.getString("Distancia", "-1"));
      Precio = Integer.parseInt(prefs.getString("Precio", "-1"));
      DesplegarDatos = prefs.getBoolean("DespliegueResultados", false);
      preferenciasGuardadas = prefs.getBoolean("preferenciasGuardadas", false);
      
    }
	public static double getDistance(double lat_a,double lng_a, double lat_b, double lon_b)
	{
		  int Radius = 6371000; //Radio de la tierra
		  double lat1 = lat_a ;
		  double lat2 = lat_b ;
		  double lon1 = lng_a ;
		  double lon2 = lon_b ;
		  double dLat = Math.toRadians(lat2-lat1);
		  double dLon = Math.toRadians(lon2-lon1);
		  double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon /2) * Math.sin(dLon/2);
		  double c = 2 * Math.asin(Math.sqrt(a));
		  return (double) (Radius * c);  

	}
	///////////////////////////////////////////////
    private void animar(boolean mostrar)
    {
        AnimationSet set = new AnimationSet(true);
        Animation animation = null;
        if (mostrar)
        {
            //desde la esquina inferior derecha a la superior izquierda
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        }
        else
        {    //desde la esquina superior izquierda a la esquina inferior derecha
             animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        }
                //duración en milisegundos
        animation.setDuration(500);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.25f);
 
        llc.setLayoutAnimation(controller);
        llc.startAnimation(animation);
    }

    ////////////////////////////////////////7
	@Override
	public void onStart() 
	{
			super.onStart();
			
			llc = (LinearLayout)getActivity().findViewById(R.id.list_view_contenedor);
			llba = (LinearLayout)getActivity().findViewById(R.id.busquedaAvanzada);
			mapall =(LinearLayout)getActivity().findViewById(R.id.contenedor_mapa);
		    mapall.setVisibility(View.INVISIBLE);
		    llba.setVisibility(View.INVISIBLE);
		        
		 // get the listview
			lvNegocios = (ListView) getActivity().findViewById(R.id.lvNegocios);
			// Adding items to listview
	        adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, R.id.comercio_name, comercios);
	        lvNegocios.setAdapter(adapter);
	        // listening to single list item on click
	        lvNegocios.setOnItemClickListener(new OnItemClickListener() 
	        {
	          public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
	          {        	  
			        if (llc.getVisibility() == View.VISIBLE)
			        {
			        	animar(false); 
			            llc.setVisibility(View.GONE);
			            mapall.setVisibility(View.VISIBLE);
					    if(googleMap!= null)
					    {
					    	googleMap.clear();
					    	uno();
					    }
			        }
		        	// selected item 
				    idTipoNegocio = Integer.toString(position+1);
				    // creating connection detector class instance
				    cd = new DetectaConexion(getActivity().getApplicationContext());
				    // Calling async task to get json
				    new GetContacts().execute();

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
			        	llba.setVisibility(View.GONE);
			        	mapall.setVisibility(View.GONE);
			        	llc.setVisibility(View.VISIBLE);
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
					if(progresValue>30 && progresValue<=40)
						tvDistancia.setText("Muy lejos");
					
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
					{
						tvPrecios.setText("$"+Integer.toString(progresValue)+"+");
					}
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


	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class GetContacts extends AsyncTask<Void, Void, Void> 
	{

		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();

		}
		
		/////////////archivos de texto //////////////////////
		
		public String leer_raw()
		{
			String linea = "";
			try
			{
			    InputStream fraw = getResources().openRawResource(R.raw.negocios);
			    BufferedReader brin = new BufferedReader(new InputStreamReader(fraw));
			    linea = brin.readLine(); 
			    //linea = brin.readLine();
			    fraw.close();
			    return linea;
			}
			catch (Exception ex)
			{
			    Log.e("Ficheros", "Error al leer fichero desde recurso raw");
			    return linea;
			}
		}
		/////////////////////////////////////////////////////		

		@Override
		protected Void doInBackground(Void... arg0) 
		{	
			String jsonStr;
			 // get Internet status
            isInternetPresent = cd.isConnectingToInternet();

            // check for Internet status
            if (isInternetPresent) 
            {
                // Internet Connection is Present
                // make HTTP requests
            	// Creating service handler class instance
    			ServiceHandler sh = new ServiceHandler();

    			// Making a request to url and getting response
    			jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
            } 
            else 
            {
                // Internet connection is not present
                // Ask user to connect to Internet
            	jsonStr = leer_raw();
            }
			
			Log.d("Response: ", "> " + jsonStr);

			if (jsonStr != null) 
			{
				try 
				{
					JSONObject jsonObj = new JSONObject(jsonStr);
					// Getting JSON Array node
					contacts = jsonObj.getJSONArray("negocios");

					// looping through All Contacts
					for (int i = 0; i < contacts.length(); i++) 
					{
						JSONObject c = contacts.getJSONObject(i);						
						String id_icono =c.getString(TAG_ICON_ID);

						if(id_icono.equals(idTipoNegocio))
						{
							//Marker marker_negocio;
							String id = c.getString(TAG_ID);
							String name = c.getString(TAG_NAME);
							double latitud = Double.parseDouble(c.getString(TAG_LAT));
							double longitud = Double.parseDouble(c.getString(TAG_LON));
							
							horarios = c.getJSONArray("horarios");
							for (int hi = 0; hi < horarios.length(); hi++) 
							{
								JSONObject h = horarios.getJSONObject(hi);
								String dia =h.getString("dia");
								String habre =h.getString("hora_abre");
								String hcierra = h.getString("hora_cierra");
							}

							menu = c.getJSONArray("menu");
							for (int mi = 0; mi < menu.length(); mi++) 
							{
								JSONObject m = menu.getJSONObject(mi);
								String producto =m.getString("producto");
								String precio =m.getString("precio");
								String descripcion = m.getString("descripcion");
							}
							
							final MarkerOptions marker = new MarkerOptions().position(new LatLng(latitud, longitud));
				            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
				            marker.title(c.getString(TAG_NEG_NAME));
				            marker.snippet(c.getString(TAG_MENSAJE));
				            handler.post(new Runnable() 
							{
								public void run() 
								{
									googleMap.addMarker(marker);//Toast.makeText(getActivity(), "Wallpaper set",Toast.LENGTH_SHORT).show();
								}
							});
				            
				           // marker_negocio = googleMap.addMarker(marker);
						}	
					}
				} 
				catch (JSONException e) 
				{
					e.printStackTrace();
				}
			} 
			else 
			{
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();
		}

	}
 

}