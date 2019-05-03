package com.supermarket;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.URLEncoder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.util.List;
import java.math.BigDecimal;

public class Superama {

	public static String url="https://www.superama.com.mx/";
	public static String PAGE="https://www.superama.com.mx/";

	public Superama(){

	}


	public JSONObject search(String searchQuery, Integer pageNum) throws Exception {
		JSONArray listJson = new JSONArray();

		OkHttpClient client = new OkHttpClient();

		Request request = new Request.Builder().url("http://www.superama.com.mx/buscador/resultado?busqueda=" + searchQuery)
  		.get().addHeader("cache-control", "no-cache").addHeader("postman-token", "bb8124df-5fa2-0bb7-3fa8-8e255387c712")
  		.build();

		Response response = client.newCall(request).execute();
		ResponseBody responseBody = response.body();
		String body = responseBody.string();

		JSONObject responseJson = (JSONObject) new JSONParser().parse(body);
		JSONArray productos = (JSONArray) responseJson.get("Products");

		for(int i=0; i<productos.size(); i++ ){
			JSONObject producto = (JSONObject) productos.get(i);
			JSONObject itemJson = new JSONObject();

			String title = (String) producto.get("DescriptionDisplay");
			itemJson.put("titulo", title);

			String img = (String) producto.get("ImageUrl");
			itemJson.put("imagen", PAGE + img);	

			itemJson.put("cadena", "Superama");

			String price = (String) producto.get("Precio");
			itemJson.put("precio", price.replace("$", ""));

			String enlace_informacion = PAGE+ "/catalogo/";
			String departmentName = (String) producto.get("SeoDepartamentoUrlName");
			String familyName = (String) producto.get("SeoFamiliaUrlName");
			String lineName = (String) producto.get("SeoLineaUrlName");
			String productName = (String) producto.get("SeoProductUrlName");
			String upc = (String) producto.get("Upc");
			enlace_informacion = enlace_informacion + departmentName + "/" + familyName + "/" + lineName + "/"+ productName+ "/"+ upc;
			itemJson.put("enlace_informacion", enlace_informacion);


			listJson.add(itemJson);

		}


		JSONObject res= new JSONObject();

		res.put("results", listJson);
		//res.put("total", total);
		//System.out.println(res.toJSONString());

		return res;

	}


	



}