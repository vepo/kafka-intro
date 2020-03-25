package io.vepo.bookstore.cart;

import static java.util.Objects.isNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Active Cart from logged user. This Cart should have an expiration timeout to
 * release all products.
 * 
 * @author Victor Osório <victor.perticarrari@gmail.com>
 *
 */
public class Cart {

    private Map<String, Integer> reserved = new HashMap<>();

    public void add(String productId, int quantity) {
        reserved.compute(productId,
                (__, reservedProduct) -> quantity + (isNull(reservedProduct) ? 0 : reservedProduct));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Cart other = (Cart) obj;
        if (reserved == null) {
            if (other.reserved != null) {
                return false;
            }
        } else if (!reserved.equals(other.reserved)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((reserved == null) ? 0 : reserved.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Cart [reserved=" + reserved + "]";
    }

}
