package io.vepo.bookstore.cartservice;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CartReserve(@JsonProperty("productId")String productId, @JsonProperty("quantity")int quantity) {
}
