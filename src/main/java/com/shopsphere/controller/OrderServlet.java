package com.shopsphere.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.shopsphere.dao.OrderDao;
import com.shopsphere.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/orders", "/order-details"})
public class OrderServlet extends HttpServlet {
    private final OrderDao orderDao = new OrderDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        try {
            if ("/orders".equals(request.getServletPath())) {
                request.setAttribute("orders", orderDao.getOrdersForUser(user.getUserId()));
                request.getRequestDispatcher("/orders.jsp").forward(request, response);
                return;
            }
            int orderId = Integer.parseInt(request.getParameter("id"));
            var order = orderDao.getOrderForUser(user.getUserId(), orderId);
            if (order == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            request.setAttribute("order", order);
            request.setAttribute("orderItems", orderDao.getOrderItems(orderId));
            request.getRequestDispatcher("/order-details.jsp").forward(request, response);
        } catch (NumberFormatException exception) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (SQLException exception) {
            throw new ServletException("Unable to load your orders.", exception);
        }
    }
}
