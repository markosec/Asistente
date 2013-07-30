package com.example.asistente;

import java.util.HashMap;
import java.util.Locale;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;


public class Voz implements TextToSpeech.OnInitListener {

	private TextToSpeech tts;
	private Estados jefe;
	FinHablar listener = null;
	
	
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
		listener = new FinHablar();
		tts.setOnUtteranceProgressListener(listener);
		jefe = Estados.getInstance();
		jefe.setHablador(this);
		
	};

	public void decirNada()
	{
		tts.playSilence(500, TextToSpeech.QUEUE_ADD, null);
	}
	
	public boolean decir(String texto) 
	{
	    HashMap<String, String> ttsHashMap = new HashMap<String, String>();
	    ttsHashMap.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "hola");    
	    int resultado = 0;
		tts.playSilence(500, TextToSpeech.QUEUE_ADD, null);
		resultado = tts.speak(texto, TextToSpeech.QUEUE_ADD, ttsHashMap);
		
		if (resultado != TextToSpeech.SUCCESS)
			return false;
		else
			return true;

	}

	public boolean ocupado()
	{
		return tts.isSpeaking();
	}
	
	public boolean avisarRte(String quien)
	{
		String dialogo = "";
		dialogo += ",, Atencion, mensaje de " + quien + " desea escucharlo? .. ";
		return decir(dialogo);
	}
	
	public void callate()
	{
		tts.stop();		
	}

};
