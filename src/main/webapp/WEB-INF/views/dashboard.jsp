<html>
<head>

<link href="resources/css/style.css" rel="stylesheet" type="text/css"/>
<link href="resources/css/dashboard.css" rel="stylesheet" type="text/css"/>
<link href="resources/css/jquery-ui.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="resources/js/jquery-ui.js"></script>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
<script>
	$(function() {
		$( "#accordion" ).accordion();
	});
	</script>

</head>
<body>
<div id="header">
<h1>
	ARMY-ANTS
</h1>
</div>
<img src="resources/images/signout_button.png" name="sign_out_button" style="float:right; clear:left">
<div>
<form action="/Army-Ants/upload" method="post" enctype="multipart/form-data">
	<input type="file" name="upload_file"/>
	<input type="submit" value="Upload!"/>
</form>
</div>
<div id="content">

<%
String abc="xyz";
for(int i=0;i<100;i++)
{

%>
<div id="item">
Name <%=abc %><br />
Title<%=abc %>
</div>	
<%
}
%>
</div>
<div id="footer">

</div>
</body>
</html>