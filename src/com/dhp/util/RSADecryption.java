package com.dhp.util;

import java.io.*;
import java.security.Security;

import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.util.PrivateKeyFactory;

import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import sun.misc.BASE64Decoder;

public class RSADecryption {

    public static void main(String[] args) throws  Exception
    {

        //@Cleanup
        FileReader privateKeyReader = new FileReader(new File("/home/m4ndr4ck/BTC/RSA/private_key.pem"));
        //@Cleanup
        PEMParser parser = new PEMParser(privateKeyReader);

        PEMKeyPair keyPair = (PEMKeyPair) parser.readObject();
        //keyPair.getPrivateKeyInfo();

        String privateKeyFilename = "/home/m4ndr4ck/BTC/RSA/private_key.pem";
        String encryptedData = "/home/m4ndr4ck/BTC/RSA/crypted";
        String outputFilename = "/home/m4ndr4ck/BTC/RSA/out";


        RSADecryption rsaDecryption = new RSADecryption();


        rsaDecryption.decrypt(keyPair, encryptedData, outputFilename);

    }

    private void decrypt (PEMKeyPair keyPair, String encryptedFilename, String outputFilename){

        try {

            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

            //String key = readFileAsString(keyPair);
            BASE64Decoder b64 = new BASE64Decoder();
            AsymmetricKeyParameter privateKey =
                    (AsymmetricKeyParameter) PrivateKeyFactory.createKey(keyPair.getPrivateKeyInfo());
            AsymmetricBlockCipher e = new RSAEngine();
            e = new org.bouncycastle.crypto.encodings.PKCS1Encoding(e);
            e.init(false, privateKey);

            String inputdata = readFileAsString(encryptedFilename);
            byte[] messageBytes = hexStringToByteArray(inputdata);
            byte[] hexEncodedCipher = e.processBlock(messageBytes, 0, messageBytes.length);

            System.out.println(new String(hexEncodedCipher));
            BufferedWriter out = new BufferedWriter(new FileWriter(outputFilename));
            out.write(new String(hexEncodedCipher));
            out.close();

        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    private String decrypt (String privateKeyFilename, String encryptedData) {

        String outputData = null;
        try {

            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

            String key = readFileAsString(privateKeyFilename);
            BASE64Decoder b64 = new BASE64Decoder();
            AsymmetricKeyParameter privateKey =
                    (AsymmetricKeyParameter) PrivateKeyFactory.createKey(b64.decodeBuffer(key));
            AsymmetricBlockCipher e = new RSAEngine();
            e = new org.bouncycastle.crypto.encodings.PKCS1Encoding(e);
            e.init(false, privateKey);

            byte[] messageBytes = hexStringToByteArray(encryptedData);
            byte[] hexEncodedCipher = e.processBlock(messageBytes, 0, messageBytes.length);

            System.out.println(new String(hexEncodedCipher));
            outputData = new String(hexEncodedCipher);

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return outputData;
    }

    public static String getHexString(byte[] b) throws Exception {
        String result = "";
        for (int i=0; i < b.length; i++) {
            result +=
                    Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return result;
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
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

}