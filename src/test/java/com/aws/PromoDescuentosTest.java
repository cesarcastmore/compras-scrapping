package com.stage.api.ms.entity;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.Before;
import  org.junit.Assert;

import com.aws.Configuration;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.supermarket.HebSuper;


public class PromoDescuentosTest {

    Configuration conf= null;
    FileInputStream input;
    ByteArrayOutputStream baos;

    static String SEARCH="estufa";
    static String COMPANY="promodescuentos";

	@Before
	public void initialize() throws Exception {

		try{
			input=new FileInputStream(new File("src/test/resources/compras.json"));
    		baos = new ByteArrayOutputStream();
            conf= new Configuration();

		} catch(Exception ex){
            ex.printStackTrace(); 
    		Assert.fail("Error: No se pudo inicializar");
		}


	}

    @Test
    public void method() throws Exception {

    	try{

            String request = conf.toString(input);
            JSONObject requestJson = (JSONObject) new JSONParser().parse(request);
            JSONObject params = (JSONObject) requestJson.get("params");

            JSONObject path = (JSONObject) params.get("path");
            JSONObject querystring = (JSONObject) params.get("querystring");

            path.put("cadena", COMPANY);
            querystring.put("value", SEARCH);

    		conf.execute(requestJson.toJSONString(), baos, null);
    		JSONObject response = (JSONObject) new JSONParser().parse( baos.toString());
            //System.out.println(response.toJSONString());
            JSONArray results  = (JSONArray) response.get("results");

            if(results == null){
                Assert.fail("No tiene resultado");
            } 

            System.out.println("Tiene un total de " + results.size() +" resultados");

            

       

    	} catch(Exception ex){

      		  ex.printStackTrace(); 
    		  Assert.fail("Error: No regreso nada el servicio");

    	}

    }

 }