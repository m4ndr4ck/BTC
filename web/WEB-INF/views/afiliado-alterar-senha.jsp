<%@include file="_header-afiliado.jsp"%>


<div class="jumbotron" style="padding-left: 0px; padding-right: 0px;">

    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="/afiliado/painel">Home</a></li>
        <li class="breadcrumb-item"><a href="/afiliado/painel/minha-conta">Minha conta</a></li>
        <li class="breadcrumb-item active">Alterar senha</li>
    </ol>

    <div align="center" style="margin: auto;">
        <c:if test="${empty response}">
            <h3>Altere sua senha</h3><br>
            <form:form method="POST" modelAttribute="senhaAlterarForm">
                <div class="form-group">
                    <spring:bind path="oldPassword">
                        <form:input type="password" path="oldPassword" class="form-control input-add-contabancaria" placeholder="Senha atual" required="required"></form:input>
                        <form:errors path="oldPassword"></form:errors>
                    </spring:bind>

                </div>
                <div class="form-group">
                    <spring:bind path="newPassword">
                        <form:input type="password" path="newPassword" class="form-control input-add-contabancaria" placeholder="Senha nova" required="required"></form:input>
                        <form:errors path="newPassword"></form:errors>
                    </spring:bind>

                </div>
                <div class="form-group">
                    <spring:bind path="confirmPassword">
                        <form:input type="password" path="confirmPassword" class="form-control input-add-contabancaria" placeholder="Confirme sua senha" required="required"></form:input>
                        <form:errors path="confirmPassword"></form:errors>
                    </spring:bind>
                </div>

                <br>
                <button type="submit" class="btn btn-primary" style="padding: 8px 23px !important;font-size: 17px !important;">Alterar senha</button>


                <div>
                </div>
            </form:form>
        </c:if>

        <c:if test="${!empty response}">
            <div style="text-align: center;">
                Sua senha foi alterada com sucesso.
            </div>
        </c:if>

    </div>

</div>

<%@include file="_footer-afiliado.jsp"%>

</body>
</html>