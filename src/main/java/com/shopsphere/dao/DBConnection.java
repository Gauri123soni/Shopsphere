/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.shopsphere.dao;

import java.sql.Connection;
import java.sql.SQLException;

class DBConnection {

    public static Connection getConnection1() throws SQLException {
        return com.shopsphere.util.DatabaseConnection.getConnection();
    }

    public static Connection getConnection() throws SQLException {
        return com.shopsphere.util.DatabaseConnection.getConnection();
    }

}
