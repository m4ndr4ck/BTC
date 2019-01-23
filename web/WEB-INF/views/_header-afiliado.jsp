<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<fmt:setLocale value = "pt_BR" scope="session"/>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <!-- Global site tag (gtag.js) - Google Analytics -->
    <script async src="https://www.googletagmanager.com/gtag/js?id=UA-108266698-1"></script>
    <script>
        window.dataLayer = window.dataLayer || [];
        function gtag(){dataLayer.push(arguments);}
        gtag('js', new Date());

        gtag('config', 'UA-108266698-1');
    </script>

    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="A forma mais simples de obter Bitcoin. Compre utilizando seu cartão de crédito.">
    <meta name="author" content="BTC Moedas">
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

    <title>BTC Moedas | Dashboard</title>

    <!--  MAIN CSS -->
    <link href="${contextPath}/resources/css/style.css" rel="stylesheet">

    <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="${contextPath}/resources/css/ie10-viewport-bug-workaround.css" rel="stylesheet">
    <link href="${contextPath}/resources/css/jumbotron-narrow.css" rel="stylesheet">
    <link rel="stylesheet" href="${contextPath}/resources/css/jquery-editable-select.min.css">
    <!--<link rel="stylesheet" href="${contextPath}/resources/css/home/foundation-icons.min.css">-->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/foundicons/3.0.0/foundation-icons.min.css">
    <script src="${contextPath}/resources/js/ie-emulation-modes-warning.js"></script>

</head>

<body id="bodybg">

<div class="container" id="container">
    <div>
        <h1 id="logo"><i class="fi-bitcoin-circle" style="font-size: 44px;padding-right: 10px;top: 4px;position: relative;"></i>BTC Moedas</h1>
    </div>

    <div id="cotacaoDiv" class="header clearfix jumbotron">
        <h3 class="text-muted" id="cotacaoH3">
            Você tem <b>${saldo} BTC</b><span style="margin-left: 5px;">
        </h3>
    </div>
