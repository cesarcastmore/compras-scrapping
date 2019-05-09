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

public class Pcel {

	public static String url="https://pcel.com/index.php?route=product/search&filter_name=";
	public static String PAGE="https://pcel.com";

	public Pcel(){

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
		} catch(Exception e){
  			e.printStackTrace();
		}

		addItems("//div[@class='product-list']/table/tbody/tr", listJson, page);

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
  		HtmlElement enlaceHtml = (HtmlElement) htmlItem.getFirstByXPath(".//div[@class='image']/a");  

  		if(enlaceHtml != null){
  			String enlace = enlaceHtml.getAttribute("href");
			itemJson.put("enlace_informacion", enlace);

			HtmlElement imagenHtml = (HtmlElement) enlaceHtml.getFirstByXPath(".//img");  
			String imagen = imagenHtml.getAttribute("src");
			itemJson.put("imagen", imagen);


			HtmlElement nameHtml = (HtmlElement) htmlItem.getFirstByXPath(".//div[@class='name']/a");  
  			String titulo = nameHtml.asText();
			itemJson.put("titulo", titulo);
			itemJson.put("cadena", "Pcel");

			HtmlElement priceHtml = (HtmlElement) htmlItem.getFirstByXPath(".//span[@class='price-new']");  

			if(priceHtml == null)
				priceHtml = (HtmlElement) htmlItem.getFirstByXPath(".//div[@class='price']");  
			
			String price = priceHtml.asText();
	  		price= price.replace("$", "");
	  		price= price.replace(",", "");
			itemJson.put("precio", price.replace(",", ""));


			return itemJson;


  		}


		return null;

	}



}