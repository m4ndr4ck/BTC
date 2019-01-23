<%@include file="_header.jsp"%>

<script type="text/javascript" src="https://stc.pagseguro.uol.com.br/pagseguro/api/v2/checkout/pagseguro.directpayment.js"></script>
<script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jquery.mask/1.14.0/jquery.mask.js"></script>
<!--<script type="text/javascript" src="https://stc.sandbox.pagseguro.uol.com.br/pagseguro/api/v2/checkout/pagseguro.directpayment.js"></script>-->


<script type="text/javascript" >

    $(document).ready(function() {
        $("#nome").val("${nome}");
        $("#cpf").val("${cpf}");

        function limpa_formulário_cep() {
            // Limpa valores do formulário de cep.
            $("#logradouro").val("");
            $("#bairro").val("");
            $("#cidade").val("");
            $("#uf").val("");
        }

        //Quando o campo cep perde o foco.
        $("#cep").keyup(function() {
            var s = $(this).val();
            if(s.length == 9){
                //Nova variável "cep" somente com dígitos.
                var cep = $(this).val().replace(/\D/g, '');
                cep = $(this).val().replace(/-/g, '');


                //Verifica se campo cep possui valor informado.
                if (cep != "") {

                    //Expressão regular para validar o CEP.
                    var validacep = /^[0-9]{8}$/;

                    //Valida o formato do CEP.
                    if(validacep.test(cep)) {

                        //Preenche os campos com "..." enquanto consulta webservice.
                        $("#logradouro").val("...");
                        $("#bairro").val("...");
                        $("#cidade").val("...");
                        $("#uf").val("...");

                        //Consulta o webservice viacep.com.br/
                        $.getJSON("https://viacep.com.br/ws/"+ cep +"/json/?callback=?", function(dados) {

                            if (!("erro" in dados)) {
                                //Atualiza os campos com os valores da consulta.
                                $("#logradouro").val(dados.logradouro);
                                $("#bairro").val(dados.bairro);
                                $("#cidade").val(dados.localidade);
                                $("#uf").val(dados.uf);
                                $("#blocoendereco").show();
                            } //end if.
                            else {
                                //CEP pesquisado não foi encontrado.
                                limpa_formulário_cep();
                                alert("CEP não encontrado.");
                            }
                        });
                    } //end if.
                    else {
                        //cep é inválido.
                        limpa_formulário_cep();
                        alert("Formato de CEP inválido.");
                    }
                } //end if.
                else {
                    //cep sem valor, limpa formulário.
                    limpa_formulário_cep();
                }
            }
        });
    });

</script>

<script>
    $(document).ready(function () {
        var $CPF = $("#cpf");
        $CPF.mask('000.000.000-00', {reverse: true});

        var $CEP = $("#cep");
        $CEP.mask('00000-000');

        var $nascimento = $("#nascimento");
        $nascimento.mask('00/00/0000');

        var $validade = $("#validade");
        $validade.mask('00/0000');

    });
</script>

<script>
    //Array para armazenar dados do comprador
    var map = {};
    $(document).ready(function () {
        $("form").submit(function( event ) {
            //  console.log( $( this ).serializeArray() );
            event.preventDefault();

            var fields = $( ":input" ).serializeArray();
            jQuery.each( fields, function( i, field ) {
                //console.log( field.value + " " );
                map[field.name] = field.value;
            });
            efetivaPagamento(map)
        });

    });


    function efetivaPagamento(dadosComprador){

        $(document).ready(function () {
            $("#content").hide();
            $("#loading").show();
        });
        // pegando os dados
        var vUrl = "/painel/checkout/transferencia/";
        var vData = {

        };

        $.post(
            vUrl, //Required URL of the page on server
            vData,
            function(response,status)
            {
                // tratando o status de retorno. Sucesso significa que o envio e retorno foi executado com sucesso.
                if(status == "success")
                {
                    console.log(response);

                    if(response == "erro"){
                        $("#loading").hide();
                        $("#erro").show();
                    } else{
                        $("#loading").hide();
                        $("#linkpagamento").attr("href", response)
                        $("#sucesso").show();
                    }
                }
            }
        );
    }
</script>


<div class="jumbotron" style="padding-left: 0px; padding-right: 0px;">

    <div id="content">

        <div align="center" style="margin: auto;padding-bottom: 15px;">
            <h2>Transferência e DOC/TED</h2>
        </div>

        <div style="width: 65%;margin: auto;">
            <form>


                <div id="bradesco">
                    <div style="
    text-align: left;
    font-size: 16px;
    color: #616060;
    padding-bottom: 0px;
    border-bottom: solid 1px #b3b3b3;
">Bradesco</div><br>



                    <div class="row" style="
    text-align: left;
    padding-top: .75rem;
    padding-bottom: .75rem;
    background-color: rgba(86,61,124,.15);
    border: 1px solid rgba(86,61,124,.2);
    margin-right: 0;
    margin-left: 0;
