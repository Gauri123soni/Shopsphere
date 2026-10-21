



<!doctype html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${product.name} | ShopSphere</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Style.css">
</head>
<body>
    <jsp:include page="/fragments/navbar.jsp" />
    <main class="page-shell">
        <c:choose>
            <c:when test="${not empty product}">
                <article class="product-detail">
                    <c:choose>
                        <c:when test="${not empty product.imageUrl}">
                            <img class="product-detail-image" src="${pageContext.request.contextPath}${product.imageUrl}" alt="${product.name}">
                        </c:when>
                        <c:otherwise>
                            <img class="product-detail-image" src="${pageContext.request.contextPath}/images/no-image.png" alt="${product.name}">
                        </c:otherwise>
                    </c:choose>
                    <div class="product-detail-copy">
                        <p class="eyebrow">${product.brand}</p>
                        <h1><c:out value="${product.name}" /></h1>
                        <p><c:out value="${product.description}" /></p>
                        <p class="product-price">₹<c:out value="${product.price * (1 - product.discount / 100)}" /></p>
                        <c:if test="${product.discount > 0}">
                            <p><c:out value="${product.discount}" />% off</p>
                        </c:if>
                        <p class="stock-label">Available stock: ${product.stock}</p>
                        <c:if test="${product.stock > 0}">
                            <form action="${pageContext.request.contextPath}/cart" method="post">
                                <input type="hidden" name="action" value="add">
                                <input type="hidden" name="productId" value="${product.productId}">
                                <label for="quantity">Quantity</label>
                                <input id="quantity" type="number" name="quantity" value="1" min="1" max="${product.stock}" required>
                                <button class="button button-dark" type="submit">Add to cart <span aria-hidden="true">+</span></button>
                            </form>
                            <form action="${pageContext.request.contextPath}/wishlist" method="post">
                                <input type="hidden" name="action" value="add">
                                <input type="hidden" name="productId" value="${product.productId}">
                                <button class="button button-outline" type="submit">Save to wishlist</button>
                            </form>
                        </c:if>
                        <p><a class="text-link" href="${pageContext.request.contextPath}/products">← Back to the collection</a></p>
                    </div>
                </article>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <h1>Product not found</h1>
                    <p>This product may have moved. Browse the collection to find something else.</p>
                    <a class="button button-dark" href="${pageContext.request.contextPath}/products">Browse products</a>
                </div>
            </c:otherwise>
        </c:choose>
    </main>
    <jsp:include page="/fragments/footer.jsp" />
</body>
</html>
