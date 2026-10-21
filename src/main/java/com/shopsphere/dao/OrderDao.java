package com.shopsphere.dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.shopsphere.model.Cartitem;
import com.shopsphere.model.Product;

public class OrderDao {
    public List<Map<String, Object>> getOrdersForUser(int userId) throws SQLException {
        String sql = "SELECT order_id, total_amount, payment_status, order_status, created_at "
                + "FROM orders WHERE user_id = ? ORDER BY order_id DESC";
        List<Map<String, Object>> orders = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    Map<String, Object> order = new HashMap<>();
                    order.put("orderId", result.getInt("order_id"));
                    order.put("totalAmount", result.getBigDecimal("total_amount"));
                    order.put("paymentStatus", result.getString("payment_status"));
                    order.put("orderStatus", result.getString("order_status"));
                    order.put("createdAt", result.getTimestamp("created_at"));
                    orders.add(order);
                }
            }
        }
        return orders;
    }

    public Map<String, Object> getOrderForUser(int userId, int orderId) throws SQLException {
        String sql = "SELECT order_id, total_amount, payment_status, order_status, created_at "
                + "FROM orders WHERE user_id = ? AND order_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, orderId);
            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    return null;
                }
                Map<String, Object> order = new HashMap<>();
                order.put("orderId", result.getInt("order_id"));
                order.put("totalAmount", result.getBigDecimal("total_amount"));
                order.put("paymentStatus", result.getString("payment_status"));
                order.put("orderStatus", result.getString("order_status"));
                order.put("createdAt", result.getTimestamp("created_at"));
                return order;
            }
        }
    }

    public List<Map<String, Object>> getOrderItems(int orderId) throws SQLException {
        String sql = "SELECT p.name AS product_name, oi.price, oi.quantity "
                + "FROM order_items oi JOIN products p ON p.product_id = oi.product_id "
                + "WHERE oi.order_id = ? ORDER BY oi.order_item_id";
        List<Map<String, Object>> items = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, orderId);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("productName", result.getString("product_name"));
                    item.put("price", result.getBigDecimal("price"));
                    item.put("quantity", result.getInt("quantity"));
                    items.add(item);
                }
            }
        }
        return items;
    }

    public int placeOrder(int userId, Map<Integer, Integer> cart, String address,
            String city, String state, String pincode, String paymentMethod) throws SQLException {
        if (cart.isEmpty()) {
            throw new IllegalArgumentException("Your cart is empty.");
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);
            try {
                List<Cartitem> lines = lockAndPriceCart(connection, cart);
                BigDecimal total = lines.stream()
                        .map(Cartitem::getLineTotal)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .setScale(2, RoundingMode.HALF_UP);

                int addressId = insertAddress(connection, userId, address, city, state, pincode);
                int orderId = insertOrder(connection, userId, addressId, total, paymentMethod);
                insertOrderItems(connection, orderId, lines);
                reduceStock(connection, lines);
                connection.commit();
                return orderId;
            } catch (SQLException | RuntimeException exception) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    exception.addSuppressed(rollbackException);
                }
                throw exception;
            }
        }
    }

    private List<Cartitem> lockAndPriceCart(Connection connection, Map<Integer, Integer> cart)
            throws SQLException {
        String sql = "SELECT product_id, category_id, name, brand, description, price, discount, stock, "
                + "image_url, status FROM products WHERE product_id = ? AND status = TRUE FOR UPDATE";
        List<Cartitem> lines = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
                Integer requestedQuantity = entry.getValue();
                if (requestedQuantity == null) {
                    throw new IllegalArgumentException("Cart quantities must be at least one.");
                }
                int quantity = requestedQuantity;
                if (quantity < 1) {
                    throw new IllegalArgumentException("Cart quantities must be at least one.");
                }
                statement.setInt(1, entry.getKey());
                try (ResultSet result = statement.executeQuery()) {
                    if (!result.next()) {
                        throw new IllegalArgumentException("A product in your cart is no longer available.");
                    }
                    int stock = result.getInt("stock");
                    if (quantity > stock) {
                        throw new IllegalArgumentException("There is not enough stock for " + result.getString("name") + ".");
                    }
                    Product product = new Product(
                            result.getInt("product_id"), result.getInt("category_id"), result.getString("name"),
                            result.getString("brand"), result.getString("description"), result.getDouble("price"),
                            result.getDouble("discount"), stock, result.getString("image_url"),
                            result.getBoolean("status"));
                    lines.add(new Cartitem(product, quantity));
                }
            }
        }
        return lines;
    }

    private int insertAddress(Connection connection, int userId, String address,
            String city, String state, String pincode) throws SQLException {
        String sql = "INSERT INTO addresses (user_id, address_line, city, state, pincode, address_type) "
                + "VALUES (?, ?, ?, ?, ?, 'HOME')";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, userId);
            statement.setString(2, address);
            statement.setString(3, city);
            statement.setString(4, state);
            statement.setString(5, pincode);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        throw new SQLException("Creating the delivery address did not return an ID.");
    }

    private int insertOrder(Connection connection, int userId, int addressId,
            BigDecimal total, String paymentMethod) throws SQLException {
        String sql = "INSERT INTO orders (user_id, address_id, total_amount, discount, payment_method, "
                + "payment_status, order_status) VALUES (?, ?, ?, 0, ?, 'PENDING', 'PLACED')";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, userId);
            statement.setInt(2, addressId);
            statement.setBigDecimal(3, total);
            statement.setString(4, paymentMethod);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        throw new SQLException("Creating the order did not return an ID.");
    }

    private void insertOrderItems(Connection connection, int orderId, List<Cartitem> lines)
            throws SQLException {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (Cartitem line : lines) {
                statement.setInt(1, orderId);
                statement.setInt(2, line.getProduct().getProductId());
                statement.setInt(3, line.getQuantity());
                statement.setBigDecimal(4, line.getUnitPrice());
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    private void reduceStock(Connection connection, List<Cartitem> lines) throws SQLException {
        String sql = "UPDATE products SET stock = stock - ? WHERE product_id = ? AND stock >= ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (Cartitem line : lines) {
                statement.setInt(1, line.getQuantity());
                statement.setInt(2, line.getProduct().getProductId());
                statement.setInt(3, line.getQuantity());
                if (statement.executeUpdate() != 1) {
                    throw new SQLException("Product stock changed during checkout. Please review your cart.");
                }
            }
        }
    }
}