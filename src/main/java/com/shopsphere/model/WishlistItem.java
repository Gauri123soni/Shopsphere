package com.shopsphere.model;

import java.io.Serializable;

public class WishlistItem implements Serializable {

    private int wishlistId;
    private int userId;
    private int productId;

    public WishlistItem() {
    }

    public int getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(int wishlistId) {
        this.wishlistId = wishlistId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
