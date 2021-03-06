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

import com.util.Util;


public class InnovaSport {

	public static String url="https://www.innovasport.com/search/?text=";
	public static String PAGE="https://www.innovasport.com";

	public InnovaSport(){

	}


	public JSONObject search(String searchQuery, Integer pageNum) throws Exception {

		WebClient client = new WebClient(BrowserVersion.FIREFOX_60);  
		client.getOptions().setCssEnabled(false);  
		client.getOptions().setJavaScriptEnabled(false); 
		client.getOptions().setThrowExceptionOnFailingStatusCode(false);
		client.getOptions().setThrowExceptionOnScriptError(false);
            client.setRefreshHandler(new ThreadedRefreshHandler());

		JSONArray listJson = new JSONArray();
		String total ="0";

		HtmlPage page= null; 
		try {  
  			String searchUrl = url  + URLEncoder.encode(searchQuery, "UTF-8") +"%3Arelevance&page=" + pageNum ;
  			System.out.println(searchUrl);

  			page = client.getPage(searchUrl);
		} catch(Exception e){
  			e.printStackTrace();
		}

		addItems("//div[@class='product-item innovasport-gallery-product']", listJson, page);
	

		//HtmlElement totalHtml =  page.getFirstByXPath(".//p[@class='results-count']"); 
		//total = totalHtml.asText();
		//total= total.replace("resultados", "").trim();

		JSONObject res= new JSONObject();

		res.put("results", listJson);
		//res.put("total", total);
		//System.out.println(res.toJSONString());

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
  		HtmlElement enlaceHtml = (HtmlElement) htmlItem.getFirstByXPath(".//a[@class='thumb js-gtm-product-click']");  
  		String enlace = enlaceHtml.getAttribute("href");
		itemJson.put("enlace_informacion", PAGE+  enlace);


  		HtmlElement imagenHtml = (HtmlElement) htmlItem.getFirstByXPath(".//img");  
  		String imagen = imagenHtml.getAttribute("src");
		itemJson.put("imagen", PAGE + imagen);

  		HtmlElement nameHtml = (HtmlElement) htmlItem.getFirstByXPath(".//div[@class='producto-nombre']");  
  		String titulo = nameHtml.asText();
		itemJson.put("titulo", titulo);
		itemJson.put("cadena", "InnovaSport");

  		HtmlElement priceHtml = (HtmlElement) htmlItem.getFirstByXPath(".//div[@class='price']");  
  		String price = priceHtml.asText();
  		price= price.replace("$", "").replace(",", "");
		itemJson.put("precio", price);

		JSONArray palabras_claves = Util.palabrasClaves(titulo);
		itemJson.put("palabras_claves", palabras_claves);


		return itemJson;
	}



}