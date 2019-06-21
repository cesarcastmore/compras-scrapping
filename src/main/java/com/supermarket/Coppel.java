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
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlButton;


import java.util.List;
import java.math.BigDecimal;
import com.util.Util;

public class Coppel {

	public static String PAGE="https://pcel.com";

	public Coppel(){

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
  			String searchUrl = "https://www.coppel.com/ProductListingView?searchType=1001&filterTerm=&langId=-5&advancedSearch=&sType=SimpleSearch&gridPosition=&metaData=&manufacturer=&ajaxStoreImageDir=https%3A%2F%2Fcdn2.coppel.com%2Fwcsstore%2FAuroraStorefrontAssetStore%2F&resultCatEntryType=2&catalogId=10001&searchTerm=" +  URLEncoder.encode(searchQuery, "UTF-8") + "&resultsPerPage=36&emsName=&facet=&categoryId=&storeId=12757&disableProductCompare=false&ddkey=ProductListingView_6_1120&filterFacet=";

  			page = client.getPage(searchUrl);

  			System.out.println(searchUrl);

		} catch(Exception e){
  			e.printStackTrace();
		}


		//System.out.println("HTML" + page.asXml());

		addItems("//ul[@class='grid_mode grid']/li", listJson, page);

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
  		HtmlElement enlaceHtml = (HtmlElement) htmlItem.getFirstByXPath(".//div[@class='product_name']/a"); 

		String enlace = enlaceHtml.getAttribute("href");
		itemJson.put("enlace_informacion", enlace);

		HtmlElement imagenHtml = (HtmlElement) htmlItem.getFirstByXPath(".//img");  
		String imagen = imagenHtml.getAttribute("src");
		itemJson.put("imagen", imagen);

		HtmlElement nameHtml = (HtmlElement) htmlItem.getFirstByXPath(".//p[@class='m0']");  
		String titulo = nameHtml.asText();
		itemJson.put("titulo", titulo);
		itemJson.put("cadena", "Coppel");
		System.out.println("------------------");
		System.out.println(titulo);

		JSONArray palabras_claves = Util.palabrasClaves(titulo);
		itemJson.put("palabras_claves", palabras_claves);

		HtmlElement priceHtml = (HtmlElement) htmlItem.getFirstByXPath(".//div[@class='pcontado']/input");  

		String price = priceHtml.getAttribute("value");
  		price= price.replace("$", "");
  		price= price.replace(",", "");


		itemJson.put("precio", price);


		return itemJson;

	}



}