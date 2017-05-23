<%@ page contentType="text/html; charset=UTF-8" %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page isELIgnored="false" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
	<title>Cart</title>
</head>
<body>
	<jsp:include page="header.jsp"/>
	<div class="container" align="center">
	<c:choose>
		<c:when test="${not empty cart.cartItem}">
			<table>
				 <thead>
					<tr>
						<td><h4>Item Name</h4></td>
						<td><h4>Quantity</h4></td>
						<td><h4>Price</h4></td>
						<td></td>
					</tr>
				</thead>
				<c:forEach var="item" items="${cart.cartItem}">
				<tr>
					<td><h5>${item.productInfo.productName}</h5></td>
					
					<td><h5>${item.quantity}</h5></td>
					
					<td><h5>$ ${item.subTotal} <%-- ${item.productInfo.price*item.quantity} --%></h5></td>
					
					<td><a class="btn btn-default" href="delete?code=${item.productInfo.productCode}">Delete</a></td>
				</tr>
				</c:forEach>
				
				
			</table>
			<div class="totalprice">
				
				<h4>Sub Total:$ ${cart.subTotal}</h4>
				<h4>Taxes:$ ${cart.taxes}</h4>
				<h4>Total:$ ${cart.totalPrice}</h4>
			</div>
			<div>	
			<a class="btn btn-default" href="index">Continue Shopping</a>	
			
			<a class="btn btn-default" href="hotness">Choose Hotness Level</a>	
			</div>
		</c:when>
		<c:otherwise>
			There are no items in your cart
			
		</c:otherwise>
	</c:choose>
	
	</div>
	
	<jsp:include page="footer.jsp"/>
</body>
</html>