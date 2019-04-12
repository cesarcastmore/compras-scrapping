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
import java.util.Base64;

public class Sears {

	public static String url="https://www.sears.com.mx/buscador/";
	public static String PAGE="https://www.sears.com.mx";

	public Sears(){

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

		List<?> items = page.getByXPath("//article[@class='productbox']");  
		if(items.isEmpty()){  
  			System.out.println("No items found !");
		}else{
		for(Object  obj : items){  
		  HtmlElement htmlItem = (HtmlElement) obj; 

		  JSONObject itemJson = new JSONObject();
		  HtmlElement htmlAddress=  htmlItem.getFirstByXPath(".//a");
		  String info = htmlAddress.getAttribute("href");
		  itemJson.put("enlace_informacion", PAGE + info);
		  itemJson.put("cadena", "sears");

		  HtmlElement htmlImg=  htmlItem.getFirstByXPath(".//img");
		  System.out.println(htmlImg.asXml());
		  String srcImage = htmlImg.getAttribute("src");
		  itemJson.put("imagen", srcImage);

		  HtmlElement titleHtml=  htmlItem.getFirstByXPath(".//p");
		  String title = titleHtml.asXml();
		  itemJson.put("titulo", title);

		  HtmlElement precioHtml=  htmlItem.getFirstByXPath(".//span[@class='preciodesc']");
		  String precio = precioHtml.asText();
		  precio=precio.replace("Precio Internet: $", "");
		  precio=precio.replace("Oferta en Tienda: $", "");
		  precio=precio.replace("Precio en Tienda: $", "");
		  precio=precio.replace("Precio Tienda: $", "");
		  precio=precio.replace(",", "");
		  precio = precio.trim();
		  System.out.println("PRECIO" + precio);
		  BigDecimal precioBD = new BigDecimal(precio);

		  itemJson.put("precio", precioBD.toString());

		 // HtmlElement itemPrice =  htmlItem.getFirstByXPath(".//span[@class='sale-original-price']");  
		  listJson.add(itemJson);
		}




		}

		

		JSONObject res= new JSONObject();
		res.put("results", listJson);
		//res.put("total", total);

		return res;

	}



}