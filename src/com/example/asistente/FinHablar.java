package com.example.asistente;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.UtteranceProgressListener;

@SuppressLint("NewApi")
public class FinHablar extends UtteranceProgressListener {

	public FinHablar() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onDone(String utteranceId) {

		Intent i = new Intent("com.mark.INTENT1");
		Estados jefe = Estados.getInstance();
		Context contexto = jefe.darContexto();
        contexto.sendBroadcast(i);
        System.out.println("Fin Hablar:On done!");

	}

	@Override
	public void onError(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart(String arg0) {
		// TODO Auto-generated method stub
	
	}

}
