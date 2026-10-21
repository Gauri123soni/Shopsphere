

<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>

<head>

    <title>My Orders</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/Style.css">

</head>

<body>



<div class="container">

    <h1>My Orders</h1>

    <c:choose>

        <c:when test="${empty orders}">

            <p>
                You have not placed any orders yet.
            </p>

        </c:when>

        <c:otherwise>

            <table>

                <tr>

                    <th>Order ID</th>
                    <th>Total</th>
                    <th>Payment</th>
                    <th>Status</th>
                    <th>Date</th>
                    <th>Details</th>

                </tr>

                <c:forEach var="order"
                           items="${orders}">

                    <tr>

                        <td>
                            #${order.orderId}
                        </td>

                        <td>
                            ₹${order.totalAmount}
                        </td>

                        <td>
                            ${order.paymentStatus}
                        </td>

                        <td>
                            ${order.orderStatus}
                        </td>

                        <td>
                            ${order.createdAt}
                        </td>

                        <td>

                            <a href="order-details?id=${order.orderId}">
                                View
                            </a>

                        </td>

                    </tr>

                </c:forEach>

            </table>

        </c:otherwise>

    </c:choose>

</div>



</body>
</html>
