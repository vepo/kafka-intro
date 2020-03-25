package io.vepo.bookstore.infra;

import io.quarkus.kafka.client.serialization.JsonbDeserializer;
import io.vepo.bookstore.cart.CartProductReserved;

public class CartProductReservedDeserializer extends JsonbDeserializer<CartProductReserved> {

	public CartProductReservedDeserializer() {
		super(CartProductReserved.class);
	}

}
