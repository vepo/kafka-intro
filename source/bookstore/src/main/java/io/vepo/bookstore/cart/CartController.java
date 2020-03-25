package io.vepo.bookstore.cart;

import static java.util.UUID.randomUUID;

import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.smallrye.reactive.messaging.kafka.KafkaRecord;
import io.vepo.bookstore.infra.MemoryDatabase;

/**
 * Cart Controller. It should handle all Cart operations.
 * 
 * @author Victor Osório <victor.perticarrari@gmail.com>
 */
@Path("/cart")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartController {
	private final Logger logger = LoggerFactory.getLogger(CartController.class);

	// TODO: This userId is hard coded, but in a product it should be read from JWT
	private static String userId = randomUUID().toString();

	// TODO: OK. This is a dumb memory database!
	@Inject
	MemoryDatabase database;

	@Inject
	@Channel("reserve-product")
	Emitter<CartReserveProduct> addCartEmitter;

	@PUT
	@Path("/{productId}")
	public Cart addProductToCart(@PathParam("productId") String productId, AddProductToCart body) {
		logger.info("Adding product to cart! productId={} body={}", productId, body);
		Optional<Cart> maybeCart = database.find(userId, Cart.class);
		if (!maybeCart.isPresent()) {
			maybeCart = Optional.of(new Cart());
			database.insert(userId, maybeCart.get());
		}
		Cart cart = maybeCart.get();
		cart.add(productId, body.getQuantity());
		logger.info("Current cart! cart={}", cart);
		CartReserveProduct event = CartReserveProduct.from(userId, body);
		logger.info("Generating Start \n\nevent={}\n\n", event);
		addCartEmitter.send(KafkaRecord.of(userId, event));
		return cart;
	}
}