<%@include file="_header.jsp"%> 
<c:set var="verificado" value="${verificado}" />
<div class="header clearfix jumbotron" id="menu" style="background-color: #eee;padding-top: 10px;border-radius: 6px;padding-bottom: 10px;text-align: center;">
<nav>
<ul id="nav">                  
<li><a href="${contextPath}/painel/minha-conta">Minha conta</a></li>
<!--<li><a href="#">Transferir bitcoin</a></li>-->
</ul>
</nav>        
</div>
      

<div class="jumbotron" style="padding-left: 0px; padding-right: 0px;">        
      <p class="lead" style="margin-bottom: 0px;"><span id="saldo-btc">Você tem <b>${saldoBTC} BTC</b> <!--| Seu saldo é <b>${saldoBRL} --></b></span></p>
</div>
<div class="header clearfix jumbotron" id="menu" style="background-color: #eee;padding-top: 10px;border-radius: 6px;padding-bottom: 10px;text-align: center;">
<nav>
          <ul id="nav">
            
            <li><a href="${contextPath}/painel/comprar-bitcoin">Comprar bitcoin</a></li>
            <li><a href="${contextPath}/painel/resgatar/carteira-bitcoin">Sacar bitcoin</a></li>
            <!--<li><a href="#">Transferir bitcoin</a></li>-->
          </ul>
</nav>
        
      </div>
<div class="jumbotron" style="padding-left: 0px; padding-right: 0px;">        
        <p class="lead">Seu endereço bitcoin</p>
<span id="endereco-btc"><b>${enderecoBTC}</b></span>
</div>
      
<%@include file="_footer.jsp"%> 

    </div> <!-- /container -->


    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="https://getbootstrap.com/assets/js/ie10-viewport-bug-workaround.js"></script>
  

</body>
</html>