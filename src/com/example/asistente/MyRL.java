package com.example.asistente;

import java.util.ArrayList;

import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;

class  MyRL implements RecognitionListener {


	 @Override
	 public void onBeginningOfSpeech() {
	  Log.d("Speech", "onBeginningOfSpeech");
	 }

	 @Override
	 public void onBufferReceived(byte[] buffer) {
	  Log.d("Speech", "onBufferReceived");
	 }

	 @Override
	 public void onEndOfSpeech() {
	  Log.d("Speech", "onEndOfSpeech");
	 }

	 @Override
	 public void onError(int error) {
	  Log.d("Speech", "onError");
	  Estados jefe = Estados.getInstance();
	  jefe.noEscucheNada();
	  System.out.println("MyRL:No escuche nada reconocible");
	 }

	 @Override
	 public void onEvent(int eventType, Bundle params) {
	  Log.d("Speech", "onEvent");
	 }

	 @Override
	 public void onPartialResults(Bundle partialResults) {
	  Log.d("Speech", "onPartialResults");
	 }

	 @Override
	 public void onReadyForSpeech(Bundle params) {
	  Log.d("Speech", "onReadyForSpeech");
	  System.out.println("MyRL: escuchando...");
	 }
	 

	 @Override
	 public void onResults(Bundle results) {
	  
	  Estados jefe = Estados.getInstance();
	  
	  ArrayList<String> strlist = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
	  if (strlist.size() > 0 )
		  jefe.escuche(strlist.get(0));
	  else 
		  jefe.escuche("ni idea");	  

	 }

	 @Override
	 public void onRmsChanged(float rmsdB) {
	  Log.d("Speech", "onRmsChanged");
	 }
	 

	
	
}
