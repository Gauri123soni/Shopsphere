package com.shopsphere.util;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.http.HttpSession;

public final class ShoppingSession {
    private static final String CART_ATTRIBUTE = "shopSphereCart";
    private static final String WISHLIST_ATTRIBUTE = "shopSphereWishlist";

    private ShoppingSession() {
    }

    @SuppressWarnings("unchecked")
    public static Map<Integer, Integer> cart(HttpSession session) {
        Object value = session.getAttribute(CART_ATTRIBUTE);
        if (value instanceof Map<?, ?>) {
            return (Map<Integer, Integer>) value;
        }
        Map<Integer, Integer> cart = new LinkedHashMap<>();
        session.setAttribute(CART_ATTRIBUTE, cart);
        return cart;
    }

    @SuppressWarnings("unchecked")
    public static Set<Integer> wishlist(HttpSession session) {
        Object value = session.getAttribute(WISHLIST_ATTRIBUTE);
        if (value instanceof Set<?>) {
            return (Set<Integer>) value;
        }
        Set<Integer> wishlist = new LinkedHashSet<>();
        session.setAttribute(WISHLIST_ATTRIBUTE, wishlist);
        return wishlist;
    }
}