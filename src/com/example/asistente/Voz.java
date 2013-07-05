package com.example.asistente;

import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;


public class Voz extends Activity implements TextToSpeech.OnInitListener {

	private TextToSpeech tts;
	private Estados jefe;
	
	
	public void iniciar(Context cont) {
		tts = new TextToSpeech(cont, this);
	}

	@Override
	public void onInit(int status) {
		
		if (status == TextToSpeech.SUCCESS) {
			Locale lang = new Locale("spa", "ESP");
			if (tts.isLanguageAvailable(lang) == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
				int result = tts.setLanguage(lang);
				if (result == TextToSpeech.LANG_MISSING_DATA
   				 || result == TextToSpeech.LANG_NOT_SUPPORTED) {
					Log.e("TTS", "Initilization Failed!");
				}
			} else {

				Log.e("TTS", "Initilization Failed2!");
			}
		} else {
			Log.e("TTS", "Initilization Failed!");
		}
		jefe = Estados.getInstance();
		jefe.setHablador(this);
	};

	public void decirNada()
	{
		tts.playSilence(500, TextToSpeech.QUEUE_ADD, null);
		//tts.speak(",", TextToSpeech.QUEUE_ADD, null);
		jefe.termineDeHablar();
		
	}
	
	public boolean decir(String texto) {
		AudioManager sonido = null;

		sonido = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
		sonido.setBluetoothScoOn(true);

		int resultado = 0;
		tts.playSilence(500, TextToSpeech.QUEUE_ADD, null);
		resultado = tts.speak(texto, TextToSpeech.QUEUE_ADD, null);
		sonido.setBluetoothScoOn(false);
		if (resultado != TextToSpeech.SUCCESS)
			return false;
		else
			return true;

	}

	public void terminar()
	{
		this.finish();
		
	}
	public boolean ocupado()
	{
		return tts.isSpeaking();
	}
	
	public boolean avisarRte(String quien)
	{
		
		String dialogo = "";
		dialogo += "Atencion, mensaje de " + quien + " .. ";
		//dialogo += "dice .. " + texto;
		return decir(dialogo);
	}
	
	public void callate()
	{
		tts.stop();		
	}
	
	@Override
	public void onDestroy() {
		tts.stop();
		tts.shutdown();
	}

};
