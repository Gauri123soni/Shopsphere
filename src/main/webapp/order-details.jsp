

<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>

<head>

    <title>Order Details</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/Style.css">

</head>

<body>



<div class="container">

    <h1>
        Order #${order.orderId}
    </h1>

    <p>
        Order Status:
        <strong>${order.orderStatus}</strong>
    </p>

    <p>
        Payment Status:
        ${order.paymentStatus}
    </p>

    <h2>Products</h2>

    <table>

        <tr>
            <th>Product</th>
            <th>Price</th>
            <th>Quantity</th>
            <th>Total</th>
        </tr>

        <c:forEach var="item"
                   items="${orderItems}">

            <tr>

                <td>
                    ${item.productName}
                </td>

                <td>
                    ₹${item.price}
                </td>

                <td>
                    ${item.quantity}
                </td>

                <td>
                    ₹${item.price * item.quantity}
                </td>

            </tr>

        </c:forEach>

    </table>

    <h2>
        Total:
        ₹${order.totalAmount}
    </h2>

</div>



</body>
</html>