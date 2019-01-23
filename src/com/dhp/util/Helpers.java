package com.dhp.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.google.gson.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.google.protobuf.TextFormat.ParseException;

public class Helpers {
	
	@Autowired
    private MessageSource messages;
	
	@Autowired
	private Environment env;
	
	public static List<String> listarArquivos(String path){
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		
		List<String> files = new ArrayList<String>();

		    for (int i = 0; i < listOfFiles.length; i++) {
		    	if (listOfFiles[i].isFile()) {
		        System.out.println("File " + listOfFiles[i].getName());
		        files.add(listOfFiles[i].getName());
		    	}
		    }
		    
		return files;
	}
	
	public static String notificaPagSeg(Object param, boolean getcode) throws Exception{
		String url = null;
		String tipo = null;
		if (param instanceof String){
		tipo = "reference";
		url = "https://ws.pagseguro.uol.com.br/v3/transactions/notifications/"+(String)param+"?email=davi@deliverance.com.br&token=68CD735382894D66849D40F697019912";
		//url = "https://ws.sandbox.pagseguro.uol.com.br/v3/transactions/notifications/"+(String)param+"?email=davi@deliverance.com.br&token=B4ECA37335194B12915B474F67AAE4F8";

		}else if (param instanceof Integer){
		tipo = "status";	
		url = "https://ws.pagseguro.uol.com.br/v2/transactions?email=davi@deliverance.com.br&token=68CD735382894D66849D40F697019912&reference=BTCM"+(Integer)param;
		//url = "https://ws.sandbox.pagseguro.uol.com.br/v2/transactions?email=davi@deliverance.com.br&token=B4ECA37335194B12915B474F67AAE4F8&reference=BTC"+(Integer)param;

		}
		
		if (getcode) tipo = "code";
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(new URL(url).openStream());
		NodeList nList = doc.getElementsByTagName(tipo);
		String retorno = nList.item(0).getFirstChild().getTextContent();
	    
		
		return retorno;
	}

	public static double geraCotacaoBiscointFox(String param) {
		double cotacao = 0;
		String tipo = param.equals("buy") ? "compra" : "venda";

		try {
			String url = "https://biscoint.io/api/market/" + param + "/single/quote/50?currencyPair=BTC_BRL&exchanges=WALTM&version=0.1&fetchAll=false";
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


			cotacao = Double.valueOf(jp.parse(response.toString())
					.getAsJsonObject()
					.get("orderedResults")
					.getAsJsonArray()
					.get(0)
					.getAsJsonObject()
					.get("efAvgPrice").toString().replace("\"", ""));

		} catch (Exception e){
			e.printStackTrace();
		}
		return cotacao + 4500;
		//return 44999.00;

	}
	public static double geraCotacaoBitvalor(){
		double cotacao = 0;
		String param = "buy";
		String tipo = param.equals("buy") ? "compra" : "venda";

		try {
			String url = "https://api.bitvalor.com/v1/ticker.json";
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

			JsonObject Jarray =  jp.parse(response.toString()).getAsJsonObject().get("ticker_1h").getAsJsonObject().get("exchanges").getAsJsonObject().get("BZX").getAsJsonObject();
			cotacao = Jarray.get("last").getAsDouble() + 4520;

			/**double total = 0;
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

			 double cotacaoCompra =total / 10 	;
			 double cotacaoVenda =total / 10;
			 double porcentagemVenda = cotacaoVenda * 0.03;


			 if (tipo.equals("compra"))
			 cotacao = cotacaoCompra;
			 else cotacao = cotacaoVenda - porcentagemVenda;
			 }*/
		}catch (Exception e){
			cotacao = 2000;
			e.printStackTrace();
		}

		return cotacao;


	}

	public static double geraCotacaoLocalBitcoins(){

	return 0;

	}
	
	public static double geraCotacao(String param){
		  double cotacao = 0;
		  String tipo = param.equals("buy") ? "compra" : "venda";
		  JSONObject obj = new JSONObject(consultaExchanges("compra"));
		  double compra = obj.getJSONObject("BRL").getDouble("buy");
		  double venda = obj.getJSONObject("BRL").getDouble("sell");
		  if(tipo.equals("compra"))
		  	cotacao = compra * 1.3099; //COMPRA
		  else cotacao = venda * 1.004;  //VENDA
		  return cotacao;
		  //while(test.hasNext()){
			// System.out.println(test.next());
		  //}
		  
	   
		  
		  //System.out.println("Cotação de "+tipo+":" + cotacao);
		 	
		  
		  //System.out.println("Taxa real Bitcambio: " + obj2.getDouble("efAvgPrice"));
		  //System.out.println("Custo real (com taxa PagSeguro): " + obj2.getDouble("efAvgPrice") * 1.00399);
		  //System.out.println("Cotação COMPRA BTC Moedas:" + obj2.getDouble("efAvgPrice") * 1.00399 * 1.05 );
		
	}
	
	private static String consultaExchanges(String param) {

		String url = "https://blockchain.info/pt/ticker";
		StringBuffer response = new StringBuffer();
		try{
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", "Mozilla/5.0");

		int responseCode = con.getResponseCode();
		//System.out.println("\nSending 'GET' request to URL : " + url);
		//System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		} catch(Exception e){
			e.printStackTrace();
		}
		return response.toString();

	}
	
	public static BigDecimal parse(final String amount, final Locale locale) throws ParseException, java.text.ParseException {
	    final NumberFormat format = NumberFormat.getNumberInstance(locale);
	    if (format instanceof DecimalFormat) {
	        ((DecimalFormat) format).setParseBigDecimal(true);
	    }
	    return (BigDecimal) format.parse(amount.replaceAll("[^\\d.,]",""));
	}
	
	public static boolean isCPF(String CPF) {
		// considera-se erro CPF's formados por uma sequencia de numeros iguais
		if (CPF.equals("00000000000") || CPF.equals("11111111111") ||
				CPF.equals("22222222222") || CPF.equals("33333333333") ||
				CPF.equals("44444444444") || CPF.equals("55555555555") ||
				CPF.equals("66666666666") || CPF.equals("77777777777") ||
				CPF.equals("88888888888") || CPF.equals("99999999999") ||
				(CPF.length() != 11))
			return(false);

		char dig10, dig11;
		int sm, i, r, num, peso;

		// "try" - protege o codigo para eventuais erros de conversao de tipo (int)
		try {
			// Calculo do 1o. Digito Verificador
			sm = 0;
			peso = 10;
			for (i=0; i<9; i++) {              
				// converte o i-esimo caractere do CPF em um numero:
				// por exemplo, transforma o caractere '0' no inteiro 0         
				// (48 eh a posicao de '0' na tabela ASCII)         
				num = (int)(CPF.charAt(i) - 48); 
				sm = sm + (num * peso);
				peso = peso - 1;
			}

			r = 11 - (sm % 11);
			if ((r == 10) || (r == 11))
				dig10 = '0';
			else dig10 = (char)(r + 48); // converte no respectivo caractere numerico

			// Calculo do 2o. Digito Verificador
			sm = 0;
			peso = 11;
			for(i=0; i<10; i++) {
				num = (int)(CPF.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso - 1;
			}

			r = 11 - (sm % 11);
			if ((r == 10) || (r == 11))
				dig11 = '0';
			else dig11 = (char)(r + 48);

			// Verifica se os digitos calculados conferem com os digitos informados.
			if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
				return(true);
			else return(false);
		} catch (InputMismatchException erro) {
			return(false);
		}
	}

	public static void main(String[] args) throws Exception {

		System.out.println(geraCotacaoLocalBitcoins());




	}
}
