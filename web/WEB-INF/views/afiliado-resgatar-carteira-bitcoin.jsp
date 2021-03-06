<%@include file="_header-afiliado.jsp"%>

<div class="jumbotron" style="padding-left: 0px; padding-right: 0px;">
    <div align="center" style="margin: auto;">

        <form:form id="Resgate" modelAttribute="Resgate"
                   action="${contextPath}/afiliado/painel/resgatar/carteira-bitcoin"
                   method="post">
            <h3>Quanto voc� quer sacar</h3>
            <input id="btc" maxlength="10" data-decimal="," data-precision="8"
                   name="bitcoins" value="${fn:escapeXml(bitcoins)}" type="text" placeholder="0,00000000"
                   class="form-control input inputresgates" style="font-size: 18px;"
                   required>

            <h3>Sua carteira bitcoin</h3>
            <input id="enderecobitcoin" name="enderecobitcoin" value="${fn:escapeXml(enderecobitcoin)}" minlength="26"
                   maxlength="35" pattern="[a-zA-Z0-9-]+"
                   placeholder="Ex: 13m8twt1ppfsrfTm9WwXCAgQQAqPAdK7KO" type="text"
                   class="form-control input inputresgates" required>
            <spring:bind path="erros">
                <form:input type="hidden" path="erros" />
                <form:errors path="erros" cssClass="errorGeneric"></form:errors>
            </spring:bind>
            <br>
            <button type="submit" class="btn btn-primary"
                    style="padding: 8px 23px !important; font-size: 17px !important;">Confirmar
                saque</button>

            <div></div>
        </form:form>

    </div>

</div>

<%@include file="_footer-afiliado.jsp"%>

</div>
<!-- /container -->


<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
<script src="https://code.jquery.com/jquery-1.12.4.min.js"></script>
<script src="${contextPath}/resources/js/jquery.maskMoney.js"></script>

<script>
    $(document).ready(function() {
        $("#btc").maskMoney();

    });

    $('#enderecobitcoin').keypress(function(e) {
        var regex = new RegExp("^[a-zA-Z0-9]+$");
        var str = String.fromCharCode(!e.charCode ? e.which : e.charCode);
        if (regex.test(str)) {
            return true;
        }
        e.preventDefault();
        return false;
    });


</script>


</body>
</html>