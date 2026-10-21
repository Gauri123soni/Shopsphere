package com.shopsphere.controller.admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.shopsphere.util.DatabaseConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {
            request.setAttribute("productCount", count(statement, "products"));
            request.setAttribute("categoryCount", count(statement, "categories"));
            request.setAttribute("orderCount", count(statement, "orders"));
            request.setAttribute("userCount", count(statement, "users"));
            request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
        } catch (SQLException exception) {
            throw new ServletException("Unable to load admin dashboard.", exception);
        }
    }

    private int count(Statement statement, String table) throws SQLException {
        try (ResultSet result = statement.executeQuery("SELECT COUNT(*) FROM " + table)) {
            result.next();
            return result.getInt(1);
        }
    }
}
