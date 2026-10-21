

<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>

<head>

    <title>Register - ShopSphere</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/Style.css">

</head>

<body>



<div class="form-container">

    <h2>Create Account</h2>

  

        <c:if test="${not empty error}">
            <p class="error"><c:out value="${error}" /></p>
        </c:if>

    

    <form action="register" method="post">

        <label>Name</label>

        <input type="text"
               name="name"
               required>

        <label>Email</label>

        <input type="email"
               name="email"
               required>

        <label>Mobile</label>

        <input type="text"
               name="mobile">

        <label>Password</label>

        <input type="password"
               name="password"
               minlength="8"
               required>

        <label>Confirm Password</label>

        <input type="password"
               name="confirmPassword"
               required>

        <button type="submit">
            Register
        </button>

    </form>

    <p>
        Already have an account?

        <a href="${pageContext.request.contextPath}/login">
            Login
        </a>
    </p>

</div>



</body>
</html>
