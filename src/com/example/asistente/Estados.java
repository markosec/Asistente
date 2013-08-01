package com.example.asistente;

import java.util.Locale;
import java.util.Set;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Toast;

public class Estados {

	private Voz hablador = null;
	private LeeSms lector = null;
	private Context contexto = null;
	private BluetoothDevice auricular = null;
	private static Estados yo = null;
	private boolean llamada = false;
	private SpeechRecognizer sr = null;
	private MyRL listener = null;
	private String msjPendiente = "";
	private boolean atento = false;
	private Reproductor tocadiscos = null;
	private boolean pausaMusica = false;
	private MainActivity visual = null;


	public Estados() {


	}

	@SuppressLint("InlinedApi")
	public void buscarAuricular() {
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		if (mBluetoothAdapter == null) {
			// Device does not support Bluetooth
			Toast.makeText(contexto, "Bluetooth no soportado",Toast.LENGTH_SHORT).show();
		} else {

			if (!mBluetoothAdapter.isEnabled()) {
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				if (contexto != null) {
					enableBtIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					// contexto.startActivity(enableBtIntent);
					mBluetoothAdapter.enable();
				}
			}
			if (mBluetoothAdapter.isEnabled()) {

				Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
				// If there are paired devices
				if (pairedDevices.size() > 0) {
					// Loop through paired devices
					for (BluetoothDevice device : pairedDevices) {
						if (device.getName().contentEquals("N6")) {

							if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
								auricular = null;
							} else if (mBluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET) == BluetoothAdapter.STATE_CONNECTED)
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
		if (auricular != null && !llamada)
			hablador.decir("sistema de voz, iniciado");
	}

	public void setLector(LeeSms cual) {
		lector = cual;
	}

	public void setContexto(Context cont) {
		contexto = cont;
	}

	public void llamadaEnCurso() {

		llamada = true;
	}

	public void finLlamada() {
		llamada = false;

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

	public void cambieCancion(String cancion)
	{
		MediaMetadataRetriever mmr = new MediaMetadataRetriever();
		mmr.setDataSource(cancion);
		String texto;
		texto = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST) +
				" - " +
				mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
		if (mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)  == null && 
				mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST) == null)
		{
			texto = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
		}
		visual.cambiarTitulo(texto);
		Log.d("MC", "Tags de la cancion: " + texto);

	}

	public void setVisual(MainActivity cual)
	{
		visual = cual;
	}

	public void iniciar() {

		AudioManager sonido = null;
		if (lector == null)
			lector = new LeeSms();
		if (hablador == null) {
			hablador = new Voz();
			hablador.iniciar(contexto);

		}

		if (auricular == null)
			this.buscarAuricular();
		if ((contexto != null) ) {
			sonido = (AudioManager) contexto.getSystemService(Context.AUDIO_SERVICE);
			if (sonido.getStreamVolume(6) != sonido.getStreamMaxVolume(6))
				sonido.setStreamVolume(6, sonido.getStreamMaxVolume(6), AudioManager.FLAG_PLAY_SOUND);

		}
		if (tocadiscos == null)
			tocadiscos = new Reproductor();
	}


	public void noEscucheNada()
	{
		hablador.decir("No escuché nada!");
		tocadiscos.continuar();
		atento = false;

	}
	public void terminar()
	{
		AudioManager sonido = (AudioManager) contexto.getSystemService(Context.AUDIO_SERVICE);
		sonido.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		tocadiscos.terminar();
	}

	public void btConectado() {
		buscarAuricular();
		if (auricular != null & !llamada) {

			hablador.decir("Casco conectado");
		}
	}

	public void btDesconectado() {
		auricular = null;
		if (hablador != null)
			hablador.callate();
		if (tocadiscos.ocupado())
			tocadiscos.pausar();
	}

	public String buscarPorTel(String telefono) {
		if (contexto == null)
			return telefono;

		String nombre = "";

		// define the columns I want the query to return
		String[] projection = new String[] {
				ContactsContract.Contacts.DISPLAY_NAME,
				ContactsContract.PhoneLookup._ID };

		Uri uri = Uri.withAppendedPath(
				ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(telefono));
		Cursor cursor = contexto.getContentResolver().query(uri, projection,
				null, null, null);
		if (cursor.moveToFirst()) {

			// Get values from contacts database:
			nombre = cursor
					.getString(cursor
							.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
			if (nombre == null)
				nombre = cursor.getString(cursor
						.getColumnIndex(ContactsContract.PhoneLookup._ID));

			return nombre;

		} else {

			return telefono; // contact not found

		}

	}

	public void termineDeHablar() {

		System.out.println("Termino de hablar de hablar!");
		if (atento) {
			sr = SpeechRecognizer.createSpeechRecognizer(contexto);
			listener = new MyRL();
			sr.setRecognitionListener(listener);
			Intent algo = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			algo.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			algo.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES");
			algo.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
			sr.startListening(algo);
			atento = false;
		}
		else
			System.out.println("Y a mi que me importa");

	}

	public void probar() {

		MediaPlayer mp = MediaPlayer.create(contexto, R.raw.coin );
		mp.start();

	}

	public void escuche(String respuesta) {
		Locale lang = new Locale("spa", "ESP");
		respuesta = respuesta.toLowerCase(lang);
		if (respuesta.contains("si") || respuesta.contains("sí") || respuesta.contains("iii")) 
		{
			if (msjPendiente.length() > 0)
				hablador.decir("dice.." + msjPendiente); //Se lee el msj
			else
				hablador.decir("Me olvidé");            //No hay mensaje para leer..raro...
		}
		else if (respuesta.contains("no") || respuesta.contains("ooo"))
		{
			hablador.decir("a la mierrda"); // AKA no se lee
		}
		else if (respuesta.contentEquals("ni idea"))
			hablador.decir("no entendí");
		else
			hablador.decir("Algo raro");

		//desilenciarMusica();
	}
	public void tocarMusica()
	{
		if (tocadiscos.ocupado() && !pausaMusica)
		{
			tocadiscos.pausar();
			pausaMusica = true;
		}
		else if (pausaMusica)
		{
			tocadiscos.continuar();
			pausaMusica = false;
		}
		else
		{
			tocadiscos.tocarMusica();
			pausaMusica = false;
		}
	}
	public void silenciarMusica() {
		tocadiscos.pausar();
	}

	public void desilenciarMusica() {
		tocadiscos.continuar();
	}

	public void siguienteCancion()
	{
		if (tocadiscos.ocupado())
			tocadiscos.siguienteCancion();
		else
			tocadiscos.tocarMusica();
	}
	public void pararMusica()
	{
		if (tocadiscos.ocupado())
			tocadiscos.reset();

	}

	public Context darContexto()
	{
		return contexto;

	}

	public void nuevoMensaje(String quien, String texto) {
		if (hablador != null && !llamada) {
			quien = this.buscarPorTel(quien);	
			tocadiscos.pausar();	
			Log.d("MC", "Coin sonido");
			hablador.avisarRte(quien);
			hablador.decirNada();
			msjPendiente = texto;
			atento = true;
		}
	}

	public void reconocerVoz() {

		BluetoothAdapter Bt = BluetoothAdapter.getDefaultAdapter();
		Bt.getProfileProxy(contexto, null, BluetoothProfile.HEADSET);

	}
}
