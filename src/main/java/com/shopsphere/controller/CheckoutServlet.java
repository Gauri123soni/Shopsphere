package com.shopsphere.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.shopsphere.dao.OrderDao;
import com.shopsphere.model.Cartitem;
import com.shopsphere.model.Product;
import com.shopsphere.model.User;
import com.shopsphere.service.ProductService;
import com.shopsphere.util.ShoppingSession;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {
    private final ProductService productService = new ProductService();
    private final OrderDao orderDao = new OrderDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!requireUser(request, response)) {
            return;
        }
        Map<Integer, Integer> cart = ShoppingSession.cart(request.getSession());
        if (cart.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }
        setCartView(request, cart);
        request.getRequestDispatcher("/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!requireUser(request, response)) {
            return;
        }
        request.setCharacterEncoding("UTF-8");
        String address = trim(request.getParameter("address"));
        String city = trim(request.getParameter("city"));
        String state = trim(request.getParameter("state"));
        String pincode = trim(request.getParameter("pincode"));
        String paymentMethod = request.getParameter("paymentMethod");
        if (address.isEmpty() || city.isEmpty() || state.isEmpty() || !pincode.matches("[0-9]{4,10}")) {
            showError(request, response, "Enter a complete address and a valid postal code.");
            return;
        }
        if (!"COD".equals(paymentMethod) && !"DEMO_CARD".equals(paymentMethod)) {
            showError(request, response, "Choose a supported demo payment method.");
            return;
        }

        User user = (User) request.getSession().getAttribute("user");
        try {
            int orderId = orderDao.placeOrder(user.getUserId(), ShoppingSession.cart(request.getSession()),
                    address, city, state, pincode, paymentMethod);
            ShoppingSession.cart(request.getSession()).clear();
            request.setAttribute("orderId", orderId);
            request.getRequestDispatcher("/order-success.jsp").forward(request, response);
        } catch (IllegalArgumentException exception) {
            showError(request, response, exception.getMessage());
        } catch (SQLException exception) {
            showError(request, response, "We could not place the order. Please review your cart and try again.");
        }
    }

    private boolean requireUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getSession().getAttribute("user") != null) {
            return true;
        }
        response.sendRedirect(request.getContextPath() + "/login");
        return false;
    }

    private void showError(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {
        Map<Integer, Integer> cart = ShoppingSession.cart(request.getSession());
        if (cart.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }
        request.setAttribute("error", message);
        setCartView(request, cart);
        request.getRequestDispatcher("/checkout.jsp").forward(request, response);
    }

    private void setCartView(HttpServletRequest request, Map<Integer, Integer> cart) {
        List<Cartitem> lines = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
            Product product = productService.getProductById(entry.getKey());
            if (product != null && entry.getValue() > 0) {
                int quantity = Math.min(entry.getValue(), product.getStock());
                if (quantity > 0) {
                    Cartitem line = new Cartitem(product, quantity);
                    lines.add(line);
                    total = total.add(line.getLineTotal());
                }
            }
        }
        request.setAttribute("cartLines", lines);
        request.setAttribute("cartTotal", total);
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }
}