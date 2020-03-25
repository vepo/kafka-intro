package io.vepo.bookstore.cartservice.infra;

import io.vepo.bookstore.cartservice.CartProductReserve;
import io.vepo.bookstore.cartservice.CartProductReserved;
import io.vepo.bookstore.cartservice.StockItemReserve;
import io.vepo.bookstore.cartservice.StockItemReserved;

public enum Topics {
    CART_PRODUCT_RESERVE("cart.product.reserve", CartProductReserve.class),
    CART_PRODUCT_RESERVED("cart.product.reserved", CartProductReserved.class),
    STOCK_ITEM_RESERVE("stock.item.reserve", StockItemReserve.class),
    STOCK_ITEM_RESERVED("stock.item.reserved", StockItemReserved.class);

    private final String topicName;
    private final Class<?> schemaClass;

    Topics(String name, Class<?> schemaClass) {
        this.topicName = name;
        this.schemaClass = schemaClass;
    }

    public String topicName() {
        return this.topicName;
    }

    public Class<?> schemaClass() {
        return schemaClass;
    }
}
