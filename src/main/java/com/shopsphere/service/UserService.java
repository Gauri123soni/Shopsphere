package com.shopsphere.service;

import com.shopsphere.dao.UserDao;
import com.shopsphere.model.User;
import com.shopsphere.util.PasswordUtil;
import java.sql.SQLException;
import java.util.Locale;

public class UserService {
    private final UserDao userDao;

    public UserService() {
        this(new UserDao());
    }

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public int register(String name, String email, String password, String mobile) throws SQLException {
        String normalizedName = name == null ? "" : name.trim();
        String normalizedEmail = email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
        if (normalizedName.isEmpty() || !normalizedEmail.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException("Enter a name and a valid email address.");
        }
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Password must contain at least 8 characters.");
        }
        if (userDao.findByEmail(normalizedEmail) != null) {
            throw new IllegalArgumentException("An account with this email already exists.");
        }

        User user = new User();
        user.setName(normalizedName);
        user.setEmail(normalizedEmail);
        user.setPasswordHash(PasswordUtil.hash(password));
        user.setMobile(mobile == null || mobile.isBlank() ? null : mobile.trim());
        user.setRole("CUSTOMER");
        user.setStatus(true);
        return userDao.create(user);
    }

    public User authenticate(String email, String password) throws SQLException {
        if (email == null || password == null) {
            return null;
        }
        User user = userDao.findByEmail(email.trim().toLowerCase(Locale.ROOT));
        if (user == null || !user.isStatus() || !PasswordUtil.verify(password, user.getPasswordHash())) {
            return null;
        }
        user.setPasswordHash(null);
        return user;
    }

    public User updateProfile(User user, String name, String mobile) throws SQLException {
        String normalizedName = name == null ? "" : name.trim();
        if (normalizedName.isEmpty()) {
            throw new IllegalArgumentException("Name is required.");
        }
        String normalizedMobile = mobile == null || mobile.isBlank() ? null : mobile.trim();
        if (!userDao.updateProfile(user.getUserId(), normalizedName, normalizedMobile)) {
            throw new SQLException("The user profile was not updated.");
        }
        user.setName(normalizedName);
        user.setMobile(normalizedMobile);
        return user;
    }
}
