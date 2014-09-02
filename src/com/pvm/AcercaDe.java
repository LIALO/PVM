package com.pvm;

import com.pvm.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AcercaDe extends Fragment 
{

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		View rootView = inflater.inflate(R.layout.fragment_acercade, container, false);
		return rootView;
	}
	
	@Override
	public void onStart() 
	{
		super.onStart();
	    LinearLayout acerca = (LinearLayout) getActivity().findViewById(R.id.cancelCBTN);
	    acerca.setVisibility(View.INVISIBLE);
	    acerca.setVisibility(View.VISIBLE);
		Activity s = getActivity();
		LinearLayout registro = (LinearLayout) s.findViewById(R.id.layout_unete);
		LinearLayout comentarios = (LinearLayout) s.findViewById(R.id.layout_comentarios);
		registro.setVisibility(View.GONE);
		comentarios.setVisibility(View.GONE);
		
		 Button comentariosButon = (Button) getActivity().findViewById(R.id.comentariosButton);
		 Button RegistroButon = (Button) getActivity().findViewById(R.id.registrarseButton);
		 Button enviarCButon = (Button) getActivity().findViewById(R.id.enviarCButton);
		 Button enviarRButon = (Button) getActivity().findViewById(R.id.enviarRButton);
		 Button cancelarCButon = (Button) getActivity().findViewById(R.id.cancelCBoton);
		 Button cancelarRButon = (Button) getActivity().findViewById(R.id.cancelRBoton);
		 comentariosButon.setOnClickListener(new OnClickListener(){
		        @Override
		        public void onClick(View arg0) 
		        {
		        	Activity s = getActivity();
		        	EditText tvComentarios = (EditText) s.findViewById(R.id.et_comentario);
		    		LinearLayout registro = (LinearLayout) s.findViewById(R.id.layout_unete);
		    		LinearLayout comentarios = (LinearLayout) s.findViewById(R.id.layout_comentarios);
		    		LinearLayout opcacercade = (LinearLayout) s.findViewById(R.id.opciones_acercade);
		    		LinearLayout opcacercade2 = (LinearLayout) s.findViewById(R.id.opciones_acercade2);
		    		tvComentarios.setText("");

		    		registro.setVisibility(View.VISIBLE);
		    		comentarios.setVisibility(View.GONE);
		    		opcacercade.setVisibility(View.GONE);
		    		opcacercade2.setVisibility(View.GONE);
		    		
		        }
		 
		    });
		 RegistroButon.setOnClickListener(new OnClickListener(){
		        @Override
		        public void onClick(View arg0) 
		        {
		        	Activity s = getActivity();
		        	EditText tvNombre= (EditText) s.findViewById(R.id.etnomlReg);
		        	EditText tvDireccion= (EditText) s.findViewById(R.id.etdirReg);
		        	EditText tvTelefono= (EditText) s.findViewById(R.id.ettelReg);
		    		LinearLayout registro = (LinearLayout) s.findViewById(R.id.layout_unete);
		    		LinearLayout comentarios = (LinearLayout) s.findViewById(R.id.layout_comentarios);
		    		LinearLayout opcacercade = (LinearLayout) s.findViewById(R.id.opciones_acercade);
		    		LinearLayout opcacercade2 = (LinearLayout) s.findViewById(R.id.opciones_acercade2);
		    		tvNombre.setText("");
		    		tvDireccion.setText("");
		    		tvTelefono.setText("");
		    		registro.setVisibility(View.GONE);
		    		comentarios.setVisibility(View.VISIBLE);
		    		opcacercade.setVisibility(View.GONE);
		    		opcacercade2.setVisibility(View.GONE);
		        }
		 
		    });
		 enviarCButon.setOnClickListener(new OnClickListener(){
		        @Override
		        public void onClick(View arg0) 
		        {
		        	//Codigo para enviar el comentario
		        	EditText comentario = (EditText)getActivity().findViewById(R.id.et_comentario);
		        	sendEmail("Comentario",comentario.getText().toString());
		        	ocultar(getActivity());
		        }
		 
		    });
		 enviarRButon.setOnClickListener(new OnClickListener(){
		        @Override
		        public void onClick(View arg0) 
		        {
		        	//codigo para enviar datos del registro
		        	Activity s = getActivity();
		        	EditText nombre = (EditText)getActivity().findViewById(R.id.etnomlReg);
		        	EditText direccion = (EditText)getActivity().findViewById(R.id.etdirReg);
		        	EditText telefono = (EditText)getActivity().findViewById(R.id.ettelReg);
		        	sendEmail("Registro",nombre.getText().toString()+"\n\r"+direccion.getText().toString()+"\n\r"+telefono.getText().toString());
		        	ocultar(s);
		        }
		 
		    });
		 cancelarCButon.setOnClickListener(new OnClickListener(){
		        @Override
		        public void onClick(View arg0) 
		        {
		        	Activity s = getActivity();
		        	ocultar(s);
		        }
		 
		    });
		 cancelarRButon.setOnClickListener(new OnClickListener(){
		        @Override
		        public void onClick(View arg0) 
		        {
		        	Activity s = getActivity();
		        	ocultar(s);
		        }
		 
		    });
	}

	public void ocultar(Activity s)
	{
		LinearLayout registro = (LinearLayout) s.findViewById(R.id.layout_unete);
		LinearLayout comentarios = (LinearLayout) s.findViewById(R.id.layout_comentarios);
		LinearLayout opcacercade = (LinearLayout) s.findViewById(R.id.opciones_acercade);
		LinearLayout opcacercade2 = (LinearLayout) s.findViewById(R.id.opciones_acercade2);
		registro.setVisibility(View.GONE);
		comentarios.setVisibility(View.GONE);
		opcacercade.setVisibility(View.VISIBLE);
		opcacercade2.setVisibility(View.VISIBLE);
	}
	@Override
	public void onPause() 
	{
		super.onPause();
	}
	
	protected void sendEmail(String coment_reg_titulo,String coment_reg) 
	{ 
		Log.i("Send email", ""); 
		String[] TO = {"paloverdelapaz2014@gmail.com"}; 
		String[] CC = {""}; 
		Intent emailIntent = new Intent(Intent.ACTION_SEND); 
		emailIntent.setData(Uri.parse("mailto:")); 
		emailIntent.setType("text/plain"); 
		emailIntent.putExtra(Intent.EXTRA_EMAIL, TO); 
		emailIntent.putExtra(Intent.EXTRA_CC, CC); 
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, coment_reg_titulo); 
		emailIntent.putExtra(Intent.EXTRA_TEXT, coment_reg); 
		try 
		{ 
			startActivity(Intent.createChooser(emailIntent, "Enviando Comentario...")); 
			Log.i("Finished sending email...", ""); 
		} 
		catch (android.content.ActivityNotFoundException ex) 
		{ 
			Toast.makeText(getActivity(), "There is no email client installed.", Toast.LENGTH_SHORT).show(); 
			
		}
		
	}
	
	
}