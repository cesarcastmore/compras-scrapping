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

public class WalMart {

	public static String url="https://www.walmart.com.mx/productos?Ntt=";
	public static String PAGE="https://www.walmart.com.mx/productos?Ntt=";

	public WalMart(){

	}


	public JSONObject search(String searchQuery, Integer pageNum) throws Exception {

		WebClient client = new WebClient(BrowserVersion.FIREFOX_60);  
		client.getOptions().setCssEnabled(true);  
		client.getOptions().setJavaScriptEnabled(true); 
		client.getOptions().setThrowExceptionOnFailingStatusCode(true);
		client.getOptions().setThrowExceptionOnScriptError(true);
            client.setRefreshHandler(new ThreadedRefreshHandler());

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
		System.out.println("HTML" +page.asXml());

		addItems("//div[@class='_1ZplxGlyCnvyO0gMSgUv3h _3pbbScAH_l-HkRL05mtPvP _37wpYTs0if2wh2mOPy3Pzb oe1yn7YsSprw1BNeuteLa _20uJJOKj_YzI06TzrVkyR4 _1i1_-U8qaR8cJPVpbFp2z-']", listJson, page);

		//HtmlElement totalHtml =  page.getFirstByXPath(".//p[@class='results-count']"); 
		//total = totalHtml.asText();
		//total= total.replace("resultados", "").trim();

		JSONObject res= new JSONObject();

		res.put("results", listJson);
		//res.put("total", total);
		System.out.println(res.toJSONString());

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

		System.out.println("HTML" +  htmlItem.asXml());
  		

		return itemJson;
	}



}