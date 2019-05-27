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
import com.gargoylesoftware.htmlunit.html.DomElement;
import java.util.List;
import java.math.BigDecimal;

import com.util.Util;

public class NetShoes {

	public static String url="https://www.netshoes.com.mx/busca?nsCat=Natural&q=";
	public static String PAGE="https://www.netshoes.com.mx";

	public NetShoes(){

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
  			String searchUrl = url  + URLEncoder.encode(searchQuery, "UTF-8");
  			System.out.println(searchUrl);

  			page = client.getPage(searchUrl);
  			Thread.sleep(3000);

		} catch(Exception e){
  			e.printStackTrace();
		}

		addItems("//div[@class='item card-desktop card-with-rating lazy-price ']", listJson, page);

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
  			System.out.println("No items found !");
		}else{
			for(Object  obj : items){  

			  JSONObject itemJson = createItem(obj);

			  if(itemJson != null)
			  	lines.add(itemJson);
			}

		}

	}

	public JSONObject createItem(Object  obj){
		HtmlElement htmlItem = (HtmlElement) obj; 

		//System.out.println("HTML" + htmlItem.asXml());

		JSONObject itemJson = new JSONObject();
  		HtmlElement enlaceHtml = (HtmlElement) htmlItem.getFirstByXPath(".//a[@class='i card-link']");  
		String enlace = enlaceHtml.getAttribute("href");
		itemJson.put("enlace_informacion", PAGE + enlace);

		HtmlElement imagenHtml = (HtmlElement) htmlItem.getFirstByXPath(".//a[@class='i card-link']/img");  
		String imagen = imagenHtml.getAttribute("data-src");
		imagen=imagen.replace("//", "").replace("?resize=240:*", "");
		itemJson.put("imagen",  imagen);

		String titulo = enlaceHtml.getAttribute("title");
		itemJson.put("titulo", titulo);
		itemJson.put("cadena", "netshoes");

		HtmlElement priceHtml = (HtmlElement) htmlItem.getFirstByXPath(".//div[@class='pr']");  
		String price = priceHtml.getAttribute("data-final-price");
		itemJson.put("precio", price);


		JSONArray palabras_claves = Util.palabrasClaves(titulo);
		itemJson.put("palabras_claves", palabras_claves);


		return itemJson;

	}



}