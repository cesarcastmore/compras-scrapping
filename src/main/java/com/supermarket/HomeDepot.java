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

import com.util.Util;

public class HomeDepot {

	public static String url = " https://www.homedepot.com.mx/SearchDisplay?searchTerm=";
	public static String PAGE="https://www.homedepot.com.mx";

	public HomeDepot(){

	}


	public JSONObject search(String searchQuery, Integer pageNum) throws Exception {


		JSONArray listJson = new JSONArray();
		String total ="0";
		//System.out.println("Entrando a home depot");

  		String searchUrl = "https://www.homedepot.com.mx/SearchDisplay?categoryId=&storeId=10351&catalogId=10101&langId=-5&sType=SimpleSearch&resultCatEntryType=2&showResultsPage=true&searchSource=Q&pageView=&beginIndex=0&pageSize=80&searchTerm=" + searchQuery;

  		WebClient client = new WebClient(BrowserVersion.CHROME);  
			client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);
		client.getOptions().setThrowExceptionOnFailingStatusCode(false);
		client.getOptions().setThrowExceptionOnScriptError(false);
		client.setRefreshHandler(new ThreadedRefreshHandler());

		HtmlPage page= null; 
		try {  
  			System.out.println(searchUrl);


  			page = client.getPage(searchUrl);
  			Thread.sleep(3000);




  			//System.out.println("HTML" +page.asXml());

		} catch(Exception e){
  			e.printStackTrace();
		}

		
		JSONObject res= new JSONObject();
		addItems("//ul[@class='grid_mode grid']/li", listJson, page);

		res.put("results", listJson);
		return res;

	}


	public void addItems(String value, JSONArray lines, HtmlPage page){

		List<?> items = page.getByXPath(value);  
		if(items.isEmpty()){  
  			//System.out.println("No items found !");
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

		//System.out.println("HTML" +  htmlItem.asXml());
		//System.out.println("---------------------------------") ;


  		HtmlElement imagenHtml = (HtmlElement) htmlItem.getFirstByXPath(".//div[@class='image']/a/img");  
  		//System.out.println("HTML" +  imagenHtml.asXml());

  		String imagen = imagenHtml.getAttribute("data-src");
		itemJson.put("imagen", imagen);



  		HtmlElement linkHtml = (HtmlElement) htmlItem.getFirstByXPath(".//div[@class='product_name']/a");  
  				//System.out.println("HTML" +  linkHtml.asXml());

  		String link = linkHtml.getAttribute("href");
  		String title = linkHtml.asText();
		itemJson.put("enlace_informacion", PAGE + link);
		itemJson.put("titulo", title);
		//System.out.println(title);
		

		HtmlElement skuHtml = (HtmlElement) htmlItem.getFirstByXPath(".//span[@class='sku']");  
		if(skuHtml != null){
			String sku= skuHtml.asText();
			itemJson.put("sku", sku);

		}

		HtmlElement priceHtml = (HtmlElement) htmlItem.getFirstByXPath(".//span[@class='price']");  
		String price= priceHtml.asText();
		price= price.replace("$", "").trim();
		price= price.substring(0, price.length() - 2);
		itemJson.put("precio", price.replace(",", ""));
		itemJson.put("cadena", "HomeDepot");
		
		JSONArray palabras_claves = Util.palabrasClaves(title);
		itemJson.put("palabras_claves", palabras_claves);



		return itemJson;
	}



}