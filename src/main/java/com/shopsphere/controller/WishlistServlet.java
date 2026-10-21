package com.shopsphere.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.shopsphere.model.Product;
import com.shopsphere.service.ProductService;
import com.shopsphere.util.ShoppingSession;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/wishlist")
public class WishlistServlet extends HttpServlet {
    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Set<Integer> wishlist = ShoppingSession.wishlist(request.getSession());
        List<Product> products = new ArrayList<>();
        Iterator<Integer> productIds = wishlist.iterator();
        while (productIds.hasNext()) {
            Product product = productService.getProductById(productIds.next());
            if (product == null) {
                productIds.remove();
            } else {
                products.add(product);
            }
        }
        request.setAttribute("wishlist", products);
        request.getRequestDispatcher("/wishlist.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            String action = request.getParameter("action");
            Set<Integer> wishlist = ShoppingSession.wishlist(request.getSession());
            if ("remove".equals(action)) {
                wishlist.remove(productId);
            } else if ("add".equals(action) && productService.getProductById(productId) != null) {
                wishlist.add(productId);
            }
        } catch (NumberFormatException ignored) {
            request.getSession().setAttribute("wishlistError", "Select a valid product.");
        }
        response.sendRedirect(request.getContextPath() + "/wishlist");
    }
}