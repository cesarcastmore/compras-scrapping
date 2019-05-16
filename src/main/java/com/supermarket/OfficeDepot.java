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

import com.util.Util;


public class OfficeDepot {

	public static String url="https://www.officedepot.com.mx/officedepot/en/Categor%C3%ADa/Todas/c/0-0-0-0?q=";
	public static String PAGE="https://www.officedepot.com.mx";

	public OfficeDepot(){

	}


	public JSONObject search(String searchQuery, Integer pageNum) throws Exception {

		WebClient client = new WebClient();  
		client.getOptions().setCssEnabled(false);  
		client.getOptions().setJavaScriptEnabled(false); 
		JSONArray listJson = new JSONArray();
		String total ="0";

		HtmlPage page= null; 
		try {  
  			String searchUrl = url  + URLEncoder.encode(searchQuery, "UTF-8") + "%3Arelevance&page=" + pageNum;
  			System.out.println(searchUrl);

  			page = client.getPage(searchUrl);
		} catch(Exception e){
  			e.printStackTrace();
		}

		//System.out.println("HTML" +page.asXml());

		List<?> items = page.getByXPath("//div[@class='product-item']");  
		if(items.isEmpty()){  
  			System.out.println("No items found !");
		}else{
		for(Object  obj : items){
		  
		  HtmlElement htmlItem = (HtmlElement) obj; 

		  JSONObject itemJson = new JSONObject();


		  HtmlElement titleHtml =  htmlItem.getFirstByXPath(".//div[@class='name description-style']"); 
		  String titulo = titleHtml.asText();
		  itemJson.put("titulo", titulo);
		  JSONArray palabras_claves = Util.palabrasClaves(titulo);
		  itemJson.put("palabras_claves", palabras_claves);


		  HtmlElement linkHtml =  htmlItem.getFirstByXPath(".//a[@class='thumb picture-product']"); 
		  String enlace_informacion = linkHtml.getAttribute("href");
		  itemJson.put("enlace_informacion", PAGE + enlace_informacion);

		  HtmlElement imgHtml =  linkHtml.getFirstByXPath(".//img[@class='lazy']");
		  String imagen = imgHtml.getAttribute("data-src");
		  itemJson.put("imagen", PAGE + imagen);


		  HtmlElement priceHtml =  htmlItem.getFirstByXPath(".//div[@class='price']");

		  if(priceHtml == null){
		  	priceHtml =  htmlItem.getFirstByXPath(".//div[@class='discountedPrice-grid cont-price-grid']");
		  }

		  String price = priceHtml.asText();
		  price= price.replace("$", "").replace(",", "").trim();

		  if(price.contains("\n")){
		  	price = price.substring(0, price.indexOf("\n"));
		  	price= price.trim().replace(",", "");
		  }
		  itemJson.put("precio", price);
		  itemJson.put("cadena", "officedepot");


		  listJson.add(itemJson);
		}




		}


		/*HtmlElement totalHtml =  page.getFirstByXPath(".//p[@class='results-count']"); 
		total = totalHtml.asText();
		total= total.replace("resultados", "").trim();*/


		JSONObject res= new JSONObject();
		res.put("results", listJson);
		//res.put("total", total);

		return res;

	}



}