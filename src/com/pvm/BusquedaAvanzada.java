package com.pvm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
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
			if(precio == 50)
			{
				return true;
			}
			else
			{
				boolean precioOK=false;
				for (int mi = 0; mi < menu.length(); mi++) 
				{
					JSONObject m;
					try 
					{
						m = menu.getJSONObject(mi);
						if( Double.parseDouble(m.getString("precio")) <= precio )
						{
							precioOK =true;
							break;
						}
					} 
					catch (JSONException e) 
					{
						precioOK=false;
						e.printStackTrace();
					}
				}
				return precioOK;
			}
		
	}
	
	public void respaldarNegocios(String negocios, Context c,String nomArchivo)
	{
		try
		{
		    OutputStreamWriter fout= new OutputStreamWriter(c.openFileOutput(nomArchivo, c.MODE_PRIVATE));
		 
		    fout.write(negocios);
		    fout.close();
		}
		catch (Exception ex)
		{
		    Log.e("Ficheros", "Error al escribir fichero a memoria interna");
		}
	}

	public String leerNegocios(Context c,String nomArchivo)
	{
		String linea = "";
		String todo = "";
		try
		{
		    BufferedReader brin =   new BufferedReader(new InputStreamReader(c.openFileInput(nomArchivo)));
		    linea = brin.readLine();
	         while (linea != null) 
	         {
	        	 todo = todo + linea + " ";
	        	 linea = brin.readLine();
	         }	
		    brin.close();
		    return todo;
		}
		catch (Exception ex)
		{
		    Log.e("Ficheros", "Error al leer fichero desde memoria interna");
		    return "";
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
                //duración en milisegundos
        animation.setDuration(500);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.25f);
 
        //llc.setLayoutAnimation(controller);
        //llc.startAnimation(animation);
    }
}
