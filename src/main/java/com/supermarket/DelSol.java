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
import com.gargoylesoftware.htmlunit.ScriptResult;

import com.http.*;

import java.util.List;
import java.math.BigDecimal;

import com.util.Util;

public class DelSol{

	public static String url="https://www.delsol.com.mx/search/";
	public static String PAGE="https://www.fahorro.com";

	public DelSol(){

	}


	public JSONObject search(String searchQuery, Integer pageNum) throws Exception {
		JSONArray listJson = new JSONArray();

		

		/*WebClient client = new WebClient(BrowserVersion.FIREFOX_60);  
		client.getOptions().setCssEnabled(false);  
		client.getOptions().setJavaScriptEnabled(true); 
		client.getOptions().setThrowExceptionOnFailingStatusCode(false);
		client.getOptions().setThrowExceptionOnScriptError(false);
        client.setRefreshHandler(new ThreadedRefreshHandler());

		String total ="0";

		HtmlPage page= null; 
		try {  
  			String searchUrl = url  + URLEncoder.encode(searchQuery, "UTF-8");
  			System.out.println(searchUrl);

  			page = client.getPage(searchUrl);
  			Thread.sleep(3000);



		} catch(Exception e){
  			e.printStackTrace();
		}

		Thread.sleep(3000);
		ScriptResult r = page.executeJavaScript("localStorage.SessionId; ");
		String cookie=(String) r.getJavaScriptResult();

		JSONParser parser = new JSONParser();
		JSONObject sessionId = (JSONObject) parser.parse(cookie);

		System.out.println(sessionId.toJSONString());*/

		Request request= new Request();
		request.setUrl("https://www.delsol.com.mx:3000/api/0.1/catalog/product/searchterm/"+searchQuery+"?&pageSize=9999");
		request.setAuthorization("Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbiI6IjVjYzlmMmRhZWVjZmYwNzM5ZWFhZjRmMCIsInVzZXJJZCI6IjM2MzE5MSIsInR5cGVVc2VyIjoiZ3Vlc3RpZGVudGl0eSIsImlzQ3NyIjpmYWxzZSwiaWF0IjoxNTU2NzM4Nzc4fQ.Vfz6lSdBjRlIHDU46m89uU6iWp5yxopnIXIjH4mFAhM");
		request.setMethod(Request.GET);
		request.setContentType("application/json");

		Connection conn= new Connection();
		Response response= conn.send(request);
		String body = response.getBody();

		JSONObject responseJson = (JSONObject) new JSONParser().parse(body);
		JSONObject data = (JSONObject) responseJson.get("data");

		JSONArray catalogEntryViews = (JSONArray) data.get("catalogEntryView");

		for(int i=0; i<catalogEntryViews.size(); i++ ){
			JSONObject catalogEntryView = (JSONObject) catalogEntryViews.get(i);
			JSONObject itemJson = new JSONObject();

			String title = (String) catalogEntryView.get("shortDescription");
			itemJson.put("titulo", title);

			String img = (String) catalogEntryView.get("thumbnail");
			itemJson.put("imagen", img);	

			itemJson.put("cadena", "DelSol");

			JSONArray preciosJson = (JSONArray) catalogEntryView.get("price");
			JSONObject priceJson = (JSONObject) preciosJson.get(0);
			String price = (String) priceJson.get("value");
			itemJson.put("precio", price.replace(",", ""));


			JSONArray palabras_claves = Util.palabrasClaves(title);
			itemJson.put("palabras_claves", palabras_claves);




			listJson.add(itemJson);

		}


		JSONObject res= new JSONObject();

		res.put("results", listJson);
		//res.put("total", total);
		//System.out.println(res.toJSONString());

		return res;

	}


	



}