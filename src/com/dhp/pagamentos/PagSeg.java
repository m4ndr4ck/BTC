/*
 * 2007-2016 [PagSeguro Internet Ltda.]
 *
 * NOTICE OF LICENSE
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Copyright: 2007-2016 PagSeguro Internet Ltda.
 * Licence: http://www.apache.org/licenses/LICENSE-2.0
 */
package com.dhp.pagamentos;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Map;

import br.com.uol.pagseguro.api.PagSeguro;
import br.com.uol.pagseguro.api.PagSeguroEnv;
import br.com.uol.pagseguro.api.checkout.CheckoutRegistrationBuilder;
import br.com.uol.pagseguro.api.checkout.RegisteredCheckout;
import br.com.uol.pagseguro.api.common.domain.ShippingType;
import br.com.uol.pagseguro.api.common.domain.builder.*;
import br.com.uol.pagseguro.api.common.domain.enums.*;
import br.com.uol.pagseguro.api.credential.Credential;
import br.com.uol.pagseguro.api.http.JSEHttpClient;
import br.com.uol.pagseguro.api.preapproval.PreApprovalRegistrationBuilder;
import br.com.uol.pagseguro.api.preapproval.RegisteredPreApproval;
import br.com.uol.pagseguro.api.session.CreatedSession;
import br.com.uol.pagseguro.api.transaction.register.DirectPaymentRegistrationBuilder;
import br.com.uol.pagseguro.api.transaction.search.TransactionDetail;
import br.com.uol.pagseguro.api.utils.logging.SimpleLoggerFactory;

/**
 * @author PagSeguro Internet Ltda.
 */
public class PagSeg {

  public static String criaSessao(){
	  String sellerEmail = "";
	  String sellerToken = "";//SANDBOX
	  final PagSeguro pagSeguro = PagSeguro
			  .instance(new SimpleLoggerFactory(), new JSEHttpClient(),
					  Credential.sellerCredential(sellerEmail, sellerToken), PagSeguroEnv.SANDBOX);

	  CreatedSession createdSessionApplication = pagSeguro.sessions().create();
	  return createdSessionApplication.getId();
  }

  public static String NovoPagamento(String valorPagamento, String bitcoins, String nome, String email, String cpf, Long referencia){
	  
	    String sellerEmail = "";
	    String sellerToken = "";
	    String urlRetorno = "";
	    

	    try {

	      final PagSeguro pagSeguro = PagSeguro
	          .instance(new SimpleLoggerFactory(), new JSEHttpClient(),
	              Credential.sellerCredential(sellerEmail, sellerToken), PagSeguroEnv.PRODUCTION);
	      RegisteredCheckout registeredCheckout = pagSeguro.checkouts().register(//
	              new CheckoutRegistrationBuilder() //
	                  .withCurrency(Currency.BRL) //
	                  .withReference("BTCM"+referencia.toString())
	                  .withSender(new SenderBuilder()//
	                      .withEmail(email)//
	                      .withName(nome)
	                      .withCPF(cpf)) //
	          
	                  .addItem(new PaymentItemBuilder()//
	                      .withId("0001")//
	                      .withDescription(bitcoins + " BTC") //
	                      .withAmount(new BigDecimal(valorPagamento))//
	                      .withQuantity(1))
				  		.addParameter((new ParameterBuilder()
						  .withName("redirectURL")
						  .withValue("https://btcmoedas.com.br/painel")))
	                      
	    		  );
	      urlRetorno = registeredCheckout.getRedirectURL().replace("ws.", "");
	      

	    }catch (Exception e){
	      e.printStackTrace();
	    }
		return urlRetorno;
	  
  }
  
  public static String NovaAssinatura(String valorPagamento, String bitcoins, String nome, String email, String cpf, long referencia){
	  
	    String sellerEmail = "";
	    String sellerToken = "";
	    String urlRetorno = "";

	     try{
	      final PagSeguro pagSeguro = PagSeguro
	          .instance(new SimpleLoggerFactory(), new JSEHttpClient(),
	              Credential.sellerCredential(sellerEmail, sellerToken), PagSeguroEnv.PRODUCTION);


	      //Assinatura
	      RegisteredPreApproval registeredPreApproval = pagSeguro.preApprovals().register(
	          new PreApprovalRegistrationBuilder()
	              .withCurrency(Currency.BRL)
	              .withReference("BTC"+referencia)
	              .withSender(new SenderBuilder()//
	                  .withEmail(email)//
	                  .withName(nome)
	                  .withCPF(cpf)) //
	              .withPreApproval(new PreApprovalBuilder()
	                  .withCharge("auto")
	                  .withName("Investimento mensal de "+bitcoins+" BTC")
	                  .withDetails("A cada 30 dias será debitado o valor de R$"+ valorPagamento)
	                  .withAmountPerPayment(new BigDecimal(valorPagamento))
	                  .withMaxTotalAmount(new BigDecimal(valorPagamento))
	                  .withPeriod("monthly")
	              )
	              .withRedirectURL("http://btcmoedas.com.br/retorno")
	              .withNotificationURL("http://btcmoedas.com.br/retorno")
	      );
	      urlRetorno = registeredPreApproval.getRedirectURL().replace("ws.", "");
	    }catch (Exception e){
	      e.printStackTrace();
	    }
	 return urlRetorno;
  }
  

