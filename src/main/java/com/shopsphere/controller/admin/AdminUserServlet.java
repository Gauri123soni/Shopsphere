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

@WebServlet("/admin/users")
public class AdminUserServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(AdminUserServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Map<String, Object>> users = new ArrayList<>();
        String sql = "SELECT user_id, name, email, mobile, role, status, created_at "
                + "FROM users ORDER BY user_id DESC";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Map<String, Object> user = new HashMap<>();
                user.put("userId", result.getInt("user_id"));
                user.put("name", result.getString("name"));
                user.put("email", result.getString("email"));
                user.put("mobile", result.getString("mobile"));
                user.put("role", result.getString("role"));
                user.put("status", result.getBoolean("status"));
                user.put("createdAt", result.getTimestamp("created_at"));
                users.add(user);
            }
            request.setAttribute("users", users);
            request.getRequestDispatcher("/admin/users.jsp").forward(request, response);
        } catch (SQLException exception) {
            LOGGER.log(Level.SEVERE, "Unable to load admin users.", exception);
            throw new ServletException("Unable to load users.", exception);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (!"activate".equals(action) && !"deactivate".equals(action)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unsupported account action.");
            return;
        }
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE users SET status=? WHERE user_id=? AND role <> 'ADMIN'")) {
            statement.setBoolean(1, "activate".equals(action));
            statement.setInt(2, Integer.parseInt(request.getParameter("userId")));
            statement.executeUpdate();
            response.sendRedirect(request.getContextPath() + "/admin/users");
        } catch (SQLException | NumberFormatException exception) {
            LOGGER.log(Level.SEVERE, "Unable to update user account.", exception);
            throw new ServletException("Unable to update user account.", exception);
        }
    }
}
