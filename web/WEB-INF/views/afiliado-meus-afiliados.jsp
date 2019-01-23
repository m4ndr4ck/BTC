<%@include file="_header-afiliado.jsp"%>

<div class="jumbotron" style="padding-left: 0px; padding-right: 0px;">

    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="/afiliado/painel">Home</a></li>
        <li class="breadcrumb-item"><a href="/afiliado/painel/minha-conta">Minha conta</a></li>
        <li class="breadcrumb-item active">Meus afiliados</li>
    </ol>

    <div class="container" id="tabelaConfirma">
        <h2>Meus afiliados</h2>
        <br>
        <c:choose>
            <c:when test="${fn:length(Afiliados) == 0}">
                <div>Você ainda não tem afiliados cadastrados. Quanto tiver, eles vão aparecer aqui.</div>
            </c:when>
            <c:otherwise>
                <table class="table table-bordered" id="responsivetable">
                    <thead>
                    <tr id="fontTabela">
                        <th style="text-align: center;background: #dbdff3;">Afiliado</th>
                        <th style="text-align: center;background: #dbdff3;">Já enviou documentos</th>
                        <th style="text-align: center;background: #dbdff3;">Compras aprovadas</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${Afiliados}" var="afiliado">
                        <tr class="active">
                            <td id="fontTabela"  data-label="Afiliado">${afiliado.nome}</td>
                            <td id="fontTabela"  data-label="Já enviou documentos">
                                <c:if test="${afiliado.verificado == 0}">Não</c:if>
                                <c:if test="${afiliado.verificado == 1}">Sim</c:if>

                            </td>
                            <td id="fontTabela"  data-label="Compras Aprovadas">${afiliado.comprasAprovadas}</td>
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