package com.util;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

import com.google.firebase.FirebaseApp;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.firebase.FirebaseOptions;

import com.google.cloud.firestore.DocumentReference;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.WriteResult;

import java.util.Map;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;



 public class FireStoreClient {
	
	private  Firestore db;

	public FireStoreClient() throws Exception {

		FirestoreOptions options2 = FirestoreOptions.newBuilder()
		.setTimestampsInSnapshotsEnabled(true)
		.setCredentials(GoogleCredentials.fromStream(getClass().getResourceAsStream("/credentials.json")))
		.setProjectId("compras-c9ff5").build();

		this.db = options2.getService();

	}

	public Firestore getDB(){
		return this.db;
	}

	public  void insert(String uuid, JSONObject item) throws Exception {
		Map<String, Object> data = new HashMap<>();
		data.put("precio", (String) item.get("precio") );
		data.put("enlace_informacion", (String) item.get("enlace_informacion") );
		data.put("titulo", (String) item.get("titulo") );
		data.put("cadena", (String) item.get("cadena") );
		data.put("imagen", (String) item.get("imagen") );
		data.put("value", (String) item.get("value") );


		ApiFuture<DocumentReference> addedDocRef = db.collection("busqueda/" + uuid + "/resultados").add(data);
		System.out.println("Added document with ID: " + addedDocRef.get().getId());

	} 


	
}