/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.shopsphere.dao;

import java.sql.Connection;

final class DatabaseConnection {
    private DatabaseConnection() {
    }

    static Connection getConnection() throws java.sql.SQLException {
        return com.shopsphere.util.DatabaseConnection.getConnection();
    }
}
