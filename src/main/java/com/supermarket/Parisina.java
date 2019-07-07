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
import java.util.Map;
import java.util.HashMap;

public class Parisina {

	public static String url="https://www.laparisina.mx/catalogsearch/result/?q=";
	public static String PAGE="https://www.laparisina.mx";

	Map<String, String> urls = new HashMap<String, String>();

	public Parisina(){

	}
	public JSONObject search(String searchQuery) throws Exception {

		Integer pageNum=1;
		JSONArray items= new JSONArray();

		JSONObject response = search(searchQuery, pageNum);
		JSONArray results = (JSONArray) response.get("results");


		while(results.size() > 0){

			for(Integer i=0; i< results.size(); i++){
				items.add((JSONObject) results.get(i));
			}


			pageNum= pageNum + 1;
			response = search(searchQuery, pageNum);
			results = (JSONArray) response.get("results");

		}


		JSONObject json= new JSONObject();
		json.put("results", items);

		return json;
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
  				

  			//System.out.println(page.asXml());
		} catch(Exception e){
  			e.printStackTrace();
		}

		addItems("//li[@class='item product product-item']", listJson, page);
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
			  if(itemJson != null)
			  	lines.add(itemJson);
			}

		}

	}

	public JSONObject createItem(Object  obj){
		HtmlElement htmlItem = (HtmlElement) obj; 
		JSONObject itemJson = new JSONObject();

		//System.out.println(htmlItem.asXml());
  		HtmlElement enlaceHtml = (HtmlElement) htmlItem.getFirstByXPath(".//a[@class='product-item-link']");  
  		String enlace = enlaceHtml.getAttribute("href");
		itemJson.put("enlace_informacion",  enlace);

		if(this.urls.containsKey(enlace)){
			return null;
		} else {
			this.urls.put(enlace, enlace);
		}

		String titulo = enlaceHtml.asText();
		itemJson.put("titulo", titulo);
		itemJson.put("cadena", "parisina");

  		HtmlElement imagenHtml = (HtmlElement) htmlItem.getFirstByXPath(".//img[@class='product-image-photo']");  
  		String imagen = imagenHtml.getAttribute("src");
		itemJson.put("imagen", PAGE + imagen);

  		
  		HtmlElement priceHtml = (HtmlElement) htmlItem.getFirstByXPath(".//span[@class='internet_unidad']");  
  		String price = priceHtml.asText();
  		price= price.replace("$", "").replace(",", "");
		itemJson.put("precio", price);

		JSONArray palabras_claves = Util.palabrasClaves(titulo);
		itemJson.put("palabras_claves", palabras_claves);
		
		
		return itemJson;
	}



}