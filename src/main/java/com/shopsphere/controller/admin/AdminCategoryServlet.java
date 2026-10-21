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
import java.util.logging.Level;
import java.util.logging.Logger;

import com.shopsphere.util.DatabaseConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/categories")
public class AdminCategoryServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(AdminCategoryServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String id = request.getParameter("edit");
            if (id != null) {
                try (PreparedStatement statement = connection.prepareStatement(
                        "SELECT category_id, name, description, status FROM categories WHERE category_id=?")) {
                    statement.setInt(1, Integer.parseInt(id));
                    try (ResultSet result = statement.executeQuery()) {
                        if (!result.next()) {
                            response.sendError(HttpServletResponse.SC_NOT_FOUND);
                            return;
                        }
                        Map<String, Object> category = new HashMap<>();
                        category.put("categoryId", result.getInt("category_id"));
                        category.put("name", result.getString("name"));
                        category.put("description", result.getString("description"));
                        category.put("status", result.getBoolean("status"));
                        request.setAttribute("category", category);
                    }
                }
            }
            List<Map<String, Object>> categories = new ArrayList<>();
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT category_id, name, description, status FROM categories ORDER BY category_id DESC");
                 ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    Map<String, Object> category = new HashMap<>();
                    category.put("categoryId", result.getInt("category_id"));
                    category.put("name", result.getString("name"));
                    category.put("description", result.getString("description"));
                    category.put("status", result.getBoolean("status"));
                    categories.add(category);
                }
            }
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("/admin/categories.jsp").forward(request, response);
        } catch (SQLException | NumberFormatException exception) {
            LOGGER.log(Level.SEVERE, "Unable to load admin categories.", exception);
            throw new ServletException("Unable to load categories.", exception);
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
                        "UPDATE categories SET status=FALSE WHERE category_id=?")) {
                    statement.setInt(1, Integer.parseInt(request.getParameter("categoryId")));
                    statement.executeUpdate();
                }
            } else if ("add".equals(action) || "update".equals(action)) {
                String name = request.getParameter("name") == null ? "" : request.getParameter("name").trim();
                if (name.isEmpty()) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Category name is required.");
                    return;
                }
                if ("add".equals(action)) {
                    try (PreparedStatement statement = connection.prepareStatement(
                            "INSERT INTO categories (name, description, status) VALUES (?, ?, ?)")) {
                        statement.setString(1, name);
                        statement.setString(2, request.getParameter("description"));
                        statement.setBoolean(3, true);
                        statement.executeUpdate();
                    }
                } else {
                    try (PreparedStatement statement = connection.prepareStatement(
                            "UPDATE categories SET name=?, description=?, status=? WHERE category_id=?")) {
                        statement.setString(1, name);
                        statement.setString(2, request.getParameter("description"));
                        statement.setBoolean(3, "true".equals(request.getParameter("status")));
                        statement.setInt(4, Integer.parseInt(request.getParameter("categoryId")));
                        if (statement.executeUpdate() == 0) {
                            response.sendError(HttpServletResponse.SC_NOT_FOUND);
                            return;
                        }
                    }
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unsupported category action.");
                return;
            }
            response.sendRedirect(request.getContextPath() + "/admin/categories");
        } catch (SQLException | NumberFormatException exception) {
            LOGGER.log(Level.SEVERE, "Unable to save admin category.", exception);
            throw new ServletException("Unable to save category.", exception);
        }
    }
}
