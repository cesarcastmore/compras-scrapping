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


public class ComprasTest {

    Configuration conf= null;
    FileInputStream input;
    ByteArrayOutputStream baos;

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

    		conf.execute(input, baos, null);
            System.out.println("out" + baos.toString());
    		JSONObject json = (JSONObject) new JSONParser().parse( baos.toString());

    
   

    	} catch(Exception ex){

      		  ex.printStackTrace(); 
    		  Assert.fail("Error: No regreso nada el servicio");

    	}

    }

 }