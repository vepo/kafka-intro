package io.vepo.bookstore.cartservice;

import static java.util.Objects.isNull;

import java.util.Map;

public record Cart(String cartId, 
                   String userId, 
                   Map<String, Integer> contents) {

    public void addProduct(String productId, int quantity) {
        contents.compute(productId, (__, currQuantity) -> isNull(currQuantity) ? quantity : currQuantity + quantity);
    }
}
