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
        var vUrl = "/painel/checkout/boleto/";
        var vData = {
            nome:dadosComprador["nome"],
            cpf:dadosComprador["cpf"],
            nascimento:dadosComprador["nascimento"],
            cep:dadosComprador["cep"],
            logradouro:dadosComprador["logradouro"],
            numero:dadosComprador["numero"],
            complemento:dadosComprador["complemento"],
            bairro:dadosComprador["bairro"],
            cidade:dadosComprador["cidade"],
            uf:dadosComprador["uf"],
            banco:dadosComprador["banco"],
            senderhash:PagSeguroDirectPayment.getSenderHash()
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
            <h2>Informe os dados de pagamento</h2>
        </div>

        <div style="width: 65%;margin: auto;">
            <form>
                <div style="
    text-align: left;
    font-size: 16px;
    color: #616060;
    padding-bottom: 0px;
    border-bottom: solid 1px #b3b3b3;
">Dados Pessoais</div><br>
                <div class="form-group">
                    <input type="text" class="form-control" id="nome" name="nome" value="" placeholder="Nome completo" required>
                </div>

                <div class="form-group">
                    <div class="row">
                        <div class="col-xs-6">
                            <input type="text" class="form-control" id="cpf" name="cpf" placeholder="CPF" required>
                        </div>

                        <div class="col-xs-6">
                            <input type="text" class="form-control" id="nascimento" name="nascimento" placeholder="Data de Nascimento" required>
                        </div>
                    </div>
                </div>
                <div style="
    text-align: left;
    font-size: 16px;
    color: #616060;
    padding-bottom: 0px;
    border-bottom: solid 1px #b3b3b3;
">Endereço</div>
                <br>

                <div class="form-group">
                    <input type="text" class="form-control" id="cep" name="cep" value="" placeholder="Informe seu CEP" required>
                </div>
                <div id="blocoendereco" style="display:none">
                    <div class="form-group">
                        <input type="text" class="form-control" id="logradouro" name="logradouro" value="" placeholder="Logradouro" required>
                    </div>
                    <div class="form-group">
                        <div class="row">
                            <div class="col-xs-6">
                                <input type="text" class="form-control" id="numero" name="numero" placeholder="Número" required>

                            </div>

                            <div class="col-xs-6">
                                <input type="text" class="form-control" id="complemento" name="complemento" placeholder="Complemento" required>
                                <input type="hidden" class="form-control" id="bairro" name="bairro" placeholder="Bairro" required>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="row">
                            <div class="col-xs-6">
                                <input type="text" class="form-control" id="cidade" name="cidade" placeholder="Cidade" id="cidade" required>

                            </div>

                            <div class="col-xs-6">
                                <input type="text" class="form-control" id="uf" name="uf" placeholder="Estado" required>
                            </div>
                        </div>
                    </div>
                </div>

                <br>

                <button type="submit" class="btn btn-primary" style="padding: 8px 23px !important;font-size: 17px !important;">Gerar boleto</button>


            </form>
            <span style="color: #9e6060;">Ao comprar via boleto a cotação de compra utilizada será a da data/hora de compensação do boleto, que pode levar de 1 até 3 dias úteis.</span>
        </div>
    </div> <!--Content -->
    <div id="loading" style="display:none">
        <img src="/resources/img/ajax-loader.gif">
        <br><br>
        Estamos gerando seu boleto...
    </div>

    <div id="sucesso" style="display:none;width: 50%;margin: auto;">
        <br><br>
        <b>Finalize o pagamento</b>
        <br><br>
        Para finalizar o pagamento você precisa pagar o boleto no link abaixo:
        <br><br>
        <a id="linkpagamento" target="_blank">Boleto</a>
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