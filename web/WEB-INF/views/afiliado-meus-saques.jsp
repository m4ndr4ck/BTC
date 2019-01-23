<%@include file="_header-afiliado.jsp"%>

<div class="jumbotron" style="padding-left: 0px; padding-right: 0px;">

    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="/afiliado/painel">Home</a></li>
        <li class="breadcrumb-item"><a href="/afiliado/painel/minha-conta">Minha conta</a></li>
        <li class="breadcrumb-item active">Saques bitcoin</li>
    </ol>

    <div class="container" id="tabelaConfirma">
        <c:if test="${fn:length(Resgates) > 0}">
            <h2>Seus saques em bitcoin</h2>
            <br>
        </c:if>
        <c:choose>
            <c:when test="${fn:length(Resgates) == 0}">
                <div>Você ainda não solicitou nenhum saque de bitcoin.</div>
            </c:when>
            <c:otherwise>
                <table class="table table-bordered">
                    <thead>
                    <tr id="fontTabela">
                        <th style="text-align: center;background: #dbdff3;">Data e Hora</th>
                        <th style="text-align: center;background: #dbdff3;">Carteira Bitcoin</th>
                        <th style="text-align: center;background: #dbdff3;">Bitcoin Sacado</th>
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
                            <td id="fontTabela" data-label="Carteira Bitcoin">
                                    ${resgate.enderecobitcoin}
                            </td>
                            <td class="last-row-responsive-resgates" id="fontTabela" data-label="Bitcoins Resgatados">
                                <fmt:formatNumber type = "number" maxFractionDigits = "8" value = "${resgate.bitcoins}" />

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