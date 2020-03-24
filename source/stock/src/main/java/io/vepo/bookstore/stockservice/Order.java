package io.vepo.bookstore.stockservice;

import java.util.List;

public record Order(String orderId, String cartId, List<OrderItem> items) {

}
