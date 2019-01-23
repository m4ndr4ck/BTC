<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="_header-home.jsp"%>

    <!-- Begin page content -->
<div class="container" style="max-width:730px!important;color:white;font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif!important;">
<!--Você pode entrar em contato conosco através do e-mail <b>admin@btcmoedas.com.br</b> ou pelo número <b>41 99973-7902</b>. Se preferir pode deixar também uma mensagem através do formulário abaixo, iremos lhe responder o mais rápido possível.
<br><br>
<form action="${contextPath}/contato" method="POST">
  <div class="form-group">
    <label for="nome">Nome</label>
    <input type="nome" class="form-control" id="nome" placeholder="">
  </div>

  <div class="form-group">
    <label for="email">E-mail</label>
    <input type="email" class="form-control" id="email" aria-describedby="emailHelp" placeholder="">
  </div>
  <div class="form-group">
    <label for="mensagem">Mensagem</label>
    <textarea class="form-control" id="mensagem" rows="3"></textarea>
  </div>
  <button type="submit" class="btn btn-primary">Enviar</button>
  <br>
<c:if test="${sucesso}">
  <div align="center">Sua mensagem foi enviada com sucesso.</div>
</c:if>
  <br>
</form>-->
  <div class="card" style="background-color: transparent !important; border:none">
      <div class="jumbotron">
          <div align="center" style="margin: auto;"><h2 class="atendimento">Atendimento 24 horas</h2></div>
          <div class="card-text" style="
    padding-top: 20px;
">


              <table class="table">
                  <tbody>
                  <tr>
                      <td class="tdcontato"><b>E-mail</b></td>
                      <td class="tdcontato">admin@btcmoedas.com.br</td>
                  </tr>
                  <tr>
                      <td class="tdcontato"><b>Telefone</b></td>
                      <td class="tdcontato">+55 41 99973-7902 | +55 41 3044-2916</td>
                  </tr>
                  <tr>
                      <td class="tdcontato"><b>Facebook</b></td>
                      <td class="tdcontato"><a target="_blank" href="https://www.facebook.com/BTC-Moedas-878798678935682/" style="
    color: white;
">@BTCMoedas</a></td>
                  </tr>
                  <tr>
                      <td class="tdcontato"><b>Chat Online</b></td>
                      <td class="tdcontato">Esse botão cinza na parte de baixo da página</td>
                  </tr>
                  </tr>
                  <tr>
                      <td class="tdcontato"><b>Endereço</b></td>
                      <td class="tdcontato">Avenida República Argentina 3109 - Curitiba/PR</td>
                  </tr>
                  <tr>
                      <td class="tdcontato"></td><td class="tdcontato responsive"></td>
                  </tr>
                  </tbody>
              </table>
          </div>
      </div>
  </div>
</div>

<%@include file="_footer-home.jsp"%> 		
  </body>
</html>