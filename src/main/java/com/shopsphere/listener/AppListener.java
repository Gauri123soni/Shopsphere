package com.shopsphere.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppListener
        implements ServletContextListener {

    @Override
    public void contextInitialized(
            ServletContextEvent event) {

        System.out.println(
                "================================"
        );

        System.out.println(
                "ShopSphere Application Started"
        );

        System.out.println(
                "================================"
        );
    }

    @Override
    public void contextDestroyed(
            ServletContextEvent event) {

        System.out.println(
                "ShopSphere Application Stopped"
        );
    }
}
