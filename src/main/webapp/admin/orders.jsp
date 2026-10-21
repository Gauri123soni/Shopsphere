<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Manage Orders</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/Style.css">

</head>

<body>

<div class="admin-container">

    <h1>Manage Orders</h1>

    <table>

        <tr>

            <th>Order ID</th>
            <th>User</th>
            <th>Total</th>
            <th>Payment</th>
            <th>Status</th>
            <th>Action</th>

        </tr>

        <c:forEach var="order"
                   items="${orders}">

            <tr>

                <td>
                    #${order.orderId}
                </td>

                <td>
                    ${order.userName}
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

                    <form action="${pageContext.request.contextPath}/admin/orders"
                          method="post">

                        <input type="hidden"
                               name="orderId"
                               value="${order.orderId}">

                        <select name="status">
                            <option value="${order.orderStatus}" selected>Keep ${order.orderStatus}</option>
                            <option value="PLACED">
                                Placed
                            </option>

                            <option value="PROCESSING">
                                Processing
                            </option>

                            <option value="SHIPPED">
                                Shipped
                            </option>

                            <option value="DELIVERED">
                                Delivered
                            </option>

                            <option value="CANCELLED">
                                Cancelled
                            </option>

                        </select>

                        <button type="submit">
                            Update
                        </button>

                    </form>

                </td>

            </tr>

        </c:forEach>

    </table>

</div>

</body>
</html>