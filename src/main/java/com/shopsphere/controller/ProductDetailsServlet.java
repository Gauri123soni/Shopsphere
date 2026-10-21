
package com.shopsphere.controller;

import com.shopsphere.model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import com.shopsphere.service.ProductService;

@WebServlet("/product-details")
public class ProductDetailsServlet
        extends HttpServlet {

    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String id =
                request.getParameter("id");

        int productId;
        try {
            productId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Product product = productService.getProductById(productId);
        if (product == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        request.setAttribute(
                "product",
                product
        );

        request.getRequestDispatcher(
                "/product-details.jsp"
        ).forward(
                request,
                response
        );
    }

}