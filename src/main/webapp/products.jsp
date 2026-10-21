

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Shop the collection | ShopSphere</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Style.css">
</head>
<body>
    <jsp:include page="/fragments/navbar.jsp" />
    <main class="page-shell">
        <p class="eyebrow">Good finds, right this way</p>
        <h1 class="page-title">Shop the collection.</h1>
        <p class="page-intro">Thoughtful everyday essentials, picked just for you.</p>
        <c:choose>
            <c:when test="${not empty products}">
                <div class="product-grid">
                    <c:foreach var="product" items="${products}">
                        <article class="product-card">
                            <a class="product-image-link" href="${pageContext.request.contextPath}/product?id=${product.productId}">
                                <c:choose>
                                    <c:when test="${not empty product.imageUrl}">
                                        <img src="${pageContext.request.contextPath}${product.imageUrl}" alt="${product.name}" loading="lazy">
                                    </c:when>
                                    <c:otherwise>
                                        <img src="${pageContext.request.contextPath}/images/no-image.png" alt="${product.name}" loading="lazy">
                                    </c:otherwise>
                                </c:choose>
                            </a>
                            <div class="product-info">
                                <p class="product-brand"><c:out value="${product.brand}" /></p>
                                <h2 class="product-name">
                                    <a href="${pageContext.request.contextPath}/product?id=${product.productId}"><c:out value="${product.name}" /></a>
                                </h2>
                                <p class="product-description"><c:out value="${product.description}" /></p>
                                <div class="product-meta">
                                    <span class="product-price">₹<c:out value="${product.price * (1 - product.discount / 100)}" /></span>
                                    <c:if test="${product.discount > 0}">
                                        <span class="stock-label"><c:out value="${product.discount}" />% off</span>
                                    </c:if>
                                    <c:choose>
                                        <c:when test="${product.stock > 0}">
                                            <span class="stock-label">In stock</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="stock-label stock-label-unavailable">Sold out</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <c:if test="${product.stock > 0}">
                                    <div class="product-actions">
                                        <form action="${pageContext.request.contextPath}/cart" method="post">
                                            <input type="hidden" name="action" value="add">
                                            <input type="hidden" name="productId" value="${product.productId}">
                                            <input type="hidden" name="quantity" value="1">
                                            <button class="button button-dark" type="submit">Add to cart <span aria-hidden="true">+</span></button>
                                        </form>
                                        <a class="button button-outline" href="${pageContext.request.contextPath}/product?id=${product.productId}">Details</a>
                                    </div>
                                </c:if>
                            </div>
                        </article>
                    </c:foreach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <h2>We’re getting the shelves ready.</h2>
                    <p>There aren’t any products to show yet. Please check back soon.</p>
                </div>
            </c:otherwise>
        </c:choose>
    </main>
    <jsp:include page="/fragments/footer.jsp" />
</body>
</html>
