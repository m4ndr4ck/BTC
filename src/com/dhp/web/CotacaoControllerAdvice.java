package com.dhp.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.google.gson.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.dhp.util.Helpers;

@ControllerAdvice(annotations = CotacaoController.class)
public class CotacaoControllerAdvice {

	public static double getCotacaoCOMPRA(){return Helpers.geraCotacaoLocalBitcoins();}
	public static double getCotacaoVENDA(){return geraCotacaoLocalBitcoins("sell");}
	//public static double getFee(){return FeeLocalBitcoins().get("fee");}
	//public static double getFee(){return 0.00025;}
	//public static double getTaxaBTC(){return FeeLocalBitcoins();}

	Locale locale = new Locale("pt", "BR");
    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
	
	@ModelAttribute
	public void setCotacoes(Model model){
    	model.addAttribute("cotacaoCOMPRA", currencyFormatter.format(getCotacaoCOMPRA()));
//    	model.addAttribute("cotacaoVENDA", currencyFormatter.format(getCotacaoVENDA()));
//		model.addAttribute("bitcoinFEE", getFee());
//		model.addAttribute("taxaBTC", getTaxaBTC());

	}


	public static double FeeLocalBitcoins(){
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
		taxas.put("taxaBTC", 0.0005);

		return fee;

	}

	public static double geraCotacaoWall(){
		double cotacao = 99999;
		try {
			String url = "https://s3.amazonaws.com/data-production-walltime-info/production/dynamic/walltime-info.json?now=1528962473468.679.0000000000873";
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

			cotacao = jp.parse(response.toString()).getAsJsonObject().get("BRL_XBT").getAsJsonObject().get("last_inexact").getAsDouble();
				}catch (Exception e){
			e.printStackTrace();
		}
		return cotacao+1750;
	}

	public static double geraCotacaoLocalBitcoins(String param){
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

			double total = 4500;
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

				double cotacaoCompra =total / 10;
				double cotacaoVenda =total / 10;
				double porcentagemVenda = cotacaoVenda * 0;


				if (tipo.equals("compra"))
					cotacao = cotacaoCompra + 4500;
				else cotacao = cotacaoVenda - porcentagemVenda;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return cotacao;

	}

	public static void main(String... args){
		System.out.print(geraCotacaoWall());
	}

}
