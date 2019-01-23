package com.dhp.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class SendBitcoin {

    public String send(String address, String amount) throws Exception {

        long unixTime = System.currentTimeMillis() / 1000L;
        String hmac_key = "";
        String api_endpoint ="/api/wallet-send/";
        String postParameters = "address="+address+"&amount="+amount;
        String secret = "";

        String message = unixTime + hmac_key + api_endpoint + postParameters;

        String url = "https://localbitcoins.com/api/wallet-send/";
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("charset", "utf-8");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        con.setRequestProperty("Apiauth-Key", "");
        con.setRequestProperty("Apiauth-Nonce", String.valueOf(unixTime));
        con.setRequestProperty("Apiauth-Signature", hmacDigest(message, secret, "HmacSHA256"));

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(postParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + postParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in;
        if (responseCode == 200) {
            return "200";
        } else{
            in = new BufferedReader(new InputStreamReader(con.getErrorStream(), Charset.forName("UTF-8")));
        }

        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(responseCode);
        return ("400");
        //return ("200");

    }

    public String hmacDigest(String msg, String keyString, String algo) {
        String digest = null;
        try {
            SecretKeySpec key = new SecretKeySpec((keyString).getBytes("UTF-8"), algo);
            Mac mac = Mac.getInstance(algo);
            mac.init(key);

            byte[] bytes = mac.doFinal(msg.getBytes("ASCII"));

            StringBuffer hash = new StringBuffer();
            for (int i = 0; i < bytes.length; i++) {
                String hex = Integer.toHexString(0xFF & bytes[i]);
                if (hex.length() == 1) {
                    hash.append('0');
                }
                hash.append(hex);
            }
            digest = hash.toString();
        } catch (UnsupportedEncodingException e) {
        } catch (InvalidKeyException e) {
        } catch (NoSuchAlgorithmException e) {
        }
        return digest;
    }

}
