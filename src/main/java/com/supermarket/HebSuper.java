package com.supermarket;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.URLEncoder;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAddress;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.util.List;
import java.math.BigDecimal;

public class HebSuper {

	public static String url="https://www.heb.com.mx/catalogsearch/result/index/?cat=";

	public HebSuper(){

	}


	public JSONObject search(String searchQuery, Integer pageNum) throws Exception {

		WebClient client = new WebClient();  
		client.getOptions().setCssEnabled(false);  
		client.getOptions().setJavaScriptEnabled(false); 
		JSONArray listJson = new JSONArray();

		HtmlPage page= null; 
		try {  
  			String searchUrl = url + "&p=" + pageNum +"&q=" + URLEncoder.encode(searchQuery, "UTF-8");
  			page = client.getPage(searchUrl);
		} catch(Exception e){
  			e.printStackTrace();
		}

		List<?> items = page.getByXPath("//li[@class='item last ']");  
		if(items.isEmpty()){  
  			System.out.println("No items found !");
		}else{
		for(Object  obj : items){  

		  HtmlElement htmlItem = (HtmlElement) obj; 

		  HtmlElement itemAddress =  ((HtmlElement) htmlItem.getElementsByTagName("a").item(0));
		  String info = itemAddress.getAttribute("href");
		  String title = itemAddress.getAttribute("title");

		  JSONObject itemJson =  new JSONObject();
		  itemJson.put("titulo", title);
		  itemJson.put("enlace_informacion", info);
		  itemJson.put("cadena", "HEB");

		  HtmlElement itemImagen =  ((HtmlElement) itemAddress.getElementsByTagName("img").item(0));
		  String imagen = itemImagen.getAttribute("src");
		  itemJson.put("imagen", imagen);

		  HtmlElement itemPrecio =  htmlItem.getFirstByXPath(".//span[@class='price']");  
		  String precio = itemPrecio.asText();
		  precio= precio.replace("$", "");
		  precio= precio.replace(",", "");
		  precio= precio.trim();
		  BigDecimal precioBG = new BigDecimal(precio); 
		  itemJson.put("precio", precioBG.toString().replace(",", ""));

  		  HtmlElement itemMarca =  htmlItem.getFirstByXPath(".//span[@class='marca-related']");  
		  String marca = itemMarca.asText();
		  itemJson.put("marca", marca);

		  listJson.add(itemJson);

		  }
		}


		JSONObject res= new JSONObject();
		res.put("results", listJson);

		//Sacar el total
		/*items =  page.getByXPath("//span[@class='catalog-qty']");  
		Object obj= items.get(0);
		HtmlElement htmlTotal = (HtmlElement) obj;
		htmlTotal = items.get(0);
		String total= htmlTotal.asText();
		res.put("total", total);*/


		return res;

	}



}