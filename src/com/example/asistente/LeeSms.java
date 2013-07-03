package com.example.asistente;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class LeeSms extends BroadcastReceiver {

	public LeeSms() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onReceive(Context arg0, Intent arg1) {

		Estados jefe = Estados.getInstance(arg0);
		Bundle bndl = arg1.getExtras();
		SmsMessage[] msg = null;
		String quien = "";
		String texto = "";
		if (null != bndl) {
			// ---retrieve the SMS message received---
			Object[] pdus = (Object[]) bndl.get("pdus");
			msg = new SmsMessage[pdus.length];
			for (int i = 0; i < msg.length; i++) {
				msg[i]  = SmsMessage.createFromPdu((byte[]) pdus[i]);
				quien   = msg[i].getOriginatingAddress();
				texto += msg[i].getMessageBody().toString(); 
			}
			jefe.nuevoMensaje(quien, texto);

		}

	}

}
