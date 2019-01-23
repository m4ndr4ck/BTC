<%@include file="_header-home.jsp"%>

<!-- Begin page content -->
<div class="container" class="text-black"
     style="max-width: 730px !important; font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif !important;">

    <div class="jumbotron" style="padding-left: 0px; padding-right: 0px;">
        <div align="center" style="margin: auto;">
            <h2>Simulador</h2>
        </div>
        <div align="center" style="margin: auto;width: 70%;font-size: 15px;text-align: justify;">
            Saiba exatamente a quantidade de bitcoin que você vai receber na sua carteira após solicitar o saque. Não escondemos taxas e mostramos o quanto vale de verdade o bitcoin comprado na BTC Moedas.
        </div>
        <div class="row" id="inputReal"
             style="margin: auto; text-align: center">
            <div class="col-md-6" style="display: none">
                <div class="row form-group">

                    <div class="col-md-7" style="display: none">
                        <select class="form-control" id="base">
                            <option selected>R$ 20,00</option>
                            <option>R$ 30,00</option>
                            <option>R$ 40,00</option>
                            <option>R$ 50,00</option>
                        </select>
                    </div>
                </div>

            </div>
            <div class="row form-group"
                 style="margin: 0 auto; padding-top: 15px;color:#fff;font-size:14px">
                <form>
                    <h3 style="margin-top: 20px">Você paga</h3>
                    <div id="no-filtering-place" data-filter="false" style="color: #464a4c;"></div>

                    <h3 style="margin-top: 20px">Chega na sua carteira</h3>
                    <input id="btc" name="bitcoins" type="text"
                           class="form-control input" /> <br>

                    <div style="padding-top: 15px;padding-bottom: 20px;text-align: justify;font-size: 14px;">
                        Cotação: <b>1 BTC | ${cotacaoCOMPRA}</b>
                        <br>Taxa de transação: <b>${taxaBTCcomvirgula} BTC</b>
                        <br>Taxa (fee) da blockchain: <b>${feeLocalBitcoinscomvirgula} BTC</b>
                    </div>

                </form>
            </div>

        </div>
    </div>
    <div class="spacer"></div>

</div>
<%@include file="_footer-home.jsp"%>
<script>
    $(document).ready(
        function() {
            $('pre code').each(function(i, block) {
                hljs.highlightBlock(block);
            });
            $.each([ 'basic', 'default', 'slide', 'fade', 'appendTo',
                'no-filtering', 'html' ], function(i, id) {
                var $place = $('#' + id + '-place');
                var $select = $('#base').clone().attr("id", "real").attr(
                    "name", "valorPagamento").attr("data-thousands",
                    ".").attr("data-decimal", ",").attr("data-prefix",
                    "R$ ").removeAttr('base').appendTo($place);
                if (id != 'basic')
                    $select.find('option:selected').removeAttr('selected');
                $select.editableSelect($place.data());
            });

            $('#select').editableSelect().on(
                'select.editable-select',
                function(e, li) {
                    $('#last-selected').html(
                        li.val() + '. ' + li.text());
                });

            $('#real').on(
                'keydown',
                function() {
                    setTimeout(function() {
                        var $real = $("#real").val().replace("R$", "")
                            .replace(",", ".");
                        var $valorbtc = $real / ${cotacaoCompraSemBRL};
                        $('#btc').val(
                            $valorbtc.toLocaleString('pt-BR', {
                                maximumSignificantDigits : 6
                            }));
                    }, 0);
                });

            /**$('#btc').on('keydown', function() {
                setTimeout(function() {
                    var $btc = $("#btc").val().replace(",", ".") * ${cotacaoCompraSemBRL};
                    var $brl = "R$ ";
                    $('#real').val($brl + $btc.toLocaleString('pt-BR', {
                        maximumFractionDigits : 2,
                        minimumFractionDigits : 2
                    }));
                }, 0);
            });*/

            $("#real").focusout(function() {
                setTimeout(function() {
                    //var $real = $("#real").val().replace("R$", "")
                    //		.replace(",", ".");
                    //var $valorbtc = $real / ${cotacaoCompraSemBRL};
                    var $real = parseInt($("#real").val().replace("R$", "").replace('.','').replace(' ',''));
                    var $valorbtc = $real*100000000/parseInt(${cotacaoCompraSemBRL}*100000000);
                    $valorbtc = $valorbtc - ${taxaBTC} - ${feeLocalBitcoins};
                    $('#btc').val($valorbtc.toLocaleString('pt-BR', {
                        maximumSignificantDigits : 6
                    }));
                }, 0);
            });

            $("#real").maskMoney();

        });
</script>
</body>
</html>