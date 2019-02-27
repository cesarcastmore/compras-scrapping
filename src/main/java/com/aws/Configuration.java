package com.aws;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.nio.charset.Charset;

import com.amazonaws.services.lambda.runtime.Context;

import com.supermarket.HebSuper;
import com.supermarket.SorianaSuper;
import com.supermarket.Soriana;
import com.supermarket.Liverpool;
import com.supermarket.Sears;
import com.supermarket.Sanborns;
import com.supermarket.BestBuy;
import com.supermarket.OfficeDepot;
import com.supermarket.Sams;
import com.supermarket.PromoDescuentos;

public  class Configuration {

  public void execute(InputStream inputStream, OutputStream outputStream, Context ctt) throws Exception
	{
	    String requestString = toString(inputStream);
        JSONParser parser = new JSONParser();
        JSONObject jsonRequest = (JSONObject) parser.parse(requestString);

        JSONObject context = (JSONObject) jsonRequest.get("context");
        JSONObject params = (JSONObject) jsonRequest.get("params");
        JSONObject path = (JSONObject) params.get("path");
        JSONObject querystring = (JSONObject) params.get("querystring");

        String resource_path = (String) context.get("resource-path");

        if(resource_path.equals("/compras/{cadena}")){
            String cadena = (String) path.get("cadena");

            JSONObject  res = new JSONObject();
            String search= (String) querystring.get("value");
            Long page= toLong(querystring.get("page"));

            if(cadena.equals("heb")){

                HebSuper heb = new HebSuper();
                res = heb.search(search, page.intValue());

            }else if(cadena.equals("soriana_super")){

                SorianaSuper soriana = new SorianaSuper();
                res = soriana.search(search, page.intValue());

            }else if(cadena.equals("soriana")){

                Soriana soriana = new Soriana();
                res = soriana.search(search, page.intValue());

            }else if(cadena.equals("liverpool")){

                Liverpool liverpool = new Liverpool();
                res = liverpool.search(search, page.intValue());

            }else if(cadena.equals("sears")){

                Sears sears = new Sears();
                res = sears.search(search, page.intValue());

            }else if(cadena.equals("sanborns")){

                Sanborns sanborns = new Sanborns();
                res = sanborns.search(search, page.intValue());

            }else if(cadena.equals("bestbuy")){

                BestBuy bestbuy = new BestBuy();
                res = bestbuy.search(search, page.intValue());

            }else if(cadena.equals("officedepot")){

                OfficeDepot office = new OfficeDepot();
                res = office.search(search, page.intValue());

            }else if(cadena.equals("sams")){

                Sams sams = new Sams();
                res = sams.search(search, page.intValue());

            }else if(cadena.equals("promodescuentos")){

                PromoDescuentos pd = new PromoDescuentos();
                res = pd.search(search, page.intValue());

            }
            outputStream.write(res.toJSONString().getBytes(Charset.forName("UTF-8")));
            outputStream.close();

        }





	}


	private String toString(InputStream in) throws Exception
    {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String read;

        while ((read = br.readLine()) != null)
        {
            sb.append(read);
        }

        br.close();
        return sb.toString();
    }

    private  Long toLong(Object obj){

        if(obj instanceof Long ){
            return (Long) obj;
        } else if(obj instanceof Integer){
            return Long.valueOf((Integer) obj);
        }else if(obj instanceof String){
            String value = (String) obj;
            return Long.parseLong(value);
        }

        return null;
    }
}