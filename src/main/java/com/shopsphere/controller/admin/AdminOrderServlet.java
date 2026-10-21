package com.shopsphere.controller.admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.shopsphere.util.DatabaseConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/orders")
public class AdminOrderServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(AdminOrderServlet.class.getName());
    private static final Set<String> ORDER_STATUSES =
            Set.of("PLACED", "PROCESSING", "SHIPPED", "DELIVERED", "CANCELLED");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sql = "SELECT o.order_id, u.name AS user_name, o.total_amount, o.payment_status, "
                + "o.order_status, o.created_at FROM orders o JOIN users u ON u.user_id=o.user_id "
                + "ORDER BY o.order_id DESC";
        List<Map<String, Object>> orders = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Map<String, Object> order = new HashMap<>();
                order.put("orderId", result.getInt("order_id"));
                order.put("userName", result.getString("user_name"));
                order.put("totalAmount", result.getBigDecimal("total_amount"));
                order.put("paymentStatus", result.getString("payment_status"));
                order.put("orderStatus", result.getString("order_status"));
                order.put("createdAt", result.getTimestamp("created_at"));
                orders.add(order);
            }
            request.setAttribute("orders", orders);
            request.getRequestDispatcher("/admin/orders.jsp").forward(request, response);
        } catch (SQLException exception) {
            LOGGER.log(Level.SEVERE, "Unable to load admin orders.", exception);
            throw new ServletException("Unable to load orders.", exception);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String status = request.getParameter("status");
        if (!ORDER_STATUSES.contains(status)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unsupported order status.");
            return;
        }
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE orders SET order_status=? WHERE order_id=?")) {
            statement.setString(1, status);
            statement.setInt(2, Integer.parseInt(request.getParameter("orderId")));
            if (statement.executeUpdate() == 0) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            response.sendRedirect(request.getContextPath() + "/admin/orders");
        } catch (SQLException | NumberFormatException exception) {
            LOGGER.log(Level.SEVERE, "Unable to update admin order.", exception);
            throw new ServletException("Unable to update order.", exception);
        }
    }
}
