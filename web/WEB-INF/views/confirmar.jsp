<%@include file="_header.jsp"%>    
      
<div class="jumbotron" style="padding-left: 0px; padding-right: 0px;">

    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="/painel">Home</a></li>
        <li class="breadcrumb-item active">Comprar bitcoin</li>
    </ol>

<!-- Start CONTAINER -->
<div class="container" id="tabelaConfirma">
  <table class="table table-bordered">
    <thead>
      <tr>
        <th style="text-align: center;background: #dbdff3;">Confirme sua compra</th>

      </tr>
    </thead>
    <tbody>     
      <tr class="active">
        <td class="tdresgate">Cotação: ${cotacaoCOMPRA}</td>
      </tr>
      <tr class="active">
        <td class="tdresgate">Data e Hora: ${dataHora}</td>
      </tr>
      <!--<tr class="active">
      	<td class="tdresgate">
      				<c:choose>
					    <c:when test="${tipoPagamento == 1}">
					        Investimento mensal
					    </c:when>
					    <c:otherwise>
					        Compra avulsa
					    </c:otherwise>
					</c:choose>
        </td>
      </tr>-->
      <tr class="active" style="background-color: #cae2c0;">
          <td class="tdresgate">Taxa de transação: ${taxaBTCcomvirgula} BTC</td>

      </tr>
      <tr class="active" style="background-color: #cae2c0;">
          <td class="tdresgate">Bitcoin: ${bitcoins} BTC</td>
      </tr>
      <tr class="active" style="background-color: #cae2c0;">
        <td class="tdresgate">Total a pagar: <b>${valorPagamento}</b></td>
   	
      </tr>
      <tr class="success">
          <td class="tdresgate">Você vai receber: <b>${bitcoins2} BTC</b></td>
      </tr>
      </tbody>
  </table>
  <c:if test = "${tipoPagamento == 1}">
  <span style="font-size: 13px;">Todo mês será debitado o valor de <b>${valorPagamento}</b> do seu cartão de crédito e convertido em bitcoin com base na cotação do dia.<br></span>
  </c:if>
<br>
<form:form method="post" modelAttribute="Pagamento" action="${contextPath}/painel/comprar-bitcoin/confirmar">
<button type="submit" class="btn btn-primary" style="padding: 8px 23px !important;font-size: 17px !important;">Efetuar pagamento</button>
</form:form>
</div>        
<!-- END CONTAINER -->

</div>

<%@include file="_footer.jsp"%>

</div> <!-- /container -->


<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
<script src="http://getbootstrap.com/assets/js/ie10-viewport-bug-workaround.js"></script>
		<script src="https://code.jquery.com/jquery-1.12.4.min.js"></script>
		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.0.3/js/bootstrap.min.js"></script>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/9.6.0/highlight.min.js"></script>
		<script src="https://rawgithub.com/indrimuska/jquery-editable-select/master/dist/jquery-editable-select.min.js"></script>
		<script>
			$(document).ready(function() {
				$('pre code').each(function(i, block) { hljs.highlightBlock(block); });
				$.each(['basic', 'default', 'slide', 'fade', 'appendTo', 'no-filtering', 'html'], function (i, id) {
					var $place  = $('#' + id + '-place');
					var $select = $('#base').clone().attr("id","real").removeAttr('base').appendTo($place);
					if (id != 'basic') $select.find('option:selected').removeAttr('selected');
					$select.editableSelect($place.data());
				});

				$('#select').editableSelect().on('select.editable-select', function (e, li) {
					$('#last-selected').html(li.val() + '. ' + li.text());
				});

			 	$('#real').on('keydown', function () {
					setTimeout(function () {
					var $real = $("#real").val().replace("R$", "").replace(",", ".");
					var $valorbtc = $real/9850;
					$('#btc').val($valorbtc.toLocaleString('pt-BR', { maximumSignificantDigits: 6 }));
					}, 0);
				});
				
				$('#btc').on('keydown', function () {
					setTimeout(function () {
					var $btc = $("#btc").val().replace(",", ".")*9850;
					var $brl = "R$ ";
					$('#real').val($brl+$btc.toLocaleString('pt-BR', { maximumFractionDigits: 2, minimumFractionDigits: 2 }));
					}, 0);
				});
				
    				$("#real").focusout(function(){
       			 var $real = $("#real").val().replace("R$", "").replace(",", ".");
				 var $valorbtc = $real/8870;
				 $('#btc').val($valorbtc.toLocaleString('pt-BR', { maximumSignificantDigits: 6 }));
    				});

			});

		</script>
  

</body>
</html>