">
                        <div class="col-xs-4" style="
"><b>Agência:</b></div>
                        <div class="col-xs-6">5723</div>
                    </div>
                    <div class="row" style="
    text-align: left;
    padding-top: .75rem;
    padding-bottom: .75rem;
    background-color: rgba(86,61,124,.15);
    border: 1px solid rgba(86,61,124,.2);
    margin-left: 0;
    margin-right: 0;
">
                        <div class="col-xs-4" style="
                             "><b>Conta:</b></div>
                        <div class="col-xs-2">00236772</div>
                    </div>
                    <div class="row" style="
    text-align: left;
    padding-top: .75rem;
    padding-bottom: .75rem;
    background-color: rgba(86,61,124,.15);
    border: 1px solid rgba(86,61,124,.2);
    margin-left: 0;
    margin-right: 0;
">
                        <div class="col-xs-4" style=""><b>CPF:</b></div>
                        <div class="col-xs-8">058.643.889-04</div>
                    </div><div class="row" style="
    text-align: left;
    padding-top: .75rem;
    padding-bottom: .75rem;
    background-color: rgba(86,61,124,.15);
    border: 1px solid rgba(86,61,124,.2);
    margin-left: 0;
    margin-right: 0;
">
                    <div class="col-xs-4" style="
                             "><b>Nome:</b></div>
                    <div class="col-xs-8" style="
">Davi Vieira Selio Junior</div>
                </div>
                </div>
                <br><div id="itau">
                <div style="
    text-align: left;
    font-size: 16px;
    color: #616060;
    padding-bottom: 0px;
    border-bottom: solid 1px #b3b3b3;
">Itaú</div><br>



                <div class="row" style="
    text-align: left;
    padding-top: .75rem;
    padding-bottom: .75rem;
    background-color: rgba(86,61,124,.15);
    border: 1px solid rgba(86,61,124,.2);
    margin-right: 0;
    margin-left: 0;
">
                    <div class="col-xs-4" style="
"><b>Agência:</b></div>
                    <div class="col-xs-6">3834</div>
                </div>
                <div class="row" style="
    text-align: left;
    padding-top: .75rem;
    padding-bottom: .75rem;
    background-color: rgba(86,61,124,.15);
    border: 1px solid rgba(86,61,124,.2);
    margin-left: 0;
    margin-right: 0;
">
                    <div class="col-xs-4" style="
                             "><b>Conta:</b></div>
                    <div class="col-xs-5">34481-8</div>
                </div>
                <div class="row" style="
    text-align: left;
    padding-top: .75rem;
    padding-bottom: .75rem;
    background-color: rgba(86,61,124,.15);
    border: 1px solid rgba(86,61,124,.2);
    margin-left: 0;
    margin-right: 0;
">
                    <div class="col-xs-4" style=""><b>CPF:</b></div>
                    <div class="col-xs-8">058.643.889-04</div>
                </div><div class="row" style="
    text-align: left;
    padding-top: .75rem;
    padding-bottom: .75rem;
    background-color: rgba(86,61,124,.15);
    border: 1px solid rgba(86,61,124,.2);
    margin-left: 0;
    margin-right: 0;
">
                <div class="col-xs-4" style="
                             "><b>Nome:</b></div>
                <div class="col-xs-8" style="
">Davi Vieira Selio Junior</div>
            </div>
            </div>









                <br><p style="font-size:15px">
                Valor a ser pago: <b>${valorPagamento}</b></p>
                <p style="
    font-size: 15px;
">Após efetuar a transferência/depósito clique no botão abaixo e envie o comprovante da transferência para <b>admin@btcmoedas.com.br</b></p>

                <br>

                <button type="submit" class="btn btn-primary" style="padding: 8px 23px !important;font-size: 17px !important;">Finalizar pagamento</button>


            </form>
            </div>
    </div> <!--Content -->
    <div id="loading" style="display:none">
        <img src="/resources/img/ajax-loader.gif">
        <br><br>
        Estamos registrando seu pagamento...
    </div>

    <div id="sucesso" style="display:none;width: 50%;margin: auto;">
        <img src="/resources/img/sucesso.png">
        <br><br>
        <b>Pagamento registrado com sucesso</b>
        <br><br>
        Se você fez TED/DOC ou depósito, a cotação de compra será atualizada de acordo com a data e hora de compensação do pagamento.
    </div>

    <div id="erro" style="display:none;width: 50%;margin: auto;">
        <img src="/resources/img/erro.png">
        <br><br>
        <b>Houve um erro ao processar seu pagamento</b>
        <br><br>
        Revise seus dados e tente fazer a compra novamente.
    </div>

</div>

<%@include file="_footer.jsp"%>

</div> <!-- /container -->


<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->



</body>
</html>