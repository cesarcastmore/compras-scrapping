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

import com.supermarket.*;
import com.util.FireStoreClient;

import java.util.Map;
import java.util.HashMap;

public  class Configuration {

    public FireStoreClient fire;

    public void execute(InputStream inputStream, OutputStream outputStream, Context ctt) throws Exception{
        String requestString = toString(inputStream);
        execute(requestString, outputStream, ctt);
    }

  public void execute(String requestString, OutputStream outputStream, Context ctt) throws Exception
	{

        this.fire = new FireStoreClient();

        JSONParser parser = new JSONParser();
        JSONObject jsonRequest = (JSONObject) parser.parse(requestString);
        System.out.println(jsonRequest.toJSONString());

        JSONObject context = (JSONObject) jsonRequest.get("context");
        JSONObject params = (JSONObject) jsonRequest.get("params");
        JSONObject path = (JSONObject) params.get("path");
        JSONObject querystring = (JSONObject) params.get("querystring");

        String resource_path = (String) context.get("resource-path");

        if(resource_path.equals("/compras/{cadena}")){
            String cadena = (String) path.get("cadena");

            JSONObject  res = new JSONObject();
            String search= (String) querystring.get("value");
            String uuid= (String) querystring.get("uuid");

            Long page= toLong(querystring.get("page"));

            if(cadena.equals("heb")){

                HebSuper heb = new HebSuper();
                res = heb.search(search, page.intValue());

            }else if(cadena.equals("super_soriana")){

                SorianaSuper soriana = new SorianaSuper();
                res = soriana.search(search, page.intValue());

            }else if(cadena.equals("soriana")){

                Soriana soriana = new Soriana();
                res = soriana.search(search);

            }else if(cadena.equals("liverpool")){

                Liverpool liverpool = new Liverpool();
                res = liverpool.search(search, 1);

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

            }else if(cadena.equals("farmacias_del_ahorro")){

                FarmaciasDelAhorro pd = new FarmaciasDelAhorro();
                res = pd.search(search, page.intValue());

            }else if(cadena.equals("walmart")){

                WalMart pd = new WalMart();
                res = pd.search(search, page.intValue());

            }else if(cadena.equals("famsa")){

                Famsa pd = new Famsa();
                res = pd.search(search, page.intValue());

            }else if(cadena.equals("home_depot")){

                HomeDepot pd = new HomeDepot();
                res = pd.search(search, page.intValue());

            }else if(cadena.equals("pcel")){

                Pcel pd = new Pcel();
                res = pd.search(search, page.intValue());

            }else if(cadena.equals("costco")){

                Costco pd = new Costco();
                res = pd.search(search, page.intValue());

            }else if(cadena.equals("coppel")){

                Coppel pd = new Coppel();
                res = pd.search(search, page.intValue());

            }else if(cadena.equals("super_walmart")){

                SuperWalMart pd = new SuperWalMart();
                res = pd.search(search, page.intValue());

            }else if(cadena.equals("alsuper")){

                AlSuper pd = new AlSuper();
                res = pd.search(search);

            }else if(cadena.equals("officemax")){

                OfficeMax pd = new OfficeMax();
                res = pd.search(search, page.intValue());

            }else if(cadena.equals("delsol")){

                DelSol pd = new DelSol();
                res = pd.search(search, page.intValue());

            }else if(cadena.equals("superama")){

                Superama pd = new Superama();
                res = pd.search(search, page.intValue());

            }else if(cadena.equals("bodega_aurrera")){

                BodegaAurrera pd = new BodegaAurrera();
                res = pd.search(search, page.intValue());

            }else if(cadena.equals("chedraui")){

                Chedraui pd = new Chedraui();
                res = pd.search(search, page.intValue());

            }else if(cadena.equals("innova_sport")){

                InnovaSport pd = new InnovaSport();
                res = pd.search(search, page.intValue());

            }else if(cadena.equals("marti")){

                Marti pd = new Marti();
                res = pd.search(search, page.intValue());

            }else if(cadena.equals("farmacias_especializadas")){
                
                FarmaciasEspecializadas fe= new FarmaciasEspecializadas();
                res = fe.search(search, page.intValue());

            }else if(cadena.equals("palacio_hierro")){
                
                PalacioHierro ph= new PalacioHierro();
                res = ph.search(search, page.intValue());

            }else if(cadena.equals("netshoes")){
                
                NetShoes ns= new NetShoes();
                res = ns.search(search, page.intValue());

            }else if(cadena.equals("elektra")){
                
                Elektra ek= new Elektra();
                res = ek.search(search, page.intValue());

            }else if(cadena.equals("parisina")){
                
                Parisina par= new Parisina();
                res = par.search(search);

            }

            JSONArray results= (JSONArray) res.get("results");

            for(Integer i=0; i<results.size(); i++ ){
                JSONObject item= (JSONObject) results.get(i);
                item.put("value", cadena);

                if(i == results.size() - 1 ){
                    item.put("end", true);
                }
                this.fire.insert(uuid, item);

            }


            res.put("search", search);


            outputStream.write(res.toJSONString().getBytes(Charset.forName("UTF-8")));
            outputStream.close();

        }


        if(resource_path.equals("/compras/busqueda")){
            JSONObject  res = new JSONObject();

            String uuid= (String) querystring.get("uuid");
            this.fire.delete(uuid);


            outputStream.write(res.toJSONString().getBytes(Charset.forName("UTF-8")));
            outputStream.close();
        }





	}


	public String toString(InputStream in) throws Exception
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