package com.arch.rental;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import com.arch.rental.entity.Rental;

import io.quarkus.logging.Log;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;

@Path("/rentals")
public class RentalResource {

  private final AtomicLong id = new AtomicLong(0);

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
    Optional<Rental> optionalRental = Rental.findByUserAndReservationIdsOptional(userId, reservationId);
    if (optionalRental.isPresent()) {
      Rental rental = optionalRental.get();
      rental.setActive(Boolean.FALSE);
      rental.setEndDate(LocalDate.now());
      rental.update();
      return rental;
    }
    throw new NotFoundException("Rental not found");
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

}
