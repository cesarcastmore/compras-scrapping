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

public class BestBuy {

	public static String url="https://www.bestbuy.com.mx/c/buscar-best-buy/buscar?query=";
	public static String PAGE="https://www.bestbuy.com.mx";

	public BestBuy(){

	}


	public JSONObject search(String searchQuery, Integer pageNum) throws Exception {

		WebClient client = new WebClient();  
		client.getOptions().setCssEnabled(false);  
		client.getOptions().setJavaScriptEnabled(false); 
		JSONArray listJson = new JSONArray();
		String total ="0";

		HtmlPage page= null; 
		try {  
  			String searchUrl = url  + URLEncoder.encode(searchQuery, "UTF-8") + "&page=" + pageNum;
  			System.out.println(searchUrl);

  			page = client.getPage(searchUrl);
		} catch(Exception e){
  			e.printStackTrace();
		}

		//System.out.println("HTML" +page.asXml());

		List<?> items =  page.getByXPath("//div[@class='product-line-item-line']");  
		if(items.isEmpty()){  
  			System.out.println("No items found !");
		}else{
		for(Object  obj : items){  
			
		  HtmlElement htmlItem = (HtmlElement) obj; 

		  JSONObject itemJson = new JSONObject();


		  HtmlElement addressHtml =  htmlItem.getFirstByXPath(".//div[@class='col-xs-3 image-container']/a"); 
		  String enlace= addressHtml.getAttribute("href");
		  itemJson.put("enlace_informacion", enlace);

		  HtmlElement skuHtml =  htmlItem.getFirstByXPath(".//span[@class='sku-value']"); 
		  String sku= skuHtml.asText();
		  itemJson.put("sku", sku);


		  HtmlElement titleHtml =  htmlItem.getFirstByXPath(".//div[@class='product-title']/a/h4"); 
		  String title= titleHtml.asText();
		  itemJson.put("titulo", title);

 		  HtmlElement priceHtml =  htmlItem.getFirstByXPath(".//div[@class='product-price']"); 
		  String price= priceHtml.asText();
		  price= price.replace("$", "").replace(",", "").trim();
		  itemJson.put("precio", price.replace(",", ""));

 		  HtmlElement imageHtml =  htmlItem.getFirstByXPath(".//img[@class='product-image']"); 
		  String imgText= imageHtml.getAttribute("src");
		  itemJson.put("imagen", imgText);
		  itemJson.put("cadena", "bestbuy");

		  listJson.add(itemJson);
		}




		}


		HtmlElement totalHtml =  page.getFirstByXPath(".//p[@class='results-count']"); 
		total = totalHtml.asText();
		total= total.replace("resultados", "").trim();


		JSONObject res= new JSONObject();
		res.put("results", listJson);
		res.put("total", total);

		return res;

	}



}