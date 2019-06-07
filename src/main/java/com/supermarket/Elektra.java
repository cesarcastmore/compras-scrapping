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
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;


import java.util.List;
import java.math.BigDecimal;

import com.http.Request;
import com.http.Response;
import com.http.Connection;

import com.util.Util;

public class Elektra {

	public static String url="https://www.elektra.com.mx/";
	public static String PAGE="https://www.elektra.com.mx";

	public Elektra(){

	}


	public JSONObject search(String searchQuery, Integer pageNum) throws Exception {

		//getHtml();

		WebClient client = new WebClient(BrowserVersion.FIREFOX_60);  
		client.getOptions().setCssEnabled(false);  
		client.getOptions().setJavaScriptEnabled(false); 
		//client.getOptions().setThrowExceptionOnFailingStatusCode(false);
		//client.getOptions().setThrowExceptionOnScriptError(false);
        //client.setRefreshHandler(new ThreadedRefreshHandler());

		JSONArray listJson = new JSONArray();
		String total ="0";

		HtmlPage page= null; 
		try {  
  			String searchUrl = url + URLEncoder.encode(searchQuery, "UTF-8") + "?currentPage=" + pageNum;
  			System.out.println(searchUrl);

  			page = client.getPage(searchUrl);

		} catch(Exception e){
  			e.printStackTrace();
		}


		/*final HtmlForm form = page.getFormById("ekt-search-desktop");
        final HtmlSubmitInput button = page.getFirstByXPath("//");
        final HtmlTextInput textField = form.getInputByName("search");*/

		addItems("//div[@class='product-border']", listJson, page);

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
			  lines.add(itemJson);
			}

		}

	}

	public JSONObject createItem(Object  obj){
		HtmlElement htmlItem = (HtmlElement) obj; 
		JSONObject itemJson = new JSONObject();

  		HtmlElement enlaceHtml = (HtmlElement) htmlItem.getFirstByXPath(".//a");  
  		String enlace = enlaceHtml.getAttribute("href");
		itemJson.put("enlace_informacion", enlace);



  		HtmlElement imagenHtml = (HtmlElement) htmlItem.getFirstByXPath(".//img");
  		String imagen = imagenHtml.getAttribute("src");
		itemJson.put("imagen", imagen);

  		HtmlElement nameHtml = (HtmlElement) htmlItem.getFirstByXPath(".//h3[@class='product-name']");  
  		if(nameHtml != null){
	  		String titulo = nameHtml.asText();
			itemJson.put("titulo", titulo);
			itemJson.put("cadena", "AlSuper");

			JSONArray palabras_claves = Util.palabrasClaves(titulo);
			itemJson.put("palabras_claves", palabras_claves);
		}

  		HtmlElement priceHtml = (HtmlElement) htmlItem.getFirstByXPath(".//small[@class='price-new ']");  
  		if(priceHtml == null){
  			priceHtml = (HtmlElement) htmlItem.getFirstByXPath(".//small[@class='price-new lines2']");  

  		}
  		String price = priceHtml.asText();
	  	price= price.replace("$", "");
		itemJson.put("precio", price);
		



		return itemJson;
	}


	public void getHtml() throws Exception {

		Request request= new Request();
		request.setUrl("https://www.alsuper.com/buscar?q=azucar&page=1");
		request.setContentType("application/json");
		request.setMethod(Request.GET);

		Connection conn= new Connection();
		Response response= conn.send(request);


		//System.out.println(response.getBody());



	}



}