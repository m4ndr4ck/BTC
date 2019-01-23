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
			<h2>Resgates para Conta Bancária</h2>
			<div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Nome</th>
                  <th>Data e Hora</th>
                  <th>Banco</th>
                  <th>Tipo de Conta</th>
                  <th>Agência</th>
                  <th>Conta</th>
                  <th>Valor do Resgate</th>
                </tr>
              </thead>
              <tbody>
    <c:forEach items="${Resgates}" var="resgate">
      <tr class="active">
      	<td>${resgate.id}</td>
      	<td>${resgate.nome}</td>
        <td>
        <javatime:parseLocalDateTime value="${resgate.dataHora}"  pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedDate" style="MS" />
        <fmt:parseDate value="${parsedDate}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="data"/>
        <fmt:formatDate value="${data}" pattern="dd/MM/yyyy HH:mm" var="dataFinal"/>
        ${dataFinal}
        </td>
        <td>
		${resgate.banco}
		</td>
		<td>
		<c:choose>
		<c:when test="${resgate.tipodeconta == 1}">
		Conta Corrente
		</c:when>
		<c:otherwise>
		Conta Poupança
		</c:otherwise>
		</c:choose>
		</td>
        <td>
		${resgate.agencia}
        </td>
        <td>
        ${resgate.conta}
        </td>
        <td>
         <fmt:formatNumber value="${resgate.valorResgate}" minFractionDigits="2" type="currency"/>
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