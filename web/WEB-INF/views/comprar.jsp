<%@include file="_header.jsp"%>

<div class="jumbotron" style="padding-left: 0px; padding-right: 0px;">

	<div align="center" style="margin: auto;">
		<h2>Comprar bitcoin</h2>
	</div>
	<div class="row" id="inputReal" style="margin: auto;">
		<div class="col-md-6">
			<div class="row form-group">

				<div class="col-md-7" style="display: none">
					<select class="form-control" id="base">
						<option selected>R$ 20,00</option>
						<option>R$ 30,00</option>
						<option>R$ 40,00</option>
						<option>R$ 50,00</option>
					</select>
				</div>
			</div>

		</div>
		<div class="row form-group">
			<form:form method="post" modelAttribute="Pagamento">
				<h3>Real</h3>
				<div id="no-filtering-place" data-filter="false"></div>

				<h3>Bitcoin</h3>
				<input required id="btc" name="bitcoins" value="${fn:escapeXml(bitcoins)}" maxlength="11"
					data-decimal="," data-precision="8" type="text"
					class="form-control input" />
				<br>
				<!--<label class="radio-inline"> <input type="radio"
					name="tipoPagamento" value="1" required/>Investimento Mensal
				</label>
				<label class="radio-inline"> <input type="radio"
					name="tipoPagamento" value="2" required/>Compra avulsa
				</label>-->

				
				<spring:bind path="erros">
				<form:input type="hidden" path="erros" />
				<form:errors path="erros" cssClass="errorGeneric"></form:errors>
				</spring:bind>
				
				<br>
				<button type="submit" class="btn btn-primary"
					style="padding: 8px 23px !important; font-size: 17px !important;">Confirmar
					compra</button>
					
			</form:form>
		</div>

	</div>
</div>

<%@include file="_footer.jsp"%>

</div>
<!-- /container -->


<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
<script
	src="http://getbootstrap.com/assets/js/ie10-viewport-bug-workaround.js"></script>
<script src="https://code.jquery.com/jquery-1.12.4.min.js"></script>
<script src="${contextPath}/resources/js/jquery.maskMoney.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.0.3/js/bootstrap.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/9.6.0/highlight.min.js"></script>
<script
	src="https://rawgithub.com/indrimuska/jquery-editable-select/master/dist/jquery-editable-select.min.js"></script>
<script>
    $(document).ready(
        function() {
            $('pre code').each(function(i, block) {
                hljs.highlightBlock(block);
            });
            $.each([ 'basic', 'default', 'slide', 'fade', 'appendTo',
                'no-filtering', 'html' ], function(i, id) {
                var $place = $('#' + id + '-place');
                var $select = $('#base').clone().attr("id", "real").attr(
                    "name", "valorPagamento").attr("data-thousands",
                    ".").attr("data-decimal", ",").attr("data-prefix",
                    "R$ ").removeAttr('base').appendTo($place);
                if (id != 'basic')
                    $select.find('option:selected').removeAttr('selected');
                $select.editableSelect($place.data());
            });

            $('#select').editableSelect().on(
                'select.editable-select',
                function(e, li) {
                    $('#last-selected').html(
                        li.val() + '. ' + li.text());
                });

            $('#real').on(
                'keydown',
                function() {
                    setTimeout(function() {
                        var $real = $("#real").val().replace("R$", "")
                            .replace(",", ".");
                        var $valorbtc = $real / ${cotacaoCompraSemBRL};
                        $('#btc').val(
                            $valorbtc.toLocaleString('pt-BR', {
                                maximumSignificantDigits : 6
                            }));
                    }, 0);
                });

            $('#btc').on('keydown', function() {
                setTimeout(function() {
                    var $btc = $("#btc").val().replace(",", ".") * ${cotacaoCompraSemBRL};
                    var $brl = "R$ ";
                    $('#real').val($brl + $btc.toLocaleString('pt-BR', {
                        maximumFractionDigits : 2,
                        minimumFractionDigits : 2
                    }));
                }, 0);
            });

            $("#real").focusout(function() {
                setTimeout(function() {
                    //var $real = $("#real").val().replace("R$", "")
                    //		.replace(",", ".");
                    //var $valorbtc = $real / ${cotacaoCompraSemBRL};
                    var $real = parseInt($("#real").val().replace("R$", "").replace('.','').replace(' ',''));
                    var $valorbtc = $real*100000000/parseInt(${cotacaoCompraSemBRL}*100000000);
                    $('#btc').val($valorbtc.toLocaleString('pt-BR', {
                        maximumSignificantDigits : 6
                    }));
                }, 0);
            });

            $("#real").maskMoney();

        });
</script>

</body>
</html>