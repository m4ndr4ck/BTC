
function brandCard() {
    PagSeguroDirectPayment.getBrand({
        cardBin: $("#numerocartao").val(),
        success: function(response) {
            return response.brand.name;
        },
        error: function(response) {
            $("#numerocartao").css('border', '2px solid red');
            $("#numerocartao").focus();
        },
        complete: function(response) {
        }
    });
}

function pagarCartao(senderHash) {
    var bandname = brandCard();

    var data =$('#validade').text();
    var arr = data.split('/');
    var mesExpiracao = arr[0];
    var anoExpiracao = arr[1];

    PagSeguroDirectPayment.setSessionId("${sessaoid}");
    PagSeguroDirectPayment.createCardToken({
        cardNumber: $("#numerocartao").val(),
        brand: bandname,
        cvv: $("#cvv").val(),
        expirationMonth: mesExpiracao,
        expirationYear: anoExpiracao,
        success: function (response) {
            alert("Deu certo");
            alert(response.card.token);
            $("#creditCardToken").val(response.card.token);
        },
        error: function (response) {
            alert("Deu erro");
            if (response.error) {
                console.log("4" + response);
                $.each(response.errors, function (index, value) {
                    console.log(value);
                });
            }
        },
        complete: function (response) {
        }
    });
    /**$.ajax({
        type: 'POST',
        url: 'pagamentoCartao.php',
        cache: false,
        data: {
        id: 11111,
        email: $("#senderEmail").val(),
        nome: $("#senderName").val(),
        cpf: $("#senderCPF").val(),
        ddd: $("#senderAreaCode").val(),
        telefone: $("#senderPhone").val(),
        cep: $("#shippingAddressPostalCode").val(),
        endereco: $("#shippingAddressStreet").val(),
        numero: $("#shippingAddressNumber").val(),
        complemento: $("#shippingAddressComplement").val(),
        bairro: $("#shippingAddressDistrict").val(),
        cidade: $("#shippingAddressCity").val(),
        estado: $("#shippingAddressState").val(),
        pais: "BRA",
        senderHash: senderHash,
        enderecoPagamento: $("#billingAddressStreet").val(),
        numeroPagamento: $("#billingAddressNumber").val(),
        complementoPagamento: $("#billingAddressComplement").val(),
        bairroPagamento: $("#billingAddressDistrict").val(),
        cepPagamento: $("#billingAddressPostalCode").val(),
        cidadePagamento: $("#billingAddressCity").val(),
        estadoPagamento: $("#billingAddressState").val(),
        cardToken: $("#creditCardToken").val(),
        cardNome: $("#creditCardHolderName").val(),
        cardCPF: $("#creditCardHolderCPF").val(),
        cardNasc: $("#creditCardHolderBirthDate").val(),
        cardFoneArea: $("#creditCardHolderAreaCode").val(),
        cardFoneNum: $("#creditCardHolderPhone").val(),
        numParcelas: $("#installmentQuantity").val(),
        valorParcelas: $("#installmentValue").val(),
},
    success: function(data) {
        //console.log(data);
        if (data.error) {
            if (data.error.code == "53037") {
                $("#creditCardPaymentButton").click();
            } else {
                $("#modal-title").html("<font color='red'>Erro</font>");
                $("#modal-body").html("");
                $.each(data.error, function (index, value) {
                    if (value.code) {
                        tratarError(value.code);
                    } else {
                        tratarError(data.error.code)
                    }
                })
                //console.log("2 " + data);
            }
        } else {
            $.ajax({
                type: 'POST',
                url: 'getStatus.php',
                cache: false,
                data: {
                    id: data.code,
                },
                success: function(status) {
                    if (status == "7") {
                        //alert(data);
                        $("#modal-title").html("<font color='red'>Erro</font>");
                        $("#modal-body").html("Erro ao processar o seu pagamento.<br/> Não se preocupe pois esse valor <b>não será debitado de sua conta ou não constará em sua fatura</b><br /><br />Verifique se você possui limite suficiente para efetuar a transação e/ou tente um cartão diferente");
                    } else {
                        window.location = "http://download.infoenem.com.br/pagamento-efetuado/";
                        setTimeout(function () {
                            $("#modal-body").html("");
                            $("#modal-title").html("<font color='green'>Sucesso!</font>")
                            $("#modal-body").html("Caso você não seja redirecionado para a nossa página de instruções, clique no botão abaixo.<br /><br /><a href='http://download.infoenem.com.br/pagamento-efetuado/'><center><button class='btn-success btn-block btn-lg'>Ir para a página de instruções</button></center></a>");
                        }, 1500);
                    }
                }
            });
            //console.log("1 " + data);
        }
    }
});*/
}
