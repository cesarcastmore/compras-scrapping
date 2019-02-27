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
import java.math.BigDecimal;

public class Sears {

	public static String url="https://www.sears.com.mx/buscar/?c=";
	public static String PAGE="https://www.sears.com.mx";

	public Sears(){

	}


	public JSONObject search(String searchQuery, Integer pageNum) throws Exception {

		WebClient client = new WebClient();  
		client.getOptions().setCssEnabled(false);  
		client.getOptions().setJavaScriptEnabled(false); 
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

		//System.out.println("HTML" +page.asXml());

		List<HtmlElement> items = (List<HtmlElement>) page.getByXPath("//div[@class='producto col-xl-3 col-md-4 col-sm-12 mb-4']");  
		if(items.isEmpty()){  
  			System.out.println("No items found !");
		}else{
		for(HtmlElement htmlItem : items){  

		  JSONObject itemJson = new JSONObject();
		  HtmlElement htmlAddress=  htmlItem.getFirstByXPath(".//a");
		  String info = htmlAddress.getAttribute("href");
		  itemJson.put("enlace_informacion", PAGE + info);
		  itemJson.put("cadena", "sears");

		  HtmlElement htmlImg=  htmlAddress.getFirstByXPath(".//img");
		  String srcImage = htmlImg.getAttribute("src");
		  String title = htmlImg.getAttribute("title");
		  itemJson.put("imagen", srcImage);
		  itemJson.put("titulo", title);

		  HtmlElement precioHtml=  htmlItem.getFirstByXPath(".//p[@class='precio_venta']");
		  String precio = precioHtml.asText();
		  precio=precio.replace("Precio Internet: $", "");
		  precio=precio.replace("Oferta en Tienda: $", "");
		  precio=precio.replace("Precio en Tienda: $", "");
		  precio=precio.replace(",", "");
		  precio = precio.trim();
		  BigDecimal precioBD = new BigDecimal(precio);

		  itemJson.put("precio", precioBD.toString());

		  //System.out.println(htmlAddress.asXml());

		  


		 // HtmlElement itemPrice =  htmlItem.getFirstByXPath(".//span[@class='sale-original-price']");  
		  listJson.add(itemJson);
		}




		}

		

		JSONObject res= new JSONObject();
		res.put("results", listJson);
		//res.put("total", total);

		return res;

	}



}