package com.example.asistente;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.os.Environment;
import android.util.Log;


public class Logueador {

	public Logueador() {
		// TODO Auto-generated constructor stub
	}

	public void loguear(String que)
	{
		
		File root = new File("/storage/sdcard1/DCIM/");
		File file = new File("/storage/sdcard1/DCIM/", "salida.txt");
		if (que.length() > 0) {
		    try {
		        if (root.canWrite()) {
		            FileWriter filewriter = new FileWriter(file,true);
		            BufferedWriter out = new BufferedWriter(filewriter);
		            out.write(que + "\n");
		            out.close();
		        }
		    } catch (IOException e) {
		        Log.e("TAG", "Could not write file " + e.getMessage());
		    }
		}		
		
		
	}
	
	
}
