package com.paloverdeMA;
import org.jasypt.util.text.BasicTextEncryptor;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.customlistviewvolley.app.AppController;
import com.google.android.gms.maps.SupportMapFragment;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener 
{
	BusquedaAvanzada ba = new BusquedaAvanzada();
	private ProgressDialog pDialogg;
	private SeekBar sbPrecios;
	private TextView tvPrecios;
	private SeekBar sbDistancia;
	private TextView tvDistancia;
	
	private LinearLayout lista_llTN;
	private LinearLayout lista_llNE;
	private LinearLayout lista_BA;
	private LinearLayout lista_vista;
	
	private LinearLayout mapa_llTN;
	private LinearLayout mapa_CM;
	private LinearLayout mapa_BA;
	private LinearLayout mapa_vista;

	ControlUbicacion cu= new ControlUbicacion(); 
	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	// URL to get contacts JSONhttp://paqueteubiquen.com/
	private static String url = "http://paqueteubiquen.com/json.json";
	
	Resources mResources;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mResources = getResources();
		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(90,172,165)));
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() 
		{
					@Override
					public void onPageSelected(int position) 
					{
						actionBar.setSelectedNavigationItem(position);
					}
				});
		
		
		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) 
		{
			actionBar.addTab(actionBar.newTab().setText(mSectionsPagerAdapter.getPageTitle(i)).setTabListener(this));
		}
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) 
		{
			ActionBar.Tab tab = actionBar.getTabAt(i);
			asigna_icono(tab);
		}
		pDialogg = new ProgressDialog(this);
		// Showing progress dialog before making http request
		pDialogg.setMessage("Bienvenido...\nCargando datos");
		pDialogg.show();
		String reln = ba.leerNegocios(getBaseContext(),"iconos.txt");
		if(reln.equals(""))
		{
			ba.respaldarNegocios(ba.leerNegociosR(getBaseContext()),getApplicationContext(), "negocios.txt");
			ba.respaldarNegocios(ba.leerIconosR(getBaseContext()), getApplicationContext(), "iconos.txt");
		}
		JsonObjectRequest negReq = new JsonObjectRequest(url,null, new Response.Listener<JSONObject>() 
				{
					@Override
					public void onResponse(JSONObject response)
					{
						try 
						{
							JSONObject mensaje = response.getJSONObject("mensaje");
							String encriptado =decryptB( mensaje.getString("encriptado"));
							JSONObject info = new JSONObject(encriptado);
							
							ba.respaldarNegocios(info.getJSONArray("negocios").toString(),getApplicationContext(), "negocios.txt");
							ba.respaldarNegocios(info.getJSONArray("iconos").toString(), getApplicationContext(), "iconos.txt");

						}catch (JSONException e) 
						{
							e.printStackTrace();
						}
						if (pDialogg != null) {
							pDialogg.dismiss();
							pDialogg = null;
						}
					}
				}, new Response.ErrorListener() 
				{
					@Override
					public void onErrorResponse(VolleyError error) 
					{				
						Toast.makeText(getApplicationContext(),"No se ha conectado con el servidor.\n Se usaran los datos locales", Toast.LENGTH_SHORT).show();
						if (pDialogg != null) {
							pDialogg.dismiss();
							pDialogg = null;
						}
					}
				});

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(negReq);
		
	}

	public static String decryptB(String cadena) 
	{ 
		BasicTextEncryptor s = new BasicTextEncryptor(); 
		s.setPassword("paloverde"); 
		String devuelve = ""; 
		try 
		{ 
			devuelve = s.decrypt(cadena); 
		} 
		catch (Exception e) 
		{ 
			devuelve = e.getMessage();
		} 
		return devuelve; 
	} 
	/////////////////////////////////////////
	public void asigna_icono(ActionBar.Tab tab)
	{
		switch (tab.getPosition()) 
		{
			case 0:
				tab.setIcon(android.R.drawable.ic_dialog_map);
				return;
			case 1:
				tab.setIcon(mResources.getDrawable(R.drawable.ic_action_view_as_list));
				return;
			case 2:
				return;
		}
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
	}

