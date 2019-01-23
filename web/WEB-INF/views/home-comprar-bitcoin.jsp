<%@include file="_header-home.jsp"%>

<!-- Begin page content -->
<div class="container" class="text-black"
	style="max-width: 730px !important; font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif !important;">

	<div class="jumbotron" style="padding-left: 0px; padding-right: 0px;">
		<div align="center" style="margin: auto;">
			<h2>Comprar bitcoin</h2>
		</div>
		<div class="row" id="inputReal"
			style="margin: auto; text-align: center">
			<div class="col-md-6" style="display: none">
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
			<div class="row form-group"
				style="margin: 0 auto; padding-top: 15px;color #333;font-size:14px">
				<form>
					<h3 style="margin-top: 20px">Real</h3>
					<div id="no-filtering-place" data-filter="false" style="color: #464a4c;"></div>

					<h3 style="margin-top: 20px">Bitcoin</h3>
					<input id="btc" name="bitcoins" type="text"
						class="form-control input" /> <br>

					<!--<label
						class="radio-inline"> <input type="radio"
						name="tipoPagamento" value="1" />&nbsp;&nbsp;Investimento
						Mensal&nbsp;&nbsp;
					</label>
					<label class="radio-inline"> <input type="radio"
						name="tipoPagamento" value="2" />&nbsp;&nbsp;Compra avulsa
					</label>-->
					
					<div style="padding-top: 0px;padding-bottom: 20px;font-weight: bold;font-size: 16px;">1 BTC = ${cotacaoCOMPRA}</div>
					
					<button type="button"
						onclick="window.open('http://btcmoedas.com.br/login');"
						class="btn btn-primary"
						style="padding: 8px 23px !important; font-size: 17px !important; cursor: pointer;">Confirmar
						compra</button>

				</form>
			</div>

		</div>
	</div>
	<div class="spacer"></div>

</div>
<%@include file="_footer-home.jsp"%>
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