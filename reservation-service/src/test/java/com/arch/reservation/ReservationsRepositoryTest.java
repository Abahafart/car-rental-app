package com.arch.reservation;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
class ReservationsRepositoryTest {

  @Inject
  ReservationsRepository repository;

  @Test
  public void testCreateReservation() {
    Reservation reservation = new Reservation();
    reservation.startDay = LocalDate.now().plus(5, ChronoUnit.DAYS);
    reservation.endDay = LocalDate.now().plusDays(12L);
    reservation.carId = 384L;
    repository.save(reservation);
    Assertions.assertNotNull(reservation.id);
    Assertions.assertTrue(repository.findAll().contains(reservation));
  }
}