<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<nav class="navbar">
    <div class="logo">
        <a href="${pageContext.request.contextPath}/index.jsp" aria-label="ShopSphere home">
            Shop<span>Sphere</span>
        </a>
    </div>
    <div class="nav-links" aria-label="Main navigation">
        <a href="${pageContext.request.contextPath}/index.jsp">Home</a>
        <a href="${pageContext.request.contextPath}/products">Shop</a>
        <a href="${pageContext.request.contextPath}/cart">Cart</a>
        <a href="${pageContext.request.contextPath}/wishlist">Wishlist</a>
        <c:choose>
            <c:when test="${not empty sessionScope.user}">
                <a href="${pageContext.request.contextPath}/orders">Orders</a>
                <a href="${pageContext.request.contextPath}/profile">Profile</a>
                <c:if test="${sessionScope.user.role eq 'ADMIN'}">
                    <a href="${pageContext.request.contextPath}/admin/dashboard">Admin</a>
                </c:if>
                <a href="${pageContext.request.contextPath}/logout">Logout</a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/login">Log in</a>
                <a class="nav-cta" href="${pageContext.request.contextPath}/register">Create account</a>
            </c:otherwise>
        </c:choose>
    </div>
</nav>