<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
	<script type="text/javascript">

	</script>
</head>
<body>
<div id="header" style="background-color:#ccc">
	Army-Ants
</div>
<p> search result: ${result}</p>
<div id="signin">     
    <f:form action="/login" method="post" enctype="multipart/form-data">
   		<input type="image" src="resources/images/sign-in-with-twitter.png" name="twitterImage" />
    </f:form>
</div>
</body>
</html>