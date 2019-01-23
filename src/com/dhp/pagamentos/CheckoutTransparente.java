package com.dhp.pagamentos;

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
        import br.com.uol.pagseguro.api.PagSeguro;
        import java.math.BigDecimal;
        import java.text.SimpleDateFormat;
        import java.util.Map;

        import br.com.uol.pagseguro.api.PagSeguroEnv;
        import br.com.uol.pagseguro.api.common.domain.BankName;
        import br.com.uol.pagseguro.api.common.domain.ShippingType;
        import br.com.uol.pagseguro.api.common.domain.builder.*;
        import br.com.uol.pagseguro.api.common.domain.enums.Currency;
        import br.com.uol.pagseguro.api.common.domain.enums.DocumentType;
        import br.com.uol.pagseguro.api.common.domain.enums.State;
        import br.com.uol.pagseguro.api.credential.Credential;
        import br.com.uol.pagseguro.api.http.JSEHttpClient;
        import br.com.uol.pagseguro.api.session.CreatedSession;
        import br.com.uol.pagseguro.api.transaction.register.DirectPaymentRegistrationBuilder;
        import br.com.uol.pagseguro.api.transaction.search.TransactionDetail;
        import br.com.uol.pagseguro.api.utils.logging.SimpleLoggerFactory;

/**
 * @author PagSeguro Internet Ltda.
 */
public class CheckoutTransparente {


    public static String pagamentoCartao(Map<String, String> dadosComprador){
        try{

            String sellerEmail = "";
            //String sellerToken = ""; //DEV
            String sellerToken = ""; //PROD


            final PagSeguro pagSeguro = PagSeguro
                    .instance(new SimpleLoggerFactory(), new JSEHttpClient(),
                            Credential.sellerCredential(sellerEmail, sellerToken), PagSeguroEnv.PRODUCTION);

            CreatedSession createdSessionApplication = pagSeguro.sessions().create();
            System.out.println(createdSessionApplication.getId());

            // Checkout transparente (pagamento direto) com cartao de credito
            TransactionDetail creditCardTransaction =
                    pagSeguro.transactions().register(new DirectPaymentRegistrationBuilder()
                            .withPaymentMode("default")
                            .withCurrency(Currency.BRL)
                            .addItem(new PaymentItemBuilder()//
                                    .withId("0001")//
                                    .withDescription("BTC "+dadosComprador.get("bitcoin")) //
                                    .withAmount(new BigDecimal(dadosComprador.get("preco")))//
                                    .withQuantity(1)
                                    .withWeight(1000))
                            .withNotificationURL("https://btcmoedas.com.br/retorno")
                            .withReference(dadosComprador.get("referencia"))
                            .withSender(new SenderBuilder()//
                                    .withHash(dadosComprador.get("senderhash"))
                                    //.withEmail("comprador@sandbox.pagseguro.com.br")//
                                    .withEmail(dadosComprador.get("email"))//
                                    .withName(dadosComprador.get("nome"))
                                    .withCPF(dadosComprador.get("cpf"))
                                    .withPhone(new PhoneBuilder()//
                                            .withAreaCode(dadosComprador.get("celDDD")) //
                                            .withNumber(dadosComprador.get("celNum")))) //
                            .withShipping(new ShippingBuilder()//
                                    .withType(ShippingType.Type.USER_CHOISE) //
                                    .withCost(BigDecimal.ZERO)//
                                    .withAddress(new AddressBuilder() //
                                            .withPostalCode(dadosComprador.get("cep"))
                                            .withCountry("BRA")
                                            .withState(dadosComprador.get("uf"))//
                                            .withCity(dadosComprador.get("cidade"))
                                            .withComplement(dadosComprador.get("complemento"))
                                            .withDistrict(dadosComprador.get("bairro"))
                                            .withNumber(dadosComprador.get("numero"))
                                            .withStreet(dadosComprador.get("logradouro")))
                            )
                    ).withCreditCard(new CreditCardBuilder()
                            .withBillingAddress(new AddressBuilder() //
                                    .withPostalCode(dadosComprador.get("cep"))
                                    .withCountry("BRA")
                                    .withState(dadosComprador.get("uf"))//
                                    .withCity(dadosComprador.get("cidade"))
                                    .withComplement(dadosComprador.get("complemento"))
                                    .withDistrict(dadosComprador.get("bairro"))
                                    .withNumber(dadosComprador.get("numero"))
                                    .withStreet(dadosComprador.get("logradouro"))
                            )
                            .withInstallment(new InstallmentBuilder()
                                    .withQuantity(1)
                                    .withValue(new BigDecimal(dadosComprador.get("preco")))
                            )
                            .withHolder(new HolderBuilder()
                                    .addDocument(new DocumentBuilder()
                                            .withType(DocumentType.CPF)
                                            .withValue(dadosComprador.get("cpf"))
                                    )
                                    .withName(dadosComprador.get("nome"))
                                    .withBithDate(new SimpleDateFormat("dd/MM/yyyy").parse(dadosComprador.get("nascimento")))
                                    .withPhone(new PhoneBuilder()
                                            .withAreaCode(dadosComprador.get("celDDD"))
                                            .withNumber(dadosComprador.get("celNum"))
                                    )
                            )
                            .withToken(dadosComprador.get("tokencartao"))
                    );
            System.out.println(creditCardTransaction);
            return "ok";

        }catch (Exception e){
            e.printStackTrace();
        }
        return "erro";
    }

