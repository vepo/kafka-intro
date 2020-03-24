package io.vepo.bookstore.stockservice;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StockItemReserve(@JsonProperty("cartId")String cartId, @JsonProperty("productId")String productId,
		@JsonProperty("quantity")int quantity) {

}
