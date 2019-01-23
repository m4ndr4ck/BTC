<%@include file="_header.jsp"%> 
      
<div class="jumbotron" style="padding-left: 0px; padding-right: 0px;">

    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="/painel">Home</a></li>
        <li class="breadcrumb-item"><a href="/painel/minha-conta">Minha conta</a></li>
        <li class="breadcrumb-item active">Saques bancários</li>
    </ol>

<div class="container" id="tabelaConfirma">
<c:if test="${fn:length(Resgates) > 0}">
<h2>Seus saques bancários</h2>
<br>
</c:if>
<c:choose>
<c:when test="${fn:length(Resgates) == 0}">
<div>Você ainda não solicitou nenhum resgate para sua conta bancária.</div>
</c:when>
<c:otherwise>
<table class="table table-bordered">
    <thead>
      <tr id="fontTabela">
        <th style="text-align: center;background: #dbdff3;">Data e Hora</th>
        <th style="text-align: center;background: #dbdff3;">Banco</th>
        <th style="text-align: center;background: #dbdff3;">Tipo de Conta</th>
        <th style="text-align: center;background: #dbdff3;">Agência</th>
        <th style="text-align: center;background: #dbdff3;">Conta</th>
        <th style="text-align: center;background: #dbdff3;">Valor do Saque</th>
      </tr>
    </thead>
    <tbody>
    <c:forEach items="${Resgates}" var="resgate">
      <tr class="active">
        <td id="fontTabela" data-label="Data e Hora">
        <javatime:parseLocalDateTime value="${resgate.dataHora}"  pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedDate" style="MS" />
        <fmt:parseDate value="${parsedDate}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="data"/>
        <fmt:formatDate value="${data}" pattern="dd/MM/yyyy HH:mm" var="dataFinal"/>
        ${dataFinal}
        </td>
        <td id="fontTabela" data-label="Banco">
		${resgate.banco}
		</td>
		<td id="fontTabela" data-label="Tipo de Conta">
		<c:choose>
		<c:when test="${resgate.tipodeconta == 1}">
		Conta Corrente
		</c:when>
		<c:otherwise>
		Conta Poupança
		</c:otherwise>
		</c:choose>
		</td>
        <td id="fontTabela" data-label="Agência">
		${resgate.agencia}
        </td>
        <td id="fontTabela" data-label="Conta">
        ${resgate.conta}
        </td>
        <td class="last-row-responsive-resgates" id="fontTabela" data-label="Valor do Resgate">
         <fmt:formatNumber value="${resgate.valorResgate}" minFractionDigits="2" type="currency"/>
        </td>
      </tr>
     </c:forEach>
    </tbody>
 </table>
</c:otherwise>
</c:choose>
</div>
		
</div>
      
<%@include file="_footer.jsp"%>

</div> <!-- /container -->


<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->

  

</body>
</html>