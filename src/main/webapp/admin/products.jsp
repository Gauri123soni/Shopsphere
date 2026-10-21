<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Manage Products</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/Style.css">
</head>
<body>
<div class="admin-container">
    <h1>Manage Products</h1>
    <p><a href="${pageContext.request.contextPath}/admin/products?action=add" class="btn">Add Product</a>
       <a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a></p>
    <table>
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Brand</th>
            <th>Price</th>
            <th>Stock</th>
            <th>Status</th>
            <th>Action</th>
        </tr>
        <c:forEach var="product" items="${products}">
            <tr>
                <td><c:out value="${product.productId}" /></td>
                <td><c:out value="${product.name}" /></td>
                <td><c:out value="${product.brand}" /></td>
                <td>₹<c:out value="${product.price}" /></td>
                <td><c:out value="${product.stock}" /></td>
                <td><c:out value="${product.status ? 'Active' : 'Inactive'}" /></td>
                <td>
                    <a href="${pageContext.request.contextPath}/admin/products?action=edit&amp;id=${product.productId}">Edit</a>
                    <form action="${pageContext.request.contextPath}/admin/products" method="post" class="inline-form"
                          onsubmit="return confirm('Deactivate this product?')">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="productId" value="${product.productId}">
                        <button type="submit">Deactivate</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>