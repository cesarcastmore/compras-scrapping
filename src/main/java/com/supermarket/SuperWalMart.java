package com.supermarket;

import com.gargoylesoftware.htmlunit.ScriptResult;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.URLEncoder;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAddress;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;
import org.json.simple.parser.ParseException;

import java.util.Iterator;
import java.util.List;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

public class SuperWalMart {

	public static String url="https://www.walmart.com.mx/productos?Ntt=";
	public static String PAGE="https://www.walmart.com.mx/productos?Ntt=";

	public SuperWalMart(){

	}


	public JSONObject search(String searchQuery, Integer pageNum) throws Exception {

		WebClient client = new WebClient(BrowserVersion.CHROME);
		client.getOptions().setCssEnabled(true);
		client.getOptions().setJavaScriptEnabled(true);
		client.getOptions().setThrowExceptionOnFailingStatusCode(false);
		client.getOptions().setThrowExceptionOnScriptError(false);
		client.setRefreshHandler(new ThreadedRefreshHandler());
		JSONArray listJson = new JSONArray();

		String url = "https://super.walmart.com.mx/productos?Ntt=" +searchQuery;

		try {
			System.out.println(url);

			HtmlPage page = client.getPage(url);

			page.executeJavaScript("fetch(\"https://super.walmart.com.mx/productos?Ntt=" + URLEncoder.encode(searchQuery, "UTF-8") + "&Nrpp=24&No=0\", {\"credentials\":\"omit\",\"headers\":{\"accept\":\"application/json\",\"content-type\":\"application/json\"},\"referrer\":\"https://super.walmart.com.mx/productos?Ntt=martillo\",\"referrerPolicy\":\"no-referrer-when-downgrade\",\"body\":null,\"method\":\"GET\",\"mode\":\"cors\"}).then(function(response) {\n" +
					"    return response.json();\n" +
					"  })\n" +
					"  .then(function(myJson) {\n" +
					"    window.a = myJson;\n" +
					"let b = [];\n" +
					"\n" +
					"a.contents[0].mainArea[1].records.forEach(e => {\n" +
					"\tb.push({href: e.attributes[\"productSeoUrl\"][0] ,name: e.attributes[\"product.displayText\"][0], price: e.attributes[\"sku.price\"][0], image: \"https://www.walmart.com.mx\" + e.attributes.smallProductImageUrl[0]});\n" +
					"});\n" +
					"\n" +
					"a = b;\n" +
					"  });");

			Thread.sleep(7000);

			ScriptResult r = page.executeJavaScript("JSON.stringify(a)");

			JSONParser parser = new JSONParser();
			System.out.println((String) r.getJavaScriptResult());
			JSONArray json = (JSONArray) parser.parse(((String) r.getJavaScriptResult()));

			for (int x = 0; x < json.size(); x++){
				JSONObject o = (JSONObject) json.get(x);

				JSONObject itemJson= new JSONObject();
				itemJson.put("cadena", "WalMart");
				itemJson.put("precio",o.get("price") );
				itemJson.put("imagen", o.get("image"));
				itemJson.put("enlace_informacion", o.get("href"));
				itemJson.put("titulo", o.get("name"));
				listJson.add(itemJson);

			}

		} catch (IOException | InterruptedException | ParseException e) {
			e.printStackTrace();
		}

		//addItems("//div[@class='_1ZplxGlyCnvyO0gMSgUv3h _3pbbScAH_l-HkRL05mtPvP _37wpYTs0if2wh2mOPy3Pzb oe1yn7YsSprw1BNeuteLa _20uJJOKj_YzI06TzrVkyR4 _1i1_-U8qaR8cJPVpbFp2z-']", listJson, page);

		//HtmlElement totalHtml =  page.getFirstByXPath(".//p[@class='results-count']"); 
		//total = totalHtml.asText();
		//total= total.replace("resultados", "").trim();

		JSONObject res= new JSONObject();
		res.put("results", listJson);
		//res.put("total", total);


		System.out.println(res.toJSONString());

		return res;

	}


	



}