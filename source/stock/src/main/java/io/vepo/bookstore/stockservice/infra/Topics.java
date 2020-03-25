package io.vepo.bookstore.stockservice.infra;

import io.vepo.bookstore.stockservice.StockItemReserve;
import io.vepo.bookstore.stockservice.StockItemReserved;

public enum Topics {
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
