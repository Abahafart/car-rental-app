package com.arch.rental;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

import io.quarkus.logging.Log;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/rentals")
public class RentalResource {

    private final AtomicLong id = new AtomicLong(0);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/start/{userId}/{reservationId}")
    public Rental start(String userId, Long reservationId) {
    Log.infof("Starting rental for %S with reservation %s", userId, reservationId);
    return new Rental(id.incrementAndGet(), userId, reservationId, LocalDate.now());
    }
}
