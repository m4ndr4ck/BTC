<%@include file="_header.jsp"%> 

<script src="https://code.jquery.com/jquery-1.12.4.min.js"></script>
<script src="${contextPath}/resources/js/jquery.maskMoney.js"></script>

      
<div class="jumbotron" style="padding-left: 0px; padding-right: 0px;">        
		<div align="center" style="margin: auto;">
<h4>Seu saldo em dinheiro é <b>${saldoBRL}</b></h4><br>

<form:form modelAttribute="Resgatar" action="${contextPath}/painel/resgatar/conta-bancaria" method="post">				
<h3>Quanto você quer sacar?</h3>
        <spring:bind path="valorResgate">
               	<form:input type="text"
               	class="form-control input"
                style="width: 50%;font-size: 18px;" 
                data-thousands="." 
                data-decimal="," 
                data-prefix="R$ " 
                path="valorResgate" 
                placeholder="R$ 0,00"
                required="true"
                 />
                <form:errors path="valorResgate" cssClass="errorGeneric"></form:errors>
        </spring:bind>

<script type="text/javascript">$("#valorResgate").maskMoney();</script>
<br>
<span>Saldo calculado com base em nossa cotação de venda: <b>${cotacaoVENDA}</b> </span>				
<br>
    <spring:bind path="erros">
        <form:input type="hidden" path="erros" />
        <form:errors path="erros" cssClass="errorGeneric"></form:errors>
    </spring:bind>
<br>
<button type="submit" class="btn btn-primary" style="padding: 8px 23px !important;font-size: 17px !important;">Confirmar saque</button>

<div>
</div>
</form:form>

</div>
		
</div>
      
<%@include file="_footer.jsp"%>

</div> <!-- /container -->


<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
<script src="http://getbootstrap.com/assets/js/ie10-viewport-bug-workaround.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.0.3/js/bootstrap.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/9.6.0/highlight.min.js"></script>
				
</body>
</html>