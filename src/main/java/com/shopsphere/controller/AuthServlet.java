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
import jakarta.servlet.http.HttpSession;

@WebServlet(urlPatterns = {"/login", "/register", "/logout"})
public class AuthServlet extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        switch (request.getServletPath()) {
            case "/login" -> request.getRequestDispatcher("/login.jsp").forward(request, response);
            case "/register" -> request.getRequestDispatcher("/register.jsp").forward(request, response);
            case "/logout" -> {
                request.getSession().invalidate();
                response.sendRedirect(request.getContextPath() + "/index.jsp");
            }
            default -> response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        switch (request.getServletPath()) {
            case "/login" -> login(request, response);
            case "/register" -> register(request, response);
            default -> response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            User user = userService.authenticate(request.getParameter("email"), request.getParameter("password"));
            if (user == null) {
                request.setAttribute("error", "Email or password is incorrect.");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                return;
            }
            signIn(request, user);
            response.sendRedirect(request.getContextPath()
                    + ("ADMIN".equalsIgnoreCase(user.getRole()) ? "/admin/dashboard" : "/products"));
        } catch (SQLException exception) {
            request.setAttribute("error", "Sign in is temporarily unavailable. Check the database connection.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    private void register(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String password = request.getParameter("password");
        if (password == null || !password.equals(request.getParameter("confirmPassword"))) {
            request.setAttribute("error", "The passwords do not match.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }
        try {
            userService.register(request.getParameter("name"), request.getParameter("email"), password,
                    request.getParameter("mobile"));
            User user = userService.authenticate(request.getParameter("email"), password);
            signIn(request, user);
            response.sendRedirect(request.getContextPath()
                    + (user != null && "ADMIN".equalsIgnoreCase(user.getRole())
                            ? "/admin/dashboard" : "/products"));
        } catch (IllegalArgumentException exception) {
            request.setAttribute("error", exception.getMessage());
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        } catch (SQLException exception) {
            request.setAttribute("error", "Account creation is temporarily unavailable. Check the database connection.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }

    private void signIn(HttpServletRequest request, User user) {
        HttpSession session = request.getSession();
        request.changeSessionId();
        session.setAttribute("user", user);
    }
}