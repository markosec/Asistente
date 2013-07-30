package com.example.asistente;

import java.util.Calendar;

import android.media.AudioManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.Notification;
import android.bluetooth.BluetoothAdapter;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static final String EXTRA_MESSAGE = "Empezando a escuchar";
	public static final String OTRO_MENSAJE = "Finalizando escucha";

	
	private LeeSms    receptor = new LeeSms();
	private EscuchaBt conector = null;
	private Telefono  llamadas = null;

	private Logueador loguear = new Logueador();
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		this.init();
		
		Calendar c = Calendar.getInstance(); 
		
		loguear.loguear("Inicio: " + c.get(Calendar.HOUR_OF_DAY) 
				                   + c.get(Calendar.MINUTE)
				                   + c.get(Calendar.SECOND));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/** Called when the user clicks the Iniciar button */
	public void empezar(View view) {

		this.init();

	}

	public void init() {
		// Escuchar por mensajitos que llegan...
		IntentFilter filtro = new IntentFilter();
		filtro.addAction("android.provider.Telephony.SMS_RECEIVED");
		this.registerReceiver(receptor, filtro);
		Estados jefe = Estados.getInstance(getApplicationContext());
		jefe.setLector(receptor);

		// Escuchar por conexiones bluetooth..
		IntentFilter filtroBt  = new IntentFilter();
		conector     = new EscuchaBt();
		filtroBt.addAction("android.bluetooth.device.action.ACL_CONNECTED");
		filtroBt.addAction("android.bluetooth.device.action.ACL_DISCONNECTED");
		filtroBt.addAction("android.bluetooth.device.action.BOND_STATE_CHANGED");
		this.registerReceiver(conector, filtroBt);

		// Estar atento a las llamadas...
		filtroBt = new IntentFilter();
		// EscuchaBt conector = new EscuchaBt();
		llamadas = new Telefono();
		filtroBt.addAction("android.intent.action.PHONE_STATE");
		this.registerReceiver(llamadas, filtroBt);
		
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC); 
		
		jefe.setVisual(this);
		
		//Custom intent?
		filtro = new IntentFilter();
		filtro.addAction("com.mark.INTENT1");
		CustomBroadCastReceiver finHabla = new CustomBroadCastReceiver();
		this.registerReceiver(finHabla,filtro);
		
		
	}

	
	public void terminar(View view) {
		// Escuchar por conexiones bluetooth..
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			// Device does not support Bluetooth
			Toast.makeText(view.getContext(), "Bluetooth no soportado",
					Toast.LENGTH_SHORT).show();
		} else {
			if (mBluetoothAdapter.isEnabled()) {
				mBluetoothAdapter.disable();
			}
		}

		this.unregisterReceiver(receptor);
		this.unregisterReceiver(conector);
		this.unregisterReceiver(llamadas);
		Estados jefe = Estados.getInstance(getApplicationContext());
		jefe.terminar();
		this.finish();

	}
	
public void cambiarTitulo(String texto)
{
	TextView scroller = (TextView) findViewById(R.id.texteo);
	scroller.setText(texto);

}
	 
 public void probar(View view)
 {
	 Estados jefe = Estados.getInstance(getApplicationContext());
	 jefe.probar();
//	 Notification noti = new Notification.Builder(getApplicationContext())
//     .setContentTitle("Mensaje de prueba")
//     .setContentText("Esto vendria a ser el cuerpo de la notificacion")
//     .setSmallIcon(R.drawable.ic_launcher)
//     //.setLargeIcon(aBitmap)
//     .build();
 }
}
