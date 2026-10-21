package com.shopsphere.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.shopsphere.model.Cartitem;
import com.shopsphere.model.Product;
import com.shopsphere.service.ProductService;
import com.shopsphere.util.ShoppingSession;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        render(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<Integer, Integer> cart = ShoppingSession.cart(request.getSession());
        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            String action = request.getParameter("action");
            if ("remove".equals(action)) {
                cart.remove(productId);
            } else {
                Product product = productService.getProductById(productId);
                if (product != null && product.getStock() > 0) {
                    int quantity = Integer.parseInt(request.getParameter("quantity"));
                    if ("update".equals(action)) {
                        if (quantity < 1) {
                            cart.remove(productId);
                        } else {
                            cart.put(productId, Math.min(quantity, product.getStock()));
                        }
                    } else if ("add".equals(action) && quantity > 0) {
                        int requested = cart.getOrDefault(productId, 0) + quantity;
                        cart.put(productId, Math.min(requested, product.getStock()));
                    }
                }
            }
        } catch (NumberFormatException ignored) {
            request.getSession().setAttribute("cartError", "Enter a valid product and quantity.");
        }
        response.sendRedirect(request.getContextPath() + "/cart");
    }

    private void render(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<Integer, Integer> cart = ShoppingSession.cart(request.getSession());
        List<Cartitem> lines = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        Iterator<Map.Entry<Integer, Integer>> entries = cart.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Integer, Integer> entry = entries.next();
            Product product = productService.getProductById(entry.getKey());
            if (product == null) {
                entries.remove();
                continue;
            }
            int quantity = Math.min(entry.getValue(), product.getStock());
            if (quantity < 1) {
                entries.remove();
                continue;
            }
            entry.setValue(quantity);
            Cartitem line = new Cartitem(product, quantity);
            lines.add(line);
            total = total.add(line.getLineTotal());
        }
        request.setAttribute("cartLines", lines);
        request.setAttribute("cartTotal", total);
        request.setAttribute("cartError", request.getSession().getAttribute("cartError"));
        request.getSession().removeAttribute("cartError");
        request.getRequestDispatcher("/cart.jsp").forward(request, response);
    }
}