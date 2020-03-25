package io.vepo.bookstore.stockservice;

/**
 * A PreOrderItem holds a quantity of certain products. A
 * {@link StockItemReserve} should be only accepted if the quantity of
 * PreOrderedItems is lower than the
 * <code>number of items in Stock + the required quantity</code>.
 * 
 * @author Victor Osório <victor.perticarrari@gmail.com>
 *
 */
public record PreOrderItem(String productId, 
                           int quantity) { }