  public static void main(String[] args){

    String sellerEmail = "";
    String sellerToken = "";

    try {
    	final PagSeguro pagSeguro = PagSeguro
  	          .instance(new SimpleLoggerFactory(), new JSEHttpClient(),
  	              Credential.sellerCredential(sellerEmail, sellerToken), PagSeguroEnv.SANDBOX);
      //Criando um checkout
      RegisteredCheckout registeredCheckout = pagSeguro.checkouts().register(//
          new CheckoutRegistrationBuilder() //
              .withCurrency(Currency.BRL) //
              .withExtraAmount(BigDecimal.ONE) //
              .withReference("XXXXXX")
              .withSender(new SenderBuilder()//
                  .withEmail("comprador@uol.com.br")//
                  .withName("Jose Comprador")
                  .withCPF("05864388904")) //
      
              .addItem(new PaymentItemBuilder()//
                  .withId("0001")//
                  .withDescription("DHP ") //
                  .withAmount(new BigDecimal(99999.99))//
                  .withQuantity(1)
                  .withWeight(1000))

      

              //Para definir o a inclusão ou exclusão de um meio você deverá utilizar três parâmetros: o parâmetro que define a configuração do grupo,
              // o grupo de meios de pagamento e o nome do meio de pagamento.
              // No parâmetro que define a configuração do grupo você informará se o grupo ou o meio de pagamento será incluído ou excluído.
              // Já no grupo você informará qual o grupo de meio de pagamento que receberá a configuração definida anteriormente.
              // Você poderá incluir e excluir os grupos de meios de pagamento Boleto, Débito, Cartão de Crédito, Depósito Bancário e Saldo PagSeguro.
              // Já no parâmetro nome você informará qual o meio de pagamento que receberá a configuração definida anteriormente. Os meios são as bandeiras e bancos disponíveis.
              //Atenção:  - Não é possível incluir e excluir o mesmo grupo de meio de pagamento (Ex.: incluir e excluir o grupo CREDIT_CARD).
              // - Não é possível incluir um grupo e um meio do mesmo grupo (Ex.: incluir grupo cartão e bandeira visa na mesma chamada);
              // - Não é possível excluir um grupo e um meio do mesmo grupo (Ex.: excluir grupo cartão e bandeira visa na mesma chamada);
              // - Não é possível incluir e excluir o mesmo meio de pagamento (Ex.: incluir e excluir a bandeira VISA).

              .withAcceptedPaymentMethods(new AcceptedPaymentMethodsBuilder()
                  .addInclude(new PaymentMethodBuilder()
                      .withGroup(PaymentMethodGroup.BALANCE)
                  )
                  .addInclude(new PaymentMethodBuilder()
                      .withGroup(PaymentMethodGroup.BANK_SLIP)
                  )
              )

              //Para definir o percentual de desconto para um meio de pagamento você deverá utilizar três parâmetros: grupo de meios de pagamento, chave configuração de desconto e o percentual de desconto. No parâmetro de grupo você deve informar um dos meios de pagamento disponibilizados pelo PagSeguro. Após definir o grupo é necessário definir os a configuração dos campos chave e valor.
              .addPaymentMethodConfig(new PaymentMethodConfigBuilder()
                  .withPaymentMethod(new PaymentMethodBuilder()
                      .withGroup(PaymentMethodGroup.CREDIT_CARD)
                  )
                  .withConfig(new ConfigBuilder()
                      .withKey(ConfigKey.DISCOUNT_PERCENT)
                      .withValue(new BigDecimal(10.00))
                  )
              )
              .addPaymentMethodConfig(new PaymentMethodConfigBuilder()
                  .withPaymentMethod(new PaymentMethodBuilder()
                      .withGroup(PaymentMethodGroup.BANK_SLIP)
                  )
                  .withConfig(new ConfigBuilder()
                      .withKey(ConfigKey.DISCOUNT_PERCENT)
                      .withValue(new BigDecimal(10.00))
                  )
              )

              //Para definir o parcelamento você deverá utilizar três parâmetros: grupo, chave e valor.
              // No parâmetro grupo você informará qual o meio pagamento que receberá as configurações.
              // Para limitar o parcelamento você deve informar o meio de pagamento Cartão de crédito (CREDIT_CARD).
              //Após definir o grupo você deverá definir as configurações nos campos chave e valor.
              // Estes devem ser definidos com a chave MAX_INSTALLMENTS_LIMIT que define a configuração de limite de parcelamento e como valor o número de parcelas que você deseja apresentar ao cliente (de 2 a 18 parcelas).

              .addPaymentMethodConfig(new PaymentMethodConfigBuilder()
                  .withPaymentMethod(new PaymentMethodBuilder()
                      .withGroup(PaymentMethodGroup.CREDIT_CARD)
                  )
                  .withConfig(new ConfigBuilder()
                      .withKey(ConfigKey.MAX_INSTALLMENTS_LIMIT)
                      .withValue(new BigDecimal(10))
                  )
              )
              .addPaymentMethodConfig(new PaymentMethodConfigBuilder()
                  .withPaymentMethod(new PaymentMethodBuilder()
                      .withGroup(PaymentMethodGroup.CREDIT_CARD)
                  )
                  .withConfig(new ConfigBuilder()
                      .withKey(ConfigKey.MAX_INSTALLMENTS_NO_INTEREST)
                      .withValue(new BigDecimal(5))
                  )
              )
      );
      System.out.println(registeredCheckout.getRedirectURL());

    }catch (Exception e){
      e.printStackTrace();
    }
  }
}
