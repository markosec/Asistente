package com.example.asistente;

import java.util.Set;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class Estados {

	private Voz 			hablador  = null;
	private LeeSms 			lector 	  = null;
	private Context 		contexto  = null;
	private BluetoothDevice auricular = null;
	private static Estados  yo        = null;


	// private static final int REQUEST_ENABLE_BT = 1;

	public Estados() {
		// TODO Auto-generated constructor stub
	
	}

	
	@SuppressLint("InlinedApi")
	public void buscarAuricular()
	{
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			// Device does not support Bluetooth
			Toast.makeText(contexto, "Bluetooth no soportado",
					Toast.LENGTH_SHORT).show();
		} else {			
			
			if (!mBluetoothAdapter.isEnabled()) {
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				if (contexto != null)
					contexto.startActivity(enableBtIntent);
			}
			if (mBluetoothAdapter.isEnabled()) {

				Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
				// If there are paired devices
				if (pairedDevices.size() > 0) {
					// Loop through paired devices
					for (BluetoothDevice device : pairedDevices) {
						if (device.getName().contentEquals("N6")) {
							
							String st = "Se encontro: ";
							st += device.getName();
							Toast.makeText(contexto, st, Toast.LENGTH_SHORT).show();
							if (device.getBondState() != BluetoothDevice.BOND_BONDED)
							{
								Toast.makeText(contexto, "pero no esta conectado",Toast.LENGTH_SHORT).show();
								auricular = null;
							}
							else if( mBluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET) == BluetoothAdapter.STATE_CONNECTED)
								auricular = device;
							else
								auricular = null;
						}
					}
				}

			}

		}
		
	}
	
		public void setHablador(Voz cual) {
		hablador = cual;
		if (auricular != null)
			hablador.decir("sistema de voz, iniciado");
	}

	public void setLector(LeeSms cual) {
		lector = cual;
	}

	public void setContexto(Context cont) {
		contexto = cont;
	}

	public static Estados getInstance() {

		if (yo == null) {
			yo = new Estados();
		}
		return yo;
	}
	
	public static Estados getInstance(Context cont) {

		if (yo == null) {
			yo = new Estados();
		}		
		yo.setContexto(cont);
		yo.iniciar();
		return yo;

	}
	public void iniciar()
	{

		if (lector == null)
			lector   = new LeeSms();
		if (hablador == null)
		{
			hablador = new Voz();
			hablador.iniciar(contexto);
		}
		
		if (auricular == null)
		    this.buscarAuricular();
	}
	public void btConectado()
	{
		buscarAuricular();
		if (auricular != null)
		{	
			hablador.decir("Casco conectado");
		}
	}
	public void btDesconectado()
	{
		auricular = null;
		if (hablador != null)
			hablador.callate();
	}
	
	public void nuevoMensaje(String quien, String texto)
	{
		if (auricular != null)
		{
			if (hablador != null)
			{
				hablador.avisar(quien,texto);
				
			}
			
		}
		
	}
}