    public static String pagamentoDebito(Map<String, String> dadosComprador) {
        String sellerEmail = "admin@btcmoedas.com.br";
        //String sellerToken = "B4ECA37335194B12915B474F67AAE4F8"; //DEV
        String sellerToken = "68CD735382894D66849D40F697019912"; //PROD

        br.com.uol.pagseguro.api.common.domain.BankName.Name banco =null;

        final PagSeguro pagSeguro = PagSeguro
                .instance(new SimpleLoggerFactory(), new JSEHttpClient(),
                        Credential.sellerCredential(sellerEmail, sellerToken), PagSeguroEnv.PRODUCTION);

        try{
            if(dadosComprador.get("banco").equals("bradesco")) banco = BankName.Name.BRADESCO;

            if(dadosComprador.get("banco").equals("itau")) banco = BankName.Name.ITAU;

            if(dadosComprador.get("banco").equals("bb")) banco = BankName.Name.BANCO_DO_BRASIL;

            //Checkout transparente (pagamento direto) com debito online
            TransactionDetail onlineDebitTransaction =
                    pagSeguro.transactions().register(new DirectPaymentRegistrationBuilder()
                            .withPaymentMode("default")
                            .withCurrency(Currency.BRL)
                            .addItem(new PaymentItemBuilder()//
                                    .withId("0001")//
                                    .withDescription("BTC " + dadosComprador.get("bitcoin")) //
                                    .withAmount(new BigDecimal(dadosComprador.get("preco")))//
                                    .withQuantity(1)
                                    .withWeight(1000))
                            .withNotificationURL("https://btcmoedas.com.br/retorno")
                            .withReference(dadosComprador.get("referencia"))
                            .withSender(new SenderBuilder()//
                                    .withHash(dadosComprador.get("senderhash"))
                                    //.withEmail("comprador@sandbox.pagseguro.com.br")//
                                    .withEmail(dadosComprador.get("email"))//
                                    .withName(dadosComprador.get("nome"))
                                    .withCPF(dadosComprador.get("cpf"))
                                    .withPhone(new PhoneBuilder()//
                                            .withAreaCode(dadosComprador.get("celDDD")) //
                                            .withNumber(dadosComprador.get("celNum")))) //
                            .withShipping(new ShippingBuilder()//
                                    .withType(ShippingType.Type.USER_CHOISE) //
                                    .withAddress(new AddressBuilder() //
                                            .withPostalCode(dadosComprador.get("cep"))
                                            .withCountry("BRA")
                                            .withState(dadosComprador.get("uf"))//
                                            .withCity(dadosComprador.get("cidade"))
                                            .withComplement(dadosComprador.get("complemento"))
                                            .withDistrict(dadosComprador.get("bairro"))
                                            .withNumber(dadosComprador.get("numero"))
                                            .withStreet(dadosComprador.get("logradouro")))
                            )
                    ).withOnlineDebit(new BankBuilder()
                            .withName(banco)
                    );
            System.out.println(onlineDebitTransaction.getPaymentLink());
            return onlineDebitTransaction.getPaymentLink();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "erro";
    }

    public static String pagamentoBoleto(Map<String, String> dadosComprador) {
        String sellerEmail = "admin@btcmoedas.com.br";
        //String sellerToken = "B4ECA37335194B12915B474F67AAE4F8"; //DEV
        String sellerToken = "68CD735382894D66849D40F697019912"; //PROD

        final PagSeguro pagSeguro = PagSeguro
                .instance(new SimpleLoggerFactory(), new JSEHttpClient(),
                        Credential.sellerCredential(sellerEmail, sellerToken), PagSeguroEnv.PRODUCTION);

        try{

            //Checkout transparente (pagamento direto) com debito online
            TransactionDetail bankSlipTransaction =
                    pagSeguro.transactions().register(new DirectPaymentRegistrationBuilder()
                            .withPaymentMode("default")
                            .withCurrency(Currency.BRL)
                            .addItem(new PaymentItemBuilder()//
                                    .withId("0001")//
                                    .withDescription("BTC por Boleto") //
                                    .withAmount(new BigDecimal(dadosComprador.get("preco")))//
                                    .withQuantity(1)
                                    .withWeight(1000))
                            .withNotificationURL("https://btcmoedas.com.br/retorno")
                            .withReference(dadosComprador.get("referencia"))
                            .withSender(new SenderBuilder()//
                                    .withHash(dadosComprador.get("senderhash"))
                                    //.withEmail("comprador@sandbox.pagseguro.com.br")//
                                    .withEmail(dadosComprador.get("email"))//
                                    .withName(dadosComprador.get("nome"))
                                    .withCPF(dadosComprador.get("cpf"))
                                    .withPhone(new PhoneBuilder()//
                                            .withAreaCode(dadosComprador.get("celDDD")) //
                                            .withNumber(dadosComprador.get("celNum")))) //
                            .withShipping(new ShippingBuilder()//
                                    .withType(ShippingType.Type.USER_CHOISE) //
                                    .withAddress(new AddressBuilder() //
                                            .withPostalCode(dadosComprador.get("cep"))
                                            .withCountry("BRA")
                                            .withState(dadosComprador.get("uf"))//
                                            .withCity(dadosComprador.get("cidade"))
                                            .withComplement(dadosComprador.get("complemento"))
                                            .withDistrict(dadosComprador.get("bairro"))
                                            .withNumber(dadosComprador.get("numero"))
                                            .withStreet(dadosComprador.get("logradouro")))
                            )
                    ).withBankSlip();

            System.out.println(bankSlipTransaction);
            return bankSlipTransaction.getPaymentLink();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "erro";
    }




    public static void main(String[] args) {

            String sellerEmail = "admin@btcmoedas.com.br";
            String sellerToken = "68CD735382894D66849D40F697019912";



                final PagSeguro pagSeguro = PagSeguro
                        .instance(new SimpleLoggerFactory(), new JSEHttpClient(),
                                Credential.sellerCredential(sellerEmail, sellerToken), PagSeguroEnv.PRODUCTION);

                try{
                    // Checkout transparente (pagamento direto) com boleto
                    TransactionDetail bankSlipTransaction =
                            pagSeguro.transactions().register(new DirectPaymentRegistrationBuilder()
                                    .withPaymentMode("default")
                                    .withCurrency(Currency.BRL)
                                    .addItem(new PaymentItemBuilder()//
                                            .withId("9234")//
                                            .withDescription("Produto PagSeguroI") //
                                            .withAmount(new BigDecimal(100.00))//
                                            .withQuantity(1)
                                            .withWeight(1000))
                                    .withNotificationURL("https://btcmoedas.com.br/retorno")
                                    .withReference("LIBJAVA_DIRECT_PAYMENT")
                                    .withSender(new SenderBuilder()//
                                            .withHash("")
                                            .withEmail("comprador@mail.com")//
                                            .withName("Jose Comprador")
                                            .withCPF("21418824941")
                                            .withPhone(new PhoneBuilder()//
                                                    .withAreaCode("41") //
                                                    .withNumber("999737902"))) //
                                    .withShipping(new ShippingBuilder()//
                                            .withType(ShippingType.Type.USER_CHOISE) //
                                            .withAddress(new AddressBuilder() //
                                                    .withPostalCode("80610260")
                                                    .withCountry("BRA")
                                                    .withState(State.PR)//
                                                    .withCity("Cidade Exemplo")
                                                    .withComplement("99o andar")
                                                    .withDistrict("Jardim Internet")
                                                    .withNumber("9999")
                                                    .withStreet("Av. PagSeguro")))
                            ).withBankSlip();
                    System.out.println(bankSlipTransaction);


                }catch (Exception e){
                    e.printStackTrace();
                }


    }
}
