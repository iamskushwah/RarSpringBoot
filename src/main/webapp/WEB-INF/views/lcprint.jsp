<%@page import="com.wellsfargo.rarconsumer.entity.Exam"%>
<%@page
	import="com.wellsfargo.rarconsumer.response.LinecardSectionResponseDTO"%>
<%@page import="java.util.List"%>
<%@page import="org.bson.Document"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
		Document lcData=(Document)request.getAttribute("lcData");
		List<LinecardSectionResponseDTO> lcSections=(List<LinecardSectionResponseDTO>)request.getAttribute("lcSections");
		Exam lcExam=(Exam)request.getAttribute("lcExam");
%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="ISO-8859-1">
<style>
* {
	box-sizing: border-box;
}

.header {
	border: 1px solid red;
	padding: 15px;
}

.row::after {
	content: "";
	clear: both;
	display: table;
}

[class*="col-"] {
	float: left;
	padding: 15px;
	border: 1px solid red;
}

.col-1 {
	width: 8.33%;
}

.col-2 {
	width: 16.66%;
}

.col-3 {
	width: 25%;
}

.col-4 {
	width: 33.33%;
}

.col-5 {
	width: 41.66%;
}

.col-6 {
	width: 50%;
}

.col-7 {
	width: 58.33%;
}

.col-8 {
	width: 66.66%;
}

.col-9 {
	width: 75%;
}

.col-10 {
	width: 83.33%;
}

.col-11 {
	width: 91.66%;
}

.col-12 {
	width: 100%;
}
</style>
</head>
<body>
	<%%>
	<div class="row">
		<div class="col-2">
			<%
				out.print(lcExam.getProjectID());
			%>
	</div>
	<div class="col-10">
		<%
				out.print(lcExam.getExamName());
			%>
	</div>
	</div>
	<%%>
</body>
</html>