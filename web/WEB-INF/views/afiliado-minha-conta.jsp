<%@include file="_header-afiliado.jsp"%>

<div class="jumbotron" style="padding-left: 0px; padding-right: 0px;">

    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="/afiliado/painel">Home</a></li>
        <li class="breadcrumb-item active">Minha Conta</li>
    </ol>

    <c:set var="verificado" value="${verificado}" />
    <div align="center" style="margin: auto;">
        <h2>Gerencie sua conta</h2>
        <br>
        <span style="font-size: 1.5em;"><a href="${contextPath}/afiliado/painel/meus-afiliados">Meus afiliados</a></span>
        <br>
        <br>
        <span style="font-size: 1.5em;"><a href="${contextPath}/afiliado/painel/minhas-comissoes">Minhas comissões</a></span>
        <br>
        <br>
        <span style="font-size: 1.5em;"><a href="${contextPath}/afiliado/painel/meus-resgates-carteira-bitcoin">Meus saques</a></span>
        <br>
        <br>
        <span style="font-size: 1.5em;"><a href="${contextPath}/afiliado/painel/material-divulgacao">Material de divulgação</a></span>
        <br>
        <br>
        <span style="font-size: 1.5em;"><a href="${contextPath}/afiliado/painel/alterar-senha">Alterar senha</a></span>
    </div>

</div>

<%@include file="_footer-afiliado.jsp"%>

</div> <!-- /container -->


<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->



</body>
</html>