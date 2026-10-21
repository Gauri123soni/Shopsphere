<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${empty product ? 'Add' : 'Edit'} Product | ShopSphere</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Style.css">
</head>
<body>
<main class="form-container">
    <h1>${empty product ? 'Add Product' : 'Edit Product'}</h1>
    <form action="${pageContext.request.contextPath}/admin/products" method="post">
        <input type="hidden" name="action" value="${empty product ? 'add' : 'update'}">
        <c:if test="${not empty product}">
            <input type="hidden" name="productId" value="${product.productId}">
        </c:if>
        <label for="name">Product name</label>
        <input id="name" name="name" value="${product.name}" maxlength="150" required>
        <label for="categoryId">Category</label>
        <select id="categoryId" name="categoryId" required>
            <option value="">Choose category</option>
            <c:forEach var="category" items="${categories}">
                <option value="${category.categoryId}" ${product.categoryId == category.categoryId ? 'selected' : ''}>
                    <c:out value="${category.name}" />
                </option>
            </c:forEach>
        </select>
        <label for="brand">Brand</label>
        <input id="brand" name="brand" value="${product.brand}" maxlength="100">
        <label for="description">Description</label>
        <textarea id="description" name="description">${product.description}</textarea>
        <label for="price">Price</label>
        <input id="price" type="number" name="price" min="0" step="0.01" value="${product.price}" required>
        <label for="discount">Discount (%)</label>
        <input id="discount" type="number" name="discount" min="0" max="100" step="0.01" value="${empty product ? 0 : product.discount}" required>
        <label for="stock">Stock</label>
        <input id="stock" type="number" name="stock" min="0" step="1" value="${product.stock}" required>
        <label for="imageUrl">Image path (relative to images, e.g. /images/product1.jpg)</label>
        <input id="imageUrl" name="imageUrl" value="${product.imageUrl}" maxlength="500">
        <label><input type="checkbox" name="status" value="true" ${empty product or product.status ? 'checked' : ''}> Active</label>
        <button type="submit">Save Product</button>
        <a href="${pageContext.request.contextPath}/admin/products">Cancel</a>
    </form>
</main>
</body>
</html>
