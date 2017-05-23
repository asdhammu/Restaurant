<%@ page contentType="text/html; charset=UTF-8" %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page isELIgnored="false" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
	<title>Order Confirmation</title>
</head>
<body>
	<jsp:include page="header.jsp"/>
	<div class="container">
	<h2>List of Orders</h2>
	<c:choose>
		<c:when test="${not empty orders}">
			<table>
				<thead>
					<tr>
						<td>Order Number</td>
						<td>First Name</td>
						<td>Last Name</td>
						<td>Time</td>
						<td>Price</td>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="o"  items="${orders}">
					<tr>
						<td><a href="orderDetail?o=${o.orderId}">${o.orderId}</a> </td>
						<td>${o.userId.fName}</td>
						<td>${o.userId.lName}</td>
						<td>${o.date}</td>
						<td>${o.price}</td>
					</tr>
					</c:forEach>			
				</tbody>
			
			</table>
				
			
		</c:when>
		<c:otherwise>
		
			<p>Bad Input</p>
		</c:otherwise>
		
	</c:choose>
	</div>
	
	<jsp:include page="footer.jsp"/>
</body>
</html>
