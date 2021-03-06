package io.vepo.bookstore.stockservice;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Event emitted by CartService.
 * 
 * @author Victor Osório <victor.perticarrari@gmail.com>
 *
 */
public record StockItemReserve(@JsonProperty("id") String id, 
                               @JsonProperty("cartId") String cartId,
                               @JsonProperty("productId") String productId, 
                               @JsonProperty("quantity") int quantity) { }