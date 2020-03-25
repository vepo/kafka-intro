package io.vepo.bookstore.cart;

import static java.util.UUID.randomUUID;

/**
 * Fires an CartReserveProduct event.
 * 
 * @author Victor Osório <victor.perticarrari@gmail.com>
 *
 */
public class CartReserveProduct {

    public static CartReserveProduct from(String userId, AddProductToCart addProductToCart) {
        CartReserveProduct event = new CartReserveProduct();
        event.setId(randomUUID().toString());
        event.setUserId(userId);
        event.setProductId(addProductToCart.getProductId());
        event.setQuantity(addProductToCart.getQuantity());
        return event;
    }

    private String id;
    private String productId;
    private int quantity;
    private String userId;

    public String getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getUserId() {
        return userId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CartReserveProduct other = (CartReserveProduct) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (productId == null) {
            if (other.productId != null)
                return false;
        } else if (!productId.equals(other.productId))
            return false;
        if (quantity != other.quantity)
            return false;
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((productId == null) ? 0 : productId.hashCode());
        result = prime * result + quantity;
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "CartReserveProduct [id=" + id + ", userId=" + userId + ", productId=" + productId + ", quantity="
                + quantity + "]";
    }

}
