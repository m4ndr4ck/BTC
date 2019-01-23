package com.dhp.blockchain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutionException;

//import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.InsufficientMoneyException;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.PeerGroup;
//import org.bitcoinj.store.BlockStore;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.store.BlockStoreException;
import org.bitcoinj.store.MemoryBlockStore;

import com.dhp.blockchain.api.wallet.Wallet;
import com.dhp.blockchain.api.wallet.entity.Address;
import com.dhp.blockchain.api.wallet.entity.CreateWalletResponse;

public class Carteiras {

	public static String criarEndereco(int clienteid){
		NetworkParameters env = NetworkParameters.prodNet();
		//NetworkParameters env = TestNet3Params.get();

		String enderecobtc = null;
		try {
			Path pathpub = Paths.get("/opt/prodpubkey");
			//Path pathpub = Paths.get("/opt/testpubkey");
			byte[] datapub = Files.readAllBytes(pathpub);
			DeterministicKey pubkey = DeterministicKey.deserialize(env, datapub);
			enderecobtc = HDKeyDerivation.deriveChildKey(pubkey , clienteid).toAddress(env).toString();
		} catch (Exception e){
			e.printStackTrace();
		}
		return enderecobtc;
	}
	
	
	@SuppressWarnings("deprecation")
	public static CreateWalletResponse criarCarteira(String senha, String nome, String email){
		
		CreateWalletResponse walletResponse = null;
		
		ECKey key = new ECKey();
		String privateKey = key.getPrivateKeyAsWiF(NetworkParameters.prodNet());
		
		 try {
			 		 walletResponse = Wallet.create(
					 "http://127.0.0.1:3000/",
					 senha,
					 "",
					 privateKey,
					 nome,
					 email
					 );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return walletResponse;
		
	}
	
	public static void testBitcoinj() throws Exception{
		NetworkParameters params = NetworkParameters.prodNet();
	//	BlockStore blockstore = new  MemoryBlockStore(params);
		org.bitcoinj.wallet.Wallet wallet = new org.bitcoinj.wallet.Wallet(params);
//		org.bitcoinj.core.BlockChain chain = new org.bitcoinj.core.BlockChain(params, blockstore);
		//PeerGroup peerGroup = new PeerGroup(params, chain);
		//peerGroup.addWallet(wallet);
		//peerGroup.start(); ///

		//Address a = wallet.currentReceiveAddress();
		//ECKey b = wallet.currentReceiveKey();
		//Address c = wallet.freshReceiveAddress();

		//System.out.println(b.toString() + "\n Endereco BTC valido:" + c.toString());
		wallet.saveToFileStream(new FileOutputStream(new File("/home/m4ndr4ck/wallet.test")));



		// Get the address 1RbxbA1yP2Lebauuef3cBiBho853f7jxs in object form.
		//org.bitcoinj.core.Address targetAddress = new org.bitcoinj.core.Address(params, "1RbxbA1yP2Lebauuef3cBiBho853f7jxs");
		// Do the send of 1 BTC in the background. This could throw InsufficientMoneyException.
		//org.bitcoinj.wallet.Wallet.SendResult result = wallet.sendCoins(peerGroup, targetAddress, Coin.COIN);
		// Save the wallet to disk, optional if using auto saving (see below).
		//wallet.saveToFileStream(new FileOutputStream(new File("/home/m4ndr4ck/wallet.test")));
		// Wait for the transaction to propagate across the P2P network, indicating acceptance.
		//result.broadcastComplete.get();
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception
    {
    
		ECKey key = new ECKey();
		
		byte[] pub = key.getPubKey(); // byte array
		
		String prv; 
		prv = key.getPrivateKeyAsWiF(NetworkParameters.prodNet());
		//System.out.println(prv);
		
        /**CreateWalletResponse walletResponse = Wallet.create(
        	    "http://127.0.0.1:3000/",
        	    "rzMTE1JiJsKP",
        	    "0933f4f7-d5a8-406d-a3e0-2e8f2f80e4c6",
        	    prv, //Private Key
        	    "Test Wallet", // Label
        	    "davi@deliverance.com.br" //E-mail,

        		);
        
        //System.out.println(walletResponse.getIdentifier());
        System.out.println("Pub key:" + walletResponse.getAddress());
        System.out.println("Pri key:" + prv);
        System.out.println(walletResponse.getIdentifier());
		//testBitcoinj();*/

		Wallet wallet = new Wallet(
				"http://localhost:3000/",
				"0933f4f7-d5a8-406d-a3e0-2e8f2f80e4c6",
				"1bcb78aa-7f2f-4d46-9800-09b4b7c78c38",
				"qz37B9AC9j8#");

		long totalBalance = wallet.getBalance();
		System.out.println(String.format("The total wallet balance is %s", totalBalance));

		// 	list all addresses and their balances
		List<Address> addresses = wallet.listAddresses();
		for (Address a : addresses)
			{
			System.out.println(String.format("The address %s has a balance of %s satoshi",
					a.getAddress(), a.getBalance()));
		}
        
        	
    	/**Wallet wallet = new Wallet(
    	        "http://localhost:3000/",
    	        "YOU_API_CODE",	
    	        "YOUR_GUID",
    	        "YOUR_SUPER_SECURE_PASSWORD");

    	// get an address from your wallet and include only transactions with up to 3
    	// confirmations in the balance
    	Address addr = wallet.getAddress("1JzSZFs2DQke2B3S4pBxaNaMzzVZaG4Cqh");
    	System.out.println(String.format("The balance is %s", addr.getBalance()));

    	// send 0.2 bitcoins with a custom fee of 0.01 BTC and a note
        // public notes require a minimum transaction size of 0.005 BTC
    	PaymentResponse payment = wallet.send("1dice6YgEVBf88erBFra9BHf6ZMoyvG88", 20000000L, null, 1000000L);
    	System.out.println(String.format("The payment TX hash is %s", payment.getTxHash()));

    	long totalBalance = wallet.getBalance();
    	System.out.println(String.format("The total wallet balance is %s", totalBalance));

    	// list all addresses and their balances 
    	List<Address> addresses = wallet.listAddresses();
    	for (Address a : addresses)
    	{
    		System.out.println(String.format("The address %s has a balance of %s satoshi",
    				a.getAddress(), a.getBalance()));
    	}

    	// archive an old address
    	wallet.archiveAddress("1JzSZFs2DQke2B3S4pBxaNaMzzVZaG4Cqh");

    	// create a new address and attach a label to it
    	Address newAddr = wallet.newAddress("test label 123");
    	System.out.println(String.format("The new address is %s", newAddr.getAddress()));**/
    }
	
}
