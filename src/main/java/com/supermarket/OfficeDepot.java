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

		List<HtmlElement> items = (List<HtmlElement>) page.getByXPath("//div[@class='product-item']");  
		if(items.isEmpty()){  
  			System.out.println("No items found !");
		}else{
		for(HtmlElement htmlItem : items){  

		  JSONObject itemJson = new JSONObject();


		  HtmlElement textHtml =  htmlItem.getFirstByXPath(".//a[@class='name description-style ']"); 
		  String enlace_informacion = textHtml.getAttribute("href"); 
		  itemJson.put("enlace_informacion", PAGE + enlace_informacion);

		  String titulo = textHtml.asText();
		  itemJson.put("titulo", titulo);


		  HtmlElement priceHtml =  htmlItem.getFirstByXPath(".//div[@class='price']");

		  if(priceHtml == null){
		  	priceHtml =  htmlItem.getFirstByXPath(".//div[@class='discountedPrice-grid cont-price-grid']");
		  }

		  String price = priceHtml.asText();
		  price= price.replace("$", "").replace(",", "").trim();

		  if(price.contains("\n")){
		  	price = price.substring(0, price.indexOf("\n"));
		  	price= price.trim();
		  }
		  itemJson.put("precio", price);

		  HtmlElement imgHtml =  htmlItem.getFirstByXPath(".//img");
		  System.out.println(imgHtml.asXml());
		  String imagen = imgHtml.getAttribute("data-src"); 
		  itemJson.put("imagen", PAGE + imagen);
		  itemJson.put("cadena", "officedepot");

		  HtmlElement skuHtml =  htmlItem.getFirstByXPath(".//span[@class='name-add'][2]");
		  String sku = skuHtml.asText();
		  itemJson.put("sku", sku);


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