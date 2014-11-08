package com.pvm;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ControlTabla 
{
	Activity activity;
	
	public ControlTabla(Activity activity) {
		
		this.activity = activity;
	}

	public void Inicializa(TableLayout tabla,String dato,int span)
	{
		AgregarSeparador(tabla,span,3);
		AgregarRenglon(dato,tabla);
		AgregarSeparador(tabla,span,3);
		
	}
	public void AgregarSeparador(TableLayout tabla,int span,int alto)
	{
		// Línea que separa la cabecera de los datos
	    TableRow separador_cabecera = new TableRow(activity);
	    separador_cabecera.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	    FrameLayout linea_cabecera = new FrameLayout(activity);
	    TableRow.LayoutParams linea_cabecera_params = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT,alto);
	    linea_cabecera_params.span = span;
	    linea_cabecera.setBackgroundColor(Color.parseColor("#5aaca5"));
	    separador_cabecera.addView(linea_cabecera, linea_cabecera_params);
	    tabla.addView(separador_cabecera);
	}
	public void AgregarRenglonH(ArrayList<String> Datos,TableLayout tabla)
	{
	   
	 
	    for (int i = 0;i<Datos.size();i++)
	    {
	    	 // Cabecera de la tabla
		    TableRow cabecera = new TableRow(activity);
		    cabecera.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		    tabla.addView(cabecera);
	    	String Item = Datos.get(i);
	    	String [] ItemSplit = Item.split(" ");
	    	 // Textos de la cabecera
		    for (int ii = 0; ii < ItemSplit.length; ii++)
		    {
		    	if(!ItemSplit[ii].equals(" "))
		    	{
			       TextView columna = new TextView(activity);
			       columna.setLayoutParams(new TableRow.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			       columna.setText(ItemSplit[ii]);
			       columna.setTextColor(Color.parseColor("#34495e"));
			       columna.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
			       columna.setGravity(Gravity.LEFT);
			       columna.setPadding(15, 5, 5, 5);
			       cabecera.addView(columna);
		    	}
		    }
		    AgregarSeparador(tabla,3,2);
	    }
	    
	   
	}
	
	public void AgregarRenglon(String Dato,TableLayout tabla)
	{
	    	 // Cabecera de la tabla
		    TableRow cabecera = new TableRow(activity);
		    cabecera.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		    tabla.addView(cabecera);
	    	TextView columna = new TextView(activity);
			columna.setLayoutParams(new TableRow.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			columna.setText(Dato);
			columna.setTextColor(Color.parseColor("#34495e"));
			columna.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
			columna.setGravity(Gravity.LEFT);
			columna.setPadding(15, 5, 5, 5);
			cabecera.addView(columna);
	}
	
	public void AgregarRenglonM(ArrayList<String> Datos,TableLayout tabla)
	{
	   
	 
	    for (int i = 0;i<Datos.size();i++)
	    {
	    	 // Cabecera de la tabla
		    TableRow cabecera = new TableRow(activity);
		    cabecera.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		    tabla.addView(cabecera);
	    	String Item = Datos.get(i);
	    	TextView columna = new TextView(activity);
			columna.setLayoutParams(new TableRow.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			columna.setText(Item);
			columna.setTextColor(Color.parseColor("#34495e"));
			columna.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
			columna.setGravity(Gravity.LEFT);
			columna.setPadding(15, 5, 5, 5);
			cabecera.addView(columna);
			AgregarSeparador(tabla,1,2);
	    }
	}

}
