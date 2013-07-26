package com.example.asistente;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CustomBroadCastReceiver extends BroadcastReceiver {

	public CustomBroadCastReceiver() {
		
	}

	@Override
	public void onReceive(Context arg0, Intent arg1) {

		Estados jefe = Estados.getInstance();
		jefe.termineDeHablar();
        
	}

}
