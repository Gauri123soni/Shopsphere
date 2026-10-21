package com.shopsphere.listener;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

@WebListener
public class SessionListener
        implements HttpSessionListener {

    @Override
    public void sessionCreated(
            HttpSessionEvent event) {

        System.out.println(
                "User Session Created"
        );
    }

    @Override
    public void sessionDestroyed(
            HttpSessionEvent event) {

        System.out.println(
                "User Session Destroyed"
        );
    }
}
