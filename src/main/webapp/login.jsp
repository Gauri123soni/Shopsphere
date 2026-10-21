

<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>

<head>

    <title>Login - ShopSphere</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/Style.css">

</head>

<body>



<div class="form-container">

    <h2>Login</h2>

   

        <c:if test="${not empty error}">
            <p class="error"><c:out value="${error}" /></p>
        </c:if>


    <form action="login" method="post">

        <label>Email</label>

        <input type="email"
               name="email"
               required>

        <label>Password</label>

        <input type="password"
               name="password"
               required>

        <button type="submit">
            Login
        </button>

    </form>

    <p>
        Don't have an account?

        <a href="${pageContext.request.contextPath}/register">
            <c:out value="Register" />
        </a>
    </p>

</div>



</body>
</html>
