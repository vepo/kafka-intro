package io.vepo.bookstore.cart;

public class ReserveProduct {
    private String productId;
    private int quantity;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((productId == null) ? 0 : productId.hashCode());
        result = prime * result + quantity;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ReserveProduct other = (ReserveProduct) obj;
        if (productId == null) {
            if (other.productId != null)
                return false;
        } else if (!productId.equals(other.productId))
            return false;
        if (quantity != other.quantity)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ReserveProduct [productId=" + productId + ", quantity=" + quantity + "]";
    }

    public static ReserveProduct from(AddProductToCart addProductToCart) {
        ReserveProduct reserveProduct = new ReserveProduct();
        reserveProduct.setProductId(addProductToCart.getProductId());
        reserveProduct.setQuantity(addProductToCart.getQuantity());
        return reserveProduct;
    }
}
