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

public class Soriana {

	public static String url="https://www.soriana.com/soriana/es/search?q=";

	public Soriana(){

	}


	public JSONObject search(String searchQuery, Integer pageNum) throws Exception {

		WebClient client = new WebClient();  
		client.getOptions().setCssEnabled(false);  
		client.getOptions().setJavaScriptEnabled(false); 
		JSONArray listJson = new JSONArray();
		String total ="0";

		HtmlPage page= null; 
		try {  
  			String searchUrl = url + URLEncoder.encode(searchQuery, "UTF-8") +"&page=" + pageNum;
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

		  HtmlElement itemImage=  ((HtmlElement) htmlItem.getElementsByTagName("img").item(0));
		  String srcImagen = itemImage.getAttribute("src");
		  itemJson.put("imagen", "https://www.soriana.com" + srcImagen);

		  HtmlElement itemPrice =  htmlItem.getFirstByXPath(".//span[@class='sale-original-price']");  

		  if(itemPrice != null){

		  	String price = itemPrice.asText();

		  	price= price.replace("$", "");
		 	price= price.trim();
		  	itemJson.put("precio-original", price);

		    HtmlElement ofertaPrice =  htmlItem.getFirstByXPath(".//span[@class='price sale-price']");  
			String oferta = ofertaPrice.asText();
			oferta= oferta.replace("$", "");
		 	oferta= oferta.trim();
		 	oferta= oferta.replace(",", "");
		    BigDecimal precioBG = new BigDecimal(oferta); 

		  	itemJson.put("precio", precioBG.toString());

		  } else {
		  	itemPrice =  htmlItem.getFirstByXPath(".//span[@class='price ']");  

  			String price = itemPrice.getTextContent();
		  	price= price.replace("$", "");
		 	price= price.trim();
		    price= price.replace(",", "");
		    BigDecimal precioBG = new BigDecimal(price); 
		 	itemJson.put("precio", precioBG.toString());

		  }


		  HtmlElement itemName=   htmlItem.getFirstByXPath(".//a[@class='name']");  
		  String name = itemName.asText();
		  String info = itemName.getAttribute("href");

		  itemJson.put("enlace_informacion", "https://www.soriana.com" + info);
		  itemJson.put("titulo", name);
		  itemJson.put("cadena", "Soriana");

		  listJson.add(itemJson);



		  }

		  HtmlElement results=   page.getFirstByXPath("//div[@class='results']");  
		  HtmlElement htmlTotal=   results.getFirstByXPath(".//h1"); 
		  total = htmlTotal.asText();
		  total= total.replace("Encontramos", "");
		  total= total.replace("resultados para \""+searchQuery +"\"", "");
		  total= total.trim();


		}


		JSONObject res= new JSONObject();
		res.put("results", listJson);
		res.put("total", total);

		return res;

	}



}