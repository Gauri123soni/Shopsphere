<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Manage Categories | ShopSphere</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Style.css">
</head>
<body>
<div class="admin-container">
    <h1>Manage Categories</h1>
    <p><a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a></p>
    <c:choose>
      <c:when test="${not empty category}">
        <form action="${pageContext.request.contextPath}/admin/categories" method="post">
            <input type="hidden" name="action" value="update">
            <input type="hidden" name="categoryId" value="${category.categoryId}">
            <label for="name">Category name</label>
            <input id="name" type="text" name="name" value="${category.name}" required maxlength="100">
            <label for="description">Description</label>
            <textarea id="description" name="description" maxlength="500">${category.description}</textarea>
            <label><input type="checkbox" name="status" value="true" ${category.status ? 'checked' : ''}> Active</label>
            <button type="submit">Save Category</button>
        </form>
      </c:when>
      <c:otherwise>
        <form action="${pageContext.request.contextPath}/admin/categories" method="post">
            <input type="hidden" name="action" value="add">
            <label for="name">Category name</label>
            <input id="name" type="text" name="name" required maxlength="100">
            <label for="description">Description</label>
            <textarea id="description" name="description" maxlength="500"></textarea>
            <button type="submit">Add Category</button>
        </form>
      </c:otherwise>
    </c:choose>
    <table>
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Description</th>
            <th>Status</th>
            <th>Action</th>
        </tr>
        <c:forEach var="item" items="${categories}">
            <tr>
                <td><c:out value="${item.categoryId}" /></td>
                <td><c:out value="${item.name}" /></td>
                <td><c:out value="${item.description}" /></td>
                <td><c:out value="${item.status ? 'Active' : 'Inactive'}" /></td>
                <td>
                    <a href="${pageContext.request.contextPath}/admin/categories?edit=${item.categoryId}">Edit</a>
                    <form action="${pageContext.request.contextPath}/admin/categories" method="post" class="inline-form"
                          onsubmit="return confirm('Deactivate this category?')">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="categoryId" value="${item.categoryId}">
                        <button type="submit">Deactivate</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>