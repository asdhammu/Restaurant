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
		<h2>Order Confirmation</h2>
		<div class="row">	
		<div class="col-lg-4">	
			<h4>User Details</h4>
			
			<table>
				<tr>
					<td>${cart.customerInfo.fName}, ${cart.customerInfo.lName}</td>		
				</tr>
				
				<tr>	
					<td>${cart.customerInfo.address}</td>
				</tr>	
				<tr>
					<td>${cart.customerInfo.phone}</td>
				</tr>
				<tr>
					<td>${cart.customerInfo.email}</td>
				</tr>	
			</table>
		</div>
		<div class="col-lg-4">
				<h4>Order Items</h4>
				<table border="1">
				<c:forEach var="item" items="${cart.cartItem}">
					
						<tr>
							<td>${item.productInfo.productName}</td>							
							<td>${item.hotnessLevel}</td>
							<td>${item.quantity}</td>
							<td>$ ${item.subTotal}</td>
						</tr>
					
				</c:forEach>
				</table>
				<h4>Sub Total : $ ${cart.subTotal}</h4>
				<h4>Tax : $ ${cart.taxes}</h4>
				<h4>Total Price : $ ${cart.totalPrice}</h4>
				
			
		</div>
		<div class="col-lg-4">
			<a style="float:right" class="btn btn-default" href="placeOrder">Place Order</a>
		</div>	
		
		</div>
	</div>
	<jsp:include page="footer.jsp"/>

</body>
</html>
