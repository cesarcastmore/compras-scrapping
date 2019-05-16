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

public class PalacioHierro {

	public static String url="https://www.elpalaciodehierro.com/catalogsearch/result/index?q=";
	public static String PAGE="https://www.elpalaciodehierro.com";

	public PalacioHierro(){

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
  			String searchUrl = url  + URLEncoder.encode(searchQuery, "UTF-8") + "&p=" + pageNum;
  			System.out.println(searchUrl);

  			page = client.getPage(searchUrl);
		} catch(Exception e){
  			e.printStackTrace();
		}

		//System.out.println(page.asXml());

		addItems("//li[@class='ls-grid-item col-lg-3']", listJson, page);

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

  		HtmlElement enlaceHtml = (HtmlElement) htmlItem.getFirstByXPath(".//a[@class='enlace-mobile']");  
  		String enlace = enlaceHtml.getAttribute("href");
		itemJson.put("enlace_informacion", PAGE + enlace);


  		HtmlElement imagenHtml = (HtmlElement) htmlItem.getFirstByXPath(".//img");  
  		String imagen = imagenHtml.getAttribute("src");
		itemJson.put("imagen", imagen);

  		HtmlElement nameHtml = (HtmlElement) htmlItem.getFirstByXPath(".//p[@class='jbb-list-item-description ']/span");  
  		String titulo = nameHtml.asText();

  		nameHtml = (HtmlElement) htmlItem.getFirstByXPath(".//p[@class='ls-grid-title']");  
  		String titulo2 = nameHtml.asText();
  		titulo= titulo + " "+  titulo2; 

		itemJson.put("titulo", titulo);
		itemJson.put("cadena", "PalacioHierro");

		String price=null;
  		HtmlElement priceHtml = (HtmlElement) htmlItem.getFirstByXPath(".//span[@class='ls-price-now-price price']");  
  		if(priceHtml != null){
  			price = priceHtml.asText();
  		}

  		priceHtml = (HtmlElement) htmlItem.getFirstByXPath(".//span[@class='price']");  
  		if(priceHtml != null){
  			price = priceHtml.asText();
  		}
  		price= price.replace("$", "").replace(",", "");
		itemJson.put("precio", price);

		JSONArray palabras_claves = Util.palabrasClaves(titulo);
		itemJson.put("palabras_claves", palabras_claves);

		return itemJson;
	}



}