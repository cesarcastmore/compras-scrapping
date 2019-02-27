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

public class PromoDescuentos {

	public static String url="https://www.promodescuentos.com/?page=";
	public static String PAGE="https://www.promodescuentos.com";

	public PromoDescuentos(){

	}


	public JSONObject search(String searchQuery, Integer pageNum) throws Exception {

		WebClient client = new WebClient();  
		client.getOptions().setCssEnabled(false);  
		client.getOptions().setJavaScriptEnabled(false); 
		JSONArray listJson = new JSONArray();
		String total ="0";

		HtmlPage page= null; 
		try {  
  			String searchUrl = url   + pageNum;
  			System.out.println(searchUrl);

  			page = client.getPage(searchUrl);
		} catch(Exception e){
  			e.printStackTrace();
		}

		//System.out.println("HTML" +page.asXml());

		List<HtmlElement> items = (List<HtmlElement>) page.getByXPath("//article");  
		if(items.isEmpty()){  
  			System.out.println("No items found !");
		}else{
		for(HtmlElement htmlItem : items){  

		  JSONObject itemJson = new JSONObject();

		  HtmlElement titleHtml =  htmlItem.getFirstByXPath(".//strong[@class='thread-title']"); 
		  if(titleHtml != null){
			  HtmlElement aHtml = titleHtml.getFirstByXPath(".//a");
			  String title  = aHtml.getAttribute("title");
			  String enlace =  aHtml.getAttribute("href");

			  itemJson.put("descripcion", title);
			  itemJson.put("enlace_promosion", enlace);
		}

		HtmlElement priceHtml =  htmlItem.getFirstByXPath(".//span[@class='overflow--wrap-off']"); 
		if(priceHtml != null){
			HtmlElement pHtml = priceHtml.getFirstByXPath(".//span");
			String price  = pHtml.asText();
			itemJson.put("precio", price);
		}	

		  listJson.add(itemJson);
		}




		}

		

		JSONObject res= new JSONObject();
		res.put("results", listJson);
		//res.put("total", total);

		return res;

	}



}