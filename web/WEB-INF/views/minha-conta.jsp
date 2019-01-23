<%@include file="_header.jsp"%> 
      
<div class="jumbotron" style="padding-left: 0px; padding-right: 0px;">

    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="/painel">Home</a></li>
        <li class="breadcrumb-item active">Minha Conta</li>
    </ol>

<c:set var="verificado" value="${verificado}" />   
<div align="center" style="margin: auto;">
<h2>Gerencie sua conta</h2>
<br>
<span style="font-size: 1.5em;"><a href="${contextPath}/painel/minhas-compras">Minhas compras</a></span>
<br>
<br>
<span style="font-size: 1.5em;"><a href="${contextPath}/painel/meus-resgates-carteira-bitcoin">Meus saques</a></span>
<!--<br>
<br>
<span style="font-size: 1.5em;"><a href="${contextPath}/painel/adicionar-conta-bancaria">Adicionar conta bancária</a></span>-->
<br>
<br>
<span style="font-size: 1.5em;"><a href="${contextPath}/painel/enviar-documentos">Enviar documentos</a></span>
<br>
<br>
<span style="font-size: 1.5em;"><a href="${contextPath}/painel/alterar-senha">Alterar senha</a></span>
</div>
		
</div>
      
<%@include file="_footer.jsp"%>

</div> <!-- /container -->


<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->

  

</body>
</html>