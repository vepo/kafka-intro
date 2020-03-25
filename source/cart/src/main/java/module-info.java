module io.vepo.bookstore.cartservice {
    requires org.slf4j;
    requires ch.qos.logback.classic;
    requires transitive kafka.clients;
    requires transitive com.fasterxml.jackson.databind;
}