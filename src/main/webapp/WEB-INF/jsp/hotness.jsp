<%@ page contentType="text/html; charset=UTF-8" %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page isELIgnored="false" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<html>
<head>
	<title>Checkout</title>
</head>
<body>
	
	<jsp:include page="header.jsp"/>
	
	<div class="container">
		<h3>Choose Hotness Level</h3>
		
		<form:form method="post" commandName="hotnessInfo">
			<table>
			<c:forEach var="o" items="${orderItems}" varStatus="varStatus">
				<tr>
				<td>${o.productInfo.productName}</td>
				
				
				
				<td><form:radiobutton id="hotnessLevel[${varStatus.index}]" path="hotnessLevel[${varStatus.index}]" value="mild"/>Mild
				<form:radiobutton id="hotnessLevel[${varStatus.index}]" path="hotnessLevel[${varStatus.index}]" value="medium"/>Medium
				<form:radiobutton id="hotnessLevel[${varStatus.index}]" path="hotnessLevel[${varStatus.index}]" value="spicy"/>Spicy
				<form:radiobutton id="hotnessLevel[${varStatus.index}]" path="hotnessLevel[${varStatus.index}]" value="verySpicy"/>Very Spicy
				</td>
				<td><form:errors id="hotnessLevel[${varStatus.index}]" path="hotnessLevel[${varStatus.index}]" class="error-message" /></td>
				
				</tr>
			</c:forEach>
			</table>
			<input class="btn btn-default" type="submit" value="Checkout">
		</form:form>
		
	</div>
	
	
	<jsp:include page="footer.jsp"/>
</body>
</html>