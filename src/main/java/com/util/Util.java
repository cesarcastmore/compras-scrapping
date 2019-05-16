package com.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public  class Util{

	public static JSONArray palabrasClaves(String titulo){
		String claves[] = titulo.split(" ");
		JSONArray clavesJson= new JSONArray();
		for(int i=0; i< claves.length; i++){
			clavesJson.add(claves[i]);
	
		}

		return clavesJson;


	}


	


}