package com.dhp.blockchain;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.atomic.DoubleAccumulator;
import java.util.stream.Stream;

import com.dhp.model.EnderecosBTC;
import com.dhp.service.EnderecosBTCService;
import com.google.gson.*;
import info.blockchain.api.APIException;
import info.blockchain.api.blockexplorer.*;
import info.blockchain.api.blockexplorer.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;


@Component
public class Consultas {

	@Autowired
	private  EnderecosBTCService enderecosBTCService;


	public static Double verificaSaldo(String enderecoBTC)
    {
    	// instantiate a block explorer
    	BlockExplorer blockExplorer = new BlockExplorer();


    	// get an address and read its final balance
    	Address address = null;
		try {
			address = blockExplorer.getAddress(enderecoBTC);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	Long finalBalance = getBalance(enderecoBTC);
    	Double saldo = finalBalance.doubleValue()/100000000;
    	
    	return saldo;
    }
	
	public static boolean enderecoExiste(String enderecoBTC){
		BlockExplorer blockExplorer = new BlockExplorer();
		Address address = null;
    	try {
    		address = blockExplorer.getAddress(enderecoBTC);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return address != null ? true : false;
		
	}

	static String getSignature(String secretKey, String publicKey) throws NoSuchAlgorithmException, InvalidKeyException {

		long timestamp = System.currentTimeMillis() / 1000L;
		String payload = timestamp + "." + publicKey;

		Mac sha256_Mac = Mac.getInstance("HmacSHA256");
		SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
		sha256_Mac.init(secretKeySpec);
		String hashHex = DatatypeConverter.printHexBinary(sha256_Mac.doFinal(payload.getBytes())).toLowerCase();
		String signature = payload + "." + hashHex;
		return signature;
	}

	public static Long getBalance(String enderecoBTC){
		Long balance = null;
		try{
		String url = "https://api.blockcypher.com/v1/btc/main/addrs/"+enderecoBTC+"/balance";
		//String url = "https://api.blockcypher.com/v1/btc/test3/addrs/"+enderecoBTC+"/balance";
		URL urlObj = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
		connection.setRequestMethod("GET");

		// read all the lines of the response into response StringBuffer
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = bufferedReader.readLine()) != null) {
			response.append(inputLine);
		}
		bufferedReader.close();

		// if you don't want to use Gson, you can just print the plain response
		//System.out.println(response.toString());

		// print result in nice format using the Gson library
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(response.toString());
		JsonObject a = je.getAsJsonObject();
			balance = a.get("balance").getAsLong();
//			balance = a.get("unconfirmed_balance").getAsLong();


		} catch (Exception e){
			e.printStackTrace();
		}

		return balance;

	}

	public static void main(String[] args) throws IOException,
			NoSuchAlgorithmException, InvalidKeyException {

		String url = "https://localbitcoins.com/buy-bitcoins-online/brl/.json";
		URL urlObj = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
//		connection.setRequestMethod("GET");

		// read all the lines of the response into response StringBuffer
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = bufferedReader.readLine()) != null) {
			response.append(inputLine);
		}
		bufferedReader.close();

		// if you don't want to use Gson, you can just print the plain response
		//System.out.println(response.toString());

		// print result in nice format using the Gson library
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(response.toString());
		JsonObject test = je.getAsJsonObject();

		JsonArray Jarray = (JsonArray) jp.parse(response.toString()).getAsJsonObject().get("data").getAsJsonObject().getAsJsonArray("ad_list");

		double total = 0;
		for(int i = 0; i<10; i++){


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


			System.out.println(minAmount+" "+price);
			System.out.println(total);
			System.out.println(total/10 * 1.062);
		}

		String prettyJsonResponse = gson.toJson(je);
		//System.out.println(prettyJsonResponse);



	}
	

}
