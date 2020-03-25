package io.vepo.bookstore.cartservice;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CartProductReserve(@JsonProperty("id") String id, 
                                 @JsonProperty("userId") String userId,
                                 @JsonProperty("productId") String productId, 
                                 @JsonProperty("quantity")int quantity) { }