package com.shopsphere.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.shopsphere.model.User;
import com.shopsphere.util.DatabaseConnection;

public class UserDao {
    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT user_id, name, email, password, mobile, role, status, created_at "
                + "FROM users WHERE email = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    return null;
                }
                return new User(
                        result.getInt("user_id"),
                        result.getString("name"),
                        result.getString("email"),
                        result.getString("password"),
                        result.getString("mobile"),
                        result.getString("role"),
                        result.getBoolean("status"),
                        result.getTimestamp("created_at"));
            }
        }
    }

    public int create(User user) throws SQLException {
        String sql = "INSERT INTO users (name, email, password, mobile, role, status) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPasswordHash());
            statement.setString(4, user.getMobile());
            statement.setString(5, user.getRole());
            statement.setBoolean(6, user.isStatus());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        throw new SQLException("Creating the user did not return an ID");
    }

    public boolean updateProfile(int userId, String name, String mobile) throws SQLException {
        String sql = "UPDATE users SET name = ?, mobile = ? WHERE user_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, mobile);
            statement.setInt(3, userId);
            return statement.executeUpdate() == 1;
        }
    }
}
