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


public class Chedraui {

	public static String url="https://www.chedraui.com.mx/search?text=";
	public static String PAGE="https://www.chedraui.com.mx";

	public Chedraui(){

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
  			String searchUrl = url  + URLEncoder.encode(searchQuery, "UTF-8") + "&page=" + pageNum;
  			System.out.println(searchUrl);

  			page = client.getPage(searchUrl);
  						Thread.sleep(3000);

  			//System.out.println(page.asXml());
		} catch(Exception e){
  			e.printStackTrace();
		}

		addItems("//li[@class='product__list--item']", listJson, page);
		/*addItems("//li[@class='item span3']", listJson, page);
		addItems("//li[@class='item last span3']", listJson, page);*/

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

		//System.out.println(htmlItem.asXml());
  		HtmlElement enlaceHtml = (HtmlElement) htmlItem.getFirstByXPath(".//a[@class='product__list--name']");  
  		String enlace = enlaceHtml.getAttribute("href");
		itemJson.put("enlace_informacion", PAGE + enlace);


  		HtmlElement imagenHtml = (HtmlElement) htmlItem.getFirstByXPath(".//img");  
  		String imagen = imagenHtml.getAttribute("src");
		itemJson.put("imagen", PAGE + imagen);

  		String titulo = enlaceHtml.getAttribute("title");
		itemJson.put("titulo", titulo);
		itemJson.put("cadena", "Chedraui");

  		HtmlElement priceHtml = (HtmlElement) htmlItem.getFirstByXPath(".//div[@class='product__listing--price price-colour-final']");  
  		String price = priceHtml.asText();
  		price= price.replace("$", "").replace(",", "");
		itemJson.put("precio", price);

		JSONArray palabras_claves = Util.palabrasClaves(titulo);
		itemJson.put("palabras_claves", palabras_claves);
		
		return itemJson;
	}



}