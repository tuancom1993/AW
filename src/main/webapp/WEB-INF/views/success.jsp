
<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta charset="UTF-8">
<title>Amway - VietNam official site</title>
<link rel="shortcut icon" href="<c:url value='/resources/img/amway-logo.ico'/>" />

<link href="<c:url value='/resources/css/style.css'/>"
rel="stylesheet"></link>
</head>

<body>
	<div>
		<img
			src="data:image/png;base64,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"
			data-th-src="|cid:amway-logo|" name="Picture 1" align="bottom"
			width="200" height="80" border="0" />

    <h1 class="display-1" style="text-align: center; color: #3E50B4">Accept course for trainee success. Thank you !</h1>

	</div>

	</div>
	<script
		src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>


</body>
</html>
