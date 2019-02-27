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

import java.util.List;
import java.math.BigDecimal;

public class Sams {

	public static String url="https://www.sams.com.mx/search/Ntt=";
	public static String PAGE="https://www.sams.com.mx";

	public Sams(){

	}


	public JSONObject search(String searchQuery, Integer pageNum) throws Exception {

		WebClient client = new WebClient(BrowserVersion.CHROME);  
		client.getOptions().setCssEnabled(true);  
		client.getOptions().setJavaScriptEnabled(true); 
		JSONArray listJson = new JSONArray();
		String total ="0";

		HtmlPage page= null; 
		try {  
  			String searchUrl = url  + URLEncoder.encode(searchQuery, "UTF-8");
  			System.out.println(searchUrl);

  			page = client.getPage(searchUrl);
		} catch(Exception e){
  			e.printStackTrace();
		}

		System.out.println("HTML" +page.asXml());

		List<HtmlElement> items = (List<HtmlElement>) page.getByXPath("//div[@class='product-listing  desktop']");  
		if(items.isEmpty()){  
  			System.out.println("No items found !");
		}else{
		for(HtmlElement htmlItem : items){  

		  JSONObject itemJson = new JSONObject();
		  System.out.println("1");


		  listJson.add(itemJson);
		}




		}


		//HtmlElement totalHtml =  page.getFirstByXPath(".//p[@class='results-count']"); 
		//total = totalHtml.asText();
		//total= total.replace("resultados", "").trim();


		JSONObject res= new JSONObject();
		res.put("results", listJson);
		//res.put("total", total);

		return res;

	}



}