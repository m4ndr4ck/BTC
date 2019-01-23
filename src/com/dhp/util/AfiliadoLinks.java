package com.dhp.util;

import com.google.gson.*;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class AfiliadoLinks {

    private final String USER_AGENT = "Mozilla/5.0";
    String linkAfiliado;


    public String geraLinkAfiliado(String ref){

        try {
            String token = getToken();
            linkAfiliado = getLink(token, ref);
        }catch (Exception e){
            e.printStackTrace();
        }
        return linkAfiliado;
    }

    // HTTP GET request
    private String getLink(String token, String ref) throws Exception {


        String url = "https://api-ssl.bitly.com/v3/user/link_save?access_token="+token+"&longUrl=https://btcmoedas.com.br/cadastro/"+ref;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // print result in nice format using the Gson library
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(response.toString());
        JsonObject test = je.getAsJsonObject();

        String linkAfiliado = jp.parse(response.toString()).getAsJsonObject().get("data").getAsJsonObject().get("link_save").getAsJsonObject().get("link").toString().replace("\"","" );

        return linkAfiliado;


    }

    // HTTP POST request
    private String getToken() throws Exception {

        String url = "https://api-ssl.bitly.com/oauth/access_token";
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Authorization", "Basic "+ Base64.getEncoder().encodeToString(("m4ndr4ck:hitman666").getBytes()));



        String urlParameters = "client_id=a8a3ba578248c38a5f83a2c365434d688d541f2e&client_secret=bcdd933cf3a4b308d72219560b4f1ffe2eefe192";

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        //    System.out.println("\nSending 'POST' request to URL : " + url);
        //    System.out.println("Post parameters : " + urlParameters);
        //    System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());

        return response.toString();

    }


}
