package io.vepo.bookstore.cartservice;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CartProductReserved(@JsonProperty("id") String id,  
                                  @JsonProperty("status") ReserveStatus status,
                                  @JsonProperty("quantity") int quantity) { }