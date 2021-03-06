package com.supermarket;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
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

public class Famsa {

	public static String url="https://www.famsa.com/busqueda/?s=";
	public static String PAGE="https://www.famsa.com";

	public Famsa(){

	}


	public JSONObject search(String searchQuery, Integer numPage) {

		WebClient client = new WebClient(BrowserVersion.BEST_SUPPORTED);  
		client.getOptions().setCssEnabled(true);  
		client.getOptions().setJavaScriptEnabled(true); 
		client.getOptions().setThrowExceptionOnFailingStatusCode(false);
		client.getOptions().setThrowExceptionOnScriptError(false);
		client.setRefreshHandler(new ThreadedRefreshHandler());
		client.setJavaScriptTimeout(3000);

		JSONArray listJson = new JSONArray();
		String total ="0";

		HtmlPage page= null;

		try {
			String searchUrl = url + URLEncoder.encode(searchQuery, "UTF-8") +"#"+ numPage;
			System.out.println(searchUrl);
			page = client.getPage(searchUrl);

			Thread.sleep(3000);

			//System.out.println(page.asXml());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		addItems("//li[@class='item col-xs-12 col-sm-6 col-md-4 col-lg-4']", listJson, page);

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

		//System.out.println("OUT XML" + page.asXml());

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
  		HtmlElement enlaceHtml = (HtmlElement) htmlItem.getFirstByXPath(".//h2[@class='product-name cuit_title']/a");  
  		String enlace = enlaceHtml.getAttribute("href");
		itemJson.put("enlace_informacion", enlace.replace("//", ""));

		//System.out.println(htmlItem.asXml());


  		HtmlElement imagenHtml = (HtmlElement) htmlItem.getFirstByXPath(".//div[@class='product-image']/a");  
  		String imagen = imagenHtml.getAttribute("hrefimg");
		itemJson.put("imagen", imagen.replace("//", ""));

  		HtmlElement h2Html = (HtmlElement) htmlItem.getFirstByXPath(".//h2[@class='product-name cuit_title']/a");  
  		String titulo = h2Html.getAttribute("title");
		itemJson.put("titulo", titulo);
		itemJson.put("cadena", "Famsa");

  		HtmlElement priceHtml = (HtmlElement) htmlItem.getFirstByXPath(".//div[@class='price_sale']");  
  		String price = priceHtml.asXml();
  		price= price.replace("<span class=\"price_symbol\">", "").replace("CTDO.", "").replace("</span>", "").replace("\n", "")
  		.replace("\t", "").replace("<div class=\"price_sale\">", "").replace("</div>", "");
  		price= price.replace("$", "").replace("\r", "").replace(",", "").trim();
		itemJson.put("precio", price);

		JSONArray palabras_claves = Util.palabrasClaves(titulo);
		itemJson.put("palabras_claves", palabras_claves);


		return itemJson;
	}



}