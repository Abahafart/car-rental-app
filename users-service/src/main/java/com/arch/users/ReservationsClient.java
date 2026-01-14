package com.arch.users;

import java.time.LocalDate;
import java.util.Collection;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestQuery;

import com.arch.users.model.Car;
import com.arch.users.model.Reservation;

import io.quarkus.oidc.token.propagation.common.AccessToken;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@AccessToken
@Path("/reservations")
@RegisterRestClient(baseUri = "http://localhost:8081")
public interface ReservationsClient {

    @GET
    @Path("/all")
    Collection<Reservation> allReservations();

    @POST
    Reservation make(Reservation reservation);

    @GET
    @Path("/availability")
    Collection<Car> availability(@RestQuery LocalDate startDate, @RestQuery LocalDate endDate);

}
