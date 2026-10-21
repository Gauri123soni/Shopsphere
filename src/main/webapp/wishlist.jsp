

<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>

<head>

    <title>Wishlist</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/Style.css">

</head>

<body>



<div class="container">

    <h1>My Wishlist ❤️</h1>

    <c:choose>

        <c:when test="${empty wishlist}">

            <p>
                Your wishlist is empty.
            </p>

        </c:when>

        <c:otherwise>

            <div class="product-grid">

                <c:forEach var="product"
                           items="${wishlist}">

                    <div class="product-card">

                        <h2>
                            ${product.name}
                        </h2>

                        <p>
                            ₹${product.price}
                        </p>

                        <form action="cart" method="post">
                            <input type="hidden" name="action" value="add">
                            <input type="hidden" name="productId" value="${product.productId}">
                            <input type="hidden" name="quantity" value="1">
                            <button type="submit">Add to Cart</button>
                        </form>

                        <form action="wishlist" method="post">
                            <input type="hidden" name="action" value="remove">
                            <input type="hidden" name="productId" value="${product.productId}">
                            <button type="submit">Remove</button>
                        </form>

                    </div>

                </c:forEach>

            </div>

        </c:otherwise>

    </c:choose>

</div>



</body>
</html>
