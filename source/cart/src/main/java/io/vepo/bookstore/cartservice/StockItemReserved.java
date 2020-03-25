package io.vepo.bookstore.cartservice;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * {@link StockItemReserve} answer emitted by StockService.
 * 
 * @author Victor Osório <victor.perticarrari@gmail.com>
 *
 */
public record StockItemReserved(@JsonProperty("id") String id, 
                                @JsonProperty("status") ReserveStatus status,
                                @JsonProperty("quantity") int quantity) { }