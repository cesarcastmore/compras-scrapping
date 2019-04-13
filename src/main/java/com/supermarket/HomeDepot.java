package com.supermarket;

import java.util.Set;

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
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;


//import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.List;
import java.math.BigDecimal;

import com.http.Request;
import com.http.Connection;
import com.http.Response;


public class HomeDepot {

	public static String url = " https://www.homedepot.com.mx/SearchDisplay?searchTerm=";
	public static String PAGE="https://www.homedepot.com.mx";

	public HomeDepot(){

	}


	public JSONObject search(String searchQuery, Integer pageNum) throws Exception {


		JSONArray listJson = new JSONArray();
		String total ="0";

  		String searchUrl = "https://www.homedepot.com.mx/SearchDisplay?searchTerm=" + searchQuery;

  		WebClient client = new WebClient(BrowserVersion.CHROME);  
			client.getOptions().setCssEnabled(true);
		client.getOptions().setJavaScriptEnabled(true);
		client.getOptions().setThrowExceptionOnFailingStatusCode(false);
		client.getOptions().setThrowExceptionOnScriptError(false);
		client.setRefreshHandler(new ThreadedRefreshHandler());

		HtmlPage page= null; 
		try {  
  			System.out.println(searchUrl);


  			page = client.getPage(searchUrl);
  			Thread.sleep(5000);


  			System.out.println("HTML" +page.asXml());

		} catch(Exception e){
  			e.printStackTrace();
		}

		
		JSONObject res= new JSONObject();
		addItems("div[@class='product']", listJson, page);

		res.put("results", listJson);
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



  		HtmlElement linkHtml = (HtmlElement) htmlItem.getFirstByXPath(".//a");  
  		String link = imagenHtml.getAttribute("href");
  		String title = imagenHtml.getAttribute("title");
		itemJson.put("enlace_informacion", link);
		itemJson.put("titulo", title);

		HtmlElement skuHtml = (HtmlElement) htmlItem.getFirstByXPath(".//span[@class='sku']");  
		String sku= skuHtml.asText();
		itemJson.put("sku", sku);

		HtmlElement priceHtml = (HtmlElement) htmlItem.getFirstByXPath(".//span[@itemprop='price']");  
		String price= skuHtml.asText();
		itemJson.put("precio", price);



		return itemJson;
	}



}