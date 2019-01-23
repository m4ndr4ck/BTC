package com.dhp.util;


//import org.bitcoinj.protocols.channels.PaymentChannelServer;
import org.apache.commons.lang3.RandomStringUtils;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.salt.RandomSaltGenerator;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;


public class Crypto {

     public static void main(String[] args)
    {

      /**  String publicKeyFilename = "/home/m4ndr4ck/BTC/RSA/public_key.pem";
        String inputFilename = "/home/m4ndr4ck/BTC/RSA/plaintext";
        String encryptedFilename = "/home/m4ndr4ck/BTC/RSA/crypted";

        Crypto rsaEncryptFile = new Crypto();

        rsaEncryptFile.encrypt(publicKeyFilename, inputFilename, encryptedFilename);

        char[] possibleCharacters = (new String("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%*")).toCharArray();
        String randomStr = RandomStringUtils.random( 12, 0, possibleCharacters.length-1, false, false, possibleCharacters, new SecureRandom() );
        System.out.println( randomStr );**/

    }

    static public String encrypt (String apikey){
         String value="";

        try {

            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            String publicKeyFilename = "/opt/public_key.pem";
            String key = readFileAsString(publicKeyFilename);
            BASE64Decoder b64 = new BASE64Decoder();
            AsymmetricKeyParameter publicKey =
                    (AsymmetricKeyParameter) PublicKeyFactory.createKey(b64.decodeBuffer(key));
            AsymmetricBlockCipher e = new RSAEngine();
            e = new org.bouncycastle.crypto.encodings.PKCS1Encoding(e);
            e.init(true, publicKey);

            String inputdata = apikey;
            byte[] messageBytes = inputdata.getBytes();
            int i = 0;
            int len = e.getInputBlockSize();
            while (i < messageBytes.length)
            {
                if (i + len > messageBytes.length)
                    len = messageBytes.length - i;

                byte[] hexEncodedCipher = e.processBlock(messageBytes, i, len);
                value = value + getHexString(hexEncodedCipher);
                i += e.getInputBlockSize();
            }

        }
        catch (Exception e) {
            System.out.println(e);
        }
        return value;
    }

    public static String getHexString(byte[] b) throws Exception {
        String result = "";
        for (int i=0; i < b.length; i++) {
            result +=
                    Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return result;
    }

    private static String readFileAsString(String filePath)
            throws java.io.IOException{
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        System.out.println(fileData.toString());
        return fileData.toString();
    }

    public static PublicKey get(String filename)
            throws Exception {

        byte[] keyBytes = Files.readAllBytes(Paths.get(filename));

        X509EncodedKeySpec spec =
                new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

}
