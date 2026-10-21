package com.shopsphere.service;

import java.util.List;

import com.shopsphere.dao.ProductDao;
import com.shopsphere.model.Product;

public class ProductService {

    private final ProductDao productDAO;

    public ProductService() {
        productDAO = new ProductDao();
    }

    /**
     * @return
     */
    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    public Product getProductById(int id) {
        return productDAO.getProductById(id);
    }
}