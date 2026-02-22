package com.arch.reservation;

import static io.restassured.RestAssured.given;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.awaitility.Awaitility;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.arch.billing.Invoice;
import com.arch.entity.Reservation;
import com.arch.rest.ReservationResource;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.QuarkusTestProfile;
import io.quarkus.test.junit.TestProfile;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.MediaType;

@QuarkusTest
@ApplicationScoped
@TestProfile(ReservationInvoiceProducerTest.RabbitMQTest.class)
public class ReservationInvoiceProducerTest {

  public static final class RabbitMQTest implements QuarkusTestProfile {}

  private final Map<Integer, Invoice> receivedInvoices = new HashMap<>();
  private final AtomicInteger ids = new AtomicInteger();

  @Incoming("invoices-rabbitmq")
  public void processInvoice(JsonObject json) {
    Invoice invoice = json.mapTo(Invoice.class);
    receivedInvoices.put(ids.incrementAndGet(), invoice);
  }

  @Test
  void testInvoiceProduced() {
    Reservation reservation = new Reservation();
    reservation.startDay = LocalDate.now();
    reservation.endDay = reservation.startDay;

    given().body(reservation).contentType(MediaType.APPLICATION_JSON).when()
        .post("/reservations").then().statusCode(200);

    Awaitility.await().atMost(15, TimeUnit.SECONDS).until(() -> receivedInvoices.size()==1);

    Assertions.assertEquals(1, receivedInvoices.size());
    Assertions.assertEquals(ReservationResource.STANDARD_RATE_PER_DAY, receivedInvoices.get(1).price);
  }
}
