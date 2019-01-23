<%@include file="_header-afiliado.jsp"%>

<div class="jumbotron" style="padding-left: 0px; padding-right: 0px;">

    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="/afiliado/painel">Home</a></li>
        <li class="breadcrumb-item"><a href="/afiliado/painel/minha-conta">Minha conta</a></li>
        <li class="breadcrumb-item active">Minhas comissões</li>
    </ol>

    <div class="container" id="tabelaConfirma">
        <h2>Minhas comissões</h2>
        <br>
        <c:choose>
            <c:when test="${fn:length(Pagamentos) == 0}">
                <div>Nenhum afiliado seu comprou bitcoin ainda. Assim que algum deles comprar, sua comissão vai aparecer aqui.</div>
            </c:when>
            <c:otherwise>
                <table class="table table-bordered" id="responsivetable">
                    <thead>
                    <tr id="fontTabela">
                        <th style="text-align: center;background: #dbdff3;">ID</th>
                        <th style="text-align: center;background: #dbdff3;">Afiliado</th>
                        <th style="text-align: center;background: #dbdff3;">Data e Hora</th>
                        <th style="text-align: center;background: #dbdff3;">Bitcoin</th>
                        <th style="text-align: center;background: #dbdff3;">Comissão</th>
                        <th style="text-align: center;background: #dbdff3;">Status</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${Pagamentos}" var="pagamento">
                        <tr class="active">
                            <td id="fontTabela"  data-label="ID">${pagamento.id}</td>
                            <td id="fontTabela"  data-label="Afiliado">${pagamento.nome}</td>

                            <td id="fontTabela" data-label="Data e Hora">
                                <javatime:parseLocalDateTime value="${pagamento.dataHora}"  pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedDate" style="MS" />
                                <fmt:parseDate value="${parsedDate}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="data"/>
                                <fmt:formatDate value="${data}" pattern="dd/MM/yyyy HH:mm" var="dataFinal"/>
                                    ${dataFinal}
                            </td>
                            <td id="fontTabela" data-label="Bitcoin">
                                <fmt:formatNumber value="${pagamento.bitcoins}" minFractionDigits="8" type="number"/>
                            </td>
                            <td id="fontTabela" data-label="Comissão">
                                <b>0,0001 BTC</b>
                            </td>
                            <td id="fontTabela" data-label="Status" style="background-color: #d7e6ec;">
                                <b>
                                    <c:if test="${pagamento.status == 0}">Pagamento pendente</c:if>
                                    <c:if test="${pagamento.status == 1}">Pagamento pendente</c:if>
                                    <c:if test="${pagamento.status == 2}">Em análise</c:if>
                                    <c:if test="${pagamento.status == 3}">Pagamento aprovado</c:if>
                                    <c:if test="${pagamento.status == 4}">Disponível</c:if>
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

<%@include file="_footer-afiliado.jsp"%>

</div> <!-- /container -->


<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->



</body>
</html>