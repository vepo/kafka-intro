package io.vepo.bookstore.cart;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class CartProcessor {
	private final Logger logger = LoggerFactory.getLogger(CartProcessor.class);

	@Incoming("reserved-product")
	public void process(CartProductReserved message) {
		logger.info("Receiving final event!\n\nevent={}\n\n", message);
	}
}
