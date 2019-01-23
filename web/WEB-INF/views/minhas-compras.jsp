<%@include file="_header.jsp"%>
      
<div class="jumbotron" style="padding-left: 0px; padding-right: 0px;">

    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="/painel">Home</a></li>
        <li class="breadcrumb-item"><a href="/painel/minha-conta">Minha conta</a></li>
        <li class="breadcrumb-item active">Minhas compras</li>
    </ol>

<div class="container" id="tabelaConfirma">
<h2>Suas compras de bitcoin</h2>
<br>
<c:choose>
<c:when test="${fn:length(Pagamentos) == 0}">
<div>Você ainda não fez nenhuma compra de bitcoin. <a href="#">Clique aqui</a> para fazer.</div>
</c:when>
<c:otherwise>
<table class="table table-bordered" id="responsivetable">
    <thead>
      <tr id="fontTabela">
        <th style="text-align: center;background: #dbdff3;">Data e Hora</th>
        <!--<th style="text-align: center;background: #dbdff3;">Método</th>-->
        <th style="text-align: center;background: #dbdff3;">Cotação</th>
        <th style="text-align: center;background: #dbdff3;">Bitcoin</th>
        <th style="text-align: center;background: #dbdff3;">Preço</th>
        <th style="text-align: center;background: #dbdff3;">Status</th>
      </tr>
    </thead>
    <tbody>
    <c:forEach items="${Pagamentos}" var="pagamento">
      <tr class="active">
        <td id="fontTabela" data-label="Data e Hora">
        <javatime:parseLocalDateTime value="${pagamento.dataHora}"  pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedDate" style="MS" />
        <fmt:parseDate value="${parsedDate}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="data"/>
        <fmt:formatDate value="${data}" pattern="dd/MM/yyyy HH:mm" var="dataFinal"/>
        ${dataFinal}
        </td>
<!--		<td id="fontTabela" data-label="Método">
		<c:choose>
		<c:when test="${pagamento.tipoPagamento == 1}">
		Mensal
		</c:when>
		<c:otherwise>
		Compra avulsa
		</c:otherwise>
		</c:choose>
		</td>-->
        <td id="fontTabela" data-label="Cotação">
        <c:set var="cotacao" value="${fn:replace(pagamento.cotacao, 'R$', 'R$ ')}" />
        ${cotacao}
        </td>
        <td id="fontTabela" data-label="Bitcoins">

            <fmt:formatNumber value="${pagamento.bitcoins}" minFractionDigits="8" type="number"/>

        </td>
        <td id="fontTabela" data-label="Preço">
         <fmt:formatNumber value="${pagamento.valorPagamento}" minFractionDigits="2" type="currency"/>
        </td>
        <td id="fontTabela" data-label="Status" style="background-color: #d7e6ec;">
        <b>
        <c:if test="${pagamento.status == 0}">Pendente</c:if>
        <c:if test="${pagamento.status == 1}">Pendente</c:if>
        <c:if test="${pagamento.status == 2}">Em análise</c:if>
        <c:if test="${pagamento.status == 3}">Aprovada</c:if>
        <c:if test="${pagamento.status == 4}">Aprovada</c:if>
        <c:if test="${pagamento.status == 5}">Em disputa</c:if>
        <c:if test="${pagamento.status == 6}">Devolvida</c:if>
        <c:if test="${pagamento.status == 7}">Cancelada</c:if>
        </b>
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