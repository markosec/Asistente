package com.example.asistente;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.media.MediaPlayer;
import android.util.Log;

public class Reproductor implements MediaPlayer.OnCompletionListener  {

	public Reproductor() {
		// TODO Auto-generated constructor stub
	}

	private List<String> archivos = new ArrayList<String>();
	private MediaPlayer mp = new MediaPlayer();
	private Logueador loguear = new Logueador();

	private int siguiente()
	{
		Random randomer = new Random();
		return randomer.nextInt(archivos.size() - 1  + 1) + 1;		
		
	}
	public boolean ocupado()
	{
		return mp.isPlaying();
		
	}
	public boolean tocarMusica() {
		
			if (archivos.size() == 0)
				this.incializar();
			if (archivos.size() == 0)
				return false;
		try {
			String tema;
			tema = archivos.get(this.siguiente());
			mp.setDataSource(tema);
			
			mp.prepare(); // might take long! (for buffering, etc)
			mp.start();
			Estados jefe = Estados.getInstance();
			jefe.cambieCancion(tema);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			loguear.loguear(e.getMessage());
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			loguear.loguear(e.getMessage());
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			loguear.loguear(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			loguear.loguear(e.getMessage());
		}
		return true; 
			
			
	}

	public void terminar()
	{
		mp.release();
		
	}
	
	   public void walk( String path ) {

	        File root = new File( path );
	        File[] list = root.listFiles();

	        if (list == null) return;

	        for ( File f : list ) {
	            if ( f.isDirectory() ) {
	                walk( f.getAbsolutePath() );
	                System.out.println( "Dir:" + f.getAbsoluteFile() );
	                
	            }
	            else {
	                System.out.println( "File:" + f.getAbsoluteFile());
	                archivos.add(f.getAbsoluteFile().getAbsolutePath());                
	            }
	        }
	    }
	
	public void incializar() {
		String yourPath = "/storage/sdcard1/DCIM/Musica/";
		this.walk(yourPath);
		loguear.loguear("Se agregan " + archivos.size() + " archivos inicialmente");
		mp.setOnCompletionListener(this);
		
	}

	public void pausar()
	{
		mp.pause();
		
	}
	public void continuar()
	{
		mp.start();
		Log.d("MC", "Continuar con la cancion");
	}
	@Override
	public void onCompletion(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		
		try {
			loguear.loguear("Cambio de cancion...");
			mp.reset();
			loguear.loguear("Reset on completion");
			String cancion;
			cancion = archivos.get(this.siguiente());
			loguear.loguear("Intento cambiar a: " + cancion);
			mp.setDataSource(cancion);
			mp.prepare(); // might take long! (for buffering, etc)
			mp.start();
			loguear.loguear("Funciono!");
			Estados jefe = Estados.getInstance();
			jefe.cambieCancion(cancion);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			loguear.loguear(e.getMessage());
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			loguear.loguear(e.getMessage());
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			loguear.loguear(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			loguear.loguear(e.getMessage());
		}
	}
}
