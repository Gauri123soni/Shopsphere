
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>

<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Cart | ShopSphere</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/Style.css">
</head>
<body>

<main class="container">
	<h1>Your Cart</h1>
	<c:if test="${not empty cartError}">
		<p class="error"><c:out value="${cartError}" /></p>
	</c:if>
	<c:choose>
		<c:when test="${empty cartLines}">
			<p>Your cart is empty.</p>
			<a href="products" class="btn">Browse Products</a>
		</c:when>
		<c:otherwise>
			<c:forEach var="line" items="${cartLines}">
				<section class="cart-line">
					<h2><c:out value="${line.product.name}" /></h2>
					<p>Unit price: ₹${line.unitPrice}</p>
					<p>Line total: ₹${line.lineTotal}</p>
					<form action="cart" method="post">
						<input type="hidden" name="action" value="update">
						<input type="hidden" name="productId" value="${line.product.productId}">
						<label for="quantity-${line.product.productId}">Quantity</label>
						<input id="quantity-${line.product.productId}" type="number" name="quantity"
							   min="1" max="${line.product.stock}" value="${line.quantity}" required>
						<button type="submit">Update</button>
					</form>
					<form action="cart" method="post">
						<input type="hidden" name="action" value="remove">
						<input type="hidden" name="productId" value="${line.product.productId}">
						<button type="submit">Remove</button>
					</form>
				</section>
			</c:forEach>
			<h2>Subtotal: ₹${cartTotal}</h2>
			<a href="checkout" class="btn">Proceed to Checkout</a>
		</c:otherwise>
	</c:choose>
</main>
</body>
</html>
