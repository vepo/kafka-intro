package io.vepo.bookstore.cart;

import java.util.Optional;
import java.util.UUID;

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
 * @author vepo
 */
@Path("/cart")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartController {
    private final Logger logger = LoggerFactory.getLogger(CartController.class);

    // TODO: OK. This is a dumb memory database!
    @Inject
    MemoryDatabase database;

    // TODO: This usedId is hard coded, but in a product it should be read from JWT
    private static String usedId = UUID.randomUUID().toString();

    @Inject
    @Channel("reserve-product")
    Emitter<ReserveProduct> addCartEmitter;

    @PUT
    @Path("/{productId}")
    public Cart addProductToCart(@PathParam("productId") String productId, AddProductToCart body) {
        logger.info("Adding product to cart! productId={} body={}", productId, body);
        Optional<Cart> maybeCart = database.find(usedId, Cart.class);
        if (!maybeCart.isPresent()) {
            maybeCart = Optional.of(new Cart());
            database.insert(usedId, maybeCart.get());
        }
        Cart cart = maybeCart.get();
        cart.add(productId, body.getQuantity());
        logger.info("Current cart! cart={}", cart);
        addCartEmitter.send(KafkaRecord.of(usedId, ReserveProduct.from(body)));
        return cart;
    }
}