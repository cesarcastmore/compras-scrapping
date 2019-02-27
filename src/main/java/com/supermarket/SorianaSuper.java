package com.supermarket;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.URLEncoder;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAddress;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.util.List;

public class SorianaSuper {

	public static String url="http://superentucasa.soriana.com/default.aspx?p=13365&postback=1&cantCeldas=0&minCeldas=0&Txt_Bsq_Descripcion=";

	public SorianaSuper(){

	}


	public JSONObject search(String searchQuery, Integer pageNum) throws Exception {

		WebClient client = new WebClient();  
		client.getOptions().setCssEnabled(false);  
		client.getOptions().setJavaScriptEnabled(false); 
		JSONArray listJson = new JSONArray();

		HtmlPage page= null; 
		try {  
  			String searchUrl = url + URLEncoder.encode(searchQuery, "UTF-8");
  			System.out.println(searchUrl);

  			page = client.getPage(searchUrl);
		} catch(Exception e){
  			e.printStackTrace();
		}

		System.out.println("HTML" +page.asXml());

		List<HtmlElement> items = (List<HtmlElement>) page.getByXPath("//div[@class='col-lg-3 col-md-4 col-sm-12 col-xs-12 product-item']");  
		if(items.isEmpty()){  
  			System.out.println("No items found !");
		}else{
		for(HtmlElement htmlItem : items){  


		  /*HtmlElement itemAddress =  ((HtmlElement) htmlItem.getElementsByTagName("a").item(0));
		  String info = itemAddress.getAttribute("href");
		  String title = itemAddress.getAttribute("title");

	

		  listJson.add(itemJson);*/

		  System.out.println(htmlItem.asXml());

		  }
		}


		JSONObject res= new JSONObject();
		//res.put("results", listJson);

		return res;

	}



}