package com.pvm;
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
import android.content.SharedPreferences;
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
	private RadioButton radioMapa;
	private RadioButton radioLista;
	private LinearLayout llc;
	private LinearLayout lllst;
	private LinearLayout llba;
	private LinearLayout llv;
	private LinearLayout llmapa;

	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	// URL to get contacts JSON
	private static String url = "http://paqueteubiquen.esy.es/json.json";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(00,154,49)));
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
		JsonObjectRequest negReq = new JsonObjectRequest(url,null, new Response.Listener<JSONObject>() 
				{
					@Override
					public void onResponse(JSONObject response)
					{
						try 
						{
							ba.respaldarNegocios(response.getJSONArray("negocios").toString(),getApplicationContext(), "negocios.txt");
							ba.respaldarNegocios(response.getJSONArray("iconos").toString(), getApplicationContext(), "iconos.txt");

						} catch (JSONException e) 
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

	
	//BOTONES ACERCADE//////////////////////////
	public void comentarButtonClicked(View view)
	{	}
	public void registroButtonClicked(View view)
	{	}
	public void enviarCButtonClicked(View view)
	{	}
	public void enviarRButtonClicked(View view)
	{	}
	public void cancelCButtonClicked(View view)
	{	}
	public void cancelRButtonClicked(View view)
	{	}

	/////////////////////////////////////////
	//BOTONES MAPA//////////////////////////
	public void hateButtonClicked(View view)
	{	}
	public void coffeButtonClicked(View view)
	{	}
	public void expendiosButtonClicked(View view)
	{	}
	public void baresButtonClicked(View view)
	{	}
	public void sushiButtonClicked(View view)
	{	}
	public void listViewClicked(View view)
	{	}
	public void omButtonClicked(View view){ 
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
				tab.setIcon(android.R.drawable.ic_dialog_info);
				return;
			case 2:
				return;
		}
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
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
public void mostrarContenido(boolean negocios,boolean lista,boolean mapa,boolean opcAvanzadas,boolean vista)
{
	if(negocios)
	{
		llv.setVisibility(View.GONE);
		lllst.setVisibility(View.GONE);
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
		lllst.setVisibility(View.VISIBLE);
	}

	if(mapa)
	{
		llv.setVisibility(View.GONE);
		llc.setVisibility(View.GONE);
		lllst.setVisibility(View.GONE);
		llba.setVisibility(View.GONE);
		llmapa.setVisibility(View.VISIBLE);
	}
	if(opcAvanzadas)
	{
		llv.setVisibility(View.GONE);
		llc.setVisibility(View.GONE);
		lllst.setVisibility(View.GONE);
		llmapa.setVisibility(View.GONE);
		llba.setVisibility(View.VISIBLE);
	}
	if(lista)
	{
		llc.setVisibility(View.GONE);
		lllst.setVisibility(View.GONE);
		llmapa.setVisibility(View.GONE);
		llba.setVisibility(View.GONE);
		llv.setVisibility(View.VISIBLE);
	}
	
}
/////////////////////////////////////////////////
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		llc = (LinearLayout)findViewById(R.id.list_view_contenedor);
		lllst = (LinearLayout)findViewById(R.id.llNegociosB);
		llba = (LinearLayout)findViewById(R.id.busquedaAvanzada);
		llv = (LinearLayout)findViewById(R.id.vistaNegocio);
		llmapa  = (LinearLayout)findViewById(R.id.contenedor_mapa);
		if (item.getItemId() == R.id.action_search) 
		{
	       	 if (llc.getVisibility() == View.GONE)
	         {
	             animar(true);
	             mostrarContenido(true,false,false,false,false);	             
	         }
	       	 if(llba.getVisibility()==View.VISIBLE)
	       	 {
	       		 animar(true);
	       		 mostrarContenido(true,false,false,false,false);
	       	 }
			return true;
		}
		if(item.getItemId()==R.id.action_configBA)
		{
	       	 if (llc.getVisibility() == View.VISIBLE || llmapa.getVisibility()==View.VISIBLE || lllst.getVisibility()==View.VISIBLE )
	         {
	       		cargarPreferencias();
	       		mostrarContenido(false,false,false,true,false);
	         }

			return true;
		}
		return super.onOptionsItemSelected(item);
	}
    public void cargarPreferencias()
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
        
    	radioMapa = (RadioButton)findViewById(R.id.rbMapa);
    	radioLista = (RadioButton)findViewById(R.id.rbLista);
    	String d = prefs.getString("DespliegueResultados", "");
        if(d.equals("Mapa"))
        {
        	radioMapa.setChecked(true);
        	radioLista.setChecked(false);
        }
        if(d.equals("Lista"))
        {
        	radioMapa.setChecked(false);
        	radioLista.setChecked(true);
        }
        
        prefs.getBoolean("preferenciasGuardadas", false);
      
    }
	@Override
	public void onTabSelected(ActionBar.Tab tab,FragmentTransaction fragmentTransaction) 
	{
		android.support.v4.app.FragmentManager myFM = getSupportFragmentManager();
		SupportMapFragment myMAPF = (SupportMapFragment) myFM.findFragmentById(R.id.map);
		LinearLayout acercade = (LinearLayout) findViewById(R.id.cancelCBTN);
		LinearLayout mapaF = (LinearLayout) findViewById(R.id.mapa_frag);

		mViewPager.setCurrentItem(tab.getPosition());
		
		if(myMAPF != null && acercade!= null && mapaF!=null)
		{
			if(tab.getPosition()==1)
			{
				
				myMAPF.getView().setVisibility(View.GONE);
				acercade.setVisibility(View.GONE);
				acercade.setVisibility(View.VISIBLE);
				mapaF.setVisibility(View.GONE);
				mapaF.setVisibility(View.VISIBLE);
			}
			else
			{
				acercade.setVisibility(View.GONE);
				acercade.setVisibility(View.VISIBLE);
				mapaF.setVisibility(View.GONE);
				mapaF.setVisibility(View.VISIBLE);
				myMAPF.getView().setVisibility(View.VISIBLE);
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
				return new AcercaDe();
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
					return "  Acerca de";
				default:
					return "  ";
			}
		}
	}


}
