<%@include file="_header.jsp"%>       
      
<div class="jumbotron" style="padding-left: 0px; padding-right: 0px;">

<!-- Start CONTAINER -->
<div class="container" id="tabelaConfirma">
  <table class="table table-bordered">
    <thead>
      <tr>
        <th style="text-align: center;background: #dbdff3;">Confirme seu resgate</th>

      </tr>
    </thead>
    <tbody>
      <!--  <tr class="active">
        <td>Data e Hora: ${dataHora}</td>
      </tr>-->     
      <tr class="active">
        <td class="tdresgate">Valor do saque: ${valorResgate}</td>
      </tr>
      			<c:forEach items="${contaCadastrada}" var="firstVar">
			      <tr class="active">
			        <td class="tdresgate">Banco: <c:out value="${firstVar.banco}"/></td>
			      </tr>
			      <tr class="active">
			      <td class="tdresgate">
					<c:choose>
					    <c:when test="${firstVar.tipodeconta == 1}">
					        Conta Corrente
					    </c:when>
					    <c:otherwise>
					        Conta Poupança
					    </c:otherwise>
					</c:choose>
				  </td>
				  </tr>
				  <tr class="active">
			        <td class="tdresgate">Agência: <c:out value="${firstVar.agencia}"/></td>
			      </tr>
				  <tr class="active">
			        <td class="tdresgate">Conta: <c:out value="${firstVar.conta}"/></td>
			      </tr>
			      <tr class="active">
			        <td class="tdresgate">CPF: <c:out value="${cpf}"/></td>
			      </tr>
			      <tr class="active">
			        <td class="tdresgate"> Nome: <c:out value="${nome}"/></td>
			      </tr>
			      <c:if test="${not empty firstVar.dadosadicionais}">
			      <tr class="active">
			       <td class="tdresgate">Dados adicionais:			        
    				<c:choose>
					    <c:when test="${not empty firstVar.dadosadicionais}">
					        <c:out value="${firstVar.dadosadicionais}"/>
					    </c:when>
					    <c:otherwise>
					        Nenhum
					    </c:otherwise>
					</c:choose>			        
			        </td>
			      </tr>
			      </c:if>
			    </c:forEach>
     </tbody>
  </table>
  <span style="font-size: 13px;"><b>## ATENÇÃO ##</b><br>Certifique-se de que seus dados bancários estão corretos.</span>
 
<br>
<br>
<form:form method="post" modelAttribute="Resgate" action="${contextPath}/painel/resgatar/confirmar-resgate-conta-bancaria">
<button type="submit" class="btn btn-primary" style="padding: 8px 23px !important;font-size: 17px !important;">Finalizar saque</button>
</form:form>
</div>        
<!-- END CONTAINER -->

</div>
      
<%@include file="_footer.jsp"%>

</div> <!-- /container -->


<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->

  

</body>
</html>