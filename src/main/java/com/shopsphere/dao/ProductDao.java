package com.shopsphere.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shopsphere.model.Product;
import com.shopsphere.util.DatabaseConnection;

public class ProductDao {

    public List<Product> getAllProducts() {
        String sql = "SELECT * FROM products WHERE status = TRUE ORDER BY created_at DESC, product_id DESC";
        List<Product> products = new ArrayList<>();

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                products.add(mapProduct(rs));
            }
            return products;
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to load products from the database.", e);
        }
    }

    public Product getProductById(int productId) {
        String sql = "SELECT * FROM products WHERE product_id = ? AND status = TRUE";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapProduct(rs) : null;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to load product " + productId + " from the database.", e);
        }
    }

    private Product mapProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setProductId(rs.getInt("product_id"));
        product.setCategoryId(rs.getInt("category_id"));
        product.setName(rs.getString("name"));
        product.setBrand(rs.getString("brand"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getDouble("price"));
        product.setDiscount(rs.getDouble("discount"));
        product.setStock(rs.getInt("stock"));
        product.setImageUrl(rs.getString("image_url"));
        product.setStatus(rs.getBoolean("status"));
        return product;
    }
}
