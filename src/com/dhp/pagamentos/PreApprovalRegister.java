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
import java.util.Date;

import javax.xml.bind.DatatypeConverter;

import br.com.uol.pagseguro.api.PagSeguro;
import br.com.uol.pagseguro.api.PagSeguroEnv;
import br.com.uol.pagseguro.api.common.domain.ShippingType;
import br.com.uol.pagseguro.api.common.domain.builder.AddressBuilder;
import br.com.uol.pagseguro.api.common.domain.builder.DateRangeBuilder;
import br.com.uol.pagseguro.api.common.domain.builder.PhoneBuilder;
import br.com.uol.pagseguro.api.common.domain.builder.PreApprovalBuilder;
import br.com.uol.pagseguro.api.common.domain.builder.SenderBuilder;
import br.com.uol.pagseguro.api.common.domain.builder.ShippingBuilder;
import br.com.uol.pagseguro.api.common.domain.enums.Currency;
import br.com.uol.pagseguro.api.common.domain.enums.State;
import br.com.uol.pagseguro.api.credential.Credential;
import br.com.uol.pagseguro.api.http.JSEHttpClient;
import br.com.uol.pagseguro.api.preapproval.PreApprovalRegistrationBuilder;
import br.com.uol.pagseguro.api.preapproval.RegisteredPreApproval;
import br.com.uol.pagseguro.api.utils.logging.SimpleLoggerFactory;

/**
 * @author PagSeguro Internet Ltda.
 */
public class PreApprovalRegister {

  public static void main(String[] args){

    String sellerEmail = "";
    String sellerToken = "";

    try{

      final PagSeguro pagSeguro = PagSeguro
          .instance(new SimpleLoggerFactory(), new JSEHttpClient(),
              Credential.sellerCredential(sellerEmail, sellerToken), PagSeguroEnv.PRODUCTION);

      
      //Assinatura
      RegisteredPreApproval registeredPreApproval = pagSeguro.preApprovals().register(
          new PreApprovalRegistrationBuilder()
              .withCurrency(Currency.BRL)
              .withExtraAmount(BigDecimal.ONE)
              .withReference("XXXXXX")
              .withSender(new SenderBuilder()//
                  .withEmail("comprador@uol.com.br")//
                  .withName("Jose Comprador")
                  .withCPF("99999999999")) //
              .withPreApproval(new PreApprovalBuilder()
                  .withCharge("manual")
                  .withName("Investimento mensal de bitcoins - 0.02 BTC")
                  .withDetails("A cada 30 dias será debitado o valor de R$10")
                  .withAmountPerPayment(BigDecimal.TEN)
                  .withMaxTotalAmount(new BigDecimal(200))
                  .withMaxAmountPerPeriod(BigDecimal.TEN)
                  .withMaxPaymentsPerPeriod(1)
                  .withPeriod("monthly")
              )
              .withRedirectURL("http://btcmoedas.com.br/retorno")
              .withNotificationURL("http://btcmoedas.com.br/retorno")
      );
      System.out.println(registeredPreApproval.getPreApprovalCode());
      System.out.println(pagSeguro.getAuthHttpClient().getCorrelationId());
      System.out.println(registeredPreApproval.getRedirectURL());
    }catch (Exception e){
      e.printStackTrace();
    }
  }
}
