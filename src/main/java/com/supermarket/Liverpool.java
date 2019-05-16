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

import com.util.Util;


public class Liverpool {

	public static String url="https://www.liverpool.com.mx/tienda/page-";

	public Liverpool(){

	}


	public JSONObject search(String searchQuery, Integer pageNum) throws Exception {

		WebClient client = new WebClient();  
		client.getOptions().setCssEnabled(false);  
		client.getOptions().setJavaScriptEnabled(false); 
		JSONArray listJson = new JSONArray();
		String total ="0";

		HtmlPage page= null; 
		try {  
  			String searchUrl = url + pageNum + "?s=" + URLEncoder.encode(searchQuery, "UTF-8");
  			System.out.println(searchUrl);

  			page = client.getPage(searchUrl);
		} catch(Exception e){
  			e.printStackTrace();
		}

		//System.out.println("HTML" +page.asXml());

		List<?> items = page.getByXPath("//li[@class='product-cell']");  
		if(items.isEmpty()){  
  			System.out.println("No items found !");
		}else{
		for(Object  obj : items){  
		  HtmlElement htmlItem = (HtmlElement) obj; 

		  JSONObject itemJson = new JSONObject();

		  HtmlElement itemAddress=  ((HtmlElement) htmlItem.getElementsByTagName("a").item(0));
		  String hrefAddres= itemAddress.getAttribute("href");
		  itemJson.put("enlace_informacion", "https://www.liverpool.com.mx" + hrefAddres);
		  String title= itemAddress.getAttribute("title");
		  itemJson.put("titulo", title);
		  JSONArray palabras_claves = Util.palabrasClaves(title);

		  HtmlElement itemImg=  htmlItem.getFirstByXPath(".//img[@class='lazy foto-modulo foto-list product-thumb']"); 

		  String srcimg= itemImg.getAttribute("data-original");
		  String sku= itemImg.getAttribute("id");

		  itemJson.put("imagen", srcimg);
		  itemJson.put("sku", sku);
		  itemJson.put("cadena", "liverpool");

		  palabras_claves.add(sku);
		  itemJson.put("palabras_claves", palabras_claves);


		  String precioOriginal=null;

		  HtmlElement htmlPrecioEsp=   htmlItem.getFirstByXPath(".//p[@class='precio-especial']");  
		  if(htmlPrecioEsp != null){
		  	HtmlElement htmlPrecioOrig=   htmlPrecioEsp.getFirstByXPath(".//span[@class='price-amount']");

		  	//System.out.println(htmlPrecioOrig.asXml());

		  	precioOriginal = htmlPrecioOrig.asText();

		  	if(precioOriginal != null){
		  		precioOriginal= precioOriginal.replace(",", "");
		  		precioOriginal= precioOriginal.trim();
		  	 	BigDecimal pr = new BigDecimal(precioOriginal);
		  	 	pr = pr.divide(new BigDecimal(100));
		  		itemJson.put("precio-original", pr.toString().replace(",", ""));
		      }


		   HtmlElement htmlPrecioProm=   htmlItem.getFirstByXPath(".//p[@class='precio-promocion']");  
		  if(htmlPrecioProm != null){
		  	HtmlElement htmlPrecio=   htmlPrecioProm.getFirstByXPath(".//span[@class='price-amount']");  
		  	String precio = htmlPrecio.asText();
		  	 if(precio != null){
		  	 	precio= precio.replace(",", "");
		  		precio= precio.trim();
		  	 	BigDecimal pr = new BigDecimal(precio);
		  	 	pr = pr.divide(new BigDecimal(100));
		  		itemJson.put("precio", pr.toString().replace(",", ""));
		      }
		  } else {
		  	if(precioOriginal != null){
		  		precioOriginal= precioOriginal.replace(",", "");
		  		precioOriginal= precioOriginal.trim();
		  	 	BigDecimal pr = new BigDecimal(precioOriginal);
		  	 	pr = pr.divide(new BigDecimal(100));
		  		itemJson.put("precio", pr.toString().replace(",", ""));
		      }
		  	itemJson.remove("precio-original");

		  }

		  } else {
		  	htmlPrecioEsp=   htmlItem.getFirstByXPath(".//span[@class='price-state price-strike-special']");  
			  if(htmlPrecioEsp != null){
			  	HtmlElement htmlPrecioOrig=   htmlPrecioEsp.getFirstByXPath(".//span[@class='price-amount']");

			  	//System.out.println(htmlPrecioOrig.asXml());

			  	precioOriginal = htmlPrecioOrig.asText();

			  	if(precioOriginal != null){
			  		precioOriginal= precioOriginal.replace(",", "");
			  		precioOriginal= precioOriginal.replace("$", "");
			  		precioOriginal= precioOriginal.trim();
			  	 	BigDecimal pr = new BigDecimal(precioOriginal);
			  	 	pr = pr.divide(new BigDecimal(100));
			  		itemJson.put("precio-original", pr.toString());
			      }
			  } 


		   HtmlElement htmlPrecioProm=   htmlItem.getFirstByXPath(".//span[@class='price-state price-special']");  
		  if(htmlPrecioProm != null){
		  	HtmlElement htmlPrecio=   htmlPrecioProm.getFirstByXPath(".//span[@class='price-amount']");  
		  	String precio = htmlPrecio.asText();
		  	 if(precio != null){
		  	 	precio= precio.replace(",", "");
		  	 	precioOriginal= precioOriginal.replace("$", "");
		  		precio= precio.trim();
		  	 	BigDecimal pr = new BigDecimal(precio);
		  	 	pr = pr.divide(new BigDecimal(100));
		  		itemJson.put("precio", pr.toString());
		      }
		  } else {
		  	if(precioOriginal != null){
		  		precioOriginal= precioOriginal.replace(",", "");
		  		precioOriginal= precioOriginal.replace("$", "");
		  		precioOriginal= precioOriginal.trim();
		  	 	BigDecimal pr = new BigDecimal(precioOriginal);
		  	 	pr = pr.divide(new BigDecimal(100));
		  		itemJson.put("precio", pr.toString());
		      }



		  	itemJson.remove("precio-original");

		  } 
		  } 



		 // HtmlElement itemPrice =  htmlItem.getFirstByXPath(".//span[@class='sale-original-price']");  
		  listJson.add(itemJson);
		}




		}

		HtmlElement htmlTotal=  page.getFirstByXPath("//div[@id='product-total']/p/span"); 
		total = htmlTotal.asText();


		JSONObject res= new JSONObject();
		res.put("results", listJson);
		res.put("total", total);

		return res;

	}



}