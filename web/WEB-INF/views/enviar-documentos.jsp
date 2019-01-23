<%@include file="_header.jsp"%> 
      
<div class="jumbotron" style="padding-left: 0px; padding-right: 0px;">

	<ol class="breadcrumb">
		<li class="breadcrumb-item"><a href="/painel">Home</a></li>
		<li class="breadcrumb-item"><a href="/painel/minha-conta">Minha conta</a></li>
		<li class="breadcrumb-item active">Enviar documentos</li>
	</ol>

<div align="center" style="margin: auto;" id="divdocumentos">
<h4>Documentos necessários para confirmar cadastro</h4>

<div style="text-align: left;padding-bottom: 15px;" id="textodocumentos">
<ul>
<li>Uma foto sua (selfie) <b>segurando</b> RG ou CNH</li>
<!--<li>Comprovante de residência no seu nome</li>-->
</ul>
</div>
	<form:form method="POST" modelAttribute="Documentos" action="${contextPath}/painel/enviar-documentos" enctype="multipart/form-data">

		
		<span class="btn btn-success fileinput-button" style="margin-bottom: 10px; padding: 5px 10px; font-size: 14px; position: relative; overflow: hidden;">
        <i class="glyphicon glyphicon-plus"></i>
        <span>Enviar arquivos...</span>
        <!-- The file input field used as target for the file upload widget -->
        <input style="position: absolute; right: 0; direction: ltr; margin: 0; opacity: 0; top: 0; font-size: 200px !important; cursor: pointer;" id="fileupload" type="file" name="file" multiple>
		</span>
		
		<div id="progress" class="progress" style="width: 80%;">
        	<div class="progress-bar progress-bar-success"></div>
    	</div>
		<div id="files" class="files"></div>
	</form:form>	
		<c:if test="${fn:length(documentosAnexados) gt 0}">
			<table class="table table-bordered" id="tabledocumentos">
			    <thead>
			      <tr>
			        <th style="background: #e0e2e6;text-align: center;">Arquivos anexados</th>
			      </tr>
			    </thead>
			    <tbody>
			    <c:forEach items="${documentosAnexados}" var="firstVar">
			      <tr>
			        <td>
			           <c:out value="${firstVar}"/>
			        </td>
			      </tr>
			    </c:forEach>  
			    </tbody>
			</table>
			<form method="POST" action="${contextPath}/painel/solicitar-verificacao">
			<br>
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			<button type="submit" class="btn btn-primary" style="padding: 8px 23px !important;font-size: 17px !important;">Solicitar verificação</button>
			</form>
		</c:if>
		<!--  <input type="submit" value="Upload"> Press here to upload the file!-->	
		
</div>
		
</div>

<%@include file="_footer.jsp"%>

</div> <!-- /container -->


<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script src="${contextPath}/resources/js/vendor/jquery.ui.widget.js"></script>
<script src="${contextPath}/resources/js/jquery.iframe-transport.js"></script>
<!-- The basic File Upload plugin -->
<script src="${contextPath}/resources/js/jquery.fileupload.js"></script>
<script>
/*jslint unparam: true */
/*global window, $ */
$(function () {
    'use strict';
    // Change this to the location of your server-side upload handler:
    var url = "${contextPath}/painel/enviar-documentos";
    $('#fileupload').fileupload({
        url: url,
        done: function (e, data) {
        	$.each(data.files, function (index, file) {
                $('<p/>').text(file.name).appendTo('#files');
                $('#progress .progress-bar').html('Arquivo enviado com sucesso');
                location.reload();
            });
        },
        progressall: function (e, data) {
            var progress = parseInt(data.loaded / data.total * 100, 10);
            $('#progress .progress-bar').html('');
            $('#progress .progress-bar').css(
                'width',
                progress + '%'
            );
        }
    }).prop('disabled', !$.support.fileInput)
        .parent().addClass($.support.fileInput ? undefined : 'disabled');
});
</script>

  

</body>
</html>