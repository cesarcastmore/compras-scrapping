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

	public static String url="http://superentucasa.soriana.com/default.aspx?p=13365&postback=1&Txt_Bsq_Descripcion=azucar&cantCeldas=0&minCeldas=0cd ";

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
  			Thread.sleep(7000);

		} catch(Exception e){
  			e.printStackTrace();
		}

		System.out.println("HTML" +page.asXml());

		addItems("//div[@class='col-lg-3 col-md-4 col-sm-12 col-xs-12 product-item']", listJson, page);



		JSONObject res= new JSONObject();
		//res.put("results", listJson);

		return res;

	}


	public void addItems(String value, JSONArray lines, HtmlPage page){

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

		System.out.println(htmlItem.asXml());
		JSONObject itemJson = new JSONObject();
  		/*HtmlElement enlaceHtml = (HtmlElement) htmlItem.getFirstByXPath(".//a[@class='product-image']");  
  		String enlace = enlaceHtml.getAttribute("href");
		itemJson.put("enlace_informacion", enlace);


  		HtmlElement imagenHtml = (HtmlElement) htmlItem.getFirstByXPath(".//img");  
  		String imagen = imagenHtml.getAttribute("src");
		itemJson.put("imagen", imagen);

  		HtmlElement nameHtml = (HtmlElement) htmlItem.getFirstByXPath(".//h2[@class='product-name']");  
  		String titulo = nameHtml.asText();
		itemJson.put("titulo", titulo);
		itemJson.put("cadena", "Farmacias Del Ahorro");

  		HtmlElement priceHtml = (HtmlElement) htmlItem.getFirstByXPath(".//span[@class='price']");  
  		String price = priceHtml.asText();
  		price= price.replace("$", "");
		itemJson.put("precio", price);*/

		return itemJson;
	}




}