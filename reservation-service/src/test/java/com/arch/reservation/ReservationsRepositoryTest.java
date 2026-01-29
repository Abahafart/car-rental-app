package com.arch.reservation;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.arch.reservation.entity.Reservation;

import io.quarkus.test.hibernate.reactive.panache.TransactionalUniAsserter;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;

@QuarkusTest
class ReservationsRepositoryTest {

  @Test
  @RunOnVertxContext
  public void testCreateReservation(TransactionalUniAsserter asserter) {
    Reservation reservation = new Reservation();
    reservation.startDay = LocalDate.now().plus(5, ChronoUnit.DAYS);
    reservation.endDay = LocalDate.now().plusDays(12L);
    reservation.carId = 384L;

    asserter.<Reservation>assertThat(reservation::persist, r -> {
      Assertions.assertNotNull(r.id);
      asserter.putData("resservation.id", r.id);
    });

    asserter.assertEquals(Reservation::count, 1L);
    asserter.assertThat(() -> Reservation.<Reservation>findById(asserter.getData("reservation.id")),
        persistedReservation -> {
          Assertions.assertNotNull(persistedReservation);
          Assertions.assertEquals(reservation.carId, persistedReservation.carId);
        });
  }
}