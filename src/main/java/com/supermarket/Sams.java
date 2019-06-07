package com.supermarket;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.URLEncoder;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAddress;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;

import java.util.List;
import java.math.BigDecimal;

import com.util.FireStoreClient;


public class Sams {

	public static String url="https://www.sams.com.mx/search/Ntt=";
	public static String PAGE="https://www.sams.com.mx";
	
	public Sams(){

	}


	public JSONObject search(String searchQuery, Integer pageNum) throws Exception {

		WebClient client = new WebClient(BrowserVersion.CHROME);  
		client.getOptions().setCssEnabled(true);  
		client.getOptions().setJavaScriptEnabled(true); 
		client.getOptions().setThrowExceptionOnFailingStatusCode(false);
		client.getOptions().setThrowExceptionOnScriptError(false);

		client.setRefreshHandler(new ThreadedRefreshHandler());
		JSONArray listJson = new JSONArray();
		String total ="0";

		HtmlPage page= null; 
		try {  
  			String searchUrl = url  + URLEncoder.encode(searchQuery, "UTF-8");
  			System.out.println(searchUrl);

  			page = client.getPage(searchUrl);
  			Thread.sleep(3000);

		} catch(Exception e){
  			e.printStackTrace();
		}

		System.out.println("HTML" +page.asXml());

		List<?> items =  page.getByXPath("//div[@class='product-listing  desktop']");  
		if(items.isEmpty()){  
  			System.out.println("No items found !");
		}else{
		for(Object  obj : items){  
			
		  HtmlElement htmlItem = (HtmlElement) obj; 

		  JSONObject itemJson = new JSONObject();


		  listJson.add(itemJson);
		}




		}


		//HtmlElement totalHtml =  page.getFirstByXPath(".//p[@class='results-count']"); 
		//total = totalHtml.asText();
		//total= total.replace("resultados", "").trim();


		JSONObject res= new JSONObject();
		res.put("results", listJson);
		//res.put("total", total);

		return res;

	}



}