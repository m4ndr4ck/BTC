<%@include file="_header.jsp"%>

<div class="jumbotron" style="padding-left: 0px; padding-right: 0px;">

    <div align="center" style="margin: auto;">
        <h2>Meios de Pagamento</h2>
    </div>
    <div class="container" id="meiosdepagamento" style="padding-top: 20px;">
        <div class="row" id="pagamentosrow">
            <c:if test="${valorPagamento <= 250}">
                <div class="col-sm-6">
                    <p><img class="pagamentosimg" src="/resources/img/pagseguro.png"></p>
                    <p style="font-size: 16px;"><a href="/painel/checkout/pagseguro">PagSeguro</a></p>
                </div>

                <div class="col-sm-3" style="display:none">
                    <p><img class="pagamentosimg" src="/resources/img/boleto2.png"></p>
                    <p style="font-size: 16px;"><a href="/painel/checkout/boleto">Boleto</a></p>
                </div>

                <div class="col-sm-6">
                    <p><img class="pagamentosimg" src="/resources/img/paypal.png"></p>
                    <p style="font-size: 16px;"><a href="/painel/checkout/paypal">PayPal</a></p>
                </div>

                <div class="col-sm-3"  style="display:none">
                    <p><img class="pagamentosimg" src="/resources/img/transferencia.png"></p>
                    <p style="font-size: 16px;"><a href="/painel/checkout/transferencia">Transferência e DOC/TED</a></p>
                </div>
            </c:if>


            <c:if test="${valorPagamento > 1000}">
            <div class="col-sm-3" style="width: 100%;">
                <p><img class="pagamentosimg" src="/resources/img/transferencia.png"></p>
                <p style="font-size: 16px;"><a href="/painel/checkout/transferencia">Transferência e DOC/TED</a></p>
            </div>
            </c:if>

        </div>
    </div>
    <div align="center" style="padding-top: 28px;"><span id="siteseal"><script async="" type="text/javascript" src="https://seal.godaddy.com/getSeal?sealID=OEOGMfXuFcBTbNyMPxxulnmiCt9pP8xlL3KWfU8geYMe9SQ8BYCkq5RfIi1C"></script></span></div>
</div>

<%@include file="_footer.jsp"%>

</div> <!-- /container -->


<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->



</body>
</html>