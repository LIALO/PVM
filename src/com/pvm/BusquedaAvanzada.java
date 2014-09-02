package com.pvm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;

public class BusquedaAvanzada 
{

	public boolean distancia(double lat_a,double lng_a, double lat_b, double lon_b,int distancia)
	{
		//cerca 500mts no muy lejos 1 km lejos 2 km  y 4 y todo		
		if(distancia  == 40)
				return true;
		else
		{
			if(distancia  >= 0 && distancia <10)
				distancia = 500;
			else
				if(distancia  >= 10 && distancia <30)
					distancia = 1000;
				else
					if(distancia  >= 30 && distancia <40)
						distancia = 2000;		
	
	
			int Radius = 6371000; //Radio de la tierra
			double lat1 = lat_a ;
			double lat2 = lat_b ;
			double lon1 = lng_a ;     
			double lon2 = lon_b ;
			double dLat = Math.toRadians(lat2-lat1);
			double dLon = Math.toRadians(lon2-lon1);
			double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon /2) * Math.sin(dLon/2);
			double c = 2 * Math.asin(Math.sqrt(a));
			double cc= (double) (Radius * c);
			if(cc<distancia)
				return true;
			else
				return false;
		}

	}
	public boolean precio(JSONArray menu,int precio)
	{
		try 
		{
			if(precio == 50)
			{
				return true;
			}
			else
			{
				boolean precioOK=false;
				for (int mi = 0; mi < menu.length(); mi++) 
				{
					JSONObject m = menu.getJSONObject(mi);
					
					if( Integer.parseInt(m.getString("precio")) <= precio )
					{
						precioOK =true;
						break;
					}
				}
				return precioOK;
			}
		} 
		catch (JSONException e) 
		{
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
		}
	}
	
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
                //duraci�n en milisegundos
        animation.setDuration(500);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.25f);
 
        //llc.setLayoutAnimation(controller);
        //llc.startAnimation(animation);
    }
}