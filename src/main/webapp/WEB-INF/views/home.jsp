<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
	<script type="text/javascript">
	$(body).add
	</script>
	<link href="resources/css/home.css" rel="stylesheet" type="text/css"/>
	<link href="resources/css/style.css" rel="stylesheet" type="text/css"/>

</head>
<body>
<div id="header">
<h1>
	ARMY-ANTS
</h1>
</div>
<div id="signin">     
    <f:form action="/login" method="post" enctype="multipart/form-data">
   		<input type="image" src="resources/images/sign-in-with-twitter.png" name="twitterImage" />
    </f:form>
</div>

</body>
</html>
