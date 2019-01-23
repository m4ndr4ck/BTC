<%@include file="_header.jsp"%>
            
<div class="jumbotron" style="padding-left: 0px; padding-right: 0px;">

	<ol class="breadcrumb">
		<li class="breadcrumb-item"><a href="/painel">Home</a></li>
		<li class="breadcrumb-item"><a href="/painel/minha-conta">Minha conta</a></li>
		<li class="breadcrumb-item active">Conta bancária</li>
	</ol>


<div align="center" style="margin: auto;">

<c:choose>
<c:when test="${fn:length(contaCadastrada) == 0}">
<h3>Cadastre sua conta bancária</h3>
</c:when>
<c:otherwise>
<h3>Conta bancária cadastrada</h3>
</c:otherwise>
</c:choose>
<br>

<c:choose>
<c:when test="${fn:length(contaCadastrada) == 0}">
<form:form modelAttribute="ContaBancaria" action="${contextPath}/painel/adicionar-conta-bancaria" method="post" style="">				
<div class="form-group">
<input type="text" class="form-control input-add-contabancaria" id="input-placeholder" name="banco" value="${fn:escapeXml(banco)}" placeholder="Banco" required>
</div>
				
<div class="form-group">
<select class="form-control m-b-sm input-add-contabancaria" name="tipodeconta" value="${fn:escapeXml(tipodeconta)}">
<option value="1">Conta Corrente</option>
<option value="2">Conta Poupança</option>
</select>
</div>

<div class="form-group">
<input type="text" class="form-control input-add-contabancaria" id="input-placeholder" name="agencia" value="${fn:escapeXml(agencia)}" placeholder="Agência com dígito" required>
</div>
<div class="form-group">
<input type="text" class="form-control input-add-contabancaria" id="input-placeholder" name="conta" value="${fn:escapeXml(conta)}" placeholder="Conta com dígito" required>
</div>
<div class="form-group">
<input type="text" class="form-control input-add-contabancaria" id="input-placeholder" name="dadosadicionais" value="${fn:escapeXml(dadosadicionais)}" placeholder="Dados Adicionais">
</div>
<span style="color: #777;">Somente contas vinculadas ao seu CPF serão aceitas.</span>

<br>	       

<br>
<button type="submit" class="btn btn-primary" style="padding: 8px 23px !important;font-size: 17px !important;">Cadastrar conta</button> 


<div>
</div>
</form:form>
</c:when>
<c:otherwise>
		<c:if test="${fn:length(contaCadastrada) gt 0}">
			<table class="table table-bordered" style="background: #fbfbfb;width: 65%;">
			    <thead>
			      <tr id="fontTabela">
			        <th style="text-align: center;background: #dbdff3;">Banco</th>
			        <th style="text-align: center;background: #dbdff3;">Tipo de Conta</th>
			        <th style="text-align: center;background: #dbdff3;">Agência</th>
			        <th style="text-align: center;background: #dbdff3;">Conta</th>
			        <th style="text-align: center;background: #dbdff3;">Dados Adicionais</th>
			        </tr>
			    </thead>
			    <tbody>
			    <c:forEach items="${contaCadastrada}" var="firstVar">
			      <tr style="margin-bottom:0 !important;">
			        <td data-label="Banco" id="fontTabela"><c:out value="${firstVar.banco}"/></td>
			        <td data-label="Tipo de Conta" id="fontTabela">
					<c:choose>
					    <c:when test="${firstVar.tipodeconta == 1}">
					        Conta Corrente
					    </c:when>
					    <c:otherwise>
					        Conta Poupança
					    </c:otherwise>
					</c:choose>
					<td data-label="Agência" id="fontTabela"><c:out value="${firstVar.agencia}"/></td>
			        <td data-label="Conta" id="fontTabela"><c:out value="${firstVar.conta}"/></td>
			        <td data-label="Dados Adicionais" id="fontTabela">
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
			    </c:forEach>  
			    </tbody>
			</table>
			
			<form:form modelAttribute="ContaBancaria" action="${contextPath}/painel/remover-conta-bancaria" method="post" style="">
			<button type="submit" class="btn btn-primary" style="padding: 8px 23px !important;font-size: 17px !important;">Excluir conta bancária</button> 
			</form:form>
			
		</c:if>
</c:otherwise>
</c:choose>	
		
		
</div>
		
</div>
      
<%@include file="_footer.jsp"%>

</div> <!-- /container -->


<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->

  

</body>
</html>