<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
	<link href="resources/css/home.css" rel="stylesheet" type="text/css"/>
	<link href="resources/css/style.css" rel="stylesheet" type="text/css"/>
	<script type="text/javascript" src="resources/js/jquery.js"></script>
	<script type="text/javascript" src="resources/js/bootstrap-collapse.js"></script>
	<script type="text/javascript" src="resources/js/modal.js"></script>
	
	<script type="text/javascript">
	$(document).ready(function(){
		$(".collapse").collapse();
	});
	
		$(".launch_modal").click(
				function(e) {
					e.preventDefault();
					if ($("#launch_modal").find(".error").length > 0)
						return false
					var $form = $(this)
					// Run Ajax 
					$.ajax({
						url : $form.attr("action"),
						data : $form.serializeArray(),
						type : "POST",
						success : function(response) {
							if (response.err) {
								$('#add2_form').find(".help-block").html(
										response.data)
								$('#add2_form').find('.control-group')
										.addClass("error")
							} else {
								$('#add2_form').find(".help-block").html(
										response.data)
							}
						}
					})
					return false
				});
	</script>
	
	    <!-- Le styles -->
    <link href="resources/assets/css/bootstrap.css" rel="stylesheet">
    <style>
      body {
        padding-top: 60px; /* 60px to make the container go all the way to the bottom of the topbar */
      }
    </style>
    <link href="resources/assets/css/bootstrap-responsive.css" rel="stylesheet">

    <!-- Le fav and touch icons -->
    <link rel="shortcut icon" href="resources/assets/ico/favicon.ico">
    <link rel="apple-touch-icon-precomposed" sizes="144x144" href="resources/assets/ico/apple-touch-icon-144-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="114x114" href="resources/assets/ico/apple-touch-icon-114-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="72x72" href="resources/assets/ico/apple-touch-icon-72-precomposed.png">
    <link rel="apple-touch-icon-precomposed" href="resources/assets/ico/apple-touch-icon-57-precomposed.png">
</head>
<body>
    <div class="navbar navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container">
          <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </a>
          <a class="brand" href="#">Army Ants</a>
          <div class="nav-collapse">
            <ul class="nav">
              <li class="active"><a href="#">Home</a></li>
              <li><a href="#about">About</a></li>
              <li><a href="#contact">Contact</a></li>
              <li><a href="logout" style="margin-left:720px"><img src="resources/images/signout_button.png"></img></a>
            </ul>
          </div><!--/.nav-collapse -->
        </div>
      </div>
    </div>
<div id="content">
<div id="uploadForm">     
<a id="launch_modal" class="btn"  href="/dashboard" style="font-size:20px;margin-top:50px;" >Go Back</a>
	
<!-- Collapse starts -->
			<div data-toggle="collapse" data-target="#demo"></div>
			<div class="accordion" id="demo"
				style="width: 80%; text-align: center; margin-left: 10%;">
				<c:if test="${not empty search_results}">
					<c:forEach items="${search_results}" var="eachapp">
						<div class="accordion-group">
							<div class="accordion-heading">
								<a class="accordion-toggle" data-toggle="collapse"
									data-parent="#accordion2" href="#collapseOne"> 
									${eachapp.name}</a>
							</div>
							<div id="collapseOne" class="accordion-body collapse in">
								<div class="accordion-inner">
									${eachapp.body} <br>
								</div>
							</div>
							<br>
							
						</div>

					</c:forEach>
				</c:if>

			</div>

			<!-- Collapse ends -->
</div>
</div>

</body>
</html>