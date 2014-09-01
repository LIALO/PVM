package com.pvm;
import com.google.android.gms.maps.SupportMapFragment;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;



public class MainActivity extends ActionBarActivity implements ActionBar.TabListener 
{
	private SeekBar sbPrecios;
	private TextView tvPrecios;
	private SeekBar sbDistancia;
	private TextView tvDistancia;
	LinearLayout llc ;
	LinearLayout llba ;
	LinearLayout mapall ;
	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
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
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab().setText(mSectionsPagerAdapter.getPageTitle(i)).setTabListener(this));
			
		}
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) 
		{
			ActionBar.Tab tab = actionBar.getTabAt(i);
			asigna_icono(tab);
		}
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
/////////////////////////////////////////////////
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		llc = (LinearLayout)findViewById(R.id.list_view_contenedor);
	 	mapall =(LinearLayout)findViewById(R.id.contenedor_mapa);
	 	llba = (LinearLayout)findViewById(R.id.busquedaAvanzada);
		int id = item.getItemId();
		if (id == R.id.action_search) 
		{
	       	 if (llc.getVisibility() == View.GONE)
	         {
	             animar(true);
	             llc.setVisibility(View.VISIBLE);
	             mapall.setVisibility(View.GONE);
	             llba.setVisibility(View.GONE);
	             
	         }
	       	 if(llba.getVisibility()==View.VISIBLE)
	       	 {
	       		 animar(true);
	             llc.setVisibility(View.VISIBLE);
	             mapall.setVisibility(View.GONE);
	             llba.setVisibility(View.GONE);
	       	 }
			return true;
		}
		if(id==R.id.action_configBA)
		{
	       	 if (llc.getVisibility() == View.VISIBLE ||mapall.getVisibility()==View.VISIBLE )
	         {
	       		cargarPreferencias();
	             llc.setVisibility(View.GONE);
	             mapall.setVisibility(View.GONE);
	             llba.setVisibility(View.VISIBLE);
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
		sbPrecios.setProgress(Integer.parseInt(prefs.getString("Precio", "0")));
		if(sbDistancia.getProgress()>=0 && sbDistancia.getProgress()<=10)
			tvDistancia.setText("Cerca");
		if(sbDistancia.getProgress()>10 && sbDistancia.getProgress()<=20)
			tvDistancia.setText("No tan ejos");
		if(sbDistancia.getProgress()>20 && sbDistancia.getProgress()<=30)
			tvDistancia.setText("Lejos");
		if(sbDistancia.getProgress()>30 && sbDistancia.getProgress()<=40)
			tvDistancia.setText("Muy lejos");
        tvPrecios.setText("$"+prefs.getString("Precio", "0"));
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
