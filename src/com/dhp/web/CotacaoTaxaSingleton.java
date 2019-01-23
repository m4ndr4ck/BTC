package com.dhp.web;

import com.google.gson.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class CotacaoTaxaSingleton {


    private static CotacaoTaxaSingleton instance = null;

    private CotacaoTaxaSingleton(){

    }

    public static synchronized CotacaoTaxaSingleton getInstance() {
        if(instance == null) {
            instance = new CotacaoTaxaSingleton();
        }
        return instance;
    }

    public Map<String, Double> FeeLocalBitcoins(){
        double fee=0;

        try {
            String url = "https://localbitcoins.com/api/fees/";
            URL urlObj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();

            // read all the lines of the response into response StringBuffer
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = bufferedReader.readLine()) != null) {
                response.append(inputLine);
            }
            bufferedReader.close();

            // print result in nice format using the Gson library
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(response.toString());
            String strFee =  jp.parse(response.toString()).getAsJsonObject().get("data").getAsJsonObject().get("outgoing_fee").toString().replace("\"", "");
            //System.out.println("Taxa: " + strFee);

            fee = Double.valueOf(strFee);

        }catch (Exception e){
            e.printStackTrace();
        }

        Map<String, Double> taxas = new HashMap<>();
        taxas.put("fee", fee);
        taxas.put("taxaBTC", fee*2);

        return taxas;

    }

    public double geraCotacaoLocalBitcoins(String param){
        double cotacao = 0;
        String tipo = param.equals("buy") ? "compra" : "venda";

        try {
            String url = "https://localbitcoins.com/"+param+"-bitcoins-online/brl/.json";
            URL urlObj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();

            // read all the lines of the response into response StringBuffer
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = bufferedReader.readLine()) != null) {
                response.append(inputLine);
            }
            bufferedReader.close();

            // print result in nice format using the Gson library
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(response.toString());
            JsonObject test = je.getAsJsonObject();

            JsonArray Jarray = (JsonArray) jp.parse(response.toString()).getAsJsonObject().get("data").getAsJsonObject().getAsJsonArray("ad_list");

            double total = 0;
            for (int i = 0; i < 10; i++) {


                double price = Double.valueOf(Jarray
                        .get(i)
                        .getAsJsonObject()
                        .get("data")
                        .getAsJsonObject()
                        .get("temp_price")
                        .toString().replace("\"", ""));

                double minAmount = Double.valueOf(Jarray
                        .get(i)
                        .getAsJsonObject()
                        .get("data")
                        .getAsJsonObject()
                        .get("min_amount")
                        .toString().replace("\"", ""));

                total = total + price;

                double cotacaoCompra =total / 10 * 0.05;
                double cotacaoVenda =total / 10;
                double porcentagemVenda = cotacaoVenda * 0.03;


                if (tipo.equals("compra"))
                    cotacao = cotacaoCompra;
                else cotacao = cotacaoVenda - porcentagemVenda;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return cotacao;

    }




}
