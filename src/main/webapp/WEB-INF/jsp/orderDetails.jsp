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
	<c:choose>
		<c:when test="${not empty order}">
			
				<div class="row">	
					<div class="col-lg-4">	
						<h3>Order Details</h3>
						
						Order Id: ${order.orderId}
						
						
					</div>
					<div class="col-lg-4">
						<h3>User Details</h3>
						
						${order.userId.fName}
						${order.userId.lName} <br/>
						${order.userId.phone} <br/>
						${order.userId.address}
					</div>
					<div class="col-lg-4">
					<h4>Order Items</h4>
						<table>
						<c:forEach var="item" items="${order.items}">
							
								<tr>
									<td><h5>${item.name}</h5></td>
									
									<td>${item.quantity}</td>
									<td>${item.hotnessLevel}</td>
								</tr>
							
						</c:forEach>
						</table>
						<h4 class="price">Total Price : $ ${order.price}</h4>
					</div>
				</div>
			
		</c:when>
		<c:otherwise>
			<h2>No such order</h2>
		</c:otherwise>
	</c:choose>
	</div>
	
	<jsp:include page="footer.jsp"/>
</body>
</html>