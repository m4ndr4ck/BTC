<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="description" content="">
<meta name="author" content="">
	<link rel="apple-touch-icon" sizes="57x57" href="${contextPath}/resources/img/ico/apple-icon-57x57.png">
	<link rel="apple-touch-icon" sizes="60x60" href="${contextPath}/resources/img/ico/apple-icon-60x60.png">
	<link rel="apple-touch-icon" sizes="72x72" href="${contextPath}/resources/img/ico/apple-icon-72x72.png">
	<link rel="apple-touch-icon" sizes="76x76" href="${contextPath}/resources/img/ico/apple-icon-76x76.png">
	<link rel="apple-touch-icon" sizes="114x114" href="${contextPath}/resources/img/ico/apple-icon-114x114.png">
	<link rel="apple-touch-icon" sizes="120x120" href="${contextPath}/resources/img/icoapple-icon-120x120.png">
	<link rel="apple-touch-icon" sizes="144x144" href="${contextPath}/resources/img/ico/apple-icon-144x144.png">
	<link rel="apple-touch-icon" sizes="152x152" href="${contextPath}/resources/img/ico/apple-icon-152x152.png">
	<link rel="apple-touch-icon" sizes="180x180" href="${contextPath}/resources/img/ico/apple-icon-180x180.png">
	<link rel="icon" type="image/png" sizes="192x192"  href="${contextPath}/resources/img/ico/android-icon-192x192.png">
	<link rel="icon" type="image/png" sizes="32x32" href="${contextPath}/resources/img/ico/favicon-32x32.png">
	<link rel="icon" type="image/png" sizes="96x96" href="${contextPath}/resources/img/ico/favicon-96x96.png">
	<link rel="icon" type="image/png" sizes="16x16" href="${contextPath}/resources/img/ico/favicon-16x16.png">
	<link rel="manifest" href="${contextPath}/resources/img/ico/manifest.json">
	<meta name="msapplication-TileColor" content="#ffffff">
	<meta name="msapplication-TileImage" content="${contextPath}/resources/img/ico/ms-icon-144x144.png">


<title>BTC Moedas</title>

<!-- Bootstrap core CSS -->
<link
	href="https://v4-alpha.getbootstrap.com/dist/css/bootstrap.min.css"
	rel="stylesheet">

<!-- Custom styles for this template -->
<link
	href="https://v4-alpha.getbootstrap.com/examples/dashboard/dashboard.css"
	rel="stylesheet">
</head>

<body>
	<nav
		class="navbar navbar-toggleable-md navbar-inverse fixed-top bg-inverse">
		<button class="navbar-toggler navbar-toggler-right hidden-lg-up"
			type="button" data-toggle="collapse"
			data-target="#navbarsExampleDefault"
			aria-controls="navbarsExampleDefault" aria-expanded="false"
			aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>
		<a class="navbar-brand" href="#">BTC Moedas</a>

		<div class="collapse navbar-collapse" id="navbarsExampleDefault">
			<ul class="navbar-nav mr-auto">
				<li class="nav-item active"><a class="nav-link" href="/admin">Home
						<span class="sr-only">(current)</span>
				</a></li>
				<li class="nav-item"><a class="nav-link" href="/logout">Sair</a>
				</li>
			</ul>
		</div>
	</nav>

	<div class="container-fluid">
		<div class="row">
		
<%@include file="_admin-menu-left.jsp"%>

			<main class="col-sm-9 offset-sm-3 col-md-10 offset-md-2 pt-3">
			<h2>Compras</h2>
			<div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Cliente</th>
                  <th>Data e Hora</th>
                  <th>Método</th>
                  <th>Cotação</th>
                  <th>Bitcoins</th>
                  <th>Preço</th>
                  <th>Status</th>
                </tr>
              </thead>
              <tbody>
		   <c:forEach items="${Compras}" var="compra">
		      <tr class="active">
		      	<td>${compra.id}</td>
		      	<td><a href="/admin/clientes/${compra.clienteid}">${compra.nome}</a></td>
		        <td>
					<javatime:parseLocalDateTime value="${compra.dataHora}"  pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedDate" style="MS" />
					<fmt:parseDate value="${parsedDate}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="data"/>
					<fmt:formatDate value="${data}" pattern="dd/MM/yyyy HH:mm" var="dataFinal"/>
					${dataFinal}
				</td>
				<td>
				<c:choose>
				<c:when test="${compra.tipoPagamento == 1}">
				Mensal
				</c:when>
				<c:otherwise>
				Compra avulsa
				</c:otherwise>
				</c:choose>
				</td>
		        <td>
		        <c:set var="cotacao" value="${fn:replace(compra.cotacao, 'R$', 'R$ ')}" />
		        ${cotacao}
		        </td>
		        <td><fmt:formatNumber value="${compra.bitcoins}" minFractionDigits="8" type="number"/></td>
		        <td>
		         <fmt:formatNumber value="${compra.valorPagamento}" minFractionDigits="2" type="currency"/>
		        </td>
		        <td>
		        <b>
					<c:if test="${compra.status == 0}">Pendente</c:if>
					<c:if test="${compra.status == 1}">Pendente</c:if>
					<c:if test="${compra.status == 2}">Em análise</c:if>
					<c:if test="${compra.status == 3}">Aprovado</c:if>
					<c:if test="${compra.status == 4}">Aprovado</c:if>
					<c:if test="${compra.status == 5}">Em disputa</c:if>
					<c:if test="${compra.status == 6}">Devolvida</c:if>
					<c:if test="${compra.status == 7}">Cancelada</c:if>
		        </b>
		        </td>
		      </tr>
		     </c:forEach>
              </tbody>
            </table>
          </div>
			</main>
		</div>
	</div>

	<!-- Bootstrap core JavaScript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script src="https://code.jquery.com/jquery-3.1.1.slim.min.js"
		integrity="sha384-A7FZj7v+d/sdmMqp/nOQwliLvUsJfDHW+k9Omg/a/EheAdgtzNs3hpfag6Ed950n"
		crossorigin="anonymous"></script>
	<script>window.jQuery || document.write('<script src="https://v4-alpha.getbootstrap.com/assets/js/vendor/jquery.min.js"><\/script>')</script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/tether/1.4.0/js/tether.min.js"
		integrity="sha384-DztdAPBWPRXSA/3eYEEUWrWCy7G5KFbe8fFjk5JAIxUYHKkDx6Qin1DkWx51bBrb"
		crossorigin="anonymous"></script>
	<script
		src="https://v4-alpha.getbootstrap.com/dist/js/bootstrap.min.js"></script>
	<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
	<script
		src="https://v4-alpha.getbootstrap.com/assets/js/ie10-viewport-bug-workaround.js"></script>


</body>
</html>