package com.supermarket;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.URLEncoder;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAddress;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;

//import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.List;
import java.math.BigDecimal;

import com.http.Request;
import com.http.Connection;
import com.http.Response;


public class HomeDepot {

	public static String url="https://www.homedepot.com.mx/SearchDisplay?sType=SimpleSearch&resultCatEntryType=2&showResultsPage=true&searchSource=Q&beginIndex=0&pageSize=20&searchTerm=";
	public static String PAGE="https://www.homedepot.com.mx";

	public HomeDepot(){

	}


	public JSONObject search(String searchQuery, Integer pageNum) throws Exception {


		JSONArray listJson = new JSONArray();
		String total ="0";

  		String searchUrl = url  + URLEncoder.encode(searchQuery, "UTF-8") + "&p=" + pageNum;

  		WebClient client = new WebClient(BrowserVersion.FIREFOX_60);  
		client.getOptions().setCssEnabled(false);  
		client.getOptions().setJavaScriptEnabled(false); 
		client.getOptions().setThrowExceptionOnFailingStatusCode(false);
		client.getCookieManager().setCookiesEnabled(true); 


		HtmlPage page= null; 
		try {  
  			System.out.println(searchUrl);

  			page = client.getPage(searchUrl);
		} catch(Exception e){
  			e.printStackTrace();
		}

		
		JSONObject res= new JSONObject();

	

		return res;

	}


	public void addItems(String value, JSONArray lines, HtmlPage page){

		List<?> items = page.getByXPath(value);  
		if(items.isEmpty()){  
  			System.out.println("No items found !");
		}else{
			for(Object  obj : items){  
			  JSONObject itemJson = createItem(obj);
			  lines.add(itemJson);
			}

		}

	}

	public JSONObject createItem(Object  obj){
		HtmlElement htmlItem = (HtmlElement) obj; 
		JSONObject itemJson = new JSONObject();

		System.out.println("HTML" +  htmlItem.asXml());


  		HtmlElement imagenHtml = (HtmlElement) htmlItem.getFirstByXPath(".//img");  
  		String imagen = imagenHtml.getAttribute("src");
		itemJson.put("imagen", imagen);

		return itemJson;
	}



}