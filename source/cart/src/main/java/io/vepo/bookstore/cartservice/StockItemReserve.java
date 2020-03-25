package io.vepo.bookstore.cartservice;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StockItemReserve(@JsonProperty("id") String id, 
                               @JsonProperty("cartId") String cartId,
                               @JsonProperty("productId") String productId, 
                               @JsonProperty("quantity") int quantity) {

}