/////////////////////////////////////////////////
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		lista_llTN = (LinearLayout)findViewById(R.id.lista_llTiposNegocios);
		lista_llNE= (LinearLayout)findViewById(R.id.lista_llNegociosEncontrados);
		lista_BA= (LinearLayout)findViewById(R.id.lista_busquedaAvanzada);
		lista_vista=(LinearLayout)findViewById(R.id.lista_vistaNegocio);
		
		mapa_llTN = (LinearLayout)findViewById(R.id.mapa_llTiposNegocios);
		mapa_CM= (LinearLayout)findViewById(R.id.contenedor_mapa);
		mapa_BA= (LinearLayout)findViewById(R.id.mapa_busquedaAvanzada);
		mapa_vista=(LinearLayout)findViewById(R.id.mapa_vistaNegocio);
		
		if (item.getItemId() == R.id.action_search) 
		{
			

			if(cu.isTab()==true)
			{
				// estoy en lista
				if(lista_llTN.getVisibility() ==View.VISIBLE)
				{
					finish();
				}else
				if(lista_vista.getVisibility()==View.VISIBLE)
				{
					lista_llTN.setVisibility(View.GONE);
					lista_llNE.setVisibility(View.VISIBLE);
					lista_BA.setVisibility(View.GONE);
					lista_vista.setVisibility(View.GONE);
				}
				else
				if(lista_llNE.getVisibility()==View.VISIBLE)
				{
					lista_llTN.setVisibility(View.VISIBLE);
					lista_llNE.setVisibility(View.GONE);
					lista_BA.setVisibility(View.GONE);
					lista_vista.setVisibility(View.GONE);
				}else
				if(lista_BA.getVisibility()==View.VISIBLE)
				{
					lista_llTN.setVisibility(View.VISIBLE);
					lista_llNE.setVisibility(View.GONE);
					lista_BA.setVisibility(View.GONE);
					lista_vista.setVisibility(View.GONE);
				}
			}
			if(cu.isTab()==false)
			{
				// estoy en mapa
				if(mapa_llTN.getVisibility() ==View.VISIBLE)
				{
					finish();
				}else
				if(mapa_vista.getVisibility()==View.VISIBLE)
				{
					mapa_llTN.setVisibility(View.GONE);
					mapa_CM.setVisibility(View.VISIBLE);
					mapa_BA.setVisibility(View.GONE);
					mapa_vista.setVisibility(View.GONE);
				}
				else
				if(mapa_CM.getVisibility()==View.VISIBLE)
				{
					mapa_llTN.setVisibility(View.VISIBLE);
					mapa_CM.setVisibility(View.GONE);
					mapa_BA.setVisibility(View.GONE);
					mapa_vista.setVisibility(View.GONE);
				}else
				if(mapa_BA.getVisibility()==View.VISIBLE)
				{
					mapa_llTN.setVisibility(View.VISIBLE);
					mapa_CM.setVisibility(View.GONE);
					mapa_BA.setVisibility(View.GONE);
					mapa_vista.setVisibility(View.GONE);
				}
			}
			return true;
		}
		if(item.getItemId()==R.id.action_configBA)
		{

			if(cu.isTab()==true)
			{
				cargarPreferenciasL();
				// estoy en lista
				if(lista_BA.getVisibility() ==View.GONE)
				{
					lista_vista.setVisibility(View.GONE);
					lista_llTN.setVisibility(View.GONE);
					lista_llNE.setVisibility(View.GONE);
					lista_BA.setVisibility(View.VISIBLE);
				}
				else
				if(lista_BA.getVisibility() ==View.VISIBLE)
				{
					lista_llTN.setVisibility(View.VISIBLE);
					lista_llNE.setVisibility(View.GONE);
					lista_BA.setVisibility(View.GONE);
					lista_vista.setVisibility(View.GONE);
				}
			}
			if(cu.isTab()==false)
			{
				cargarPreferenciasM();
				// estoy en mapa
				if(mapa_BA.getVisibility() ==View.GONE)
				{
					mapa_vista.setVisibility(View.GONE);
					mapa_llTN.setVisibility(View.GONE);
					mapa_CM.setVisibility(View.GONE);
					mapa_BA.setVisibility(View.VISIBLE);
				}
				else
				if(mapa_BA.getVisibility() ==View.VISIBLE)
				{
					mapa_llTN.setVisibility(View.VISIBLE);
					mapa_CM.setVisibility(View.GONE);
					mapa_BA.setVisibility(View.GONE);
					mapa_vista.setVisibility(View.GONE);
				}
			}
			return true;
		}
		if(item.getItemId()==R.id.action_configAcercade)
		{
			Intent intent = new Intent(this, Acerca.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
    public void cargarPreferenciasL()
    {
		sbDistancia = (SeekBar) findViewById(R.id.lista_sbDistancia);
		tvDistancia= (TextView) findViewById(R.id.lista_tvDistancia);
		sbPrecios = (SeekBar) findViewById(R.id.lista_sbPrecio);
		tvPrecios = (TextView) findViewById(R.id.lista_tvPrecio);
		SharedPreferences prefs = getSharedPreferences("preferenciasMiApp", Context.MODE_PRIVATE);
        
		sbDistancia.setProgress(Integer.parseInt(prefs.getString("Distancia", "0")));
		if(sbDistancia.getProgress()>=0 && sbDistancia.getProgress()<=10)
			tvDistancia.setText("Cerca");
		if(sbDistancia.getProgress()>10 && sbDistancia.getProgress()<=20)
			tvDistancia.setText("No tan ejos");
		if(sbDistancia.getProgress()>20 && sbDistancia.getProgress()<=30)
			tvDistancia.setText("Lejos");
		if(sbDistancia.getProgress()>30 && sbDistancia.getProgress()<40)
			tvDistancia.setText("Muy lejos");
		if(sbDistancia.getProgress()==40)
			tvDistancia.setText("Todo");
        
        
        sbPrecios.setProgress(Integer.parseInt(prefs.getString("Precio", "0")));
        if( sbPrecios.getProgress()==50)
        	tvPrecios.setText("Todo");
        else
        	tvPrecios.setText("$"+prefs.getString("Precio", "0"));
        
        
        prefs.getBoolean("preferenciasGuardadas", false);
      
    }
    public void cargarPreferenciasM()
    {
		sbDistancia = (SeekBar) findViewById(R.id.sbDistancia);
		tvDistancia= (TextView) findViewById(R.id.tvDistancia);
		sbPrecios = (SeekBar) findViewById(R.id.sbPrecio);
		tvPrecios = (TextView) findViewById(R.id.tvPrecio);
		SharedPreferences prefs = getSharedPreferences("preferenciasMiApp", Context.MODE_PRIVATE);
        
		sbDistancia.setProgress(Integer.parseInt(prefs.getString("Distancia", "0")));
		if(sbDistancia.getProgress()>=0 && sbDistancia.getProgress()<=10)
			tvDistancia.setText("Cerca");
		if(sbDistancia.getProgress()>10 && sbDistancia.getProgress()<=20)
			tvDistancia.setText("No tan ejos");
		if(sbDistancia.getProgress()>20 && sbDistancia.getProgress()<=30)
			tvDistancia.setText("Lejos");
		if(sbDistancia.getProgress()>30 && sbDistancia.getProgress()<40)
			tvDistancia.setText("Muy lejos");
		if(sbDistancia.getProgress()==40)
			tvDistancia.setText("Todo");
        
        
        sbPrecios.setProgress(Integer.parseInt(prefs.getString("Precio", "0")));
        if( sbPrecios.getProgress()==50)
        	tvPrecios.setText("Todo");
        else
        	tvPrecios.setText("$"+prefs.getString("Precio", "0"));
        
        
        prefs.getBoolean("preferenciasGuardadas", false);
      
    }
	@Override
	public void onTabSelected(ActionBar.Tab tab,FragmentTransaction fragmentTransaction) 
	{
		lista_llTN = (LinearLayout)findViewById(R.id.lista_llTiposNegocios);
		lista_llNE= (LinearLayout)findViewById(R.id.lista_llNegociosEncontrados);
		lista_BA= (LinearLayout)findViewById(R.id.lista_busquedaAvanzada);
		lista_vista=(LinearLayout)findViewById(R.id.lista_vistaNegocio);
		
		mapa_llTN = (LinearLayout)findViewById(R.id.mapa_llTiposNegocios);

		
		android.support.v4.app.FragmentManager myFM = getSupportFragmentManager();
		SupportMapFragment myMAPF = (SupportMapFragment) myFM.findFragmentById(R.id.map);
		LinearLayout mapaF = (LinearLayout) findViewById(R.id.contenedor_mapa);
		
		mViewPager.setCurrentItem(tab.getPosition());
		if(myMAPF != null  && mapaF!=null)
		{
			if(tab.getPosition() == 0)
			{	
				mapaF.setVisibility(View.VISIBLE);
				mapaF.setVisibility(View.GONE);
				myMAPF.getView().setVisibility(View.VISIBLE);
				mapa_llTN.setVisibility(View.VISIBLE);
				cu.setTab(false);

			}
			if(tab.getPosition() == 1)
			{
				myMAPF.getView().setVisibility(View.GONE);
				mapaF.setVisibility(View.GONE);
				mapaF.setVisibility(View.VISIBLE);
				
				cu.setTab(true);
				lista_llTN.setVisibility(View.VISIBLE);
				lista_llNE.setVisibility(View.GONE);
				lista_BA.setVisibility(View.GONE);
				lista_vista.setVisibility(View.GONE);
			}
		}

	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,FragmentTransaction fragmentTransaction) 
	{
		
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,FragmentTransaction fragmentTransaction) 
	{
		
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter 
	{

		public SectionsPagerAdapter(FragmentManager fm) 
		{
			super(fm);
		}

		@Override
		public Fragment getItem(int position) 
		{
			switch (position) 
			{
			case 0:
				Fragment f = new Mapa();
				return f;
			case 1:
				return new Lista();
			default:
					return null;
			}
		}

		@Override
		public int getCount() 
		{
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) 
		{
			switch (position) 
			{
				case 0:
					return "  Mapa";
				case 1:
					return "  Lista";
				default:
					return "  ";
			}
		}
	}


}
