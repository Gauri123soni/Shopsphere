<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Admin Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Style.css">
</head>
<body>

<div class="admin-container">

    <h1>ShopSphere Admin Dashboard</h1>

    <p>
        Welcome, <c:out value="${sessionScope.user.name}" />
    </p>

    <div class="dashboard">

        <div class="dashboard-card">

            <h2>Products</h2>

            <h3>${productCount}</h3>

            <a href="${pageContext.request.contextPath}/admin/products">
                Manage Products
            </a>

        </div>

        <div class="dashboard-card">

            <h2>Categories</h2>

            <h3>${categoryCount}</h3>

            <a href="${pageContext.request.contextPath}/admin/categories">
                Manage Categories
            </a>

        </div>

        <div class="dashboard-card">

            <h2>Orders</h2>

            <h3>${orderCount}</h3>

            <a href="${pageContext.request.contextPath}/admin/orders">
                Manage Orders
            </a>

        </div>

        <div class="dashboard-card">

            <h2>Customers</h2>

            <h3>${userCount}</h3>

            <a href="${pageContext.request.contextPath}/admin/users">
                View Customers
            </a>

        </div>

    </div>

</div>

</body>
</html>