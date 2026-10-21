

<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>

<head>

    <title>My Profile</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/Style.css">

</head>

<body>



<div class="form-container">

    <h1>My Profile</h1>
    <c:if test="${not empty error}">
        <p class="error"><c:out value="${error}" /></p>
    </c:if>
    <c:if test="${not empty message}">
        <p class="success"><c:out value="${message}" /></p>
    </c:if>

    <form action="profile"
          method="post">

        <label>Name</label>

        <input type="text"
               name="name"
               value="${profile.name}"
               required>

        <label>Email</label>

        <input type="email"
               value="${profile.email}"
               readonly>

        <label>Mobile</label>

        <input type="text"
               name="mobile"
               value="${profile.mobile}">

        <button type="submit">
            Update Profile
        </button>

    </form>

</div>



</body>
</html>
