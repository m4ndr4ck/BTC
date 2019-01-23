<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <!-- Global site tag (gtag.js) - Google Analytics -->
    <script async src="https://www.googletagmanager.com/gtag/js?id=UA-108266698-1"></script>
    <script>
        window.dataLayer = window.dataLayer || [];
        function gtag(){dataLayer.push(arguments);}
        gtag('js', new Date());

        gtag('config', 'UA-108266698-1');
    </script>

    <meta charset="utf-8">
    <meta name="google-site-verification" content="_gFrjeyO-X_PKAoTLCsr5_1O5GJZA38htMV8LlnQbG4" />
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="Compre bitcoin com seu cartão de crédito.">
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

    <meta property="og:title" content="BTC Moedas">
    <meta property="og:type" content="website">
    <meta property="og:url" content="/">
    <meta property="og:image" content="/resources/img/btc_ad_face.png">
    <meta property="og:site_name" content="BTC Moedas">
    <meta property="og:description" content="Compre bitcoin com seu cartão de crédito.  ">


    <title>BTC Moedas</title>

    <style>
        body {

            color: #ffffff!important;
            width: 100%;
            height:100%;
            margin-bottom: 60px;
            font-family: 'Open Sans', sans-serif;
            background: #092756;
            background: -moz-radial-gradient(0% 100%, ellipse cover, rgba(104,128,138,.4) 10%,rgba(138,114,76,0) 40%),-moz-linear-gradient(top,  rgba(57,173,219,.25) 0%, rgba(42,60,87,.4) 100%), -moz-linear-gradient(-45deg,  #670d10 0%, #092756 100%);
            background: -webkit-radial-gradient(0% 100%, ellipse cover, rgba(104,128,138,.4) 10%,rgba(138,114,76,0) 40%), -webkit-linear-gradient(top,  rgba(57,173,219,.25) 0%,rgba(42,60,87,.4) 100%), -webkit-linear-gradient(-45deg,  #670d10 0%,#092756 100%);
            background: -o-radial-gradient(0% 100%, ellipse cover, rgba(104,128,138,.4) 10%,rgba(138,114,76,0) 40%), -o-linear-gradient(top,  rgba(57,173,219,.25) 0%,rgba(42,60,87,.4) 100%), -o-linear-gradient(-45deg,  #670d10 0%,#092756 100%);
            background: -ms-radial-gradient(0% 100%, ellipse cover, rgba(104,128,138,.4) 10%,rgba(138,114,76,0) 40%), -ms-linear-gradient(top,  rgba(57,173,219,.25) 0%,rgba(42,60,87,.4) 100%), -ms-linear-gradient(-45deg,  #670d10 0%,#092756 100%);
            background: -webkit-radial-gradient(0% 100%, ellipse cover, rgba(104,128,138,.4) 10%,rgba(138,114,76,0) 40%), linear-gradient(to bottom,  rgba(57,173,219,.25) 0%,rgba(42,60,87,.4) 100%), linear-gradient(135deg,  #670d10 0%,#092756 100%);
            filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#3E1D6D', endColorstr='#092756',GradientType=1 );
        }

        @-moz-document url-prefix() {
            .spacer {padding-top:20px;}
        }

        @media screen and (max-width: 500px) {
            #btc-logo{
                display:none;
            }
        }
        @media only screen and (max-width: 1400px) and (min-width: 1170px) {
            #navbar {
                padding-left: calc(50vw - 560px);
                padding-right: calc(50vw - 560px);

            }

            #navbarColor01{
                margin-left: 49.0%;
            }
        }

        #navbar {
            border-bottom: solid 1px #848282;
            background-color: rgba(255,255,255,0.1)!important;
        }

        #real {
            background-color: #edffed !important;
        }

        .h3, h3 {
            font-size: 24px!important;
        }

        #footerlink{
            color: rgba(255,255,255,.5);
            text-decoration: none;
        }
        #footerlink:hover{
            color: #fff;
            text-decoration: none;
        }

        .heading-primary {
            font-size: 2em;
            padding: 1em;
            text-align: center;
        }

        a:focus, a:hover {
            color: #fff !important;
            text-decoration: none !important;
        }

        .accordion dl,
        .accordion-list {
            /**border: 1px solid #ddd; */
        }
        .accordion dl:after,
        .accordion-list:after {
            content: "";
            display: block;
            height: 1em;
            width: 100%;
            background-color: #3a6b92;
        }

        .accordion dd,
        .accordion__panel {
            background-color: #eee;
            font-size: 1em;
            line-height: 1.5em;
        }

        .accordion p {
            padding: 0.5em 2em 1em 2em;
        }

        .accordion {
            position: relative;
            background-color: #eee;
            color: black;
        }

        .accordionTitle,
        .accordion__Heading {
            background-color: #5284ab;
            text-align: center;
            font-weight: 700;
            padding: 2em;
            display: block;
            text-decoration: none;
            color: #fff;
            -webkit-transition: background-color 0.5s ease-in-out;
            transition: background-color 0.5s ease-in-out;
            border-bottom: 1px solid #28789c;
        }
        .accordionTitle:before,
        .accordion__Heading:before {
            content: "+";
            font-size: 1.5em;
            line-height: 0.5em;
            float: left;
            -webkit-transition: -webkit-transform 0.3s ease-in-out;
            transition: -webkit-transform 0.3s ease-in-out;
            transition: transform 0.3s ease-in-out;
            transition: transform 0.3s ease-in-out, -webkit-transform 0.3s ease-in-out;
        }
        .accordionTitle:hover,
        .accordion__Heading:hover {
            background-color: #3a6b92;
            color: white;
            text-decoration: none;
        }

        .accordionTitleActive,
        .accordionTitle.is-expanded {
            background-color: #3a6b92;
        }
        .accordionTitleActive:before,
        .accordionTitle.is-expanded:before {
            -webkit-transform: rotate(-225deg);
            transform: rotate(-225deg);
        }

        .accordionItem {
            height: auto;
            overflow: hidden;
            max-height: 50em;
            -webkit-transition: max-height 1s;
            transition: max-height 1s;
        }

        @media screen and (min-width: 48em) {
            .accordionItem {
                max-height: auto;
                -webkit-transition: max-height 0.5s;
                transition: max-height 0.5s;
            }
        }

        .accordionItem.is-collapsed {
            max-height: 0;
            margin-bottom: 0px;
        }

        .no-js .accordionItem.is-collapsed {
            max-height: auto;
        }

        .animateIn {
            -webkit-animation: accordionIn 0.45s normal ease-in-out both 1;
            animation: accordionIn 0.45s normal ease-in-out both 1;
        }

        .animateOut {
            -webkit-animation: accordionOut 0.45s alternate ease-in-out both 1;
            animation: accordionOut 0.45s alternate ease-in-out both 1;
        }

        @-webkit-keyframes accordionIn {
            0% {
                opacity: 0;
                -webkit-transform: scale(0.9) rotateX(-60deg);
                transform: scale(0.9) rotateX(-60deg);
                -webkit-transform-origin: 50% 0;
                transform-origin: 50% 0;
            }
            100% {
                opacity: 1;
                -webkit-transform: scale(1);
                transform: scale(1);
            }
        }

        @keyframes accordionIn {
            0% {
                opacity: 0;
                -webkit-transform: scale(0.9) rotateX(-60deg);
                transform: scale(0.9) rotateX(-60deg);
                -webkit-transform-origin: 50% 0;
                transform-origin: 50% 0;
            }
            100% {
                opacity: 1;
                -webkit-transform: scale(1);
                transform: scale(1);
            }
        }
        @-webkit-keyframes accordionOut {
            0% {
                opacity: 1;
                -webkit-transform: scale(1);
                transform: scale(1);
            }
            100% {
                opacity: 0;
                -webkit-transform: scale(0.9) rotateX(-60deg);
                transform: scale(0.9) rotateX(-60deg);
            }
        }
        @keyframes accordionOut {
            0% {
                opacity: 1;
                -webkit-transform: scale(1);
                transform: scale(1);
            }
            100% {
                opacity: 0;
                -webkit-transform: scale(0.9) rotateX(-60deg);
                transform: scale(0.9) rotateX(-60deg);
            }
        }

    </style>

    <!-- Bootstrap core CSS -->
    <link href="${contextPath}/resources/css/home/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${contextPath}/resources/css/home/foundation-icons.min.css">

    <!-- Custom styles for this template -->
    <link href="${contextPath}/resources/css/home/sticky-footer-navbar.css" rel="stylesheet">
    <link rel="stylesheet" href="${contextPath}/resources/css/jquery-editable-select.min.css">
    <link rel="stylesheet" href="${contextPath}/resources/css/home/octicons.css">
</head>
<body>
<div style="
    padding-top: 5%;
">

    <h1 style="
    color: #fff;
    text-shadow: 0 0 10px rgba(0,0,0,0.3);
    letter-spacing: 1px;
    text-align: center;
">${errorMsg}</h1>

</div>
</body>
</html>