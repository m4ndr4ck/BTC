<%@include file="_header.jsp"%>
<script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
      
<div class="jumbotron" style="padding-left: 0px; padding-right: 0px;">

<!-- Start CONTAINER -->
<div class="container" id="tabelaConfirma">

  <div id="loading" style="display:none">
    <img src="/resources/img/ajax-loader.gif">
    <br><br>
    <b>Estamos processando seu saque...</b>
  </div>

  <div id="content">
  <table class="table table-bordered">
    <thead>
      <tr>
        <th style="text-align: center;background: #dbdff3;">Confirme seu saque</th>

      </tr>
    </thead>
    <tbody>
      <tr class="active">
        <td class="tdresgate">Data e Hora: ${dataHora}</td>
      </tr>     
      <tr class="active">
        <td class="tdresgate">Valor do saque: ${bitcoins} BTC</td>
      </tr>
      <tr class="active">
        <td class="tdresgate">Taxa (fee) da blockchain: ${feeBTC} BTC</td>
      </tr>
      <tr class="active">
        <td class="tdresgate">Vai chegar na sua carteira: <b>${totalresgatado} BTC</b></td>
      </tr>		
      <tr class="active">
        <td class="tdresgate">Endereço bitcoin: <b>${enderecobitcoin}</b></td>
      </tr>
     </tbody>
  </table>
  <span style="font-size: 13px;"><b>## ATENÇÃO ##</b><br>Transação irreversível. Certifique-se de que o endereço da sua carteira bitcoin está correto.</span>
 
<br>
<br>
<form:form method="post" id="resgate" modelAttribute="Resgate" action="${contextPath}/painel/resgatar/confirmar-resgate-bitcoin">
<button type="submit" class="btn btn-primary" style="padding: 8px 23px !important;font-size: 17px !important;">Finalizar saque</button>
</form:form>
  </div>
</div>        
<!-- END CONTAINER -->

</div>
      
<%@include file="_footer.jsp"%>

</div> <!-- /container -->


<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->

<script>
    $(document).ready(function(){
        $('form#resgate').submit(function(){
            $('div#content').hide();
            $('div#loading').show();
        });
    });
</script>


</body>
</html>