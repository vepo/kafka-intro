package io.vepo.bookstore.cartservice;

import static java.util.Objects.isNull;
import static java.util.UUID.randomUUID;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class CartManager {
    public static final AtomicReference<CartManager> reference = new AtomicReference<>();

    public static CartManager instance() {
        return reference.updateAndGet(ref -> {
            if (isNull(ref)) {
                return new CartManager();
            }
            return ref;
        });
    }

    private Map<String, Cart> database;

    private CartManager() {
        database = new HashMap<>();
    }

    public Cart getActiveCart(String userId) {
        return database.computeIfAbsent(userId, __ -> new Cart(randomUUID().toString(), userId, new HashMap<>()));
    }

}
