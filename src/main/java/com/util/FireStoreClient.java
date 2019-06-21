package com.util;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

import com.google.firebase.FirebaseApp;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.firebase.FirebaseOptions;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.firestore.Transaction;
import com.google.cloud.firestore.WriteBatch;
import com.google.cloud.firestore.WriteResult;


import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

		if(item.containsKey("end")){
			data.put("end", (boolean) item.get("end") );

		}


		ApiFuture<DocumentReference> addedDocRef = db.collection("busqueda/" + uuid + "/resultados").add(data);
		System.out.println("Added document with ID: " + addedDocRef.get().getId());

	} 


	public void delete(String uuid) throws Exception {

		

		deleteCollection(db.collection("busqueda/" + uuid + "/resultados"), 50);

	} 


	private void deleteCollection(CollectionReference collection, int batchSize) {
		  try {
		    // retrieve a small batch of documents to avoid out-of-memory errors
		    ApiFuture<QuerySnapshot> future = collection.limit(batchSize).get();
		    int deleted = 0;
		    // future.get() blocks on document retrieval
		    List<QueryDocumentSnapshot> documents = future.get().getDocuments();
		    for (QueryDocumentSnapshot document : documents) {
		      document.getReference().delete();
		      System.out.println("future.get() blocks on document retrieval");

		      ++deleted;

		      System.out.println(deleted);
		    }
		    Thread.sleep(400);

		    if (deleted >= batchSize) {

		    	System.out.println("retrieve and delete another batch");
		      // retrieve and delete another batch
		      deleteCollection(collection, batchSize);
		    }
		  } catch (Exception e) {
		    System.err.println("Error deleting collection : " + e.getMessage());
		  }
}


	
}