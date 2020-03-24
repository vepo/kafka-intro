package io.vepo.bookstore.cartservice;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StockReserve(@JsonProperty("cartId")String cartId, @JsonProperty("productId")String productId,
		@JsonProperty("quantity")int quantity) {

}
