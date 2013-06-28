package com.example.asistente;

import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

public class Voz extends Activity implements TextToSpeech.OnInitListener {

	private TextToSpeech tts;
	private Context contexto;
	private Estados jefe;
	
	
	public void iniciar(Context cont) {
		tts = new TextToSpeech(cont, this);
		contexto = cont;
	}

	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
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

	public boolean decir(String texto) {
		int resultado = 0;
		tts.speak(",", TextToSpeech.QUEUE_ADD,null);
		resultado = tts.speak(texto, TextToSpeech.QUEUE_ADD, null);
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
	
	public boolean avisar(String quien, String texto)
	{
		
		String dialogo = "";
		dialogo += "Atencion, mensaje de " + quien + ".";
		dialogo += "dice,," + texto;
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
