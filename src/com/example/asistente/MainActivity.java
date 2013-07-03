package com.example.asistente;

import java.util.ArrayList;
import java.util.Locale;

import com.google.android.voiceime.VoiceRecognitionTrigger;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.IntentFilter;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static final String EXTRA_MESSAGE = "Empezando a escuchar";
	public static final String OTRO_MENSAJE = "Finalizando escucha";

	
	private VoiceRecognitionTrigger mVoiceRecognitionTrigger = null;
	private LeeSms receptor = new LeeSms();
	private EscuchaBt conector = null;
	private Telefono llamadas = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.init();
		findViewById(R.id.button1).setEnabled(false);

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

		this.finish();

	}
	

	 
 public void probar(View view)
 {
	
	 Estados jefe = Estados.getInstance(getApplicationContext());
	 jefe.probar();
	 /*
	  
 	 private SpeechRecognizer sr = null;
	 private MyRL listener = null;
	 sr = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
	 listener = new MyRL();
	 sr.setRecognitionListener(listener); 
	 Intent algo = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
	 algo.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
	 algo.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES");
	 algo.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,5); 
	 sr.startListening(algo);
	 */
	 //sr.startListening(RecognizerIntent.getVoiceDetailsIntent(getApplicationContext()));
	 
	 
 }
}
