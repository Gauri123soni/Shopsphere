<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>



<html>

<head>

    <title>Manage Customers</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/Style.css">

</head>

<body>

<div class="admin-container">

    <h1>Manage Customers</h1>

    <table>

        <tr>

            <th>ID</th><th>Name</th><th>Email</th><th>Mobile</th><th>Role</th><th>Action</th>

        </tr>

        <c:forEach var="user" items="${users}">

            <tr>

                <td>
                    <c:out value="${user.userId}" />
                </td>

                <td>
                    <c:out value="${user.name}" />
                </td>

                <td>
                    <c:out value="${user.email}" />
                </td>

                <td>
                    <c:out value="${user.mobile}" />
                </td>

                <td>
                    <c:out value="${user.role}" /> — <c:out value="${user.status ? 'Active' : 'Inactive'}" />
                </td>

                <td>

                    <form action="${pageContext.request.contextPath}/admin/users" method="post">
                        <input type="hidden" name="userId" value="${user.userId}">
                        <input type="hidden" name="action" value="${user.status ? 'deactivate' : 'activate'}">
                        <button type="submit">${user.status ? 'Deactivate' : 'Activate'}</button>
                    </form>

                </td>

            </tr>

        </c:forEach>

    </table>

</div>

</body>
</html>