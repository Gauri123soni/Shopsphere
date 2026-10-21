package com.shopsphere.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.shopsphere.model.User;
import com.shopsphere.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        request.setAttribute("profile", user);
        request.getRequestDispatcher("/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        User user = (User) request.getSession().getAttribute("user");
        try {
            request.setAttribute("profile", userService.updateProfile(
                    user, request.getParameter("name"), request.getParameter("mobile")));
            request.setAttribute("message", "Your profile has been updated.");
            request.getRequestDispatcher("/profile.jsp").forward(request, response);
        } catch (IllegalArgumentException exception) {
            request.setAttribute("profile", user);
            request.setAttribute("error", exception.getMessage());
            request.getRequestDispatcher("/profile.jsp").forward(request, response);
        } catch (SQLException exception) {
            throw new ServletException("Unable to update your profile.", exception);
        }
    }
}
