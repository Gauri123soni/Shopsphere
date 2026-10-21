package com.shopsphere.controller.admin;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.shopsphere.util.DatabaseConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/products")
public class AdminProductServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(AdminProductServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        try (Connection connection = DatabaseConnection.getConnection()) {
            if ("add".equals(action)) {
                loadCategories(request, connection);
                request.getRequestDispatcher("/admin/product-form.jsp").forward(request, response);
                return;
            }
            if ("edit".equals(action)) {
                int id = requiredInt(request, "id");
                try (PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM products WHERE product_id = ?")) {
                    statement.setInt(1, id);
                    try (ResultSet result = statement.executeQuery()) {
                        if (!result.next()) {
                            response.sendError(HttpServletResponse.SC_NOT_FOUND);
                            return;
                        }
                        request.setAttribute("product", product(result));
                    }
                }
                loadCategories(request, connection);
                request.getRequestDispatcher("/admin/product-form.jsp").forward(request, response);
                return;
            }
            List<Map<String, Object>> products = new ArrayList<>();
            try (Statement statement = connection.createStatement();
                 ResultSet result = statement.executeQuery(
                         "SELECT * FROM products ORDER BY product_id DESC")) {
                while (result.next()) {
                    products.add(product(result));
                }
            }
            request.setAttribute("products", products);
            request.getRequestDispatcher("/admin/products.jsp").forward(request, response);
        } catch (SQLException | ServletException exception) {
            LOGGER.log(Level.SEVERE, "Unable to load admin products.", exception);
            throw new ServletException("Unable to load products.", exception);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        try (Connection connection = DatabaseConnection.getConnection()) {
            if ("delete".equals(action)) {
                try (PreparedStatement statement = connection.prepareStatement(
                        "UPDATE products SET status = FALSE WHERE product_id = ?")) {
                    statement.setInt(1, requiredInt(request, "productId"));
                    if (statement.executeUpdate() == 0) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }
                }
            } else if ("add".equals(action) || "update".equals(action)) {
                String name = trim(request.getParameter("name"));
                String brand = trim(request.getParameter("brand"));
                String description = trim(request.getParameter("description"));
                String imageUrl = trim(request.getParameter("imageUrl"));
                BigDecimal price = requiredDecimal(request, "price");
                BigDecimal discount = requiredDecimal(request, "discount");
                int categoryId = requiredInt(request, "categoryId");
                int stock = requiredInt(request, "stock");
                if (name.isEmpty() || price.signum() < 0 || discount.signum() < 0
                        || discount.compareTo(BigDecimal.valueOf(100)) > 0 || stock < 0) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product values.");
                    return;
                }
                if ("add".equals(action)) {
                    String sql = "INSERT INTO products (category_id, name, brand, description, price, "
                            + "discount, stock, image_url, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        bindProduct(statement, categoryId, name, brand, description, price,
                                discount, stock, imageUrl, request);
                        statement.executeUpdate();
                    }
                } else {
                    String sql = "UPDATE products SET category_id=?, name=?, brand=?, description=?, "
                            + "price=?, discount=?, stock=?, image_url=?, status=? WHERE product_id=?";
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        bindProduct(statement, categoryId, name, brand, description, price,
                                discount, stock, imageUrl, request);
                        statement.setInt(10, requiredInt(request, "productId"));
                        if (statement.executeUpdate() == 0) {
                            response.sendError(HttpServletResponse.SC_NOT_FOUND);
                            return;
                        }
                    }
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unsupported product action.");
                return;
            }
            response.sendRedirect(request.getContextPath() + "/admin/products");
        } catch (NumberFormatException | SQLException exception) {
            LOGGER.log(Level.SEVERE, "Unable to save admin product.", exception);
            throw new ServletException("Unable to save product. Check the product values and database.", exception);
        }
    }

    private void loadCategories(HttpServletRequest request, Connection connection) throws SQLException {
        List<Map<String, Object>> categories = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(
                     "SELECT category_id, name FROM categories WHERE status = TRUE ORDER BY name")) {
            while (result.next()) {
                Map<String, Object> category = new HashMap<>();
                category.put("categoryId", result.getInt("category_id"));
                category.put("name", result.getString("name"));
                categories.add(category);
            }
        }
        request.setAttribute("categories", categories);
    }

    private Map<String, Object> product(ResultSet result) throws SQLException {
        Map<String, Object> product = new HashMap<>();
        product.put("productId", result.getInt("product_id"));
        product.put("categoryId", result.getInt("category_id"));
        product.put("name", result.getString("name"));
        product.put("brand", result.getString("brand"));
        product.put("description", result.getString("description"));
        product.put("price", result.getBigDecimal("price"));
        product.put("discount", result.getBigDecimal("discount"));
        product.put("stock", result.getInt("stock"));
        product.put("imageUrl", result.getString("image_url"));
        product.put("status", result.getBoolean("status"));
        return product;
    }

    private void bindProduct(PreparedStatement statement, int categoryId, String name, String brand,
            String description, BigDecimal price, BigDecimal discount, int stock, String imageUrl,
            HttpServletRequest request) throws SQLException {
        statement.setInt(1, categoryId);
        statement.setString(2, name);
        statement.setString(3, brand);
        statement.setString(4, description);
        statement.setBigDecimal(5, price);
        statement.setBigDecimal(6, discount);
        statement.setInt(7, stock);
        statement.setString(8, imageUrl);
        statement.setBoolean(9, "true".equals(request.getParameter("status")));
    }

    private int requiredInt(HttpServletRequest request, String name) {
        return Integer.parseInt(request.getParameter(name));
    }

    private BigDecimal requiredDecimal(HttpServletRequest request, String name) {
        return new BigDecimal(request.getParameter(name));
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }
}
