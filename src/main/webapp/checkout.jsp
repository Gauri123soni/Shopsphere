
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>

<head>

    <title>Checkout - ShopSphere</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/Style.css">

</head>

<body>



<div class="form-container">

    <h1>Checkout</h1>
    <c:if test="${not empty error}">
        <p class="error"><c:out value="${error}" /></p>
    </c:if>
      

       <h3>Order Total: ₹${cartTotal}</h3>

    <form action="checkout"
          method="post">

        <h3>Delivery Address</h3>

        <label>Address</label>

        <textarea name="address"
                  required><c:out value="${param.address}" /></textarea>

        <label>City</label>

        <input type="text"
               name="city"
               value="${param.city}"
               required>

        <label>State</label>

        <input type="text"
               name="state"
               value="${param.state}"
               required>

        <label>Pincode</label>

        <input type="text"
               name="pincode"
               value="${param.pincode}"
               required>

        <h3>Payment Method</h3>

        <label>
            <input type="radio"
                   name="paymentMethod"
                   value="COD"
                   checked>
            Cash on Delivery
        </label>

        <br>

        <label>
            <input type="radio"
                   name="paymentMethod"
                   value="DEMO_CARD">
            Card (demo only; no payment is processed)
        </label>

        <br><br>

        <button type="submit">
            Place Order
        </button>

    </form>

</div>



</body>
</html>
