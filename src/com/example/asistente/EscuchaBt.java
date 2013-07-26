package com.example.asistente;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class EscuchaBt extends BroadcastReceiver {

	public EscuchaBt() {
	
	}

	@Override
	public void onReceive(Context cont, Intent inte) {
		// TODO Auto-generated method stub
			Estados jefe = Estados.getInstance(cont);
		   String action = inte.getAction();
	        if(action.equals("android.bluetooth.device.action.ACL_CONNECTED"))
	        {
	            jefe.btConectado();
	        }
	        if(action.equals("android.bluetooth.device.action.ACL_DISCONNECTED") )
	        {
	            jefe.btDesconectado();
	        }
	}

}
