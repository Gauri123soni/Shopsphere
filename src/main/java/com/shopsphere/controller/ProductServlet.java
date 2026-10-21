package com.shopsphere.controller;

import java.io.IOException;

import com.shopsphere.model.Product;
import com.shopsphere.service.ProductService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/products", "/product"})
public class ProductServlet extends HttpServlet {
    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if ("/products".equals(request.getServletPath())) {
            request.setAttribute("products", productService.getAllProducts());
            request.getRequestDispatcher("/products.jsp").forward(request, response);
            return;
        }
        try {
            int productId = Integer.parseInt(request.getParameter("id"));
            Product product = productService.getProductById(productId);
            if (product == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            request.setAttribute("product", product);
            request.getRequestDispatcher("/product-details.jsp").forward(request, response);
        } catch (NumberFormatException exception) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}