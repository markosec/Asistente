package com.example.asistente;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.speech.tts.UtteranceProgressListener;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
public class FinHablar extends UtteranceProgressListener {

	public FinHablar() {
	}

	@Override
	public void onDone(String utteranceId) {

		Intent i = new Intent("com.mark.INTENT1");
		Estados jefe = Estados.getInstance();
		Context contexto = jefe.darContexto();
        contexto.sendBroadcast(i);

	}

	@Override
	public void onError(String arg0) {
		
	}

	@Override
	public void onStart(String arg0) {
	
	}

}
