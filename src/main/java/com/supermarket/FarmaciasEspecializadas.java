package com.supermarket;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.URLEncoder;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.RequestBody;
import okhttp3.MediaType;
import okhttp3.ResponseBody;

import java.util.List;
import java.math.BigDecimal;

import com.util.Util;


public class FarmaciasEspecializadas {

	public static String API="https://api.farmaciasespecializadas.com/api/v1/medicamentos";
	public static String IMAGE="https://api.farmaciasespecializadas.com/api/v1/medicamentos/imagen/";
	public static String INFO="https://www.farmaciasespecializadas.com/medicamentos/detalle/";
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	public FarmaciasEspecializadas(){

	}


	public JSONObject search(String searchQuery, Integer pageNum) throws Exception {

		JSONArray items = client(searchQuery, 0);

		System.out.println(items.toJSONString());

		JSONArray listJson = new JSONArray();

		String total ="0";
		JSONObject res= new JSONObject();

		res.put("results", listJson);
	
		return res;

	}


	public JSONArray client(String term, Integer page) throws Exception {

		OkHttpClient client = new OkHttpClient();

		MediaType mediaType = MediaType.parse("application/json, text/plain, */*");
		RequestBody body = RequestBody.create(mediaType, "{\"filtros\": {\"id_categoria\": null, \"id_laboratorio\": -1, \"termino\": \""+term+"\"}}\n");
		Request request = new Request.Builder()
		  .url("https://api.farmaciasespecializadas.com/api/v1/medicamentos")
		  .post(body)
		  .addHeader("content-type", "application/json, text/plain, */*")
		  .addHeader("authorization", "Bearer")
		  .addHeader("clientsecret", "FhCEI78CAFpOtzIgC1JE8BIJ1IHUIgzrlzCscnqW")
		  .addHeader("ordenar", "asc")
		  .addHeader("ordenar-campo", "Name")
		  .addHeader("clientid", "2")
		  .addHeader("pagina-actual", "1")
		  .addHeader("elementos-por-pagina", "32")
		  .addHeader("cache-control", "no-cache")
		  .addHeader("postman-token", "61ebcd12-d988-0508-8b99-48ea5cdddedc")
		  .build();

		Response response = client.newCall(request).execute();

		ResponseBody responseBody = response.body();
		String bodyResponse = responseBody.string();

		JSONObject responseJson = (JSONObject) new JSONParser().parse(bodyResponse);

		System.out.println(responseJson.toJSONString());


		return new JSONArray();


	}


	public void addItems(JSONObject line){

	

	}

	



}