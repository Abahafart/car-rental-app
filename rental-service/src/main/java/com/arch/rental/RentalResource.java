package com.arch.rental;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.arch.rental.billing.InvoiceAdjust;
import com.arch.rental.entity.Rental;
import com.arch.rental.reservation.Reservation;
import com.arch.rental.reservation.ReservationClient;

import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;

@Path("/rentals")
public class RentalResource {

  public static final double STANDARD_REFUND_DATE_PER_DAY = -10.99;
  public static final double STANDARD_PRICE_FOR_PROLONGED_DAY = 25.99;

  @Inject
  @RestClient
  ReservationClient reservationClient;

  @Inject
  @Channel("invoices-adjust")
  Emitter<InvoiceAdjust> adjustmentEmitter;

  @POST
  @Path("/start/{userId}/{reservationId}")
  public Rental start(String userId, Long reservationId) {
    Log.infof("Starting rental for %S with reservation %s", userId, reservationId);
    Rental rental = new Rental(userId, reservationId, LocalDate.now(), null, true);
    rental.persist();
    return rental;
  }

  @PUT
  @Path("/end/{userId}/{reservationId}")
  public Rental end(String userId, Long reservationId) {
    Log.infof("Ending rental for %s with reservation %s", userId, reservationId);
    Rental rental = Rental.findByUserAndReservationIdsOptional(userId, reservationId)
        .orElseThrow(() -> new NotFoundException("Rental not found"));

    Reservation reservation = reservationClient.getReservation(reservationId);

    LocalDate today = LocalDate.now();
    if (!reservation.endDay.isEqual(today)) {
      Log.infof("Adjusting price for rental %s. Original reservation end day was %s.", rental, reservation.endDay);
      adjustmentEmitter.send(new InvoiceAdjust(rental.id.toString(), userId, today, computePrice(reservation.endDay, today)));
    }
    rental.setActive(Boolean.FALSE);
    rental.setEndDate(today);
    rental.update();
    return rental;
  }

  @GET
  public List<Rental> list() {
    return Rental.listAll();
  }

  @GET
  @Path("/active")
  public List<Rental> listActive() {
    return Rental.listActive();
  }

  private double computePrice(LocalDate endDate, LocalDate today) {
    return endDate.isBefore(today) ?
        ChronoUnit.DAYS.between(endDate, today) * STANDARD_PRICE_FOR_PROLONGED_DAY :
        ChronoUnit.DAYS.between(endDate, today) * STANDARD_REFUND_DATE_PER_DAY;
  }

}
