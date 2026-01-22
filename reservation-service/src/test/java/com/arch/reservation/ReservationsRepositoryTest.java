package com.arch.reservation;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.arch.reservation.entity.Reservation;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;

@QuarkusTest
class ReservationsRepositoryTest {

  @Test
  @Transactional
  public void testCreateReservation() {
    Reservation reservation = new Reservation();
    reservation.startDay = LocalDate.now().plus(5, ChronoUnit.DAYS);
    reservation.endDay = LocalDate.now().plusDays(12L);
    reservation.carId = 384L;
    reservation.persist();
    Assertions.assertNotNull(reservation.id);
    Assertions.assertEquals(1, Reservation.count());
    Reservation persistedReservation = Reservation.findById(reservation.id);
    Assertions.assertNotNull(persistedReservation);
    Assertions.assertEquals(reservation.id, persistedReservation.id);
  }
}