<%@include file="_header-afiliado.jsp"%>
<div class="header clearfix jumbotron" id="menu" style="background-color: #eee;padding-top: 10px;border-radius: 6px;padding-bottom: 10px;text-align: center;">
    <nav>
        <ul id="nav">
            <li><a href="/afiliado/painel/minha-conta">Minha conta</a></li>
            <li><a href="/afiliado/painel/resgatar/carteira-bitcoin">Sacar bitcoin</a></li>
            <!--<li><a href="#">Transferir bitcoin</a></li>-->
        </ul>
    </nav>
</div>


<div class="jumbotron" style="padding-left: 0px; padding-right: 0px;">
    <p class="lead" style="margin-bottom: 0px;"><span style="font-size: 1.0em!important" id="saldo-btc">Seu link de afiliado
        <br>
        <a href="${linkAfiliado}" target="_blank"><b>${linkAfiliado}</b></a></span>
        <br>
        <span style="font-size: 15px;">Divulgue esse link para receber bitcoin</span>
        <br>
        <br>
        <span id="saldo-btc">Comissão por venda é <b>0,0001 BTC</b> <!--| Seu saldo é <b> --></span>
    </p>
</div>


<%@include file="_footer-afiliado.jsp"%>

</div> <!-- /container -->


<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
<script src="https://getbootstrap.com/assets/js/ie10-viewport-bug-workaround.js"></script>


</body>
</html>