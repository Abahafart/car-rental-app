package com.arch.rest;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.RestQuery;

import com.arch.inventory.Car;
import com.arch.inventory.GraphQLInventoryClient;
import com.arch.rental.Rental;
import com.arch.rental.RentalClient;
import com.arch.reservation.Reservation;
import com.arch.reservation.ReservationsRepository;

import io.quarkus.logging.Log;
import io.smallrye.graphql.client.GraphQLClient;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;

@Path("/reservations")
@Produces(MediaType.APPLICATION_JSON)
public class ReservationResource {

    private final ReservationsRepository reservationsRepository;
    private final GraphQLInventoryClient inventoryClient;
    private final RentalClient rentalClient;
    private final SecurityContext securityContext;

    public ReservationResource(ReservationsRepository reservationsRepository, @GraphQLClient("inventory") GraphQLInventoryClient inventoryClient, @RestClient RentalClient rentalClient,
        SecurityContext securityContext) {
        this.reservationsRepository = reservationsRepository;
        this.inventoryClient = inventoryClient;
        this.rentalClient = rentalClient;
      this.securityContext = securityContext;
    }

    @GET
    @Path("availability")
    public Collection<Car> availability(@RestQuery LocalDate startDate, @RestQuery LocalDate endDate) {
        List<Car> availableCars = inventoryClient.allCars();
        Map<Long, Car> carsById = new HashMap<>();
        for (Car car : availableCars) {
            carsById.put(car.id, car);
        }
        List<Reservation> reservations = reservationsRepository.findAll();
        for(Reservation reservation : reservations) {
            if (reservation.isReserved(startDate, endDate)) {
                carsById.remove(reservation.id);
            }
        }
        return carsById.values();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Reservation make(Reservation reservation) {
        Reservation result = reservationsRepository.save(reservation);
        String userId = securityContext.getUserPrincipal() != null ? securityContext.getUserPrincipal().getName() : "anonymous";
        if (reservation.startDay.equals(LocalDate.now())) {
            Rental rental = rentalClient.start(userId, result.id);
            Log.info("Successfully started rental " + rental);
        }
        return result;
    }

    @GET
    @Path("/all")
    public Collection<Reservation> allReservations() {
        String userId = securityContext.getUserPrincipal() != null ? securityContext.getUserPrincipal().getName() : null;
        return reservationsRepository.findAll().stream().filter(reservation -> userId == null
        || reservation.userId.equalsIgnoreCase(userId)).toList();
    }
}
