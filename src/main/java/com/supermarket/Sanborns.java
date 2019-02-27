package com.supermarket;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.URLEncoder;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAddress;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.util.Base64;

import java.util.List;
import java.math.BigDecimal;

public class Sanborns {

	public static String url="https://www.sanborns.com.mx/buscador/";
	public static String PAGE="https://www.sanborns.com.mx";

	public Sanborns(){

	}


	public JSONObject search(String searchQuery, Integer pageNum) throws Exception {

		WebClient client = new WebClient();  
		client.getOptions().setCssEnabled(false);  
		client.getOptions().setJavaScriptEnabled(false); 
		JSONArray listJson = new JSONArray();
		String total ="0";

		HtmlPage page= null; 
		try {  

			byte[] encodedBytes = Base64.getEncoder().encode(searchQuery.getBytes());
			String searchQueryBase64  = new String(encodedBytes);

  			String searchUrl = url  + searchQueryBase64 + "/" + pageNum + "/";
  			System.out.println(searchUrl);

  			page = client.getPage(searchUrl);
		} catch(Exception e){
  			e.printStackTrace();
		}

		//System.out.println("HTML" +page.asXml());

		List<HtmlElement> items = (List<HtmlElement>) page.getByXPath("//article[@class='productbox']");  
		if(items.isEmpty()){  
  			System.out.println("No items found !");
		}else{
		for(HtmlElement htmlItem : items){  

		  JSONObject itemJson = new JSONObject();
		  HtmlElement imagenHtml =  htmlItem.getFirstByXPath(".//img");
		  String srcImagen = imagenHtml.getAttribute("src");
		  itemJson.put("imagen",  srcImagen );

		  HtmlElement descrnHtml =  htmlItem.getFirstByXPath(".//a[@class='descrip']/p");
		  String descp = descrnHtml.asText();
		  itemJson.put("titulo",  descp );

		  HtmlElement enlaceHtml =  htmlItem.getFirstByXPath(".//a[@class='descrip']");
		  String ref = enlaceHtml.getAttribute("href");

		  itemJson.put("enlace_informacion",  PAGE + ref );

		  HtmlElement precioHtml =  htmlItem.getFirstByXPath(".//span[@class='preciodesc']");
		  String precioText = precioHtml.asText();
		  precioText= precioText.replace(",", "").replace("$", "");
		  precioText= precioText.trim();
		  BigDecimal pr = new BigDecimal(precioText);
		  itemJson.put("precio", pr.toString());

		  itemJson.put("cadena", "sanborns");



		 // HtmlElement itemPrice =  htmlItem.getFirstByXPath(".//span[@class='sale-original-price']");  
		  listJson.add(itemJson);
		}




		}

		JSONObject res= new JSONObject();


		HtmlElement htmlTotal=  page.getFirstByXPath("//div[@class='resultadoBusqueda']"); 
		if(htmlTotal != null){
			total = htmlTotal.asText();
			total = total.replace("productos encontrados.", "").trim();
			res.put("total", total);

		}


		res.put("results", listJson);

		return res;

	}



}