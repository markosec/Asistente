package com.example.asistente;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static final String EXTRA_MESSAGE = "Empezando a escuchar";
	public static final String OTRO_MENSAJE = "Finalizando escucha";
	
	private LeeSms receptor = new LeeSms();
	private Voz hablador = new Voz();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/** Called when the user clicks the Iniciar button */
	public void empezar(View view) {

		// Escuchar por mensajitos que llegan...
		IntentFilter filtro = new IntentFilter();
		filtro.addAction("android.provider.Telephony.SMS_RECEIVED");
		this.registerReceiver(receptor, filtro);
		Estados jefe = Estados.getInstance(view.getContext());		
		jefe.setLector(receptor);

		// Escuchar por conexiones bluetooth..
		
		IntentFilter filtroBt = new IntentFilter();
		EscuchaBt conector    = new EscuchaBt();
		filtroBt.addAction("android.bluetooth.device.action.ACL_CONNECTED");
		filtroBt.addAction("android.bluetooth.device.action.ACL_DISCONNECTED");
		filtroBt.addAction("android.bluetooth.device.action.BOND_STATE_CHANGED");
		this.registerReceiver(conector, filtroBt);	
		
	
		
		
		

	}

	public void terminar(View view) {
		// Escuchar por conexiones bluetooth..
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			// Device does not support Bluetooth
			Toast.makeText(view.getContext(), "Bluetooth no soportado",
					Toast.LENGTH_SHORT).show();
		} else {
			if (mBluetoothAdapter.isEnabled()) {
				//mBluetoothAdapter.disable();
			}
		}

		this.unregisterReceiver(receptor);
		//jefe.terminar();

		//hablador.decir("Adios");
		//hablador.terminar();
		
		this.finish();
		
	}

}
